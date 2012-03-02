package com.jamieallen.akkatest

import akka.actor.Actor.actorOf
import com.weiglewilczek.slf4s.Logging
import akka.actor.{ActorRef, Actor, PoisonPill}

/**
 * Author: Jamie Allen (jallen@chariotsolutions.com)
 */
object Bootstrap extends Logging {
  def main(args: Array[String]) {
    // Start up
    val dao = actorOf(new DeltasDao(null))
    val queue = actorOf(new DeltasQueue(100))
    val producer = actorOf(new Producer(dao, queue))
    val consumer = actorOf(new Consumer(queue))

	  // Execute application
    val errorKernel = actorOf(new ErrorKernel(null,
      Map[String, ActorRef]((classOf[DeltasDao].getName -> dao),
                           (classOf[DeltasQueue].getName -> queue),
                           (classOf[Producer].getName -> producer),
                           (classOf[Consumer].getName -> consumer)))).start()
    errorKernel ! StartCacheRefresh
  	Thread.sleep(10000)
  	errorKernel ! StopCacheRefresh
    Actor.registry.shutdownAll()

	  // Shutdown cleanly
	  System.exit(0)
  }
}
