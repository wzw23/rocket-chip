// See LICENSE.SiFive for license details.

package freechips.rocketchip.system

import chisel3._
import chisel3.util.experimental.BoringUtils
import org.chipsalliance.cde.config.Parameters
import freechips.rocketchip.devices.debug.Debug
import freechips.rocketchip.diplomacy.LazyModule
import freechips.rocketchip.util.AsyncResetReg
import freechips.rocketchip.subsystem.OpenEmu
import freechips.rocketchip.emu.VEROUTTOPIO

class TestHarness()(implicit p: Parameters)extends Module {
  val open_emu = p(OpenEmu).get
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
  //close_debug_use := io.close_debug
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
