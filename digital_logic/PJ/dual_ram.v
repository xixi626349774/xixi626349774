module dual_ram #( 
	parameter DW = 8,
	parameter AW = 7,
	parameter MEM_NUM = 128
)
(
	input wire clk,
	input wire rst,
	input wire wen,
	input wire[AW-1:0] w_addr_i,
	input wire[(4 * DW) - 1:0] w_data_i,
	input wire ren,
	input wire[AW-1:0] r_addr_i,
	output wire[(4 * DW) - 1:0] r_data_o
);	
	
	
	wire[(4 * DW) - 1:0] r_data_wire;
	//判断是否有读写冲突
	reg rw_conflict; 
	reg[(4 * DW) - 1:0]	 w_data_reg;
	
	//组合电路:若读写冲突,则将要写入的数据直接读出;否则从存储器中读出
	assign r_data_o = (rw_conflict) ? w_data_reg : r_data_wire;
	
	always @(posedge clk)begin
		if(!rst)
			w_data_reg <= 'b0;
		else
			w_data_reg <= w_data_i;
	end
	
	//时钟上升沿到来时,检查当前时钟操作是否有读写冲突,并更新相应标志位
	always @(posedge clk)begin
		if(rst && wen && ren && w_addr_i == r_addr_i )
			rw_conflict <= 1'b1;
		else if(rst && ren)
			rw_conflict <= 1'b0;
	end
		
	//未实现读写冲突处理的双端ram
	dual_ram_template #(
		.DW (DW),
		.AW (AW),
		.MEM_NUM (MEM_NUM)
	)dual_ram_template_inst
	(
		.clk(clk),
		.rst(rst),
		.wen(wen),
		.w_addr_i(w_addr_i),
		.w_data_i(w_data_i),
		.ren(ren),
		.r_addr_i(r_addr_i),
		.r_data_o(r_data_wire)
	);

endmodule

module dual_ram_template #(
	parameter DW = 8,
	parameter AW = 7,
	parameter MEM_NUM = 128
)
(
	input wire clk,
	input wire rst,
	input wire wen,
	input wire[AW-1:0] w_addr_i,
	input wire[(4 * DW) - 1:0] w_data_i,
	input wire ren,
	input wire[AW-1:0] r_addr_i,
	output reg[(4 * DW) - 1:0] r_data_o
);
	//128 bytes
	reg[DW-1:0] memory[0:MEM_NUM-1];
	
	always @(posedge clk)begin
		if(rst && ren)
			r_data_o <= {memory[r_addr_i + 3], memory[r_addr_i + 2], memory[r_addr_i + 1], memory[r_addr_i]};//小端法
	end
	
	always @(posedge clk)begin
		if(rst && wen)begin
			//小端法
			memory[w_addr_i] <= w_data_i[7:0];
			memory[w_addr_i + 1] <= w_data_i[15:8];
			memory[w_addr_i + 2] <= w_data_i[23:16];
			memory[w_addr_i + 3] <= w_data_i[31:24];
		end
	end

endmodule