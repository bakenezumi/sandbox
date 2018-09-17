package sandbox.fsm

import scala.collection.mutable

/**
  * ハンドル可能な状態になるまでイベントのハンドリングを繰り越す
  * @tparam T
  */
trait DeferSignalState[T <: Entity] extends State[T] {

  type STATE = DeferSignalState[T]

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

  final def handle(signal: SIGNAL): STATE = {
    println("handle:" + signal)
    handledSignals += signal
    handleWithRerun(signal)
  }

  def handleWithRerun(signal: SIGNAL): STATE


  protected def transit(thunk: => STATE): STATE = {
    val nextState  = thunk

    for(signal <- deferredSignals)  {
      println("rerun:" + signal)
      val kk = nextState.handle(signal.asInstanceOf[nextState.SIGNAL])
      if(kk != nextState) {

        println("rerun success:" + signal)

        return kk
      }
    }
    nextState
  }

  def deadLetter(signal: SIGNAL): STATE

}
