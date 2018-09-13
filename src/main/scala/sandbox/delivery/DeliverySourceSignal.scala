package sandbox.delivery

import java.nio.ByteBuffer

import sandbox.fsm.{Signal, SignalLifeCycle}

sealed trait DeliverySourceSignal extends Signal[DeliverySource] {
  type ID = String
  type BODY = ByteBuffer
  val id: String
  val body: Option[ByteBuffer]
}

case class Deliver(id: String,
                   body: Option[ByteBuffer],
                   lifeCycle: SignalLifeCycle)
    extends DeliverySourceSignal
