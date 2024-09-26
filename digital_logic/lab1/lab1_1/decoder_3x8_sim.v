`timescale 1ns / 1ps
//////////////////////////////////////////////////////////////////////////////////
// Company: 
// Engineer: 
// 
// Create Date: 2024/09/25 20:49:14
// Design Name: 
// Module Name: decoder_3x8_sim
// Project Name: 
// Target Devices: 
// Tool Versions: 
// Description: 
// 
// Dependencies: 
// 
// Revision:
// Revision 0.01 - File Created
// Additional Comments:
// 
//////////////////////////////////////////////////////////////////////////////////
  
module decoder_3x8_sim;   
reg [2:0] A;  
wire [7:0] Y;  
  
decoder_3x8 decoder(  
    .A(A),   
    .Y(Y)  
);  
    
initial begin  
    A = 3'b000; #10;  
  
    A = 3'b001; #10;  
    A = 3'b010; #10;  
    A = 3'b011; #10;  
    A = 3'b100; #10;  
    A = 3'b101; #10;  
    A = 3'b110; #10;  
    A = 3'b111; #10;  
 
    $finish;  
end  
  
endmodule