package sandbox.fsm

trait Signal[T <: Entity] extends Entity { // -> domain event
  type BODY
  val body: Option[BODY]
  val lifeCycle: SignalLifeCycle
}

sealed trait SignalLifeCycle
object SignalLifeCycle {

  case object Occur extends SignalLifeCycle

  case object Expire extends SignalLifeCycle

}
