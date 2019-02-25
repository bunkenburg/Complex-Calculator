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

import javax.swing.{Icon, ImageIcon}

package object icon {

  private def icon(path: String) = {
    val imgURL = this.getClass().getResource(path)
    if (imgURL != null)
      new ImageIcon(imgURL)
    else {
      System.err.println("Couldn't find file: " + path)
      null
    }
  }

  val circleIcon = icon("circle.png")
  val clearIcon = icon("clear.png")
  val drawIcon = icon("draw.jpeg")
  val gridIcon = icon("grid.png")
  val handIcon = icon("hand.png")
  val lineIcon = icon("line.png")
  val planeIcon = icon("plane.png")
  val rectangleIcon = icon("rectangle.png")
  val resetIcon = icon("reset.png")
  val sphereIcon = icon("sphere.jpeg")
  val squareIcon = icon("square.png")
  val zoomInIcon = icon("zoom-in.png")
  val zoomOutIcon = icon("zoom-out.png")

}
