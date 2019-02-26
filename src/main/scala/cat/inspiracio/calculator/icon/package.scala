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
 * *//*	Copyright 2011 Alexander Bunkenburg alex@inspiracio.cat
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

import java.awt._

import javax.swing.{Icon, ImageIcon, JButton, JToggleButton}

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
      new ImageIcon(imgURL)
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

  def toggle(icon: Icon, tooltip: String): JToggleButton = {
    val b = new JToggleButton(icon)
    b.setToolTipText(tooltip)
    b.setOpaque(false)
    b.setBorderPainted(false)
    //b.setContentAreaFilled(false) //need for signalling selected
    b
  }

  def button(icon: Icon, tooltip: String): JButton = {
    val b = new JButton(icon)
    b.setToolTipText(tooltip)
    b.setOpaque(false)
    b.setBorderPainted(false)
    //b.setContentAreaFilled(false) //need for signalling selected
    b
  }

}

