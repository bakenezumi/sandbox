package sandbox.delivery

import org.scalatest.FlatSpec
import sandbox.fsm.SignalLifeCycle

class DeliveryStateSpec extends FlatSpec {

  "test" should "test" in {

    val event1: DeliverySignal = Win("001", null, SignalLifeCycle.Occur)

    val event2 = Click("002", null, SignalLifeCycle.Occur)

    val state1: DeliveryState = UnDelivered()

    val state2: DeliveryState =
      state1.handle(event2).asInstanceOf[DeliveryState]

    state2.handle(event1)
  }
}
