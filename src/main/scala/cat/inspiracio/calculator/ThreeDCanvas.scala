package cat.inspiracio.calculator

import java.awt._
import java.awt.event.MouseEvent
import java.lang.Math.{atan2, min}

import cat.inspiracio.complex.{abs, sqrt, π}
import cat.inspiracio.geometry.{Matrix44, Point2, Vector2, Vector3}
import javax.swing.JComponent
import javax.swing.event.MouseInputAdapter

import MoreGraphics.GraphicsExtended

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
class ThreeDCanvas(world: ThreeDWorld) extends JComponent {

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
    val s = world.square.botLeft.toString

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
    val s = world.square.botRight.toString

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
    val s = world.square.topLeft.toString

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
    val s = world.square.topRight.toString

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
    import world.N

    //On the screen, the two edges of one line of patches
    var line0 = new Array[Vector2](2*N + 1)
    val line1 = new Array[Vector2](2*N + 1)

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
    val xdelta = (xmax - xmin) / (2*N)

    val (zmin,zmax) = if(zforward) (-0.5, 0.5) else (0.5, -0.5)
    val zdelta = (zmax - zmin) / (2*N)

    var x = xmin  //XXX get rid of this
    var z = zmin  //XXX get rid of this

    //assigns line0 for the first time
    for ( i <- -N to N ) {
      val y = world.M(N + cx(i))(N + cz(-N))

      line0(N + i) = Vector2(
        Q(0,0) * x + Q(0,1) * y + Q(0,2) * z,
        Q(1,0) * x + Q(1,1) * y + Q(1,2) * z
      )

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
        val y = world.M(N + cx(j))(N + cz(i))

        line1(N + j) = Vector2(
          Q(0,0) * x + Q(0,1) * y + Q(0,2) * z,
          Q(1,0) * x + Q(1,1) * y + Q(1,2) * z
        )

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
    /** Determines how much of the window the diagram should take up. */
    val factor = 0.6

    val size: Dimension = getSize
    nxpix = size.width
    nypix = size.height
    xyscale = min(nxpix, nypix) * factor
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

}