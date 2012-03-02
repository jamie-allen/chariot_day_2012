package com.jamieallen.akkatest

import com.weiglewilczek.slf4s.Logging
import akka.actor.{PoisonPill, ActorRef}
import impl._

/**
 * Author: Jamie Allen (jallen@chariotsolutions.com)
 */
class Producer(val deltasDao: ActorRef, val queue: ActorRef) extends JamieActor with Logging {
  self.receiveTimeout = Some(15000)

	def receive = {
    case (GetUpdates) =>
      logger.debug("Recieved GetUpdates")
    	(deltasDao ? GetUpdates).as[DeltaDaoResponse] match {
        case Some(DeltasReceived(deltas)) => (queue ? Add(deltas)).as[QueueAddResponse] match {
          case Some(SuccessfulAdd) => logger.debug("Queue responded SuccessfulAdd")
          case Some(QueueFull) => logger.debug("Queue is full")
          case None => logger.debug("No response returned from Queue")
        }
        case Some(NoDeltasFound) => logger.debug("@@ No entitlement updates returned")
        case Some(RspCallFailed) => logger.debug("@@ RSP call failed or timed out")
        case None => logger.debug("No response returned from DAO")
      }

  	self ! GetUpdates
  }

  override def postRestart(reason: Throwable) {
    super.postRestart(reason)
    self ! GetUpdates
  }
}
