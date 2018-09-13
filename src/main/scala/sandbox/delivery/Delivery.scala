package sandbox.delivery

import sandbox.fsm.Entity

sealed trait Delivery extends Entity {
  type ID = Int
  type SOURCE <: DeliverySource
  type ACTIVITY <: VideoActivity
}
