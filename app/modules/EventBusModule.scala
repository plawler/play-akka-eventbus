package modules

import events.EventBus
import play.api.Logger

/**
 * Created by paullawler on 8/13/15.
 */

trait EventBusModule {

  val eventBus = {
    Logger.info("Initializing the EventBus")
    EventBus()
  }

}

