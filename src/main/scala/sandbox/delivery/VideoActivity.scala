package sandbox.delivery

import sandbox.fsm.Entity

sealed trait VideoActivity extends Entity {
  type ID = Delivery#ID
}
