package sandbox.delivery

import sandbox.fsm.DeferSignalState

sealed trait VideoActivityState extends DeferSignalState[VideoActivity] {
  override type SIGNAL = VideoActivitySignal

  override def handleWithRerun(
      signal: VideoActivitySignal): DeferSignalState[VideoActivity] =
    signal match {
      case s: VideoActivitySignal.ViewProgress => handleViewProgress(s)
      case s: VideoActivitySignal.ViewComplete => handleViewComplete(s)
      case s: VideoActivitySignal.Click        => handleClick(s)
    }

  def handleViewProgress(
      signal: VideoActivitySignal.ViewProgress): DeferSignalState[VideoActivity]
  def handleViewComplete(
      signal: VideoActivitySignal.ViewComplete): DeferSignalState[VideoActivity]
  def handleClick(
      signal: VideoActivitySignal.Click): DeferSignalState[VideoActivity]

  def transitImpressed(signal: SIGNAL): DeferSignalState[VideoActivity] =
    transit(signal) { _ =>
      VideoActivityState.UnDelivered()
    }
  def transitActioned(signal: SIGNAL): DeferSignalState[VideoActivity] =
    transit(signal) { _ =>
      VideoActivityState.UnDelivered()
    }

  override def deadLetter(
      signal: VideoActivitySignal): DeferSignalState[VideoActivity] = {
    println("Dead letter:" + signal)
    this
  }
}

object VideoActivityState {

  case class UnDelivered(
      initialDeferredSignals: Set[VideoActivitySignal] = Set(),
      initialHandledSignals: Set[VideoActivitySignal] = Set())
      extends VideoActivityState {
    override def handleViewProgress(signal: VideoActivitySignal.ViewProgress)
      : DeferSignalState[VideoActivity] = ???

    override def handleViewComplete(signal: VideoActivitySignal.ViewComplete)
      : DeferSignalState[VideoActivity] = ???

    override def handleClick(
        signal: VideoActivitySignal.Click): DeferSignalState[VideoActivity] =
      ???

    override val stateId: Byte = 1
  }

}
