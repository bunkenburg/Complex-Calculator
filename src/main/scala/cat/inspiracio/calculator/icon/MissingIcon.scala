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
package cat.inspiracio.calculator.icon

import javax.swing.Icon
import java.awt._

class MissingIcon extends Icon {

  def width = 16
  def height = 16

  override def paintIcon(c: Component, g: Graphics, x: Int, y: Int): Unit = {
    val g2d = g.create.asInstanceOf[Graphics2D]

    //white background
    g2d.setColor(Color.WHITE)
    g2d.fillRect(x + 1, y + 1, width - 2, height - 2)

    //black border
    g2d.setColor(Color.BLACK)
    g2d.drawRect(x + 1, y + 1, width - 2, height - 2)

    //red cross
    val stroke = new BasicStroke(4) //width
    g2d.setColor(Color.RED)
    g2d.setStroke(stroke)
    g2d.drawLine(x + 5, y + 5, x + width - 5, y + height - 5)
    g2d.drawLine(x + 5, y + height - 5, x + width - 5, y + 5)

    g2d.dispose()
  }

  override def getIconWidth: Int = width
  override def getIconHeight: Int = height
}
