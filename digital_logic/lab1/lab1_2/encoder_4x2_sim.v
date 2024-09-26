`timescale 1ns / 1ps
//////////////////////////////////////////////////////////////////////////////////
// Company: 
// Engineer: 
// 
// Create Date: 2024/09/25 22:35:33
// Design Name: 
// Module Name: encoder_4x2_sim
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


module encoder_4x2_sim();  
reg [3:0] A;  
wire [1:0] Y;  
   
encoder_4x2 encoder(  
    .A(A),  
    .Y(Y)  
);  
  
initial begin  
    A = 4'b0000;  
 
    #10 A = 4'b0001;  
    #10 A = 4'b0010;   
    #10 A = 4'b0100;   
    #10 A = 4'b1000;  
    
    #10 A = 4'b0101;  
    #10 A = 4'b1100;  
    #10 A = 4'b1110;  
  
    #10 $finish;  
end  
  
endmodule