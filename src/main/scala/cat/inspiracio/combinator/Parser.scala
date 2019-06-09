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
import cat.inspiracio.parsing.{Arg, C, Conj, Cos, Cosh, Div, Exp, Expression, Fac, Ln, Minus, Mod, Mult, Opp, Plus, Power, Neg, Sin, Sinh, Tan, Tanh, V}

import scala.util.parsing.combinator.JavaTokenParsers

/** An alternative parser, built using Scala's combinator parsing.
  *
  * XXX Not ready yet. Does not accept many expressions that
  * occur in maths books. See ParserTest.
  * Most important: better-prefix e\\-πi That must work as expected.
  * */
class Parser extends JavaTokenParsers {

  def apply(input: String): Expression = parseAll(expr, input) match {
    case Success(result, _) => result
    case failure : NoSuccess => scala.sys.error(failure.msg)
  }

  private def expr: Parser[Expression] = summands

  private def summands = prefix ~ rep( "+"~prefix | "-"~prefix ) ^^ {
    case r~rs => (r /: rs) { case (a, c~b ) => if(c=="+") Plus(a,b) else Minus(a,b) }
  }

  private def prefix = rep( "-" | "+" ) ~ factor ^^ {
    case rs~r => (rs :\ r) { (c,a) => if(c=="+") a else Neg(a) }
  }

  private def factor = powers ~ rep( "*"~powers | "/"~powers ) ^^ {
    case r~rs => (r /: rs) { case (a, c~b) => if (c=="*") Mult(a,b) else Div(a,b) }
  }

  private def powers = functions ~ rep( "\\"~>functions ) ^^ {
    case r~rs => (r /: rs) { (a,b) => Power(a,b) }
  }


  private def functions =
    "arg"~>im ^^ (Arg(_)) |
      "conj"~>im ^^ (Conj(_)) |
      "cosh"~>im ^^ (Cosh(_)) |
      "cos"~>im ^^ (Cos(_)) |
      "exp"~>im ^^ (Exp(_)) |
      "Im"~>im ^^ (parsing.Im(_)) |
      "ln"~>im ^^ (Ln(_)) |
      "mod"~>im ^^ (Mod(_)) |
      "opp"~>im ^^ (Opp(_)) |
      "Re"~>im ^^ (parsing.Re(_)) |
      "sinh"~>im ^^ (Sinh(_)) |
      "sin"~>im ^^ (Sin(_)) |
      "tanh"~>im ^^ (Tanh(_)) |
      "tan"~>im ^^ (Tan(_)) |
    im

  /** im = invisible multiplication
    *
    * XXX Sharpen this.
    *
    * 1. forbid whitespace between factors
    * 2. decimal number can only be first factor
    * 3. other factors can only be single-char or (E)
    *
    * */
  private def im = factorial ~ rep(factorial) ^^ {
    case r~rs => (r /: rs) { (a,b) => Mult(a,b) }
  }

  private def factorial = e7 ~ rep("!") ^^ {
    case r~rs => (r /: rs) { (a,_) => Fac(a) }
  }

  private def e7: Parser[Expression] =
    variable | constant | decimal |
      parens |
      abs

  // terminal parsers (they don't delegate to other parsers) ---

  private def variable: Parser[Expression] = ("z" | "x") ^^ (_ => V())

  private def constant: Parser[Expression] =
      "i" ^^ (_ => C(i)) |
      "e" ^^ (_ => C(e)) |
      "π" ^^ (_ => C(π)) |
      "∞" ^^ (_ => C(∞))

  def decimal: Parser[Expression] = decimalNumber ^^ (s => C(s.toDouble))

  def parens: Parser[Expression] = "("~>summands<~")"

  def abs: Parser[Expression] = "|"~>summands<~"|" ^^ (Mod(_))

}
