package com.jamieallen.akkatest

import org.scalatest.junit.JUnitSuite
import org.junit.Test

import java.util.concurrent.TimeUnit
import org.multiverse.api.latches.StandardLatch
import akka.actor.{ActorRef, Actor, Death}
import Actor._
import impl._

/**
 * @author Jamie Allen (jallen@chariotsolutions.com)
 */
object ErrorKernelTest {
  class FireWorkerException(msg: String) extends Exception(msg)
}

class ErrorKernelTest extends JUnitSuite {
  import ErrorKernelTest._

//  @Test
  def killWorkerShouldRestartMangerAndOtherWorkers = {
    val timingLatch = new StandardLatch

    val dao = actorOf(new DeltasDao(null))
    val queue = actorOf(new DeltasQueue(100))
    val producer = actorOf(new Producer(dao, queue))
    val consumer = actorOf(new Consumer(queue))
    val errorKernel = actorOf(new ErrorKernel(null,
      Map[String, ActorRef]((classOf[DeltasDao].getName -> dao),
                           (classOf[DeltasQueue].getName -> queue),
                           (classOf[Producer].getName -> producer),
                           (classOf[Consumer].getName -> consumer)))).start()
    errorKernel ! StartCacheRefresh

    timingLatch.tryAwait(1, TimeUnit.SECONDS)

    dao ! Death(dao, new FireWorkerException("Fire the DAO!"))

    timingLatch.tryAwait(1, TimeUnit.SECONDS)

    assert(dao.isRunning)
    assert(queue.isRunning)
    assert(producer.isRunning)
    assert(consumer.isRunning)
  }

  @Test
  def sendingStopMessageShouldStopAllChildActors = {
    val timingLatch = new StandardLatch

    val dao = actorOf(new DeltasDao(null))
    val queue = actorOf(new DeltasQueue(100))
    val producer = actorOf(new Producer(dao, queue))
    val consumer = actorOf(new Consumer(queue))

    val errorKernel = actorOf(new ErrorKernel(null,
      Map[String, ActorRef]((classOf[DeltasDao].getName -> dao),
                           (classOf[DeltasQueue].getName -> queue),
                           (classOf[Producer].getName -> producer),
                           (classOf[Consumer].getName -> consumer)))).start()
    errorKernel ! StartCacheRefresh
    timingLatch.tryAwait(1, TimeUnit.SECONDS)

    errorKernel ! StopCacheRefresh
    timingLatch.tryAwait(5, TimeUnit.SECONDS)

    assert(dao.isShutdown)
    assert(queue.isShutdown)
    assert(producer.isShutdown)
    assert(consumer.isShutdown)
  }
}