package sandbox.fsm

import scala.collection.mutable

/**
  * ハンドル可能な状態になるまでイベントのハンドリングを繰り越す
  * @tparam T
  */
trait DeferSignalState[T <: Entity] extends State[T] {

  type STATE = DeferSignalState[T]

  override def handle(signal: SIGNAL): STATE

  val initialDeferredSignals: Set[SIGNAL]

  final lazy val deferredSignals: mutable.Set[SIGNAL] =
    initialDeferredSignals.foldLeft(mutable.Set[SIGNAL]())(_ + _)

  val initialHandledSignals: Set[SIGNAL]

  final lazy val handledSignals: mutable.Set[SIGNAL] =
    initialHandledSignals.foldLeft(mutable.Set[SIGNAL]())(_ + _)

  final def defer(signal: SIGNAL): STATE = {
    println("defer:" + signal)
    deferredSignals += signal
    this
  }

  protected def transit(thunk: => STATE): STATE = {
    val nextState = thunk

    if (deferredSignals.nonEmpty)
      nextState
        .handle(deferredSignals.head.asInstanceOf[nextState.SIGNAL])
    else
      nextState
  }

  def deadLetter(signal: SIGNAL): STATE

}
