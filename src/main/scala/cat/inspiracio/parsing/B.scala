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

/** Binary expression */
case class B(left: Syntax, token: Token, right: Syntax) extends Syntax {

  override def toString: String = s"($left)$token($right)"

  override def apply(z: Complex): Complex = {

    val a: Complex = left(z)
    val b: Complex = right(z)

    token match {
      case Plus => a + b
      case Minus => a - b
      case Mult => a * b
      case Div => a / b
      case Power => a \ b
      case t => throw new RuntimeException(s"SyntaxTreeBinary.evaluate with token $t")
    }

  }
}