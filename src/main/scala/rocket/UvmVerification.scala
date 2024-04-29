//wzw add this file to restructure verification interface
package freechips.rocketchip.rocket
import org.chipsalliance.cde.config.Parameters
import chisel3.util._
import chisel3._
import freechips.rocketchip.tile._
import scala.collection.mutable.ArrayBuffer

class VEROUTIO(implicit p: Parameters) extends CoreBundle()(p) {
  val commit_start = Output(UInt(NRET.W))
  val commit_valid = Output(UInt(NRET.W))
  val commit_prevPc = Output(UInt((NRET*xLen).W))
  val commit_currPc = Output(UInt((NRET*xLen).W))
  val commit_order = Output(UInt((NRET*10).W))
  val commit_insn = Output(UInt((NRET*32).W))
  val commit_fused = Output(UInt((NRET).W))

  val sim_halt = Output(UInt((NRET*2).W))

  val  trap_valid = Output(UInt((NTRAP).W))
  val  trap_pc = Output(UInt((xLen).W))
  val  trap_firstInsn = Output(UInt((NTRAP*xLen).W))

  val  reg_gpr= Output(UInt((NRET*31*xLen).W))
  val  reg_fpr= Output(UInt((NRET*32*fLen).W))
  val  reg_vpr= Output(UInt((NRET*32*vLen).W))
  val  dest_gprWr= Output(UInt((NRET).W))
  val  dest_fprWr= Output(UInt((NRET).W))
  val  dest_vprWr= Output(UInt((NRET).W))
  val  dest_idx= Output(UInt((NRET*8).W))
  val  src_vmaskRd= Output(UInt((NRET*vLen).W))
  val  src1_gprRd= Output(UInt((NRET).W))
  val  src1_fprRd= Output(UInt((NRET).W))
  val  src1_vprRd= Output(UInt((NRET).W))
  val  src1_idx= Output(UInt((NRET*8).W))
  val  src2_gprRd= Output(UInt((NRET).W))
  val  src2_fprRd= Output(UInt((NRET).W))
  val  src2_vprRd= Output(UInt((NRET).W))
  val  src2_idx= Output(UInt((NRET*8).W))
  val  src3_gprRd= Output(UInt((NRET).W))
  val  src3_fprRd= Output(UInt((NRET).W))
  val  src3_vprRd= Output(UInt((NRET).W))
  val  src3_idx= Output(UInt((NRET*8).W))

  val  csr_mstatusWr= Output(UInt((NRET*xLen).W))
  val  csr_mepcWr= Output(UInt((NRET*xLen).W))
  val  csr_mtvalWr= Output(UInt((NRET*xLen).W))
  val  csr_mtvecWr= Output(UInt((NRET*xLen).W))
  val  csr_mcauseWr= Output(UInt((NRET*xLen).W))
  val  csr_mipWr= Output(UInt((NRET*xLen).W))
  val  csr_mieWr= Output(UInt((NRET*xLen).W))
  val  csr_mscratchWr= Output(UInt((NRET*xLen).W))
  val  csr_midelegWr= Output(UInt((NRET*xLen).W))
  val  csr_medelegWr= Output(UInt((NRET*xLen).W))
  val  csr_minstretWr= Output(UInt((NRET*xLen).W))
  val  csr_sstatusWr= Output(UInt((NRET*xLen).W))
  val  csr_sepcWr= Output(UInt((NRET*xLen).W))
  val  csr_stvalWr= Output(UInt((NRET*xLen).W))
  val  csr_stvecWr= Output(UInt((NRET*xLen).W))
  val  csr_scauseWr= Output(UInt((NRET*xLen).W))
  val  csr_satpWr= Output(UInt((NRET*xLen).W))
  val  csr_sscratchWr= Output(UInt((NRET*xLen).W))
  val  csr_vtypeWr= Output(UInt((NRET*xLen).W))
  val  csr_vcsrWr= Output(UInt((NRET*xLen).W))
  val  csr_vlWr= Output(UInt((NRET*xLen).W))
  val  csr_vstartWr= Output(UInt((NRET*xLen).W))
  val  csr_mstatusRd= Output(UInt((NRET*xLen).W))
  val  csr_mepcRd= Output(UInt((NRET*xLen).W))
  val  csr_mtvalRd= Output(UInt((NRET*xLen).W))
  val  csr_mtvecRd= Output(UInt((NRET*xLen).W))
  val  csr_mcauseRd= Output(UInt((NRET*xLen).W))
  val  csr_mipRd= Output(UInt((NRET*xLen).W))
  val  csr_mieRd= Output(UInt((NRET*xLen).W))
  val  csr_mscratchRd= Output(UInt((NRET*xLen).W))
  val  csr_midelegRd= Output(UInt((NRET*xLen).W))
  val  csr_medelegRd= Output(UInt((NRET*xLen).W))
  val  csr_minstretRd= Output(UInt((NRET*xLen).W))
  val  csr_sstatusRd= Output(UInt((NRET*xLen).W))
  val  csr_sepcRd= Output(UInt((NRET*xLen).W))
  val  csr_stvalRd= Output(UInt((NRET*xLen).W))
  val  csr_stvecRd= Output(UInt((NRET*xLen).W))
  val  csr_scauseRd= Output(UInt((NRET*xLen).W))
  val  csr_satpRd= Output(UInt((NRET*xLen).W))
  val  csr_sscratchRd= Output(UInt((NRET*xLen).W))
  val  csr_vtypeRd= Output(UInt((NRET*xLen).W))
  val  csr_vcsrRd= Output(UInt((NRET*xLen).W))
  val  csr_vlRd = Output(UInt((NRET*xLen).W))
  val  csr_vstartRd = Output(UInt((NRET*xLen).W))

  val  mem_valid= Output(UInt((NRET).W))
  val  mem_addr= Output(UInt((NRET*xLen).W))
  val  mem_isStore= Output(UInt((NRET).W))
  val  mem_isLoad= Output(UInt((NRET).W))
  val  mem_isVector= Output(UInt((NRET).W))
  val  mem_maskWr= Output(UInt((NRET*xLen/8).W))
  val  mem_maskRd= Output(UInt((NRET*xLen/8).W))
  val  mem_dataWr= Output(UInt((NRET*vLen*8).W))
  val  mem_datatRd= Output(UInt((NRET*vLen*8).W))

  val  update_reg_valid = Output(UInt((NRET).W))
  val  update_reg_gpr_en= Output(UInt((NRET).W))
  val  update_reg_pc = Output(UInt((NRET*xLen).W))
  val  update_reg_rd = Output(UInt((NRET*5).W))
  val  update_reg_rfd = Output(UInt((NRET*5).W))
  val  update_reg_data = Output(UInt((NRET*xLen).W))
  //add for sfma
  val  sfma = Output(UInt((NRET).W))

}
class VERINIO(implicit p: Parameters) extends CoreBundle()(p){
  val usingABLU = usingBitManip || usingBitManipCrypto
  val aluFn = if (usingABLU) new ABLUFN else new ALUFN
  val adder_in = Input(UInt())

  val mem_datawr_in = Input(UInt())
  val mem_npc = Input(UInt())

  val wb_reg_valid  =Input(Bool())
  val wb_ctrl = Input(new IntCtrlSigs(aluFn))
  val vpu_commit_vld = Input(Bool())
  val wb_reg_pc = Input(UInt())
  val wb_reg_raw_inst= Input(UInt())
  val wb_reg_inst = Input(UInt())
  val ver_read_withoutrestrict = Input(UInt())
  val wb_xcpt = Input(UInt())
  val ver_read = Input(UInt())
  val vpu_rfdata =Input(Vec(32, UInt(128.W)))
  val fpu_ver_read = Input(UInt())

  val status = Input(UInt())
  val mepc = Input(UInt())
  val mtval = Input(UInt())
  val mtvec = Input(UInt())
  val mcause = Input(UInt())
  val mip= Input(UInt())
  val mie = Input(UInt())
  val mscratch = Input(UInt())
  val mideleg = Input(UInt())
  val medeleg = Input(UInt())
  val minstret = Input(UInt())
  val sstatus = Input(UInt())
  val sepc = Input(UInt())
  val stval = Input(UInt())
  val stvec = Input(UInt())
  val scause = Input(UInt())
  val satp = Input(UInt())
  val sscratch = Input(UInt())
  val vtype = Input(UInt())
  val vcsr = Input(UInt())
  val vl = Input(UInt())
  val vstart = Input(UInt())

  val fpu_1_wen = Input(UInt())

  val wb_set_sboard = Input(Bool())
  val wb_wen = Input(Bool())
  val sboard_waddr = Input(UInt())

  val id_set_sboard = Input(Bool())
  val id_wen = Input(Bool())
  val ibuf_pc = Input(UInt())
  val wb_dcache_miss = Input(Bool())
  val fpu_sboard_set = Input(Bool())
  val wb_valid = Input(Bool())
  val wb_waddr = Input(UInt())

  val ll_wen = Input(Bool())
  val ll_waddr = Input(UInt())
  val dmem_resp_replay = Input(Bool())
  val dmem_resp_fpu = Input(Bool())
  val dmem_resp_waddr = Input(UInt())
  val fpu_sboard_clr = Input(Bool())
  val fpu_sboard_clra = Input(UInt())
  //wzw: add for fpu ld
  val fpu_ld = Input(UInt())
}

class UvmQueueSignal (implicit p: Parameters) extends CoreBundle()(p) {
    val prePc = UInt(xLen.W)
    val currPc = UInt(xLen.W)
    val insn = UInt(xLen.W)
    val minstret = UInt(xLen.W)
}

class UvmVerification(implicit p:Parameters) extends CoreModule{
  val io = IO(new Bundle() {
    val uvm_in = new VERINIO()
    val uvm_out = new VEROUTIO()
  })
  val mem_reg_verif_mem_addr = Reg(Bits())
  val mem_reg_verif_mem_datawr =Reg(Bits())
  val wb_reg_verif_mem_addr = Reg(Bits())
  val wb_reg_verif_mem_datawr = Reg(Bits())
  val wb_npc = Reg(Bits())
  mem_reg_verif_mem_addr := io.uvm_in.adder_in
  mem_reg_verif_mem_datawr := io.uvm_in.mem_datawr_in
  wb_reg_verif_mem_addr := mem_reg_verif_mem_addr
  wb_reg_verif_mem_datawr := mem_reg_verif_mem_datawr
  wb_npc := io.uvm_in.mem_npc
  //
  class MyQueue extends Module {
    val io = IO(new Bundle {
      val in = Flipped(Decoupled(new UvmQueueSignal))
      val out = Decoupled(new UvmQueueSignal)
      val cnt = Output(UInt(4.W))
    })
    val q = Module(new Queue(new UvmQueueSignal,entries = 20))
    q.io.enq <> io.in
    io.out <> q.io.deq
    io.cnt <> q.io.count
  }
  val wb_insn ={if (usingCompressed) Cat(Mux(io.uvm_in.wb_reg_raw_inst(1, 0).andR, (io.uvm_in.wb_reg_inst) >> 16, 0.U), io.uvm_in.wb_reg_raw_inst(15, 0)) else io.uvm_in.wb_reg_inst}
  // add to store necessary vpu signal
  val q = Module(new MyQueue)
  q.io.in.bits.prePc := RegNext(io.uvm_in.wb_reg_pc)
  q.io.in.bits.currPc := RegNext(wb_npc)
  q.io.in.bits.insn := RegNext(wb_insn)
  q.io.in.bits.minstret := (io.uvm_in.minstret)
  q.io.in.valid := RegNext(io.uvm_in.wb_reg_valid & io.uvm_in.wb_ctrl.vector)
  q.io.out.ready := io.uvm_in.vpu_commit_vld
  //
  //记录上一条指令的insn 供异常处理时使用
  val trap_valid = RegEnable(io.uvm_in.wb_xcpt, 0.U, coreParams.useVerif.B)
  val commit_insn_r = RegEnable(io.uvm_out.commit_insn,0.U,io.uvm_out.commit_valid.asBool);
  io.uvm_out.commit_start := RegEnable(1.B, 0.B, coreParams.useVerif.B&(io.uvm_in.wb_reg_valid)&(io.uvm_out.commit_prevPc === "h8000_0000".U))
  io.uvm_out.commit_valid := RegEnable(((io.uvm_in.wb_reg_valid)&(~io.uvm_in.wb_ctrl.vector))||((io.uvm_in.wb_xcpt(0).asBool))||io.uvm_in.vpu_commit_vld , 0.U, coreParams.useVerif.B)
  io.uvm_out.commit_prevPc := RegEnable(Mux(q.io.out.fire,q.io.out.bits.prePc,io.uvm_in.wb_reg_pc), 0.U, coreParams.useVerif.B)
  io.uvm_out.commit_currPc := Mux((io.uvm_out.commit_insn === (0x30200073.U)),io.uvm_out.csr_mepcWr,Mux(trap_valid(0).asBool,io.uvm_out.csr_mtvecWr,RegEnable(Mux(q.io.out.fire,q.io.out.bits.currPc,wb_npc), 0.U, coreParams.useVerif.B)))
  io.uvm_out.csr_minstretWr := io.uvm_in.minstret
  io.uvm_out.commit_order := 0.U
  io.uvm_out.commit_insn := Mux(trap_valid(0).asBool,commit_insn_r,RegEnable(Mux(q.io.out.fire,q.io.out.bits.insn,wb_insn), 0.U, coreParams.useVerif.B))
  io.uvm_out.commit_fused := 0.U

  val RunPassFail = MuxCase(0.U,Seq(
    ((io.uvm_in.ver_read_withoutrestrict===(0.U)) && (io.uvm_in.wb_reg_inst === (0x6b.U(32.W)))) -> 1.U,
    ((io.uvm_in.ver_read_withoutrestrict===(8888.U)) && (io.uvm_in.wb_reg_inst === (0x6b.U(32.W)))) -> 2.U
  ))
  io.uvm_out.sim_halt := RegNext(RunPassFail,0.U)
  require(io.uvm_out.sim_halt != 3.U,"sim_halt should not be 3")

  // io.uvm_out.trap_valid := RegEnable(io.uvm_in.wb_xcpt, 0.U, coreParams.useVerif.B)
  io.uvm_out.trap_valid := 0.U
  //  io.uvm_out.trap_pc := RegEnable(wb_reg_pc,0.U,coreParams.useVerif.B)
  io.uvm_out.trap_pc := 0.U
  //  io.uvm_out.trap_firstInsn := RegEnable(io.uvm_in.evec,0.U,coreParams.useVerif.B)
  io.uvm_out.trap_firstInsn := 0.U

  //因为延迟了一个周期所以寄存器中的值是after commit
  io.uvm_out.reg_gpr := io.uvm_in.ver_read
  //fpu寄存器的值通过fpu io接口引出
  io.uvm_out.reg_fpr := io.uvm_in.fpu_ver_read
//io.uvm_out.reg_fpr := io.fpu.fpu_ver_reg.get
  //io.uvm_out.reg_vpr := io.vpu_rfdata
  io.uvm_out.reg_vpr := Cat((io.uvm_in.vpu_rfdata).reverse)


  //TODO: open later now set to 0
  /*
  io.uvm_out.dest_gprWr := RegEnable(wb_ctrl.wxd,0.U,coreParams.useVerif.B)
  io.uvm_out.dest_fprWr := RegEnable(wb_ctrl.wfd,0.U,coreParams.useVerif.B)
  verif_dest_vprWr
  io.uvm_out.dest_idx := RegEnable(wb_waddr,0.U,coreParams.useVerif.B)
  verif_src_vmaskRd
  io.uvm_out.src1_gprRd := RegEnable(wb_ctrl.rxs1,0.U,coreParams.useVerif.B)
  io.uvm_out.src1_fprRd := RegEnable(wb_ctrl.rfs1,0.U,coreParams.useVerif.B)
  verif_src1_vprRd
  io.uvm_out.src1_idx := RegEnable(wb_raddr1,0.U,coreParams.useVerif.B)//是直接向后引还是取再次译码的值 有时间的话再引
  io.uvm_out.src2_gprRd := RegEnable(wb_ctrl.rxs2,0.U,coreParams.useVerif.B)
  io.uvm_out.src2_fprRd := RegEnable(wb_ctrl.rfs2,0.U,coreParams.useVerif.B)
  verif_src2_vprRd
  io.uvm_out.src2_idx :=  RegEnable(wb_reg_inst(24,20),0.U,coreParams.useVerif.B)
  io.uvm_out.src3_gprRd := false.B
  io.uvm_out.src3_fprRd := false.B
  verif_src3_vprRd
  verif_src3_idx
*/
  io.uvm_out.dest_gprWr := 0.U
  io.uvm_out.dest_fprWr := 0.U
  io.uvm_out.dest_vprWr := 0.U
  io.uvm_out.dest_idx := 0.U
  io.uvm_out.src_vmaskRd := 0.U
  io.uvm_out.src1_gprRd := 0.U
  io.uvm_out.src1_fprRd := 0.U
  io.uvm_out.src1_vprRd := 0.U
  io.uvm_out.src1_idx := 0.U
  io.uvm_out.src2_gprRd := 0.U
  io.uvm_out.src2_fprRd := 0.U
  io.uvm_out.src2_vprRd := 0.U
  io.uvm_out.src2_idx := 0.U
  io.uvm_out.src3_gprRd := 0.U
  io.uvm_out.src3_fprRd := 0.U
  io.uvm_out.src3_vprRd := 0.U
  io.uvm_out.src3_idx := 0.U

  io.uvm_out.csr_mstatusWr := io.uvm_in.status.asUInt
  io.uvm_out.csr_mepcWr := io.uvm_in.mepc
  io.uvm_out.csr_mtvalWr := io.uvm_in.mtval
  io.uvm_out.csr_mtvecWr := io.uvm_in.mtvec
  io.uvm_out.csr_mcauseWr := io.uvm_in.mcause
  io.uvm_out.csr_mipWr := io.uvm_in.mip
  io.uvm_out.csr_mieWr := io.uvm_in.mie
  io.uvm_out.csr_mscratchWr := io.uvm_in.mscratch
  io.uvm_out.csr_midelegWr := io.uvm_in.mideleg
  io.uvm_out.csr_medelegWr := io.uvm_in.medeleg
  io.uvm_out.csr_sstatusWr := io.uvm_in.sstatus.asUInt
  io.uvm_out.csr_sepcWr := io.uvm_in.sepc
  io.uvm_out.csr_stvalWr := io.uvm_in.stval
  io.uvm_out.csr_stvecWr := io.uvm_in.stvec
  io.uvm_out.csr_scauseWr := io.uvm_in.scause
  io.uvm_out.csr_satpWr := io.uvm_in.satp
  io.uvm_out.csr_sscratchWr := io.uvm_in.sscratch
  //先将vtype vcsr vstart设置为0
  io.uvm_out.csr_vtypeWr := io.uvm_in.vtype
  //io.uvm_out.csr_vtypeWr := 0.U
  io.uvm_out.csr_vcsrWr := io.uvm_in.vcsr
  //io.uvm_out.csr_vcsrWr := 0.U
  io.uvm_out.csr_vlWr := io.uvm_in.vl
  io.uvm_out.csr_vstartWr := io.uvm_in.vstart
  //io.uvm_out.csr_vstartWr := 0.U
  //wzw: add for sfma
  io.uvm_out.sfma := RegNext(RegNext(RegNext(io.uvm_in.fpu_1_wen))) | RegNext(io.uvm_in.fpu_ld)

  //TODO: open later now set to 0
  /*
    io.uvm_out.csr_mstatusRd := RegEnable(io.uvm_in.status.asUInt,0.U,coreParams.useVerif.B)
    io.uvm_out.csr_mepcRd := RegEnable(io.uvm_in.mepc.get ,0.U,coreParams.useVerif.B)
    io.uvm_out.csr_mtvalRd := RegEnable(io.uvm_in.mtval.get,0.U,coreParams.useVerif.B)
    io.uvm_out.csr_mtvecRd := RegEnable(io.uvm_in.mtvec.get,0.U,coreParams.useVerif.B)
    io.uvm_out.csr_mcauseRd := RegEnable(io.uvm_in.mcause.get,0.U,coreParams.useVerif.B)
    io.uvm_out.csr_mipRd := RegEnable(io.uvm_in.mip.get,0.U,coreParams.useVerif.B)
    io.uvm_out.csr_mieRd := RegEnable(io.uvm_in.mie.get,0.U,coreParams.useVerif.B)
    io.uvm_out.csr_mscratchRd := RegEnable(io.uvm_in.mscratch.get,0.U,coreParams.useVerif.B)
    io.uvm_out.csr_midelegRd := RegEnable(io.uvm_in.mideleg.get,0.U,coreParams.useVerif.B)
    io.uvm_out.csr_medelegRd := RegEnable(io.uvm_in.medeleg.get,0.U,coreParams.useVerif.B)
    io.uvm_out.csr_minstretRd := RegEnable(io.uvm_in.minstret.get,0.U,coreParams.useVerif.B)
    io.uvm_out.csr_sstatusRd := RegEnable(io.uvm_in.sstatus.get.asUInt,0.U,coreParams.useVerif.B)
    io.uvm_out.csr_sepcRd := RegEnable(io.uvm_in.sepc.get,0.U,coreParams.useVerif.B)
    io.uvm_out.csr_stvalRd := RegEnable(io.uvm_in.stval.get,0.U,coreParams.useVerif.B)
    io.uvm_out.csr_stvecRd := RegEnable(io.uvm_in.stvec.get,0.U,coreParams.useVerif.B)
    io.uvm_out.csr_scauseRd := RegEnable(io.uvm_in.scause.get,0.U,coreParams.useVerif.B)
    io.uvm_out.csr_satpRd := RegEnable(io.uvm_in.satp.get,0.U,coreParams.useVerif.B)
    io.uvm_out.csr_sscratchRd := RegEnable(io.uvm_in.sscratch.get,0.U,coreParams.useVerif.B)
    io.uvm_out.csr_vtypeRd := RegEnable(io.uvm_in.vtype.get,0.U,coreParams.useVerif.B)
    io.uvm_out.csr_vcsrRd := RegEnable(io.uvm_in.vcsr.get,0.U,coreParams.useVerif.B)
    io.uvm_out.csr_vlRd := RegEnable(io.uvm_in.vl.get,0.U,coreParams.useVerif.B)
    io.uvm_out.csr_vstartRd := RegEnable(io.uvm_in.vstart.get,0.U,coreParams.useVerif.B)
  */
  io.uvm_out.csr_mstatusRd := 0.U
  io.uvm_out.csr_mepcRd := 0.U
  io.uvm_out.csr_mtvalRd := 0.U
  io.uvm_out.csr_mtvecRd := 0.U
  io.uvm_out.csr_mcauseRd := 0.U
  io.uvm_out.csr_mipRd := 0.U
  io.uvm_out.csr_mieRd := 0.U
  io.uvm_out.csr_mscratchRd := 0.U
  io.uvm_out.csr_midelegRd := 0.U
  io.uvm_out.csr_medelegRd := 0.U
  io.uvm_out.csr_minstretRd := 0.U
  io.uvm_out.csr_sstatusRd := 0.U
  io.uvm_out.csr_sepcRd := 0.U
  io.uvm_out.csr_stvalRd := 0.U
  io.uvm_out.csr_stvecRd := 0.U
  io.uvm_out.csr_scauseRd := 0.U
  io.uvm_out.csr_satpRd := 0.U
  io.uvm_out.csr_sscratchRd := 0.U
  io.uvm_out.csr_vtypeRd := 0.U
  io.uvm_out.csr_vcsrRd := 0.U
  io.uvm_out.csr_vlRd := 0.U
  io.uvm_out.csr_vstartRd := 0.U

  //TODO: open later now set to 0
  /*
  io.uvm_out.mem_valid := RegEnable(((wb_ctrl.mem_cmd === M_XRD)||(wb_ctrl.mem_cmd === M_XWR))&(wb_valid),0.U,coreParams.useVerif.B) //TODO:vector在这需要进行判断加一个或条件
  io.uvm_out.mem_addr := RegEnable(wb_reg_verif_mem_addr.get,0.U,coreParams.useVerif.B)
  io.uvm_out.mem_isStore := RegEnable((wb_ctrl.mem_cmd === M_XRD),0.U,coreParams.useVerif.B)
  io.uvm_out.mem_isLoad  := RegEnable((wb_ctrl.mem_cmd === M_XWR),0.U,coreParams.useVerif.B)
  io.uvm_out.mem_isVector := RegEnable((wb_ctrl.vector===true.B),0.U,coreParams.useVerif.B)
  val delay_wb_reg_mem_size = RegEnable(1.U<<(1.U<<wb_reg_mem_size)-1.U,0.U,coreParams.useVerif.B)
  io.uvm_out.mem_maskWr := delay_wb_reg_mem_size
  io.uvm_out.mem_maskRd := delay_wb_reg_mem_size
  io.uvm_out.mem_dataWr := RegEnable(wb_reg_verif_mem_datawr.get,0.U,coreParams.useVerif.B)
  io.uvm_out.mem_datatRd := RegEnable(io.dmem.resp.bits.data(xLen-1, 0),0.U,coreParams.useVerif.B)
  */
  io.uvm_out.mem_valid := 0.U
  io.uvm_out.mem_addr := 0.U
  io.uvm_out.mem_isStore := 0.U
  io.uvm_out.mem_isLoad := 0.U
  io.uvm_out.mem_isVector := 0.U
  io.uvm_out.mem_maskWr := 0.U
  io.uvm_out.mem_maskRd := 0.U
  io.uvm_out.mem_dataWr := 0.U
  io.uvm_out.mem_datatRd := 0.U
  //wzw:添加pc寄存器组，解决乱序写回验证问题
  ////
  class RegFile_pc(n: Int, w: Int, zero: Boolean = false) {
    val rf = Mem(n, UInt(w.W))

    private def access(addr: UInt) = rf(~addr(log2Up(n) - 1, 0))

    private val reads = ArrayBuffer[(UInt, UInt)]()
    //  private var canRead = true

    def read(addr: UInt) = {
      //   require(canRead)
      reads += addr -> Wire(UInt())
      reads.last._2 := Mux(zero.B && addr === 0.U, 0.U, access(addr))
      reads.last._2
    }

    def write(addr: UInt, data: UInt) = {
      //  canRead = false
      when(addr =/= 0.U) {
        access(addr) := data
        for ((raddr, rdata) <- reads)
          when(addr === raddr) {
            rdata := data
          }
      }

    }}
  val lgNXRegs = if (coreParams.useRVE) 4 else 5
  val regAddrMask = (1 << lgNXRegs) - 1
  ////
  val verif_reg_gpr_pc = new RegFile_pc(regAddrMask, xLen)
  val verif_reg_fpr_pc = new RegFile_pc(regAddrMask, xLen)
  when((io.uvm_in.wb_set_sboard) && (io.uvm_in.wb_wen)) {
    verif_reg_gpr_pc.write(io.uvm_in.sboard_waddr, io.uvm_in.wb_reg_pc);
  }.elsewhen(io.uvm_in.id_set_sboard & io.uvm_in.id_wen) {
    verif_reg_fpr_pc.write(io.uvm_in.sboard_waddr, io.uvm_in.ibuf_pc)
  }.elsewhen((io.uvm_in.wb_dcache_miss && io.uvm_in.wb_ctrl.wfd || io.uvm_in.fpu_sboard_set) && io.uvm_in.wb_valid) {
    verif_reg_fpr_pc.write(io.uvm_in.wb_waddr, io.uvm_in.wb_reg_pc)
  }

  io.uvm_out.update_reg_valid := RegEnable((io.uvm_in.ll_wen || (io.uvm_in.dmem_resp_replay && io.uvm_in.dmem_resp_fpu) || io.uvm_in.fpu_sboard_clr)&(~(io.uvm_in.ll_wen&((io.uvm_in.wb_set_sboard && io.uvm_in.wb_wen)||(io.uvm_in.id_set_sboard & io.uvm_in.id_wen)))&(~(((io.uvm_in.wb_dcache_miss && io.uvm_in.wb_ctrl.wfd || io.uvm_in.fpu_sboard_set) && io.uvm_in.wb_valid)&(io.uvm_in.dmem_resp_replay && io.uvm_in.dmem_resp_fpu)))&(~((io.uvm_in.wb_dcache_miss && io.uvm_in.wb_ctrl.wfd || io.uvm_in.fpu_sboard_set)&(io.uvm_in.fpu_sboard_clr)))), 0.U, coreParams.useVerif.B)
  io.uvm_out.update_reg_gpr_en := RegEnable(io.uvm_in.ll_wen, 0.U, coreParams.useVerif.B)
  io.uvm_out.update_reg_pc := RegEnable(Mux(io.uvm_in.ll_wen, verif_reg_gpr_pc.read(io.uvm_in.ll_waddr),
    Mux((io.uvm_in.dmem_resp_replay) && (io.uvm_in.dmem_resp_fpu), verif_reg_fpr_pc.read(io.uvm_in.dmem_resp_waddr),
      Mux(io.uvm_in.fpu_sboard_clr, verif_reg_fpr_pc.read(io.uvm_in.fpu_sboard_clra),
        0.U))), 0.U, coreParams.useVerif.B)
  io.uvm_out.update_reg_rd := RegEnable(io.uvm_in.ll_waddr, 0.U, coreParams.useVerif.B)
  io.uvm_out.update_reg_rfd := RegEnable(Mux(io.uvm_in.dmem_resp_replay && io.uvm_in.dmem_resp_fpu, io.uvm_in.dmem_resp_waddr, io.uvm_in.fpu_sboard_clra), 0.U, coreParams.useVerif.B)
  //io.uvm_out.update_reg_data := Mux(ll_wen,ll_wdata,)
  val gpr_index_begin = (64.U * io.uvm_out.update_reg_rd).asUInt
  //io.uvm_out.update_reg_data := Mux(ll_wen,(io.uvm_out.reg_gpr>>gpr_index_begin)(63:0),io.uvm_out.reg_fpr((63.U*(io.uvm_out.update_reg_rfd+1.U)).asUInt,(64.U*io.uvm_out.update_reg_rfd).asUInt))
  val reg_fpr_alone = Wire(Vec(32,UInt(64.W)))
  for(i<-0 until(32)){
    reg_fpr_alone(i):= io.uvm_out.reg_fpr((i+1)*64-1,i*64)
  }
  val reg_gpr_alone = Wire(Vec(32, UInt(64.W)))
  reg_gpr_alone(0):= 0.U
  for(i<-1 until(32)){
    reg_gpr_alone(i) := io.uvm_out.reg_gpr(i*64 - 1, (i-1) * 64)
  }
  io.uvm_out.update_reg_data := Mux(io.uvm_out.update_reg_gpr_en.asBool,reg_gpr_alone(io.uvm_out.update_reg_rd),reg_fpr_alone(io.uvm_out.update_reg_rfd))


}