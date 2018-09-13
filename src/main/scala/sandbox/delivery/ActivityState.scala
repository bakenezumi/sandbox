package sandbox.delivery

import sandbox.fsm.DeferSignalState

sealed trait ActivityState extends DeferSignalState[VideoActivity] {
  override type SIGNAL = VideioActivitySignal

  override def handle(signal: VideioActivitySignal): STATE =
    signal match {
      case s: VideioActivitySignal.ViewProgress => handleViewProgress(s)
      case s: VideioActivitySignal.ViewComplete => handleViewComplete(s)
      case s: VideioActivitySignal.Click        => handleClick(s)
    }

  def handleViewProgress(signal: VideioActivitySignal.ViewProgress): STATE
  def handleViewComplete(signal: VideioActivitySignal.ViewComplete): STATE
  def handleClick(signal: VideioActivitySignal.Click): STATE

  def transitImpressed(prev: ActivityState): STATE = transit {
    ActivityState.UnDelivered()
  }
  def transitActioned(prev: DeliveryState): STATE = transit {
    ActivityState.UnDelivered()
  }

  override def deadLetter(signal: VideioActivitySignal): STATE = {
    println("Dead letter:" + signal)
    this
  }
}

object ActivityState {

  case class UnDelivered(
      initialDeferredSignals: Set[VideioActivitySignal] = Set(),
      initialHandledSignals: Set[VideioActivitySignal] = Set())
      extends ActivityState {
    override def handleViewProgress(
        signal: VideioActivitySignal.ViewProgress): STATE = ???

    override def handleViewComplete(
        signal: VideioActivitySignal.ViewComplete): STATE = ???

    override def handleClick(signal: VideioActivitySignal.Click): STATE = ???

    override val stateId: Byte = 1
  }

}