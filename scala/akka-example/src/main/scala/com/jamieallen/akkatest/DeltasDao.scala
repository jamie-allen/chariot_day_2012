package com.jamieallen.akkatest

import com.weiglewilczek.slf4s.Logging
import akka.actor.{PoisonPill, Actor}
import impl._

/**
 * Author: Jamie Allen (jallen@chariotsolutions.com)
 */
class DeltasDao(val jdbcTemplate: Any) extends JamieActor with Logging {
  val device = Device("macType")
  val account = Account("123123123")
  val delta = Delta(System.currentTimeMillis(), account, device)

	def receive = {
    case (GetUpdates) =>
      logger.debug("Returning updates")
    	self.reply(new DeltasReceived(Seq(delta)))
//      self.reply(NoDeltasFound)
//      self.reply(RspCallFailed)
  }
}