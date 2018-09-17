package sandbox.delivery

import java.nio.ByteBuffer

import sandbox.fsm.{Signal, SignalLifeCycle}

trait VideoActivitySignal extends Signal[VideoActivity] {
  type ID = String
  type BODY = ByteBuffer
  val id: String
  val body: Option[ByteBuffer]
}
object VideoActivitySignal {

  case class ViewProgress(id: String,
                          body: Option[ByteBuffer],
                          lifeCycle: SignalLifeCycle)
      extends VideoActivitySignal

  case class ViewComplete(id: String,
                          body: Option[ByteBuffer],
                          lifeCycle: SignalLifeCycle)
      extends VideoActivitySignal

  case class Click(id: String,
                   body: Option[ByteBuffer],
                   lifeCycle: SignalLifeCycle)
      extends VideoActivitySignal

}
