package test

import akka.actor.{ActorSystem, Props}
import akka.testkit.{EventFilter, ImplicitSender, TestActorRef, TestKit, TestProbe}
import com.typesafe.config.ConfigFactory
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}
import test.EchoActor.{AnswerMessage, LogMessage}

import scala.concurrent.Future
import scala.concurrent.duration._

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
  with Matchers {

  import system.dispatcher

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
    "access underlying actor without msg" in {
      val f = Future[TestActorRef[EchoActor]] {
        TestActorRef(Props(new EchoActor))
      }
      system.scheduler.scheduleOnce(0.millis) {
        f
      }
      awaitCond(f.isCompleted)

      f.value.get.get.underlyingActor.gotCalled should equal(false)
    }

    "throw a match error" in {
      val watcher = TestProbe()
      val actor: TestActorRef[EchoActor] = TestActorRef(Props(new EchoActor))
      watcher watch actor

      actor ! 5

      watcher.expectMsgClass(classOf[MatchError])
    }
  }
}
