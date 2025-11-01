//> using scala "2.13.12"
//> using dep org.chipsalliance::chisel:7.2.0
//> using plugin org.chipsalliance:::chisel-plugin:7.2.0
//> using options -unchecked -deprecation -language:reflectiveCalls -feature -Xcheckinit
//> using options -Xfatal-warnings -Ywarn-dead-code -Ywarn-unused -Ymacro-annotations


import _root_.circt.stage.ChiselStage

import chisel3._
import chisel3.util._
import chisel3.util.experimental.decode._
import VADL._

class InstructionDecoder extends Module {

  class IO extends Bundle {
    val DECODE_en_in = Input(Bool())
    val read_readMEM1_result_in = Flipped(new VADL.RegReadPort(Bits(32.W)))
    val fwd_X0_en_in = Input(Bool())
    val fwd_X0_val_in = Input(Bits(32.W))
    val readX0_result_in = Flipped(new VADL.RegFileReadPort(Bits(32.W), 5))
    val read_DECODE_full_in = Flipped(new VADL.RegReadPort(Bits(1.W)))
    val fwd_X1_en_in = Input(Bool())
    val fwd_X1_val_in = Input(Bits(32.W))
    val readX1_result_in = Flipped(new VADL.RegFileReadPort(Bits(32.W), 5))
    val read_readMEM1_addr_in = Flipped(new VADL.RegReadPort(Bits(32.W)))
    val read_writePC1_value_in = Flipped(new VADL.RegReadPort(Bits(32.W)))
    val read_bin_is_BEQ_is_BNE_is_BGE_is_BG_out = new VADL.RegReadPort(Bits(3.W))
    val read_rs1_neq_0_out = new VADL.RegReadPort(Bits(1.W))
    val read_readX0_result_out = new VADL.RegReadPort(Bits(32.W))
    val read_rs2_neq_0_out = new VADL.RegReadPort(Bits(1.W))
    val read_readX1_result_out = new VADL.RegReadPort(Bits(32.W))
    val read_writePC1_value_0_out = new VADL.RegReadPort(Bits(32.W))
    val read_readMEM1_addr_0_out = new VADL.RegReadPort(Bits(32.W))
    val read_n204_out = new VADL.RegReadPort(Bits(32.W))
    val read_is_JALRBNEBGEUBGEJALBLTUBEQBLT_out = new VADL.RegReadPort(Bits(1.W))
    val read_sel_writePC0_value_out = new VADL.RegReadPort(Bits(2.W))
    val read_sel_readMEM0_addr_out = new VADL.RegReadPort(Bits(5.W))
    val read_readMEM0_addr_out = new VADL.RegReadPort(Bits(32.W))
    val read_rs2_out = new VADL.RegReadPort(Bits(5.W))
    val read_is_S_out = new VADL.RegReadPort(Bits(1.W))
    val read_sel_writeMEM0_words_out = new VADL.RegReadPort(Bits(2.W))
    val read_not_is_not_none_out = new VADL.RegReadPort(Bits(1.W))
    val read_sel_writeX0_value_out = new VADL.RegReadPort(Bits(3.W))
    val read_sel_readMEM0_words_out = new VADL.RegReadPort(Bits(2.W))
    val read_is_L_out = new VADL.RegReadPort(Bits(1.W))
    val read_writeX0_enable_out = new VADL.RegReadPort(Bits(1.W))
    val read_rd_out = new VADL.RegReadPort(Bits(5.W))
    val rs1_out = Output(Bits(5.W))
    val readX1_addr_out = Output(Bits(5.W))
    val readX0_enable_out = Output(Bits(1.W))
    val readX1_enable_out = Output(Bits(1.W))

  }

  val io = IO(new IO)

  val rs1 = Wire(Bits(5.W))
  val readX1_addr = Wire(Bits(5.W))
  val readX0_enable = Wire(Bits(1.W))
  val readX1_enable = Wire(Bits(1.W))
  val rd = RegInit(0.U.asTypeOf(Bits(5.W)))
  val rs2 = RegInit(0.U.asTypeOf(Bits(5.W)))
  val readMEM0_addr = RegInit(0.U.asTypeOf(Bits(32.W)))
  val rs1_neq_0 = RegInit(0.U.asTypeOf(Bits(1.W)))
  val rs2_neq_0 = RegInit(0.U.asTypeOf(Bits(1.W)))
  val n204 = RegInit(0.U.asTypeOf(Bits(32.W)))
  val writeX0_enable = RegInit(0.U.asTypeOf(Bits(1.W)))
  val is_JALRBNEBGEUBGEJALBLTUBEQBLT = RegInit(0.U.asTypeOf(Bits(1.W)))
  val is_S = RegInit(0.U.asTypeOf(Bits(1.W)))
  val is_L = RegInit(0.U.asTypeOf(Bits(1.W)))
  val bin_is_BEQ_is_BNE_is_BGE_is_BG = RegInit(0.U.asTypeOf(Bits(3.W)))
  val sel_writePC0_value = RegInit(0.U.asTypeOf(Bits(2.W)))
  val sel_readMEM0_words = RegInit(0.U.asTypeOf(Bits(2.W)))
  val sel_writeMEM0_words = RegInit(0.U.asTypeOf(Bits(2.W)))
  val sel_writeX0_value = RegInit(0.U.asTypeOf(Bits(3.W)))
  val sel_readMEM0_addr = RegInit(0.U.asTypeOf(Bits(5.W)))
  val not_is_not_none = RegInit(0.U.asTypeOf(Bits(1.W)))
  val readX0_result = RegInit(0.U.asTypeOf(Bits(32.W)))
  val readX1_result = RegInit(0.U.asTypeOf(Bits(32.W)))
  val readMEM1_addr = RegInit(0.U.asTypeOf(Bits(32.W)))
  val writePC1_value = RegInit(0.U.asTypeOf(Bits(32.W)))
  val writeX0_value = Wire(Bits(32.W))
  val n_162 = Wire(Bool())
  val n_166 = Wire(Bool())

  val rs1_neq_0_and_is_not_AUIPCLUIJ = Wire(Bits(1.W))
  val sig_122 = Wire(Bits(32.W))
  val rs2_neq_0_and_is_SRLBGEUBGESRA = Wire(Bits(1.W))
  val sig_128 = Wire(Bits(32.W))

  // -- Decode

  // {BNE, BGEU, BGE, BLTU, BEQ, BLT}
  val sig_152 = Wire(Bits(1.W))

  // {SH, SB, SW}
  val sig_147 = Wire(Bits(1.W))

  // OH ( Cat ( {BNE, BGEU, BGE, BLTU, BEQ, BLT}; {SH, SB, SW}; {JAL}; {AUIPC}; {LH, JALR, SLTI, LB, ANDI, SLTIU, XORI, LHU, LW, LBU, ADDI, ORI} ) )
  // in-width: 5, out-width 3
  val a0 = Wire(Bits(3.W))

  // {LB, JAL, SRA, LHU, LBU, ORI, SLT, XOR, SLTI, SRLI, LW, SLL, SRL, JALR, LUI, ANDI, XORI, OR, LH, SLLI, SUB, SLTIU, SLTU, AND, ADD, AUIPC, ADDI, SRAI}
  val a1 = Wire(Bool())
  // {JALR, BNE, BGEU, BGE, JAL, BLTU, BEQ, BLT}
  val a2 = Wire(Bool())

  // {LH, LB, LHU, LW, LBU}
  val sig_214 = Wire(Bits(1.W))

  // OH ( Cat ( {JALR}; {JAL}; {BLTU}; {BLT}; {BGEU}; {BGE}; {BNE}; {BEQ} ) )
  // in-width: 8, out-width: 3
  val a3 = Wire(Bits(3.W))
  // OH ( Cat ( {JALR}; {JAL}; {BNE, BGEU, BGE, BLTU, BEQ, BLT} ) )
  // in-width: 3, out-width: 2
  val a4 = Wire(Bits(2.W))
  // OH ( Cat ( {LH, LHU}; {LW}; {LB, LBU} ) )
  // in-width: 3, out-width: 2
  val a5 = Wire(Bits(2.W))
  // OH ( Cat ( {SW}; {SH}; {SB} ) )
  // in-width: 3, out-width: 2
  val a6 = Wire(Bits(2.W))
  // OH ( Cat ( {SRL, JALR, LUI, JAL, ANDI, SRA, XORI, OR, ORI, SLT, SLLI, XOR, SLTI, SUB, SLTIU, SLTU, SRLI, AND, ADD, AUIPC, ADDI, SLL, SRAI}; {LW}; {LHU}; {LH}; {LBU}; {LB} ) )
  // in-width: 6, out-width: 3
  val a7 = Wire(Bits(3.W))
  // OH ( Cat ( {JALR, JAL}; {SRAI}; {SRLI}; {SLLI}; {LUI}; {AUIPC}; {SLTIU}; {SLTI}; {XORI}; {ORI}; {ANDI}; {ADDI}; {SRA}; {SRL}; {SLL}; {SLTU}; {SLT}; {XOR}; {OR}; {AND}; {SUB}; {ADD}; {LH, LB, LHU, LW, LBU}; {SH, SB, SW} ) )
  // in-width: 24, out-width: 5
  val a8 = Wire(Bits(5.W))

  // Fallback -> Invalid
  val a9 = Wire(Bool())

  // {LB, SRA, LHU, LBU, ORI, BLTU, SLT, XOR, BNE, SLTI, SRLI, LW, BEQ, SB, SLL, JALR, SRL, BGEU, BGE, ANDI, XORI, OR, SH, BLT, LH, LH, SLLI, SUB, SLTIU, SLTU, AND, ADD, ADDI, SW, SRAI}
  val a10 = Wire(Bool())
  // {SRL, BGEU, BGE, SRA, OR, SH, BLTU, BLT, SLT, XOR, BNE, SUB, SLTU, AND, ADD, SLL, SB, BEQ, SW}
  val a11 = Wire(Bool())

  val input = Wire(Bits(32.W))
  input := io.read_readMEM1_result_in.data.asUInt

  val dec_output = Wire(Bits(28.W))

  // Schema: [signal: width]
  //
  // [sig_152: 1] [sig_147: 1] [sig_214: 1] [a1: 1] [a2: 1] [a10: 1] [a11: 1] [a0: 3] [a3: 3] [a4: 2] [a5: 2] [a6: 2] [a7: 3] [a8: 5] [a9: 1]

  val table = TruthTable(
    Map(
      BitPat("b?0110011?000????????????0000000?") -> BitPat("b 0 0 0 1 0 1 1 000 000 00 00 00 101 00010 0"), // ADD
      BitPat("b?0110011?000????????????0100000?") -> BitPat("b 0 0 0 1 0 1 1 000 000 00 00 00 101 00011 0"), // SUB
      BitPat("b?0110011?111????????????0000000?") -> BitPat("b 0 0 0 1 0 1 1 000 000 00 00 00 101 00100 0"), // AND
      BitPat("b?0110011?110????????????0000000?") -> BitPat("b 0 0 0 1 0 1 1 000 000 00 00 00 101 00101 0"), // OR
      BitPat("b?0110011?100????????????0000000?") -> BitPat("b 0 0 0 1 0 1 1 000 000 00 00 00 101 00110 0"), // XOR
      BitPat("b?0110011?010????????????0000000?") -> BitPat("b 0 0 0 1 0 1 1 000 000 00 00 00 101 00111 0"), // SLT
      BitPat("b?0110011?011????????????0000000?") -> BitPat("b 0 0 0 1 0 1 1 000 000 00 00 00 101 01000 0"), // SLTU
      BitPat("b?0110011?001????????????0000000?") -> BitPat("b 0 0 0 1 0 1 1 000 000 00 00 00 101 01001 0"), // SLL
      BitPat("b?0110011?101????????????0000000?") -> BitPat("b 0 0 0 1 0 1 1 000 000 00 00 00 101 01010 0"), // SRL
      BitPat("b?0110011?101????????????0100000?") -> BitPat("b 0 0 0 1 0 1 1 000 000 00 00 00 101 01011 0"), // SRA
      BitPat("b?0010011?000????????????????????") -> BitPat("b 0 0 0 1 0 1 0 000 000 00 00 00 101 01100 0"), // ADDI
      BitPat("b?0010011?111????????????????????") -> BitPat("b 0 0 0 1 0 1 0 000 000 00 00 00 101 01101 0"), // ANDI
      BitPat("b?0010011?110????????????????????") -> BitPat("b 0 0 0 1 0 1 0 000 000 00 00 00 101 01110 0"), // ORI
      BitPat("b?0010011?100????????????????????") -> BitPat("b 0 0 0 1 0 1 0 000 000 00 00 00 101 01111 0"), // XORI
      BitPat("b?0010011?010????????????????????") -> BitPat("b 0 0 0 1 0 1 0 000 000 00 00 00 101 10000 0"), // SLTI
      BitPat("b?0010011?011????????????????????") -> BitPat("b 0 0 0 1 0 1 0 000 000 00 00 00 101 10001 0"), // SLTIU
      BitPat("b?0010011?001????????????0000000?") -> BitPat("b 0 0 0 1 0 1 0 000 000 00 00 00 101 10100 0"), // SLLI
      BitPat("b?0010011?101????????????0000000?") -> BitPat("b 0 0 0 1 0 1 0 000 000 00 00 00 101 10101 0"), // SRLI
      BitPat("b?0010011?101????????????0100000?") -> BitPat("b 0 0 0 1 0 1 0 000 000 00 00 00 101 10110 0"), // SRAI
      BitPat("b?0010111????????????????????????") -> BitPat("b 0 0 0 1 0 0 0 001 000 00 00 00 101 10010 0"), // AUIPC
      BitPat("b?0110111????????????????????????") -> BitPat("b 0 0 0 1 0 0 0 000 000 00 00 00 101 10011 0"), // LUI
      BitPat("b?0000011?000????????????????????") -> BitPat("b 0 0 1 1 0 1 0 000 000 00 00 00 000 00001 0"), // LB
      BitPat("b?0000011?100????????????????????") -> BitPat("b 0 0 1 1 0 1 0 000 000 00 00 00 001 00001 0"), // LBU
      BitPat("b?0000011?001????????????????????") -> BitPat("b 0 0 1 1 0 1 0 000 000 00 10 00 010 00001 0"), // LH
      BitPat("b?0000011?101????????????????????") -> BitPat("b 0 0 1 1 0 1 0 000 000 00 10 00 011 00001 0"), // LHU
      BitPat("b?0000011?010????????????????????") -> BitPat("b 0 0 1 1 0 1 0 000 000 00 01 00 100 00001 0"), // LW
      BitPat("b?0100011?000????????????????????") -> BitPat("b 0 1 0 0 0 1 1 011 000 00 00 00 000 00000 0"), // SB
      BitPat("b?0100011?001????????????????????") -> BitPat("b 0 1 0 0 0 1 1 011 000 00 00 01 000 00000 0"), // SH
      BitPat("b?0100011?010????????????????????") -> BitPat("b 0 1 0 0 0 1 1 011 000 00 00 10 000 00000 0"), // SW
      BitPat("b?1100011?000????????????????????") -> BitPat("b 1 0 0 0 1 1 1 100 000 00 00 00 000 00000 0"), // BEQ
      BitPat("b?1100011?001????????????????????") -> BitPat("b 1 0 0 0 1 1 1 100 001 00 00 00 000 00000 0"), // BNE
      BitPat("b?1100011?101????????????????????") -> BitPat("b 1 0 0 0 1 1 1 100 010 00 00 00 000 00000 0"), // BGE
      BitPat("b?1100011?111????????????????????") -> BitPat("b 1 0 0 0 1 1 1 100 011 00 00 00 000 00000 0"), // BGEU
      BitPat("b?1100011?100????????????????????") -> BitPat("b 1 0 0 0 1 1 1 100 100 00 00 00 000 00000 0"), // BLT
      BitPat("b?1100011?110????????????????????") -> BitPat("b 1 0 0 0 1 1 1 100 101 00 00 00 000 00000 0"), // BLTU
      BitPat("b?1101111????????????????????????") -> BitPat("b 0 0 0 1 1 0 0 010 110 01 00 00 101 10111 0"), // JAL
      BitPat("b?1100111?000????????????????????") -> BitPat("b 0 0 0 1 1 1 0 000 111 10 00 00 101 10111 0"), // JALR
      BitPat("b01110011000000000000000000000000") -> BitPat("b 0 0 0 0 0 0 0 000 000 00 00 00 000 00000 0"), // ECALL
      BitPat("b01110011000000000001000000000000") -> BitPat("b 0 0 0 0 0 0 0 000 000 00 00 00 000 00000 0")  // EBREAK
    ),
    BitPat("b 0 0 0 0 0 0 0 000 000 00 00 00 000 00000 1")
  )

  dec_output := decoder(input, table)

  sig_152 := dec_output(27, 27)
  sig_147 := dec_output(26, 26)
  sig_214 := dec_output(25, 25)
  a1      := dec_output(24, 24)
  a2      := dec_output(23, 23)
  a10     := dec_output(22, 22)
  a11     := dec_output(21, 21)

  a0      := dec_output(20, 18)
  a3      := dec_output(17, 15)
  a4      := dec_output(14, 13)
  a5      := dec_output(12, 11)
  a6      := dec_output(10, 9)
  a7      := dec_output(8, 6)
  a8      := dec_output(5, 1)

  a9      := dec_output(0, 0)

  // -- Decode end

  when ((io.DECODE_en_in).asBool) {
    rd := io.read_readMEM1_result_in.data.asUInt(11, 7)
  }
  when ((io.DECODE_en_in).asBool) {
    rs2 := io.read_readMEM1_result_in.data.asUInt(24, 20)
  }
  writeX0_value := lsl(io.read_readMEM1_result_in.data.asUInt(31, 12).sext(32.W), "hc".U(4.W))
  when ((io.DECODE_en_in).asBool) {
    readMEM0_addr := writeX0_value
  }
  n_162 := neq(io.read_readMEM1_result_in.data.asUInt(19, 15), "h0".U(5.W))
  when ((io.DECODE_en_in).asBool) {
    rs1_neq_0 := n_162
  }
  n_166 := neq(io.read_readMEM1_result_in.data.asUInt(24, 20), "h0".U(5.W))
  when ((io.DECODE_en_in).asBool) {
    rs2_neq_0 := n_166
  }

  when ((io.DECODE_en_in).asBool) {
    n204 := MuxLookup[Bits](a0, 0.U)(Seq(0.U -> io.read_readMEM1_result_in.data.asUInt(31, 20).sext(32.W), 1.U -> writeX0_value, 2.U -> lsl(Cat(io.read_readMEM1_result_in.data.asUInt(31, 31), io.read_readMEM1_result_in.data.asUInt(19, 12), io.read_readMEM1_result_in.data.asUInt(20, 20), io.read_readMEM1_result_in.data.asUInt(30, 21)).sext(32.W), "h1".U(1.W)), 3.U -> Cat(io.read_readMEM1_result_in.data.asUInt(31, 25), io.read_readMEM1_result_in.data.asUInt(11, 7)).sext(32.W), 4.U -> lsl(Cat(io.read_readMEM1_result_in.data.asUInt(31, 31), io.read_readMEM1_result_in.data.asUInt(7, 7), io.read_readMEM1_result_in.data.asUInt(30, 25), io.read_readMEM1_result_in.data.asUInt(11, 8)).sext(32.W), "h1".U(1.W))))
  }

  when ((io.DECODE_en_in).asBool) {
    writeX0_enable := and(neq(io.read_readMEM1_result_in.data.asUInt(11, 7), "h0".U(5.W)), a1)
  }
  when ((io.DECODE_en_in).asBool) {
    is_JALRBNEBGEUBGEJALBLTUBEQBLT := a2
  }
  when ((io.DECODE_en_in).asBool) {
    is_S := sig_147
  }

  when ((io.DECODE_en_in).asBool) {
    is_L := sig_214
  }

  when ((io.DECODE_en_in).asBool) {
    bin_is_BEQ_is_BNE_is_BGE_is_BG := a3
  }

  when ((io.DECODE_en_in).asBool) {
    sel_writePC0_value := a4
  }

  when ((io.DECODE_en_in).asBool) {
    sel_readMEM0_words := a5
  }

  when ((io.DECODE_en_in).asBool) {
    sel_writeMEM0_words := a6

  }
  when ((io.DECODE_en_in).asBool) {
    sel_writeX0_value := a7
  }

  when ((io.DECODE_en_in).asBool) {
    sel_readMEM0_addr := a8
  }

  when ((io.DECODE_en_in).asBool) {
    not_is_not_none := a9
  }

  rs1_neq_0_and_is_not_AUIPCLUIJ := and(n_162, a10)
  io.readX0_result_in.enable := and(io.read_DECODE_full_in.data.asUInt, and(rs1_neq_0_and_is_not_AUIPCLUIJ, not(io.fwd_X0_en_in)))
  io.readX0_result_in.address := io.read_readMEM1_result_in.data.asUInt(19, 15)
  sig_122 := Mux((io.fwd_X0_en_in).asBool, io.fwd_X0_val_in, io.readX0_result_in.data.asUInt)

  when ((io.DECODE_en_in).asBool) {
    readX0_result := sig_122
  }

  rs2_neq_0_and_is_SRLBGEUBGESRA := and(n_166, a11)
  io.readX1_result_in.enable := and(io.read_DECODE_full_in.data.asUInt, and(rs2_neq_0_and_is_SRLBGEUBGESRA, not(io.fwd_X1_en_in)))
  io.readX1_result_in.address := io.read_readMEM1_result_in.data.asUInt(24, 20)
  sig_128 := Mux((io.fwd_X1_en_in).asBool, io.fwd_X1_val_in, io.readX1_result_in.data.asUInt)

  when ((io.DECODE_en_in).asBool) {
    readX1_result := sig_128
  }

  when ((io.DECODE_en_in).asBool) {
    readMEM1_addr := io.read_readMEM1_addr_in.data.asUInt
  }

  when ((io.DECODE_en_in).asBool) {
    writePC1_value := io.read_writePC1_value_in.data.asUInt
  }

  rs1 := io.read_readMEM1_result_in.data.asUInt(19, 15)
  readX1_addr := io.read_readMEM1_result_in.data.asUInt(24, 20)
  readX0_enable := rs1_neq_0_and_is_not_AUIPCLUIJ
  readX1_enable := rs2_neq_0_and_is_SRLBGEUBGESRA

  when ((rs1_neq_0_and_is_not_AUIPCLUIJ).asBool) {
    //printf(cf"%T ${io.read_readMEM1_addr_in.data.asUInt}%x rd X(${io.read_readMEM1_result_in.data.asUInt(19, 15)}%d) = ${sig_122}%x\n")
  }

  when ((rs2_neq_0_and_is_SRLBGEUBGESRA).asBool) {
    //printf(cf"%T ${io.read_readMEM1_addr_in.data.asUInt}%x rd X(${io.read_readMEM1_result_in.data.asUInt(24, 20)}%d) = ${sig_128}%x\n")
  }

  io.read_bin_is_BEQ_is_BNE_is_BGE_is_BG_out := bin_is_BEQ_is_BNE_is_BGE_is_BG
  io.read_rs1_neq_0_out := rs1_neq_0
  io.read_readX0_result_out := readX0_result
  io.read_rs2_neq_0_out := rs2_neq_0
  io.read_readX1_result_out := readX1_result
  io.read_writePC1_value_0_out := writePC1_value
  io.read_readMEM1_addr_0_out := readMEM1_addr
  io.read_n204_out := n204
  io.read_is_JALRBNEBGEUBGEJALBLTUBEQBLT_out := is_JALRBNEBGEUBGEJALBLTUBEQBLT
  io.read_sel_writePC0_value_out := sel_writePC0_value
  io.read_sel_readMEM0_addr_out := sel_readMEM0_addr
  io.read_readMEM0_addr_out := readMEM0_addr
  io.read_rs2_out := rs2
  io.read_is_S_out := is_S
  io.read_sel_writeMEM0_words_out := sel_writeMEM0_words
  io.read_not_is_not_none_out := not_is_not_none
  io.read_sel_writeX0_value_out := sel_writeX0_value
  io.read_sel_readMEM0_words_out := sel_readMEM0_words
  io.read_is_L_out := is_L
  io.read_writeX0_enable_out := writeX0_enable
  io.read_rd_out := rd
  io.rs1_out := rs1
  io.readX1_addr_out := readX1_addr
  io.readX0_enable_out := readX0_enable
  io.readX1_enable_out := readX1_enable
}

object Main extends App {
  println(
    ChiselStage.emitSystemVerilog(
      gen = new InstructionDecoder,
      firtoolOpts = Array(
        "-disable-all-randomization",
        "--lowering-options=disallowLocalVariables",
        "-strip-debug-info")
    )
  )
}

