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

// Referenced classes of package bunkenba.parsing:
//            SyntaxTree

class Binary(token: Token, left: Syntax, right: Syntax) extends Syntax {

  override def toString: String = s"($left)$token($right)"

  override def apply(z: Complex): Complex = {

    val a: Complex = left(z)
    val b: Complex = right(z)

    token match {
      case SUMTOKEN => a + b
      case DIFFERENCETOKEN => a - b
      case PRODUCTTOKEN => a * b
      case QUOTIENTTOKEN => a / b
      case POWERTOKEN => a ^ b
      case t => throw new RuntimeException(s"SyntaxTreeBinary.evaluate with token $t")
    }

  }
}