package sandbox.delivery

import java.nio.ByteBuffer

import sandbox.fsm.{Signal, SignalLifeCycle}

trait VideioActivitySignal extends Signal[VideoActivity] {
  type ID = String
  type BODY = ByteBuffer
  val id: String
  val body: Option[ByteBuffer]
}
object VideioActivitySignal {

  case class ViewProgress(id: String,
                          body: Option[ByteBuffer],
                          lifeCycle: SignalLifeCycle)
      extends VideioActivitySignal

  case class ViewComplete(id: String,
                          body: Option[ByteBuffer],
                          lifeCycle: SignalLifeCycle)
      extends VideioActivitySignal

  case class Click(id: String,
                   body: Option[ByteBuffer],
                   lifeCycle: SignalLifeCycle)
      extends VideioActivitySignal

}
