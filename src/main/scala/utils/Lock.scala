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

package utils

import chisel3._
import chisel3.util._

class LockBundle extends Bundle {
  val lock = Input(Bool())
  val unlock = Input(Bool())
  val holding = Output(Bool())
}

class Lock(n: Int) extends Module {
  val io = IO(new Bundle {
    val bundle = Vec(n, new LockBundle)
  })

  val lock = RegInit(0.U(n.W))
  val lockReq = VecInit(io.bundle.map(_.lock)).asUInt
  val unlockReq = VecInit(io.bundle.map(_.unlock)).asUInt

  val lockEmpty = lock === 0.U
  val hasLockReq = lockReq =/= 0.U
  val lockNext = 1.U << PriorityEncoder(lockReq)
  when (lockEmpty && hasLockReq) { lock := lockNext }

  val hasUnlockReq = unlockReq =/= 0.U
  assert(PopCount(unlockReq) <= 1.U, "only the lock holder can issue unlock request")
  assert(!(lockEmpty && hasUnlockReq), "only the lock holder can issue unlock request")
  assert((lock & lockReq) === 0.U, "can not issue lock request when holding the lock")
  when (!lockEmpty && hasUnlockReq) {
    assert(unlockReq === lock, "only the lock holder can issue unlock request")
    lock := 0.U
  }

  val holding = Mux(lockEmpty && hasLockReq, lockNext, lock)
  io.bundle.map(_.holding).zip(holding.asBools).map{ case (l, r) => l := r }
  assert(PopCount(io.bundle.map(_.holding)) <= 1.U, "there should be only one lock holder")

  Debug() {
    when (lockEmpty && hasLockReq) { printf("%d: %d acquire lock\n", GTimer(), PriorityEncoder(lockNext)) }
    when (!lockEmpty && hasUnlockReq) { printf("%d: %d release lock\n", GTimer(), PriorityEncoder(lock)) }
  }
}
