package sandbox.delivery

import sandbox.fsm.Entity

trait DeliverySource extends Entity {
  type ID = Delivery#ID
}
