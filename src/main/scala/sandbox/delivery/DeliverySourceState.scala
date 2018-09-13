package sandbox.delivery

import sandbox.fsm.{DeferSignalState, State}

sealed trait DeliverySourceState extends DeferSignalState[DeliverySource] {

  override type SIGNAL = DeliverySourceSignal

  override def deadLetter(
      signal: DeliverySourceSignal): State[DeliverySource] = {
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
