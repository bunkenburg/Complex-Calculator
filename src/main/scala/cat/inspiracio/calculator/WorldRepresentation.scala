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

import java.awt.{Color,Dimension,Point}

import cat.inspiracio.complex._
import cat.inspiracio.geometry.Piclet
import javax.swing._

// Referenced classes of package bunkenba.calculator:
//            DoubleBuffer, World, Drawing

abstract class WorldRepresentation protected(var w: World) extends JComponent {

  setBackground(Color.white)

  //State ---------------------------------------------------------

  //XXX maybe swing can do double-buffering now?
  protected var doubleBuffer: DoubleBuffer = new DoubleBuffer(this)

  //Methods ------------------------------------------------------

  def paint(): Unit = paint(getGraphics)

  private[calculator] def draw(drawing: Drawing, z: Complex)
  private[calculator] def draw(drawing: Drawing, zs: List[Complex] )
  private[calculator] def draw(drawing: Drawing, p: Piclet): Unit = draw(drawing, p.getSamples)

  override def getPreferredSize: Dimension = getMinimumSize

  /** Initial size wide enough so that the interaction menu is visible. */
  override def getMinimumSize: Dimension = new Dimension(550, 300)

  private[calculator] def moveTo(drawing: Drawing, z: Complex)
  private[calculator] def lineTo(drawing: Drawing, z: Complex)

  private[calculator] def point2Complex(p: Point): Complex

  private[calculator] def reset()

  private[calculator] def shift(x: Int, y: Int)
  private[calculator] def shift(p: Point): Unit = shift(p.x, p.y)

  //There is zoom for plane but not for sphere.
  //zoom causes repaint.
  private[calculator] def zoomIn(): Unit = {}
  private[calculator] def zoomOut(): Unit = {}

  // helpers -----------------------------------

  protected def toString(d: Double): String = double2Complex(d).toString

}