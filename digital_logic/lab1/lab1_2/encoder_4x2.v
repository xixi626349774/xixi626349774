`timescale 1ns / 1ps
//////////////////////////////////////////////////////////////////////////////////
// Company: 
// Engineer: 
// 
// Create Date: 2024/09/25 22:34:49
// Design Name: 
// Module Name: encoder_4x2
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


module encoder_4x2(  
    input [3:0] A, 
    output reg [1:0] Y  
);  

always @(A[0],A[1],A[2],A[3]) begin 
    if (A == 4'b0001)  
        Y = 2'b00; 
    else if (A == 4'b0010)  
        Y = 2'b01;  
    else if (A == 4'b0100)  
        Y = 2'b10;   
    else if (A == 4'b1000)  
        Y = 2'b11;    
    else   
        Y = 2'bxx;    
 end
 
endmodule

