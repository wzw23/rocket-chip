#ifndef WZQ_DIFF_H
#define WZQ_DIFF_H
void wzw_difftest_init(const char *fname);
int wzw_difftest_diff_and_exec(uint64_t dut_arr[154]);
void wzw_difftest_set_syn(uint64_t arr[154]);
extern "C" void inchi_difftest_exec(void);
#endif
