package com.jamieallen.akkatest

import com.weiglewilczek.slf4s.Logging
import akka.actor.{PoisonPill, ActorRef}
import impl._

class Consumer(val deltasQueue: ActorRef) extends JamieActor with Logging {
	def receive = {
    case (TakeNextFromQueue) =>
      logger.debug("Received TakeNextFromQueue")
      (deltasQueue ? Poll).as[QueuePollResponse] match {
        case Some(SuccessfulPoll(deltasToReturn)) => logger.debug("Consumer got deltas: " + deltasToReturn)
        case Some(QueueEmpty) => logger.debug("Queue is empty, no data returned")
        case None => logger.debug("Got no response from queue")
      }

      self ! TakeNextFromQueue
  }

  override def postRestart(reason: Throwable) {
    super.postRestart(reason)
    self ! TakeNextFromQueue
  }
}