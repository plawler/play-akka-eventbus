package events

import java.util.UUID

import akka.actor.{ActorRef, Props, ActorSystem}
import akka.event.{SubchannelClassification, ActorEventBus}
import akka.util.Subclassification
import org.joda.time.DateTime
import play.api.libs.concurrent.Akka

import play.api.Play.current // required for referencing Play's ActorSystem

/**
 * Created by paullawler on 8/13/15.
 */
trait BaseEvent {
  val id: UUID = UUID.randomUUID()
  val aggregateId: Option[UUID]
  val timestamp: DateTime = DateTime.now()
  val eventType: String
  val version: Int
  val payload: Payload
}

case class Payload(data: Any)

case class SimpleEventCreated(payload: Payload, aggregateId: Option[UUID] = None) extends BaseEvent {
  override val eventType = "simple-event-created"
  override val version = 1
}

case class InterestingEventCreated(payload: Payload, aggregateId: Option[UUID] = None) extends BaseEvent {
  override val eventType = "interesting-event-created"
  override val version = 1
}

trait EventBus extends ActorEventBus with SubchannelClassification {

  override type Classifier = Class[_ <: BaseEvent]
  override type Event = BaseEvent

  override protected implicit def subclassification: Subclassification[Classifier] = new Subclassification[Classifier] {
    def isEqual(x: Classifier, y: Classifier): Boolean = x == y
    def isSubclass(x: Classifier, y: Classifier): Boolean = y.isAssignableFrom(x)
  }

  override protected def publish(event: Event, subscriber: Subscriber): Unit = subscriber ! event

  override protected def classify(event: Event): Classifier = event.getClass

}

object EventBus {
  private lazy val instance = new EventBus{}
  def apply() = instance
}
