//> using repository https://central.sonatype.com/repository/maven-snapshots
//> using scala 2.13.17
//> using dep org.chipsalliance::chisel:7.2.0
//> using plugin org.chipsalliance:::chisel-plugin:7.2.0
//> using options -unchecked -deprecation -language:reflectiveCalls -feature -Xcheckinit
//> using options -Xfatal-warnings -Ywarn-dead-code -Ywarn-unused -Ymacro-annotations

import chisel3._
import chisel3.util.BitPat
import chisel3.util.experimental.decode.{TruthTable, decoder}
// _root_ disambiguates from package chisel3.util.circt if user imports chisel3.util._
import _root_.circt.stage.ChiselStage

class Foo extends Module {
  val input = IO(Input(UInt(4.W)))
  val output = IO(Output(UInt(2.W)))
  val dec_output = Wire(UInt(4.W))

  val table = TruthTable(
    Map(
      BitPat("b0010") -> BitPat("b0001"),
      BitPat("b0100") -> BitPat("b0010")
    ), BitPat("b0000"))

  dec_output := decoder(input, table)

  output := dec_output(1,0)

}

object Main extends App {
  println(
    ChiselStage.emitSystemVerilog(
      gen = new Foo,
      firtoolOpts = Array("-disable-all-randomization", "-strip-debug-info", "-default-layer-specialization=enable")
    )
  )
}
