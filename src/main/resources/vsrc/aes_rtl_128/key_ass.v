`default_nettype none

module aes_key_acc(
                   input wire            clk,
                   input wire            reset_n,

                   input wire [255 : 0]  key,
                   input wire            next,
                   input wire            encdec,

                   input wire    [3 : 0] round,
                   output wire [127 : 0] round_key

//                   output wire [31 : 0]  sboxw,
//                   input wire  [31 : 0]  new_sboxw
);
  //----------------------------------------------------------------
  // Parameters.
  //----------------------------------------------------------------
  localparam AES_128_NUM_ROUNDS = 8;
  localparam IDLE     = 3'h0;
  localparam INIT     = 3'h1;
  localparam MAIN = 3'h2;
  localparam DONE     = 3'h3;

  //----------------------------------------------------------------
  // Registers.
  //----------------------------------------------------------------
  reg [1:0]state_cur;
  reg [127:0]  key_reg;
  reg [3 : 0] round_b;

  //----------------------------------------------------------------
  // Wires.
  //----------------------------------------------------------------
  reg [1:0]state_next;
  reg  key_reg_we;
  reg [127:0]  key_reg_new;
  reg round_key_sit;
  wire  [127:0] key_trans;
  reg round_b_we;
  reg [3 : 0]round_b_new;
  wire [3:0] round_use;

  //----------------------------------------------------------------
  // Concurrent assignments for ports.
  //----------------------------------------------------------------
  assign round_key = (round_key_sit == 1'b1)? key_trans : key_reg;

  //----------------------------------------------------------------
  // Wire
  //----------------------------------------------------------------
  assign key_trans = (encdec==1)?{key_reg[71:64],key_reg[127:72],key_reg[7:0],key_reg[63:8]}:
{key_reg[119:64],key_reg[127:120],key_reg[55:0],key_reg[63:56]};
  assign round_use = (encdec==1 || (next&&round ==0))?round : AES_128_NUM_ROUNDS  - round;

  //----------------------------------------------------------------
  // reg_update
  //----------------------------------------------------------------
  always @ (posedge clk or negedge reset_n)
	begin
		if(!reset_n)
			begin
				state_cur <= IDLE;
				key_reg <= 0;
				round_b <= 0;
			end
		else begin
			state_cur <= state_next;
			if(key_reg_we)
				key_reg <= key_reg_new;
			if(round_b_we)
				round_b <= round_b_new;
		end
	end

  //----------------------------------------------------------------
  // state signal.
  //----------------------------------------------------------------
  always @*
	begin: key_state_ctrl
	case(state_cur)
		IDLE:
			begin
			round_key_sit = 1'b0;
			if(next)
			begin
				state_next = INIT;
				key_reg_we = 1'b1;
				key_reg_new = key[255:128];
			end
			else 
				key_reg_we = 1'b0;
			end
		INIT:
			begin
				state_next = MAIN;
				round_key_sit = 1'b0;
				round_b_we = 1'b1;
				round_b_new = round_use;
			end
		MAIN:
			if(round_use < AES_128_NUM_ROUNDS )
				if(round_use != round_b)
				begin
					round_key_sit = 1'b1;
					key_reg_we = 1'b1;
					key_reg_new = key_trans;
					round_b_we = 1'b1;
					round_b_new = round_use;
				end
				else begin
					round_key_sit = 1'b0;
				end
			else begin
				state_next = IDLE;
				if(round_use != round_b)
				begin
					round_key_sit = 1'b1;
					key_reg_we = 1'b1;
					key_reg_new = key_trans;
					round_b_we = 1'b1;
					round_b_new = round_use;
				end
				else begin
					round_key_sit = 1'b0;
				end
			end
		default:
			begin
			state_next = IDLE;
			round_key_sit = 1'b0;
			key_reg_we = 1'b0;
			end
	endcase
	end
endmodule
