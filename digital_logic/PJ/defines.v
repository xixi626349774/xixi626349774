// I type inst
`define INST_TYPE_I 7'b0010011
`define INST_ADDI   3'b000
`define INST_SLTI   3'b010
`define INST_ORI    3'b110

// R type inst
`define INST_TYPE_R 7'b0110011
`define INST_ADD_SUB 3'b000
`define INST_SLT    3'b010
`define INST_OR     3'b110

// B type inst
`define INST_TYPE_B 7'b1100011

// L type inst
`define INST_TYPE_L 7'b0000011
 
// S type inst
`define INST_TYPE_S 7'b0100011

`define INST_NOP    32'h00000013
