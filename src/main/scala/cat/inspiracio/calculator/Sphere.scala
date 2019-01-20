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
import cat.inspiracio.geometry.{Matrix44, Vector3}

// Referenced classes of package bunkenba.calculator:
//            WorldRepresentation, DoubleBuffer, Drawing, Matrix44,
//            Vector3, World

final class Sphere private[calculator](val world: World) extends WorldRepresentation(world) {

  //State -------------------------------------------------------

  private var MARKLENGTH = 2

  private var R = Matrix44.unit
  private var R1 = Matrix44.unit

  private var xyscale: Double = 0
  final private val marks = Array[Complex](0, ∞, 1, -1, i, -i )

  //Methods ------------------------------------------------------

  override private[calculator] def draw(drawing: Drawing, z: Complex) = {
    var d = .0
    var d1 = .0
    var d2 = .0

    if (finite(z)) {
      val a = abs(z)
      val d3 = sqr(a)
      val d5 = 1.0D + d3
      d = Re(z) / d5
      d1 = d3 / d5 - 0.5D
      d2 = Im(z) / d5
    }
    else {
      d = 0.0D
      d1 = 0.5D
      d2 = 0.0D
    }

    val d4 = R(2,0) * d + R(2,1) * d1 + R(2,2) * d2 + R(2,3)
    if (d4 <= 0.0D) {
      val d6 = R(0,0) * d + R(0,1) * d1 + R(0,2) * d2 + R(0,3)
      val d7 = R(1,0) * d + R(1,1) * d1 + R(1,2) * d2 + R(1,3)

      val size: Dimension = getSize
      val width = size.width
      val height = size.height
      val x = (d6 * xyscale + width.toDouble * 0.5).toInt
      val y = (-d7 * xyscale + height.toDouble * 0.5).toInt
      drawing.cross( x, y, MARKLENGTH )
      drawing.move(2, -2)
      drawing.drawString(z.toString)
    }
  }

  override private[calculator] def draw(drawing: Drawing, list: List[Complex]) = {
    val point = new Point
    if ( list != null && !list.isEmpty ) {

      var flag = isFrontC( list.head, point)
      drawing.moveTo(point.x, point.y)

      list.foreach{ z =>
        if (isFrontC( z, point )) {
          if (flag)
            drawing.lineTo(point.x, point.y)
          else
            drawing.moveTo(point.x, point.y)
          flag = true
        }
        else
          flag = false
      }

    }
  }

  private def f3dC(v: Vector3): Complex = {
    if (v.y == 0.5 )
      ∞
    else if (v.y == -0.5 )
      0
    else
      Cartesian(v.x / (0.5 - v.y), v.z / (0.5 - v.y))
  }

  /** Also sets point (very bad style) */
  private def isFrontC(z: Complex, point: Point): Boolean = {
    var d = .0
    var d1 = .0
    var d2 = .0
    if (finite(z)) {
      val d3 = sqr(abs(z))
      val d5 = 1.0D + d3
      d = Re(z) / d5
      d1 = d3 / d5 - 0.5D
      d2 = Im(z) / d5
    }
    else {
      d = 0.0D
      d1 = 0.5D
      d2 = 0.0D
    }
    val d4 = R(2,0) * d + R(2,1) * d1 + R(2,2) * d2 + R(2,3)
    if (d4 <= 0.0D) {
      val d6 = R(0,0) * d + R(0,1) * d1 + R(0,2) * d2 + R(0,3)
      val d7 = R(1,0) * d + R(1,1) * d1 + R(1,2) * d2 + R(1,3)
      val size: Dimension = getSize()
      val width = size.width
      val height = size.height
      point.x = (d6 * xyscale + width.toDouble * 0.5).toInt
      point.y = (-d7 * xyscale + height.toDouble * 0.5).toInt
      true
    }
    else false
  }

  override private[calculator] def lineTo(drawing: Drawing, z: Complex) = {
    var d = .0
    var d1 = .0
    var d2 = .0
    if (finite(z)) {
      val d3 = sqr(abs(z))
      val d5 = 1.0D + d3
      d = Re(z) / d5
      d1 = d3 / d5 - 0.5D
      d2 = Im(z) / d5
    }
    else {
      d = 0.0
      d1 = 0.5
      d2 = 0.0
    }
    val d4 = R(2,0) * d + R(2,1) * d1 + R(2,2) * d2 + R(2,3)
    if (d4 <= 0.0D) {
      val d6 = R(0,0) * d + R(0,1) * d1 + R(0,2) * d2 + R(0,3)
      val d7 = R(1,0) * d + R(1,1) * d1 + R(1,2) * d2 + R(1,3)
      val size: Dimension = getSize
      val width = size.width
      val height = size.height
      drawing.lineTo(
        (d6 * xyscale + width.toDouble * 0.5).toInt,
        (-d7 * xyscale + height.toDouble * 0.5).toInt
      )
    }
  }

  override private[calculator] def moveTo(drawing: Drawing, z: Complex) = {
    var d = .0
    var d1 = .0
    var d2 = .0
    if (finite(z)) {
      val d3 = sqr(abs(z))
      val d5 = 1.0D + d3
      d = Re(z) / d5
      d1 = d3 / d5 - 0.5D
      d2 = Im(z) / d5
    }
    else {
      d = 0.0D
      d1 = 0.5D
      d2 = 0.0D
    }
    val d4 = R(2,0) * d + R(2,1) * d1 + R(2,2) * d2 + R(2,3)
    if (d4 <= 0.0D) {
      val d6 = R(0,0) * d + R(0,1) * d1 + R(0,2) * d2 + R(0,3)
      val d7 = R(1,0) * d + R(1,1) * d1 + R(1,2) * d2 + R(1,3)
      val size: Dimension = getSize
      val width = size.width
      val height = size.height
      drawing.moveTo(
        (d6 * xyscale + width.toDouble * 0.5).toInt,
        (-d7 * xyscale + height.toDouble * 0.5).toInt
      )
    }
  }

  /** Called by swing */
  override def paint(g: Graphics): Unit = {
    val size: Dimension = getSize
    xyscale = Math.min(size.width, size.height).toDouble * 0.80000000000000004D
    val drawing = new Drawing(g)
    drawing.drawCircle(size.width / 2, size.height / 2, 0.5 * xyscale)

    for( m <- marks )
      draw(drawing, m)

    w.drawStuff(drawing)
  }

  /** Can return null. */
  override private[calculator] def point2Complex(p: Point): Complex = {
    val size: Dimension = getSize
    val width = size.width
    val height = size.height
    val d = (p.x - width * 0.5) / xyscale
    val d1 = (height * 0.5 - p.y) / xyscale
    val d2 = 0.25D - d * d - d1 * d1
    if (d2 >= 0.0D) {
      val vector3 = R1 * (d, d1, -sqrt(d2) )
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
    MARKLENGTH = i / 5
  }

  override private[calculator] def shift(p: Point) = {
    val size: Dimension = getSize()
    val width = size.width
    val height = size.height
    val d = p.y * 2 * π / width
    val d1 = p.x * 2 * π / height
    R = R.preRot('x', -d)
    R = R.preRot('y', -d1)
    R1 = R1.postRot('x', d)
    R1 = R1.postRot('y', d1)
    repaint()
  }

}