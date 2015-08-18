package modules

import akka.actor.{Props, ActorRef}
import events._
import play.api.libs.concurrent.Akka

import play.api.Play.current

/**
 * Created by paullawler on 8/13/15.
 */

trait EventBusModule {

  val storingSubscriber: ActorRef = Akka.system.actorOf(Props[SimpleEventSubscriber])
  val interestingSubscriber: ActorRef = Akka.system.actorOf(Props[InterestingEventSubscriber])

  lazy val eventBus = new EventBus {
    subscribe(storingSubscriber, classOf[BaseEvent])
    subscribe(interestingSubscriber, classOf[InterestingEventCreated])
  }

}
