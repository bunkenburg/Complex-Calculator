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

import java.awt.Color
import java.awt.font.TextLayout
import java.awt.geom.{Dimension2D, Rectangle2D}

import cat.inspiracio.complex._
import java.text.ParseException

import cat.inspiracio.combinator.Parser

import scala.swing.Graphics2D

object Expression {

  /** The main client method: parses a tree from a String. */
  @throws[ParseException]
  def parse(s: String): Expression = {
    val p = new Parser
    p(s)
  }

}

abstract class Expression() {

  //override def toString: String

  /** Evaluate the expression to a complex number,
   * substituting the given number for variable z. */
  def apply(z: Complex): Complex

  //def apply(p: Pictlet) : Pictlet

  def paint(g: Graphics2D)

  /** Constructing and drawing a TextLayout and its bounding rectangle.
    * @param x
    * @param y (x,y) is topleft of where it should go */
  protected def draw(g: Graphics2D, x: Int, y: Int, s: String) = {
    cross(g, x, y)

    val font = g.getFont
    val frc = g.getFontRenderContext
    val layout = new TextLayout(s, font, frc)
    val ascent = layout.getAscent
    layout.draw(g, x, y + ascent )  // (x,y) here is origin of text layout, on baseline

    val bounds = layout.getBounds()  // java.awt.geom.Rectangle2D$Float[x=0.8125,y=-8.90625,w=6.0,h=9.078125]
    bounds.setRect(
      bounds.getX() + x,
      bounds.getY() + y + ascent,
      bounds.getWidth(),
      bounds.getHeight())
    g.draw(bounds);
  }

  private def cross(g: Graphics2D, x: Int, y: Int) = {
    g.setColor(Color.red)
    val c = 2
    g.drawLine(x-c, y-c, x+c, y+c)
    g.drawLine(x-c, y+c, x+c, y-c)
    g.setColor(Color.black)
  }

  /** Gives a box for a string. */
  protected def preferredSize(g: Graphics2D, s: String): Dimension2D = {
    val font = g.getFont
    val frc = g.getFontRenderContext
    val layout = new TextLayout(s, font, frc)
    layout.getBounds() // java.awt.geom.Rectangle2D$Float[x=0.8125,y=-8.90625,w=6.0,h=9.078125]
  }

  /** Returns dimension for a good rendering of this expression */
  def preferredSize(g: Graphics2D): Dimension2D

  /** Lay out the expression in this rectangle,
    * which usually will be the preferred size of the expression,
    * unless the expression is too big for the Editor.
    *
    * This methods sizes and positions all the TextLayout objects
    * in the expression. After that, the expression is ready for painting-
    * */
  def layout(g: Graphics2D, bounds: Rectangle2D): Unit = {}

  /** Makes a TextLayout for a String.
   * It will still have to be positioned at the right place,
   * and maybe scaled. */
  protected def textlayout(g: Graphics2D, s: String) = new TextLayout(s, g.getFont, g.getFontRenderContext)

  // classes =========================================================================================

  // java.awt.Rectangle extends Rectangle2D(public x, y, width,height : int)
  // java.awt.geom.Rectangle2D .Float .Double
  // java.awt.geom.RectangularShape
  // java.awt.Dimension extends Dimension2D(public width height : int)
  // java.awt.geom.Dimension2D abstract double

  // Dimension2D is the one I want.

  /** Makes a Rectangle2D (with position) appear as a Dimension2D (without position). */
  implicit class Rectangle2DDimension2D(r: Rectangle2D) extends Dimension2D{
    override def getWidth: Double = r.getWidth
    override def getHeight: Double = r.getHeight
    override def setSize(v: Double, v1: Double): Unit = ???
  }

  protected class Rectangle extends Dimension2D {
    // implicit constructor from Rectangle2D
    // implicit constructor from Dimension2D

    // extends Dimension2D
    override def getWidth: Double = ???
    override def getHeight: Double = ???
    override def setSize(v: Double, v1: Double): Unit = ???

    def | (b: Rectangle): Rectangle = ???
    def / (b: Rectangle): Rectangle = ???
  }

}