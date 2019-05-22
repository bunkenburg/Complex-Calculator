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

import java.awt.{Graphics, Point}

import cat.inspiracio.calculator.Interaction.MOVE
import cat.inspiracio.complex.Complex
import cat.inspiracio.geometry.{Curve, Piclet, Point2}

final class FzWorld private[calculator](override val calculator: Calculator) extends World(calculator) {
  import calculator.{f, zW}

  //State --------------------------------------------------------------------

  /** In z->f(z) mode, while the user drags on the z world, this is the curve
    * of resulting f(z). Otherwise null.
    * (That procedure is called "dynamic map".)
    * Can zs be improved somehow? Make it more local? */
  private var zs: List[Complex] = null

  /** The piclets that are displayed on the f(z) world. Usually curves. */
  private var piclets: List[Piclet] = Nil

  // initialisation -----------------------------------------------------------

  init()

  private def init() = {
    import icon._
    title = "f(z)"

    toolbar.peer.addSeparator()

    //move button always selected: just there to show user that they can move
    val moveButton = toggle(handIcon, "Move")
    moveButton.selected = true
    moveButton.enabled = false
    toolbar.contents += moveButton

    interaction = MOVE
    pack()
    applyPreferences()
    visible = true
  }

      /** start and previous mouse position during dragging */
      private var startPoint: Point = null
      private var previousPoint: Point = null

      override def mousePressed(point: Point) = {
        startPoint = point
        previousPoint = point
        canvas.startShift(startPoint)
      }

      override def mouseDragged(point: Point) = {
        canvas.shift(startPoint, previousPoint, point)
        previousPoint = point
      }

      override def mouseReleased(point: Point) = {
        canvas.endShift(startPoint, point)
      }

  private def applyPreferences() = {
    val p = preferences

    // to the right of z-world
    val zWorldDimension = zW.size //550 372
    val zWorldPosition = zW.locationOnScreen  //77 414
    val x = p.getInt("x", zWorldPosition.x + zWorldDimension.width + 10 )
    val y = p.getInt("y", zWorldPosition.y )
    location = Point2( x, y )

    val width = p.getInt("width", 560)
    val height = p.getInt("height", 365)
    size = (width, height)

    val c = p.get("canvas", "plane")
    c match {
      case "plane" => usePlane()
      case "sphere" => useSphere()
    }
    canvas.zoom = p.getDouble("zoom", 1)
  }

  // methods --------------------------------------------------------

  /** During dynamic map, adds another number. */
  private[calculator] def addNumberToCurve(c: Complex) = if (f != null) {
    try {
      val z = f(c)
      if(zs == null) zs = Nil
      zs = z :: zs
      updateExtremes(z)
      canvas.repaint()
    }
    //Maybe f(c) failed.
    catch {
      case _ex: Exception =>
    }
  }

  /** Adds a piclet to be mapped. */
  private[calculator] def add(piclet: Piclet) = if (f != null) {
    Complex.resetArg()
    val samples = piclet.samples //sure these are continuous?
    var zs: List[Complex] = Nil
    samples.foreach{ z =>
        try {
          val fz = f(z)
          updateExtremes(fz)
          zs = fz :: zs
        } catch {
          case _ex: Exception =>
        }
    }
    piclets = Curve(zs) :: piclets
    canvas.repaint()
  }

  private[calculator] def add( list: List[Piclet]): Unit = list foreach add

  /** during dragging in z world, the current piclet */
  private[calculator] def addCurrent(p: Piclet) =
    if (f != null) {

      //XXX maybe better:
      //this.current = f(p)

      Complex.resetArg()
      zs = Nil
      val samples = p.samples
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

  override final private[calculator] def draw(g: Graphics) = {
    if (zs != null) canvas.draw(g, zs)
    piclets.foreach{ canvas.draw(g, _) }
  }

  override private[calculator] def erase() = {
    zs = null //forget current curve
    piclets = Nil //forget all piclets
    resetExtremes()
    canvas.repaint()
  }

  /** Event listener: function has changed. */
  private[calculator] def functionChanged() = {
    erase()
    add(zW.piclets)
  }

  private[calculator] def stopDynamicMap() = {
    piclets = Curve(zs) :: piclets
    zs = null
  }
}