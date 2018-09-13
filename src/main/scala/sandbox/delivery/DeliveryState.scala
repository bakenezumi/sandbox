package sandbox.delivery

import sandbox.fsm.DeferSignalState

sealed trait DeliveryState extends DeferSignalState[Delivery] {
  override type SIGNAL = DeliverySignal
  type STATE = DeferSignalState[Delivery]

  override def handle(signal: DeliverySignal): STATE =
    signal match {
      case s: Win          => handleWin(s)
      case s: ViewProgress => handleViewProgress(s)
      case s: ViewComplete => handleViewComplete(s)
      case s: Click        => handleClick(s)
    }

  def handleWin(signal: Win): STATE
  def handleViewProgress(signal: ViewProgress): STATE
  def handleViewComplete(signal: ViewComplete): STATE
  def handleClick(signal: Click): STATE

  def transitImpressed(prev: DeliveryState): STATE =
    transit {
      Impressed(prev)
    }
  def transitActioned(prev: DeliveryState): STATE =
    transit {
      Actioned(prev)
    }

  override def deadLetter(signal: DeliverySignal): STATE = {
    println("Dead letter:" + signal)
    this
  }
}

case class UnDelivered(initialDeferredSignals: Set[DeliverySignal] = Set(),
                       initialHandledSignals: Set[DeliverySignal] = Set())
    extends DeliveryState {

  println("Delivered")

  def handleWin(signal: Win): STATE =
    transitImpressed(this)
  def handleViewProgress(signal: ViewProgress): STATE =
    defer(signal)
  def handleViewComplete(signal: ViewComplete): STATE =
    defer(signal)
  def handleClick(signal: Click): STATE =
    defer(signal)

}

case class Delivered(initialDeferredSignals: Set[DeliverySignal] = Set(),
                     initialHandledSignals: Set[DeliverySignal] = Set())
    extends DeliveryState {

  println("Delivered")

  def handleWin(signal: Win): STATE =
    transitImpressed(this)
  def handleViewProgress(signal: ViewProgress): STATE =
    defer(signal)
  def handleViewComplete(signal: ViewComplete): STATE =
    defer(signal)
  def handleClick(signal: Click): STATE =
    defer(signal)

}

case class Impressed(prevState: DeliveryState,
                     initialDeferredSignals: Set[DeliverySignal] = Set(),
                     initialHandledSignals: Set[DeliverySignal] = Set())
    extends DeliveryState {

  println("Impressed")

  def handleWin(signal: Win): STATE =
    deadLetter(signal)
  def handleViewProgress(signal: ViewProgress): STATE =
    transitActioned(this)
  def handleViewComplete(signal: ViewComplete): STATE =
    transitActioned(this)
  def handleClick(signal: Click): STATE =
    transitActioned(this)
}

case class Actioned(prevState: DeliveryState,
                    initialDeferredSignals: Set[DeliverySignal] = Set(),
                    initialHandledSignals: Set[DeliverySignal] = Set())
    extends DeliveryState {

  println("Actioned")

  def handleWin(signal: Win): STATE =
    deadLetter(signal)
  def handleViewProgress(signal: ViewProgress): STATE =
    transitActioned(this)
  def handleViewComplete(signal: ViewComplete): STATE =
    transitActioned(this)
  def handleClick(signal: Click): STATE =
    transitActioned(this)
}
