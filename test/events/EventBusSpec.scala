package events

import akka.actor.Actor.Receive
import akka.actor.{Actor, Props}
import akka.testkit.TestProbe
import org.scalatestplus.play.{OneAppPerSuite, PlaySpec}
import play.api.libs.concurrent.Akka

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
      val eventBus = EventBus()

      val subscriber = system.actorOf(Props(new Actor {
        def receive: Receive = {
          case e @ SimpleEventCreated(_, _) => testProbe.ref ! e
        }
      }))

      val simpleEvent = SimpleEventCreated(Payload("The payload"))

      eventBus.subscribe(subscriber, classOf[SimpleEventCreated])
      eventBus.publish(simpleEvent)
      testProbe.expectMsg(simpleEvent)
    }

  }

  trait Context {
    lazy implicit val system = Akka.system
    lazy val testProbe = TestProbe()
  }

}
