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

  /** Unary prefix + and -. Binds quite strongly.
    * Parsing swallows prefix +. */
  private def prefix = {
    val next = functions
    rep( "-" | "+" ) ~ next ^^ {
      case rs~r => (rs :\ r) { (c,a) => if(c=="+") a else Neg(a) }
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

  /** im = invisible multiplication
    *
    * Multiplication without space.
    * Should bind very strongly.
    * Motivated by expressions like
    *
    *   e\πi = e\(πi)
    *   sin πi = sin(πi)
    *
    * XXX Sharpen this.
    *
    * 1. forbid whitespace between factors
    * 2. decimal number can only be first factor
    * 3. other factors can only be single-char
    * 4. ... or maybe also (E)
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
    val next = e7
    next ~ rep("!") ^^ {
      case r~rs => (r /: rs) { (a,_) => Fac(a) }
    }
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
