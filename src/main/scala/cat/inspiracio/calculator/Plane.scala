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
import MoreGraphics.GraphicsExtended

final class Plane private[calculator](val world: World) extends WorldRepresentation(world) {

  /** pixel-distance between tips of axes and window edge */
  private var axisPadding = 30

  /** pixel-distance between marks on the axes */
  private var markDistance = 40

  private var fontAscent = 10

  /** pixel-size of the arrow triangles at the tips of axes */
  private var triangleSize = 5

  /** pixel-length of the marks on the axes */
  private var markLength = 2

  // state ----------------------------------------------------------------

  /** real * factor = pixels */
  private var factor = 40.0

  /** complex number at the centre of the plane */
  private var centre: Complex = 0

  /** complex number at the top left of the plane */
  private var topleft: Complex = 0

  /** complex number at the bottom right of the plane */
  private var botright: Complex = 0

  override private[calculator] def draw(g: Graphics, z: Complex) =
    if (finite(z)) {
      val x = ((Re(z) - Re(topleft)) * factor).asInstanceOf[Int]
      val y = -((Im(z) - Im(topleft)) * factor).asInstanceOf[Int]
      g.drawCross(x, y, markLength)
      g.drawString(z.toString, x+2, y+2 )
  }

  /** XXX get rid of null-check */
  override private[calculator] def draw(g: Graphics, zs: List[Complex]) =
    if (zs != null) {
      val ps = pairs( zs map complex2Point )
      ps.foreach{ case (a,b) => g.drawLine(a, b) }
    }

  def pairs[A](cs : List[A]): List[(A,A)] = cs match {
    case Nil => Nil
    case a::Nil => Nil
    case a::b::cs => (a,b) :: pairs(b::cs)
  }

  //XXX remove isInstanceOf
  override private[calculator] def draw(g: Graphics, piclet: Piclet) =

    if (piclet.isInstanceOf[Line]) {
      val line = piclet.asInstanceOf[Line]
      g.drawLine(
        complex2Point(line.a),
        complex2Point(line.b)
      )
    }

    else if (piclet.isInstanceOf[Circle]) {
      val circle = piclet.asInstanceOf[Circle]
      val x = ((Re(circle.c) - Re(topleft)) * factor).asInstanceOf[Int]
      val y = -((Im(circle.c) - Im(topleft)) * factor).asInstanceOf[Int]
      val radius = math2Pix(circle.r)
      g.drawCircle( x, y, radius )
    }

    else if (piclet.isInstanceOf[Rectangle]) {
      val rectangle = piclet.asInstanceOf[Rectangle]
      import rectangle._
      line(g, botLeft, botRight)
      line(g, botRight, topRight)
      line(g, topRight, topLeft)
      line(g, topLeft, botLeft)
    }

    else
      draw(g, piclet.getSamples)

  private def line(g: Graphics, a: Complex, b: Complex) = g.drawLine(complex2Point(a), complex2Point(b))

  //XXX Merge with cartesian2Point(z: Complex): Point
  private def complex2Point(c: Complex) = Point2(
    ((Re(c) - Re(topleft)) * factor).toInt,
    -((Im(c) - Im(topleft)) * factor).toInt
  )

  //XXX Merge with complex2Point(z: Complex): Point
  private def cartesian2Point(re: Double, im: Double): Point = Point2(
    ((re - Re(topleft)) * factor).toInt,
    -((im - Im(topleft)) * factor).toInt
  )

  override private[calculator] def point2Complex(point: Point): Complex = {
    val re = Re(topleft) + point.x.toDouble / factor
    val im = Im(topleft) - point.y.toDouble / factor
    Cartesian(re, im)
  }

  /** Called by swing to paint. */
  override def paint(g: Graphics): Unit = {

    val size: Dimension = getSize()
    val width = size.width
    val height = size.height

    topleft = centre - pix2Math( width / 2) + pix2Math( height / 2)*i
    botright = centre + pix2Math( width / 2) - pix2Math( height / 2)*i

    val d = DoubleHelper.raiseSmooth(pix2Math(markDistance))
    val l = math2Pix(d)
    var d1 = 0.0
    var d2 = 0.0
    var d3 = Re(topleft) + pix2Math(axisPadding)
    val d4 = Re(botright) - pix2Math(axisPadding)
    var d5 = Im(botright) + pix2Math(axisPadding)
    val d6 = Im(topleft) - pix2Math(axisPadding)

    if (d3 <= 0.0 && d4 >= 0.0) d1 = 0.0
    else if (d3 > 0.0) d1 = d3
    else if (d4 < 0.0) d1 = d4
    if (d5 <= 0.0 && d6 >= 0.0) d2 = 0.0
    else if (d5 > 0.0) d2 = d5
    else if (d6 < 0.0) d2 = d6

    val point2 = cartesian2Point(d1, d2)
    val point = cartesian2Point(d3, d2)
    val point1 = cartesian2Point(d4, d2)

    g.drawLine(point, point1, Color.lightGray)
    var polygon = g.mkTriangle(point1, EAST, triangleSize)
    g.drawPolygon(polygon)

    if (Re(botright) <= w.MaxReal) g.fillPolygon(polygon)
    polygon = g.mkTriangle(point, WEST, triangleSize)
    g.drawPolygon(polygon)

    if (w.MinReal <= Re(topleft)) g.fillPolygon(polygon)
    val j = point2.y
    var d7 = Math.ceil(d3 / d)
    d3 = d7 * d
    var i0 = real2Pix(d3)
    while ( d3 < d4 ) {
      val a: Point2 = Point2(i0, j)
      val b: Point2 = a + (0, markLength)
      g.drawLine(a, b)
      g.drawString(toString(d3), i0 + markLength, j + fontAscent)
      i0 += l
      d3 += d
    }
    val point3 = cartesian2Point(d1, d5)
    val point4 = cartesian2Point(d1, d6)
    g.drawLine(point3, point4, Color.lightGray)
    polygon = g.mkTriangle(point4, NORTH, triangleSize)
    g.drawPolygon(polygon)

    if (Im(topleft) <= w.MaxImaginary) g.fillPolygon(polygon)
    polygon = g.mkTriangle(point3, SOUTH, triangleSize)
    g.drawPolygon(polygon)

    if (w.MinImaginary <= Im(botright)) g.fillPolygon(polygon)
    i0 = point2.x
    d7 = Math.ceil(d5 / d)
    d5 = d7 * d
    var k = imag2Pix(d5)
    while ( d5 < d6 ) {
      if (d5 != 0.0D || d1 != 0.0D) {
        val s = toString(d5) + "i"
        val a = Point2(i0, k)
        val b = a + (-markLength, 0)
        g.drawLine(a, b)
        g.drawString(s, i0 - markLength - g.getFontMetrics.stringWidth(s), k + fontAscent)
      }
      d5 += d
      k -= l
    }
    w.draw(g)
  }

  override private[calculator] def reset() = {
    centre = 0
    repaint()
  }

  private def pix2Math(i: Int): Double = i.toDouble / factor
  private def math2Pix(d: Double): Int = (d * factor).toInt

  private def real2Pix(d: Double): Int = ((d - Re(topleft)) * factor).toInt
  private def imag2Pix(d: Double): Int = ((Im(topleft) - d) * factor).toInt

  override def setFont(font: Font): Unit = {
    super.setFont(font)
    fontAscent = getFontMetrics(font).getAscent
    axisPadding = 3 * fontAscent
    markDistance = 4 * fontAscent
    triangleSize = fontAscent / 2
    markLength = fontAscent / 5
  }

  override private[calculator] def shift(p: Point) = {
    centre = centre +pix2Math(p.x) -pix2Math(p.y)*i
    repaint()
  }

  override private[calculator] def zoomIn() = {
    factor *= 2
    repaint()
  }
  override private[calculator] def zoomOut() = {
    factor /= 2
    repaint()
  }
}