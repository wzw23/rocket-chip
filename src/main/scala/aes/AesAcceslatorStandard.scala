package freechips.rocketchip.tile

import chisel3._
import chisel3.util._
import chisel3.util.HasBlackBoxResource
import org.chipsalliance.cde.config._
import freechips.rocketchip.rocket._




class AesAccStandard(opcodes: OpcodeSet)(implicit p: Parameters) extends LazyRoCC(opcodes) {
  override lazy val module = new AesAccStandModuleImp(this)
}

class AesAccStandModuleImp(outer: AesAccStandard)(implicit p: Parameters) extends LazyRoCCModuleImp(outer)
  with HasCoreParameters {
  val funct = io.cmd.bits.inst.funct
  val doExKey = funct === 0.U
  val doDecipher = funct === 1.U
  val doEncipher = funct === 2.U
  val doSd = funct === 3.U


  val rs1_b = RegInit(0.U(xLen.W))


  val Aes = Module(new Aes_standard())

  val s_idle :: s_b_w_ready ::s_w_ready ::s_b_store:: s_store :: Nil = Enum(5)

  val state = RegInit(s_idle)
  io.busy := (state =/= s_idle)
  io.interrupt := false.B
  io.cmd.ready := (state === s_idle)

  //idle->w_ready
  //变量rs2_rs1用于拼接rs1和rs2,即拼接完成后是128bit的密钥或明文组
  val regfile = Mem(2,UInt(xLen.W))
  val dv_b = RegInit(false.B)
  val dprv_b = RegInit(3.U)
  val rs2_rs1 = Cat(io.cmd.bits.rs2, io.cmd.bits.rs1)
  //将rs1和rs2与128bit的0拼接 因为接口长度为256bit
  val rs2_rs1_exp = Cat(rs2_rs1,0.U(128.W))
  val issue_init = WireDefault(false.B)
  val issue_next = WireDefault(false.B)
  val issue_encdec = WireDefault(doEncipher)
  val issue_key = WireDefault(rs2_rs1_exp)
  val issue_keylen = WireDefault(false.B)
  val issue_block = WireDefault(rs2_rs1)

  val doExKey_b = RegInit(doExKey)
  val doDecipher_b = RegInit(doDecipher)
  val doEncipher_b = RegInit(doEncipher)
  val rs2_rs1_exp_b = RegInit(rs2_rs1_exp)


  Aes.io.init := issue_init
  Aes.io.next := issue_next
  Aes.io.encdec := doEncipher_b
  Aes.io.key := rs2_rs1_exp_b
  Aes.io.keylen := false.B
  Aes.io.block := rs2_rs1_exp_b(255,128)

  //s_b_store->s_store
  // MEMORY REQUEST INTERFACE
  val addend = rs1_b
  val count = RegInit(0.U(2.W))
  assert(count=/=3.U,"count should not be 3")

  io.mem.req.valid := state === s_b_store
  io.mem.req.bits.addr := addend + (count << 3.U)
  io.mem.req.bits.tag := 0.U
  io.mem.req.bits.cmd := M_XWR // perform a load (M_XWR for stores)
  io.mem.req.bits.size := log2Ceil(8).U
  io.mem.req.bits.signed := false.B
  io.mem.req.bits.data := Mux(count === 1.U, regfile(0),regfile(1))
  io.mem.req.bits.phys := false.B
  io.mem.req.bits.dprv := dprv_b
  io.mem.req.bits.dv := dv_b

  switch(state){
    is(s_idle) {
      when(io.cmd.fire()) {
        when(doExKey||doDecipher||doEncipher) {
          state := s_b_w_ready
          doEncipher_b := doEncipher
          doDecipher_b := doDecipher
          doExKey_b := doExKey
          rs2_rs1_exp_b := rs2_rs1_exp
//          issue_init := doExKey
//          issue_next := doEncipher || doDecipher
          dv_b := io.cmd.bits.status.dv
          dprv_b := io.cmd.bits.status.dprv
        }.otherwise{
          state := s_b_store
          rs1_b := io.cmd.bits.rs1
        }
      }
    }
    is(s_b_w_ready){
      state := s_w_ready
      issue_init := doExKey_b
      issue_next := doEncipher_b || doDecipher_b
    }
    is(s_w_ready){
      when(Aes.io.ready){
        state := s_idle;
        when(doDecipher_b || doEncipher_b) {
          //截取result的高64位
          regfile(0) := Aes.io.result(63,0);
          regfile(1) := Aes.io.result(127,64);
        }
      }
    }
    is(s_b_store){
      when(io.mem.req.fire){
        state := s_store
        count := count + 1.U
      }
    }
    is(s_store){
      when(io.mem.resp.fire()){
        when(count === 2.U){
          state := s_idle
          count := 0.U
        }.elsewhen(count === 1.U){
          state := s_b_store
        }
      }
    }

  }

}
class aes_core_standard extends BlackBox with HasBlackBoxResource{
  val io = IO(new Bundle{
    val clk = Input(Clock())
    val reset_n = Input(Reset())
    val encdec = Input(Bool())
    val init = Input(Bool())
    val next = Input(Bool())
    val ready = Output(Bool())

    val key = Input(UInt(256.W))
    val keylen = Input(Bool())

    val block = Input(UInt(128.W))
    val result = Output(UInt(128.W))
    val result_valid = Output(Bool())
  })

  addResource("/vsrc/aes_core_standard.v")
  addResource("/vsrc/aes_decipher_block.v")
  addResource("/vsrc/aes_encipher_block.v")
  addResource("/vsrc/aes_inv_sbox.v")
  addResource("/vsrc/aes_key_mem.v")
  addResource("/vsrc/aes_sbox.v")
}

class Aes_standard extends Module{
  val io = IO(new Bundle {
    val encdec = Input(Bool())
    val init = Input(Bool())
    val next = Input(Bool())
    val ready = Output(Bool())

    val key = Input(UInt(256.W))
    val keylen = Input(Bool())

    val block = Input(UInt(128.W))
    val result = Output(UInt(128.W))
    val result_valid = Output(Bool())
  })
  val aes_core_inst = Module(new aes_core_standard)
  aes_core_inst.io.clk := clock
  aes_core_inst.io.reset_n := ~(reset.asBool())
  aes_core_inst.io.encdec := io.encdec
  aes_core_inst.io.init := io.init
  aes_core_inst.io.next := io.next
  aes_core_inst.io.key := io.key
  aes_core_inst.io.keylen := io.keylen
  aes_core_inst.io.block := io.block
  io.result := aes_core_inst.io.result
  io.result_valid := aes_core_inst.io.result_valid
  io.ready := aes_core_inst.io.ready
}
