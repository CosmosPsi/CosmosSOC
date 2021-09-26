/**************************************************************************************
* Copyright (c) 2020 Institute of Computing Technology, CAS
* Copyright (c) 2020 University of Chinese Academy of Sciences
* 
* NutShell is licensed under Mulan PSL v2.
* You can use this software according to the terms and conditions of the Mulan PSL v2. 
* You may obtain a copy of Mulan PSL v2 at:
*             http://license.coscl.org.cn/MulanPSL2 
* 
* THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND, EITHER 
* EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT, MERCHANTABILITY OR 
* FIT FOR A PARTICULAR PURPOSE.  
*
* See the Mulan PSL v2 for more details.  
***************************************************************************************/

package device

import chisel3._
import chisel3.util._

import bus.axi4._
import utils._

class UARTGetc extends BlackBox with HasBlackBoxInline {
  val io = IO(new Bundle {
    val clk = Input(Clock())
    val getc = Input(Bool())
    val ch = Output(UInt(8.W))
  })

  setInline("UARTGetc.v",
    s"""
      |import "DPI-C" function void uart_getc(output byte ch);
      |
      |module UARTGetc (
      |  input clk,
      |  input getc,
      |  output reg [7:0] ch
      |);
      |
      |  always@(posedge clk) begin
      |    if (getc) uart_getc(ch);
      |  end
      |
      |endmodule
     """.stripMargin)
}

class AXI4UART extends AXI4SlaveModule(new AXI4Lite) {
  val rxfifo = RegInit(0.U(32.W))
  val txfifo = Reg(UInt(32.W))
  val stat = RegInit(1.U(32.W))
  val ctrl = RegInit(0.U(32.W))

  val getcHelper = Module(new UARTGetc)
  getcHelper.io.clk := clock
  getcHelper.io.getc := (raddr(3,0) === 0.U && ren)

  def putc(c: UInt): UInt = { printf("%c", c(7,0)); c }
  def getc = getcHelper.io.ch

  val mapping = Map(
    RegMap(0x0, getc, RegMap.Unwritable),
    RegMap(0x4, txfifo, putc),
    RegMap(0x8, stat),
    RegMap(0xc, ctrl)
  )

  RegMap.generate(mapping, raddr(3,0), in.r.bits.data,
    waddr(3,0), in.w.fire(), in.w.bits.data, MaskExpand(in.w.bits.strb >> waddr(log2Ceil(DataBytes)-1,0)))
}
