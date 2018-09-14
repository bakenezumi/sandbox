package sandbox.delivery

import sandbox.fsm.DeferSignalState

sealed trait DeliverySourceState extends DeferSignalState[DeliverySource] {

  override type SIGNAL = DeliverySourceSignal

  override def deadLetter(signal: DeliverySourceSignal): STATE = {
    println("Dead letter:" + signal)
    this
  }

  val isImpressible: Boolean

}

object DeliverySourceState {

  trait UnDelivered extends DeliverySource {
    val isImpressible = false
  }

  trait Delivered extends DeliverySource {
    val isImpressible = true
  }

}
