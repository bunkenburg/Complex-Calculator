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
import java.util.prefs.Preferences

import javax.swing._
import cat.inspiracio.complex._
import cat.inspiracio.geometry._
import cat.inspiracio.parsing.{Constant, Syntax}
import javax.swing.event.MouseInputAdapter

import MoreGraphics.GraphicsExtended

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
    * In both axes, ten to either side from the center.
    * The range from 0 to 1. */
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

  /** to the right of z-world */
  private def locate() = {
    //z world
    val zW = calculator.zW
    val zWorldDimension: Dimension = zW.getSize //550 372
    val zWorldPosition: Point = zW.getLocationOnScreen  //77 414

    val p = preferences
    val x = p.getInt("x", zWorldPosition.x + zWorldDimension.width + 10 )
    val y = p.getInt("y", zWorldPosition.y )
    setLocation( x, y )

    val width = p.getInt("width", 560)
    val height = p.getInt("height", 365)
    setSize(width,height)
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
    canvas.repaint()
  }

  override def setFont(font: Font): Unit = {
    super.setFont(font)
    canvas.setFont(font)
  }

  //Inner class ThreeDCanvas ------------------------------------------------------------

  /** We are in a 3d space where x and z are flat, and y points up.
    *
    * A cube with height 0 <= y <= 1 and sides -0.5 <= x <= 0.5
    * and -0.5 <= z <= 0.5.
    *
    * The real axis is x.
    * The imaginary axis is z.
    *
    * The starting position is real axis comes slightly to the front,
    * but imaginary axis points backwards. That means:
    *
    *   xforward && !zforward.
    *
    * */
  private class ThreeDCanvas private[calculator]() extends JComponent {

    //State ----------------------------------------------------------

    private var FONT_HEIGHT = 12

    private[calculator] val eye = Vector3(3, 0.5, 1)
    private[calculator] val direct = Vector3(2.5, 0.5, 0.5)

    private[calculator] var nxpix = 0
    private[calculator] var nypix = 0
    private[calculator] var xyscale = 0.0

    private var Q: Matrix44 = initQ

    /** Does the real axis point forward? At the start, yes. */
    private[calculator] var xforward = false

    /** Does the imaginary axis point forward? At the start, no. */
    private[calculator] var zforward = false

    init()

    //Constructor ---------------------------------------------------

    private def init()= {
      setBackground(Color.white)

      val mouse = new MouseInputAdapter {

        //during dragging, the previous mouse position
        var previous: Point2 = null

        override def mousePressed(e: MouseEvent): Unit = previous = e.getPoint
        override def mouseReleased(e: MouseEvent): Unit = drag(e)
        override def mouseDragged(e: MouseEvent): Unit = drag(e)

        private def drag(e: MouseEvent) = {
          val p = e.getPoint
          shift(previous - p)
          previous = p
        }
      }
      addMouseListener(mouse)
      addMouseMotionListener(mouse)

      setDoubleBuffered(true)
    }

    //Methods -------------------------------------------------------

    /** Draws the lines behind the surface.
      * Depends on xforward, zforward. */
    private[calculator] def drawBackAxes(g: Graphics) = {
      drawBottomLeftNumber(g)
      drawBottomRightNumber(g)
      drawTopleftNumber(g)
      drawTopRightNumber(g)

      //two base lines ---
      if (xforward)
        //left base line, which is a bit more behind
        drawLine3(g)( -0.5, 0.0, -0.5)( -0.5, 0.0, 0.5)
      else
        //right base line, which is a bit more in front
        drawLine3(g)(  0.5, 0.0, -0.5)(  0.5, 0.0, 0.5)

      if (zforward) //imaginary axis points forward (not at the start)
        //front base line, which is behind
        drawLine3(g)( -0.5, 0.0, -0.5)( 0.5, 0.0, -0.5)
      else
        //back base line
        drawLine3(g)( -0.5, 0.0,  0.5)( 0.5, 0.0,  0.5)

      //two poles ---
      if (!xforward || !zforward)
        //top right pole, on (x=0.5, z=0.5)
        drawLine3(g)( 0.5, 0.0, 0.5)( 0.5, 1.0, 0.5)

      if (!xforward || zforward)
        //bottom right pole, on (x=0.5, z=-0.5)
        drawLine3(g)( 0.5, 0.0, -0.5)( 0.5, 1.0, -0.5)

      if (xforward || !zforward)
        //top left pole, on (x=-0.5, z=0.5)
        drawLine3(g)( -0.5, 0.0, 0.5)( -0.5, 1.0, 0.5)
      if (xforward || zforward)
        //bottom left pole on (x=-0.5, z=-0.5)
        drawLine3(g)( -0.5, 0.0, -0.5)( -0.5, 1.0, -0.5)

      // two lid lines --
      if (xforward)
        //left lid line
        drawLine3(g)( -0.5, 1.0, -0.5)( -0.5, 1.0, 0.5)
      else
        //right lid line
        drawLine3(g)( 0.5, 1.0, -0.5)(  0.5, 1.0, 0.5)

      if (zforward)
        //front lid line
        drawLine3(g)( -0.5, 1.0, -0.5)( 0.5, 1.0, -0.5)
      else
        //back lid line
        drawLine3(g)( -0.5, 1.0,  0.5)( 0.5, 1.0,  0.5)
    }

    private def drawBottomLeftNumber(g: Graphics) = {
      val botleft: Point2 = f3dPix(-0.5, 0.0, -0.5)
      val s = square.botLeft.toString

      val adjusted: Point =
        if (xforward && zforward)
          botleft + (2, -2)
        else if (xforward && !zforward){
          val width = g.getFontMetrics.stringWidth(s)
          botleft + (-width - 2, FONT_HEIGHT)
        }
        else if (!xforward && zforward)
          botleft + (2, FONT_HEIGHT)
        else //if (!xforward && !zforward)
          botleft + (2, FONT_HEIGHT)

      g.drawString(s, adjusted)
    }

    private def drawBottomRightNumber(g: Graphics) = {
      val botright: Point2 = f3dPix(0.5, 0.0, -0.5)
      val s = square.botRight.toString

      val adjusted: Point =
        if (xforward && zforward) {
          val width = g.getFontMetrics.stringWidth(s)
          botright + (-width - 2, FONT_HEIGHT)
        }
        else if (xforward && !zforward)
          botright + (2, FONT_HEIGHT)
        else if (!xforward && zforward)
          botright + (2, -2)
        else //if (!xforward && !zforward)
          botright + (2, FONT_HEIGHT)

      g.drawString(s, adjusted)
    }

    private def drawTopleftNumber(g: Graphics)= {
      val topleft: Point2 = f3dPix(-0.5, 0.0, 0.5)
      val s = square.topLeft.toString

      val adjusted: Point =
        if (xforward && zforward)
          topleft + (2, FONT_HEIGHT)
        else if (xforward && !zforward)
          topleft + (2, -2)
        else if (!xforward && zforward)
          topleft + (2, FONT_HEIGHT)
        else /* if (!xforward && !zforward) */ {
          val width = g.getFontMetrics.stringWidth(s)
          topleft + (-width - 2, FONT_HEIGHT)
        }

      g.drawString(s, adjusted)
    }

    private def drawTopRightNumber(g: Graphics) = {
      val topright: Point2 = f3dPix(0.5, 0.0, 0.5)
      val s = square.topRight.toString

      val adjusted: Point =
        if (xforward && zforward)
          topright + (2, FONT_HEIGHT)
        else if (xforward && !zforward)
          topright + (2, FONT_HEIGHT)
        else if (!xforward && zforward) {
          val width = g.getFontMetrics.stringWidth(s)
          topright + (-width - 2, FONT_HEIGHT)
        }
        else //if (!xforward && !zforward)
          topright + (2, -2)

      g.drawString(s, adjusted)
    }

    /** Draws the lines in front of the surface.
      * Reads xforward, zforward. */
    private[calculator] def drawFrontAxes(g: Graphics) = {

      if (xforward)
        //right base line
        drawLine3(g)( 0.5, 0.0, -0.5)( 0.5, 0.0, 0.5)
      else
        //left base line
        drawLine3(g)( -0.5, 0.0, -0.5)( -0.5, 0.0, 0.5)

      if (zforward)
        //back base line
        drawLine3(g)( -0.5, 0.0, 0.5)( 0.5, 0.0, 0.5)
      else
        //front base line
        drawLine3(g)( -0.5, 0.0, -0.5)( 0.5, 0.0, -0.5)

      if (xforward && zforward)
        //top right pole on (x=0.5, z=0.5)
        drawLine3(g)( 0.5, 0.0, 0.5)( 0.5, 1.0, 0.5)
      else if (xforward && !zforward)
        //bottom right pole on (x=0.5, z=-0.5)
        drawLine3(g)( 0.5, 0.0, -0.5)( 0.5, 1.0, -0.5)
      else if (!xforward && zforward)
        //topleft pole on (x=-0.5, z=0.5)
        drawLine3(g)( -0.5, 0.0, 0.5)( -0.5, 1.0, 0.5)
      else if (!xforward && !zforward)
        //bottom left pole on (x=-0.5, y=-0.5)
        drawLine3(g)( -0.5, 0.0, -0.5)( -0.5, 1.0, -0.5)

      if (xforward)
        drawLine3(g)( 0.5, 1.0, -0.5)( 0.5, 1.0, 0.5)
      else
        drawLine3(g)( -0.5, 1.0, -0.5)( -0.5, 1.0, 0.5)

      if (zforward)
        drawLine3(g)( -0.5, 1.0, 0.5)( 0.5, 1.0, 0.5)
      else
        drawLine3(g)( -0.5, 1.0, -0.5)( 0.5, 1.0, -0.5)
    }

    /** z is the imaginary axis */
    private[calculator] def drawImaginaryAxis(g: Graphics) =
      drawLine3(g)( 0.0, 0.0, -0.5)( 0.0, 0.0, 0.5)

    /** y is the axis for |f(z)| */
    private[calculator] def drawModAxis(g: Graphics) =
      drawLine3(g)( 0.0, 0.0, 0.0)( 0.0, 1.0, 0.0)

    /** x is the real axis */
    private[calculator] def drawRealAxis(g: Graphics) =
      drawLine3(g)( -0.5, 0.0, 0.0)( 0.5, 0.0, 0.0)

    private[calculator] def drawLine3(g: Graphics)(x1: Double, y1: Double, z1: Double)(x2: Double, y2: Double, z2: Double) = {
      val a = f3dPix(x1, y1, z1)
      val b = f3dPix(x2, y2, z2)
      g.drawLine(a, b)
    }

    private[calculator] def cx(x: Int) = if (xforward) x else -x
    private[calculator] def cz(z: Int) = if (zforward) z else -z

    /** Draws the whole diagram. */
    private[calculator] def drawIt(g: Graphics) = {

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

      //first the axes at the back, because everything else with overwrite them
      drawBackAxes(g)

      //second draw the patches, with axis in between them
      val (xmin,xmax) = if(xforward) (-0.5, 0.5) else (0.5, -0.5)
      val xdelta = (xmax - xmin) / 20.0

      val (zmin,zmax) = if(zforward) (-0.5, 0.5) else (0.5, -0.5)
      val zdelta = (zmax - zmin) / 20.0

      var x = xmin  //XXX get rid of this
      var z = zmin  //XXX get rid of this

      //assigns line0 for the first time
      for ( i <- -10 to +10 ) {
        val y = M(10 + cx(i))(10 + cz(-10))

        line0(10 + i) = Vector2(
          Q(0,0) * x + Q(0,1) * y + Q(0,2) * z,
          Q(1,0) * x + Q(1,1) * y + Q(1,2) * z
        )

        x += xdelta
      }

      //loop over patch lines
      for ( i <- -9 to +10 ) {
        if (i == 1)
          drawRealAxis(g)
        var x = xmin
        z += zdelta

        //assigns line1
        for ( j <- -10 to +10 ) {
          val y = M(10 + cx(j))(10 + cz(i))

          line1(10 + j) = Vector2(
            Q(0,0) * x + Q(0,1) * y + Q(0,2) * z,
            Q(1,0) * x + Q(1,1) * y + Q(1,2) * z
          )

          x += xdelta
        }

        //draw one line of patches
        for ( k <- -10 to +9 ) {
          if (k == 0 && i == 0) {
            drawModAxis(g)
            drawImaginaryAxis(g)
          }
          val a = line0(10 + k)
          val b = line0(10 + k + 1)
          val c = line1(10 + k)
          val d = line1(10 + k + 1)
          patch(g, a, b, c, d)
        }

        for( i <- 0 to 20 )
          line0(i) = line1(i)
      }

      //third the front axes above everything else
      drawFrontAxes(g)
    }

    /** Maps a 3d position into 2d space.
      * (Is the 2d space the screen pixel space?) */
    private def f3d2d(v: Vector3): Vector2 =
      Vector2(
        Q(0,0) * v.x + Q(0,1) * v.y + Q(0,2) * v.z,
        Q(1,0) * v.x + Q(1,1) * v.y + Q(1,2) * v.z
      )

    /** Maps a 3d position to a screen pixel. */
    private def f3dPix(x: Double, y: Double, z: Double): Point =
      new Point(
        fx(Q(0,0) * x + Q(0,1) * y + Q(0,2) * z),
        fy(Q(1,0) * x + Q(1,1) * y + Q(1,2) * z)
      )

    private[calculator] def angle(x: Double, y: Double): Double = atan2(y, x)

    /** Maps a 2d x-coordinate to horizontal screen pixel. */
    private[calculator] def fx(x: Double): Int = (x * xyscale + nxpix * 0.5).toInt

    /** Maps a 2d y-coordinate to vertical screen pixel. */
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

      Matrix44.translate(eye).preRot('z', d).preRot('y', d1).preRot('z', -d2).postRot('y', π/2)
    }

    /** Paint with double-buffering.
      *
      * Invoked by Swing to draw components. Applications should not invoke
      * paint directly, but should instead use the repaint method to schedule
      * the component for redrawing. */
    override def paint(g: Graphics): Unit = {
      val size: Dimension = getSize
      nxpix = size.width
      nypix = size.height
      xyscale = min(nxpix, nypix) * XYFACTOR
      drawIt(g)
    }

    /** Draws a patch for vectors a, b, c, d.
      * May be a quadrilateral or a triangle. */
    private[calculator] def patch(g: Graphics, a: Vector2, b: Vector2, c: Vector2, d: Vector2): Unit = {

      val something = (b.x - a.x) * (d.y - c.y) - (b.y - a.y) * (d.x - c.x)
      if ( 0.0001 < abs(something) ) {
        val d1 = ((c.x - a.x) * (d.y - c.y) - (c.y - a.y) * (d.x - c.x)) / something

        if ( 0 <= d1 && d1 <= 1 ) {
          val v5 = Vector2(
            (1 - d1) * a.x + d1 * b.x,
            (1 - d1) * a.y + d1 * b.y
          )
          triangle(g, a, c, v5)
          triangle(g, b, d, v5)
        }

        else {
          val something = (c.x - a.x) * (d.y - b.y) - (c.y - a.y) * (d.x - b.x)
          if ( 0.0001D < abs(something) ) {
            val d2 = ((b.x - a.x) * (d.y - b.y) - (b.y - a.y) * (d.x - b.x)) / something
            if ( 0 <= d2 && d2 <= 1 ) {
              val v5 = Vector2(
                (1 - d2) * a.x + d2 * c.x,
                (1.0D - d2) * a.y + d2 * c.y
              )
              triangle(g, a, b, v5)
              triangle(g, c, d, v5)
            }
            else
              quadrilateral(g, a, b, d, c)
          }
          else
            quadrilateral(g, a, b, d, c)
        }
      }

      else {
        val something = (c.x - a.x) * (d.y - b.y) - (c.y - a.y) * (d.x - b.x)
        if ( 0.0001 < abs(something) ) {
          val d3 = ((b.x - a.x) * (d.y - b.y) - (b.y - a.y) * (d.x - b.x)) / something
          if (0 <= d3 && d3 <= 1) {
            val v5 = Vector2(
              (1 - d3) * a.x + d3 * c.x,
              (1 - d3) * a.y + d3 * c.y
            )
            triangle(g, a, b, v5)
            triangle(g, c, d, v5)
          }
          else
            quadrilateral(g, a, b, d, c)
        }
        else
          quadrilateral(g, a, b, d, c)
      }

    }

    /** draws a quadrilateral */
    private[calculator] def quadrilateral(g: Graphics, a: Vector2, b: Vector2, c: Vector2, d: Vector2) = {
      val quad = new Polygon
      quad.addPoint(fx(a.x), fy(a.y))
      quad.addPoint(fx(b.x), fy(b.y))
      quad.addPoint(fx(c.x), fy(c.y))
      quad.addPoint(fx(d.x), fy(d.y))
      g.fillPolygon(quad, Color.lightGray)
      g.drawPolygon(quad)
    }

    override def setFont(font: Font): Unit = {
      super.setFont(font)
      FONT_HEIGHT = getFontMetrics(font).getAscent
    }

    /** For now, we can only rotate horizontally.
      * Could consider other rotations and a reset button. */
    private[calculator] def shift(p: Point) = {
      val size: Dimension = getSize
      val d = p.x * 2 * π / size.height
      Q = Q.postRot('y', -d)
      repaint()
    }

    /** draws a triangle */
    private[calculator] def triangle(g: Graphics, a: Vector2, b: Vector2, c: Vector2) = {
      val triangle = new Polygon
      triangle.addPoint(fx(a.x), fy(a.y))
      triangle.addPoint(fx(b.x), fy(b.y))
      triangle.addPoint(fx(c.x), fy(c.y))
      g.fillPolygon(triangle, Color.lightGray)
      g.drawPolygon(triangle)
    }

  }//inner class ThreeDCanvas

  protected def preferences = Preferences.userNodeForPackage(getClass).node(getClass.getSimpleName)

  override def dispose(): Unit = {
    if(isVisible){
      val p = preferences
      val Point2(x,y) = getLocationOnScreen
      p.putInt("x", x )
      p.putInt("y", y )

      val size = getSize
      p.putInt("width", size.width)
      p.putInt("height", size.height)
    }
    super.dispose()
  }
}