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
import java.awt.geom.{Dimension2D, Point2D, Rectangle2D}

import cat.inspiracio.complex._

import scala.swing.Graphics2D

/** An expression that is a Complex number */
case class C(constant: Complex) extends Expression {

  private var tl: TextLayout = null
  private var top: Double = 0
  private var left: Double = 0

  override def toString: String = constant.toString
  override def apply(z: Complex): Complex = constant

  /** Returns dimension for a good rendering of this expression */
  override def preferredSize(g: Graphics2D): Dimension2D = {
    tl = textlayout(g, constant.toString)
    tl.getBounds()
  }

  /** Lay out the expression in this rectangle,
    * which usually will be the preferred size of the expression,
    * unless the expression is too big for the Editor.
    *
    * This methods sizes and positions all the TextLayout objects
    * in the expression. After that, the expression is ready for painting.
    * */
  override def layout(g: Graphics2D, bounds: Rectangle2D) = {
    // size tl correctly
    val tlb = tl.getBounds
    if(tlb.getWidth <= bounds.getWidth && tlb.getHeight <= bounds.getHeight){
      //do nothing
    }
    else{
      // resize tl
      println("C.layout must resize tl")
    }

    // position tl correctly
    top = bounds.getY
    left = bounds.getX
  }

  /** Paints this expression.
   * Its text layouts have already been created, scaled, and positioned. */
  override def paint(g: Graphics2D) = {
    val ascent = tl.getAscent
    tl.draw(g, left.toFloat, top.toFloat + ascent )  // (x,y) here is origin of text layout, on baseline
  }

}