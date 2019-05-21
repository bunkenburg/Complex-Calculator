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
import java.awt.event._

import scala.swing._
import cat.inspiracio.calculator.Interaction._
import cat.inspiracio.calculator.Mode.{FZ, MODFZ, Mode}
import cat.inspiracio.complex._
import cat.inspiracio.geometry._
//import scala.swing.event.MouseInputAdapter

/** The z-World is where the user gives input for function f. */
final class ZWorld private[calculator](override val calculator: Calculator) extends World(calculator) {
  import calculator.{fzW, modfzW}
  import icon._

  //State ------------------------------------------------

  private val clearButton = button(clearIcon, "Clear")

  private val interactionChoice = new ButtonGroup()
  private val moveButton = toggle(handIcon, "Move")
  private val drawButton = toggle(drawIcon, "Draw")
  private val lineButton = toggle(lineIcon, "Line")
  private val circleButton = toggle(circleIcon, "Circle")
  private val rectangleButton = toggle(rectangleIcon, "Rectangle")
  private val squareButton = toggle(squareIcon, "Square")
  private val gridButton = toggle(gridIcon, "Plane")

  private var _mode: Mode = null

  /** During dragging of a free line, the points, otherwise null.
    * When dragging is finished, will be made into a curve and added
    * to pictlets. */
  private var zs: List[Complex] = null

  /** During dragging, the current provisional piclet. */
  private var currentPiclet: Piclet = null

  private var piclets: List[Piclet] = Nil

  /** The square that is used in z -> |f(z)| */
  private[calculator] var square = Square(0, 1+i)

  init()

  // method ----------------------------------------------

  private def init()= {

    title = "z"

    //clearButton.addActionListener( _ => erase() )
    toolbar.contents += clearButton
    //toolbar.addSeparator()

    interaction = DRAW

    //moveButton.addActionListener( _ => interaction = Interaction.MOVE )
    interactionChoice.buttons += moveButton
    toolbar.contents += moveButton

    //drawButton.addActionListener( _ => interaction = Interaction.DRAW )
    interactionChoice.buttons += drawButton
    toolbar.contents += drawButton

    //lineButton.addActionListener( _ => interaction = Interaction.LINE )
    interactionChoice.buttons += lineButton
    toolbar.contents += lineButton

    //circleButton.addActionListener( _ => interaction = Interaction.CIRCLE )
    interactionChoice.buttons += circleButton
    toolbar.contents += circleButton

    //rectangleButton.addActionListener( _ => interaction = Interaction.RECTANGLE )
    interactionChoice.buttons += rectangleButton
    toolbar.contents += rectangleButton

    //squareButton.addActionListener( _ => interaction = Interaction.SQUARE )
    interactionChoice.buttons += squareButton
    toolbar.contents += squareButton

    //gridButton.addActionListener( _ => interaction = Interaction.GRID )
    interactionChoice.buttons += gridButton
    toolbar.contents += gridButton

    /*
    val mouse = new MouseInputAdapter() {

      //during dragging, starting and ending complex number
      var startComplex: Complex = null
      var endComplex: Complex = null

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
                addGrid(current.asInstanceOf[Rectangle])
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
      var startPoint: Point = null
      var previousPoint: Point = null

      override def mousePressed(e: MouseEvent): Unit = interaction match {
        case MOVE => {
          startPoint = e.getPoint
          previousPoint = e.getPoint
          canvas.startShift(startPoint)
        }

        case DRAW => {
          canvas.point2Complex(e.getPoint).foreach{ z =>
            Complex.resetArg()
            addNumberToCurve(z)
            canvas.repaint()
          }
        }

        case GRID => startCurrent(e.getPoint)
        case LINE => startCurrent(e.getPoint)
        case CIRCLE => startCurrent(e.getPoint)
        case RECTANGLE => startCurrent(e.getPoint)
        case SQUARE => startCurrent(e.getPoint)
      }

      override def mouseDragged(e: MouseEvent): Unit = interaction match {
        case MOVE => {
          val p = e.getPoint
          canvas.shift(startPoint, previousPoint, p)
          previousPoint = p
        }

        case DRAW => {
          val maybe = canvas.point2Complex(e.getPoint)
          if (zs != null) {
            maybe match {
              case Some(z) =>
                addNumberToCurve(z)
              case None => {
                piclets = Curve(zs) :: piclets
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

        case CIRCLE => continueCurrent(e.getPoint)
        case GRID => continueCurrent(e.getPoint)
        case LINE => continueCurrent(e.getPoint)
        case RECTANGLE => continueCurrent(e.getPoint)
        case SQUARE => continueCurrent(e.getPoint)
      }

      override def mouseReleased(e: MouseEvent): Unit = interaction match {
        case MOVE => {
          val endPoint = e.getPoint
          canvas.endShift(startPoint, endPoint)
          startPoint = null
          previousPoint = null
        }

        case DRAW => {
          if (zs != null) {
            canvas.point2Complex(e.getPoint).foreach { z =>
              addNumberToCurve(z)
            }
            piclets = Curve(zs) :: piclets
            zs = null   //mouse released => finish free line
            canvas.repaint()
            fzW.stopDynamicMap()
          }
        }

        case LINE => finishCurrent(e.getPoint)
        case CIRCLE => finishCurrent(e.getPoint)
        case RECTANGLE => finishCurrent(e.getPoint)
        case SQUARE => finishCurrent(e.getPoint)
        case GRID => finishCurrent(e.getPoint)
      }

    }//MouseInputAdapter
    */

    //plane.addMouseListener(mouse)
    //plane.addMouseMotionListener(mouse)
    //sphere.addMouseListener(mouse)
    //sphere.addMouseMotionListener(mouse)

    pack()
    preferred()
    visible = true

  }//init

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
    piclets = p :: piclets
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

    piclets.foreach{
      canvas.draw(g, _)
    }

    if (_mode == MODFZ)
      canvas.draw(g, square)
  }

  /** Erases everything and repaints. */
  override private[calculator] def erase(): Unit = {
    eraseCurrent()
    piclets = Nil
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

  private[calculator] def getPiclets = piclets

  //private[calculator]
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
        //interactionChoice.setSelected(drawButton.getModel, true)
        interaction = DRAW

      case MODFZ =>
        clearButton.enabled = false
        drawButton.enabled = false
        circleButton.enabled = false
        lineButton.enabled = false
        gridButton.enabled = false
        rectangleButton.enabled = false
        //interactionChoice.setSelected(squareButton.getModel, true)
        interaction = SQUARE

      case _ =>
    }
    erase()
  }

}