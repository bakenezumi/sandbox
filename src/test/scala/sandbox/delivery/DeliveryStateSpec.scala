package sandbox.delivery

import org.scalatest.FlatSpec
import sandbox.fsm.SignalLifeCycle

class DeliveryStateSpec extends FlatSpec {

  "test" should "test" in {

    val event1: DeliverySignal =
      DeliverySignal.Deliver("001", None, SignalLifeCycle.Occur)

    val event2 = DeliverySignal.Act("002", None, SignalLifeCycle.Occur)

    val state1: DeliveryState = DeliveryState.UnDelivered()

    val state2: DeliveryState =
      state1.handle(event2).asInstanceOf[DeliveryState]

    state2.handle(event1)
  }
}
