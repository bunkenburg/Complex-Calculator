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
import java.util.prefs.Preferences

import javax.swing._
import cat.inspiracio.calculator.Direction._
import cat.inspiracio.complex._
import cat.inspiracio.geometry.{DoubleHelper, Point2}
import cat.inspiracio.parsing.Syntax
import javax.swing.event.MouseInputAdapter
import MoreGraphics.GraphicsExtended

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

  protected val buttonPanel = new JPanel
  private val canvas = new RefxCanvas

  private var Max: Double = .0
  private var Min: Double = .0

  private var f: Syntax = null

  init()

  private def init()= {
    setTitle("Re(f(x)) World")

    resetExtremes()

    val button = new JButton("Zoom In")
    button.addActionListener( _ => zoomIn() )
    val button1 = new JButton("Zoom Out")
    button1.addActionListener( _ => zoomOut() )
    val button2 = new JButton("Reset")
    button2.addActionListener( _ => reset() )

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
    locate()
    setVisible(true)
  }

  /** to the right of the calculator */
  private def locate() = {
    //calculator
    val calculatorDimension: Dimension = calculator.getSize // 319 x 328
    val calculatorPosition: Point = calculator.getLocationOnScreen  // 77 38

    val p = preferences
    val x = p.getInt("x", calculatorPosition.x + calculatorDimension.width + 10 )
    val y = p.getInt("y", calculatorPosition.y )
    setLocation( x, y )

    val width = p.getInt("width", 560)
    val height = p.getInt("height", 365)
    setSize(width,height)
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

  /** Event listener: the function has changed. */
  private[calculator] def functionChanged(t: Syntax) = {
    f = t
    canvas.repaint()
  }

  protected def resetExtremes(): Unit = {
    Min = Double.PositiveInfinity
    Max = Double.NegativeInfinity
  }

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

      val mouse = new MouseInputAdapter{

        //previous mouse position during dragging
        var previous: Point2 = null

        override def mousePressed(e: MouseEvent): Unit = previous = e.getPoint
        override def mouseReleased(e: MouseEvent): Unit = drag(e)
        override def mouseDragged(e: MouseEvent): Unit = drag(e)

        private def drag(e: MouseEvent) = {
          val p = e.getPoint
          shift(previous - p)
          repaint()
          previous = p
        }

      }

      addMouseListener(mouse)
      addMouseMotionListener(mouse)

      setDoubleBuffered(true)
    }

    //Methods ------------------------------------------------------

    //XXX make it return Point
    private def xy2Point(x: Double, y: Double, point: Point) = {
      point.x = ((x - Left) * ScaleFactor).toInt
      point.y = -((y - Top) * ScaleFactor).toInt
    }

    private def drawIt(g: Graphics) = {
      resetExtremes()

      var visible = false
      var i = 0
      val dimension = getSize()
      val height = dimension.height
      val width = dimension.width
      var a: Point = null //the last point that was drawn

      while ( i < width ) {
        try {
          val x: Complex = pix2x(i)
          val fx = f(x)
          var refx = 0.0
          if (finite(fx))
            refx = Re(fx)
          else
            throw new Exception
          updateExtremes(refx)
          var j = 0
          if (refx < Bottom)
            j = height
          else if (Top < refx)
            j = -1
          else
            j = ((Top - refx) * ScaleFactor).toInt
          if (visible) {
            //draw from p to (i, j)
            val b = Point2(i, j)
            g.drawLine( a, b )
            a = b
            //drawing.lineTo(i, j)
          }
          else {
            //drawing.moveTo(i, j)
            a = Point2(i, j)
            visible = true
          }
        } catch {
          case _ex: Exception =>
            visible = false
        }
        i = i+1
      }
    }

    override def getPreferredSize: Dimension = getMinimumSize
    override def getMinimumSize: Dimension = MIN_SIZE

    /** Paint with double-buffering.
      *
      * Invoked by Swing to draw components. Applications should not invoke
      * paint directly, but should instead use the repaint method to schedule
      * the component for redrawing. */
    override def paint(g: Graphics): Unit = {
      val dimension: Dimension = getSize()

      Top = CenterY + Pix2Math(dimension.height / 2)
      Bottom = CenterY - Pix2Math(dimension.height / 2)
      Left = CenterX - Pix2Math(dimension.width / 2)
      Right = CenterX + Pix2Math(dimension.width / 2)

      val point = new Point
      val point1 = new Point
      val point2 = new Point

      val d = DoubleHelper.raiseSmooth(Pix2Math(AXISMARKING))
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

      g.drawLine(point, point1, Color.lightGray)
      var polygon = g.mkTriangle(point1, EAST, TRIANGLESIZE)
      g.drawPolygon(polygon)
      polygon = g.mkTriangle(point, WEST, TRIANGLESIZE)
      g.drawPolygon(polygon)
      val j = point2.y
      var d7 = Math.ceil(d3 / d)
      d3 = d7 * d
      var i = x2Pix(d3)
      while ( d3 < d4 ) {
        val a = Point2(i, j)
        val b = a + (0, MARKLENGTH)
        g.drawLine(a, b)
        val s: String = toString(d3)
        g.drawString(s, i + MARKLENGTH, j + FONTHEIGHT)
        i += l
        d3 += d
      }
      xy2Point(d1, d5, point)
      xy2Point(d1, d6, point1)
      g.drawLine(point, point1, Color.lightGray)
      upTriangle = g.mkTriangle(point1, NORTH, TRIANGLESIZE)
      g.drawPolygon(upTriangle)
      downTriangle = g.mkTriangle(point, SOUTH, TRIANGLESIZE)
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
          val a = Point2(i, k)
          val b = a + (-MARKLENGTH, 0)
          g.drawLine(a, b)
          g.drawString(s, i - MARKLENGTH - g.getFontMetrics.stringWidth(s), k + FONTHEIGHT)
        }
        d5 += d
        k -= l
      }
      if (f != null) drawIt(g)
      if (Top <= Max) g.fillPolygon(upTriangle)
      if (Min <= Bottom) g.fillPolygon(downTriangle)
    }

    private def Pix2Math(i: Int): Double = i.toDouble / ScaleFactor
    private def Math2Pix(d: Double): Int = (d * ScaleFactor).toInt
    private def x2Pix(x: Double): Int = ((x - Left) * ScaleFactor).toInt
    private def y2Pix(y: Double): Int = -((y - Top) * ScaleFactor).toInt
    private def pix2x(i: Int): Double = i.toDouble / ScaleFactor + Left

    def reset() = {
      CenterX = 0.0
      CenterY = 0.0
    }

    override def setFont(font: Font): Unit = {
      super.setFont(font)
      FONTHEIGHT = getFontMetrics(font).getAscent
      AXISSPACE = 3 * FONTHEIGHT
      AXISMARKING = 4 * FONTHEIGHT
      TRIANGLESIZE = FONTHEIGHT / 2
      MARKLENGTH = FONTHEIGHT / 5
    }

    private[calculator] def shift(p: Point) = {
      CenterY -= Pix2Math(p.y)
      CenterX += Pix2Math(p.x)
    }

    def zoomIn() = ScaleFactor *= 2.0
    def zoomOut() = ScaleFactor /= 2.0

    // helpers -----------------------------------

    private def toString(d: Double): String = double2Complex(d).toString
  }

  // helpers -----------------------------------

  private def toString(d: Double): String = double2Complex(d).toString

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