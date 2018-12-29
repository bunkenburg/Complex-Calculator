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

import cat.inspiracio.complex._
import cat.inspiracio.geometry
import cat.inspiracio.geometry.{Matrix44, Vector2}
import cat.inspiracio.numbers.Square
import cat.inspiracio.parsing.SyntaxTree
import javax.swing._

// Referenced classes of package bunkenba.calculator:
//            Calculator, DoubleBuffer, Drawing, Matrix44,
//            Vector2, Vector3

final class ThreeDWorld private[calculator](var calculator: Calculator) extends JFrame("|f(z)| World") {

  private val MIN_SIZE = new Dimension(400, 300)

  //State ---------------------------------------------

  private[calculator] val XYFACTOR = 0.6D
  private[calculator] val n = 10

  private var canvas: ThreeDCanvas = null
  private var square: Square = null
  private var f: SyntaxTree = null

  //XXX improve
  private[calculator] var M: Array[Array[Double]] = null

  init()

  private def init()= {
    square = new Square(0, 1)
    square = calculator.getSquare

    M = new Array[Array[Double]](21)
    var i = 0
    while ( i < 21 ) {
      M(i) = new Array[Double](21)
      i = i+1
    }

    canvas = new ThreeDCanvas
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

  private[calculator] def functionChange(syntaxtree: SyntaxTree) = {
    f = syntaxtree
    setNeighbourhood()
    canvas.repaint()
  }

  private[calculator] def setNeighbourhood(): Unit = {
    if (f == null) {
      var i1 = -10
      while ( i1 <= 10 ) {
        var i = -10
        while ( i <= 10 ) {
          M(10 + i1)(10 + i) = 0.0
          i = 1+1
        }
        i1 = i1+1
      }
      return
    }

    val step: Double = square.getSide / 2.0 / 10.0
    val center: Complex = square.getCenter
    var j1 = -10
    while ( j1 <= 10 ) {
      var j = -10
      while ( j <= 10 ) {
        val z: Complex = Cartesian(step * j1, step * j)
        try {
          val fz = f.evaluate( center + z )
          M(10 + j1)(10 + j) = abs(fz)
        } catch {
          case _ex: Exception =>
            //XXX Why -0.2 ?
            M(10 + j1)(10 + j) = -0.20000000000000001D
        }
        j = j+1
      }
      j1 = j1+1
    }

    var d = 0.0D
    var k1 = -10
    while ( k1 <= 10 ) {
      var k = -10
      while ( k <= 10 ) {
        val x = M(10 + k1)(10 + k)
        if (! x.isInfinite() )
          d = Math.max(d, M(10 + k1)(10 + k))
          k += 1
      }
      k1 = k1+1
    }

    if (d < 0.0001D)
      d = 1.0D
    var l1 = -10
    while ( l1 <= 10 ) {
      var l = -10
      while ( l <= 10 ) {
        val x = M(10 + l1)(10 + l)
        if ( x.isInfinite() )
          M(10 + l1)(10 + l) = 1.2D
        else if (M(10 + l1)(10 + l) < 0.0D)
          M(10 + l1)(10 + l) = -0.20000000000000001D
        else
          M(10 + l1)(10 + l) = M(10 + l1)(10 + l) / d
        l += 1
      }
      l1 = l1+1
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
    private var FONT_HEIGHT = 0
    private var prevx = 0
    private var prevy = 0

    /** What are these? */
    private[calculator] var vs: Array[Array[Vector2]] = null

    private[calculator] var eye: Vector3 = null
    private[calculator] var direct: Vector3 = null
    private[calculator] var nxpix = 0
    private[calculator] var nypix = 0
    private var Q: Matrix44 = null
    private[calculator] var xforward = false
    private[calculator] var zforward = false
    private[calculator] var xyscale = .0

    init()

    //Constructor ---------------------------------------------------

    private def init()= {
      FONT_HEIGHT = 12

      vs = new Array[Array[Vector2]](2)
      vs(0) = new Array[Vector2](21)
      vs(1) = new Array[Vector2](21)
      var i = 0
      while ( i < 21 ) {
        vs(0)(i) = Vector2(0,0)
        vs(1)(i) = Vector2(0,0)
        i = i+1
      }

      eye = new Vector3(3D, 0.5D, 1.0D)
      direct = new Vector3(2.5D, 0.5D, 0.5D)
      Q = Matrix44.zero
      setBackground(Color.white)

      addMouseListener(new MouseAdapter() {

        override def mousePressed(mouseevent: MouseEvent): Unit = {
          prevx = mouseevent.getX
          prevy = mouseevent.getY
          mouseevent.consume()
        }

        override def mouseReleased(mouseevent: MouseEvent): Unit = {
          val j = mouseevent.getX
          val k = mouseevent.getY
          shift(prevx - j, prevy - k)
          paint(getGraphics)
          prevx = j
          prevy = k
          mouseevent.consume()
        }

      })

      addMouseMotionListener(new MouseMotionAdapter() {

        override def mouseDragged(mouseevent: MouseEvent): Unit = {
          val j = mouseevent.getX
          val k = mouseevent.getY
          shift(prevx - j, prevy - k)
          paint(getGraphics)
          prevx = j
          prevy = k
          mouseevent.consume()
        }

      })

      initQ()
    }


    //Methods -------------------------------------------------------

    private[calculator] def drawBackAxes(drawing: Drawing, flag: Boolean, flag1: Boolean) = {

      var s = square.botLeft.toString
      moveTo3(drawing, -0.5D, 0.0D, -0.5D)
      if (flag && flag1) drawing.move(2, -2)
      else if (flag && !flag1) drawing.move(-drawing.graphics.getFontMetrics.stringWidth(s) - 2, FONT_HEIGHT)
      else if (!flag && flag1) drawing.move(2, FONT_HEIGHT)
      else if (!flag && !flag1) drawing.move(2, FONT_HEIGHT)
      drawing.drawString(s)
      moveTo3(drawing, 0.5D, 0.0D, -0.5D)

      s = square.botRight.toString
      if (flag && flag1) drawing.move(-drawing.graphics.getFontMetrics.stringWidth(s) - 2, FONT_HEIGHT)
      else if (flag && !flag1) drawing.move(2, FONT_HEIGHT)
      else if (!flag && flag1) drawing.move(2, -2)
      else if (!flag && !flag1) drawing.move(2, FONT_HEIGHT)
      drawing.drawString(square.botRight.toString)
      moveTo3(drawing, -0.5D, 0.0D, 0.5D)

      s = square.topLeft.toString
      if (flag && flag1) drawing.move(2, FONT_HEIGHT)
      else if (flag && !flag1) drawing.move(2, -2)
      else if (!flag && flag1) drawing.move(2, FONT_HEIGHT)
      else if (!flag && !flag1) drawing.move(-drawing.graphics.getFontMetrics.stringWidth(s) - 2, FONT_HEIGHT)
      drawing.drawString(s)
      moveTo3(drawing, 0.5D, 0.0D, 0.5D)

      s = square.topRight.toString
      if (flag && flag1) drawing.move(2, FONT_HEIGHT)
      else if (flag && !flag1) drawing.move(2, FONT_HEIGHT)
      else if (!flag && flag1) drawing.move(-drawing.graphics.getFontMetrics.stringWidth(s) - 2, FONT_HEIGHT)
      else if (!flag && !flag1) drawing.move(2, -2)
      drawing.drawString(s)

      if (flag) drawLine3(drawing, -0.5D, 0.0D, -0.5D, -0.5D, 0.0D, 0.5D)
      else drawLine3(drawing, 0.5D, 0.0D, -0.5D, 0.5D, 0.0D, 0.5D)

      if (flag1) drawLine3(drawing, -0.5D, 0.0D, -0.5D, 0.5D, 0.0D, -0.5D)
      else drawLine3(drawing, -0.5D, 0.0D, 0.5D, 0.5D, 0.0D, 0.5D)

      if (!flag || !flag1) drawLine3(drawing, 0.5D, 0.0D, 0.5D, 0.5D, 1.0D, 0.5D)
      if (!flag || flag1) drawLine3(drawing, 0.5D, 0.0D, -0.5D, 0.5D, 1.0D, -0.5D)
      if (flag || !flag1) drawLine3(drawing, -0.5D, 0.0D, 0.5D, -0.5D, 1.0D, 0.5D)
      if (flag || flag1) drawLine3(drawing, -0.5D, 0.0D, -0.5D, -0.5D, 1.0D, -0.5D)

      if (flag) drawLine3(drawing, -0.5D, 1.0D, -0.5D, -0.5D, 1.0D, 0.5D)
      else drawLine3(drawing, 0.5D, 1.0D, -0.5D, 0.5D, 1.0D, 0.5D)

      if (flag1)
        drawLine3(drawing, -0.5D, 1.0D, -0.5D, 0.5D, 1.0D, -0.5D)
      else
        drawLine3(drawing, -0.5D, 1.0D, 0.5D, 0.5D, 1.0D, 0.5D)
    }

    private[calculator] def drawFrontAxes(drawing: Drawing, flag: Boolean, flag1: Boolean) = {
      if (flag)
        drawLine3(drawing, 0.5D, 0.0D, -0.5D, 0.5D, 0.0D, 0.5D)
      else
        drawLine3(drawing, -0.5D, 0.0D, -0.5D, -0.5D, 0.0D, 0.5D)
      if (flag1)
        drawLine3(drawing, -0.5D, 0.0D, 0.5D, 0.5D, 0.0D, 0.5D)
      else
        drawLine3(drawing, -0.5D, 0.0D, -0.5D, 0.5D, 0.0D, -0.5D)
      if (flag && flag1)
        drawLine3(drawing, 0.5D, 0.0D, 0.5D, 0.5D, 1.0D, 0.5D)
      else if (flag && !flag1)
        drawLine3(drawing, 0.5D, 0.0D, -0.5D, 0.5D, 1.0D, -0.5D)
      else if (!flag && flag1)
        drawLine3(drawing, -0.5D, 0.0D, 0.5D, -0.5D, 1.0D, 0.5D)
      else if (!flag && !flag1)
        drawLine3(drawing, -0.5D, 0.0D, -0.5D, -0.5D, 1.0D, -0.5D)
      if (flag)
        drawLine3(drawing, 0.5D, 1.0D, -0.5D, 0.5D, 1.0D, 0.5D)
      else
        drawLine3(drawing, -0.5D, 1.0D, -0.5D, -0.5D, 1.0D, 0.5D)

      if (flag1)
        drawLine3(drawing, -0.5D, 1.0D, 0.5D, 0.5D, 1.0D, 0.5D)
      else
        drawLine3(drawing, -0.5D, 1.0D, -0.5D, 0.5D, 1.0D, -0.5D)
    }

    private[calculator] def drawImaginaryAxis(drawing: Drawing) = drawLine3(drawing, 0.0D, 0.0D, -0.5D, 0.0D, 0.0D, 0.5D)

    private[calculator] def drawModAxis(drawing: Drawing) = drawLine3(drawing, 0.0D, 0.0D, 0.0D, 0.0D, 1.0D, 0.0D)

    private[calculator] def drawRealAxis(drawing: Drawing) = drawLine3(drawing, -0.5D, 0.0D, 0.0D, 0.5D, 0.0D, 0.0D)

    private[calculator] def drawLine3(drawing: Drawing, d: Double, d1: Double, d2: Double, d3: Double, d4: Double, d5: Double) = {
      moveTo3(drawing, d, d1, d2)
      lineTo3(drawing, d3, d4, d5)
    }

    private[calculator] def cx(i: Int) = if (xforward) i else -i

    private[calculator] def cz(i: Int) = if (zforward) i else -i

    private[calculator] def drawIt(drawing: Drawing) = {

      val v2 = f3d2d(new Vector3(0, 0, 0))

      val vector2_1 = f3d2d(new Vector3(1, 0, 0))
      xforward = v2.y >= vector2_1.y

      val vector2_2 = f3d2d(new Vector3(0, 0, 1))
      zforward = v2.y >= vector2_2.y

      drawBackAxes(drawing, xforward, zforward)

      var d2 = -0.5D
      var d3 = 0.5D
      //byte byte0 = 20;
      if (!xforward) {
        val d11 = d2
        d2 = d3
        d3 = d11
      }
      //int j1 = byte0 + 1;
      val d4 = (d3 - d2) / 20D
      var d8 = -0.5D
      var d9 = 0.5D
      //byte byte1 = 20;
      if (!zforward) {
        val d12 = d8
        d8 = d9
        d9 = d12
      }
      val d10 = (d9 - d8) / 20D
      var d = d2
      var d7 = d8

      var i = -10
      while ( i <= 10 ) {
        val d5 = M(10 + cx(i))(10 + cz(-10))

        vs(0)(10 + i) = Vector2(
          Q(0,0) * d + Q(0,1) * d5 + Q(0,2) * d7,
          Q(1,0) * d + Q(1,1) * d5 + Q(1,2) * d7
        )

        d += d4
        i = i+1
      }

      var i1 = -9
      while ( i1 <= 10 ) {
        if (i1 == 1) drawRealAxis(drawing)
        //boolean flag = true;
        var d1 = d2
        d7 += d10
        var j = -10
        while ( j <= 10 ) {
          val d6 = M(10 + cx(j))(10 + cz(i1))

          vs(1)(10 + j) = Vector2(
            Q(0,0) * d1 + Q(0,1) * d6 + Q(0,2) * d7,
            Q(1,0) * d1 + Q(1,1) * d6 + Q(1,2) * d7
          )

          d1 += d4
          j = j+1
        }

        var k = -10
        while ( k <= 9 ) {
          if (k == 0 && i1 == 0) {
            drawModAxis(drawing)
            drawImaginaryAxis(drawing)
          }
          patch(drawing, vs(0)(10 + k), vs(0)(10 + k + 1), vs(1)(10 + k), vs(1)(10 + k + 1))
          k += 1
        }

        var l = -10
        while ( l <= 10 ) {

          vs(0)(10 + l) = Vector2(
            vs(1)(10 + l).x,
            vs(1)(10 + l).y
          )

          l = l+1
        }
        i1 = i1+1
      }
      drawFrontAxes(drawing, xforward, zforward)
    }

    private def f3d2d(v: Vector3): Vector2 =
      Vector2(
        Q(0,0) * v.x + Q(0,1) * v.y + Q(0,2) * v.z,
        Q(1,0) * v.x + Q(1,1) * v.y + Q(1,2) * v.z
      )

    private def f3dPix(d: Double, d1: Double, d2: Double): Point =
      new Point(
        fx(Q(0,0) * d + Q(0,1) * d1 + Q(0,2) * d2),
        fy(Q(1,0) * d + Q(1,1) * d1 + Q(1,2) * d2)
      )

    private[calculator] def angle(d: Double, d1: Double): Double = Math.atan2(d1, d)

    private[calculator] def fx(d: Double): Int = (d * xyscale + nxpix * 0.5D).toInt

    private[calculator] def fy(d: Double): Int = (-d * xyscale + nypix * 0.80000000000000004D).toInt

    override def getPreferredSize: Dimension = getMinimumSize
    override def getMinimumSize: Dimension = MIN_SIZE

    private[calculator] def initQ() = {
      Q = Matrix44.translation(eye)
      val d = angle(-direct.x, -direct.y)
      Q = Q.preRot('z', d)
      val d3 = Math.sqrt(direct.x * direct.x + direct.y * direct.y)
      val d1 = angle(-direct.z, d3)
      Q = Q.preRot('y', d1)
      val d4 = Math.sqrt(d3 * d3 + direct.z * direct.z)
      val d2 = angle(-direct.x * d4, direct.y * direct.z)
      Q = Q.preRot('z', -d2)
      Q = Q.postRot('y', π/2)
    }

    private[calculator] def lineTo3(drawing: Drawing, d: Double, d1: Double, d2: Double) =
      drawing.lineTo(f3dPix(d, d1, d2 ))

    private[calculator] def moveTo3(drawing: Drawing, d: Double, d1: Double, d2: Double) =
      drawing.moveTo(f3dPix(d, d1, d2 ))

    override def paint(g0: Graphics): Unit = {
      val size: Dimension = getSize
      val g = doubleBuffer.offScreen(g0)
      val drawing = new Drawing(g)
      nxpix = size.width
      nypix = size.height
      xyscale = Math.min(nxpix, nypix) * XYFACTOR
      drawIt(drawing)
      doubleBuffer.onScreen()
    }

    private[calculator] def patch(drawing: Drawing, a: Vector2, b: Vector2, c: Vector2, d: Vector2): Unit = {

      var something = (b.x - a.x) * (d.y - c.y) - (b.y - a.y) * (d.x - c.x)

      if (Math.abs(something) > 0.0001D) {
        val d1 = ((c.x - a.x) * (d.y - c.y) - (c.y - a.y) * (d.x - c.x)) / something
        if (d1 >= 0.0D && d1 <= 1.0D) {
          val v5 = Vector2(
            (1 - d1) * a.x + d1 * b.x,
            (1 - d1) * a.y + d1 * b.y
          )
          triangle(drawing, a, c, v5)
          triangle(drawing, b, d, v5)
          return
        }
        something = (c.x - a.x) * (d.y - b.y) - (c.y - a.y) * (d.x - b.x)
        if (Math.abs(something) > 0.0001D) {
          val d2 = ((b.x - a.x) * (d.y - b.y) - (b.y - a.y) * (d.x - b.x)) / something
          if (d2 >= 0.0D && d2 <= 1.0D) {
            val v5 = Vector2(
              (1 - d2) * a.x + d2 * c.x,
              (1.0D - d2) * a.y + d2 * c.y
            )
            triangle(drawing, a, b, v5)
            triangle(drawing, c, d, v5)
            return
          }
          else {
            quadrilateral(drawing, a, b, d, c)
            return
          }
        }
        else {
          quadrilateral(drawing, a, b, d, c)
          return
        }
      }

      something = (c.x - a.x) * (d.y - b.y) - (c.y - a.y) * (d.x - b.x)
      if (Math.abs(something) > 0.0001D) {
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

    private[calculator] def shift(i: Int, j: Int) = {
      val size: Dimension = getSize
      val d = i * 2 * π / size.height
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