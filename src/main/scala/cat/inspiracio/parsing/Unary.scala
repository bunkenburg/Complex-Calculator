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
 * *//*	Copyright 2011 Alexander Bunkenburg alex@cat.inspiracio.com
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
import Syntax._

class Unary(token: Int, argument: Syntax) extends Syntax {

  override def toString: String =
    if (token == FACTOKEN) "(" + argument + ")!"
    else token2String(token) + "(" + argument + ")"

  override def apply(z: Complex): Complex = {
    val a: Complex = argument(z)

    token match {

      case NOTOKEN => throw new RuntimeException("SyntaxTreeUnary.evaluate(NOTOKEN)")

      case ACOSTOKEN => throw new RuntimeException("SyntaxTreeUnary.evaluate(acos,asin,atan)")
      case ASINTOKEN => throw new RuntimeException("SyntaxTreeUnary.evaluate(acos,asin,atan)")
      case ATANTOKEN => throw new RuntimeException("SyntaxTreeUnary.evaluate(acos,asin,atan)")

      case FACTOKEN => fac(a)
      case 0 => a
      case 1 => -a

      case 2 => throw new RuntimeException("SyntaxTreeUnary.evaluate with binary token " + Syntax.token2String(token))
      case 3 => throw new RuntimeException("SyntaxTreeUnary.evaluate with binary token " + Syntax.token2String(token))
      case 4 => throw new RuntimeException("SyntaxTreeUnary.evaluate with binary token " + Syntax.token2String(token))

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

      case _ => throw new RuntimeException("SyntaxTreeUnary.evaluate with unexpected token " + Syntax.token2String(token))
    }
  }

}