module risc_v_soc(
	input  wire clk,
	input  wire rst     
);
	
    //risc_v to rom 
	wire[31:0] risc_v_inst_addr_o;

    //risc_v to ram
	wire risc_v_dm_wr_req_o;
	wire[31:0] risc_v_dm_wr_addr_o;
	wire[31:0] risc_v_dm_wr_data_o;
	wire 	   risc_v_dm_rd_req_o ;
	wire[31:0] risc_v_dm_rd_addr_o;

    //ram to risc_v
	wire[31:0] ram_rd_data_o;

    //rom to risc_v
	wire[31:0] rom_inst_o;

	risc_v risc_v_inst(
		.clk(clk),
		.rst(rst),
		.inst_i(rom_inst_o),
		.inst_addr_o(risc_v_inst_addr_o),
		.dm_rd_req_o(risc_v_dm_rd_req_o),
		.dm_rd_addr_o(risc_v_dm_rd_addr_o),
		.dm_rd_data_i(ram_rd_data_o),
		.dm_wr_req_o(risc_v_dm_wr_req_o),
		.dm_wr_addr_o(risc_v_dm_wr_addr_o),
		.dm_wr_data_o(risc_v_dm_wr_data_o)	
    );
	
	ram ram_inst(
		.clk(clk),
		.rst(rst),
		.wen(risc_v_dm_wr_req_o),
		.w_addr_i(risc_v_dm_wr_addr_o),
		.w_data_i(risc_v_dm_wr_data_o),
		.ren(risc_v_dm_rd_req_o), 
		.r_addr_i(risc_v_dm_rd_addr_o),
		.r_data_o(ram_rd_data_o)
	);
	
	rom rom_inst(
		.clk(clk),
		.rst(rst),	
		.wen(1'b0), //指令存储器只读不写
		.w_addr_i(7'b0),
		.w_data_i(32'b0),
		.ren(1'b1), 
		.r_addr_i(risc_v_inst_addr_o),
		.r_data_o(rom_inst_o)
	);
endmodule