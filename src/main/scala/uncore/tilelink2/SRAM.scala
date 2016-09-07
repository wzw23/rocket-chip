// See LICENSE for license details.

package uncore.tilelink2

import Chisel._

class TLRAM(address: AddressSet, beatBytes: Int = 4) extends LazyModule
{
  val node = TLManagerNode(beatBytes, TLManagerParameters(
    address            = List(address),
    regionType         = RegionType.UNCACHED,
    supportsGet        = TransferSizes(1, beatBytes),
    supportsPutPartial = TransferSizes(1, beatBytes),
    supportsPutFull    = TransferSizes(1, beatBytes),
    fifoId             = Some(0))) // requests are handled in order

  // We require the address range to include an entire beat (for the write mask)
  require ((address.mask & (beatBytes-1)) == beatBytes-1)

  lazy val module = new LazyModuleImp(this) {
    val io = new Bundle {
      val in = node.bundleIn
    }

    def bigBits(x: BigInt, tail: List[Boolean] = List.empty[Boolean]): List[Boolean] =
      if (x == 0) tail.reverse else bigBits(x >> 1, ((x & 1) == 1) :: tail)
    val mask = bigBits(address.mask >> log2Ceil(beatBytes))

    val in = io.in(0)
    val addrBits = (mask zip in.a.bits.addr_hi.toBools).filter(_._1).map(_._2)
    val memAddress = Cat(addrBits.reverse)
    val mem = SeqMem(1 << addrBits.size, Vec(beatBytes, Bits(width = 8)))

    val d_full = RegInit(Bool(false))
    val d_read = Reg(Bool())
    val d_size = Reg(UInt())
    val d_source = Reg(UInt())
    val d_addr = Reg(UInt())
    val d_data = Wire(UInt())

    // Flow control
    when (in.d.fire()) { d_full := Bool(false) }
    when (in.a.fire()) { d_full := Bool(true)  }
    in.d.valid := d_full
    in.a.ready := in.d.ready || !d_full

    val edge = node.edgesIn(0)
    in.d.bits := edge.AccessAck(d_addr, UInt(0), d_source, d_size)
    // avoid data-bus Mux
    in.d.bits.data := d_data
    in.d.bits.opcode := Mux(d_read, TLMessages.AccessAckData, TLMessages.AccessAck)

    val read = in.a.bits.opcode === TLMessages.Get
    val rdata = Wire(Vec(beatBytes, Bits(width = 8)))
    val wdata = Vec.tabulate(beatBytes) { i => in.a.bits.data(8*(i+1)-1, 8*i) }
    d_data := Cat(rdata.reverse)
    when (in.a.fire()) {
      d_read   := read
      d_size   := in.a.bits.size
      d_source := in.a.bits.source
      d_addr   := edge.addr_lo(in.a.bits)
      when (read) {
        rdata := mem.read(memAddress)
      } .otherwise {
        mem.write(memAddress, wdata, in.a.bits.mask.toBools)
      }
    }

    // Tie off unused channels
    in.b.valid := Bool(false)
    in.c.ready := Bool(true)
    in.e.ready := Bool(true)
  }
}