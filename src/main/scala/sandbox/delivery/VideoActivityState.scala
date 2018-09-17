package sandbox.delivery

import sandbox.fsm.DeferSignalState

sealed trait VideoActivityState extends DeferSignalState[VideoActivity] {
  override type SIGNAL = VideioActivitySignal

  override def handleWithRerun(
      signal: VideioActivitySignal): DeferSignalState[VideoActivity] =
    signal match {
      case s: VideioActivitySignal.ViewProgress => handleViewProgress(s)
      case s: VideioActivitySignal.ViewComplete => handleViewComplete(s)
      case s: VideioActivitySignal.Click        => handleClick(s)
    }

  def handleViewProgress(signal: VideioActivitySignal.ViewProgress)
    : DeferSignalState[VideoActivity]
  def handleViewComplete(signal: VideioActivitySignal.ViewComplete)
    : DeferSignalState[VideoActivity]
  def handleClick(
      signal: VideioActivitySignal.Click): DeferSignalState[VideoActivity]

  def transitImpressed(signal: SIGNAL): DeferSignalState[VideoActivity] =
    transit(signal) { _ =>
      VideoActivityState.UnDelivered()
    }
  def transitActioned(signal: SIGNAL): DeferSignalState[VideoActivity] =
    transit(signal) { _ =>
      VideoActivityState.UnDelivered()
    }

  override def deadLetter(
      signal: VideioActivitySignal): DeferSignalState[VideoActivity] = {
    println("Dead letter:" + signal)
    this
  }
}

object VideoActivityState {

  case class UnDelivered(
      initialDeferredSignals: Set[VideioActivitySignal] = Set(),
      initialHandledSignals: Set[VideioActivitySignal] = Set())
      extends VideoActivityState {
    override def handleViewProgress(signal: VideioActivitySignal.ViewProgress)
      : DeferSignalState[VideoActivity] = ???

    override def handleViewComplete(signal: VideioActivitySignal.ViewComplete)
      : DeferSignalState[VideoActivity] = ???

    override def handleClick(
        signal: VideioActivitySignal.Click): DeferSignalState[VideoActivity] =
      ???

    override val stateId: Byte = 1
  }

}
