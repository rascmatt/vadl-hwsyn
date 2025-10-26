//> using scala "2.13.12"
//> using dep org.chipsalliance::chisel:7.2.0
//> using plugin org.chipsalliance:::chisel-plugin:7.2.0
//> using options "-unchecked", "-deprecation", "-language:reflectiveCalls", "-feature", "-Xcheckinit", "-Xfatal-warnings", "-Ywarn-dead-code", "-Ywarn-unused", "-Ymacro-annotations"


import _root_.circt.stage.ChiselStage

import chisel3._
import chisel3.util.BitPat

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

  /*
  val is_BNE = Wire(Bool())
  val is_BGEU = Wire(Bool())
  val is_BGE = Wire(Bool())
  val is_BLTU = Wire(Bool())
  val is_BEQ = Wire(Bool())
  val is_BLT = Wire(Bool())

  val sig_152 = Wire(Bits(1.W))

  val is_SH = Wire(Bool())
  val is_SB = Wire(Bool())
  val is_SW = Wire(Bool())

  val sig_147 = Wire(Bits(1.W))

  val is_JAL = Wire(Bool())
  val is_AUIPC = Wire(Bool())
  val is_LH = Wire(Bool())
  val is_JALR = Wire(Bool())
  val is_SLTI = Wire(Bool())
  val is_LB = Wire(Bool())
  val is_ANDI = Wire(Bool())
  val is_SLTIU = Wire(Bool())
  val is_XORI = Wire(Bool())
  val is_LHU = Wire(Bool())
  val is_LW = Wire(Bool())
  val is_LBU = Wire(Bool())
  val is_ADDI = Wire(Bool())
  val is_ORI = Wire(Bool())
  val is_SRA = Wire(Bool())
  val is_SLT = Wire(Bool())
  val is_XOR = Wire(Bool())
  val is_SRLI = Wire(Bool())
  val is_SLL = Wire(Bool())
  val is_SRL = Wire(Bool())
  val is_LUI = Wire(Bool())
  val is_OR = Wire(Bool())
  val is_SLLI = Wire(Bool())
  val is_SUB = Wire(Bool())
  val is_SLTU = Wire(Bool())
  val is_AND = Wire(Bool())
  val is_ADD = Wire(Bool())
  val is_SRAI = Wire(Bool())
  val sig_214 = Wire(Bits(1.W))
  val is_ECALL = Wire(Bool())
  val is_EBREAK = Wire(Bool())*/

  // {BNE, BGEU, BGE, BLTU, BEQ, BLT}
  sig_152 := or(or(or(or(or(is_BNE, is_BGEU), is_BGE), is_BLTU), is_BEQ), is_BLT)
  // {SH, SB, SW}
  sig_147 := or(or(is_SH, is_SB), is_SW)
  // OH ( Cat ( {BNE, BGEU, BGE, BLTU, BEQ, BLT}; {SH, SB, SW}; {JAL}; {AUIPC}; {LH, JALR, SLTI, LB, ANDI, SLTIU, XORI, LHU, LW, LBU, ADDI, ORI} ) )
  // in-width: 5, out-width 3
  val a0 := OHToUInt(Cat(sig_152, sig_147, is_JAL, is_AUIPC, or(or(or(or(or(or(or(or(or(or(or(is_LH, is_JALR), is_SLTI), is_LB), is_ANDI), is_SLTIU), is_XORI), is_LHU), is_LW), is_LBU), is_ADDI), is_ORI)))

  // {LB, JAL, SRA, LHU, LBU, ORI, SLT, XOR, SLTI, SRLI, LW, SLL, SRL, JALR, LUI, ANDI, XORI, OR, LH, SLLI, SUB, SLTIU, SLTU, AND, ADD, AUIPC, ADDI, SRAI}
  val a1 := or(or(or(or(or(or(or(or(or(or(or(or(or(or(or(or(or(or(or(or(or(or(or(or(or(or(or(is_LB, is_JAL), is_SRA), is_LHU), is_LBU), is_ORI), is_SLT), is_XOR), is_SLTI), is_SRLI), is_LW), is_SLL), is_SRL), is_JALR), is_LUI), is_ANDI), is_XORI), is_OR), is_LH), is_SLLI), is_SUB), is_SLTIU), is_SLTU), is_AND), is_ADD), is_AUIPC), is_ADDI), is_SRAI)
  // {JALR, BNE, BGEU, BGE, JAL, BLTU, BEQ, BLT}
  val a2 := or(or(or(or(or(or(or(is_JALR, is_BNE), is_BGEU), is_BGE), is_JAL), is_BLTU), is_BEQ), is_BLT)
  // {LH, LB, LHU, LW, LBU}
  sig_214 := or(or(or(or(is_LH, is_LB), is_LHU), is_LW), is_LBU)

  // OH ( Cat ( {JALR}; {JAL}; {BLTU}; {BLT}; {BGEU}; {BGE}; {BNE}; {BEQ} ) )
  // in-width: 8, out-width: 3
  val a3 := OHToUInt(Cat(is_JALR, is_JAL, is_BLTU, is_BLT, is_BGEU, is_BGE, is_BNE, is_BEQ))
  // OH ( Cat ( {JALR}; {JAL}; {BNE, BGEU, BGE, BLTU, BEQ, BLT} ) )
  // in-width: 3, out-width: 2
  val a4 := OHToUInt(Cat(is_JALR, is_JAL, sig_152))
  // OH ( Cat ( {LH, LHU}; {LW}; {LB, LBU} ) )
  // in-width: 3, out-width: 2
  val a5 := OHToUInt(Cat(or(is_LH, is_LHU), is_LW, or(is_LB, is_LBU)))
  // OH ( Cat ( {SW}; {SH}; {SB} ) )
  // in-width: 3, out-width: 2
  val a6 := OHToUInt(Cat(is_SW, is_SH, is_SB))
  // OH ( Cat ( {SRL, JALR, LUI, JAL, ANDI, SRA, XORI, OR, ORI, SLT, SLLI, XOR, SLTI, SUB, SLTIU, SLTU, SRLI, AND, ADD, AUIPC, ADDI, SLL, SRAI}; {LW}; {LHU}; {LH}; {LBU}; {LB} ) )
  // in-width: 6, out-width: 3
  val a7 := OHToUInt(Cat(or(or(or(or(or(or(or(or(or(or(or(or(or(or(or(or(or(or(or(or(or(or(is_SRL, is_JALR), is_LUI), is_JAL), is_ANDI), is_SRA), is_XORI), is_OR), is_ORI), is_SLT), is_SLLI), is_XOR), is_SLTI), is_SUB), is_SLTIU), is_SLTU), is_SRLI), is_AND), is_ADD), is_AUIPC), is_ADDI), is_SLL), is_SRAI), is_LW, is_LHU, is_LH, is_LBU, is_LB))
  // OH ( Cat ( {JALR, JAL}; {SRAI}; {SRLI}; {SLLI}; {LUI}; {AUIPC}; {SLTIU}; {SLTI}; {XORI}; {ORI}; {ANDI}; {ADDI}; {SRA}; {SRL}; {SLL}; {SLTU}; {SLT}; {XOR}; {OR}; {AND}; {SUB}; {ADD}; {LH, LB, LHU, LW, LBU}; {SH, SB, SW} ) )
  // in-width: 24, out-width: 5
  val a8 := OHToUInt(Cat(or(is_JALR, is_JAL), is_SRAI, is_SRLI, is_SLLI, is_LUI, is_AUIPC, is_SLTIU, is_SLTI, is_XORI, is_ORI, is_ANDI, is_ADDI, is_SRA, is_SRL, is_SLL, is_SLTU, is_SLT, is_XOR, is_OR, is_AND, is_SUB, is_ADD, sig_214, sig_147))

  // Fallback -> Invalid
  val a9 := not(or(or(or(or(or(or(or(or(or(or(or(or(or(or(or(or(or(or(or(or(or(or(or(or(or(or(or(or(or(or(or(or(or(or(or(or(or(or(is_ADD, is_SUB), is_AND), is_OR), is_XOR), is_SLT), is_SLTU), is_SLL), is_SRL), is_SRA), is_ADDI), is_ANDI), is_ORI), is_XORI), is_SLTI), is_SLTIU), is_AUIPC), is_LUI), is_LB), is_LBU), is_LH), is_LHU), is_LW), is_SB), is_SH), is_SW), is_BEQ), is_BNE), is_BGE), is_BGEU), is_BLT), is_BLTU), is_JAL), is_JALR), is_SLLI), is_SRLI), is_SRAI), is_ECALL), is_EBREAK))

  // {LB, SRA, LHU, LBU, ORI, BLTU, SLT, XOR, BNE, SLTI, SRLI, LW, BEQ, SB, SLL, JALR, SRL, BGEU, BGE, ANDI, XORI, OR, SH, BLT, LH, LH, SLLI, SUB, SLTIU, SLTU, AND, ADD, ADDI, SW, SRAI}
  val a10 := or(or(or(or(or(or(or(or(or(or(or(or(or(or(or(or(or(or(or(or(or(or(or(or(or(or(or(or(or(or(or(or(or(is_LB, is_SRA), is_LHU), is_LBU), is_ORI), is_BLTU), is_SLT), is_XOR), is_BNE), is_SLTI), is_SRLI), is_LW), is_BEQ), is_SB), is_SLL), is_JALR), is_SRL), is_BGEU), is_BGE), is_ANDI), is_XORI), is_OR), is_SH), is_BLT), is_LH), is_SLLI), is_SUB), is_SLTIU), is_SLTU), is_AND), is_ADD), is_ADDI), is_SW), is_SRAI)
  // {SRL, BGEU, BGE, SRA, OR, SH, BLTU, BLT, SLT, XOR, BNE, SUB, SLTU, AND, ADD, SLL, SB, BEQ, SW}
  val a11 := or(or(or(or(or(or(or(or(or(or(or(or(or(or(or(or(or(or(is_SRL, is_BGEU), is_BGE), is_SRA), is_OR), is_SH), is_BLTU), is_BLT), is_SLT), is_XOR), is_BNE), is_SUB), is_SLTU), is_AND), is_ADD), is_SLL), is_SB), is_BEQ), is_SW)

  when(input === BitPat("b0110011????????????????????????")) {
    when(input === BitPat("b000????????????0000000?")) {

      // ADD

      // 1 bit wires
      sig_152 := false.B
      sig_147 := false.B
      sig_214 := false.B
      a1      := true.B
      a2      := false.B
      a10     := true.B
      a11     := true.B

      // k bit wires
      a0      := 0.U
      a3      := 0.U
      a4      := 0.U
      a5      := 0.U
      a6      := 0.U
      a7      := 5.U
      a8      := 2.U

      // Invalid
      a9      := false.B

    }.elsewhen(input === BitPat("b000????????????0100000?")) {
      // SUB

      // 1 bit wires
      sig_152 := false.B
      sig_147 := false.B
      sig_214 := false.B
      a1      := true.B
      a2      := false.B
      a10     := true.B
      a11     := true.B

      // k bit wires
      a0      := 0.U
      a3      := 0.U
      a4      := 0.U
      a5      := 0.U
      a6      := 0.U
      a7      := 5.U
      a8      := 3.U

      // Invalid
      a9      := false.B
    }.elsewhen(input === BitPat("b111????????????0000000?")) {
      // AND

      // 1 bit wires
      sig_152 := false.B
      sig_147 := false.B
      sig_214 := false.B
      a1      := true.B
      a2      := false.B
      a10     := true.B
      a11     := true.B

      // k bit wires
      a0      := 0.U
      a3      := 0.U
      a4      := 0.U
      a5      := 0.U
      a6      := 0.U
      a7      := 5.U
      a8      := 4.U

      // Invalid
      a9      := false.B
    }.elsewhen(input === BitPat("b110????????????0000000?")) {
      // OR

      // 1 bit wires
      sig_152 := false.B
      sig_147 := false.B
      sig_214 := false.B
      a1      := true.B
      a2      := false.B
      a10     := true.B
      a11     := true.B

      // k bit wires
      a0      := 0.U
      a3      := 0.U
      a4      := 0.U
      a5      := 0.U
      a6      := 0.U
      a7      := 5.U
      a8      := 5.U

      // Invalid
      a9      := false.B
    }.elsewhen(input === BitPat("b100????????????0000000?")) {
      // XOR

      // 1 bit wires
      sig_152 := false.B
      sig_147 := false.B
      sig_214 := false.B
      a1      := true.B
      a2      := false.B
      a10     := true.B
      a11     := true.B

      // k bit wires
      a0      := 0.U
      a3      := 0.U
      a4      := 0.U
      a5      := 0.U
      a6      := 0.U
      a7      := 5.U
      a8      := 6.U

      // Invalid
      a9      := false.B
    }.elsewhen(input === BitPat("b010????????????0000000?")) {
      // SLT

      // 1 bit wires
      sig_152 := false.B
      sig_147 := false.B
      sig_214 := false.B
      a1      := true.B
      a2      := false.B
      a10     := true.B
      a11     := true.B

      // k bit wires
      a0      := 0.U
      a3      := 0.U
      a4      := 0.U
      a5      := 0.U
      a6      := 0.U
      a7      := 5.U
      a8      := 7.U

      // Invalid
      a9      := false.B
    }.elsewhen(input === BitPat("b011????????????0000000?")) {
      // SLTU

      // 1 bit wires
      sig_152 := false.B
      sig_147 := false.B
      sig_214 := false.B
      a1      := true.B
      a2      := false.B
      a10     := true.B
      a11     := true.B

      // k bit wires
      a0      := 0.U
      a3      := 0.U
      a4      := 0.U
      a5      := 0.U
      a6      := 0.U
      a7      := 5.U
      a8      := 8.U

      // Invalid
      a9      := false.B
    }.elsewhen(input === BitPat("b001????????????0000000?")) {
      // SLL

      // 1 bit wires
      sig_152 := false.B
      sig_147 := false.B
      sig_214 := false.B
      a1      := true.B
      a2      := false.B
      a10     := true.B
      a11     := true.B

      // k bit wires
      a0      := 0.U
      a3      := 0.U
      a4      := 0.U
      a5      := 0.U
      a6      := 0.U
      a7      := 5.U
      a8      := 9.U

      // Invalid
      a9      := false.B
    }.elsewhen(input === BitPat("b101????????????0000000?")) {
      // SRL

      // 1 bit wires
      sig_152 := false.B
      sig_147 := false.B
      sig_214 := false.B
      a1      := true.B
      a2      := false.B
      a10     := true.B
      a11     := true.B

      // k bit wires
      a0      := 0.U
      a3      := 0.U
      a4      := 0.U
      a5      := 0.U
      a6      := 0.U
      a7      := 5.U
      a8      := 10.U

      // Invalid
      a9      := false.B
    }.elsewhen(input === BitPat("b101????????????0100000?")) {
      // SRA

      // 1 bit wires
      sig_152 := false.B
      sig_147 := false.B
      sig_214 := false.B
      a1      := true.B
      a2      := false.B
      a10     := true.B
      a11     := true.B

      // k bit wires
      a0      := 0.U
      a3      := 0.U
      a4      := 0.U
      a5      := 0.U
      a6      := 0.U
      a7      := 5.U
      a8      := 11.U

      // Invalid
      a9      := false.B
    }.otherwise {
      // invalid

      // 1 bit wires
      sig_152 := false.B
      sig_147 := false.B
      sig_214 := false.B
      a1      := false.B
      a2      := false.B
      a10     := false.B
      a11     := false.B

      // k bit wires
      a0      := 0.U
      a3      := 0.U
      a4      := 0.U
      a5      := 0.U
      a6      := 0.U
      a7      := 0.U
      a8      := 0.U

      a9      := true.B
    }
  }.elsewhen(input === BitPat("b0010011????????????????????????")) {
    when(input === BitPat("b000????????????????????")) {
      // ADDI

      // 1 bit wires
      sig_152 := false.B
      sig_147 := false.B
      sig_214 := false.B
      a1      := true.B
      a2      := false.B
      a10     := true.B
      a11     := false.B

      // k bit wires
      a0      := 0.U
      a3      := 0.U
      a4      := 0.U
      a5      := 0.U
      a6      := 0.U
      a7      := 5.U
      a8      := 12.U

      // Invalid
      a9      := false.B
    }.elsewhen(input === BitPat("b111????????????????????")) {
      // ANDI

      // 1 bit wires
      sig_152 := false.B
      sig_147 := false.B
      sig_214 := false.B
      a1      := true.B
      a2      := false.B
      a10     := true.B
      a11     := false.B

      // k bit wires
      a0      := 0.U
      a3      := 0.U
      a4      := 0.U
      a5      := 0.U
      a6      := 0.U
      a7      := 5.U
      a8      := 13.U

      // Invalid
      a9      := false.B
    }.elsewhen(input === BitPat("b110????????????????????")) {
      // ORI

      // 1 bit wires
      sig_152 := false.B
      sig_147 := false.B
      sig_214 := false.B
      a1      := true.B
      a2      := false.B
      a10     := true.B
      a11     := false.B

      // k bit wires
      a0      := 0.U
      a3      := 0.U
      a4      := 0.U
      a5      := 0.U
      a6      := 0.U
      a7      := 5.U
      a8      := 14.U

      // Invalid
      a9      := false.B
    }.elsewhen(input === BitPat("b100????????????????????")) {
      // XORI

      // 1 bit wires
      sig_152 := false.B
      sig_147 := false.B
      sig_214 := false.B
      a1      := true.B
      a2      := false.B
      a10     := true.B
      a11     := false.B

      // k bit wires
      a0      := 0.U
      a3      := 0.U
      a4      := 0.U
      a5      := 0.U
      a6      := 0.U
      a7      := 5.U
      a8      := 15.U

      // Invalid
      a9      := false.B
    }.elsewhen(input === BitPat("b010????????????????????")) {
      // SLTI

      // 1 bit wires
      sig_152 := false.B
      sig_147 := false.B
      sig_214 := false.B
      a1      := true.B
      a2      := false.B
      a10     := true.B
      a11     := false.B

      // k bit wires
      a0      := 0.U
      a3      := 0.U
      a4      := 0.U
      a5      := 0.U
      a6      := 0.U
      a7      := 5.U
      a8      := 16.U

      // Invalid
      a9      := false.B
    }.elsewhen(input === BitPat("b011????????????????????")) {
      // SLTIU

      // 1 bit wires
      sig_152 := false.B
      sig_147 := false.B
      sig_214 := false.B
      a1      := true.B
      a2      := false.B
      a10     := true.B
      a11     := false.B

      // k bit wires
      a0      := 0.U
      a3      := 0.U
      a4      := 0.U
      a5      := 0.U
      a6      := 0.U
      a7      := 5.U
      a8      := 17.U

      // Invalid
      a9      := false.B
    }.elsewhen(input === BitPat("b001????????????????????")) {
      when(input === BitPat("b0000000?")) {
        // SLLI

      // 1 bit wires
      sig_152 := false.B
      sig_147 := false.B
      sig_214 := false.B
      a1      := true.B
      a2      := false.B
      a10     := true.B
      a11     := false.B

      // k bit wires
      a0      := 0.U
      a3      := 0.U
      a4      := 0.U
      a5      := 0.U
      a6      := 0.U
      a7      := 5.U
      a8      := 20.U

      // Invalid
      a9      := false.B
      }.otherwise {
        // invalid

        // 1 bit wires
        sig_152 := false.B
        sig_147 := false.B
        sig_214 := false.B
        a1      := false.B
        a2      := false.B
        a10     := false.B
        a11     := false.B

        // k bit wires
        a0      := 0.U
        a3      := 0.U
        a4      := 0.U
        a5      := 0.U
        a6      := 0.U
        a7      := 0.U
        a8      := 0.U

        a9      := true.B
      }
    }.elsewhen(input === BitPat("b101????????????????????")) {
      when(input === BitPat("b0000000?")) {
        // SRLI

        // 1 bit wires
        sig_152 := false.B
        sig_147 := false.B
        sig_214 := false.B
        a1      := true.B
        a2      := false.B
        a10     := true.B
        a11     := false.B

        // k bit wires
        a0      := 0.U
        a3      := 0.U
        a4      := 0.U
        a5      := 0.U
        a6      := 0.U
        a7      := 5.U
        a8      := 21.U

        // Invalid
        a9      := false.B
      }.elsewhen(input === BitPat("b0100000?")) {
        // SRAI

        // 1 bit wires
        sig_152 := false.B
        sig_147 := false.B
        sig_214 := false.B
        a1      := true.B
        a2      := false.B
        a10     := true.B
        a11     := false.B

        // k bit wires
        a0      := 0.U
        a3      := 0.U
        a4      := 0.U
        a5      := 0.U
        a6      := 0.U
        a7      := 5.U
        a8      := 22.U

        // Invalid
        a9      := false.B
      }.otherwise {
        // invalid

        // 1 bit wires
        sig_152 := false.B
        sig_147 := false.B
        sig_214 := false.B
        a1      := false.B
        a2      := false.B
        a10     := false.B
        a11     := false.B

        // k bit wires
        a0      := 0.U
        a3      := 0.U
        a4      := 0.U
        a5      := 0.U
        a6      := 0.U
        a7      := 0.U
        a8      := 0.U

        a9      := true.B
      }
    }.otherwise {
      // invalid

      // 1 bit wires
      sig_152 := false.B
      sig_147 := false.B
      sig_214 := false.B
      a1      := false.B
      a2      := false.B
      a10     := false.B
      a11     := false.B

      // k bit wires
      a0      := 0.U
      a3      := 0.U
      a4      := 0.U
      a5      := 0.U
      a6      := 0.U
      a7      := 0.U
      a8      := 0.U

      a9      := true.B
    }
  }.elsewhen(input === BitPat("b0010111????????????????????????")) {
    // AUIPC

    // 1 bit wires
    sig_152 := false.B
    sig_147 := false.B
    sig_214 := false.B
    a1      := true.B
    a2      := false.B
    a10     := false.B
    a11     := false.B

    // k bit wires
    a0      := 1.U
    a3      := 0.U
    a4      := 0.U
    a5      := 0.U
    a6      := 0.U
    a7      := 5.U
    a8      := 18.U

    // Invalid
    a9      := false.B
  }.elsewhen(input === BitPat("b0110111????????????????????????")) {
    // LUI

    // 1 bit wires
    sig_152 := false.B
    sig_147 := false.B
    sig_214 := false.B
    a1      := true.B
    a2      := false.B
    a10     := false.B
    a11     := false.B

    // k bit wires
    a0      := 0.U
    a3      := 0.U
    a4      := 0.U
    a5      := 0.U
    a6      := 0.U
    a7      := 5.U
    a8      := 19.U

    // Invalid
    a9      := false.B
  }.elsewhen(input === BitPat("b0000011????????????????????????")) {
    when(input === BitPat("b000????????????????????")) {
      // LB

      // 1 bit wires
      sig_152 := false.B
      sig_147 := false.B
      sig_214 := true.B
      a1      := true.B
      a2      := false.B
      a10     := true.B
      a11     := false.B

      // k bit wires
      a0      := 0.U
      a3      := 0.U
      a4      := 0.U
      a5      := 0.U
      a6      := 0.U
      a7      := 0.U
      a8      := 1.U

      // Invalid
      a9      := false.B
    }.elsewhen(input === BitPat("b100????????????????????")) {
      // LBU

      // 1 bit wires
      sig_152 := false.B
      sig_147 := false.B
      sig_214 := true.B
      a1      := true.B
      a2      := false.B
      a10     := true.B
      a11     := false.B

      // k bit wires
      a0      := 0.U
      a3      := 0.U
      a4      := 0.U
      a5      := 0.U
      a6      := 0.U
      a7      := 1.U
      a8      := 1.U

      // Invalid
      a9      := false.B
    }.elsewhen(input === BitPat("b001????????????????????")) {
      // LH

      // 1 bit wires
      sig_152 := false.B
      sig_147 := false.B
      sig_214 := true.B
      a1      := true.B
      a2      := false.B
      a10     := true.B
      a11     := false.B

      // k bit wires
      a0      := 0.U
      a3      := 0.U
      a4      := 0.U
      a5      := 2.U
      a6      := 0.U
      a7      := 2.U
      a8      := 1.U

      // Invalid
      a9      := false.B
    }.elsewhen(input === BitPat("b101????????????????????")) {
      // LHU

      // 1 bit wires
      sig_152 := false.B
      sig_147 := false.B
      sig_214 := true.B
      a1      := true.B
      a2      := false.B
      a10     := true.B
      a11     := false.B

      // k bit wires
      a0      := 0.U
      a3      := 0.U
      a4      := 0.U
      a5      := 2.U
      a6      := 0.U
      a7      := 3.U
      a8      := 1.U

      // Invalid
      a9      := false.B
    }.elsewhen(input === BitPat("b010????????????????????")) {
      // LW

      // 1 bit wires
      sig_152 := false.B
      sig_147 := false.B
      sig_214 := true.B
      a1      := true.B
      a2      := false.B
      a10     := true.B
      a11     := false.B

      // k bit wires
      a0      := 0.U
      a3      := 0.U
      a4      := 0.U
      a5      := 1.U
      a6      := 0.U
      a7      := 4.U
      a8      := 1.U

      // Invalid
      a9      := false.B
    }.otherwise {
      // invalid

      // 1 bit wires
      sig_152 := false.B
      sig_147 := false.B
      sig_214 := false.B
      a1      := false.B
      a2      := false.B
      a10     := false.B
      a11     := false.B

      // k bit wires
      a0      := 0.U
      a3      := 0.U
      a4      := 0.U
      a5      := 0.U
      a6      := 0.U
      a7      := 0.U
      a8      := 0.U

      a9      := true.B
    }
  }.elsewhen(input === BitPat("b0100011????????????????????????")) {
    when(input === BitPat("b000????????????????????")) {
      // SB

      // 1 bit wires
      sig_152 := false.B
      sig_147 := true.B
      sig_214 := false.B
      a1      := false.B
      a2      := false.B
      a10     := true.B
      a11     := true.B

      // k bit wires
      a0      := 3.U
      a3      := 0.U
      a4      := 0.U
      a5      := 0.U
      a6      := 0.U
      a7      := 0.U
      a8      := 0.U

      // Invalid
      a9      := false.B
    }.elsewhen(input === BitPat("b001????????????????????")) {
      // SH

      // 1 bit wires
      sig_152 := false.B
      sig_147 := true.B
      sig_214 := false.B
      a1      := false.B
      a2      := false.B
      a10     := true.B
      a11     := true.B

      // k bit wires
      a0      := 3.U
      a3      := 0.U
      a4      := 0.U
      a5      := 0.U
      a6      := 1.U
      a7      := 0.U
      a8      := 0.U

      // Invalid
      a9      := false.B
    }.elsewhen(input === BitPat("b010????????????????????")) {
      // SW

      // 1 bit wires
      sig_152 := false.B
      sig_147 := true.B
      sig_214 := false.B
      a1      := false.B
      a2      := false.B
      a10     := true.B
      a11     := true.B

      // k bit wires
      a0      := 3.U
      a3      := 0.U
      a4      := 0.U
      a5      := 0.U
      a6      := 2.U
      a7      := 0.U
      a8      := 0.U

      // Invalid
      a9      := false.B
    }.otherwise {
      // invalid

      // 1 bit wires
      sig_152 := false.B
      sig_147 := false.B
      sig_214 := false.B
      a1      := false.B
      a2      := false.B
      a10     := false.B
      a11     := false.B

      // k bit wires
      a0      := 0.U
      a3      := 0.U
      a4      := 0.U
      a5      := 0.U
      a6      := 0.U
      a7      := 0.U
      a8      := 0.U

      a9      := true.B
    }
  }.elsewhen(input === BitPat("b1100011????????????????????????")) {
    when(input === BitPat("b000????????????????????")) {
      // BEQ

      // 1 bit wires
      sig_152 := true.B
      sig_147 := false.B
      sig_214 := false.B
      a1      := false.B
      a2      := true.B
      a10     := true.B
      a11     := true.B

      // k bit wires
      a0      := 4.U
      a3      := 0.U
      a4      := 0.U
      a5      := 0.U
      a6      := 0.U
      a7      := 0.U
      a8      := 0.U

      // Invalid
      a9      := false.B
    }.elsewhen(input === BitPat("b001????????????????????")) {
      // BNE

      // 1 bit wires
      sig_152 := true.B
      sig_147 := false.B
      sig_214 := false.B
      a1      := false.B
      a2      := true.B
      a10     := true.B
      a11     := true.B

      // k bit wires
      a0      := 4.U
      a3      := 1.U
      a4      := 0.U
      a5      := 0.U
      a6      := 0.U
      a7      := 0.U
      a8      := 0.U

      // Invalid
      a9      := false.B
    }.elsewhen(input === BitPat("b101????????????????????")) {
      // BGE

      // 1 bit wires
      sig_152 := true.B
      sig_147 := false.B
      sig_214 := false.B
      a1      := false.B
      a2      := true.B
      a10     := true.B
      a11     := true.B

      // k bit wires
      a0      := 4.U
      a3      := 2.U
      a4      := 0.U
      a5      := 0.U
      a6      := 0.U
      a7      := 0.U
      a8      := 0.U

      // Invalid
      a9      := false.B
    }.elsewhen(input === BitPat("b111????????????????????")) {
      // BGEU

      // 1 bit wires
      sig_152 := true.B
      sig_147 := false.B
      sig_214 := false.B
      a1      := false.B
      a2      := true.B
      a10     := true.B
      a11     := true.B

      // k bit wires
      a0      := 4.U
      a3      := 3.U
      a4      := 0.U
      a5      := 0.U
      a6      := 0.U
      a7      := 0.U
      a8      := 0.U

      // Invalid
      a9      := false.B
    }.elsewhen(input === BitPat("b100????????????????????")) {
      // BLT

      // 1 bit wires
      sig_152 := true.B
      sig_147 := false.B
      sig_214 := false.B
      a1      := false.B
      a2      := true.B
      a10     := true.B
      a11     := true.B

      // k bit wires
      a0      := 4.U
      a3      := 4.U
      a4      := 0.U
      a5      := 0.U
      a6      := 0.U
      a7      := 0.U
      a8      := 0.U

      // Invalid
      a9      := false.B
    }.elsewhen(input === BitPat("b110????????????????????")) {
      // BLTU

      // 1 bit wires
      sig_152 := true.B
      sig_147 := false.B
      sig_214 := false.B
      a1      := false.B
      a2      := true.B
      a10     := true.B
      a11     := true.B

      // k bit wires
      a0      := 4.U
      a3      := 5.U
      a4      := 0.U
      a5      := 0.U
      a6      := 0.U
      a7      := 0.U
      a8      := 0.U

      // Invalid
      a9      := false.B
    }.otherwise {
      // invalid

      // 1 bit wires
      sig_152 := false.B
      sig_147 := false.B
      sig_214 := false.B
      a1      := false.B
      a2      := false.B
      a10     := false.B
      a11     := false.B

      // k bit wires
      a0      := 0.U
      a3      := 0.U
      a4      := 0.U
      a5      := 0.U
      a6      := 0.U
      a7      := 0.U
      a8      := 0.U

      a9      := true.B
    }
  }.elsewhen(input === BitPat("b1101111????????????????????????")) {
    // JAL

    // 1 bit wires
    sig_152 := false.B
    sig_147 := false.B
    sig_214 := false.B
    a1      := true.B
    a2      := true.B
    a10     := false.B
    a11     := false.B

    // k bit wires
    a0      := 2.U
    a3      := 6.U
    a4      := 1.U
    a5      := 0.U
    a6      := 0.U
    a7      := 5.U
    a8      := 23.U

    // Invalid
    a9      := false.B
  }.elsewhen(input === BitPat("b1100111????????????????????????")) {
    when(input === BitPat("b000????????????????????")) {
      // JALR

      // 1 bit wires
      sig_152 := false.B
      sig_147 := false.B
      sig_214 := false.B
      a1      := true.B
      a2      := true.B
      a10     := true.B
      a11     := false.B

      // k bit wires
      a0      := 0.U
      a3      := 7.U
      a4      := 2.U
      a5      := 0.U
      a6      := 0.U
      a7      := 5.U
      a8      := 23.U

      // Invalid
      a9      := false.B
    }.otherwise {
      // invalid

      // 1 bit wires
      sig_152 := false.B
      sig_147 := false.B
      sig_214 := false.B
      a1      := false.B
      a2      := false.B
      a10     := false.B
      a11     := false.B

      // k bit wires
      a0      := 0.U
      a3      := 0.U
      a4      := 0.U
      a5      := 0.U
      a6      := 0.U
      a7      := 0.U
      a8      := 0.U

      a9      := true.B
    }
  }.elsewhen(input === BitPat("b1110011????????????????????????")) {
    when(input === BitPat("b0???????000000000000000000000000")) {
      // ECALL

      // 1 bit wires
      sig_152 := false.B
      sig_147 := false.B
      sig_214 := false.B
      a1      := false.B
      a2      := false.B
      a10     := false.B
      a11     := false.B

      // k bit wires
      a0      := 0.U
      a3      := 0.U
      a4      := 0.U
      a5      := 0.U
      a6      := 0.U
      a7      := 0.U
      a8      := 0.U

      // Invalid
      a9      := false.B
    }.elsewhen(input === BitPat("b0???????000000000001000000000000")) {
      // EBREAK

      // 1 bit wires
      sig_152 := false.B
      sig_147 := false.B
      sig_214 := false.B
      a1      := false.B
      a2      := false.B
      a10     := false.B
      a11     := false.B

      // k bit wires
      a0      := 0.U
      a3      := 0.U
      a4      := 0.U
      a5      := 0.U
      a6      := 0.U
      a7      := 0.U
      a8      := 0.U

      // Invalid
      a9      := false.B
    }.otherwise {
      // invalid

      // 1 bit wires
      sig_152 := false.B
      sig_147 := false.B
      sig_214 := false.B
      a1      := false.B
      a2      := false.B
      a10     := false.B
      a11     := false.B

      // k bit wires
      a0      := 0.U
      a3      := 0.U
      a4      := 0.U
      a5      := 0.U
      a6      := 0.U
      a7      := 0.U
      a8      := 0.U

      a9      := true.B
    }
  }.otherwise {
    // invalid

      // 1 bit wires
      sig_152 := false.B
      sig_147 := false.B
      sig_214 := false.B
      a1      := false.B
      a2      := false.B
      a10     := false.B
      a11     := false.B

      // k bit wires
      a0      := 0.U
      a3      := 0.U
      a4      := 0.U
      a5      := 0.U
      a6      := 0.U
      a7      := 0.U
      a8      := 0.U

      a9      := true.B
  }

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
      firtoolOpts = Array("-disable-all-randomization", "-strip-debug-info")
    )
  )
}

