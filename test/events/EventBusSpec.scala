package events

import akka.actor.Actor.Receive
import akka.actor.{Actor, Props}
import akka.testkit.TestProbe
import org.scalatestplus.play.{OneAppPerSuite, PlaySpec}
import play.api.libs.concurrent.Akka

import scala.concurrent.duration._
import scala.language.postfixOps

/**
 * Created by paullawler on 8/17/15.
 */
class EventBusSpec extends PlaySpec with OneAppPerSuite {

  "The EventBus" must {

    "be a single instance" in {
      val eb1 = EventBus
      val eb2 = EventBus

      eb1 mustEqual eb2
    }

    "handle an event" in new Context {
      val subscriber = system.actorOf(Props(new Actor {
        def receive: Receive = {
          case e @ SimpleEventCreated(_, _) => testProbe.ref ! e
        }
      }))

      eventBus.subscribe(subscriber, classOf[SimpleEventCreated])
      eventBus.publish(simpleEvent)
      testProbe.expectMsg(simpleEvent)
    }

    "handle multiple events" in new Context {
      val subscriber = system.actorOf(Props(new Actor {
        def receive: Receive = {
          case e @ SimpleEventCreated(_,_) => testProbe.ref ! e
          case e @ InterestingEventCreated(_,_) => testProbe.ref ! e
        }
      }))

      eventBus.subscribe(subscriber, classOf[BaseEvent])
      eventBus.publish(simpleEvent)
      eventBus.publish(interestingEvent)

      testProbe.expectMsg(500 millis, simpleEvent)
      testProbe.expectMsg(500 millis, interestingEvent)
    }

  }

  trait Context {
    lazy implicit val system = Akka.system
    lazy val testProbe = TestProbe()

    val eventBus = EventBus()

    val simpleEvent = SimpleEventCreated(Payload("The payload"))
    val interestingEvent = InterestingEventCreated(Payload("Interesting payload"))
  }

}
