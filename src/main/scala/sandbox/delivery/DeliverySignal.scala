package sandbox.delivery

import java.nio.ByteBuffer

import sandbox.fsm.{Signal, SignalLifeCycle}

sealed trait DeliverySignal extends Signal[Delivery] {
  type ID = String
  type BODY = ByteBuffer
  val id: String
  val body: Option[ByteBuffer]
}

object DeliverySignal {

  case class Deliver(id: String,
                     body: Option[ByteBuffer],
                     lifeCycle: SignalLifeCycle)
      extends DeliverySignal

  case class Impress(id: String,
                     body: Option[ByteBuffer],
                     lifeCycle: SignalLifeCycle)
      extends DeliverySignal

  case class Act(id: String,
                 body: Option[ByteBuffer],
                 lifeCycle: SignalLifeCycle)
      extends DeliverySignal

}
