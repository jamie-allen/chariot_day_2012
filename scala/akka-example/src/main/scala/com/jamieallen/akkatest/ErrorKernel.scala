package com.jamieallen.akkatest

import akka.actor._
import akka.config.Supervision.{AllForOneStrategy, Permanent, SupervisorConfig, Supervise}
import com.weiglewilczek.slf4s.Logging
import impl._

sealed trait ApplicationManagementMessage
case object StartCacheRefresh extends ApplicationManagementMessage
case object StopCacheRefresh extends ApplicationManagementMessage

class ErrorKernel(val jdbcTemplate: Any, val actors: Map[String, ActorRef]) extends JamieActor with Logging {
  self.lifeCycle = Permanent

  // Register actors for supervision; this will start each actor automatically
  private val supervisor: Supervisor = Supervisor(
    SupervisorConfig(
      AllForOneStrategy(List(classOf[Throwable]), 10, 5000),
      actors.map{ case (_, actorRef) => Supervise(actorRef, Permanent)}.toList))

  def receive = {
    case StartCacheRefresh =>
      logger.debug("Received StartCacheRefresh message")
      actors.map(_ match {
        case ("com.jamieallen.akkatest.Producer", p) =>
          checkActorStatusAndStart(p)
          p ! GetUpdates
        case ("com.jamieallen.akkatest.Consumer", c) =>
          checkActorStatusAndStart(c)
          c ! TakeNextFromQueue
        case (_, a) => checkActorStatusAndStart(a)
      })
    case StopCacheRefresh =>
      logger.debug("Received StopCacheRefresh message, stopping actors")
      actors.map { case (className, actorRef) =>
        logger.debug("Stopping " + className)
        actorRef ! PoisonPill
      }
      logger.debug("Finished stopping actors")
    case MaximumNumberOfRestartsWithinTimeRangeReached(victimActorRef, maxNrOfRetries, withinTimeRange, lastExceptionCausingRestart) =>
      logger.debug("Too many restart retries within retry period for actor: " + victimActorRef + "\n\tReason: " + lastExceptionCausingRestart + "\n\tWaiting, trying again.")
      self ! StopCacheRefresh
      self ! StartCacheRefresh
    case x => logger.debug("ErrorKernel received undefined message: " + x)
  }

  private def checkActorStatusAndStart(actor: ActorRef) = if (!actor.isRunning) actor.start()

  override def postRestart(reason: Throwable) {
    super.postRestart(reason)
    self ! StartCacheRefresh
  }
}