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
 * */
package cat.inspiracio.calculator

import java.awt.{Color, Dimension, Font, Graphics, Point}
import scala.swing._

import cat.inspiracio.complex._
import cat.inspiracio.calculator.Direction._
import cat.inspiracio.geometry._

final class Plane private[calculator](val world: World) extends WorldRepresentation(world) {
  import Point2._
  import MoreGraphics.GraphicsExtended

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
  override private[calculator] def zoom: Double = factor / 40.0
  override private[calculator] def zoom_=(z: Double) : Unit = { factor = 40 * z}

  /** complex number at the centre of the plane */
  private var centre: Complex = 0

  /** complex number at the top left of the plane */
  private var topleft: Complex = 0

  /** complex number at the bottom right of the plane */
  private var botright: Complex = 0

  // methods -------------------------------------------------------------
  
  override private[calculator] def draw(g: Graphics, z: Complex) =
    if (finite(z)) {
      val p = complex2Point(z)
      g.drawCross(p, markLength)
      g.drawString(z.toString, p.x+2, p.y+2 )
    }

  override private[calculator] def draw(g: Graphics, zs: List[Complex]) =
    if (zs != null)
      pairs( zs map complex2Point ).foreach{ case (a,b) => g.drawLine(a, b) }

  override private[calculator] def draw(g: Graphics, piclet: Piclet) =
    piclet match {

      case line: Line =>
        g.drawLine(
          complex2Point(line.a),
          complex2Point(line.b)
        )

      case circle: Circle =>
        val centre = complex2Point(circle.c)
        val radius = math2Pix(circle.r)
        g.drawCircle( centre, radius )

      case rectangle: cat.inspiracio.geometry.Rectangle =>
        import rectangle._
        line(g, botLeft, botRight)
        line(g, botRight, topRight)
        line(g, topRight, topLeft)
        line(g, topLeft, botLeft)

      case c: Curve =>
        draw(g, c.getSamples)

      case _ => //Curve
        draw(g, piclet.getSamples)
    }

  private def line(g: Graphics, a: Complex, b: Complex) = g.drawLine(complex2Point(a), complex2Point(b))

  private def complex2Point(c: Complex): Point = {
    val Cartesian(x,y) = (c - topleft) * factor
    Point2(x.toInt, -y.toInt)
  }

  override private[calculator] def point2Complex(point: Point): Option[Complex] =
    Some(topleft + Cartesian( point.x, - point.y ) / factor)

  /** Draws an axis tip at where pointing in direction, and maybe filled. */
  private def tip(g: Graphics, where: Point2, direction: Direction, fill: Boolean)= {
    val triangle = g.mkTriangle(where, direction, triangleSize)
    g.fillPolygon(triangle, if(fill)Color.RED else Color.WHITE)
    g.drawPolygon(triangle)
  }

  /** Called by swing to paint. */
  override def paintComponent(g: Graphics2D): Unit = {
    val halfWidthMath = pix2Math( width / 2)
    val halfHeightMath = i*pix2Math( height / 2);

    topleft = centre - halfWidthMath + halfHeightMath
    botright = centre + halfWidthMath - halfHeightMath

    val markDistanceMath = DoubleHelper.raiseSmooth(pix2Math(markDistance))
    val markDistancePix = math2Pix(markDistanceMath)

    val axisPaddingMath = pix2Math(axisPadding)

    val axisLeftMath = Re(topleft) + axisPaddingMath
    val axisRightMath = Re(botright) - axisPaddingMath

    val axisBottomMath = Im(botright) + axisPaddingMath
    val axisTopMath = Im(topleft) - axisPaddingMath

    val crossReal =
      if ( 0.0 < axisLeftMath ) axisLeftMath
      else if ( axisRightMath < 0.0 ) axisRightMath
      else /*  axisLeft <= 0.0 && 0.0 <= axisRight */ 0.0

    val crossImg =
      if ( 0.0 < axisBottomMath ) axisBottomMath
      else if (axisTopMath < 0.0) axisTopMath
      else /* axisBottom <= 0.0 && 0.0 <= axisTop */ 0.0

    /** Point where axes cross. Often 0. */
    val cross = complex2Point( i*crossImg + crossReal )

    /** rounds towards zero and towards a mark */
    def round(d: Double) = Math.ceil(d / markDistanceMath) * markDistanceMath

    def horizontal() = {
      val left = complex2Point(i*crossImg + axisLeftMath )
      val right = complex2Point(i*crossImg + axisRightMath )
      g.drawLine(left, right, Color.lightGray)
      tip(g, right, EAST, Re(botright) <= world.MaxReal)
      tip(g, left, WEST, world.MinReal <= Re(topleft))

      //marks on horizontal axis
      var mark = round(axisLeftMath)
      while (mark < axisRightMath) {
        //draw mark at a
        val a: Point = Point2(real2Pix(mark), cross.y)
        val b: Point = a + (0, markLength)
        g.drawLine(a, b)
        g.drawString(toString(mark), a.x + markLength, cross.y + fontAscent)

        mark += markDistanceMath
      }
    }

    def vertical() = {
      val bottom = complex2Point( i*axisBottomMath + crossReal)
      val top = complex2Point( i*axisTopMath + crossReal )
      g.drawLine(bottom, top, Color.lightGray)

      tip(g, top, NORTH, Im(topleft) <= world.MaxImaginary)
      tip(g, bottom, SOUTH, world.MinImaginary <= Im(botright))

      //marks on vertical axis
      var mark = round(axisBottomMath)
      while (mark < axisTopMath) {
        //draw mark at a
        if (mark != 0.0 || crossReal != 0.0) {
          val a: Point = Point2(cross.x, imag2Pix(mark))
          val b = a + (-markLength, 0)
          g.drawLine(a, b)
          val s = toString(mark) + "i"
          g.drawString(s, cross.x - markLength - g.getFontMetrics.stringWidth(s), a.y + fontAscent)
        }

        mark += markDistanceMath
      }
    }

    horizontal()
    vertical()
    world.draw(g)
  }

  override private[calculator] def reset() = {
    centre = 0
    repaint()
  }

  private def pix2Math(i: Int): Double = i.toDouble / factor
  private def math2Pix(d: Double): Int = (d * factor).toInt

  private def real2Pix(d: Double): Int = ((d - Re(topleft)) * factor).toInt
  private def imag2Pix(d: Double): Int = ((Im(topleft) - d) * factor).toInt

  override def font_=(font: Font): Unit = {
    //super.font = font
    //fontAscent = getFontMetrics(font).ascent
    axisPadding = 3 * fontAscent
    markDistance = 4 * fontAscent
    triangleSize = fontAscent / 2
    markLength = fontAscent / 5
  }

  override private[calculator] def shift(start: Point, from: Point, to: Point) = {
    val p: Point = to - from
    centre = centre - pix2Math(p.x) + i*pix2Math(p.y)
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