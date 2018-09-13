package sandbox.fsm

trait State[T <: Entity] {
  type SIGNAL <: Signal[T]

  /**
    *  各状態は必ずユニークなstateIdを定義すること
    **/
  val stateId: Byte
  def handle(signal: SIGNAL): State[T]
}
