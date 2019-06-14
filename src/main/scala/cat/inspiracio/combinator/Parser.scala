/*	Copyright 2019 Alexander Bunkenburg alex@inspiracio.cat
 *
 * This file is part of Complex Calculator.
 *
 * Complex Calculator is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Complex Calculator is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Complex Calculator. If not, see <http://www.gnu.org/licenses/>.
 * */
package cat.inspiracio.combinator

import cat.inspiracio.complex._
import cat.inspiracio.parsing
import cat.inspiracio.parsing.{Arg, C, Conj, Cos, Cosh, Div, Exp, Expression, Fac, Ln, Minus, Mod, Mult, Neg, Opp, Plus, Power, Sin, Sinh, Tan, Tanh, V}

import scala.util.matching.Regex
import scala.util.parsing.combinator.JavaTokenParsers

/** An alternative parser, built using Scala's combinator parsing. */
class Parser extends JavaTokenParsers {
  override def skipWhitespace: Boolean = false

  private def eat: Parser[String] = "\\s*".r
  private def space: Parser[String] = "\\s+".r

  /** Adds some methods */
  implicit class AlexParser[T](p: Parser[T]) extends Parser[T]{
    def apply(in: Input): ParseResult[T] = p(in)

    /** p ~* q = p followed by 0-* space followed by q, returning results of p and q */
    def ~* [U](q: Parser[U]): Parser[T~U] = (this <~ eat) ~ q

    /** p ~*> q = p followed by 0-* space followed by q, returning result of q */
    def ~*> [U](q: Parser[U]): Parser[U] = this ~> eat ~> q

    /** p <~* q = p followed by 0-* space followed by q, returning result of p */
    def <~* [U](q: Parser[U]): Parser[T] = this <~ eat <~ q

    /** p ~+ q = p followed by at least one space followed by q, returning both results */
    def ~+ [U](q: Parser[U]): Parser[T~U] = (this <~ space) ~ q

    /** p ~+> q = p followed by at least one space followed by q, returning result of p */
    def ~+> [U](p: Parser[U]): Parser[U] = this ~> space ~> p

    /** p <~+ q = p followed by at least one space followed by q, returning result of p */
    def <~+ [U](p: Parser[U]): Parser[T] = this <~ space ~ p
  }
  implicit def string2AlexParser(s: String): AlexParser[String] = AlexParser(literal(s))

  def apply(input: String): Expression = parseAll(expression, input) match {
    case Success(result, _) => result
    case failure : NoSuccess => scala.sys.error(failure.msg)
  }

  private def expression: Parser[Expression] = eat ~*> summands

  /** Binary + and - binds least of all. */
  private def summands = {
    val next = factors
    next ~* rep( "+" ~* next | "-" ~* next ) ^^ {
      case r~rs => (r /: rs) { case (a,c~b) => if(c=="+") Plus(a,b) else Minus(a,b) }
    }
  }

  /** Binary * and / for multiplication and division.
    *
    * It would be cool to add " " (space) as an alternative
    * multiplication operator, but Scala's combinator parsing
    * library either ignores all white space or never ignores it.
    * It can see no easy way of parsing one space just here.
    * */
  private def factors: Parser[Expression] = {
    val next = powers
    next ~ rep(
      " " ~* next |
        "" ~*> "*" ~* next |
        "" ~*> "/" ~* next
    ) ^^ {
      case r~rs => (r /: rs) {
        case (a, c~b) => c match {
          case " " => Plus(a,b)
          case "*" => Mult(a,b)
          case "/" => Div(a,b)
        }
      }
    }
  }

  /** Binary exponentiation. */
  private def powers = {
    val next = functions
    next ~* rep( "\\" ~*> next ) ^^ {
      case r~rs => (r /: rs) { (a,b) => Power(a,b) }
    }
  }

  /** Functions like -E and sin(E).
    * Can be used with parentheses: sin(z)
    * or without: sin z.
    * Not desired without space: sinz.
    * Binds quite strongly.
    *
    * +E and sin(E) are different.
    * sin(E) sin(E) = (sin(E))\2
    * -E -E != (-E)\2
    *
    * XXX incorporate: after "sin" either " " or "("
    * */
  private def functions: Parser[Expression] = {
    val next = factorial
    "+" ~> functions ^^ (a => a) |        //no space
    "-" ~> functions ^^ (Neg(_)) |        //no space
      "arg("  ~*> expression <~* ")" ^^ (Arg(_)) |
      "arg"   ~+> functions  ^^ (Arg(_)) |
      "conj(" ~*> expression <~* ")" ^^ (Conj(_)) |
      "conj"  ~+> functions  ^^ (Conj(_)) |
      "cosh(" ~*> expression <~* ")" ^^ (Cosh(_)) |
      "cosh"  ~+> functions  ^^ (Cosh(_)) |
      "cos("  ~*> expression <~* ")" ^^ (Cos(_)) |
      "cos"   ~+> functions  ^^ (Cos(_)) |
      "exp("  ~*> expression <~* ")" ^^ (Exp(_)) |
      "exp"   ~+> functions  ^^ (Exp(_)) |
      "Im("   ~*> expression <~* ")" ^^ (parsing.Im(_)) |
      "Im"    ~+> functions  ^^ (parsing.Im(_)) |
      "ln("   ~*> expression <~* ")" ^^ (Ln(_)) |
      "ln"    ~+> functions  ^^ (Ln(_)) |
      "opp("  ~*> expression <~* ")" ^^ (Opp(_)) |
      "opp"   ~+> functions  ^^ (Opp(_)) |
      "Re("   ~*> expression <~* ")" ^^ (parsing.Re(_)) |
      "Re"    ~+> functions  ^^ (parsing.Re(_)) |
      "sinh(" ~*> expression <~* ")" ^^ (Sinh(_)) |
      "sinh"  ~+> functions  ^^ (Sinh(_)) |
      "sin("  ~*> expression <~ ")" ^^ (Sin(_)) |
      "sin"   ~+> functions  ^^ (Sin(_)) |
      "tanh(" ~*> expression <~* ")" ^^ (Tanh(_)) |
      "tanh"  ~+> functions  ^^ (Tanh(_)) |
      "tan("  ~*> expression <~* ")" ^^ (Tan(_)) |
      "tan"   ~+> functions  ^^ (Tan(_)) |
    next
  }

  /** Postfix factorial function: 3!
    * Binds very strongly.
    * Not very important in complex analysis. */
  private def factorial = {
    val next = atom
    next ~ rep("!") ^^ {      //no space
      case r~rs => (r /: rs) { (a,_) => Fac(a) }
    }
  }

  private def atom: Parser[Expression] =
      constants |
      decimal |
      parens |
      abs

  // terminal parsers (they don't delegate to other parsers) ----------------------------

  /** Maybe a decimal number followed by one or more constants,
    * all without space. Examples.
    *
    *   i
    *   2.5i
    *   2zi
    *   zi
    *   πi
    *   ∞
    *
    * https://stackoverflow.com/questions/1815716/accessing-scala-parser-regular-expression-match-data
    * */
  private def constants: Parser[Expression] = {

    val cn = (c: Char) => c match {
      case 'i' => C(i)
      case 'e' => C(e)
      case 'π' => C(π)
      case '∞' => C(∞)
      case 'x' => V()
      case 'z' => V()
    }

    regexMatch("""(\d+(\.\d*)?)?([xzieπ∞]+)""".r) ^^
      ((m: Regex.Match) => {
        val decimal = m.group(1)  // 2.5 or null
        val symbols: String = m.group(3)  // πi or ""
        val constants: Seq[Expression] = symbols.toSeq.map(cn)
        val cs = if (decimal==null) constants else C(decimal.toDouble) +: constants
        cs.reduce( Mult(_,_) )
      })
  }

  private def decimal: Parser[Expression] = decimalNumber ^^ (s => C(s.toDouble))

  private def parens: Parser[Expression] = "(" ~*> expression <~* ")"

  private def abs: Parser[Expression] = "|" ~> expression <~ "|" ^^ (Mod(_))  //no space

  // helpers --------------------------------------------------------

  /** A parser that matches a regex string and returns the Match.
    * https://stackoverflow.com/questions/1815716/accessing-scala-parser-regular-expression-match-data
    * */
  private def regexMatch(r: Regex): Parser[Regex.Match] = new Parser[Regex.Match] {
    def apply(in: Input) = {
      val source = in.source
      val offset = in.offset
      val start = handleWhiteSpace(source, offset)
      (r findPrefixMatchOf (source.subSequence(start, source.length))) match {
        case Some(matched) =>
          Success(matched,
            in.drop(start + matched.end - offset))
        case None =>
          Failure("string matching regex `"+r+"' expected but `"+in.first+"' found", in.drop(start - offset))
      }
    }
  }

}
