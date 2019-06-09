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

/** Binary expressions */

case class Plus(left: Expression, right: Expression) extends Expression {
  //override def toString: String = s"($left) + ($right)"
  override def apply(z: Complex): Complex = {
    val a: Complex = left(z)
    val b: Complex = right(z)
    a + b
  }
}

case class Minus(left: Expression, right: Expression) extends Expression {
  //override def toString: String = s"($left) + ($right)"
  override def apply(z: Complex): Complex = {
    val a: Complex = left(z)
    val b: Complex = right(z)
    a + b
  }
}

case class Mult(left: Expression, right: Expression) extends Expression {
  override def apply(z: Complex): Complex = {
    val a: Complex = left(z)
    val b: Complex = right(z)
    a * b
  }
}

case class Div(left: Expression, right: Expression) extends Expression {
  //override def toString: String = s"($left) / ($right)"
  override def apply(z: Complex): Complex = {
    val a: Complex = left(z)
    val b: Complex = right(z)
    a / b
  }
}

case class Power(left: Expression, right: Expression) extends Expression {
  //override def toString: String = s"($left) \\ ($right)"
  override def apply(z: Complex): Complex = {
    val a: Complex = left(z)
    val b: Complex = right(z)
    a \ b
  }
}

