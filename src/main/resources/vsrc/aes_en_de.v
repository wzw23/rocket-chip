
module aes_en_de(
                          input wire            en_de,
                          input wire            last,

                          input wire [127 : 0]  block,
                          input wire [127 : 0]  round_key,
                          output wire [127 : 0] new_block, 
                          output wire [31:0] sbox_out1,
		                      input wire [31:0] sbox_in1,
		                      output wire [31:0] sbox_out2,
		                      input wire [31:0] sbox_in2,
		                      output wire [31:0] sbox_out3,
		                      input wire [31:0] sbox_in3,
		                      output wire [31:0] sbox_out4,
		                      input wire [31:0] sbox_in4
                          );
  //----------------------------------------------------------------
  // Round functions with sub functions.
  //----------------------------------------------------------------
  function [7 : 0] gm2(input [7 : 0] op);
    begin
      gm2 = {op[6 : 0], 1'b0} ^ (8'h1b & {8{op[7]}});
    end
  endfunction // gm2

  function [7 : 0] gm3(input [7 : 0] op);
    begin
      gm3 = gm2(op) ^ op;
    end
  endfunction // gm3

  function [31 : 0] mixw(input [31 : 0] w);
    reg [7 : 0] b0, b1, b2, b3;
    reg [7 : 0] mb0, mb1, mb2, mb3;
    begin
      b0 = w[31 : 24];
      b1 = w[23 : 16];
      b2 = w[15 : 08];
      b3 = w[07 : 00];

      mb0 = gm2(b0) ^ gm3(b1) ^ b2      ^ b3;
      mb1 = b0      ^ gm2(b1) ^ gm3(b2) ^ b3;
      mb2 = b0      ^ b1      ^ gm2(b2) ^ gm3(b3);
      mb3 = gm3(b0) ^ b1      ^ b2      ^ gm2(b3);

      mixw = {mb0, mb1, mb2, mb3};
    end
  endfunction // mixw

  function [127 : 0] mixcolumns(input [127 : 0] data);
    reg [31 : 0] w0, w1, w2, w3;
    reg [31 : 0] ws0, ws1, ws2, ws3;
    begin
      w0 = data[127 : 096];
      w1 = data[095 : 064];
      w2 = data[063 : 032];
      w3 = data[031 : 000];

      ws0 = mixw(w0);
      ws1 = mixw(w1);
      ws2 = mixw(w2);
      ws3 = mixw(w3);

      mixcolumns = {ws0, ws1, ws2, ws3};
    end
  endfunction // mixcolumns

  function [127 : 0] shiftrows(input [127 : 0] data);
    reg [31 : 0] w0, w1, w2, w3;
    reg [31 : 0] ws0, ws1, ws2, ws3;
    begin
      w0 = data[127 : 096];
      w1 = data[095 : 064];
      w2 = data[063 : 032];
      w3 = data[031 : 000];

      ws0 = {w0[31 : 24], w1[23 : 16], w2[15 : 08], w3[07 : 00]};
      ws1 = {w1[31 : 24], w2[23 : 16], w3[15 : 08], w0[07 : 00]};
      ws2 = {w2[31 : 24], w3[23 : 16], w0[15 : 08], w1[07 : 00]};
      ws3 = {w3[31 : 24], w0[23 : 16], w1[15 : 08], w2[07 : 00]};

      shiftrows = {ws0, ws1, ws2, ws3};
    end
  endfunction // shiftrows

  function [127 : 0] addroundkey(input [127 : 0] data, input [127 : 0] rkey);
    begin
      addroundkey = data ^ rkey;
    end
  endfunction // addroundkey

  //----------------------------------------------------------------
  // Gaolis multiplication functions for Inverse MixColumn.
  // //----------------------------------------------------------------
  // function [7 : 0] gm2(input [7 : 0] op);
  //   begin
  //     gm2 = {op[6 : 0], 1'b0} ^ (8'h1b & {8{op[7]}});
  //   end
  // endfunction // gm2

  // function [7 : 0] gm3(input [7 : 0] op);
  //   begin
  //     gm3 = gm2(op) ^ op;
  //   end
  // endfunction // gm3

  function [7 : 0] gm4(input [7 : 0] op);
    begin
      gm4 = gm2(gm2(op));
    end
  endfunction // gm4

  function [7 : 0] gm8(input [7 : 0] op);
    begin
      gm8 = gm2(gm4(op));
    end
  endfunction // gm8

  function [7 : 0] gm09(input [7 : 0] op);
    begin
      gm09 = gm8(op) ^ op;
    end
  endfunction // gm09

  function [7 : 0] gm11(input [7 : 0] op);
    begin
      gm11 = gm8(op) ^ gm2(op) ^ op;
    end
  endfunction // gm11

  function [7 : 0] gm13(input [7 : 0] op);
    begin
      gm13 = gm8(op) ^ gm4(op) ^ op;
    end
  endfunction // gm13

  function [7 : 0] gm14(input [7 : 0] op);
    begin
      gm14 = gm8(op) ^ gm4(op) ^ gm2(op);
    end
  endfunction // gm14

  function [31 : 0] inv_mixw(input [31 : 0] w);
    reg [7 : 0] b0, b1, b2, b3;
    reg [7 : 0] mb0, mb1, mb2, mb3;
    begin
      b0 = w[31 : 24];
      b1 = w[23 : 16];
      b2 = w[15 : 08];
      b3 = w[07 : 00];

      mb0 = gm14(b0) ^ gm11(b1) ^ gm13(b2) ^ gm09(b3);
      mb1 = gm09(b0) ^ gm14(b1) ^ gm11(b2) ^ gm13(b3);
      mb2 = gm13(b0) ^ gm09(b1) ^ gm14(b2) ^ gm11(b3);
      mb3 = gm11(b0) ^ gm13(b1) ^ gm09(b2) ^ gm14(b3);

      inv_mixw = {mb0, mb1, mb2, mb3};
    end
  endfunction // mixw

  function [127 : 0] inv_mixcolumns(input [127 : 0] data);
    reg [31 : 0] w0, w1, w2, w3;
    reg [31 : 0] ws0, ws1, ws2, ws3;
    begin
      w0 = data[127 : 096];
      w1 = data[095 : 064];
      w2 = data[063 : 032];
      w3 = data[031 : 000];

      ws0 = inv_mixw(w0);
      ws1 = inv_mixw(w1);
      ws2 = inv_mixw(w2);
      ws3 = inv_mixw(w3);

      inv_mixcolumns = {ws0, ws1, ws2, ws3};
    end
  endfunction // inv_mixcolumns

  function [127 : 0] inv_shiftrows(input [127 : 0] data);
    reg [31 : 0] w0, w1, w2, w3;
    reg [31 : 0] ws0, ws1, ws2, ws3;
    begin
      w0 = data[127 : 096];
      w1 = data[095 : 064];
      w2 = data[063 : 032];
      w3 = data[031 : 000];

      ws0 = {w0[31 : 24], w3[23 : 16], w2[15 : 08], w1[07 : 00]};
      ws1 = {w1[31 : 24], w0[23 : 16], w3[15 : 08], w2[07 : 00]};
      ws2 = {w2[31 : 24], w1[23 : 16], w0[15 : 08], w3[07 : 00]};
      ws3 = {w3[31 : 24], w2[23 : 16], w1[15 : 08], w0[07 : 00]};

      inv_shiftrows = {ws0, ws1, ws2, ws3};
    end
  endfunction // inv_shiftrows

  wire [31:0] block_w0;
  wire [31:0] block_w1;
  wire [31:0] block_w2;
  wire [31:0] block_w3;

  wire [127:0] inv_sbox_in;
  wire [127:0] inv_sbox_out;
  wire [127:0] de_first_block;
  wire [127:0] de_main_block;
  wire [127:0] old_block;
  wire [127:0] addkey_final_block; 
  wire [127:0] addkey_main_block; 
  wire [127:0] shiftrows_block;
  wire [127:0] mixcolumns_block;
  wire [127:0] addkey_block;
  wire [127:0] inv_shiftrows_block_first;
  wire [127:0] inv_mixcolumns_block;
  wire [127:0] inv_shiftrows_block;

  
  assign sbox_out1 = en_de? block[31:0] : inv_sbox_in[31:0];
  assign sbox_out2 = en_de? block[63:32] : inv_sbox_in[63:32];
  assign sbox_out3 = en_de? block[95:64] : inv_sbox_in[95:64];
  assign sbox_out4 = en_de? block[127:96] : inv_sbox_in[127:96];
  assign block_w0 = sbox_in4;
  assign block_w1 = sbox_in3;
  assign block_w2 = sbox_in2;
  assign block_w3 = sbox_in1;
  assign inv_sbox_out[31:0] = sbox_in1;
  assign inv_sbox_out[63:32] = sbox_in2;
  assign inv_sbox_out[95:64] = sbox_in3;
  assign inv_sbox_out[127:96] = sbox_in4;
  // aes_4sbox aes_4sbox_a(.sboxw(block[127:96]),.new_sboxw(block_w0));
  // aes_4sbox aes_4sbox_b(.sboxw(block[95:64]),.new_sboxw(block_w1));
  // aes_4sbox aes_4sbox_c(.sboxw(block[63:32]),.new_sboxw(block_w2));
  // aes_4sbox aes_4sbox_d(.sboxw(block[31:0]),.new_sboxw(block_w3));

  // aes_4inv_sbox aes_4inv_sbox_first (.sboxw(inv_sbox_in[31:0]),  .new_sboxw(inv_sbox_out[31:0]));
  // aes_4inv_sbox aes_4inv_sbox_second(.sboxw(inv_sbox_in[63:32]), .new_sboxw(inv_sbox_out[63:32]));
  // aes_4inv_sbox aes_4inv_sbox_third (.sboxw(inv_sbox_in[95:64]), .new_sboxw(inv_sbox_out[95:64]));
  // aes_4inv_sbox aes_4inv_sbox_fourth(.sboxw(inv_sbox_in[127:96]),.new_sboxw(inv_sbox_out[127:96]));

//en
  assign old_block          = {block_w0, block_w1, block_w2, block_w3};
  assign shiftrows_block    = shiftrows(old_block);
  assign mixcolumns_block   = mixcolumns(shiftrows_block);
  assign addkey_main_block  = addroundkey(mixcolumns_block, round_key);
  assign addkey_final_block = addroundkey(shiftrows_block, round_key);
//de
  // assign addkey_block         = addroundkey(old_block, round_key);
  assign inv_shiftrows_block = inv_shiftrows(block);
  assign inv_sbox_in =  inv_shiftrows_block;
  // assign inv_mixcolumns_block = inv_mixcolumns(addkey_block);
  assign addkey_block = addroundkey(inv_sbox_out, round_key);
  assign inv_mixcolumns_block = inv_mixcolumns(addkey_block);
  // assign inv_shiftrows_block_main  = inv_shiftrows(inv_mixcolumns_block);

  // assign inv_sbox_in = (!en_de & last) ? inv_shiftrows_block_first : inv_shiftrows_block_main;
  assign new_block = (en_de && last)? addkey_final_block:
                     (en_de && !last)?addkey_main_block:
                     (!en_de && !last)?inv_mixcolumns_block:
                     addkey_block; 
endmodule // aes_encipher_block

//======================================================================
// EOF aes_encipher_block.v
//======================================================================