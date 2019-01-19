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

import java.awt.{Color, Dimension, Font, Graphics, Point}

import cat.inspiracio.complex._
import cat.inspiracio.calculator.Direction._
import cat.inspiracio.geometry._

final class Plane private[calculator](val world: World) extends WorldRepresentation(world) {

  private var AXISSPACE = 30
  private var AXISMARKING = 40
  private var FONTHEIGHT = 10
  private var TRIANGLESIZE = 5
  private var MARKLENGTH = 2

  // state ----------------------------------------------------------------

  private var ScaleFactor = 40.0
  private var CenterReal = 0.0
  private var CenterImaginary = 0.0
  private var TopImaginary = 0.0
  private var LeftReal = 0.0
  private var BottomImaginary = 0.0
  private var RightReal = 0.0

  //XXX unit tests
  //XXX unify with repeated method
  private def raiseSmooth(d1: Double): Double = {
    var d = d1
    var i = 0

    while ( d < 1 ) {
      d = d * 10
      i = i-1
    }

    while ( 10 <= d ) {
      d = d / 10
      i = i+1
    }

    if ( 5 < d )
      d = 10
    else if ( 2.5 < d )
      d = 5
    else if ( 2 < d )
      d = 2.5
    else if ( 1.0 < d )
      d = 2

    while ( i < 0 ) {
      d = d / 10
      i = i+1
    }

    while ( 0 < i ) {
      d = d * 10
      i = i-1
    }

    d
  }

  //XXX return Point
  private def cartesian2Point(re: Double, im: Double, point: Point) = {
    point.x = ((re - LeftReal) * ScaleFactor).toInt
    point.y = -((im - TopImaginary) * ScaleFactor).toInt
  }

  override private[calculator] def draw(drawing: Drawing, z: Complex) =
    if (finite(z)) {
      val x = ((Re(z) - LeftReal) * ScaleFactor).asInstanceOf[Int]
      val y = -((Im(z) - TopImaginary) * ScaleFactor).asInstanceOf[Int]
      drawing.cross(x, y, MARKLENGTH)
      drawing.move(2, 2)
      drawing.drawString(z.toString)
  }

  override private[calculator] def draw(drawing: Drawing, eclist: List[Complex]) =
    if (eclist != null && !eclist.isEmpty) {
      var list = eclist

      moveTo(drawing, list.head)
      lineTo(drawing, list.head)
      list = list.tail

      list.foreach{ lineTo(drawing, _) }
    }

  //XXX remove isInstanceOf
  override private[calculator] def draw(drawing: Drawing, piclet: Piclet) =

    if (piclet.isInstanceOf[Line]) {
      moveTo(drawing, piclet.asInstanceOf[Line].a)
      lineTo(drawing, piclet.asInstanceOf[Line].b)
    }

    else if (piclet.isInstanceOf[Circle]) {
      val circle = piclet.asInstanceOf[Circle]
      val x = ((Re(circle.c) - LeftReal) * ScaleFactor).asInstanceOf[Int]
      val y = -((Im(circle.c) - TopImaginary) * ScaleFactor).asInstanceOf[Int]
      val radius = math2Pix(circle.r)
      drawing.drawCircle( x, y, radius )
    }

    else if (piclet.isInstanceOf[Rectangle]) {
      val rectangle = piclet.asInstanceOf[Rectangle]
      moveTo(drawing, rectangle.botLeft)
      lineTo(drawing, rectangle.botRight)
      lineTo(drawing, rectangle.topRight)
      lineTo(drawing, rectangle.topLeft)
      lineTo(drawing, rectangle.botLeft)
    }

    else
      draw(drawing, piclet.getSamples)

  override private[calculator] def lineTo(drawing: Drawing, c: Complex) = {
    val x = ((Re(c) - LeftReal) * ScaleFactor).asInstanceOf[Int]
    val y = -((Im(c) - TopImaginary) * ScaleFactor).asInstanceOf[Int]
    drawing.lineTo(x, y)
  }

  override private[calculator] def moveTo(drawing: Drawing, c: Complex) = {
    val x = ((Re(c) - LeftReal) * ScaleFactor).asInstanceOf[Int]
    val y = -((Im(c) - TopImaginary) * ScaleFactor).asInstanceOf[Int]
    drawing.moveTo(x, y)
  }

  override private[calculator] def point2Complex(point: Point): Complex = {
    val re = LeftReal + point.x.toDouble / ScaleFactor
    val im = TopImaginary - point.y.toDouble / ScaleFactor
    Cartesian(re, im)
  }

  override def paint(g1: Graphics): Unit = {
    val g = doubleBuffer.offScreen(g1)

    val point = new Point
    val point1 = new Point
    val drawing = new Drawing(g)
    val point2 = new Point

    val size: Dimension = getSize()
    val width = size.width
    val height = size.height
    TopImaginary = CenterImaginary + pix2Math( height / 2)
    BottomImaginary = CenterImaginary - pix2Math( height / 2)
    LeftReal = CenterReal - pix2Math( width / 2)
    RightReal = CenterReal + pix2Math( width / 2)

    val d = raiseSmooth(pix2Math(AXISMARKING))
    val l = math2Pix(d)
    var d1 = 0.0D
    var d2 = 0.0D
    var d3 = LeftReal + pix2Math(AXISSPACE)
    val d4 = RightReal - pix2Math(AXISSPACE)
    var d5 = BottomImaginary + pix2Math(AXISSPACE)
    val d6 = TopImaginary - pix2Math(AXISSPACE)

    if (d3 <= 0.0D && d4 >= 0.0D) d1 = 0.0D
    else if (d3 > 0.0D) d1 = d3
    else if (d4 < 0.0D) d1 = d4
    if (d5 <= 0.0D && d6 >= 0.0D) d2 = 0.0D
    else if (d5 > 0.0D) d2 = d5
    else if (d6 < 0.0D) d2 = d6

    cartesian2Point(d1, d2, point2)
    cartesian2Point(d3, d2, point)
    cartesian2Point(d4, d2, point1)

    drawing.drawLine(point, point1, Color.lightGray)
    var polygon = drawing.mkTriangle(point1, EAST, TRIANGLESIZE)
    g.drawPolygon(polygon)

    if (RightReal <= w.MaxReal) g.fillPolygon(polygon)
    polygon = drawing.mkTriangle(point, WEST, TRIANGLESIZE)
    g.drawPolygon(polygon)

    if (w.MinReal <= LeftReal) g.fillPolygon(polygon)
    val j = point2.y
    var d7 = Math.ceil(d3 / d)
    d3 = d7 * d
    var i = real2Pix(d3)
    while ( d3 < d4 ) {
      drawing.moveTo(i, j)
      drawing.line(0, MARKLENGTH)
      g.drawString(toString(d3), i + MARKLENGTH, j + FONTHEIGHT)
      i += l
      d3 += d
    }
    cartesian2Point(d1, d5, point)
    cartesian2Point(d1, d6, point1)
    drawing.drawLine(point, point1, Color.lightGray)
    polygon = drawing.mkTriangle(point1, NORTH, TRIANGLESIZE)
    g.drawPolygon(polygon)

    if (TopImaginary <= w.MaxImaginary) g.fillPolygon(polygon)
    polygon = drawing.mkTriangle(point, SOUTH, TRIANGLESIZE)
    g.drawPolygon(polygon)

    if (w.MinImaginary <= BottomImaginary) g.fillPolygon(polygon)
    i = point2.x
    d7 = Math.ceil(d5 / d)
    d5 = d7 * d
    var k = imag2Pix(d5)
    while ( d5 < d6 ) {
      if (d5 != 0.0D || d1 != 0.0D) {
        val s = toString(d5) + "i"
        drawing.moveTo(i, k)
        drawing.line(-MARKLENGTH, 0)
        g.drawString(s, i - MARKLENGTH - g.getFontMetrics.stringWidth(s), k + FONTHEIGHT)
      }
      d5 += d
      k -= l
    }
    w.drawStuff(drawing)

    doubleBuffer.onScreen
  }

  override private[calculator] def reset() = {
    CenterReal = 0.0D
    CenterImaginary = 0.0D
  }

  private def pix2Math(i: Int): Double = i.toDouble / ScaleFactor
  private def math2Pix(d: Double): Int = (d * ScaleFactor).toInt

  private def real2Pix(d: Double): Int = ((d - LeftReal) * ScaleFactor).toInt
  private def imag2Pix(d: Double): Int = ((TopImaginary - d) * ScaleFactor).toInt

  override def setFont(font: Font): Unit = {
    super.setFont(font)
    FONTHEIGHT = getFontMetrics(font).getAscent
    AXISSPACE = 3 * FONTHEIGHT
    AXISMARKING = 4 * FONTHEIGHT
    TRIANGLESIZE = FONTHEIGHT / 2
    MARKLENGTH = FONTHEIGHT / 5
  }

  override private[calculator] def shift(x: Int, y: Int) = {
    CenterReal += pix2Math(x)
    CenterImaginary -= pix2Math(y)
  }

  override private[calculator] def zoomIn() = {
    ScaleFactor *= 2
    repaint()
  }
  override private[calculator] def zoomOut() = {
    ScaleFactor /= 2
    repaint()
  }
}