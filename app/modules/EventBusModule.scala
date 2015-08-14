package modules

import events.EventBus
import play.api.Logger

/**
 * Created by paullawler on 8/13/15.
 */

trait EventBusModule {

  lazy val eventBus = EventBus()

}