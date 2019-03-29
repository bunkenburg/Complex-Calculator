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

// Referenced classes of package bunkenba.parsing:
//            SyntaxTree

import cat.inspiracio.complex._
import cat.inspiracio.parsing.Token.Token
import Token._

class Unary(token: Token, argument: Syntax) extends Syntax {

  override def toString: String =
    if (token == FACTOKEN) s"($argument)!"
    else s"$token($argument)"

  override def apply(z: Complex): Complex = {
    val a: Complex = argument(z)

    token match {

      case FACTOKEN => fac(a)
      case SUMTOKEN => a
      case DIFFERENCETOKEN => -a
      case CONJTOKEN => conj(a)
      case SINHTOKEN => sinh(a)
      case COSHTOKEN => cosh(a)
      case TANHTOKEN => tanh(a)
      case ARGTOKEN => { val Polar(_, angle) = a; angle }
      case COSTOKEN => cos(a)
      case EXPTOKEN => exp(a)
      case MODTOKEN => abs(a)
      case OPPTOKEN => opp(a)
      case SINTOKEN => sin(a)
      case TANTOKEN => tan(a)
      case IMTOKEN => Im(a)
      case LNTOKEN => ln(a)
      case RETOKEN => Re(a)

      case t => throw new RuntimeException(s"SyntaxTreeUnary.evaluate with unexpected token $t")
    }
  }

}