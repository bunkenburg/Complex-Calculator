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

import java.awt.{Dimension, Font, Graphics, Point}

import cat.inspiracio.complex._
import cat.inspiracio.geometry.{Matrix44, Point2, Vector3}

// Referenced classes of package bunkenba.calculator:
//            WorldRepresentation, DoubleBuffer, Drawing, Matrix44,
//            Vector3, World

final class Sphere private[calculator](val world: World) extends WorldRepresentation(world) {
  import MoreGraphics.GraphicsExtended

  //State -------------------------------------------------------

  private var markLength = 2

  private var R = Matrix44.unit
  private var R1 = Matrix44.unit

  private var xyscale: Double = 0

  //Methods ------------------------------------------------------

  override private[calculator] def draw(g: Graphics, c: Complex) = {
    val (front, point) = isFrontC(c)
    if ( front ) {
      g.drawCross( point, markLength )
      g.drawString(c.toString, point.x+2, point.y+2 )
    }
  }

  override private[calculator] def draw(g: Graphics, zs: List[Complex]) = {
    if ( zs != null ) {
      var previous: Point = null
      var visible = false

      zs.foreach{ z =>
        val (front, point) = isFrontC(z)
        if (front) {
          if (visible)
            g.drawLine(previous, point);
          previous = Point2(point)
          visible = true
        }
        else
          visible = false
      }

    }
  }

  /** Maps 3d space to Complex */
  private def f3dC(v: Vector3): Complex =
    if (v.y == 0.5 ) ∞
    else if (v.y == -0.5 ) 0
    else Cartesian(v.x / (0.5 - v.y), v.z / (0.5 - v.y))

  /** Maps complex to 3d space and returns whether it is visible on the front. */
  private def isFrontC(c: Complex): (Boolean, Point) = {
    val (x, y, z) = fC3d(c)

    val d4 = R(2,0) * x + R(2,1) * y + R(2,2) * z + R(2,3)
    if (d4 <= 0.0D) {
      val d6 = R(0,0) * x + R(0,1) * y + R(0,2) * z + R(0,3)
      val d7 = R(1,0) * x + R(1,1) * y + R(1,2) * z + R(1,3)
      val size: Dimension = getSize()
      val width = size.width
      val height = size.height
      val point = Point2(
        (d6 * xyscale + width.toDouble * 0.5).toInt,
        (-d7 * xyscale + height.toDouble * 0.5).toInt
      )
      (true, point)
    }
    else (false, null)
  }

  /** Maps Complex to 3d space */
  private def fC3d(c: Complex): (Double, Double, Double) =
    if (finite(c)) {
      val a = sqr(abs(c))
      val d5 = 1.0 + a
      ( Re(c) / d5, a / d5 - 0.5, Im(c) / d5 )
    }
    else (0.0, 0.5, 0.0)

  /** Called by swing */
  override def paint(g: Graphics): Unit = {
    val size: Dimension = getSize
    xyscale = Math.min(size.width, size.height) * 0.8   //0.80000000000000004D
    g.drawCircle(size.width / 2, size.height / 2, 0.5 * xyscale)
    List[Complex](0, ∞, 1, -1, i, -i ).foreach{ draw(g, _) }
    w.draw(g)
  }

  /** Can return null. */
  override private[calculator] def point2Complex(p: Point): Complex = {
    val size: Dimension = getSize
    val width = size.width
    val height = size.height
    val x = (p.x - width * 0.5) / xyscale
    val y = (height * 0.5 - p.y) / xyscale
    val z = 0.25 - x * x - y * y
    if ( 0 <= z ) {
      val vector3 = R1 * (x, y, -sqrt(z) )
      f3dC(vector3)
    }
    else
      null
  }

  override private[calculator] def reset() = {
    R = Matrix44.unit
    R1 = Matrix44.unit
    repaint()
  }

  override def setFont(font: Font): Unit = {
    super.setFont(font)
    val i = getFontMetrics(font).getAscent
    markLength = i / 5
  }

  override private[calculator] def shift(p: Point) = {
    val size: Dimension = getSize()
    val width = size.width
    val height = size.height
    val x = p.y * 2 * π / width
    val y = p.x * 2 * π / height
    R = R.preRot('x', -x)
    R = R.preRot('y', -y)
    R1 = R1.postRot('x', x)
    R1 = R1.postRot('y', y)
    repaint()
  }

}