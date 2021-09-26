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

package nutcore

import chisel3._
import chisel3.util._

object RVAInstr extends HasInstrType {
  // Note: use instr(14,12) to distinguish D/W inst
  // def LR      = BitPat("b00010??00000_?????_???_?????_0101111")
  // def SC      = BitPat("b00011??00000_?????_???_?????_0101111")
  def LR_D    = BitPat("b00010_??_00000_?????_011_?????_0101111")
  def SC_D    = BitPat("b00011_??_?????_?????_011_?????_0101111")
  def LR_W    = BitPat("b00010_??_00000_?????_010_?????_0101111")
  def SC_W    = BitPat("b00011_??_?????_?????_010_?????_0101111")
  def AMOSWAP = BitPat("b00001_??_?????_?????_01?_?????_0101111")
  def AMOADD  = BitPat("b00000_??_?????_?????_01?_?????_0101111")
  def AMOXOR  = BitPat("b00100_??_?????_?????_01?_?????_0101111")
  def AMOAND  = BitPat("b01100_??_?????_?????_01?_?????_0101111")
  def AMOOR   = BitPat("b01000_??_?????_?????_01?_?????_0101111")
  def AMOMIN  = BitPat("b10000_??_?????_?????_01?_?????_0101111")
  def AMOMAX  = BitPat("b10100_??_?????_?????_01?_?????_0101111")
  def AMOMINU = BitPat("b11000_??_?????_?????_01?_?????_0101111")
  def AMOMAXU = BitPat("b11100_??_?????_?????_01?_?????_0101111")
  // funct3 === 010 or 011
  
  val table = Array(
    // LR          -> List(InstrI, FuType.lsu, LSUOpType.lr),
    LR_D        -> List(InstrI, FuType.lsu, LSUOpType.lr),
    LR_W        -> List(InstrI, FuType.lsu, LSUOpType.lr),
    // SC          -> List(InstrS, FuType.lsu, LSUOpType.sc),
    SC_D        -> List(InstrSA, FuType.lsu, LSUOpType.sc),
    SC_W        -> List(InstrSA, FuType.lsu, LSUOpType.sc),
    AMOSWAP     -> List(InstrR, FuType.lsu, LSUOpType.amoswap),
    AMOADD      -> List(InstrR, FuType.lsu, LSUOpType.amoadd),
    AMOXOR      -> List(InstrR, FuType.lsu, LSUOpType.amoxor),
    AMOAND      -> List(InstrR, FuType.lsu, LSUOpType.amoand),
    AMOOR       -> List(InstrR, FuType.lsu, LSUOpType.amoor),
    AMOMIN      -> List(InstrR, FuType.lsu, LSUOpType.amomin),
    AMOMAX      -> List(InstrR, FuType.lsu, LSUOpType.amomax),
    AMOMINU     -> List(InstrR, FuType.lsu, LSUOpType.amominu),
    AMOMAXU     -> List(InstrR, FuType.lsu, LSUOpType.amomaxu)
  )
}
