/*	Copyright 2011 Alexander Bunkenburg alex@inspiracio.cat
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
package cat.inspiracio.parsing

import cat.inspiracio.complex._
import cat.inspiracio.parsing.Token.Token
import Token._

/** An expression that is a unary function application */
case class U(token: Token, argument: Syntax) extends Syntax {

  override def toString: String =
    if (token == Fac) s"($argument)!"
    else s"$token($argument)"

  override def apply(z: Complex): Complex = {
    val a: Complex = argument(z)

    token match {

      case Fac => fac(a)
      case Plus => a
      case Minus => -a
      case Conj => conj(a)
      case Sinh => sinh(a)
      case Cosh => cosh(a)
      case Tanh => tanh(a)
      case Arg => { val Polar(_, angle) = a; angle }
      case Cos => cos(a)
      case Exp => exp(a)
      case Mod => abs(a)
      case Opp => opp(a)
      case Sin => sin(a)
      case Tan => tan(a)
      case Im => Im(a)
      case Ln => ln(a)
      case ReToken => Re(a)

      case t => throw new RuntimeException(s"SyntaxTreeUnary.evaluate with unexpected token $t")
    }
  }

}