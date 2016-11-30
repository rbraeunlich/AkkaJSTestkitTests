package test

import akka.actor.{Actor, ActorLogging}
import test.EchoActor._

/**
  * @author Ronny Bräunlich
  */
class EchoActor extends Actor with ActorLogging {

  var gotCalled = false

  def receive = {
    case LogMessage => log.info("foo")
    case AnswerMessage => {
      gotCalled = true
      sender ! AnswerMessage
    }
  }
}

object EchoActor {

  case object LogMessage

  case object AnswerMessage

}
