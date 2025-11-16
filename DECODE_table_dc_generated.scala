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

class DECODE extends Module {

  class IO extends Bundle {
    val read_readMEM1_result_in = Flipped(new VADL.RegReadPort(Bits(32.W)))
    val DECODE_en_in = Input(Bool())
    val fwd_X0_en_in = Input(Bool())
    val fwd_X0_val_in = Input(Bits(32.W))
    val readX0_result_in = Flipped(new VADL.RegFileReadPort(Bits(32.W), 5))
    val read_DECODE_full_in = Flipped(new VADL.RegReadPort(Bits(1.W)))
    val fwd_X1_en_in = Input(Bool())
    val fwd_X1_val_in = Input(Bits(32.W))
    val readX1_result_in = Flipped(new VADL.RegFileReadPort(Bits(32.W), 5))
    val read_readMEM1_addr_in = Flipped(new VADL.RegReadPort(Bits(32.W)))
    val read_writePC1_value_in = Flipped(new VADL.RegReadPort(Bits(32.W)))
    val read_n290_out = new VADL.RegReadPort(Bits(3.W))
    val read_rs1_neq_0_out = new VADL.RegReadPort(Bits(1.W))
    val read_readX0_result_out = new VADL.RegReadPort(Bits(32.W))
    val read_rs2_neq_0_out = new VADL.RegReadPort(Bits(1.W))
    val read_readX1_result_out = new VADL.RegReadPort(Bits(32.W))
    val read_writePC1_value_0_out = new VADL.RegReadPort(Bits(32.W))
    val read_readMEM1_addr_0_out = new VADL.RegReadPort(Bits(32.W))
    val read_n246_out = new VADL.RegReadPort(Bits(32.W))
    val read_is_BGEJALRBLTBLTUBEQBNEBGEUJAL_out = new VADL.RegReadPort(Bits(1.W))
    val read_bin_writePC0_value_out = new VADL.RegReadPort(Bits(2.W))
    val read_sel_readMEM0_addr_out = new VADL.RegReadPort(Bits(5.W))
    val read_readMEM0_addr_out = new VADL.RegReadPort(Bits(32.W))
    val read_rs2_out = new VADL.RegReadPort(Bits(5.W))
    val read_is_S_out = new VADL.RegReadPort(Bits(1.W))
    val read_sel_writeMEM0_words_out = new VADL.RegReadPort(Bits(2.W))
    val read_invalid_insn_out = new VADL.RegReadPort(Bits(1.W))
    val read_bin_writeX0_value_out = new VADL.RegReadPort(Bits(3.W))
    val read_bin_readMEM0_words_out = new VADL.RegReadPort(Bits(2.W))
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
  val n246 = RegInit(0.U.asTypeOf(Bits(32.W)))
  val writeX0_enable = RegInit(0.U.asTypeOf(Bits(1.W)))
  val is_BGEJALRBLTBLTUBEQBNEBGEUJAL = RegInit(0.U.asTypeOf(Bits(1.W)))
  val is_S = RegInit(0.U.asTypeOf(Bits(1.W)))
  val is_L = RegInit(0.U.asTypeOf(Bits(1.W)))
  val n290 = RegInit(0.U.asTypeOf(Bits(3.W)))
  val bin_writePC0_value = RegInit(0.U.asTypeOf(Bits(2.W)))
  val bin_readMEM0_words = RegInit(0.U.asTypeOf(Bits(2.W)))
  val sel_writeMEM0_words = RegInit(0.U.asTypeOf(Bits(2.W)))
  val bin_writeX0_value = RegInit(0.U.asTypeOf(Bits(3.W)))
  val sel_readMEM0_addr = RegInit(0.U.asTypeOf(Bits(5.W)))
  val invalid_insn = RegInit(0.U.asTypeOf(Bits(1.W)))
  val readX0_result = RegInit(0.U.asTypeOf(Bits(32.W)))
  val readX1_result = RegInit(0.U.asTypeOf(Bits(32.W)))
  val readMEM1_addr = RegInit(0.U.asTypeOf(Bits(32.W)))
  val writePC1_value = RegInit(0.U.asTypeOf(Bits(32.W)))
  val dec_n_295 = Wire(Bits(3.W))
  val dec_is_not_SBSHSWBEQBNEBGEBGEUBLTB = Wire(Bool())
  val dec_n_280 = Wire(Bool())
  val dec_writeMEM0_enable = Wire(Bool())
  val dec_readMEM0_enable = Wire(Bool())
  val dec_n_290 = Wire(Bits(3.W))
  val dec_sel_writePC0_value = Wire(Bits(2.W))
  val dec_sel_readMEM0_words = Wire(Bits(2.W))
  val dec_bin_writeMEM0_words = Wire(Bits(2.W))
  val dec_sel_writeX0_value = Wire(Bits(3.W))
  val dec_bin_readMEM0_addr = Wire(Bits(5.W))
  val dec_n_297 = Wire(Bool())
  val dec_is_not_AUIPCLUIJALECALLEBREAK = Wire(Bool())
  val dec_is_not_ADDIANDIORIXORISLTISLTI = Wire(Bool())
  val writeX0_value = Wire(Bits(32.W))
  val n_204 = Wire(Bool())
  val n_208 = Wire(Bool())
  val rs1_neq_0_and_is_not_AUIPCLUIJ = Wire(Bits(1.W))
  val sig_79 = Wire(Bits(32.W))
  val rs2_neq_0_and_is_not_ADDIANDIO = Wire(Bits(1.W))
  val sig_85 = Wire(Bits(32.W))


  val dec_output = Wire(Bits(27.W))

  val table = TruthTable(
    Map(
      BitPat("b?1100111?000????????????????????") -> BitPat("b 000 1 1 ? ? 111 10 ?? ?? 101 11111 ? 1 ?"), // JALR
      BitPat("b?0000011?010????????????????????") -> BitPat("b 000 1 ? ? 1 ??? ?? 01 ?? 100 00001 ? 1 ?"), // LW
      BitPat("b?0010011?010????????????????????") -> BitPat("b 000 1 ? ? ? ??? ?? ?? ?? 101 10000 ? 1 ?"), // SLTI
      BitPat("b?0000011?101????????????????????") -> BitPat("b 000 1 ? ? 1 ??? ?? 10 ?? 011 00001 ? 1 ?"), // LHU
      BitPat("b?0010011?011????????????????????") -> BitPat("b 000 1 ? ? ? ??? ?? ?? ?? 101 10001 ? 1 ?"), // SLTIU
      BitPat("b?0000011?000????????????????????") -> BitPat("b 000 1 ? ? 1 ??? ?? 00 ?? 000 00001 ? 1 ?"), // LB
      BitPat("b?0000011?100????????????????????") -> BitPat("b 000 1 ? ? 1 ??? ?? 00 ?? 001 00001 ? 1 ?"), // LBU
      BitPat("b?0010011?000????????????????????") -> BitPat("b 000 1 ? ? ? ??? ?? ?? ?? 101 01100 ? 1 ?"), // ADDI
      BitPat("b?0000011?001????????????????????") -> BitPat("b 000 1 ? ? 1 ??? ?? 10 ?? 010 00001 ? 1 ?"), // LH
      BitPat("b?0010011?110????????????????????") -> BitPat("b 000 1 ? ? ? ??? ?? ?? ?? 101 01110 ? 1 ?"), // ORI
      BitPat("b?0010011?111????????????????????") -> BitPat("b 000 1 ? ? ? ??? ?? ?? ?? 101 01101 ? 1 ?"), // ANDI
      BitPat("b?0010011?100????????????????????") -> BitPat("b 000 1 ? ? ? ??? ?? ?? ?? 101 01111 ? 1 ?"), // XORI
      BitPat("b?0010111????????????????????????") -> BitPat("b 001 1 ? ? ? ??? ?? ?? ?? 101 10010 ? ? ?"), // AUIPC
      BitPat("b?1101111????????????????????????") -> BitPat("b 010 1 1 ? ? 110 01 ?? ?? 101 11111 ? ? ?"), // JAL
      BitPat("b?0100011?000????????????????????") -> BitPat("b 011 ? ? 1 ? ??? ?? ?? 00 ??? 00000 ? 1 1"), // SB
      BitPat("b?0100011?001????????????????????") -> BitPat("b 011 ? ? 1 ? ??? ?? ?? 01 ??? 00000 ? 1 1"), // SH
      BitPat("b?0100011?010????????????????????") -> BitPat("b 011 ? ? 1 ? ??? ?? ?? 10 ??? 00000 ? 1 1"), // SW
      BitPat("b?1100011?101????????????????????") -> BitPat("b 100 ? 1 ? ? 010 00 ?? ?? ??? ????? ? 1 1"), // BGE
      BitPat("b?1100011?100????????????????????") -> BitPat("b 100 ? 1 ? ? 100 00 ?? ?? ??? ????? ? 1 1"), // BLT
      BitPat("b?1100011?110????????????????????") -> BitPat("b 100 ? 1 ? ? 101 00 ?? ?? ??? ????? ? 1 1"), // BLTU
      BitPat("b?1100011?000????????????????????") -> BitPat("b 100 ? 1 ? ? 000 00 ?? ?? ??? ????? ? 1 1"), // BEQ
      BitPat("b?1100011?001????????????????????") -> BitPat("b 100 ? 1 ? ? 001 00 ?? ?? ??? ????? ? 1 1"), // BNE
      BitPat("b?1100011?111????????????????????") -> BitPat("b 100 ? 1 ? ? 011 00 ?? ?? ??? ????? ? 1 1"), // BGEU
      BitPat("b?0010011?101????????????0100000?") -> BitPat("b ??? 1 ? ? ? ??? ?? ?? ?? 101 10110 ? 1 ?"), // SRAI
      BitPat("b?0110011?010????????????0000000?") -> BitPat("b ??? 1 ? ? ? ??? ?? ?? ?? 101 00111 ? 1 1"), // SLT
      BitPat("b?0110011?011????????????0000001?") -> BitPat("b ??? 1 ? ? ? ??? ?? ?? ?? 101 11010 ? 1 1"), // MULHU
      BitPat("b?0110011?110????????????0000000?") -> BitPat("b ??? 1 ? ? ? ??? ?? ?? ?? 101 00101 ? 1 1"), // OR
      BitPat("b?0110011?110????????????0000001?") -> BitPat("b ??? 1 ? ? ? ??? ?? ?? ?? 101 11101 ? 1 1"), // REM
      BitPat("b?0110011?000????????????0000001?") -> BitPat("b ??? 1 ? ? ? ??? ?? ?? ?? 101 10111 ? 1 1"), // MUL
      BitPat("b?0110011?101????????????0100000?") -> BitPat("b ??? 1 ? ? ? ??? ?? ?? ?? 101 01011 ? 1 1"), // SRA
      BitPat("b?0110011?100????????????0000001?") -> BitPat("b ??? 1 ? ? ? ??? ?? ?? ?? 101 11011 ? 1 1"), // DIV
      BitPat("b?0110111????????????????????????") -> BitPat("b ??? 1 ? ? ? ??? ?? ?? ?? 101 10011 ? ? ?"), // LUI
      BitPat("b?0110011?101????????????0000001?") -> BitPat("b ??? 1 ? ? ? ??? ?? ?? ?? 101 11100 ? 1 1"), // DIVU
      BitPat("b?0010011?101????????????0000000?") -> BitPat("b ??? 1 ? ? ? ??? ?? ?? ?? 101 10101 ? 1 ?"), // SRLI
      BitPat("b?0110011?100????????????0000000?") -> BitPat("b ??? 1 ? ? ? ??? ?? ?? ?? 101 00110 ? 1 1"), // XOR
      BitPat("b?0110011?101????????????0000000?") -> BitPat("b ??? 1 ? ? ? ??? ?? ?? ?? 101 01010 ? 1 1"), // SRL
      BitPat("b?0110011?000????????????0100000?") -> BitPat("b ??? 1 ? ? ? ??? ?? ?? ?? 101 00011 ? 1 1"), // SUB
      BitPat("b?0110011?001????????????0000001?") -> BitPat("b ??? 1 ? ? ? ??? ?? ?? ?? 101 11000 ? 1 1"), // MULH
      BitPat("b?0010011?001????????????0000000?") -> BitPat("b ??? 1 ? ? ? ??? ?? ?? ?? 101 10100 ? 1 ?"), // SLLI
      BitPat("b?0110011?010????????????0000001?") -> BitPat("b ??? 1 ? ? ? ??? ?? ?? ?? 101 11001 ? 1 1"), // MULHSU
      BitPat("b?0110011?000????????????0000000?") -> BitPat("b ??? 1 ? ? ? ??? ?? ?? ?? 101 00010 ? 1 1"), // ADD
      BitPat("b?0110011?111????????????0000000?") -> BitPat("b ??? 1 ? ? ? ??? ?? ?? ?? 101 00100 ? 1 1"), // AND
      BitPat("b?0110011?011????????????0000000?") -> BitPat("b ??? 1 ? ? ? ??? ?? ?? ?? 101 01000 ? 1 1"), // SLTU
      BitPat("b?0110011?111????????????0000001?") -> BitPat("b ??? 1 ? ? ? ??? ?? ?? ?? 101 11110 ? 1 1"), // REMU
      BitPat("b?0110011?001????????????0000000?") -> BitPat("b ??? 1 ? ? ? ??? ?? ?? ?? 101 01001 ? 1 1"), // SLL
    ), BitPat("b ??? ? ? ? ? ??? ?? ?? ?? ??? ????? 1 ? ?") // Invalid
  )

  dec_output := decoder(io.read_readMEM1_result_in.data.asUInt, table)

  dec_n_295 := dec_output(26, 24)
  dec_is_not_SBSHSWBEQBNEBGEBGEUBLTB := dec_output(23, 23)
  dec_n_280 := dec_output(22, 22)
  dec_writeMEM0_enable := dec_output(21, 21)
  dec_readMEM0_enable := dec_output(20, 20)
  dec_n_290 := dec_output(19, 17)
  dec_sel_writePC0_value := dec_output(16, 15)
  dec_sel_readMEM0_words := dec_output(14, 13)
  dec_bin_writeMEM0_words := dec_output(12, 11)
  dec_sel_writeX0_value := dec_output(10, 8)
  dec_bin_readMEM0_addr := dec_output(7, 3)
  dec_n_297 := dec_output(2, 2)
  dec_is_not_AUIPCLUIJALECALLEBREAK := dec_output(1, 1)
  dec_is_not_ADDIANDIORIXORISLTISLTI := dec_output(0, 0)


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
  n_204 := neq(io.read_readMEM1_result_in.data.asUInt(19, 15), "h0".U(5.W))
  when ((io.DECODE_en_in).asBool) {
    rs1_neq_0 := n_204
  }
  n_208 := neq(io.read_readMEM1_result_in.data.asUInt(24, 20), "h0".U(5.W))
  when ((io.DECODE_en_in).asBool) {
    rs2_neq_0 := n_208
  }
  when ((io.DECODE_en_in).asBool) {
    n246 := MuxLookup[Bits](dec_n_295, 0.U)(Seq(0.U -> io.read_readMEM1_result_in.data.asUInt(31, 20).sext(32.W), 1.U -> writeX0_value, 2.U -> lsl(Cat(io.read_readMEM1_result_in.data.asUInt(31, 31), io.read_readMEM1_result_in.data.asUInt(19, 12), io.read_readMEM1_result_in.data.asUInt(20, 20), io.read_readMEM1_result_in.data.asUInt(30, 21)).sext(32.W), "h1".U(1.W)), 3.U -> Cat(io.read_readMEM1_result_in.data.asUInt(31, 25), io.read_readMEM1_result_in.data.asUInt(11, 7)).sext(32.W), 4.U -> lsl(Cat(io.read_readMEM1_result_in.data.asUInt(31, 31), io.read_readMEM1_result_in.data.asUInt(7, 7), io.read_readMEM1_result_in.data.asUInt(30, 25), io.read_readMEM1_result_in.data.asUInt(11, 8)).sext(32.W), "h1".U(1.W))))
  }
  when ((io.DECODE_en_in).asBool) {
    writeX0_enable := and(neq(io.read_readMEM1_result_in.data.asUInt(11, 7), "h0".U(5.W)), dec_is_not_SBSHSWBEQBNEBGEBGEUBLTB)
  }
  when ((io.DECODE_en_in).asBool) {
    is_BGEJALRBLTBLTUBEQBNEBGEUJAL := dec_n_280
  }
  when ((io.DECODE_en_in).asBool) {
    is_S := dec_writeMEM0_enable
  }
  when ((io.DECODE_en_in).asBool) {
    is_L := dec_readMEM0_enable
  }
  when ((io.DECODE_en_in).asBool) {
    n290 := dec_n_290
  }
  when ((io.DECODE_en_in).asBool) {
    bin_writePC0_value := dec_sel_writePC0_value
  }
  when ((io.DECODE_en_in).asBool) {
    bin_readMEM0_words := dec_sel_readMEM0_words
  }
  when ((io.DECODE_en_in).asBool) {
    sel_writeMEM0_words := dec_bin_writeMEM0_words
  }
  when ((io.DECODE_en_in).asBool) {
    bin_writeX0_value := dec_sel_writeX0_value
  }
  when ((io.DECODE_en_in).asBool) {
    sel_readMEM0_addr := dec_bin_readMEM0_addr
  }
  when ((io.DECODE_en_in).asBool) {
    invalid_insn := dec_n_297
  }
  rs1_neq_0_and_is_not_AUIPCLUIJ := and(n_204, dec_is_not_AUIPCLUIJALECALLEBREAK)
  io.readX0_result_in.enable := and(io.read_DECODE_full_in.data.asUInt, and(rs1_neq_0_and_is_not_AUIPCLUIJ, not(io.fwd_X0_en_in)))
  io.readX0_result_in.address := io.read_readMEM1_result_in.data.asUInt(19, 15)
  sig_79 := Mux((io.fwd_X0_en_in).asBool, io.fwd_X0_val_in, io.readX0_result_in.data.asUInt)
  when ((io.DECODE_en_in).asBool) {
    readX0_result := sig_79
  }
  rs2_neq_0_and_is_not_ADDIANDIO := and(n_208, dec_is_not_ADDIANDIORIXORISLTISLTI)
  io.readX1_result_in.enable := and(io.read_DECODE_full_in.data.asUInt, and(rs2_neq_0_and_is_not_ADDIANDIO, not(io.fwd_X1_en_in)))
  io.readX1_result_in.address := io.read_readMEM1_result_in.data.asUInt(24, 20)
  sig_85 := Mux((io.fwd_X1_en_in).asBool, io.fwd_X1_val_in, io.readX1_result_in.data.asUInt)
  when ((io.DECODE_en_in).asBool) {
    readX1_result := sig_85
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
  readX1_enable := rs2_neq_0_and_is_not_ADDIANDIO
  when ((rs1_neq_0_and_is_not_AUIPCLUIJ).asBool) {
    printf(cf"%T ${io.read_readMEM1_addr_in.data.asUInt}%x rd X(${io.read_readMEM1_result_in.data.asUInt(19, 15)}%d) = ${sig_79}%x\n")
  }
  when ((rs2_neq_0_and_is_not_ADDIANDIO).asBool) {
    printf(cf"%T ${io.read_readMEM1_addr_in.data.asUInt}%x rd X(${io.read_readMEM1_result_in.data.asUInt(24, 20)}%d) = ${sig_85}%x\n")
  }
  io.read_n290_out := n290
  io.read_rs1_neq_0_out := rs1_neq_0
  io.read_readX0_result_out := readX0_result
  io.read_rs2_neq_0_out := rs2_neq_0
  io.read_readX1_result_out := readX1_result
  io.read_writePC1_value_0_out := writePC1_value
  io.read_readMEM1_addr_0_out := readMEM1_addr
  io.read_n246_out := n246
  io.read_is_BGEJALRBLTBLTUBEQBNEBGEUJAL_out := is_BGEJALRBLTBLTUBEQBNEBGEUJAL
  io.read_bin_writePC0_value_out := bin_writePC0_value
  io.read_sel_readMEM0_addr_out := sel_readMEM0_addr
  io.read_readMEM0_addr_out := readMEM0_addr
  io.read_rs2_out := rs2
  io.read_is_S_out := is_S
  io.read_sel_writeMEM0_words_out := sel_writeMEM0_words
  io.read_invalid_insn_out := invalid_insn
  io.read_bin_writeX0_value_out := bin_writeX0_value
  io.read_bin_readMEM0_words_out := bin_readMEM0_words
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
      gen = new DECODE,
      firtoolOpts = Array(
        "-disable-all-randomization",
        "--lowering-options=disallowLocalVariables",
        "-strip-debug-info")
    )
  )
}
