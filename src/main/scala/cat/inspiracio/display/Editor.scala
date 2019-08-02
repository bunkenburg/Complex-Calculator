/*	Copyright 2019 Alexander Bunkenburg alex@inspiracio.cat
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
package cat.inspiracio.display

import java.awt.Color
import java.awt.font.FontRenderContext
import java.awt.geom.{Dimension2D, Rectangle2D}

import cat.inspiracio.calculator.{Calculator, MyKeyListener}
import cat.inspiracio.parsing.Expression

import scala.swing._

/** The text area of the complex calculator. */
class Editor (calculator: Calculator) extends Component {
  import Color._

  background = LIGHT_GRAY

  // Any size is good. This matches size of previously used TextArea.
  val width = 336
  val height = 46
  preferredSize = new Dimension(width,height)

  listenTo(keys)
  reactions += new MyKeyListener(calculator)

  def clear() = {} //text = ""
  def eraseOldResult() = {}

  /** deletes selected text or backspaces */
  def delete() = {}

  def paste(c: Char): Unit = paste(String.valueOf(c) )
  def paste(s: String): Unit = {}

  /** Put s in from of displayed string */
  def prepend(s: String) = {}

  /** Replace one char by another. */
  def replace(z: Char, x: Char) = {}

  // menu actions
  def cut() = ???
  def copy() = ???
  def selectAll() = ???
  def paste() = ???

  private var e: Expression = null
  private var elayout: Expression = null

  /** Creates all the TextLayouts for this expression,
    * containing the right String,
    * sized right,
    * and positioned right.
    * Size adapted to available space in Editor.
    * Positioned in the middle of the Editor. */
  def show(ex: Expression) = {
    e = ex
    elayout = null
    repaint()
  }

  /** Paints the Expression e which has already been decorated with
    * TextLayouts that are sized and positioned. */
  override def paintComponent(g: Graphics2D) = {
    super.paintComponent(g)

    if(e!=null){

      if(elayout==null)
        elayout = layout(g, e)

      elayout.paint(g)
    }
  }

  private def layout(g: Graphics2D, e: Expression) = if(e==null) null else {
    val preferredSize: Dimension2D = e.preferredSize(g)

    if (
      preferredSize.getWidth <= bounds.width &&
      preferredSize.getHeight <= bounds.height
    ) {
      val x = bounds.x + (bounds.width - preferredSize.getWidth)/2
      val y = bounds.y + (bounds.height - preferredSize.getHeight)/2
      val rect = new Rectangle2D.Double(x, y, preferredSize.getWidth, preferredSize.getHeight)
      e.layout(g, rect)
    } else {
      println(s"expression $e is too big for $bounds")
    }

    e
  }

}