package events

import akka.actor.{ActorRef, Props, ActorSystem}
import akka.event.{SubchannelClassification, ActorEventBus}
import akka.util.Subclassification
import org.joda.time.DateTime

/**
 * Created by paullawler on 8/13/15.
 */
trait AlpEvent {
  def message: String
  def retrievePayload: Any
  def timestamp: DateTime
}

case class GeneralEvent(message: String, timestamp: DateTime) extends AlpEvent {
  override def retrievePayload: Any = "This is my payload"
}

case class InterestingEvent(message: String, timestamp: DateTime) extends AlpEvent {
  override def retrievePayload: Any = "This is my interesting payload"
}

class EventBus extends ActorEventBus with SubchannelClassification {

  override type Classifier = Class[_ <: AlpEvent]
  override type Event = AlpEvent

  override protected implicit def subclassification: Subclassification[Classifier] = new Subclassification[Classifier] {
    def isEqual(x: Classifier, y: Classifier): Boolean = x == y
    def isSubclass(x: Classifier, y: Classifier): Boolean = y.isAssignableFrom(x)
  }

  override protected def publish(event: Event, subscriber: Subscriber): Unit = subscriber ! event

  override protected def classify(event: Event): Classifier = event.getClass

}

object EventBus {

  private lazy val instance = new EventBus
  private lazy val system = ActorSystem("EventBus")

  val storingSubscriber: ActorRef = system.actorOf(Props[EventStoringSubscriber])
  val interestingSubscriber: ActorRef = system.actorOf(Props[InterestingEventSubscriber])

  def apply() = {
    registerSubscribers
    instance
  }

  def registerSubscribers = {
    instance.subscribe(storingSubscriber, classOf[AlpEvent])
    instance.subscribe(interestingSubscriber, classOf[InterestingEvent])
  }

}
