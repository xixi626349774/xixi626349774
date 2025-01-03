`include "defines.v"

module ex(
	//from id_ex
	input wire[31:0] inst_i,	
	input wire[31:0] inst_addr_i,
	input wire[31:0] op1_i,
	input wire[31:0] op2_i,
	input wire[4:0]  rd_addr_i,
	input wire rd_wen_i,
	input wire[31:0] base_addr_i,
	input wire[31:0] addr_offset_i,

	//to regs
	output reg[4:0] rd_addr_o,
	output reg[31:0]rd_data_o,
	output reg rd_wen_o,
	
	//to ctrl:beq
	output reg[31:0]jump_addr_o,
	output reg jump_en_o,
	
	//to dm write
	output reg dm_wr_req_o,
	output reg[31:0] dm_wr_addr_o,
	output reg[31:0] dm_wr_data_o,	

	//from dm read
	input wire[31:0] dm_rd_data_i
);

	wire[6:0] opcode; 
	wire[2:0] func3; 
	wire[6:0] func7;
	wire[4:0] rs1;
	wire[4:0] rs2;
	wire[4:0] rd; 
	wire[11:0]imm;
	
	assign opcode = inst_i[6:0];
	assign func3 = inst_i[14:12];
	assign func7 = inst_i[31:25];
	assign rs1 = inst_i[19:15];
	assign rs2 = inst_i[24:20];
	assign rd = inst_i[11:7];
	assign imm = inst_i[31:20];
	
	//ALU,减少被综合出的加法器
	wire equal;
	wire less;
	
	assign equal= (op1_i == op2_i) ? 1'b1 : 1'b0;
	assign less = ($signed(op1_i) < $signed(op2_i)) ? 1'b1 : 1'b0;
	
	wire[31:0] add; 
	wire[31:0] andop; 
	wire[31:0] orop;
	wire[31:0] addr_calculator; 
	
	assign add = op1_i + op2_i;
	assign andop = op1_i & op2_i;
	assign orop  = op1_i | op2_i;
	assign addr_calculator = base_addr_i + addr_offset_i;
	
	always @(*)begin	
		case(opcode)		
			`INST_TYPE_I:begin
				jump_addr_o = 32'b0;
				jump_en_o = 1'b0;
				dm_wr_req_o = 1'b0;
				dm_wr_addr_o = 32'b0;
				dm_wr_data_o = 32'b0;				
				case(func3)				
					`INST_ADDI:begin
						rd_data_o = add;							//而不是rd_data_o = op1_i + op2_i;
						rd_addr_o = rd_addr_i;
						rd_wen_o  = 1'b1;
					end
					`INST_SLTI:begin
						rd_data_o = {31'b0,less};					//而不是rd_data_o = {31'b0,op1_i < op2_i};
						rd_addr_o = rd_addr_i;
						rd_wen_o  = 1'b1;
					end								
					`INST_ORI:begin
						rd_data_o = orop;                      		//而不是rd_data_o = op1_i | op2_i;
						rd_addr_o = rd_addr_i;
						rd_wen_o  = 1'b1;
					end									
					default:begin
						rd_data_o = 32'b0;
						rd_addr_o = 5'b0;
						rd_wen_o  = 1'b0;
					end						
				endcase
			end				
			`INST_TYPE_R:begin			
				jump_addr_o = 32'b0;
				jump_en_o = 1'b0;
				dm_wr_req_o = 1'b0;
				dm_wr_addr_o = 32'b0;
				dm_wr_data_o = 32'b0;				
				case(func3)				
					`INST_ADD_SUB:begin
						if(func7[5] == 1'b0)begin
							rd_data_o = add;
							rd_addr_o = rd_addr_i;
							rd_wen_o = 1'b1;
						end
						else begin
							rd_data_o = op1_i - op2_i;
							rd_addr_o = rd_addr_i;
							rd_wen_o  = 1'b1; 								
						end
					end
					`INST_SLT:begin
						rd_data_o = {31'b0,less};
						rd_addr_o = rd_addr_i;
						rd_wen_o  = 1'b1;	
					end
					`INST_OR:begin
						rd_data_o = orop;
						rd_addr_o = rd_addr_i;
						rd_wen_o  = 1'b1;	
					end		
					default:begin
						rd_data_o = 32'b0;
						rd_addr_o = 5'b0;
						rd_wen_o  = 1'b0;					
					end
				endcase
			end			
			`INST_TYPE_B:begin//beq
				rd_data_o = 32'b0; 
				rd_addr_o = 5'b0;
				rd_wen_o = 1'b0;
				dm_wr_req_o = 1'b0;
				dm_wr_addr_o = 32'b0;
				dm_wr_data_o = 32'b0;				
				jump_addr_o = addr_calculator; 
				jump_en_o = equal;				
			end
			`INST_TYPE_L:begin//lw
				jump_addr_o = 32'b0;
				jump_en_o = 1'b0;
				dm_wr_req_o = 1'b0;
				dm_wr_addr_o = 32'b0;
				dm_wr_data_o = 32'b0;				
				rd_data_o = dm_rd_data_i;
				rd_addr_o = rd_addr_i;
				rd_wen_o  = 1'b1;						
			end
			`INST_TYPE_S:begin//sw
				jump_addr_o = 32'b0;
				jump_en_o = 1'b0;
				rd_data_o = 32'b0;
				rd_addr_o = 5'b0;
				rd_wen_o = 1'b0;				
				dm_wr_req_o = 1'b1;
				dm_wr_addr_o = addr_calculator;
				dm_wr_data_o = op2_i;						
			end			
			default:begin
				rd_data_o = 32'b0;
				rd_addr_o = 5'b0;
				rd_wen_o = 1'b0;
				jump_addr_o = 32'b0;
				jump_en_o = 1'b0;
				dm_wr_req_o = 1'b0;
				dm_wr_addr_o = 32'b0;
				dm_wr_data_o = 32'b0;				
			end
		endcase
	end
endmodule