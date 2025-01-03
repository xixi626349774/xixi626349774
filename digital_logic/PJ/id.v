`include "defines.v"

module id(
	//from if_id
	input wire[31:0] inst_i,
	input wire[31:0] inst_addr_i,
		
	// to regs 
	output reg[4:0] rs1_addr_o,
	output reg[4:0] rs2_addr_o,
	// from regs
	input wire[31:0] rs1_data_i,
	input wire[31:0] rs2_data_i,
	
	//to id_ex
	output reg[31:0] inst_o,
	output reg[31:0] inst_addr_o,
	output reg[31:0] op1_o,
	output reg[31:0] op2_o,
	output reg[4:0]  rd_addr_o,
	output reg reg_wen,
	output reg[31:0] base_addr_o,
	output reg[31:0] addr_offset_o,	
	
	//to dm read
	output reg dm_rd_req_o,
	output reg[31:0] dm_rd_addr_o
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
	
	always @(*)begin
		inst_o = inst_i;
		inst_addr_o = inst_addr_i;  
		
		case(opcode)
			`INST_TYPE_I:begin
				dm_rd_req_o = 1'b0 ;//默认赋值
				dm_rd_addr_o = 32'b0;
				base_addr_o = 32'b0;
				addr_offset_o = 32'b0;		
				case(func3)
					`INST_ADDI,`INST_SLTI,`INST_ORI:begin
						rs1_addr_o = rs1;
						rs2_addr_o = 5'b0;
						op1_o = rs1_data_i;
						op2_o = {{20{imm[11]}},imm};//sign extension
						rd_addr_o = rd;
						reg_wen = 1'b1;
					end
					default:begin
						rs1_addr_o = 5'b0;
						rs2_addr_o = 5'b0;
						op1_o = 32'b0;
						op2_o = 32'b0;
						rd_addr_o = 5'b0;
						reg_wen = 1'b0;						
					end
				endcase	
			end
			`INST_TYPE_R:begin
				dm_rd_req_o = 1'b0 ;
				dm_rd_addr_o = 32'b0;
				base_addr_o = 32'b0;
				addr_offset_o = 32'b0;		
				case(func3)
					`INST_ADD_SUB,`INST_SLT,`INST_OR:begin
						rs1_addr_o = rs1;
						rs2_addr_o = rs2;
						op1_o = rs1_data_i;
						op2_o = rs2_data_i;
						rd_addr_o = rd;
						reg_wen = 1'b1;
					end	
					default:begin
						rs1_addr_o = 5'b0;
						rs2_addr_o = 5'b0;
						op1_o 	   = 32'b0;
						op2_o      = 32'b0;
						rd_addr_o  = 5'b0;
						reg_wen    = 1'b0;						
					end
				endcase				
			end
			`INST_TYPE_B:begin//beq
				dm_rd_req_o = 1'b0 ;
				dm_rd_addr_o = 32'b0;
				rs1_addr_o = rs1;
				rs2_addr_o = rs2;
				op1_o = rs1_data_i;
				op2_o = rs2_data_i;
				rd_addr_o = 5'b0;
				reg_wen = 1'b0;
				base_addr_o = inst_addr_i;
				addr_offset_o = {{20{inst_i[31]}},inst_i[7],inst_i[30:25],inst_i[11:8],1'b0};						
			end
			`INST_TYPE_L:begin//lw
				dm_rd_req_o	= 1'b1 ;
				dm_rd_addr_o = rs1_data_i + {{20{imm[11]}},imm};
				rs1_addr_o = rs1;
				rs2_addr_o = 5'b0;
				op1_o = 32'b0;
				op2_o = 32'b0;
				rd_addr_o = rd;
				reg_wen = 1'b1;	
				base_addr_o = rs1_data_i;
				addr_offset_o = {{20{imm[11]}},imm};						
			end
			`INST_TYPE_S:begin//sw
				dm_rd_req_o	= 1'b0;
				dm_rd_addr_o = 32'b0;
				rs1_addr_o = rs1;
				rs2_addr_o = rs2;
				op1_o = 32'b0;
				op2_o = rs2_data_i;
				rd_addr_o = 5'b0;
				reg_wen = 1'b0;
				base_addr_o = rs1_data_i;
				addr_offset_o = {{20{inst_i[31]}},inst_i[31:25],inst_i[11:7]};						
			end
			default:begin
				dm_rd_req_o	= 1'b0 ;
				dm_rd_addr_o = 32'b0;			
				rs1_addr_o = 5'b0;
				rs2_addr_o = 5'b0;
				op1_o = 32'b0;
				op2_o = 32'b0;
				rd_addr_o = 5'b0;
				reg_wen = 1'b0;
				base_addr_o = 32'b0;
				addr_offset_o = 32'b0;				
			end
		endcase
	end
endmodule