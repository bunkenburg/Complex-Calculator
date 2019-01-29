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

import cat.inspiracio.calculator.Interaction.MOVE
import cat.inspiracio.complex.Complex
import cat.inspiracio.geometry.{Freeline, Piclet, Point2}
import cat.inspiracio.parsing.Syntax
import javax.swing.event.{MouseInputAdapter, MouseInputListener}

final class FzWorld private[calculator](override val calculator: Calculator) extends World(calculator) {

  // connections -----------------------------

  private var zW: ZWorld = null

  //State --------------------------------------------------------------------

  /** During dragging, the sampled points of the free line. Otherwise null. */
  //XXX improve. Make it more local.
  private var zs: List[Complex] = null

  private var piclets: List[Piclet] = Nil

  private var f: Syntax = null

  init()

  private def init() = {
    setTitle("f(z) World")
    interaction = MOVE

    val mouse: MouseInputListener = new MouseInputAdapter() {

      /** the previous mouse position during dragging */
      var previous: Point2 = null

      override def mousePressed(e: MouseEvent): Unit = previous=e.getPoint
      override def mouseReleased(e: MouseEvent): Unit = drag(e)
      override def mouseDragged(e: MouseEvent): Unit = drag(e)

      private def drag(e: MouseEvent) = {
        val p = e.getPoint
        canvas.shift(previous - p)
        previous = p
      }

    }
    plane.addMouseListener(mouse)
    plane.addMouseMotionListener(mouse)
    sphere.addMouseListener(mouse)
    sphere.addMouseMotionListener(mouse)
    pack()
    locate()
    setVisible(true)
  }

  /** to the right of z-world */
  private def locate() = {

    //z world
    if(zW==null)
      zW=calculator.zW
    val zWorldDimension: Dimension = zW.getSize //550 372
    val zWorldPosition: Point = zW.getLocationOnScreen  //77 414

    val p = preferences
    val x = p.getInt("x", zWorldPosition.x + zWorldDimension.width + 10 )
    val y = p.getInt("y", zWorldPosition.y )
    setLocation( x, y )

    val width = p.getInt("width", 560)
    val height = p.getInt("height", 365)
    setSize(width,height)
  }

  override private[calculator] def add(c: Complex) = if (f != null) {
    try {
      val z = f(c)

      if(zs == null)
        zs = Nil
      zs = z :: zs

      updateExtremes(z)
    } catch {
      case _ex: Exception =>
    }
    canvas.repaint()
  }

  private[calculator] def add(piclet: Piclet) = if (f != null) {
    Complex.resetArg()
    var samples = piclet.getSamples

    zs = Nil

    samples.foreach{ z =>
        try {
          val fz = f(z)
          updateExtremes(fz)
          zs = fz :: zs
        } catch {
          case _ex: Exception =>
        }
    }

    stopDynamicMap()
    canvas.repaint()
  }

  private[calculator] def add( list: List[Piclet]): Unit = list foreach add

  private[calculator] def addCurrent(piclet: Piclet) =
    if (f != null) {
      Complex.resetArg()
      zs = Nil
      var samples = piclet.getSamples

      if ( samples != null ) {

        samples.foreach{ z =>
          try {
            val fz = f(z)
            zs = fz :: zs
          } catch {
            case _ex: Exception =>
          }
        }

      }
    canvas.repaint()
  }

  override final private[calculator] def drawStuff(g: Graphics) = {
    if (zs != null)
      canvas.draw(g, zs)

    piclets.foreach{ canvas.draw(g, _) }
  }

  override private[calculator] def erase() = {
    zs = null //forget current free line
    piclets = Nil //Forget all piclets
    resetExtremes()
    canvas.repaint()
  }

  /** Event listener: function has changed. */
  private[calculator] def functionChanged(t: Syntax) = {
    f = t
    erase()
    add(zW.getPiclets)
  }

  private[calculator] def setzWorld(zworld: ZWorld) = zW = zworld

  private[calculator] def stopDynamicMap() = {
    piclets = Freeline(zs) :: piclets
    zs = null
  }
}