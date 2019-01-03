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

import cat.inspiracio.calculator.Direction._
import cat.inspiracio.complex._
import cat.inspiracio.parsing.Syntax
import javax.swing._

// Referenced classes of package bunkenba.calculator:
//            Calculator, DoubleBuffer, Drawing

/** Shows Re(f(x)) */
final class RefxWorld private[calculator](var calculator: Calculator) extends JFrame {

  private val MIN_SIZE = new Dimension(400, 300)
  private var AXISSPACE = 30
  private var AXISMARKING = 40
  private var FONTHEIGHT = 10
  private var TRIANGLESIZE = 5
  private var MARKLENGTH = 2

  protected var buttonPanel: JPanel = null
  private var canvas: RefxCanvas = null

  protected var prevx: Int = 0
  protected var prevy: Int = 0

  private var Max: Double = .0
  private var Min: Double = .0

  private var f: Syntax = null

  init()

  private def init()= {

    canvas = new RefxCanvas

    setTitle("Re(f(x)) World")

    resetExtremes()

    val button = new JButton("Zoom In")
    button.addActionListener( _ => zoomIn() )
    val button1 = new JButton("Zoom Out")
    button1.addActionListener( _ => zoomOut() )
    val button2 = new JButton("Reset")
    button2.addActionListener( _ => reset() )

    buttonPanel = new JPanel
    buttonPanel.setBackground(Color.lightGray)
    buttonPanel.setLayout(new FlowLayout(0))
    buttonPanel.add(button)
    buttonPanel.add(button1)
    buttonPanel.add(button2)

    setLayout(new BorderLayout)
    add("North", buttonPanel)
    add("Center", canvas)

    addWindowListener(new WindowAdapter() {
      override def windowClosing(windowevent: WindowEvent): Unit = calculator.quit()
    })

    pack()
    setLocationRelativeTo(calculator)
    setLocationByPlatform(true)
    setLocation()
    setVisible(true)
  }

  //XXX unit tests
  //XXX unify with method in Plane
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

  private def reset() = {
    canvas.reset()
    canvas.repaint()
  }

  private def zoomOut() = {
    canvas.zoomOut()
    canvas.repaint()
  }

  private def zoomIn() = {
    canvas.zoomIn()
    canvas.repaint()
  }

  def setLocation() = {
    setLocationRelativeTo(calculator)
    setLocationByPlatform(true)
  }

  /** Event listener: the function has changed. */
  private[calculator] def functionChanged(t: Syntax) = {
    f = t
    canvas.repaint()
  }

  protected def resetExtremes(): Unit = {
    Min = Double.PositiveInfinity
    Max = Double.NegativeInfinity
  }

  override def update(g: Graphics): Unit = paint(g)

  protected def updateExtremes(d: Double): Unit = {
    Max = Math.max(Max, d)
    Min = Math.min(Min, d)
  }

  override def setFont(font: Font): Unit = {
    super.setFont(font)
    canvas.setFont(font)
  }

  //Inner class --------------------------------------------------------

  private class RefxCanvas private[calculator]() extends JComponent {

    //State ----------------------------------------------------

    private var doubleBuffer: DoubleBuffer = null

    private var ScaleFactor: Double = 40

    private var CenterX = .0
    private var CenterY = .0

    private var Top = .0
    private var Left = .0
    private var Bottom = .0
    private var Right = .0

    private var upTriangle: Polygon = null
    private var downTriangle: Polygon = null

    init()

    private def init()= {
      setBackground(Color.white)

      doubleBuffer = new DoubleBuffer(this)

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

    //Methods ------------------------------------------------------

    //XXX make it return Point
    private def xy2Point(x: Double, y: Double, point: Point) = {
      point.x = ((x - Left) * ScaleFactor).toInt
      point.y = -((y - Top) * ScaleFactor).toInt
    }

    private def drawIt(drawing: Drawing) = {
      resetExtremes()

      var flag = false
      var i = 0
      val dimension = getSize()
      val height = dimension.height
      val width = dimension.width

      while ( i < width ) {
        try {
          val z: Complex = pix2x(i)
          val fz = f(z)
          var d = 0.0
          if (finite(fz))
            d = Re(fz)
          else
            throw new Exception
          updateExtremes(d)
          var j = 0
          if (d < Bottom)
            j = height
          else if (Top < d)
            j = -1
          else
            j = ((Top - d) * ScaleFactor).toInt
          if (flag)
            drawing.lineTo(i, j)
          else {
            drawing.moveTo(i, j)
            flag = true
          }
        } catch {
          case _ex: Exception =>
            flag = false
        }
        i = i+1
      }
    }

    override def getPreferredSize: Dimension = getMinimumSize
    override def getMinimumSize: Dimension = MIN_SIZE

    override def paint(g0: Graphics): Unit = {
      val g = doubleBuffer.offScreen(g0)
      val drawing = new Drawing(g)

      val dimension: Dimension = getSize()

      Top = CenterY + Pix2Math(dimension.height / 2)
      Bottom = CenterY - Pix2Math(dimension.height / 2)
      Left = CenterX - Pix2Math(dimension.width / 2)
      Right = CenterX + Pix2Math(dimension.width / 2)

      val point = new Point
      val point1 = new Point
      val point2 = new Point

      val d = raiseSmooth(Pix2Math(AXISMARKING))
      val l = Math2Pix(d)
      var d1 = 0.0D
      var d2 = 0.0D
      var d3 = Left + Pix2Math(AXISSPACE)
      val d4 = Right - Pix2Math(AXISSPACE)
      var d5 = Bottom + Pix2Math(AXISSPACE)
      val d6 = Top - Pix2Math(AXISSPACE)

      if (d3 <= 0.0D && d4 >= 0.0D) d1 = 0.0D
      else if (d3 > 0.0D) d1 = d3
      else if (d4 < 0.0D) d1 = d4
      if (d5 <= 0.0D && d6 >= 0.0D) d2 = 0.0D
      else if (d5 > 0.0D) d2 = d5
      else if (d6 < 0.0D) d2 = d6

      xy2Point(d1, d2, point2)
      xy2Point(d3, d2, point)
      xy2Point(d4, d2, point1)

      drawing.drawLine(point, point1, Color.lightGray)
      var polygon = drawing.mkTriangle(point1, EAST, TRIANGLESIZE)
      g.drawPolygon(polygon)
      polygon = drawing.mkTriangle(point, WEST, TRIANGLESIZE)
      g.drawPolygon(polygon)
      val j = point2.y
      var d7 = Math.ceil(d3 / d)
      d3 = d7 * d
      var i = x2Pix(d3)
      while ( d3 < d4 ) {
        drawing.moveTo(i, j)
        drawing.line(0, MARKLENGTH)
        val s: String = toString(d3)
        g.drawString(s, i + MARKLENGTH, j + FONTHEIGHT)
        i += l
        d3 += d
      }
      xy2Point(d1, d5, point)
      xy2Point(d1, d6, point1)
      drawing.drawLine(point, point1, Color.lightGray)
      upTriangle = drawing.mkTriangle(point1, NORTH, TRIANGLESIZE)
      g.drawPolygon(upTriangle)
      downTriangle = drawing.mkTriangle(point, SOUTH, TRIANGLESIZE)
      g.drawPolygon(downTriangle)
      i = point2.x
      d7 = Math.ceil(d5 / d)
      d5 = d7 * d
      var k = y2Pix(d5)
      while ( {
        d5 < d6
      }) {
        if (d5 != 0.0D || d1 != 0.0D) {
          val s = toString(d5)
          drawing.moveTo(i, k)
          drawing.line(-MARKLENGTH, 0)
          g.drawString(s, i - MARKLENGTH - g.getFontMetrics.stringWidth(s), k + FONTHEIGHT)
        }
        d5 += d
        k -= l
      }
      if (f != null) drawIt(drawing)
      if (Top <= Max) g.fillPolygon(upTriangle)
      if (Min <= Bottom) g.fillPolygon(downTriangle)
      doubleBuffer.onScreen()
    }

    private def Pix2Math(i: Int): Double = i.toDouble / ScaleFactor
    private def Math2Pix(d: Double): Int = (d * ScaleFactor).toInt
    private def x2Pix(x: Double): Int = ((x - Left) * ScaleFactor).toInt
    private def y2Pix(y: Double): Int = -((y - Top) * ScaleFactor).toInt
    private def pix2x(i: Int): Double = i.toDouble / ScaleFactor + Left

    def reset() = {
      CenterX = 0.0D
      CenterY = 0.0D
    }

    override def setFont(font: Font): Unit = {
      super.setFont(font)
      FONTHEIGHT = getFontMetrics(font).getAscent
      AXISSPACE = 3 * FONTHEIGHT
      AXISMARKING = 4 * FONTHEIGHT
      TRIANGLESIZE = FONTHEIGHT / 2
      MARKLENGTH = FONTHEIGHT / 5
    }

    private[calculator] def shift(x: Int, y: Int) = {
      CenterY -= Pix2Math(y)
      CenterX += Pix2Math(x)
    }

    def zoomIn() = ScaleFactor *= 2D
    def zoomOut() = ScaleFactor /= 2D

    // helpers -----------------------------------

    private def toString(d: Double): String = double2Complex(d).toString
  }

  // helpers -----------------------------------

  private def toString(d: Double): String = double2Complex(d).toString
}