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

import java.awt.Dimension

import cat.inspiracio.complex._
import cat.inspiracio.complex

import scala.swing.Graphics2D

/** An expression that is a unary function application */

abstract class Unary extends Expression {

  /** Paints the expression in this rectangle, which is of preferred size for the expression. */
  override def paint(g: Graphics2D, rect: swing.Rectangle) = {
    val s = toString
    draw(g, rect.x, rect.y, s)
  }

  /** Returns dimension for a good rendering of this expression */
  override def preferredSize(g: Graphics2D): Dimension = {
    val s = toString
    preferredSize(g, s)
  }

}

abstract class Prefix(pre: String) extends Unary

case class Arg(a: Expression) extends Prefix("arg") {
  override def apply(z: Complex): Complex = { val Polar(_, angle) = a(z); angle }
}

case class Conj(a: Expression) extends Prefix("conj") {
  override def apply(z: Complex): Complex = conj(a(z))
}

case class Cos(a: Expression) extends Prefix("cos") {
  override def apply(z: Complex): Complex = cos(a(z))
}

case class Cosh(a: Expression) extends Prefix("cosh") {
  override def apply(z: Complex): Complex = cosh(a(z))
}

case class Exp(a: Expression) extends Prefix("exp") {
  override def apply(z: Complex): Complex = exp(a(z))
}

case class Im(a: Expression) extends Prefix("Im") {
  override def apply(z: Complex): Complex = complex.Im(a(z))
}

case class Ln(a: Expression) extends Prefix("ln") {
  override def apply(z: Complex): Complex = ln(a(z))
}

case class Opp(a: Expression) extends Prefix("opp") {
  override def apply(z: Complex): Complex = opp(a(z))
}

case class Neg(a: Expression) extends Prefix("-") {
  override def apply(z: Complex): Complex = -a(z)
}

case class Re(a: Expression) extends Prefix("Re") {
  override def apply(z: Complex): Complex = complex.Re(a(z))
}

case class Sin(a: Expression) extends Prefix("sin") {
  override def apply(z: Complex): Complex = sin(a(z))
}

case class Sinh(a: Expression) extends Prefix("sinh") {
  override def apply(z: Complex): Complex = sinh(a(z))
}

case class Tan(a: Expression) extends Prefix("tan") {
  override def apply(z: Complex): Complex = tan(a(z))
}

case class Tanh(a: Expression) extends Prefix("tanh") {
  override def apply(z: Complex): Complex = tanh(a(z))
}

//-------------------------------------------------

case class Fac(a: Expression) extends Unary {
  override def apply(z: Complex): Complex = fac(a(z))
}

//-------------------------------------------------

case class Mod(a: Expression) extends Unary {
  override def apply(z: Complex): Complex = abs(a(z))
}
