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
import java.awt.font.TextLayout

import cat.inspiracio.complex._

import scala.swing.Graphics2D

/** An expression that is a Complex number */
case class C(constant: Complex) extends Expression {

  override def toString: String = constant.toString
  override def apply(z: Complex): Complex = constant

  /** Paints the expression in this rectangle, which is of preferred size for the expression. */
  override def paint(g: Graphics2D, rect: swing.Rectangle) = {
    val s = constant.toString
    draw(g, rect.x, rect.y, s)
  }

  /** Returns dimension for a good rendering of this expression */
  override def preferredSize(g: Graphics2D): Dimension = {
    val s = constant.toString
    preferredSize(g, s)
  }

}