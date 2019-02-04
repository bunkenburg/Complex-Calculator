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

/** The z-World is where the user gives input for function f. */
final class ZWorld private[calculator](override val calculator: Calculator) extends World(calculator) {
  import calculator.{fzW, modfzW}

  //State ------------------------------------------------

  private var interactionChoice: JComboBox[String] = new JComboBox[String]
  private var eraseButton: JButton = new JButton("Clear")

  private var mode: Mode = null

  /** During dragging of a free line, the points, otherwise null.
    * When dragging is finished, will be made into a curve and added
    * to pictlets. */
  private var zs: List[Complex] = null

  /** During dragging, the current provisional piclet. */
  private var currentPiclet: Piclet = null

  private var piclets: List[Piclet] = Nil

  /** The square that is used in z -> |f(z)| */
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

      /** start dynamic mapping of a piclet */
      private def startCurrent(p: Point) = {
        canvas.point2Complex(p).foreach{ z =>
          start = z
          end = z
          val current = interaction match {
            case GRID => Rectangle(start, end)
            case LINE => Line(start, end)
            case CIRCLE => Circle(start, end)
            case RECTANGLE => Rectangle(start, end)
            case SQUARE => Square(start, end)
            case _ => throw new RuntimeException(interaction.toString)
          }
          if (mode == FZ)
            addCurrent(current)
          else if (mode == MODFZ && interaction==SQUARE) {
            square = current.asInstanceOf[Square]
            modfzW.change(square)
          }
          canvas.repaint()
        }
      }

      /** continue dynamic mapping of a piclet */
      private def continueCurrent(p: Point) = {
        canvas.point2Complex(p).foreach{ z =>
          end = z
          val current = interaction match {
            case GRID => Rectangle(start, end)
            case LINE => Line(start, end)
            case CIRCLE => Circle(start, end)
            case RECTANGLE => Rectangle(start, end)
            case SQUARE => Square(start, end)
            case _ => throw new RuntimeException(interaction.toString)
          }
          if (mode == FZ)
            addCurrent(current)
          else if (mode == MODFZ && interaction==SQUARE) {
            square = current.asInstanceOf[Square]
            modfzW.change(square)
          }
          canvas.repaint()
        }
      }

      /** finish dynamic mapping of a piclet */
      private def finishCurrent(p: Point) =
        if (start != null) {
          canvas.point2Complex(p).foreach{ z =>
            end = z
            val current = interaction match {
              case GRID => Rectangle(start, end)
              case LINE => Line(start, end)
              case CIRCLE => Circle(start, end)
              case RECTANGLE => Rectangle(start, end)
              case SQUARE => Square(start, end)
              case _ => throw new RuntimeException(interaction.toString)
            }
            if (mode == FZ) {
              if (interaction == GRID)
                addGrid(current.asInstanceOf[Rectangle])
              else
                add(current)
            }
            else if (mode == MODFZ && interaction==SQUARE) {
              square = current.asInstanceOf[Square]
              modfzW.change(square)
            }
            eraseCurrent()
            canvas.repaint()
          }
        }

      override def mousePressed(e: MouseEvent): Unit = interaction match {
        case MOVE => previous = e.getPoint

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

      private def drag(e: MouseEvent) = {
        val p = e.getPoint
        canvas.shift(previous - p)
        previous = p
      }

      override def mouseDragged(e: MouseEvent): Unit = interaction match {
        case MOVE => drag(e)

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
        case MOVE => drag(e)

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

    val c = p.get("canvas", "plane")
    c match {
      case "plane" => usePlane()
      case "sphere" => useSphere()
    }
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
  private def addGrid(r: Rectangle) = {
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

  /** Erases everything and repaints. */
  override private[calculator] def erase(): Unit = {
    eraseCurrent()
    piclets = Nil
    resetExtremes()
    if (mode == MODFZ)
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
  private[calculator] def getSquare = square

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
  }

}