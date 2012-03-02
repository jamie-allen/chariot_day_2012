package com.jamieallen.akkatest

import java.util.concurrent.ArrayBlockingQueue
import com.weiglewilczek.slf4s.Logging
import java.lang.IllegalStateException
import akka.actor.{PoisonPill, Actor}
import impl._

class DeltasQueue(private val queueSize: Int) extends JamieActor with Logging {
	private final val abQueue = new ArrayBlockingQueue[Seq[Delta]](queueSize, true)
	
	def receive = {
	  case Add(e: Seq[Delta]) =>
	    try {
	      if (abQueue.add(e)) {
          logger.debug("Delta added, new queue state: " + abQueue)
	        self.reply(SuccessfulAdd)
	      }
	      throw new RuntimeException("Queue is not behaving correctly")
	    } catch {
	      case ise: IllegalStateException => self.reply(QueueFull)
	    }
	  case Poll =>
	    val deltasToReturn = abQueue.poll
	    if (deltasToReturn == null) {
        logger.debug("Returning no results, queue is empty")
        self.reply(QueueEmpty)
      }
      logger.debug("Returning poll results")
	    self.reply(SuccessfulPoll(deltasToReturn))
	}
}