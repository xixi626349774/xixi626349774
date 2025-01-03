`timescale 1ns / 1ps
//////////////////////////////////////////////////////////////////////////////////
// Company: 
// Engineer: 
// 
// Create Date: 2024/11/07 22:25:03
// Design Name: 
// Module Name: vending_machine
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

module _7seg(
    input [3:0] data,
    input flag,
    output reg [6:0] seg_out
); 

always @(data, flag) begin
    if(flag)
        seg_out = 7'b1111110;
    else begin
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
            default: seg_out = 7'b1111111;  
        endcase 
    end
end
endmodule 

module vending_machine (
    input SW,
    input clk,                  
    input [2:0] product_switch,  
    input [3:0] money_button,    
    output reg [6:0] seg,     
    output reg [7:0] AN,
    output reg purchase_success 
);

    parameter DEBOUNCE_DELAY = 1500;//15ms
    reg [7:0] product_price [0:5];
    reg [3:0] selected_product; 
    reg [7:0] paid_amount;
    reg [3:0] digits;
    reg [19:0] clkdiv;
    reg [10:0] debounce_counter;
    reg invalid_product;
    wire [1:0] s;
    wire rst;

    initial begin
        product_price[0] = 8'b0000_0101; //0.5
        product_price[1] = 8'b0001_0000; //1.0
        product_price[2] = 8'b0001_0101;//1.5
        product_price[3] = 8'b0010_0000; //2.0
        product_price[4] = 8'b0110_0101;//6.5
        product_price[5] = 8'b1101_0000;//13.0
    end 

    assign s = clkdiv[19:18];
    assign rst = SW;
    
    always @(s) 
    case (s)
        2'b00:digits = selected_product;
        2'b01:digits = paid_amount[3:0];
        2'b10:digits = paid_amount[7:4];
        2'b11:digits = selected_product;
        default: digits = selected_product;
    endcase

    always @(*)begin
        AN = 8'b11111111;
        if(s == 3)
            AN[0] = 1'b0;
        else
            AN[s] = 1'b0;
    end

    reg [3:0] pre_money_button;
    wire [6:0] bits;
    _7seg _7seg(digits, invalid_product, bits);

    always @(posedge clk or posedge rst) begin
        if (rst) begin
            clkdiv <= 0;
            paid_amount <= 0;
            seg <= 7'b1111111;
            pre_money_button <= 0;
            purchase_success <= 0;
            invalid_product <= 0;
        end
        else begin
            clkdiv <= clkdiv + 1;
            seg <= bits;

            selected_product <= product_switch;
            if (selected_product == 0 || selected_product > 4'b0110)
                invalid_product <= 1'b1;
            else
                invalid_product <= 1'b0;

            if(pre_money_button!= money_button) 
                debounce_counter <= 0;
            if(debounce_counter < DEBOUNCE_DELAY - 1) 
                debounce_counter <= debounce_counter + 1;
            else begin
                if (money_button[0] && !pre_money_button[0]) begin
                    if(paid_amount[3:0] == 4'b0101) begin
                        paid_amount[3:0] <= 4'b0000;  
                        paid_amount[7:4] <= paid_amount[7:4] + 4'b0001;
                    end else 
                        paid_amount[3:0] <= 4'b0101;
                end
                if (money_button[1] && !pre_money_button[1]) 
                    paid_amount[7:4] <= paid_amount[7:4] + 4'b0001;
                if (money_button[2] && !pre_money_button[2])
                    paid_amount[7:4] <= paid_amount[7:4] + 4'b0010;
                if (money_button[3] && !pre_money_button[3]) 
                    paid_amount[7:4] <= paid_amount[7:4] + 4'b0101;
                pre_money_button <= money_button;
            end

            if (selected_product > 4'b0000 && selected_product < 4'b0111) begin
                if (paid_amount == product_price[selected_product - 1])
                    purchase_success <= 1'b1;
                    else 
                    purchase_success <= 1'b0;
            end else
                purchase_success <= 1'b0;
        end
    end
endmodule
 

 