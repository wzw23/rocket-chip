#include "wzw_diff.h"
#include <stdio.h>
#include <stdint.h>
//wzw add for my verification env
//struct diff_context_t {
//  int gpr[32];
//  int fpr[32];
//  __int128 vpr[32];
//  uint64_t priv;
//  uint64_t mstatus;
//  uint64_t sstatus;
//  uint64_t mepc;
//  uint64_t sepc;
//  uint64_t mtval;
//  uint64_t stval;
//  uint64_t mtvec;
//  uint64_t stvec;
//  uint64_t mcause;
//  uint64_t scause;
//  uint64_t satp;
//  uint64_t mip;
//  uint64_t mie;
//  uint64_t mscratch;
//  uint64_t sscratch;
//  uint64_t mideleg;
//  uint64_t medeleg;
//  uint64_t pc;
//  uint64_t pre_pc;
//  uint64_t minstret;
//  uint64_t vtype;
//  uint64_t vcsr;
//  uint64_t vl;
//  uint64_t vstart;
//  uint64_t instruction;
//  };

    //wzw add here
//    static int spike_memcpy=0;
//    static int spike_init=0;
//    static int open_difftest=0;
////    diff_context_t spike_context_t;
////    diff_context_t dut_context;
//    static int count=1;
//    if(count==1){
//
//    printf("finish getmemory");
//    count++;
//    }
//            inchi_difftest_get_reg(arr);
//            inchi_difftest_exec();
//            printf("pc=%lx\n",arr[7]);
//            inchi_difftest_get_reg(spike_context_t);
//            printf("spike_context_t.pc = %x\n",spike_context_t.pc);
//    if((spike_init == 0)&&(spike_memcpy == 0)){
//        spike_memcpy = 1;
//        printf("memcpy success\n");
//        while(1){
//            inchi_difftest_exec();
//            inchi_difftest_get_reg(spike_context_t);
//            printf("spike_context_t.pc = %x\n",spike_context_t.pc);
//            if(spike_context_t.pc == 0x80000000){
//                printf("spike init finish\n");
//                break;
//        }
//        spike_init=1;
//        }
//        }
//        if(tile->io_uvm_out_commit_prePc == 0x80000000 && tile->io_uvm_out_commit_valid){
//            dut_context.instruction = tile->io_uvm_out_commit_insn;
//            dut_context.vstart = tile->io_uvm_out_csr_vstartWr;
//            dut_context.vl = tile->io_io_uvm_out_csr_vlWr;
//            dut_context.vcsr = tile->io_uvm_out_csr_vcsrWr;
//            dut_context.vtype = tile->io_uvm_out_csr_vtypeWr;
//            dut_context.minstret = tile->io_uvm_out_csr_minstretWr;
//            dut_context.pre_pc = tile->io_uvm_out_commit_prevPc;
//            dut_context.pc = tile->io_uvm_out_commit_currPc;
//            dut_context.medeleg = tile->io_uvm_out_csr_medelegWr;
//            dut_context.mideleg = tile->io_uvm_out_csr_midelegWr;
//            dut_context.sscratch = tile->io_uvm_out_csr_sscratchWr;
//            dut_context.mscratch = tile->io_uvm_out_csr_mscratchWr;
//            dut_context.mie = tile->io_uvm_out_csr_mieWr;
//            dut_context.mip = tile->io_uvm_out_csr_mipWr;
//            dut_context.satp = tile->io_uvm_out_csr_satpWr;
//            dut_context.scause = tile->io_uvm_out_csr_scauseWr;
//            dut_context.mcause = tile->io_uvm_out_csr_mcauseWr;
//            dut_context.stvec = tile->io_uvm_out_csr_stvecWr;
//            dut_context.mtvec = tile->io_uvm_out_csr_mtvecWr;
//            dut_context.stval = tile->io_uvm_out_csr_stvalWr;
//            dut_context.mtval = tile->io_uvm_out_csr_mtvalWr;
//            dut_context.sepc = tile->io_uvm_out_csr_sepcWr;
//            dut_context.mepc = tile->io_uvm_out_csr_mepcWr;
//            dut_context.sstatus = tile->io_uvm_out_csr_sstatusWr;
//            dut_context.mstatus = tile->io_uvm_out_csr_mstatusWr;
////            dut_context.priv = tile->io_uvm_out_csr_privWr;
//            for(int i=0;i<32;i++){
//                dut_context.gpr[i] = tile->io_uvm_out_commit_gprWr[i];
//                dut_context.fpr[i] = tile->io_uvm_out_commit_fprWr[i];
//                dut_context.vpr[i] = tile->io_uvm_out_commit_vprWr[i];
//            }
//            inchi_difftest_set_reg(dut_context);
//            open_difftest =1;
//        }
//
//        if(open_difftest == 1 && tile->io_uvm_out_commit_valid){
//            inchi_difftest_set_reg(spike_context_t);
//            bool compare_success =
//            (
//            spike_context.instruction == tile->io_uvm_out_commit_instructionWr&&
//            spike_context.vstart == tile->io_uvm_out_commit_vstartWr&&
//            spike_context.vl == tile->io_io_uvm_out_commit_vlWr&&
//            spike_context.vcsr == tile->io_uvm_out_commit_vcsrWr&&
//            spike_context.vtype == tile->io_uvm_out_commit_vtypeWr&&
//            spike_context.minstret == tile->io_uvm_out_csr_minstretWr&&
//            spike_context.pre_pc == tile->io_uvm_out_commit_prevPc&&
//            spike_context.pc == tile->io_uvm_out_commit_currPc&&
//            spike_context.medeleg == tile->io_uvm_out_csr_medelegWr&&
//            spike_context.mideleg == tile->io_uvm_out_csr_midelegWr&&
//            spike_context.sscratch == tile->io_uvm_out_csr_sscratchWr&&
//            spike_context.mscratch == tile->io_uvm_out_csr_mscratchWr&&
//            spike_context.mie == tile->io_uvm_out_csr_mieWr&&
//            spike_context.mip == tile->io_uvm_out_csr_mipWr&&
//            spike_context.satp == tile->io_uvm_out_csr_satpWr&&
//            spike_context.scause == tile->io_uvm_out_csr_scauseWr&&
//            spike_context.mcause == tile->io_uvm_out_csr_mcauseWr&&
//            spike_context.stvec == tile->io_uvm_out_csr_stvecWr&&
//            spike_context.mtvec == tile->io_uvm_out_csr_mtvecWr&&
//            spike_context.stval == tile->io_uvm_out_csr_stvalWr&&
//            spike_context.mtval == tile->io_uvm_out_csr_mtvalWr&&
//            spike_context.sepc == tile->io_uvm_out_csr_sepcWr&&
//            spike_context.mepc == tile->io_uvm_out_csr_mepcWr&&
//            spike_context.sstatus == tile->io_uvm_out_csr_sstatusWr&&
//            spike_context.mstatus == tile->io_uvm_out_csr_mstatusWr&&
////            spike_context.priv == tile->io_uvm_out_csr_privWr&&
//            for(int i=0;i<32;i++){
//                dut_context.gpr[i] == tile->io_uvm_out_commit_gprWr[i]&&
//                dut_context.fpr[i] == tile->io_uvm_out_commit_fprWr[i]&&
//                dut_context.vpr[i] == tile->io_uvm_out_commit_vprWr[i]&&
//            })
//            if(compare_success == false){
//                printf("difftest error");
//                break;
//            }
//            inchi_difftest_exec();
//        }

extern "C" void inchi_difftest_init(void);
extern "C" void inchi_difftest_memcpy(const char *fname);
extern "C" void inchi_difftest_exec(void);
extern "C" void inchi_difftest_set_reg(uint64_t arr[154]);
extern "C" void inchi_difftest_get_reg(uint64_t arr[154]);

void wzw_difftest_init(const char *fname) {
  static uint64_t spike_arr[154];
  inchi_difftest_init();
  inchi_difftest_memcpy(fname);
  while(1){
      inchi_difftest_exec();
      inchi_difftest_get_reg(spike_arr);
      if(spike_arr[6] == 0x80000000){
          printf("spike init finish\n");
          break;
      }
  }
}
void wzw_difftest_set_syn(uint64_t arr[154]) {
    inchi_difftest_set_reg(arr);
}
int wzw_difftest_diff_and_exec(uint64_t dut_arr[154]) {
  static uint64_t spike_arr[154];
  static int function_return=0;
  inchi_difftest_get_reg(spike_arr);
//  printf("minstret=%lx\n",spike_arr[5]);
  inchi_difftest_exec();
  dut_arr[25]=0;
  spike_arr[25]=0;
//  printf("dut_pc=%lx\n",dut_arr[6]);
//  printf("spike_pc=%lx\n",spike_arr[6]);
  function_return=0;
  for(int i=0;i<154;i++){
    if(spike_arr[i]!=dut_arr[i]){
      fprintf(stderr,"dut_arr[%d]=%lx\n",i,dut_arr[i]);
      fprintf(stderr,"spike_arr[%d]=%lx\n",i,spike_arr[i]);
      function_return = 1;
    }
    return function_return;
  }
}
