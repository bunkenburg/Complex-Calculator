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

import cat.inspiracio.complex
import cat.inspiracio.complex._
import cat.inspiracio.complex.Complex
import cat.inspiracio.geometry.Piclet
import cat.inspiracio.numbers.ECList
import javax.swing._

// Referenced classes of package bunkenba.calculator:
//            DoubleBuffer, World, Drawing

abstract class WorldRepresentation protected(var w: World) extends JComponent {

  //State ---------------------------------------------------------

  setBackground(Color.white)

  protected var doubleBuffer: DoubleBuffer = new DoubleBuffer(this)

  //Methods ------------------------------------------------------

  override def getSize: Dimension = super.getSize

  private[calculator] def drawComplex(drawing: Drawing, ec: Complex)

  private[calculator] def drawECList(drawing: Drawing, eclist: ECList)

  private[calculator] def drawPiclet(drawing: Drawing, piclet: Piclet) = drawECList(drawing, piclet.getSamples)

  override def getPreferredSize: Dimension = getMinimumSize

  /** Initial size wide enough so that the interaction menu is visible. */
  override def getMinimumSize: Dimension = new Dimension(550, 300)

  private[calculator] def moveTo(drawing: Drawing, ec: Complex)

  private[calculator] def lineTo(drawing: Drawing, ec: Complex)

  private[calculator] def Point2Complex(point: Point): Complex

  private[calculator] def reset()

  private[calculator] def shift(i: Int, j: Int)

  private[calculator] def zoomIn()

  private[calculator] def zoomOut()

  // helpers -----------------------------------

  protected def toString(d: Double): String = double2Complex(d).toString

}