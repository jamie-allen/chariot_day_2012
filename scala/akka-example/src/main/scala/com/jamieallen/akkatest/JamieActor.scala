package com.jamieallen.akkatest

import akka.actor.Actor
import com.weiglewilczek.slf4s.Logging

trait JamieActor extends Actor with Logging {
  override def preStart() { logger.debug("Starting up") }
  override def postStop() { logger.debug("Shutting down") }
  override def postRestart(reason: Throwable) { logger.debug("Restarted, reason: " + reason.getMessage) }
}