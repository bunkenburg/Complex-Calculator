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

import cat.inspiracio.calculator.{Calculator, MyKeyListener}
import cat.inspiracio.parsing.Expression

import scala.swing._

/** The text area of the complex calculator. */
class Editor (calculator: Calculator) extends Component {

  background = Color.LIGHT_GRAY
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

  private var string = ""

  def show(e: Expression) = {
    string = e.toString
    repaint()
  }

  override def paintComponent(g: Graphics2D) = {
    super.paintComponent(g)
    g.drawString(string, 10, size.height-10)
  }
}