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
import cat.inspiracio.calculator.Mode.{FZ, MODFZ, Mode}
import cat.inspiracio.complex._
import cat.inspiracio.geometry._

/** The z-World is where the user gives input for function f. */
final class ZWorld private[calculator](override val calculator: Calculator) extends World(calculator) {
  import calculator.{fzW,modfzW}
  import icon._

  //State ------------------------------------------------

  private val clearButton = button(clearIcon, "Clear", erase() )

  private val moveButton = toggle(handIcon, "Move", { interaction = Interaction.MOVE } )
  private val drawButton = toggle(drawIcon, "Draw", { interaction = Interaction.DRAW })
  private val lineButton = toggle(lineIcon, "Line", { interaction = Interaction.LINE })
  private val circleButton = toggle(circleIcon, "Circle", { interaction = Interaction.CIRCLE })
  private val rectangleButton = toggle(rectangleIcon, "Rectangle", { interaction = Interaction.RECTANGLE })
  private val squareButton = toggle(squareIcon, "Square", { interaction = Interaction.SQUARE })
  private val gridButton = toggle(gridIcon, "Plane", { interaction = Interaction.GRID })
  private val interactionChoice = new ButtonGroup(moveButton, drawButton, lineButton, circleButton, rectangleButton, squareButton, gridButton)

  private var _mode: Mode = null

  /** During dragging of a free line, the points, otherwise null.
    * When dragging is finished, will be made into a curve and added
    * to pictlets. */
  private var zs: List[Complex] = null

  /** During dragging, the current provisional piclet. */
  private var currentPiclet: Piclet = null

  private var _piclets: List[Piclet] = Nil
  def piclets: List[Piclet] = _piclets

  /** The square that is used in z -> |f(z)| */
  private[calculator] var square = Square(0, 1+i)

  init()

  // method ----------------------------------------------

  private def init()= {
    title = "z"

    toolbar.contents += clearButton
    toolbar.peer.addSeparator()

    interaction = DRAW

    toolbar.contents += moveButton
    toolbar.contents += drawButton
    toolbar.contents += lineButton
    toolbar.contents += circleButton
    toolbar.contents += rectangleButton
    toolbar.contents += squareButton
    toolbar.contents += gridButton

    pack()
    preferred()
    visible = true

  }//init

  //during dragging, starting and ending complex number
  private var startComplex: Complex = null
  private var endComplex: Complex = null

    // start dynamic mapping of a piclet
    private def startCurrent(p: Point) = {
      canvas.point2Complex(p).foreach{ z =>
        startComplex = z
        endComplex = z
        val current = interaction match {
          case GRID => Rectangle(startComplex, endComplex)
          case LINE => Line(startComplex, endComplex)
          case CIRCLE => Circle(startComplex, endComplex)
          case RECTANGLE => Rectangle(startComplex, endComplex)
          case SQUARE => Square(startComplex, endComplex)
          case _ => throw new RuntimeException(interaction.toString)
        }
        if (_mode == FZ)
          addCurrent(current)
        else if (_mode == MODFZ && interaction==SQUARE) {
          square = current.asInstanceOf[Square]
          modfzW.change()
        }
        canvas.repaint()
      }
    }

  // continue dynamic mapping of a piclet
  private def continueCurrent(p: Point) = {
    canvas.point2Complex(p).foreach{ z =>
      endComplex = z
      val current = interaction match {
        case GRID => Rectangle(startComplex, endComplex)
        case LINE => Line(startComplex, endComplex)
        case CIRCLE => Circle(startComplex, endComplex)
        case RECTANGLE => Rectangle(startComplex, endComplex)
        case SQUARE => Square(startComplex, endComplex)
        case _ => throw new RuntimeException(interaction.toString)
      }
      if (_mode == FZ)
        addCurrent(current)
      else if (_mode == MODFZ && interaction==SQUARE) {
        square = current.asInstanceOf[Square]
        modfzW.change()
      }
      canvas.repaint()
    }
  }

  // finish dynamic mapping of a piclet
  private def finishCurrent(p: Point) =
    if (startComplex != null) {
      canvas.point2Complex(p).foreach{ z =>
        endComplex = z
        val current = interaction match {
          case GRID => Rectangle(startComplex, endComplex)
          case LINE => Line(startComplex, endComplex)
          case CIRCLE => Circle(startComplex, endComplex)
          case RECTANGLE => Rectangle(startComplex, endComplex)
          case SQUARE => Square(startComplex, endComplex)
          case _ => throw new RuntimeException(interaction.toString)
        }
        if (_mode == FZ) {
          if (interaction == GRID)
            addGrid(current.asInstanceOf[cat.inspiracio.geometry.Rectangle])
          else
            add(current)
        }
        else if (_mode == MODFZ && interaction==SQUARE) {
          square = current.asInstanceOf[Square]
          modfzW.change()
        }
        eraseCurrent()
        canvas.repaint()
      }
    }

  // during moving, start and previous end mouse position
  private var startPoint: Point = null
  private var previousPoint: Point = null

  override def mousePressed(point: Point) = interaction match {
    case MOVE => {
      startPoint = point
      previousPoint = point
      canvas.startShift(startPoint)
    }

    case DRAW => {
      canvas.point2Complex(point).foreach{ z =>
        Complex.resetArg()
        addNumberToCurve(z)
        canvas.repaint()
      }
    }

    case GRID => startCurrent(point)
    case LINE => startCurrent(point)
    case CIRCLE => startCurrent(point)
    case RECTANGLE => startCurrent(point)
    case SQUARE => startCurrent(point)
  }

  override def mouseDragged(point: Point) = interaction match {
    case MOVE => {
      val p = point
      canvas.shift(startPoint, previousPoint, p)
      previousPoint = p
    }

    case DRAW => {
      val maybe = canvas.point2Complex(point)
      if (zs != null) {
        maybe match {
          case Some(z) =>
            addNumberToCurve(z)
          case None => {
            _piclets = Curve(zs) :: _piclets
            zs = null
            fzW.stopDynamicMap()
          }
        }
      }
      else if (maybe.isDefined) {
        val Some(z) = maybe
        Complex.resetArg()
        addNumberToCurve(z)
      }
      canvas.repaint()
    }

    case CIRCLE => continueCurrent(point)
    case GRID => continueCurrent(point)
    case LINE => continueCurrent(point)
    case RECTANGLE => continueCurrent(point)
    case SQUARE => continueCurrent(point)
  }

  override def mouseReleased(point: Point) = interaction match {
    case MOVE => {
      val endPoint = point
      canvas.endShift(startPoint, endPoint)
      startPoint = null
      previousPoint = null
    }

    case DRAW => {
      if (zs != null) {
        canvas.point2Complex(point).foreach { z =>
          addNumberToCurve(z)
        }
        _piclets = Curve(zs) :: _piclets
        zs = null   //mouse released => finish free line
        canvas.repaint()
        fzW.stopDynamicMap()
      }
    }

    case LINE => finishCurrent(point)
    case CIRCLE => finishCurrent(point)
    case RECTANGLE => finishCurrent(point)
    case SQUARE => finishCurrent(point)
    case GRID => finishCurrent(point)
    case _ =>
      println("interaction = " + interaction)
  }

  /** applies the saved preferences */
  private def preferred(): Unit = {
    val p = preferences

    //Exception in thread "main" java.awt.IllegalComponentStateException: component must be showing on the screen to determine its location
    if(!calculator.visible)
      return ()

    // below the calculator
    val calculatorDimension: Dimension = calculator.size // 319 x 328
    val calculatorPosition: Point = calculator.locationOnScreen  // 77 38
    val x = p.getInt("x", calculatorPosition.x )
    val y = p.getInt("y", calculatorPosition.y + calculatorDimension.height + 10 )
    location = Point2( x, y )

    val width = p.getInt("width", 560)
    val height = p.getInt("height", 365)
    size = new Dimension(width,height)

    val c = p.get("canvas", "plane")
    c match {
      case "plane" => usePlane()
      case "sphere" => useSphere()
    }
    canvas.zoom = p.getDouble("zoom", 1)

    val is = p.get("interaction", "Draw")
    interaction = Interaction.withName(is)
    //interactionChoice.setSelected(model, true)
  }

  /** during dynamic map, adds one more number */
  private def addNumberToCurve(c: Complex) = {
    updateExtremes(c)
    if( zs == null ) zs = Nil
    zs = c :: zs
    fzW.addNumberToCurve(c)  //tell the fz world
  }

  private def add(p: Piclet) = {
    updateExtremes(p)
    _piclets = p :: _piclets
    fzW.add(p)  //tell f(z) world so it can map it
  }

  /** during dragging, update the current piclet */
  private def addCurrent(piclet: Piclet) = {
    currentPiclet = piclet
    fzW.addCurrent(piclet)
  }

  /** Adds a grid by decomposing it into lines.
    * XXX Maybe this can be improved. */
  private def addGrid(r: cat.inspiracio.geometry.Rectangle) = {
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

    _piclets.foreach{
      canvas.draw(g, _)
    }

    if (_mode == MODFZ)
      canvas.draw(g, square)
  }

  /** Erases everything and repaints. */
  override private[calculator] def erase(): Unit = {
    eraseCurrent()
    _piclets = Nil
    resetExtremes()
    if (_mode == MODFZ)
      updateExtremes(square)
    else if (fzW != null)
        fzW.erase()
    canvas.repaint()
  }

  private[calculator] def eraseCurrent() = {
    zs = null
    currentPiclet = null
  }

  def mode_=(m: Mode) = {
    _mode = m
    _mode match {

      case FZ =>
        clearButton.enabled = true
        drawButton.enabled = true
        circleButton.enabled = true
        lineButton.enabled = true
        gridButton.enabled = true
        rectangleButton.enabled = true
        interactionChoice.select(drawButton)
        interaction = DRAW

      case MODFZ =>
        clearButton.enabled = false
        drawButton.enabled = false
        circleButton.enabled = false
        lineButton.enabled = false
        gridButton.enabled = false
        rectangleButton.enabled = false
        interactionChoice.select(squareButton)
        interaction = SQUARE

      case _ =>
    }
    erase()
  }

}