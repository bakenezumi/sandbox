package sandbox.fsm

import scala.collection.mutable

/**
  * 処理できないイベントを持ち越し、ハンドル可能な状態になるまでハンドリングを繰り越す
  * @tparam T
  */
trait DeferSignalState[T <: Entity] extends State[T] {

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

  final def handle(signal: SIGNAL): DeferSignalState[T] = {
    println("handle:" + signal)
    handledSignals += signal
    handleWithRerun(signal)
  }

  def handleWithRerun(signal: SIGNAL): DeferSignalState[T]

  protected def transit(signal: SIGNAL)(
      handler: SIGNAL => DeferSignalState[T]): DeferSignalState[T] = {
    val nextState = handler(signal)

    for (signal <- deferredSignals) {
      println("rerun:" + signal)
      val kk = nextState.handle(signal.asInstanceOf[nextState.SIGNAL])
      if (kk != nextState) {
        // TODO: 状態の永続化
        deferredSignals -= signal
        println("rerun success:" + signal)

        return kk
      }
    }
    nextState
  }

  def deadLetter(signal: SIGNAL): DeferSignalState[T]

}
