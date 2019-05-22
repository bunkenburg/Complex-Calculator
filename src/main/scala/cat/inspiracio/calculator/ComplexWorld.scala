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
 * */
package cat.inspiracio.calculator

import java.awt.{Dimension, Graphics, Point}

import scala.swing._
import cat.inspiracio.calculator.Interaction._
import cat.inspiracio.complex._
import cat.inspiracio.geometry.Point2
import Point2._

/** The complex world displays results of calculations
  * and allows graphical input of numbers.
  */
final class ComplexWorld private[calculator](calculator: Calculator) extends World(calculator) {

  init()

  /** The numbers marked on this world. Never null. */
  private var numbers: List[Complex] = Nil

  private def init() {
    title = "Complex numbers"
    gui()
    pack()
    visible = true
    applyPreferences()
  }

  private def gui() = {
    import icon._

    val clearButton = button(clearIcon, "Clear", { erase() } )
    toolbar.contents += clearButton
    toolbar.peer.addSeparator()

    val drawButton = toggle(drawIcon, "Draw", { interaction = Interaction.DRAW } )
    val moveButton = toggle(handIcon, "Move", { interaction = Interaction.MOVE } )

    val group = new ButtonGroup
    group.buttons += drawButton
    group.buttons += moveButton
    group.select(drawButton)
    interaction = DRAW
    toolbar.contents += drawButton
    toolbar.contents += moveButton
  }

  private def applyPreferences(): Unit = {
    val p = preferences

    //Exception in thread "main" java.awt.IllegalComponentStateException: component must be showing on the screen to determine its location
    if(!visible)
      return ()

    // to the right of the calculator
    val calculatorPosition = calculator.locationOnScreen
    val calculatorDimension = calculator.size
    val x = p.getInt("x", calculatorPosition.x + calculatorDimension.width + 10 )
    val y = p.getInt("y", calculatorPosition.y )
    location = ( x, y )

    val width = p.getInt("width", 560)
    val height = p.getInt("height", 365)
    size = new Dimension(width,height)

    val c = p.get("canvas", "plane")
    c match {
      case "plane" => usePlane()
      case "sphere" => useSphere()
    }
    canvas.zoom = p.getDouble("zoom", 1)
  }

  override private[calculator] def add(c: Complex) = {
    numbers = c :: numbers
    updateExtremes(c)
    canvas.repaint()
  }

  override private[calculator] def draw(g: Graphics) =
    if(canvas!=null && numbers!=null)
      numbers.foreach{
        canvas.draw(g, _)
      }

  override private[calculator] def erase() = {
    numbers = Nil
    resetExtremes()
    canvas.repaint()
  }

  /** start and previous mouse position during moving */
  private var startPoint: Point = null
  private var previousPoint: Point2 = null

  override def mousePressed(point: Point) = interaction match {
    case MOVE => {
      startPoint = point
      previousPoint = point
      canvas.startShift(startPoint)
    }
    case DRAW =>
      canvas.point2Complex(point).foreach { z =>
        calculator.add(z)
        add(z)
      }
    case _ =>
      //println("interaction == " + interaction)
  }

  override def mouseDragged(point: Point) = interaction match {
    case MOVE =>
      val p = point
      canvas.shift(startPoint, previousPoint, p)
      previousPoint = p
    case _ =>
      //println("interaction == " + interaction)
  }

  override def mouseReleased(point: Point) = interaction match {
    case MOVE =>
      val end = point
      canvas.endShift(startPoint, end)
      startPoint = null
      previousPoint = null
    case _ =>
      //println("interaction == " + interaction)
  }

}