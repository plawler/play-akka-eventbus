package controllers

import events.{GeneralEvent, InterestingEvent}
import modules.EventBusModule
import org.joda.time.DateTime
import play.api._
import play.api.mvc._

class Application extends Controller with EventBusModule {

  def index = Action {
    Ok("The Akka EventBus application is ready")
  }

  def general = Action {
    eventBus.publish(GeneralEvent("General event!", DateTime.now()))
    Ok("Published a general event")
  }

  def interesting = Action {
    eventBus.publish(InterestingEvent("Interesting event!", DateTime.now()))
    Ok("Published an interesting event")
  }

}
