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

class SyntaxTreeUnary(var token: Int, var argument: SyntaxTree) extends SyntaxTree {

  override def unparse: String =
    if (token == 20) "(" + argument.unparse + ")!"
    else SyntaxTree.token2String(token) + "(" + argument.unparse + ")"

  override def evaluate(z: Complex): Complex = {
    val a: Complex = argument.evaluate(z)

    token match {

      case -1 => throw new RuntimeException("SyntaxTreeUnary.evaluate(NOTOKEN)")

      case 21 => throw new RuntimeException("SyntaxTreeUnary.evaluate(acos,asin,atan)")
      case 22 => throw new RuntimeException("SyntaxTreeUnary.evaluate(acos,asin,atan)")
      case 23 => throw new RuntimeException("SyntaxTreeUnary.evaluate(acos,asin,atan)")

      case 20 => fac(a)
      case 0 => a
      case 1 => -a

      case 2 => throw new RuntimeException("SyntaxTreeUnary.evaluate with binary token " + SyntaxTree.token2String(token))
      case 3 => throw new RuntimeException("SyntaxTreeUnary.evaluate with binary token " + SyntaxTree.token2String(token))
      case 4 => throw new RuntimeException("SyntaxTreeUnary.evaluate with binary token " + SyntaxTree.token2String(token))

      case 5 => conj(a)
      case 19 => sinh(a)
      case 6 => cosh(a)
      case 7 => tanh(a)
      case 8 => { val Polar(_, angle) = a; angle }
      case 9 => cos(a)
      case 10 => exp(a)
      case 11 => abs(a)
      case 12 => opp(a)
      case 13 => sin(a)
      case 14 => tan(a)
      case 15 => Im(a)
      case 16 => ln(a)
      case 17 => Re(a)

      case 18 => throw new RuntimeException("SyntaxTreeUnary.evaluate with unexpected token " + SyntaxTree.token2String(token))
    }
  }

}