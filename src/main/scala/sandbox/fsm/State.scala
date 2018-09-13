package sandbox.fsm

trait State[T <: Entity] {
  type SIGNAL <: Signal[T]
  def handle(signal: SIGNAL): State[T]
}
