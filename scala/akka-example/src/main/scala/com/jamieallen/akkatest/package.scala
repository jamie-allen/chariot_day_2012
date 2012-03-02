package com.jamieallen.akkatest

package object impl {
	sealed trait ProducerMessage
  case object GetUpdates extends ProducerMessage

  sealed trait ConsumerMessage
	case object TakeNextFromQueue extends ConsumerMessage
	
	sealed trait QueueInteraction
	case class Add(deltas: Seq[Delta]) extends QueueInteraction
	case object Poll extends QueueInteraction
	
	sealed trait QueueAddResponse
	case object SuccessfulAdd extends QueueAddResponse
 	case object QueueFull extends QueueAddResponse

	sealed trait QueuePollResponse
	case class SuccessfulPoll(entitlementDeltas: Seq[Delta]) extends QueuePollResponse
	case object QueueEmpty extends QueuePollResponse

  case class Delta(timeStamp: Long, account: Account, device: Device)
  case class Account(accountNumber: String)
  case class Device(macType: String)

  sealed trait DeltaDaoResponse
  case class DeltasReceived(deltas: Seq[Delta]) extends DeltaDaoResponse
  case object NoDeltasFound extends DeltaDaoResponse
  case object RspCallFailed extends DeltaDaoResponse
}