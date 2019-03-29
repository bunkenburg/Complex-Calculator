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

import java.awt._

import cat.inspiracio.calculator.Direction.Direction

/** Make it easy to draw lines, circles, crosses, ...
  * Keeps a pen position.
  *
  * These are like extra methods on Graphics. */
object MoreGraphics {

  implicit class GraphicsExtended private[calculator](g: Graphics) {

    /** cross at (x,y) */
    private[calculator] def drawCross(x: Int, y: Int, width: Int) = {
      g.drawLine(x - width, y - width, x + width, y + width)
      g.drawLine(x + width, y - width, x - width, y + width)
    }

    /** cross at (x,y) */
    private[calculator] def drawCross(p: Point, width: Int): Unit = {
      drawCross(p.x, p.y, width)
    }

    /** circle at (x,y) radius */
    private[calculator] def drawCircle(x: Int, y: Int, radius: Double) = {
      val topleftx = (x.toDouble - radius).toInt
      val toplefty = (y.toDouble - radius).toInt
      val width = (2 * radius).toInt
      val height = (2 * radius).toInt
      g.drawOval(topleftx, toplefty, width, height)
    }

    /** circle at centre with radius */
    private[calculator] def drawCircle(centre: Point, radius: Double): Unit = drawCircle(centre.x, centre.y, radius)

    /** line from a to b in colour */
    private[calculator] def drawLine(a: Point, b: Point, c: Color) = {
      val old = g.getColor
      g.setColor(c)
      g.drawLine(a.x, a.y, b.x, b.y)
      g.setColor(old)
    }

    /** line from a to b in colour */
    private[calculator] def drawLine(a: Point, b: Point): Unit = drawLine(a, b, Color.BLACK)

    /** draw string at position */
    private[calculator] def drawString(s: String, p: Point): Unit = g.drawString(s, p.x, p.y)

    private[calculator] def fillPolygon(polygon: Polygon, c: Color) = {
      val old = g.getColor
      g.setColor(c)
      g.fillPolygon(polygon)
      g.setColor(old)
    }

    /** Makes a triangle.
      * (Maybe there's better place for this?)
      * @param p         Triangle tip is here.
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

}