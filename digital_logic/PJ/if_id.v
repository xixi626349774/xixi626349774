`include "defines.v"
module if_id(
	input wire clk,
	input wire rst,
	input wire hold_flag_i,//冲刷信号
	input wire [31:0] inst_i, 
	input wire [31:0] inst_addr_i,
	output wire[31:0] inst_addr_o, 
	output wire[31:0] inst_o
);

	//判断是否需要冲刷,若需要冲刷将进行处理的指令设为NOP;否则从指令寄存器IR中取出指令
	reg rom_flag;
	
	always @(posedge clk) begin
		if(!rst | hold_flag_i)
			rom_flag <= 1'b0;
		else
			rom_flag <= 1'b1;
	end
	
	assign inst_o = rom_flag ? inst_i : `INST_NOP;

	//触发器,实现同步或异步置位,冲刷时将指令地址置为0
	dff_set #(32) dff(clk,rst,hold_flag_i,32'b0,inst_addr_i,inst_addr_o);
	
endmodule

