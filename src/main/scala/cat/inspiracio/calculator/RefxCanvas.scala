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

import java.awt.{Color, Dimension, Font, Graphics, Point, Polygon}
import scala.swing.event.MouseEvent

import cat.inspiracio.calculator.Direction._
import cat.inspiracio.complex.{Complex, Re, double2Complex, finite}
import cat.inspiracio.geometry.{DoubleHelper, Point2}
import scala.swing.Component
import scala.swing.event.MouseInputAdapter

class RefxCanvas private[calculator](calculator: Calculator) extends Component {
  import Point2._
  import calculator.f
  import MoreGraphics.GraphicsExtended

  /** pixel distance between axis tips and window edge */
  private var axisPadding = 30

  /** pixels distance between marks on axes */
  private var markDistance = 40

  private var fontAscent = 10

  /** pixel size of the triangle at the tip of axes */
  private var triangleSize = 5

  /** pixel length of marks on axes */
  private var markLength = 2

  //State ----------------------------------------------------

  /** Max and min value of f(x) within displayed range */
  private var maxfx: Double = 0
  private var minfx: Double = 0

  /** factor between math and pixels */
  private var factor: Double = 40
  def zoom : Double = factor / 40
  def zoom_=(z: Double) = factor = z*40

  /** point in centre of window, in math */
  private var centerX: Double = 0
  private var centerY: Double = 0

  /** limits of windows, in math */
  private var top: Double = 0
  private var left: Double = 0
  private var bottom: Double = 0
  private var right: Double = 0

  init()

  private def init()= {
    background = Color.white

    val mouse = new MouseInputAdapter{
      //previous mouse position during dragging
      var previous: Point2 = null

      override def mousePressed(e: MouseEvent): Unit = previous = e.point
      override def mouseReleased(e: MouseEvent): Unit = drag(e)
      override def mouseDragged(e: MouseEvent): Unit = drag(e)

      private def drag(e: MouseEvent) = {
        val p = e.point
        shift(previous - p)
        repaint()
        previous = p
      }
    }
    addMouseListener(mouse)
    addMouseMotionListener(mouse)
    doubleBuffered = true
  }

  //Methods ------------------------------------------------------

  private def xy2Point(x: Double, y: Double): Point = Point2(
    ((x - left) * factor).toInt,
    -((y - top) * factor).toInt
  )

  private def drawIt(g: Graphics) = {
    resetExtremes()

    /** Maps an pixel-x to a point if the curve should be drawn,
      * or None. */
    def x2op(x: Int): Option[Point] = {
      try {
        val z: Complex = pix2x(x)
        val fz = f(z) //maybe Exception
        if(finite(fz)){
          val refz = Re(fz)
          updateExtremes(refz)  //small side-effect
          if(bottom <= refz && refz <= top){
            val y = ((top - refz) * factor).toInt
            Some(Point2(x,y))
          }
          else None //point is outside window
        }
        else None //infinite
      } catch {
        case _ : Exception => None  //f(z) threw exception
      }
    }

    val points: List[Option[Point]] = (0 until getSize().width).toList map x2op

    def draw: List[Option[Point]] => Unit = {
      case Nil => ()
      case Some(a) :: Nil => ()
      case None :: xs => draw(xs)
      case Some(a) :: None :: xs => draw( xs )
      case Some(a) :: Some(b) :: xs => { g.drawLine(a, b); draw( Some(b) :: xs ) }
    }

    draw(points)
  }

  override def preferredSize_= = minimumSize
  override def minimumSize_= = new Dimension(400, 300)

  /** Paint with double-buffering.
    *
    * Invoked by Swing to draw components. Applications should not invoke
    * paint directly, but should instead use the repaint method to schedule
    * the component for redrawing. */
  override def paint(g: Graphics): Unit = {
    val size: Dimension = size
    val halfHeightMath = pix2Math(size.height / 2)
    val halfWidthMath = pix2Math(size.width / 2)

    top = centerY + halfHeightMath
    bottom = centerY - halfHeightMath
    left = centerX - halfWidthMath
    right = centerX + halfWidthMath

    val markDistanceMath = DoubleHelper.raiseSmooth(pix2Math(markDistance))
    val markDistancePix = math2Pix(markDistanceMath)

    var axisLeftMath = left + pix2Math(axisPadding)
    val axisRightMath = right - pix2Math(axisPadding)

    var axisBottomMath = bottom + pix2Math(axisPadding)
    val axisTopMath = top - pix2Math(axisPadding)

    val crossX: Double =
      if ( 0 < axisLeftMath ) axisLeftMath
      else if ( axisRightMath < 0 ) axisRightMath
      else /*if ( axisLeftMath <= 0 && 0 <= axisRightMath )*/ 0

    val crossY: Double =
      if ( 0 < axisBottomMath ) axisBottomMath
      else if ( axisTopMath < 0 ) axisTopMath
      else /*if ( axisBottomMath <= 0 && 0 <= axisTopMath )*/ 0

    /** Point where axes cross. Often (0,0). */
    var cross = xy2Point(crossX, crossY)

    def tip(where: Point, direction: Direction)= {
      val triangle = g.mkTriangle(where, direction, triangleSize)
      g.fillPolygon(triangle, Color.WHITE)
      g.drawPolygon(triangle)
    }

    def round(d: Double) = Math.ceil(d / markDistanceMath) * markDistanceMath

    def horizontal() = {
      val axisLeft = xy2Point(axisLeftMath, crossY)
      val axisRight = xy2Point(axisRightMath, crossY)
      g.drawLine(axisLeft, axisRight, Color.lightGray)

      tip(axisRight, EAST)
      tip(axisLeft, WEST)

      //marks on horizontal axis
      var mark = round(axisLeftMath)
      while (mark < axisRightMath) {
        //draw mark at a
        val a: Point = Point2(x2Pix(mark), cross.y)
        val b: Point = a + (0, markLength)
        g.drawLine(a, b)
        val s = toString(mark)
        g.drawString(s, a.x + markLength, cross.y + fontAscent)

        mark += markDistanceMath
      }
    }

    var upTriangle: Polygon = null
    var downTriangle: Polygon = null

    def vertical() = {
      val axisBottom = xy2Point(crossX, axisBottomMath)
      val axisTop = xy2Point(crossX, axisTopMath)
      g.drawLine(axisBottom, axisTop, Color.lightGray)

      upTriangle = g.mkTriangle(axisTop, NORTH, triangleSize)
      g.fillPolygon(upTriangle, Color.WHITE)
      g.drawPolygon(upTriangle)

      downTriangle = g.mkTriangle(axisBottom, SOUTH, triangleSize)
      g.fillPolygon(downTriangle, Color.WHITE)
      g.drawPolygon(downTriangle)

      //marks on horizontal axis
      var mark = round(axisBottomMath)
      while ( mark < axisTopMath ) {
        //draw mark at a
        if ( mark != 0.0 || crossX != 0.0 ) {
          val s = toString(mark)
          val a: Point = Point2(cross.x, y2Pix(mark))
          val b = a + (-markLength, 0)
          g.drawLine(a, b)
          g.drawString(s, cross.x - markLength - g.getFontMetrics.stringWidth(s), a.y + fontAscent)
        }

        mark += markDistanceMath
      }
    }

    horizontal()
    vertical()

    if (f != null) drawIt(g)
    if (top <= maxfx) {
      g.fillPolygon(upTriangle, Color.RED)
      g.drawPolygon(upTriangle)
    }
    if (minfx <= bottom) {
      g.fillPolygon(downTriangle, Color.RED)
      g.drawPolygon(downTriangle)
    }
  }

  private def pix2Math(i: Int): Double = i.toDouble / factor
  private def math2Pix(d: Double): Int = (d * factor).toInt
  private def x2Pix(x: Double): Int = ((x - left) * factor).toInt
  private def y2Pix(y: Double): Int = -((y - top) * factor).toInt
  private def pix2x(i: Int): Double = i.toDouble / factor + left

  def reset() = {
    centerX = 0.0
    centerY = 0.0
  }

  override def font_=(font: Font): Unit = {
    super.font = font
    fontAscent = getFontMetrics(font).getAscent
    axisPadding = 3 * fontAscent
    markDistance = 4 * fontAscent
    triangleSize = fontAscent / 2
    markLength = fontAscent / 5
  }

  private[calculator] def shift(p: Point) = {
    centerY -= pix2Math(p.y)
    centerX += pix2Math(p.x)
  }

  def zoomIn() = factor *= 2.0
  def zoomOut() = factor /= 2.0

  def resetExtremes(): Unit = {
    minfx = Double.PositiveInfinity
    maxfx = Double.NegativeInfinity
  }

  def updateExtremes(d: Double): Unit = {
    maxfx = Math.max(maxfx, d)
    minfx = Math.min(minfx, d)
  }

  // helpers -----------------------------------

  private def toString(d: Double): String = double2Complex(d).toString
}
