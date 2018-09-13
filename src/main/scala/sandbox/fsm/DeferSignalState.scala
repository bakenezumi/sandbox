package sandbox.fsm

import scala.collection.mutable

import scala.reflect.ClassTag

/**
  * ハンドル可能な状態になるまでイベントのハンドリングを繰り越す
  * @tparam T
  */
trait DeferSignalState[T <: Entity] extends State[T] {

  override def handle(signal: SIGNAL): DeferSignalState[T]
  val initialDeferredSignals: Set[SIGNAL]

  final lazy val deferredSignals: mutable.Set[SIGNAL] =
    initialDeferredSignals.foldLeft(mutable.Set[SIGNAL]())(_ + _)

  val initialHandledSignals: Set[SIGNAL]

  final lazy val handledSignals: mutable.Set[SIGNAL] =
    initialHandledSignals.foldLeft(mutable.Set[SIGNAL]())(_ + _)

  final def defer(signal: SIGNAL): DeferSignalState[T] = {
    println("defer:" + signal)
    deferredSignals += signal
    this
  }

  protected def transit(thunk: => DeferSignalState[T]): DeferSignalState[T] = {
    val nextState: DeferSignalState[T] = thunk

    if (deferredSignals.nonEmpty)
      nextState
        .handle(deferredSignals.head.asInstanceOf[nextState.SIGNAL])
    else
      nextState
  }

  def deadLetter(event: SIGNAL): State[T]

}
