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

  //XXX try to get rid of this; it's state
  private val pen: Point = new Point

  //XXX try to hang all these methods on Graphics
  private[calculator] def graphics = g

  /** cross at (x,y) */
  private[calculator] def cross(x: Int, y: Int, width: Int) = {
    g.drawLine(x - width, y - width, x + width, y + width)
    g.drawLine(x + width, y - width, x - width, y + width)
  }

  /** circle at (x,y) radius */
  private[calculator] def drawCircle(x: Int, y: Int, radius: Double) = {
    val topleftx = (x.toDouble - radius).toInt
    val toplefty = (y.toDouble - radius).toInt
    val width = (2 * radius).toInt
    val height = (2 * radius).toInt
    g.drawOval(topleftx, toplefty, width, height)
  }

  /** line from a to b in colour */
  private[calculator] def drawLine(a: Point, b: Point, c: Color) = {
    val color1 = g.getColor
    g.setColor(c)
    g.drawLine(a.x, a.y, b.x, b.y)
    g.setColor(color1)
  }

  /** draw string at pen position */
  private[calculator] def drawString(s: String): Unit = {
    //XXX Don't use pen!
    g.drawString(s, pen.x, pen.y)
  }

  /** draw string at position */
  private[calculator] def drawString(s: String, x: Int, y: Int): Unit =
    g.drawString(s, x, y)

  /** draw string at position */
  private[calculator] def drawString(s: String, p: Point): Unit =
    g.drawString(s, p.x, p.y)

  private[calculator] def fillPolygon(polygon: Polygon, c: Color) = {
    val color1 = g.getColor
    g.setColor(c)
    g.fillPolygon(polygon)
    g.setColor(color1)
  }

  /** line (x,y) at pen */
  private[calculator] def line(x: Int, y: Int) = {
    //XXX uses pen
    g.drawLine(pen.x, pen.y, pen.x + x, pen.y + y)
    pen.translate(x, y)
  }

  /** line from pen to (x,y) */
  private[calculator] def lineTo(x: Int, y: Int) = {
    //XXX uses pen, sets pen
    g.drawLine(pen.x, pen.y, x, y)
    pen.x = x
    pen.y = y
  }

  /** line to (x,y) */
  private[calculator] def lineTo(p: Point): Unit = lineTo(p.x, p.y)

  /** move pen by (x,y) */
  private[calculator] def move(x: Int, y: Int) = pen.translate(x, y)

  /** move pen to (x,y) */
  private[calculator] def moveTo(x: Int, y: Int) = {
    //XXX sets pen
    pen.x = x
    pen.y = y
  }

  /** move pen to (x,y) */
  private[calculator] def moveTo(p : Point) = {
    //XXX sets pen
    pen.x = p.x
    pen.y = p.y
  }

  /** Makes a triangle.
    *
    * @param p     Triangle tip is here.
    * @param direction Points this direction.
    * @param size      Size of the triangle. */
  private[calculator] def mkTriangle(p: Point, direction: Direction, size: Int): Polygon = {
    import Direction._

    direction match {
      case NORTH =>
        new Polygon(Array(p.x - size, p.x, p.x + size), Array(p.y + size, p.y, p.y + size), 3)
      case EAST =>
        new Polygon(Array(p.x - size, p.x, p.x - size), Array(p.y - size, p.y, p.y + size), 3)
      case SOUTH =>
        new Polygon(Array(p.x - size, p.x, p.x + size), Array(p.y - size, p.y, p.y - size), 3)
      case WEST =>
        new Polygon(Array(p.x + size, p.x, p.x + size), Array(p.y - size, p.y, p.y + size), 3)
    }
  }

}