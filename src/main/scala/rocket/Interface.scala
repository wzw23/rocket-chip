package freechips.rocketchip.rocket

import chisel3._
import org.chipsalliance.cde.config.Parameters
import freechips.rocketchip.tile._

class DecodeIO(implicit p: Parameters) extends CoreBundle()(p) {
  val instr = Output(Bits(32.W))
  val rs1 = Output(Bits(xLen.W))
  val rs2 = Output(Bits(xLen.W))
  val valid = Output(Bool())
  val vl = Output(Bits(8.W))
  val vstart = Output(Bits(8.W))
  val vma = Output(Bits(1.W))
  val vta = Output(Bits(1.W))
  val vsew = Output(Bits(3.W))
  val vlmul = Output(Bits(3.W))
  val stall_decode = Output(Bool())
  val ready = Input(Bool())
  //
  val lsu_resp_valid = Output(Bits(1.W))
  val lsu_resp_excp = Output(Bits(1.W))
  val id_resp_data = Output(Bits(xLen.W))
}
