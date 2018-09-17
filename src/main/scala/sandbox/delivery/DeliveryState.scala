package sandbox.delivery

import sandbox.fsm.DeferSignalState

sealed trait DeliveryState extends DeferSignalState[Delivery] {
  override type SIGNAL = DeliverySignal

  override def handleWithRerun(signal: DeliverySignal): STATE =
    signal match {
      case s: DeliverySignal.Deliver => handleDeliver(s)
      case s: DeliverySignal.Impress => handleImpress(s)
      case s: DeliverySignal.Act     => handleAct(s)
    }

  def handleDeliver(signal: DeliverySignal.Deliver): STATE
  def handleImpress(signal: DeliverySignal.Impress): STATE
  def handleAct(signal: DeliverySignal.Act): STATE

  def transitImpressed(signal: SIGNAL): STATE =
    transit(signal) { _ =>
      DeliveryState.Impressed()
    }
  def transitActioned(signal: SIGNAL): STATE =
    transit(signal) { _ =>
      DeliveryState.Actioned()
    }

  override def deadLetter(signal: SIGNAL): STATE = {
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

    def handleDeliver(signal: DeliverySignal.Deliver): STATE =
      transitImpressed(signal)

    def handleImpress(signal: DeliverySignal.Impress): STATE =
      defer(signal)

    def handleAct(signal: DeliverySignal.Act): STATE =
      defer(signal)

  }

  case class Delivered(initialDeferredSignals: Set[DeliverySignal] = Set(),
                       initialHandledSignals: Set[DeliverySignal] = Set())
      extends DeliveryState {
    val stateId: Byte = 1

    println("##[Delivered]")

    def handleDeliver(signal: DeliverySignal.Deliver): STATE =
      transitImpressed(signal)

    def handleImpress(signal: DeliverySignal.Impress): STATE =
      defer(signal)

    def handleAct(signal: DeliverySignal.Act): STATE =
      defer(signal)

  }

  case class Impressed(initialDeferredSignals: Set[DeliverySignal] = Set(),
                       initialHandledSignals: Set[DeliverySignal] = Set())
      extends DeliveryState {
    val stateId: Byte = 2

    println("##[Impressed]")

    def handleDeliver(signal: DeliverySignal.Deliver): STATE =
      deadLetter(signal)

    def handleImpress(signal: DeliverySignal.Impress): STATE =
      transitActioned(signal)

    def handleAct(signal: DeliverySignal.Act): STATE =
      transitActioned(signal)

  }

  case class Actioned(initialDeferredSignals: Set[DeliverySignal] = Set(),
                      initialHandledSignals: Set[DeliverySignal] = Set())
      extends DeliveryState {
    val stateId: Byte = 4

    println("##[Actioned]")

    def handleDeliver(signal: DeliverySignal.Deliver): STATE =
      deadLetter(signal)

    def handleImpress(signal: DeliverySignal.Impress): STATE =
      transitActioned(signal)

    def handleAct(signal: DeliverySignal.Act): STATE =
      transitActioned(signal)
  }

}
