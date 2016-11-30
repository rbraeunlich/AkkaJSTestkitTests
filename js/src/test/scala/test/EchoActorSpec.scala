package test

import akka.actor.{ActorSystem, Props}
import akka.testkit.{EventFilter, ImplicitSender, TestActorRef, TestKit}
import com.typesafe.config.ConfigFactory
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}
import test.EchoActor.{AnswerMessage, LogMessage}

/**
  * @author Ronny Bräunlich
  */
class EchoActorSpec extends TestKit(ActorSystem("test", ConfigFactory.parseString(
  """
  akka.loggers = ["akka.testkit.TestEventListener"]
  """)))
  with WordSpecLike
  with BeforeAndAfterAll
  with ImplicitSender
  with Matchers{

  override def afterAll(): Unit = {
    super.afterAll()
    system.terminate()
  }

  "EchoActor" must {
    "log" in {
      val actor = system.actorOf(Props(new EchoActor))
      EventFilter.info(message = "foo", occurrences = 1) intercept {
        actor ! LogMessage
      }
    }
    "answer" in {
      val actor: TestActorRef[EchoActor] = TestActorRef(Props(new EchoActor))

      actor ! AnswerMessage

      expectMsg(AnswerMessage)
      actor.underlyingActor.gotCalled should equal(true)
    }
  }
}
