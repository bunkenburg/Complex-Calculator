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

import cat.inspiracio.complex._
import cat.inspiracio.numbers.ECList

// Referenced classes of package bunkenba.calculator:
//            WorldRepresentation, DoubleBuffer, Drawing, Matrix44,
//            Vector3, World

final class Sphere private[calculator](val world: World) extends WorldRepresentation(world) {

  //State -------------------------------------------------------

  private var MARKLENGTH = 2

  private var R: Matrix44 = new Matrix44
  private var R1: Matrix44 = new Matrix44

  reset()

  private var xyscale: Double = 0
  final private val marks = Array[Complex](0, ∞, 1, -1, i, -i )

  //Methods ------------------------------------------------------

  override private[calculator] def drawComplex(drawing: Drawing, z: Complex) = {
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
    val d4 = R.data(2)(0) * d + R.data(2)(1) * d1 + R.data(2)(2) * d2 + R.data(2)(3)
    if (d4 <= 0.0D) {
      val d6 = R.data(0)(0) * d + R.data(0)(1) * d1 + R.data(0)(2) * d2 + R.data(0)(3)
      val d7 = R.data(1)(0) * d + R.data(1)(1) * d1 + R.data(1)(2) * d2 + R.data(1)(3)

      val x = (d6 * xyscale + getSize.width.toDouble * 0.5).toInt
      val y = (-d7 * xyscale + getSize.height.toDouble * 0.5).toInt
      drawing.cross( x, y, MARKLENGTH )
      drawing.move(2, -2)
      drawing.drawString(z.toString)
    }
  }

  override private[calculator] def drawECList(drawing: Drawing, eclist: ECList) = {
    var list = eclist
    val point = new Point
    if ( list != null ) {
      var flag = isFrontC( list.head, point)
      drawing.moveTo(point.x, point.y)
      while ( list != null ) {
        if (isFrontC( list.head, point )) {
          if (flag)
            drawing.lineTo(point.x, point.y)
          else
            drawing.moveTo(point.x, point.y)
          flag = true
        }
        else
          flag = false
        list = list.tail
      }
    }
  }

  private def f3dC(v: Vector3): Complex = {
    if (v.y == 0.5 )
      return ∞
    if (v.y == -0.5 )
      0
    else
      Cartesian(v.x / (0.5 - v.y), v.z / (0.5 - v.y))
  }

  private def isFrontC(z: Complex, point: Point) = {
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
    val d4 = R.data(2)(0) * d + R.data(2)(1) * d1 + R.data(2)(2) * d2 + R.data(2)(3)
    if (d4 <= 0.0D) {
      val d6 = R.data(0)(0) * d + R.data(0)(1) * d1 + R.data(0)(2) * d2 + R.data(0)(3)
      val d7 = R.data(1)(0) * d + R.data(1)(1) * d1 + R.data(1)(2) * d2 + R.data(1)(3)
      point.x = (d6 * xyscale + getSize.width.toDouble * 0.5D).toInt
      point.y = (-d7 * xyscale + getSize.height.toDouble * 0.5D).toInt
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
    val d4 = R.data(2)(0) * d + R.data(2)(1) * d1 + R.data(2)(2) * d2 + R.data(2)(3)
    if (d4 <= 0.0D) {
      val d6 = R.data(0)(0) * d + R.data(0)(1) * d1 + R.data(0)(2) * d2 + R.data(0)(3)
      val d7 = R.data(1)(0) * d + R.data(1)(1) * d1 + R.data(1)(2) * d2 + R.data(1)(3)
      drawing.lineTo((d6 * xyscale + getSize.width.toDouble * 0.5D).toInt, (-d7 * xyscale + getSize.height.toDouble * 0.5D).toInt)
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
    val d4 = R.data(2)(0) * d + R.data(2)(1) * d1 + R.data(2)(2) * d2 + R.data(2)(3)
    if (d4 <= 0.0D) {
      val d6 = R.data(0)(0) * d + R.data(0)(1) * d1 + R.data(0)(2) * d2 + R.data(0)(3)
      val d7 = R.data(1)(0) * d + R.data(1)(1) * d1 + R.data(1)(2) * d2 + R.data(1)(3)
      drawing.moveTo((d6 * xyscale + getSize.width.toDouble * 0.5D).toInt, (-d7 * xyscale + getSize.height.toDouble * 0.5D).toInt)
    }
  }

  override def paint(g: Graphics): Unit = {
    val off = doubleBuffer.offScreen(g)
    xyscale = Math.min(getSize.width, getSize.height).toDouble * 0.80000000000000004D
    val drawing = new Drawing(off)
    drawing.drawCircle(getSize.width / 2, getSize.height / 2, 0.5 * xyscale)

    for( m <- marks )
      drawComplex(drawing, m)

    w.drawStuff(drawing)
    doubleBuffer.onScreen
  }

  override private[calculator] def Point2Complex(point: Point) = {
    val d = (point.x.toDouble - getSize.width.toDouble * 0.5) / xyscale
    val d1 = (getSize.height.toDouble * 0.5 - point.y.toDouble) / xyscale
    val d2 = 0.25D - d * d - d1 * d1
    if (d2 >= 0.0D) {
      val vector3 = R1.multiply(d, d1, -Math.sqrt(d2))
      f3dC(vector3)
    }
    else null
  }

  override private[calculator] def reset() = {
    R.unit()
    R1.unit()
  }

  override def setFont(font: Font): Unit = {
    super.setFont(font)
    val i = getFontMetrics(font).getAscent
    MARKLENGTH = i / 5
  }

  override private[calculator] def shift(i: Int, j: Int) = {
    val d = j.toDouble * (6.2831853071795862D / getSize.width.toDouble)
    val d1 = i.toDouble * (6.2831853071795862D / getSize.height.toDouble)
    R.preRot('x', -d)
    R.preRot('y', -d1)
    R1.postRot('x', d)
    R1.postRot('y', d1)
  }

  override private[calculator] def zoomIn() = {}
  override private[calculator] def zoomOut() = {}
}