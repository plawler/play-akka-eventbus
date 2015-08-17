package events

import akka.actor.{ActorLogging, Actor, ActorRef}
import play.api.Logger

/**
 * Created by paullawler on 8/14/15.
 */
class EventStoringSubscriber extends Actor with ActorLogging {

  override def receive: Receive = {
    case event: BaseEvent =>
      log.info(s"The EventStoringSubscriber says: ${event.payload.data} @ ${event.timestamp}")
  }

}

class InterestingEventSubscriber extends Actor with ActorLogging {

  override def receive: Receive = {
    case event: InterestingEventCreated =>
      log.info(s"The InterestingSubscriber says: ${event.payload.data} @ ${event.timestamp}")
  }

}