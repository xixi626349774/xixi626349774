`timescale 1ns / 1ps
//////////////////////////////////////////////////////////////////////////////////
// Company: 
// Engineer: 
// 
// Create Date: 2024/10/25 13:17:06
// Design Name: 
// Module Name: ALU
// Project Name: 
// Target Devices: 
// Tool Versions: 
// Description: 
// 
// Dependencies: 
// 
// Revision:
// Revision 0.01 - File Created
// Additional Com:
// 
//////////////////////////////////////////////////////////////////////////////////

module _7seg(
    input [3:0] data,
    output reg [6:0] seg_out
); 

always @ (data) begin
    case (data)
        4'b0000: seg_out = 7'b0000001;   
        4'b0001: seg_out = 7'b1001111;   
        4'b0010: seg_out = 7'b0010010;   
        4'b0011: seg_out = 7'b0000110;   
        4'b0100: seg_out = 7'b1001100;   
        4'b0101: seg_out = 7'b0100100;   
        4'b0110: seg_out = 7'b0100000;   
        4'b0111: seg_out = 7'b0001111;   
        4'b1000: seg_out = 7'b0000000;   
        4'b1001: seg_out = 7'b0000100;   
        4'b1010: seg_out = 7'b0001000;   
        4'b1011: seg_out = 7'b1100000;   
        4'b1100: seg_out = 7'b0110001;   
        4'b1101: seg_out = 7'b1000010;   
        4'b1110: seg_out = 7'b0110000;   
        4'b1111: seg_out = 7'b0111000;      
    endcase 
end
endmodule 

module ALU(  
    input [3:0]A,
    input [3:0]B,
    input [1:0]op,
    input clk,
    output reg [6:0] seg,
    output reg [7:0] an 
    );

reg [7:0] F;
reg [3:0] digits;
wire s;
reg [19:0] clkdiv;

assign s = clkdiv[19];

always @(s) 
    case(s)
        1'b0: digits = F[3:0];
        1'b1: digits = F[7:4];
    endcase

wire [6:0] bits;
_7seg _7seg(digits, bits);

always @(*) begin
    an = 8'b11111111;
    an[s] = 1'b0;
end

always @(posedge clk) begin
    if(clkdiv == 20'b1111111111_1111111111) 
        clkdiv <= 0;
    else
        clkdiv <= clkdiv + 1;
end

always @(*) begin
    F = 8'b0;
    if(op == 2'b00) begin 
            F = A + B;
            if(F[4] == 1'b1)  
                F[7:4] = 4'b1111;
        end
    else if(op == 2'b01) begin
            F = A - B;
            if(F[4] == 1'b1) 
                F[7:4] = 4'b1111;
        end
    else if(op == 2'b10) begin
            F =8'b0;
            F[3:0] = ~A;
        end
    else begin
            F = A * B;
        end
end

always @(posedge clk) begin
    seg <= bits;
end

endmodule

 