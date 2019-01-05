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

import java.awt.event.{ItemEvent, MouseAdapter, MouseEvent, MouseMotionAdapter}
import javax.swing._
import cat.inspiracio.calculator.Interaction._
import cat.inspiracio.complex._

/** The complex world displays results of calculations
  * and allows graphical input of numbers.
  */
final class ComplexWorld private[calculator](val c: Calculator) extends World(c) {

  init()

  private def init() {
    setTitle("Complex World")

    //GUI ---

    val button = new JButton("Clear")
    button.addActionListener( _ => clear() )
    buttonPanel.add(button)
    val interactionChoice = new JComboBox[String]
    interactionChoice.addItem("Draw")
    interactionChoice.addItem("Move")
    interactionChoice.setSelectedItem("Draw")
    interaction = DRAW
    interactionChoice.addItemListener( e => {
      val state = e.getStateChange
      if (state == ItemEvent.SELECTED) {
        val s = e.getItem.toString
        interaction = Interaction.parse(s)
      }
    })
    buttonPanel.add(interactionChoice)

    val mouse: MouseAdapter = new MouseAdapter() {

      override def mousePressed(mouseevent: MouseEvent): Unit = interaction match {

        case MOVE =>
          prevx = mouseevent.getX
          prevy = mouseevent.getY
          mouseevent.consume()

        case DRAW =>
          val p = mouseevent.getPoint
          val z = canvas.point2Complex(p)
          if (z != null) {
            calculator.add(z)
            add(z)
          }

        case _ =>
      }

      override def mouseReleased(mouseevent: MouseEvent): Unit = interaction match {

        case MOVE =>
          val x = mouseevent.getX
          val y = mouseevent.getY
          canvas.shift(prevx - x, prevy - y)
          canvas.paint(canvas.getGraphics)
          prevx = x
          prevy = y
          mouseevent.consume()

        case _ =>
      }
    }

    val motion: MouseMotionAdapter = new MouseMotionAdapter() {

      override def mouseDragged(e: MouseEvent): Unit = interaction match {

        case MOVE =>
          val x = e.getX
          val y = e.getY
          canvas.shift(prevx - x, prevy - y)
          canvas.paint(canvas.getGraphics)
          prevx = x
          prevy = y
          e.consume()

        case _ =>
      }
    }

    plane.addMouseListener(mouse)
    plane.addMouseMotionListener(motion)
    sphere.addMouseListener(mouse)
    sphere.addMouseMotionListener(motion)
    pack()
    locate()
    setVisible(true)
  }

  private var numbers: List[Complex] = Nil

  /** to the right of the calculator */
  private def locate() = {
    val calculatorPosition = calculator.getLocationOnScreen
    val calculatorDimension = calculator.getSize

    val p = preferences
    val x = p.getInt("x", calculatorPosition.x + calculatorDimension.width + 10 )
    val y = p.getInt("y", calculatorPosition.y )
    setLocation( x, y )

    //val size = getSize  //width=560 height=365
    val width = p.getInt("width", 560)
    val height = p.getInt("height", 365)
    setSize(width,height)
  }

  private def clear() = {
    erase()
    canvas.repaint()
  }

  override private[calculator] def add(c: Complex) = {
    numbers = c :: numbers
    updateExtremes(c)
    canvas.repaint()
  }

  override private[calculator] def drawStuff(drawing: Drawing) =
    if(numbers!=null) numbers.foreach{ canvas.draw(drawing, _) }

  override private[calculator] def erase() = {
    numbers = Nil
    resetExtremes()
  }
}