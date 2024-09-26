`timescale 1ns / 1ps
//////////////////////////////////////////////////////////////////////////////////
// Company: 
// Engineer: 
// 
// Create Date: 2024/09/25 18:59:05
// Design Name: 
// Module Name: decoder_3x8
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


module decoder_3x8(
    input [2:0] A,
    output [7:0] Y
    );
        assign Y[0] = ~A[2] & ~A[1] & ~A[0];
        assign Y[1] = ~A[2] & ~A[1] & A[0];
        assign Y[2] = ~A[2] & A[1] & ~A[0];
        assign Y[3] = ~A[2] & A[1] & A[0];
        assign Y[4] = A[2] & ~A[1] & ~A[0];
        assign Y[5] = A[2] & ~A[1] & A[0];
        assign Y[6] = A[2] & A[1] & ~A[0];
        assign Y[7] = A[2] & A[1] & A[0];
        
        
endmodule 
