package example
// from https://gist.github.com/tiqwab/aec7018725df7b91d1cb3c7b292158d9
object SampleMain {

  import cats._
  import cats.data._
  import cats.instances.option._

  type StateMachine = StateT[Option, Int, String]

  def initial(input: String): StateMachine = StateT.pure(input)
  def consume(input: String): StateMachine = StateT { s =>
    input.headOption flatMap {
      case 'a' => Some((s, input.tail)) // only consume 'a' now
      case _   => None
    }
  }
  def consumeAll(input: String): StateMachine = input.headOption match {
    case None => StateT.pure("")
    case _    => consume(input) flatMap consumeAll
  }
  def consumeAllTailRec(input: String): StateMachine =
    FlatMap[({ type l[A] = StateT[Option, Int, A] })#l].tailRecM[String, String](input) { str =>
      str.headOption match {
        case None => StateT.pure(Right(""))
        case _    => consume(str) map Left.apply
      }
    }
  def isAcceptable(input: String): Boolean = consumeAllTailRec(input).run(0).isDefined

  // StateMachineReader

  case class TransitionRule(start: Int, char: Char, end: Int)
  type RuleSet = Set[TransitionRule]
  type StateMachineReader = ReaderT[({ type l[A] = StateT[Option, Int, A] })#l, RuleSet, String]

  def initialR(input: String): StateMachineReader = ReaderT.liftF(initial(input))
  def consumeR(input: String): StateMachineReader = {
    def consumeWithRuleSet(str: String, rules: RuleSet): StateMachine =
      for {
        s <- StateT.get[Option, Int]
        c <- StateT.liftF(str.headOption)
        r <- StateT.liftF(rules.find(r => r.start == s && r.char == c))
        _ <- StateT.set[Option, Int](r.end)
      } yield str.tail
    for {
      rules <- ReaderT.ask[({ type l[A] = StateT[Option, Int, A] })#l, RuleSet]
      next <- ReaderT.liftF(consumeWithRuleSet(input, rules))
    } yield next
  }
  def consumeAllR(input: String): StateMachineReader = input.headOption match {
    case None => ReaderT.pure("")
    case _    => consumeR(input) flatMap consumeAllR
  }
  def isAcceptableR(rules: Set[TransitionRule], start: Int, goal: Int)(input: String): Boolean =
    consumeAllR(input).run(rules).run(start).exists { case (s, _) => s == goal }

  def main(args: Array[String]): Unit = {
    // StateMachine

    val res1: StateMachine =
      for {
        str1 <- initial("aaa")
        str2 <- consume(str1)
        str3 <- consume(str2)
        str4 <- consume(str3)
      } yield str4
    println(res1.run(0)) // Some((0,))

    val res2: StateMachine =
      for {
        str1 <- initial("aba")
        str2 <- consume(str1)
        str3 <- consume(str2)
        str4 <- consume(str3)
      } yield str4
    println(res2.run(0)) // None

    println(s"accept 'aaa': ${isAcceptable("aaa")}") // true
    println(s"accept 'aba': ${isAcceptable("aba")}") // false

    // StateMachineReader
    val rules = Set(
      TransitionRule(0, 'a', 1),
      TransitionRule(1, 'b', 0)
    )

    println(s"accept 'ab': ${isAcceptableR(rules, 0, 0)("ab")}") // true
    println(s"accept 'aba': ${isAcceptableR(rules, 0, 1)("aba")}") // true
    println(s"accept 'ab': ${isAcceptableR(rules, 0, 1)("ab")}") // false
    println(s"accept 'aa': ${isAcceptableR(rules, 0, 1)("aa")}") // false
  }

}