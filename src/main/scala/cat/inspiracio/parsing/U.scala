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

import java.awt.font.TextLayout
import java.awt.geom.{Dimension2D, Rectangle2D}

import cat.inspiracio.complex._
import cat.inspiracio.complex

import scala.swing.Graphics2D

/** An expression that is a unary function application */

abstract class Unary(a: Expression) extends Expression {

  /** Returns dimension for a good rendering of this expression */
  def preferredSize(g: Graphics2D): Dimension2D

  /** Lay out the expression in this rectangle,
   * which usually will be the preferred size of the expression,
   * unless the expression is too big for the Editor.
   *
   * This methods sizes and positions all the TextLayout objects
   * in the expression. After that, the expression is ready for painting.
   * */
  override def layout(g: Graphics2D, bounds: Rectangle2D): Unit = {}

  /** Paints this expression.
   * Its text layouts have already been created, scaled, and positioned. */
  override def paint(g: Graphics2D): Unit = {}

}

abstract class Prefix(pre: String, a: Expression) extends Unary(a){

  /** for the prefix text */
  private var tl: TextLayout = null

  /** Returns dimension for a good rendering of this expression */
  def preferredSize(g: Graphics2D): Dimension2D = {
    tl = textlayout(g, pre)
    val dim = a.preferredSize(g)
    //  tl.getBounds | dim      // XXX Rectangle2D beside Dimension2D : Dimension2D
    ???
  }

  /** Lay out the expression in this rectangle,
   * which usually will be the preferred size of the expression,
   * unless the expression is too big for the Editor.
   *
   * This methods sizes and positions all the TextLayout objects
   * in the expression. After that, the expression is ready for painting.
   * */
  override def layout(g: Graphics2D, bounds: Rectangle2D): Unit = {
    // tl beside a box containing layout of a
    ???
  }

  /** Paints this expression.
   * Its text layouts have already been created, scaled, and positioned. */
  override def paint(g: Graphics2D): Unit = {
    //paint tl
    //paint a
    //box around a
    ???
  }

}

case class Arg(a: Expression) extends Prefix("arg", a) {
  override def apply(z: Complex): Complex = { val Polar(_, angle) = a(z); angle }
}

case class Conj(a: Expression) extends Prefix("conj", a) {
  override def apply(z: Complex): Complex = conj(a(z))
}

case class Cos(a: Expression) extends Prefix("cos", a) {
  override def apply(z: Complex): Complex = cos(a(z))
}

case class Cosh(a: Expression) extends Prefix("cosh", a) {
  override def apply(z: Complex): Complex = cosh(a(z))
}

case class Exp(a: Expression) extends Prefix("exp", a) {
  override def apply(z: Complex): Complex = exp(a(z))
}

case class Im(a: Expression) extends Prefix("Im", a) {
  override def apply(z: Complex): Complex = complex.Im(a(z))
}

case class Ln(a: Expression) extends Prefix("ln", a) {
  override def apply(z: Complex): Complex = ln(a(z))
}

case class Opp(a: Expression) extends Prefix("opp", a) {
  override def apply(z: Complex): Complex = opp(a(z))
}

case class Neg(a: Expression) extends Prefix("-", a) {
  override def apply(z: Complex): Complex = -a(z)
}

case class Re(a: Expression) extends Prefix("Re", a) {
  override def apply(z: Complex): Complex = complex.Re(a(z))
}

case class Sin(a: Expression) extends Prefix("sin", a) {
  override def apply(z: Complex): Complex = sin(a(z))
}

case class Sinh(a: Expression) extends Prefix("sinh", a) {
  override def apply(z: Complex): Complex = sinh(a(z))
}

case class Tan(a: Expression) extends Prefix("tan", a) {
  override def apply(z: Complex): Complex = tan(a(z))
}

case class Tanh(a: Expression) extends Prefix("tanh", a) {
  override def apply(z: Complex): Complex = tanh(a(z))
}

//-------------------------------------------------

case class Fac(a: Expression) extends Unary(a) {

  override def apply(z: Complex): Complex = fac(a(z))

  /** for the suffix "!" */
  private var tl: TextLayout = null

  /** Returns dimension for a good rendering of this expression */
  def preferredSize(g: Graphics2D): Dimension2D = {
    val dim = a.preferredSize(g)
    tl = textlayout(g, "!")
    //  dim | tl.getBounds      // XXX Dimension2D beside Rectangle2D : Dimension2D
    ???
  }

  /** Lay out the expression in this rectangle,
   * which usually will be the preferred size of the expression,
   * unless the expression is too big for the Editor.
   *
   * This methods sizes and positions all the TextLayout objects
   * in the expression. After that, the expression is ready for painting.
   * */
  override def layout(g: Graphics2D, bounds: Rectangle2D): Unit = {
    // a box containing layout of a beside tl
    ???
  }

  /** Paints this expression.
   * Its text layouts have already been created, scaled, and positioned. */
  override def paint(g: Graphics2D): Unit = {
    //paint a
    //box around a
    //paint tl
    ???
  }

}

//-------------------------------------------------

case class Mod(a: Expression) extends Unary(a) {

  override def apply(z: Complex): Complex = abs(a(z))

  /** Returns dimension for a good rendering of this expression */
  def preferredSize(g: Graphics2D): Dimension2D = ???

  /** Lay out the expression in this rectangle,
   * which usually will be the preferred size of the expression,
   * unless the expression is too big for the Editor.
   *
   * This methods sizes and positions all the TextLayout objects
   * in the expression. After that, the expression is ready for painting.
   * */
  override def layout(g: Graphics2D, bounds: Rectangle2D): Unit = {}

  /** Paints this expression.
   * Its text layouts have already been created, scaled, and positioned. */
  override def paint(g: Graphics2D): Unit = {}
}
