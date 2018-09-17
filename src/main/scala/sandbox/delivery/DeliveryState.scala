package sandbox.delivery

import sandbox.fsm.DeferSignalState

sealed trait DeliveryState extends DeferSignalState[Delivery] {
  override type SIGNAL = DeliverySignal

  override def handleWithRerun(
      signal: DeliverySignal): DeferSignalState[Delivery] =
    signal match {
      case s: DeliverySignal.Deliver => handleDeliver(s)
      case s: DeliverySignal.Impress => handleImpress(s)
      case s: DeliverySignal.Act     => handleAct(s)
    }

  def handleDeliver(signal: DeliverySignal.Deliver): DeferSignalState[Delivery]
  def handleImpress(signal: DeliverySignal.Impress): DeferSignalState[Delivery]
  def handleAct(signal: DeliverySignal.Act): DeferSignalState[Delivery]

  def transitImpressed(signal: SIGNAL): DeferSignalState[Delivery] =
    transit(signal) { _ =>
      DeliveryState.Impressed()
    }
  def transitActioned(signal: SIGNAL): DeferSignalState[Delivery] =
    transit(signal) { _ =>
      DeliveryState.Actioned()
    }

  override def deadLetter(signal: SIGNAL): DeferSignalState[Delivery] = {
    println("Dead letter:" + signal)
    this
  }
}

object DeliveryState {

  case class UnDelivered(initialDeferredSignals: Set[DeliverySignal] = Set(),
                         initialHandledSignals: Set[DeliverySignal] = Set())
      extends DeliveryState {
    val stateId: Byte = 0

    println("##[UnDelivered]")

    def handleDeliver(
        signal: DeliverySignal.Deliver): DeferSignalState[Delivery] =
      transitImpressed(signal)

    def handleImpress(
        signal: DeliverySignal.Impress): DeferSignalState[Delivery] =
      defer(signal)

    def handleAct(signal: DeliverySignal.Act): DeferSignalState[Delivery] =
      defer(signal)

  }

  case class Delivered(initialDeferredSignals: Set[DeliverySignal] = Set(),
                       initialHandledSignals: Set[DeliverySignal] = Set())
      extends DeliveryState {
    val stateId: Byte = 1

    println("##[Delivered]")

    def handleDeliver(
        signal: DeliverySignal.Deliver): DeferSignalState[Delivery] =
      transitImpressed(signal)

    def handleImpress(
        signal: DeliverySignal.Impress): DeferSignalState[Delivery] =
      defer(signal)

    def handleAct(signal: DeliverySignal.Act): DeferSignalState[Delivery] =
      defer(signal)

  }

  case class Impressed(initialDeferredSignals: Set[DeliverySignal] = Set(),
                       initialHandledSignals: Set[DeliverySignal] = Set())
      extends DeliveryState {
    val stateId: Byte = 2

    println("##[Impressed]")

    def handleDeliver(
        signal: DeliverySignal.Deliver): DeferSignalState[Delivery] =
      deadLetter(signal)

    def handleImpress(
        signal: DeliverySignal.Impress): DeferSignalState[Delivery] =
      transitActioned(signal)

    def handleAct(signal: DeliverySignal.Act): DeferSignalState[Delivery] =
      transitActioned(signal)

  }

  case class Actioned(initialDeferredSignals: Set[DeliverySignal] = Set(),
                      initialHandledSignals: Set[DeliverySignal] = Set())
      extends DeliveryState {
    val stateId: Byte = 4

    println("##[Actioned]")

    def handleDeliver(
        signal: DeliverySignal.Deliver): DeferSignalState[Delivery] =
      deadLetter(signal)

    def handleImpress(
        signal: DeliverySignal.Impress): DeferSignalState[Delivery] =
      transitActioned(signal)

    def handleAct(signal: DeliverySignal.Act): DeferSignalState[Delivery] =
      transitActioned(signal)
  }

}
