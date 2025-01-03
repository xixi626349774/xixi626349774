module rom(
	input  wire clk,
	input  wire rst,
	input  wire wen,
	input  wire[31:0] w_addr_i,
	input  wire[31:0] w_data_i,
	input  wire ren,
	input  wire[31:0] r_addr_i,
	output wire[31:0] r_data_o
);
	wire[6:0] w_addr = w_addr_i[6:0];
	wire[6:0] r_addr = r_addr_i[6:0];

	dual_ram #(
		 .DW(8),
		 .AW(7),
		 .MEM_NUM(128)
	)
	rom_32bits
	(
       .clk(clk),
	   .rst(rst),
       .wen(wen),
	   .w_addr_i(w_addr),
	   .w_data_i(w_data_i),
	   .ren(ren),
	   .r_addr_i(r_addr),
	   .r_data_o(r_data_o)
	);
endmodule