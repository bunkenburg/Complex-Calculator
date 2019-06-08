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
import cat.inspiracio.parsing.{Arg, C, Conj, Cos, Cosh, Div, Exp, Expression, Fac, Ln, Minus, Mod, Mult, Opp, Plus, Power, PreMinus, PrePlus, Sin, Sinh, Tan, Tanh, V}

import scala.util.parsing.combinator.JavaTokenParsers

/** An alternative parser, built using Scala's combinator parsing.
  * */
class Parser extends JavaTokenParsers {

  def expr: Parser[Expression] = e0

  /** summands */
  def e0 = e1 ~ rep( "+"~e1 | "-"~e1 ) ^^ {
    case r~rs => {
      (r /: rs) { case (a, c~b ) => if(c=="+") Plus(a,b) else Minus(a,b) }
    }
  }

  /** prefix + - */
  def e1 = rep( "-" | "+" ) ~ e2 ^^ {
    case rs~r =>
      (r /: rs) { (a,c) => if(c=="+") PrePlus(a) else PreMinus(a) }
  }

  /** factors */
  def e2 = e3 ~ rep( "*"~e3 | "/"~e3 ) ^^ {
    case r ~ rs => {
      (r /: rs) { case (a, c~b) => if (c=="*") Mult(a,b) else Div(a,b) }
    }
  }

  /** power */
  def e3 = e4 ~ rep( "\\"~>e4 ) ^^ {
    case r~rs =>
      (r /: rs) { (a,b) => Power(a,b) }
  }

  /** factorial */
  def e4 = e5 ~ rep("!") ^^ {
    case r~rs =>
      (r /: rs) { (a,_) => Fac(a) }
  }

  /** functions */
  def e5 = rep(
    // Mustn't have prefixes before longer strings.
    "arg" | "conj" | "cosh" | "cos" |
      "exp" | "Im" | "ln" | "mod" |
      "opp" | "Re" | "sinh" | "sin" |
      "tanh" | "tan" ) ~ e6 ^^ {
    case rs~r =>
      (r /: rs) { (a,f) => f match {
        case "arg" => Arg(a)
        case "conj" => Conj(a)
        case "cos" => Cos(a)
        case "cosh" => Cosh(a)
        case "exp" => Exp(a)
        case "Im" => parsing.Im(a)
        case "ln" => Ln(a)
        case "mod" => Mod(a)
        case "opp" => Opp(a)
        case "Re" => parsing.Re(a)
        case "sin" => Sin(a)
        case "sinh" => Sinh(a)
        case "tan" => Tan(a)
        case "tanh" => Tanh(a)
        case _ => throw new RuntimeException(f)
      }}
  }

  /** invisible multiplication */
  def e6 = e7 ~ rep(e7) ^^ {
    case r~rs =>
      (r /: rs) { (a,b) => Mult(a,b) }
  }

  def e7: Parser[Expression] =
    ("z" | "x") ^^ (_ => V()) |
      "i" ^^ (_ => C(i)) |
      "e" ^^ (_ => C(e)) |
      "π" ^^ (_ => C(π)) |
      "∞" ^^ (_ => C(∞)) |
      decimalNumber ^^ (d => C(d.toDouble)) |
      "(" ~> e0 <~ ")" |
      "|" ~> e0 <~ "|" ^^ (z => Mod(z))

}
