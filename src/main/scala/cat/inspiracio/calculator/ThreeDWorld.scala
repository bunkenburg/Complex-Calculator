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
import java.awt.event._
import java.lang.Math.{atan2, max, min}

import cat.inspiracio.complex._
import cat.inspiracio.geometry.{Matrix44, Square, Vector2, Vector3}
import cat.inspiracio.parsing.{Syntax, Constant}
import javax.swing._

// Referenced classes of package bunkenba.calculator:
//            Calculator, DoubleBuffer, Drawing, Matrix44,
//            Vector2, Vector3

final class ThreeDWorld private[calculator](var calculator: Calculator) extends JFrame("|f(z)| World") {

  //State ---------------------------------------------

  /** Determines how much of the window the diagram should take up. */
  private[calculator] val XYFACTOR = 0.6

  private val canvas = new ThreeDCanvas

  /** the square for which to show |f(z)| */
  private var square = calculator.getSquare

  private var f: Syntax = null

  /** The absolute values: |f(z)|
    * 21 * 21 Doubles.
    * In both axes, ten to either side from the center. */
  private[calculator] val M = new Array[Array[Double]](21)

  init()

  private def init()= {
    for ( i <- 0 to 20 )
      M(i) = new Array[Double](21)

    setLayout(new BorderLayout)
    add("Center", canvas)

    addWindowListener(new WindowAdapter() {
      override def windowClosing(windowevent: WindowEvent): Unit = calculator.quit()
    })
    pack()
    locate()
    setVisible(true)
  }

  //Methods -----------------------------------------

  private def locate() = {
    setLocationRelativeTo(calculator)
    setLocationByPlatform(true)
  }

  /** Event listener: the function has changed. */
  private[calculator] def functionChanged(t: Syntax) = {
    f = t
    setNeighbourhood()
    canvas.repaint()
  }

  /** Assigns to M values |f(z)|. */
  private[calculator] def setNeighbourhood(): Unit = {
    if (f == null)
      f = new Constant(0)

    val step: Double = square.side / 2.0 / 10.0
    val center: Complex = square.center

    // Find all |f(z)|
    for( x <- -10 to +10 ; y <- -10 to +10 ){
      val z = Cartesian(step * x, step * y)
      M(10 + x)(10 + y) = try {
        val fz = f( center + z )
        if(!finite(fz)) Double.PositiveInfinity else abs(fz)
      } catch {
        case _ : Exception => -0.2
      }
    }

    //Find the maximum for scaling
    var maximum = 0.0
    for( x <- -10 to +10 ; y <- -10 to +10 ){
      val r = M(10 + x)(10 + y)
      if (! x.isInfinite() )
        maximum = max( maximum, r )
    }

    //Maximum must not be zero because we will divide by it.
    if (maximum < 0.0001) maximum = 1.0

    //scale to [-0.2, 1.2]
    for( x <- -10 to +10 ; y <- -10 to +10 ){
      val r = M(10 + x)(10 + y)
      M(10 + x)(10 + y) =
        if ( r.isInfinite() ) 1.2   //Infinity peaks above the box.
        else if ( r < 0.0 ) -0.2    //A singularity peaks below the box.
        else r / maximum
    }

  }

  private[calculator] def change(sq: Square) = {
    square = sq
    setNeighbourhood()
    canvas.paint(canvas.getGraphics)
  }

  override def setFont(font: Font): Unit = {
    super.setFont(font)
    canvas.setFont(font)
  }

  override def update(g: Graphics): Unit = paint(g)

  //Inner class ThreeDCanvas ------------------------------------------------------------

  private class ThreeDCanvas private[calculator]() extends JComponent {

    //State ----------------------------------------------------------

    final private val doubleBuffer = new DoubleBuffer(this)

    private var FONT_HEIGHT = 12

    /** previous mouse position */
    private var prevx = 0
    private var prevy = 0

    private[calculator] val eye = Vector3(3, 0.5, 1)
    private[calculator] val direct = Vector3(2.5, 0.5, 0.5)

    private[calculator] var nxpix = 0
    private[calculator] var nypix = 0
    private[calculator] var xyscale = .0

    private var Q: Matrix44 = initQ

    private[calculator] var xforward = false
    private[calculator] var zforward = false

    init()

    //Constructor ---------------------------------------------------

    private def init()= {

      setBackground(Color.white)

      addMouseListener(new MouseAdapter() {

        override def mousePressed(mouseevent: MouseEvent): Unit = {
          prevx = mouseevent.getX
          prevy = mouseevent.getY
          mouseevent.consume()
        }

        override def mouseReleased(mouseevent: MouseEvent): Unit = {
          val x = mouseevent.getX
          val y = mouseevent.getY
          shift(prevx - x, prevy - y)
          paint(getGraphics)
          prevx = x
          prevy = y
          mouseevent.consume()
        }

      })

      addMouseMotionListener(new MouseMotionAdapter() {

        override def mouseDragged(mouseevent: MouseEvent): Unit = {
          val x = mouseevent.getX
          val y = mouseevent.getY
          shift(prevx - x, prevy - y)
          paint(getGraphics)
          prevx = x
          prevy = y
          mouseevent.consume()
        }

      })

    }


    //Methods -------------------------------------------------------

    /** Depends on xforward, zforward. */
    private[calculator] def drawBackAxes(drawing: Drawing) = {

      var s = square.botLeft.toString
      moveTo3(drawing, -0.5D, 0.0D, -0.5D)
      if (xforward && zforward) drawing.move(2, -2)
      else if (xforward && !zforward) drawing.move(-drawing.graphics.getFontMetrics.stringWidth(s) - 2, FONT_HEIGHT)
      else if (!xforward && zforward) drawing.move(2, FONT_HEIGHT)
      else if (!xforward && !zforward) drawing.move(2, FONT_HEIGHT)
      drawing.drawString(s)
      moveTo3(drawing, 0.5D, 0.0D, -0.5D)

      s = square.botRight.toString
      if (xforward && zforward) drawing.move(-drawing.graphics.getFontMetrics.stringWidth(s) - 2, FONT_HEIGHT)
      else if (xforward && !zforward) drawing.move(2, FONT_HEIGHT)
      else if (!xforward && zforward) drawing.move(2, -2)
      else if (!xforward && !zforward) drawing.move(2, FONT_HEIGHT)
      drawing.drawString(square.botRight.toString)
      moveTo3(drawing, -0.5D, 0.0D, 0.5D)

      s = square.topLeft.toString
      if (xforward && zforward) drawing.move(2, FONT_HEIGHT)
      else if (xforward && !zforward) drawing.move(2, -2)
      else if (!xforward && zforward) drawing.move(2, FONT_HEIGHT)
      else if (!xforward && !zforward) drawing.move(-drawing.graphics.getFontMetrics.stringWidth(s) - 2, FONT_HEIGHT)
      drawing.drawString(s)
      moveTo3(drawing, 0.5D, 0.0D, 0.5D)

      s = square.topRight.toString
      if (xforward && zforward) drawing.move(2, FONT_HEIGHT)
      else if (xforward && !zforward) drawing.move(2, FONT_HEIGHT)
      else if (!xforward && zforward) drawing.move(-drawing.graphics.getFontMetrics.stringWidth(s) - 2, FONT_HEIGHT)
      else if (!xforward && !zforward) drawing.move(2, -2)
      drawing.drawString(s)

      if (xforward) drawLine3(drawing, -0.5D, 0.0D, -0.5D, -0.5D, 0.0D, 0.5D)
      else drawLine3(drawing, 0.5D, 0.0D, -0.5D, 0.5D, 0.0D, 0.5D)

      if (zforward) drawLine3(drawing, -0.5D, 0.0D, -0.5D, 0.5D, 0.0D, -0.5D)
      else drawLine3(drawing, -0.5D, 0.0D, 0.5D, 0.5D, 0.0D, 0.5D)

      if (!xforward || !zforward) drawLine3(drawing, 0.5D, 0.0D, 0.5D, 0.5D, 1.0D, 0.5D)
      if (!xforward || zforward) drawLine3(drawing, 0.5D, 0.0D, -0.5D, 0.5D, 1.0D, -0.5D)
      if (xforward || !zforward) drawLine3(drawing, -0.5D, 0.0D, 0.5D, -0.5D, 1.0D, 0.5D)
      if (xforward || zforward) drawLine3(drawing, -0.5D, 0.0D, -0.5D, -0.5D, 1.0D, -0.5D)

      if (xforward) drawLine3(drawing, -0.5D, 1.0D, -0.5D, -0.5D, 1.0D, 0.5D)
      else drawLine3(drawing, 0.5D, 1.0D, -0.5D, 0.5D, 1.0D, 0.5D)

      if (zforward)
        drawLine3(drawing, -0.5D, 1.0D, -0.5D, 0.5D, 1.0D, -0.5D)
      else
        drawLine3(drawing, -0.5D, 1.0D, 0.5D, 0.5D, 1.0D, 0.5D)
    }

    /** Reads xforward, zforward. */
    private[calculator] def drawFrontAxes(drawing: Drawing) = {
      if (xforward)
        drawLine3(drawing, 0.5D, 0.0D, -0.5D, 0.5D, 0.0D, 0.5D)
      else
        drawLine3(drawing, -0.5D, 0.0D, -0.5D, -0.5D, 0.0D, 0.5D)
      if (zforward)
        drawLine3(drawing, -0.5D, 0.0D, 0.5D, 0.5D, 0.0D, 0.5D)
      else
        drawLine3(drawing, -0.5D, 0.0D, -0.5D, 0.5D, 0.0D, -0.5D)
      if (xforward && zforward)
        drawLine3(drawing, 0.5D, 0.0D, 0.5D, 0.5D, 1.0D, 0.5D)
      else if (xforward && !zforward)
        drawLine3(drawing, 0.5D, 0.0D, -0.5D, 0.5D, 1.0D, -0.5D)
      else if (!xforward && zforward)
        drawLine3(drawing, -0.5D, 0.0D, 0.5D, -0.5D, 1.0D, 0.5D)
      else if (!xforward && !zforward)
        drawLine3(drawing, -0.5D, 0.0D, -0.5D, -0.5D, 1.0D, -0.5D)
      if (xforward)
        drawLine3(drawing, 0.5D, 1.0D, -0.5D, 0.5D, 1.0D, 0.5D)
      else
        drawLine3(drawing, -0.5D, 1.0D, -0.5D, -0.5D, 1.0D, 0.5D)

      if (zforward)
        drawLine3(drawing, -0.5D, 1.0D, 0.5D, 0.5D, 1.0D, 0.5D)
      else
        drawLine3(drawing, -0.5D, 1.0D, -0.5D, 0.5D, 1.0D, -0.5D)
    }

    private[calculator] def drawImaginaryAxis(drawing: Drawing) = drawLine3(drawing, 0.0D, 0.0D, -0.5D, 0.0D, 0.0D, 0.5D)

    private[calculator] def drawModAxis(drawing: Drawing) = drawLine3(drawing, 0.0D, 0.0D, 0.0D, 0.0D, 1.0D, 0.0D)

    private[calculator] def drawRealAxis(drawing: Drawing) = drawLine3(drawing, -0.5D, 0.0D, 0.0D, 0.5D, 0.0D, 0.0D)

    private[calculator] def drawLine3(drawing: Drawing, x1: Double, y1: Double, z1: Double, x2: Double, y2: Double, z2: Double) = {
      moveTo3(drawing, x1, y1, z1)
      lineTo3(drawing, x2, y2, z2)
    }

    private[calculator] def cx(x: Int) = if (xforward) x else -x
    private[calculator] def cz(z: Int) = if (zforward) z else -z

    private[calculator] def drawIt(drawing: Drawing) = {

      //On the screen, the two edges of one line of patches
      var line0 = new Array[Vector2](21)
      val line1 = new Array[Vector2](21)

      {
        val zero = f3d2d(Vector3(0, 0, 0))
        val x1 = f3d2d(Vector3(1, 0, 0))
        val z1 = f3d2d(Vector3(0, 0, 1))
        xforward = x1.y <= zero.y
        zforward = z1.y <= zero.y
      }

      drawBackAxes(drawing)

      var d2 = -0.5
      var d3 = 0.5
      if (!xforward) {
        val d11 = d2
        d2 = d3
        d3 = d11
      }
      val d4 = (d3 - d2) / 20.0
      var d8 = -0.5
      var d9 = 0.5
      if (!zforward) {
        val d12 = d8
        d8 = d9
        d9 = d12
      }
      val d10 = (d9 - d8) / 20.0
      var d = d2
      var d7 = d8

      //assigns line0 for the first time
      for ( i <- -10 to +10 ) {
        val d5 = M(10 + cx(i))(10 + cz(-10))

        line0(10 + i) = Vector2(
          Q(0,0) * d + Q(0,1) * d5 + Q(0,2) * d7,
          Q(1,0) * d + Q(1,1) * d5 + Q(1,2) * d7
        )

        d += d4
      }

      //loop over patch lines
      for ( i1 <- -9 to +10 ) {
        if (i1 == 1) drawRealAxis(drawing)
        var d1 = d2
        d7 += d10

        //assigns line1
        for ( j <- -10 to +10 ) {
          val d6 = M(10 + cx(j))(10 + cz(i1))

          line1(10 + j) = Vector2(
            Q(0,0) * d1 + Q(0,1) * d6 + Q(0,2) * d7,
            Q(1,0) * d1 + Q(1,1) * d6 + Q(1,2) * d7
          )

          d1 += d4
        }

        //draw one line of patches
        for ( k <- -10 to +9 ) {
          if (k == 0 && i1 == 0) {
            drawModAxis(drawing)
            drawImaginaryAxis(drawing)
          }
          val a = line0(10 + k)
          val b = line0(10 + k + 1)
          val c = line1(10 + k)
          val d = line1(10 + k + 1)
          patch(drawing, a, b, c, d)
        }

        for( i <- 0 to 20 )
          line0(i) = line1(i)
      }

      drawFrontAxes(drawing)
    }

    private def f3d2d(v: Vector3): Vector2 =
      Vector2(
        Q(0,0) * v.x + Q(0,1) * v.y + Q(0,2) * v.z,
        Q(1,0) * v.x + Q(1,1) * v.y + Q(1,2) * v.z
      )

    private def f3dPix(x: Double, y: Double, z: Double): Point =
      new Point(
        fx(Q(0,0) * x + Q(0,1) * y + Q(0,2) * z),
        fy(Q(1,0) * x + Q(1,1) * y + Q(1,2) * z)
      )

    private[calculator] def angle(x: Double, y: Double): Double = atan2(y, x)

    private[calculator] def fx(x: Double): Int = (x * xyscale + nxpix * 0.5).toInt

    private[calculator] def fy(y: Double): Int = (-y * xyscale + nypix * 0.8).toInt

    override def getPreferredSize: Dimension = getMinimumSize
    override def getMinimumSize: Dimension = new Dimension(400, 300)

    /** depends on vector direct and eye */
    private[calculator] def initQ: Matrix44 = {

      val d = angle(-direct.x, -direct.y)
      val d3 = sqrt(direct.x * direct.x + direct.y * direct.y)
      val d1 = angle(-direct.z, d3)
      val d4 = sqrt(d3 * d3 + direct.z * direct.z)
      val d2 = angle(-direct.x * d4, direct.y * direct.z)

      Matrix44.translation(eye).preRot('z', d).preRot('y', d1).preRot('z', -d2).postRot('y', π/2)
    }

    private[calculator] def lineTo3(drawing: Drawing, x: Double, y: Double, z: Double) = drawing.lineTo(f3dPix(x, y, z ))

    private[calculator] def moveTo3(drawing: Drawing, x: Double, y: Double, z: Double) = drawing.moveTo(f3dPix(x, y, z ))

    /** Paint with double-buffering */
    override def paint(g0: Graphics): Unit = {
      val size: Dimension = getSize
      val g = doubleBuffer.offScreen(g0)
      val drawing = new Drawing(g)
      nxpix = size.width
      nypix = size.height
      xyscale = min(nxpix, nypix) * XYFACTOR
      drawIt(drawing)
      doubleBuffer.onScreen()
    }

    /** Draws a patch for vectors a, b, c, d.
      * May be a quadrilateral or a triangle. */
    private[calculator] def patch(drawing: Drawing, a: Vector2, b: Vector2, c: Vector2, d: Vector2): Unit = {

      var something = (b.x - a.x) * (d.y - c.y) - (b.y - a.y) * (d.x - c.x)

      if ( 0.0001 < abs(something) ) {
        val d1 = ((c.x - a.x) * (d.y - c.y) - (c.y - a.y) * (d.x - c.x)) / something

        if ( 0 <= d1 && d1 <= 1 ) {
          val v5 = Vector2(
            (1 - d1) * a.x + d1 * b.x,
            (1 - d1) * a.y + d1 * b.y
          )
          triangle(drawing, a, c, v5)
          triangle(drawing, b, d, v5)
        }

        else {
          something = (c.x - a.x) * (d.y - b.y) - (c.y - a.y) * (d.x - b.x)
          if ( 0.0001D < abs(something) ) {
            val d2 = ((b.x - a.x) * (d.y - b.y) - (b.y - a.y) * (d.x - b.x)) / something
            if ( 0 <= d2 && d2 <= 1 ) {
              val v5 = Vector2(
                (1 - d2) * a.x + d2 * c.x,
                (1.0D - d2) * a.y + d2 * c.y
              )
              triangle(drawing, a, b, v5)
              triangle(drawing, c, d, v5)
            }
            else
              quadrilateral(drawing, a, b, d, c)
          }
          else
            quadrilateral(drawing, a, b, d, c)
        }
      }

      else {
        something = (c.x - a.x) * (d.y - b.y) - (c.y - a.y) * (d.x - b.x)
        if ( 0.0001 < abs(something) ) {
          val d3 = ((b.x - a.x) * (d.y - b.y) - (b.y - a.y) * (d.x - b.x)) / something
          if (0 <= d3 && d3 <= 1) {
            val v5 = Vector2(
              (1 - d3) * a.x + d3 * c.x,
              (1 - d3) * a.y + d3 * c.y
            )
            triangle(drawing, a, b, v5)
            triangle(drawing, c, d, v5)
          }
          else
            quadrilateral(drawing, a, b, d, c)
        }
        else
          quadrilateral(drawing, a, b, d, c)
      }

    }

    private[calculator] def quadrilateral(drawing: Drawing, a: Vector2, b: Vector2, c: Vector2, d: Vector2) = {
      val quad = new Polygon
      quad.addPoint(fx(a.x), fy(a.y))
      quad.addPoint(fx(b.x), fy(b.y))
      quad.addPoint(fx(c.x), fy(c.y))
      quad.addPoint(fx(d.x), fy(d.y))
      drawing.fillPolygon(quad, Color.lightGray)
      drawing.graphics.drawPolygon(quad)
    }

    override def setFont(font: Font): Unit = {
      super.setFont(font)
      FONT_HEIGHT = getFontMetrics(font).getAscent
    }

    /** curiously, does not depend on y */
    private[calculator] def shift(x: Int, y: Int) = {
      val size: Dimension = getSize
      val d = x * 2 * π / size.height
      Q = Q.postRot('y', -d)
    }

    private[calculator] def triangle(drawing: Drawing, a: Vector2, b: Vector2, c: Vector2) = {
      val triangle = new Polygon
      triangle.addPoint(fx(a.x), fy(a.y))
      triangle.addPoint(fx(b.x), fy(b.y))
      triangle.addPoint(fx(c.x), fy(c.y))
      drawing.fillPolygon(triangle, Color.lightGray)
      drawing.graphics.drawPolygon(triangle)
    }

    override def update(g: Graphics): Unit = paint(g)

  }//inner class ThreeDCanvas

}