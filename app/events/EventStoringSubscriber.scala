package events

import akka.actor.{ActorLogging, Actor, ActorRef}
import play.api.Logger

/**
 * Created by paullawler on 8/14/15.
 */
class EventStoringSubscriber extends Actor {

  override def receive: Receive = {
    case event: AlpEvent => Logger.info(s"The EventStoringSubscriber says: ${event.message} @ ${event.timestamp}")
  }

}

class InterestingEventSubscriber extends Actor {

  override def receive: Receive = {
    case event: InterestingEvent => Logger.info(s"The InterestingSubscriber says: ${event.message} @ ${event.timestamp}")
  }

}