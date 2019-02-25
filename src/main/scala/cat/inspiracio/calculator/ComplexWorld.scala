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

import java.awt.{Graphics, Point}
import java.awt.event._

import javax.swing._
import cat.inspiracio.calculator.Interaction._
import cat.inspiracio.complex._
import cat.inspiracio.geometry.Point2
import javax.swing.event.{MouseInputAdapter, MouseInputListener}

/** The complex world displays results of calculations
  * and allows graphical input of numbers.
  */
final class ComplexWorld private[calculator](val c: Calculator) extends World(c) {

  init()

  /** The numbers marked on this world. Never null. */
  private var numbers: List[Complex] = Nil

  private def init() {
    setTitle("Complex World")
    gui()

    val mouse: MouseInputListener = new MouseInputAdapter() {

      /** start and previous mouse position during moving */
      var startPoint: Point = null
      var previousPoint: Point2 = null

      override def mousePressed(e: MouseEvent): Unit = interaction match {
        case MOVE => {
          startPoint = e.getPoint
          previousPoint = e.getPoint
          canvas.startShift(startPoint)
        }
        case DRAW =>
          canvas.point2Complex(e.getPoint).foreach{ z =>
            calculator.add(z)
            add(z)
          }
        case _ =>
      }

      override def mouseDragged(e: MouseEvent): Unit = interaction match {
        case MOVE =>
          val p = e.getPoint
          canvas.shift(startPoint, previousPoint, p)
          previousPoint = p
        case _ =>
      }

      override def mouseReleased(e: MouseEvent): Unit = {
        interaction match {
          case MOVE =>
            val end = e.getPoint
            canvas.endShift(startPoint, end)
            startPoint = null
            previousPoint = null
          case _ =>
        }
      }

    }

    plane.addMouseListener(mouse)
    plane.addMouseMotionListener(mouse)
    sphere.addMouseListener(mouse)
    sphere.addMouseMotionListener(mouse)
    pack()
    applyPreferences()
    setVisible(true)
  }

  private def gui() = {
    import icon._

    val clearButton = new JButton(clearIcon)
    clearButton.addActionListener( _ => erase() )
    toolbar.add(clearButton)
    toolbar.addSeparator()

    val drawButton = new JToggleButton(drawIcon)
    drawButton.setToolTipText("Draw")
    drawButton.addActionListener( _ => interaction = Interaction.DRAW )

    val moveButton = new JToggleButton(handIcon)
    moveButton.setToolTipText("Move")
    moveButton.addActionListener( _ => interaction = Interaction.MOVE )

    val group = new ButtonGroup
    group.add(drawButton)
    group.add(moveButton)
    group.setSelected(drawButton.getModel, true)
    interaction = DRAW
    toolbar.add(drawButton)
    toolbar.add(moveButton)
  }

  private def applyPreferences() = {
    val p = preferences

    // to the right of the calculator
    val calculatorPosition = calculator.getLocationOnScreen
    val calculatorDimension = calculator.getSize
    val x = p.getInt("x", calculatorPosition.x + calculatorDimension.width + 10 )
    val y = p.getInt("y", calculatorPosition.y )
    setLocation( x, y )

    val width = p.getInt("width", 560)
    val height = p.getInt("height", 365)
    setSize(width,height)

    val c = p.get("canvas", "plane")
    c match {
      case "plane" => usePlane()
      case "sphere" => useSphere()
    }

    //could save zoom and shift and numbers too
  }

  override private[calculator] def add(c: Complex) = {
    numbers = c :: numbers
    updateExtremes(c)
    canvas.repaint()
  }

  override private[calculator] def draw(g: Graphics) =
    numbers.foreach{ canvas.draw(g, _) }

  override private[calculator] def erase() = {
    numbers = Nil
    resetExtremes()
    canvas.repaint()
  }
}