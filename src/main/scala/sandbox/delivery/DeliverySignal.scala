package sandbox.delivery

import java.nio.ByteBuffer

import sandbox.fsm.{Signal, SignalLifeCycle}

sealed trait DeliverySignal extends Signal[Delivery] {
  type ID = String
  type BODY = ByteBuffer
  val id: String
  val body: Option[ByteBuffer]
}

case class Win(id: String, body: Option[ByteBuffer], lifeCycle: SignalLifeCycle)
    extends DeliverySignal

case class ViewProgress(id: String,
                        body: Option[ByteBuffer],
                        lifeCycle: SignalLifeCycle)
    extends DeliverySignal

case class ViewComplete(id: String,
                        body: Option[ByteBuffer],
                        lifeCycle: SignalLifeCycle)
    extends DeliverySignal

case class Click(id: String,
                 body: Option[ByteBuffer],
                 lifeCycle: SignalLifeCycle)
    extends DeliverySignal
