module tb();
	reg clk;
	reg rst;
	 
	always #10 clk = ~clk;
	
	risc_v_soc risc_v_soc_inst(
		.clk(clk),
		.rst(rst)
	);

	initial begin
		clk <= 1'b1;
		rst <= 1'b0;
		#30;
		rst <= 1'b1;
	end
	
	//存储器初始值
	initial begin
		$readmemh("PM.txt",tb.risc_v_soc_inst.rom_inst.rom_32bits.dual_ram_template_inst.memory);
		$readmemh("DM.txt",tb.risc_v_soc_inst.ram_inst.ram_32bits.dual_ram_template_inst.memory);
	end
endmodule