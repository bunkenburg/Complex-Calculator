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
package cat.inspiracio.calculator

import javax.swing.Icon
import scala.swing._

/**
  * https://docs.oracle.com/javase/tutorial/uiswing/components/icon.html
  * Icon
  *   ImageIcon (gif, jpeg, png)
  *
  * */
package object icon {

  private def icon(path: String) = {
    val p = "/cat/inspiracio/calculator/icon/" + path
    val c = getClass()
    val imgURL = c.getResource(p)
    if (imgURL != null)
      Swing.Icon(imgURL)
    else {
      System.err.println("Couldn't find file: " + path)
      new MissingIcon()
    }
  }

  val circleIcon = icon("circle.png")
  val clearIcon = icon("clear.png")
  val drawIcon = icon("draw.png")
  val gridIcon = icon("grid.png")
  val handIcon = icon("hand.png")
  val lineIcon = icon("line.png")
  val planeIcon = icon("plane.png")
  val rectangleIcon = icon("rectangle.png")
  val resetIcon = icon("reset.png")
  val sphereIcon = icon("sphere.png")
  val squareIcon = icon("square.png")
  val zoomInIcon = icon("zoom-in.png")
  val zoomOutIcon = icon("zoom-out.png")

  def toggle(icon: Icon, tooltip: String): ToggleButton = {
    val b = new ToggleButton
    b.icon = icon
    b.tooltip = tooltip
    b.opaque = false
    b.borderPainted = false
    b.contentAreaFilled = true //need for signalling selected
    b
  }

  def toggle(icon: Icon, tooltip: String, op: => Unit): ToggleButton = {
    val b = new ToggleButton
    b.action = Action(null)(op)
    b.icon = icon
    b.tooltip = tooltip
    b.opaque = false
    b.borderPainted = false
    b.contentAreaFilled = true //need for signalling selected
    b
  }

  def button(icon: Icon, tooltip: String): Button = {
    new Button {
      icon = icon
      tooltip = tooltip
      opaque = false
      borderPainted = false
      contentAreaFilled = false //need for signalling selected
    }
  }

  def button(icon: Icon, tooltip: String, op: => Unit): Button = {
    val b = new Button {
      icon = icon
      tooltip = tooltip
      opaque = false
      borderPainted = false
      contentAreaFilled = false //need for signalling selected
    }
    b.action = Action(null)(op)
    b.icon = icon
    b.tooltip = tooltip
    b.opaque = false
    b.borderPainted = false
    b.contentAreaFilled = false
    b
  }

}