package controllers

import events.{Payload, SimpleEventCreated, InterestingEventCreated}
import modules.EventBusModule
import play.api.mvc._

class Application extends Controller with EventBusModule {

  def index = Action {
    Ok("The Akka EventBus application is ready")
  }

  def general = Action {
    eventBus.publish(SimpleEventCreated(Payload("Simple Event Payload")))
    Ok("Published a general event")
  }

  def interesting = Action {
    eventBus.publish(InterestingEventCreated(Payload("Interesting event!")))
    Ok("Published an interesting event")
  }

}
