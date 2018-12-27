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

/** Make it easy to draw lines, circles, crosses, ...
  * Keeps a pen position.
  *
  * These are like extra methods on Graphics. */
final class Drawing private[calculator](var g: Graphics) {

  pen = new Point
  private var pen: Point = new Point

  private[calculator] def graphics = g

  /** cross at (x,y) */
  private[calculator] def cross(x: Int, y: Int, width: Int) = {
    //XXX Don't use pen!
    pen.x = x
    pen.y = y
    g.drawLine(pen.x - width, pen.y - width, pen.x + width, pen.y + width)
    g.drawLine(pen.x + width, pen.y - width, pen.x - width, pen.y + width)
  }

  /** circle at (x,y) radius */
  private[calculator] def drawCircle(x: Int, y: Int, radius: Double) = {
    val topleftx = (x.toDouble - radius).toInt
    val toplefty = (y.toDouble - radius).toInt
    val width = (2D * radius).toInt
    val height = (2D * radius).toInt
    g.drawOval(topleftx, toplefty, width, height)
  }

  /** line from a to b in colour */
  private[calculator] def drawLine(a: Point, b: Point, c: Color) = {
    //XXX Don't use pen!
    val color1 = g.getColor
    g.setColor(c)
    g.drawLine(a.x, a.y, b.x, b.y)
    g.setColor(color1)
    pen.x = b.x
    pen.y = b.y
  }

  /** draw string at pen position */
  //XXX Don't use pen!
  private[calculator] def drawString(s: String) = g.drawString(s, pen.x, pen.y)

  private[calculator] def fillPolygon(polygon: Polygon, c: Color) = {
    val color1 = g.getColor
    g.setColor(c)
    g.fillPolygon(polygon)
    g.setColor(color1)
  }

  /** line (x,y) at pen */
  private[calculator] def line(x: Int, y: Int) = {
    g.drawLine(pen.x, pen.y, pen.x + x, pen.y + y)
    pen.translate(x, y)
  }

  /** line to (x,y) */
  private[calculator] def lineTo(x: Int, y: Int) = {
    g.drawLine(pen.x, pen.y, x, y)
    pen.x = x
    pen.y = y
  }

  /** move pen by (x,y) */
  private[calculator] def move(x: Int, y: Int) = pen.translate(x, y)

  /** move pen to (x,y) */
  private[calculator] def moveTo(x: Int, y: Int) = {
    pen.x = x
    pen.y = y
  }

  /** Makes a triangle.
    *
    * @param point     Triangle tip is here.
    * @param direction Points this direction.
    * @param size      Size of the triangle. */
  private[calculator] def mkTriangle(point: Point, direction: Direction, size: Int): Polygon = {
    direction match {
      case Direction.NORTH =>
        return new Polygon(Array[Int](point.x - size, point.x, point.x + size), Array[Int](point.y + size, point.y, point.y + size), 3)
      case Direction.EAST =>
        return new Polygon(Array[Int](point.x - size, point.x, point.x - size), Array[Int](point.y - size, point.y, point.y + size), 3)
      case Direction.SOUTH =>
        return new Polygon(Array[Int](point.x - size, point.x, point.x + size), Array[Int](point.y - size, point.y, point.y - size), 3)
      case Direction.WEST =>
        return new Polygon(Array[Int](point.x + size, point.x, point.x + size), Array[Int](point.y - size, point.y, point.y + size), 3)
    }
    null
  }

}