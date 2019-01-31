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

import java.awt.{Dimension, Graphics, GraphicsConfiguration, Point}
import java.awt.event._

import javax.swing._
import cat.inspiracio.calculator.Interaction._
import cat.inspiracio.calculator.Mode.{FZ, MODFZ}
import cat.inspiracio.complex._
import cat.inspiracio.geometry._
import javax.swing.event.{MouseInputAdapter, MouseInputListener}
import Helpers.MoreGraphics

/** The z-World is where the user gives input for function f. */
final class ZWorld private[calculator](override val calculator: Calculator) extends World(calculator) {

  //State ------------------------------------------------

  private var interactionChoice: JComboBox[String] = new JComboBox[String]
  private var eraseButton: JButton = new JButton("Clear")

  //XXX access these from calculator
  private var mode: Mode = null
  private var fzW: FzWorld = null
  private var modfzW: ThreeDWorld = null

  // XXX consolidate these into
  // piclets: the finished piclets
  // current: the piclet that is just now being drawn dynamically.

  /** During dragging of a free line, the points, otherwise null. */
  //XXX consolidate to current
  private var zs: List[Complex] = null

  /** During dragging, the provisional piclet. */
  private var currentPiclet: Piclet = null

  private var piclets: List[Piclet] = Nil

  //XXX consolidate to current
  private var square = Square(0, 1+i)

  init()

  // method ----------------------------------------------

  private def init()= {

    setTitle("z World")

    eraseButton.addActionListener((e: ActionEvent) => erase())
    buttonPanel.add(eraseButton)

    interaction = DRAW
    interactionChoice.addItem("Move")
    interactionChoice.addItem("Square")
    interactionChoice.addItemListener( e => {
      val state = e.getStateChange
      if (state == ItemEvent.SELECTED) {
        val s = e.getItem.asInstanceOf[String]
        interaction = Interaction.parse(s)
      }
    })
    buttonPanel.add(interactionChoice)

    val mouse = new MouseInputAdapter() {

      // during dragging, previous mouse position
      var previous: Point2 = null

      //during dragging, starting and ending complex number
      var start: Complex = null
      var end: Complex = null

      /** starts dynamic mapping of a piclet */
      def startCurrent(p: Point) = {
        val z = canvas.point2Complex(p)
        if (z != null) {
          start = z
          end = z
          val current = interaction match {
            case GRID => Rectangle(start, end)
            case LINE => Line(start, end)
            case CIRCLE => Circle(start, end)
            case RECTANGLE => Rectangle(start, end)
            case SQUARE => Square(start, end)
          }
          if (mode == FZ)
            addCurrent(current)
          //XXX better?
          else if (mode == MODFZ && interaction==SQUARE)
            modfzW.change(current.asInstanceOf[Square])
          addCurrent(current)
          canvas.repaint()
        }
      }

      override def mousePressed(e: MouseEvent): Unit = interaction match {

        case MOVE => previous = e.getPoint

        case DRAW => {
          //XXX move this into startCurrent
          val z = canvas.point2Complex(e.getPoint)
          if (z != null) {
            Complex.resetArg()
            add(z)
            canvas.repaint()
          }
        }

        case GRID => startCurrent(e.getPoint)
        case LINE => startCurrent(e.getPoint)
        case CIRCLE => startCurrent(e.getPoint)
        case RECTANGLE => startCurrent(e.getPoint)
        case SQUARE => startCurrent(e.getPoint)

      }//match

      private def drag(e: MouseEvent) = {
        val p = e.getPoint
        canvas.shift(previous - p)
        previous = p
      }

      override def mouseDragged(e: MouseEvent): Unit = interaction match {

        case MOVE => drag(e)

        case DRAW => {
          //XXX consolidate these into continueCurrent
          val z = canvas.point2Complex(e.getPoint)
          if (zs != null) {
            if (z != null)
              add(z)
            else {
              piclets = Curve(zs) :: piclets
              zs = null
              fzW.stopDynamicMap()
            }
          }
          else if (z != null) {
            Complex.resetArg()
            add(z)
          }
          canvas.repaint()
        }

        case CIRCLE => {
          //XXX consolidate these into continueCurrent
          val z = canvas.point2Complex(e.getPoint)
          if (z != null) {
            end = z
            addCurrent(Circle(start, end))
            canvas.repaint()
          }
        }

        case GRID => {
          //XXX consolidate these into continueCurrent
          val z = canvas.point2Complex(e.getPoint)
          if (z != null) {
            end = z
            addCurrent(Rectangle(start, end))
            canvas.repaint()
          }
        }

        case LINE => {
          //XXX consolidate these into continueCurrent
          val z = canvas.point2Complex(e.getPoint)
          if (z != null) {
            end = z
            addCurrent(Line(start, end))
            canvas.repaint()
          }
        }

        case RECTANGLE => {
          //XXX consolidate these into continueCurrent
          val z = canvas.point2Complex(e.getPoint)
          if (z != null) {
            end = z
            addCurrent(Rectangle(start, end))
            canvas.repaint()
          }
        }

        case SQUARE => {
          //XXX consolidate these into continueCurrent
          val z = canvas.point2Complex(e.getPoint)
          if (z != null) {
            end = z
            square = Square(start, end)
            if (mode == FZ)
              addCurrent(square)
            else if (mode == MODFZ)
              modfzW.change(square)
            canvas.repaint()
          }
        }

      }//match

      override def mouseReleased(e: MouseEvent): Unit = interaction match {

        case MOVE => drag(e)

        case DRAW => {
          //XXX consolidate these into finishCurrent
          if (zs != null) {
            val z = canvas.point2Complex(e.getPoint)
            if (z != null)
              add(z)
            piclets = Curve(zs) :: piclets
            zs = null   //mouse released => finish free line
            canvas.repaint()
            fzW.stopDynamicMap()
          }
        }

        case LINE =>
          //XXX consolidate these into finishCurrent
          if (start != null) {
            val z = canvas.point2Complex(e.getPoint)
            if (z != null)
              end = z
            add(Line(start, end))
            canvas.repaint()
          }
          eraseCurrent()

        case CIRCLE => {
          //XXX consolidate these into finishCurrent
          if (start != null) {
            val z = canvas.point2Complex(e.getPoint)
            if (z != null)
              end = z
            add(Circle(start, end))
            canvas.repaint()
          }
          eraseCurrent()
        }

        case RECTANGLE =>
          //XXX consolidate these into finishCurrent
          if (start != null) {
            val z = canvas.point2Complex(e.getPoint)
            if (z != null)
              end = z
            add(Rectangle(start, end))
            canvas.repaint()
          }
          eraseCurrent()

        case SQUARE =>
          //XXX consolidate these into finishCurrent
          if (start != null) {
            val z = canvas.point2Complex(e.getPoint)
            if (z != null)
              end = z
            square = Square(start, end)
            if (mode == FZ)
              add(square)
            else if (mode == MODFZ)
              modfzW.change(square)
            updateExtremes(square)
            canvas.repaint()
          }
          eraseCurrent()

        case GRID =>
          //XXX consolidate these into finishCurrent
          if (start != null) {
            val z = canvas.point2Complex(e.getPoint)
            if (z != null)
              end = z
            addGrid(Rectangle(start, end))
            canvas.repaint()
          }
          eraseCurrent()

      }//match

    }//MouseInputAdapter

    plane.addMouseListener(mouse)
    plane.addMouseMotionListener(mouse)
    sphere.addMouseListener(mouse)
    sphere.addMouseMotionListener(mouse)

    pack()
    locate()
    setVisible(true)

  }//init

  /** below the calculator */
  private def locate() = {
    val calculatorDimension: Dimension = calculator.getSize // 319 x 328
    val calculatorPosition: Point = calculator.getLocationOnScreen  // 77 38

    val p = preferences
    val x = p.getInt("x", calculatorPosition.x )
    val y = p.getInt("y", calculatorPosition.y + calculatorDimension.height + 10 )
    setLocation( x, y )

    val width = p.getInt("width", 560)
    val height = p.getInt("height", 365)
    setSize(width,height)
  }

  /** during dynamic map, adds one more number */
  override private[calculator] def add(c: Complex) = {
    updateExtremes(c)
    if( zs == null )
      zs = Nil
    zs = c :: zs
    fzW.add(c)
  }

  private[calculator] def add(p: Piclet) = {
    updateExtremes(p)
    piclets = p :: piclets
    fzW.add(p)
  }

  /** during dragging, the current piclet */
  private[calculator] def addCurrent(piclet: Piclet) = {
    currentPiclet = piclet
    fzW.addCurrent(piclet)
  }

  private[calculator] def addGrid(r: Rectangle) = {
    val N = 10

    val hStep = r.width / N
    val vStep = r.height / N

    for ( i <- 0 to N ) {
      val re = r.left + i*hStep
      add(Line(Cartesian(re, r.top), Cartesian(re, r.bottom)))
      val im = r.bottom + i*vStep
      add(Line(Cartesian(r.left, im), Cartesian(r.right, im)))
    }
  }

  override private[calculator] def draw(g: Graphics) = {
    if (zs != null)
      canvas.draw(g, zs)

    if (currentPiclet != null)
      canvas.draw(g, currentPiclet)

    piclets.foreach{
      canvas.draw(g, _)
    }

    if (mode == MODFZ)
      canvas.draw(g, square)
  }

  override private[calculator] def erase(): Unit = {
    eraseCurrent()

    piclets = Nil

    resetExtremes()
    if (mode == MODFZ) {
      updateExtremes(square)
      return
    }

    if (fzW != null)
      fzW.erase()
    canvas.repaint()
  }

  private[calculator] def eraseCurrent() = {
    zs = null
    currentPiclet = null
  }

  private[calculator] def getPiclets = piclets
  private[calculator] def getSquare = square
  private[calculator] def setfzWorld(w: FzWorld) = fzW = w
  private[calculator] def setmodfzWorld(w: ThreeDWorld) = modfzW = w

  private[calculator] def setMode(m: Mode) = {
    mode = m
    mode match {

      case FZ =>
        eraseButton.setEnabled(true)
        interactionChoice.addItem("Draw")
        interactionChoice.addItem("Circle")
        interactionChoice.addItem("Line")
        interactionChoice.addItem("Grid")
        interactionChoice.addItem("Rectangle")
        interactionChoice.setSelectedItem("Draw")
        interaction = DRAW

      case MODFZ =>
        eraseButton.setEnabled(false)
        interactionChoice.removeItem("Draw")
        interactionChoice.removeItem("Circle")
        interactionChoice.removeItem("Line")
        interactionChoice.removeItem("Grid")
        interactionChoice.removeItem("Rectangle")
        interactionChoice.setSelectedItem("Square")
        interaction = SQUARE

      case _ =>
    }
    erase()
    canvas.repaint()
  }

}