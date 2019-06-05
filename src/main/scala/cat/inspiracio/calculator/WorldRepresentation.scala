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

import java.awt.{Color, Dimension, Graphics, Point}

import cat.inspiracio.complex._
import cat.inspiracio.geometry.{Piclet, Point2}

import scala.swing._
import scala.swing.event.{MouseDragged, MousePressed, MouseReleased}

// Referenced classes of package bunkenba.calculator:
//            DoubleBuffer, World, Drawing
/** Representation of a world of complex numbers:
  * as Cartesian plane or as Riemann sphere. */
abstract class WorldRepresentation protected(w: World) extends Component {
  background = Color.white
  //doubleBuffered = true

  protected val short = new ComplexFormat
  short.maximumFractionDigits = 3

  //Methods ------------------------------------------------------

  private[calculator] def draw(g: Graphics, z: Complex)
  private[calculator] def draw(g: Graphics, zs: List[Complex] )
  private[calculator] def draw(g: Graphics, p: Piclet): Unit = draw(g, p.samples)

  override def preferredSize: Dimension = minimumSize

  /** Initial size wide enough so that the interaction menu is visible. */
  override def minimumSize: Dimension = new Dimension(550, 300)

  private[calculator] def point2Complex(p: Point): Option[Complex]

  private[calculator] def reset()

  /** shift calls repaint */
  private[calculator] def startShift(start: Point) = {}
  private[calculator] def shift(start: Point, from: Point, to: Point)
  private[calculator] def endShift(start: Point, end: Point) = {}

  //There is zoom for plane but not for sphere. Zoom causes repaint.
  private[calculator] def zoomIn(): Unit = {}
  private[calculator] def zoomOut(): Unit = {}
  private[calculator] def zoom: Double = 0
  private[calculator] def zoom_=(z: Double) : Unit = {}

  // helpers -----------------------------------

  protected[calculator] def pairs[A](cs : List[A]): List[(A,A)] = cs match {
    case Nil => Nil
    case a::Nil => Nil
    case a::b::cs => (a,b) :: pairs(b::cs)
  }

  protected def width = size.width
  protected def height = size.height

  listenTo(mouse.clicks)
  listenTo(mouse.moves)
  reactions += {
    case MousePressed(source, point, modifiers, clicks, triggersPopup) => w.mousePressed(point)
    case MouseDragged(source, point, modifiers) => w.mouseDragged(point)
    case MouseReleased(source, point, modifiers, clicks, triggersPopup) => w.mouseReleased(point)
  }

}