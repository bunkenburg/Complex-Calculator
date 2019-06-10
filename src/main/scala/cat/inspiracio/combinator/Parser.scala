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

/** An alternative parser, built using Scala's combinator parsing.
  *
  * XXX Not ready yet. Does not accept many expressions that
  * occur in maths books. See ParserTest.
  * */
class Parser extends JavaTokenParsers {

  def apply(input: String): Expression = parseAll(expr, input) match {
    case Success(result, _) => result
    case failure : NoSuccess => scala.sys.error(failure.msg)
  }

  private def expr: Parser[Expression] = summands

  /** Binary + and - binds least of all. */
  private def summands = {
    val next = factors
    next ~ rep( "+"~next | "-"~next ) ^^ {
      case r~rs => (r /: rs) { case (a,c~b) => if(c=="+") Plus(a,b) else Minus(a,b) }
    }
  }

  /** Binary * and / for multiplication and division. */
  private def factors = {
    val next = powers
    next ~ rep( "*"~next | "/"~next ) ^^ {
      case r~rs => (r /: rs) { case (a, c~b) => if(c=="*") Mult(a,b) else Div(a,b) }
    }
  }

  /** Binary exponentiation. */
  private def powers = {
    val next = functions
    next ~ rep( "\\"~>next ) ^^ {
      case r~rs => (r /: rs) { (a,b) => Power(a,b) }
    }
  }

  /** Functions like sin.
    * Can be used with parentheses: sin(z)
    * or without: sin z.
    * Not desired without space: sinz.
    * Binds quite strongly. */
  private def functions: Parser[Expression] = {
    val next = im
    "+" ~> functions ^^ (a => a) |
    "-" ~> functions ^^ (Neg(_)) |
    "arg" ~> functions ^^ (Arg(_)) |
    "conj" ~> functions ^^ (Conj(_)) |
    "cosh" ~> functions ^^ (Cosh(_)) |
    "cos" ~> functions ^^ (Cos(_)) |
    "exp" ~> functions ^^ (Exp(_)) |
    "Im" ~> functions ^^ (parsing.Im(_)) |
    "ln" ~> functions ^^ (Ln(_)) |
    "mod" ~> functions ^^ (Mod(_)) |
    "opp" ~> functions ^^ (Opp(_)) |
    "Re" ~> functions ^^ (parsing.Re(_)) |
    "sinh" ~> functions ^^ (Sinh(_)) |
    "sin" ~> functions ^^ (Sin(_)) |
    "tanh" ~> functions ^^ (Tanh(_)) |
    "tan" ~> functions ^^ (Tan(_)) |
    next
  }

  /** im = implicit multiplication
    *
    * Multiplication with space.
    * Equivalent to *, also in precedence.
    * Motivated by expressions like
    *
    *   i sin z = i * sin(z)
    *   sin(z) cos(z) = sin(z) * cos(z)
    *
    * XXX Sharpen this.
    *   Ask for space explicitly.
    *   Put it in the right precedence.
    *
    * */
  private def im = {
    val next = factorial
    next ~ rep(next) ^^ {
      case r~rs => (r /: rs) { (a,b) => Mult(a,b) }
    }
  }

  /** Postfix factorial function: 3!
    * Binds very strongly.
    * Not very important in complex analysis. */
  private def factorial = {
    val next = atoms
    next ~ rep("!") ^^ {
      case r~rs => (r /: rs) { (a,_) => Fac(a) }
    }
  }

  private def atoms: Parser[Expression] =
      constants |
      decimal |
      parens |
      abs

  // terminal parsers (they don't delegate to other parsers) ---

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

  private def parens: Parser[Expression] = "("~>summands<~")"

  private def abs: Parser[Expression] = "|"~>summands<~"|" ^^ (Mod(_))

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
