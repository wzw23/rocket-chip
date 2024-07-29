`default_nettype none

module aes_key_w(
                   input wire [127 : 0]  key,
                   input wire    [3 : 0] round,
                   output wire [127 : 0] round_key);
      //G function:sbox=>replace=>add
      wire [31 : 0] rconw, rotstw, tw, trw;
      wire [31:0] tmp_sboxw,new_sboxw;
      wire [31:0]w0,w1,w2,w3,w0_p,w1_p,w2_p,w3_p;
      reg  [7:0]rcon_out;
      assign rconw = {rcon_out, 24'h0};
      assign tmp_sboxw = w3_p;
      assign rotstw = {new_sboxw[23 : 00], new_sboxw[31 : 24]};
      assign trw = rotstw ^ rconw;
      assign tw = new_sboxw;

      assign {w0_p,w1_p,w2_p,w3_p} = key;
      assign w0 = w0_p ^ trw;
      assign w1 = w1_p ^ w0_p ^ trw;
      assign w2 = w2_p ^ w1_p ^ w0_p ^ trw;
      assign w3 = w3_p ^ w2_p ^ w1_p ^ w0_p ^ trw;
      assign round_key = {w0,w1,w2,w3};

      aes_4sbox aes_4sbox (.sboxw(tmp_sboxw),. new_sboxw(new_sboxw));

      always @*
        begin : rcon_logic
        case (round)
          4'd1: rcon_out = 8'h1;
          4'd2: rcon_out = 8'h2;
          4'd3: rcon_out = 8'h4;
          4'd4: rcon_out = 8'h8;
          4'd5: rcon_out = 8'h10;
          4'd6: rcon_out = 8'h20;
          4'd7: rcon_out = 8'h40;
          4'd8: rcon_out = 8'h80;
          4'd9: rcon_out = 8'h1B;
          4'd10: rcon_out = 8'h36;
        default: rcon_out = 8'b0;
       endcase
       end
endmodule // aes_key_mem
