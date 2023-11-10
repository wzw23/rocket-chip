// See LICENSE.Berkeley for license details.
// See LICENSE.SiFive for license details.

package freechips.rocketchip.rocket

import chisel3._
import chisel3.util._
import chisel3.{withClock,withReset}
import chisel3.internal.sourceinfo.SourceInfo
import org.chipsalliance.cde.config._
import freechips.rocketchip.diplomacy._
import freechips.rocketchip.tile._
import freechips.rocketchip.util._
import freechips.rocketchip.util.property

class VERIO(implicit p: Parameters) extends CoreBundle()(p) {

    val commit_valid = Output(UInt(NRET.W))
    val commit_prevPc = Output(UInt((NRET*xLen).W))
    val commit_currPc = Output(UInt((NRET*xLen).W))
    val commit_order = Output(UInt((NRET*10).W))
    val commit_insn = Output(UInt((NRET*32).W))
    val commit_fused = Output(UInt((NRET).W))

    val sim_halt = Output(UInt((NRET).W))

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
}
