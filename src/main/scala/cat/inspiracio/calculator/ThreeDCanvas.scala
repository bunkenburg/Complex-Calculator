package cat.inspiracio.calculator

import java.awt._
import java.awt.event.MouseEvent
import java.lang.Math.{atan2, min}

import cat.inspiracio.complex.{Complex, abs, sqrt, π}
import cat.inspiracio.geometry.{Matrix44, Point2, Vector2, Vector3}
import javax.swing.JComponent
import javax.swing.event.MouseInputAdapter
import MoreGraphics.GraphicsExtended

/** Shows a grid of |f(z)|.
  *
  * The grid is ThreeDWorld.M corresponding to ZWorld.square,
  * and the function f i calculator.f.
  *
  * We are in a 3d space where x and z are flat, and y points up.
  *
  * A cube with height 0 <= y <= 1 and sides -0.5 <= x <= 0.5
  * and -0.5 <= z <= 0.5.
  *
  * The real axis is x.
  * The imaginary axis is z.
  *
  * The starting position is real axis comes slightly to the front,
  * but imaginary axis points backwards. That means, forward is:
  *
  *   xforward && !zforward.
  *
  * */
class ThreeDCanvas(world: ThreeDWorld) extends JComponent {
  import Color._

  //State ----------------------------------------------------------

  private var fontAscent = 12

  private[calculator] var xyscale = 0.0

  /** transformation matric between 3d space and view space */
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
  private def drawBackAxes(g: Graphics) = {
    import world.square

    drawNumber(g, square.botLeft, -0.5, -0.5)
    drawNumber(g, square.botRight, -0.5, 0.5)
    drawNumber(g, square.topLeft, 0.5, -0.5)
    drawNumber(g, square.topRight, 0.5, 0.5)

    val (xfront,xback) = if(xforward) (0.5, -0.5) else (-0.5, 0.5)
    val (zfront,zback) = if(zforward) (0.5, -0.5) else (-0.5, 0.5)

    //two base lines
    drawLine3(g, GRAY)(xback, 0, -0.5)(xback, 0, 0.5)
    drawLine3(g, GRAY)(-0.5, 0, zback)(0.5, 0, zback)

    //three poles
    drawLine3(g, GRAY)(xback, 0, zback)(xback, 1, zback)
    drawLine3(g, GRAY)(xback, 0, zfront)(xback, 1, zfront)
    drawLine3(g, GRAY)(xfront, 0, zback)(xfront, 1, zback)

    //two lid lines
    drawLine3(g, GRAY)(xback, 1, -0.5)(xback, 1, 0.5)
    drawLine3(g, GRAY)(-0.5, 1, zback)(0.5, 1, zback)
  }

  private def drawNumber(g: Graphics, c: Complex, x: Double, z: Double) = {
    val v = f3d2d(x, 0, z)
    val front = v.y <= 0
    val left = v.x <= 0

    val point = Point2(fx(v.x), fy(v.y))
    val s = c.toString

    val adjusted = point + (
      if(left) (-g.getFontMetrics.stringWidth(s) - 2) else 2,
      if(front) fontAscent else -2
    )

    g.drawString(s, adjusted)
  }

  /** Draws the lines in front of the surface.
    * Reads xforward, zforward. */
  private[calculator] def drawFrontAxes(g: Graphics) = {
    val (xfront,xback) = if(xforward) (0.5, -0.5) else (-0.5, 0.5)
    val (zfront,zback) = if(zforward) (0.5, -0.5) else (-0.5, 0.5)

    //two base lines
    drawLine3(g, GRAY)( xfront, 0, -0.5)( xfront, 0, 0.5)
    drawLine3(g, GRAY)( -0.5, 0, zfront)( 0.5, 0, zfront)

    //front pole
    drawLine3(g, GRAY)( xfront, 0, zfront)( xfront, 1, zfront)

    //two lid lines
    drawLine3(g, GRAY)( xfront, 1, -0.5)( xfront, 1, 0.5)
    drawLine3(g, GRAY)( -0.5, 1, zfront)( 0.5, 1, zfront)
  }

  /** z is the imaginary axis */
  private[calculator] def drawImaginaryAxis(g: Graphics) =
    drawLine3(g, GRAY)( 0, 0, -0.5)( 0, 0, 0.5)

  /** y is the axis for |f(z)| */
  private[calculator] def drawModAxis(g: Graphics) =
    drawLine3(g, GRAY)( 0, 0, 0)( 0, 1, 0)

  /** x is the real axis */
  private[calculator] def drawRealAxis(g: Graphics) =
    drawLine3(g, GRAY)( -0.5, 0, 0)( 0.5, 0, 0)

  private[calculator] def drawLine3(g: Graphics)(x1: Double, y1: Double, z1: Double)(x2: Double, y2: Double, z2: Double) = {
    val a = f3dPix(x1, y1, z1)
    val b = f3dPix(x2, y2, z2)
    g.drawLine(a, b)
  }

  private[calculator] def drawLine3(g: Graphics, c: Color)(x1: Double, y1: Double, z1: Double)(x2: Double, y2: Double, z2: Double): Unit = {
    val a = f3dPix(x1, y1, z1)
    val b = f3dPix(x2, y2, z2)
    g.drawLine(a, b, c)
  }

  private[calculator] def cx(x: Int) = if (xforward) x else -x
  private[calculator] def cz(z: Int) = if (zforward) z else -z

  /** Draws the whole diagram. */
  private[calculator] def drawIt(g: Graphics) = {
    import world.{M,N}

    //On the screen, the two edges of one line of patches
    var line0 = new Array[Vector2](2*N + 1)
    val line1 = new Array[Vector2](2*N + 1)

    //1. the axes at the back, because everything else with overwrite them
    drawBackAxes(g)

    //2. draw the patches, with axis in between them
    val (xmin,xmax) = if(xforward) (-0.5, 0.5) else (0.5, -0.5)
    val xdelta = (xmax - xmin) / (2*N)

    val (zmin,zmax) = if(zforward) (-0.5, 0.5) else (0.5, -0.5)
    val zdelta = (zmax - zmin) / (2*N)

    var x = xmin  //XXX get rid of this
    var z = zmin  //XXX get rid of this

    //assigns line0 for the first time
    for ( i <- -N to N ) {
      val y = M(N + cx(i))(N + cz(-N))
      line0(N + i) = f3d2d(x,y,z)
      x += xdelta
    }

    //loop over patch lines
    for ( i <- -N+1 to N ) {
      if (i == 1)
        drawRealAxis(g)
      var x = xmin
      z += zdelta

      //assigns line1
      for ( j <- -N to N ) {
        val y = M(N + cx(j))(N + cz(i))
        line1(N + j) = f3d2d(x,y,z)
        x += xdelta
      }

      //draw one line of patches
      for ( k <- -N to N-1 ) {
        if (k == 0 && i == 0) {
          drawModAxis(g)
          drawImaginaryAxis(g)
        }
        val a = line0(N + k)
        val b = line0(N + k + 1)
        val c = line1(N + k)
        val d = line1(N + k + 1)
        patch(g, a, b, c, d)
      }

      for( i <- 0 to 2*N )
        line0(i) = line1(i)
    }

    //3. the front axes above everything else
    drawFrontAxes(g)
  }

  /** Maps a 3d position into 2d space. */
  private def f3d2d(x: Double, y: Double, z: Double): Vector2 =
    Vector2(
      Q(0,0) * x + Q(0,1) * y + Q(0,2) * z,
      Q(1,0) * x + Q(1,1) * y + Q(1,2) * z
    )

  /** Maps a 3d position to a screen pixel. */
  private def f3dPix(x: Double, y: Double, z: Double): Point = {
    val Vector2(a,b) = f3d2d(x,y,z)
    new Point(fx(a),fy(b))
  }

  private[calculator] def angle(x: Double, y: Double): Double = atan2(y, x)

  /** Maps a 2d x-coordinate to horizontal screen pixel. */
  private[calculator] def fx(x: Double): Int = (x * xyscale + getWidth * 0.5).toInt

  /** Maps a 2d y-coordinate to vertical screen pixel. */
  private[calculator] def fy(y: Double): Int = (-y * xyscale + getHeight * 0.8).toInt

  override def getPreferredSize: Dimension = getMinimumSize
  override def getMinimumSize: Dimension = new Dimension(400, 300)

  /** depends on vector direct and eye */
  private[calculator] def initQ: Matrix44 = {

    //position of the eye of the observer
    val eye = Vector3(3, 0.5, 1)

    //direction toward the cube (I think)
    val direct = Vector3(2.5, 0.5, 0.5)

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
    /** Determines how much of the window the diagram should take up. */
    val factor = 0.6

    xyscale = min(getWidth, getHeight) * factor
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
    fontAscent = getFontMetrics(font).getAscent
  }

  /** re-calculate xforward and zforward */
  private def determineForwardNess() = {
    val x1 = f3d2d(1, 0, 0)
    xforward = x1.y <= 0

    val z1 = f3d2d(0, 0, 1)
    zforward = z1.y <= 0
  }

  /** For now, we can only rotate horizontally.
    * Could consider other rotations and a reset button. */
  private[calculator] def shift(p: Point) = {
    val size: Dimension = getSize
    val d = p.x * 2 * π / size.height
    Q = Q.postRot('y', -d)
    determineForwardNess()
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

}