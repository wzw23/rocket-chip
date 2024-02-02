// See LICENSE.SiFive for license details.

package freechips.rocketchip.system

import chisel3._
import chisel3.util.experimental.BoringUtils
import org.chipsalliance.cde.config.Parameters
import freechips.rocketchip.devices.debug.Debug
import freechips.rocketchip.diplomacy.LazyModule
import freechips.rocketchip.util.AsyncResetReg
//wzw add for my test
import freechips.rocketchip._

class VEROUTTOPIO(implicit p: Parameters) extends Bundle(){
  val NRET = 1
  val xLen = 64
  val fLen = 64
  val vLen = 128
  val NTRAP = 1
  //定义NRET=1 xLen=64 fLen=64 vLen=128 NTRAP=1
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
class TestHarness()(implicit p: Parameters)extends Module {
  val open_emu: Boolean = false
  val io = IO(new Bundle {
    val success = Output(Bool())
    val uvm_out = if (open_emu) Some(new VEROUTTOPIO()) else None
    val close_debug = if (open_emu) Some(Input(Bool())) else None
  })
  //wzw add for verification
  val uvm_out_top_v = WireDefault(0.U.asTypeOf(new VEROUTTOPIO()));
  if(open_emu)
  io.uvm_out.get := uvm_out_top_v

  BoringUtils.addSink(uvm_out_top_v,"wzw_uvm_out")
  dontTouch(uvm_out_top_v)
  val ldut = LazyModule(new ExampleRocketSystem)
  val dut = Module(ldut.module)
  //将此uvm_out接口与其子模块的uvm_out接口相连
  //io.uvm_out <> ldut.tile_prci_domains(0).tile_reset_domain.
//  close_debug_use := io.close_debug
  if(open_emu){
  BoringUtils.addSource(io.close_debug.get,"wzw_close_debug")
  }
  else{
  val message_pass = WireDefault(0.B)
  BoringUtils.addSource(message_pass,"wzw_close_debug")
  }
  // Allow the debug ndreset to reset the dut, but not until the initial reset has completed
  dut.reset := (reset.asBool | ldut.debug.map { debug => AsyncResetReg(debug.ndreset) }.getOrElse(false.B)).asBool

  dut.dontTouchPorts()
  dut.tieOffInterrupts()
  SimAXIMem.connectMem(ldut)
  SimAXIMem.connectMMIO(ldut)
  ldut.l2_frontend_bus_axi4.foreach( a => {
    a.ar.valid := false.B
    a.ar.bits := DontCare
    a.aw.valid := false.B
    a.aw.bits := DontCare
    a.w.valid := false.B
    a.w.bits := DontCare
    a.r.ready := false.B
    a.b.ready := false.B
  })
  //ldut.l2_frontend_bus_axi4.foreach(_.tieoff)
  Debug.connectDebug(ldut.debug, ldut.resetctrl, ldut.psd, clock, reset.asBool, io.success)
}
