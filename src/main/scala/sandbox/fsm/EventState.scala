package sandbox.fsm

trait EventState[E <: Event] {

  val pends: Set[E]

  def verify(event: E) : Boolean

  def next : E

}
