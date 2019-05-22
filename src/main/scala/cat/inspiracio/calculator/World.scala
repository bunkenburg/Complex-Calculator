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
import java.lang.Math.{max, min}
import java.util.prefs.Preferences

import cat.inspiracio.calculator.Interaction.{DRAW, Interaction, MOVE}

import scala.swing._
import cat.inspiracio.complex._
import cat.inspiracio.geometry.{Piclet, Point2}

import scala.swing.event.WindowClosing

/** A frame that shows complex number on the complex plane or the Riemann sphere. */
abstract class World protected(val calculator: Calculator) extends Frame {
  import icon._

  protected val toolbar: ToolBar = new ToolBar

  private val planeButton = toggle(planeIcon, "Complex plane", usePlane() )
  private val sphereButton = toggle(sphereIcon,"Riemann sphere", useSphere() )
  private val zInButton = button(zoomInIcon, "Zoom in", canvas.zoomIn() )
  private val zOutButton = button(zoomOutIcon, "Zoom out", canvas.zoomOut() )

  private val choice = new ButtonGroup(planeButton,sphereButton)

  var interaction: Interaction = null

  /** canvas: where I show stuff */
  final protected val plane = new Plane(this)
  final protected val sphere = new Sphere(this)
  protected var canvas: WorldRepresentation = plane

  /** maximal displayed stuff */
  private[calculator] var MaxImaginary = 0.0
  private[calculator] var MinImaginary = 0.0
  private[calculator] var MaxReal = 0.0
  private[calculator] var MinReal = 0.0

  resetExtremes()

  toolbar.contents += planeButton
  toolbar.contents += sphereButton
  toolbar.peer.addSeparator()

  toolbar.contents += zInButton
  toolbar.contents += zOutButton
  toolbar.peer.addSeparator()

  val resetButton = button(resetIcon, "Reset", canvas.reset() )
  toolbar.contents += resetButton

  val panel = new BorderPanel{
    layout(toolbar) = BorderPanel.Position.North
    layout(canvas) = BorderPanel.Position.Center
  }
  contents = panel

  reactions += { case WindowClosing(source) => calculator.quit() }

  pack()

  protected def useSphere(): Unit = {
    if(canvas!=sphere) {
      panel.layout(sphere) = BorderPanel.Position.Center
      canvas = sphere
      zInButton.enabled = false
      zOutButton.enabled = false
      validate()
      canvas.repaint()
    }
    choice.select(sphereButton)
  }

  protected def usePlane(): Unit = {
    if(canvas!=plane) {
      panel.layout(plane) = BorderPanel.Position.Center
      canvas = plane
      zInButton.enabled = true
      zOutButton.enabled = true
      validate()
      canvas.repaint()
    }
    choice.select(planeButton)
  }

  private[calculator] def add(c: Complex) = {}

  private[calculator] def draw(g: Graphics)

  /** erase everything that is show on the world and repaint */
  private[calculator] def erase()

  protected def resetExtremes(): Unit = {
    MinReal = Double.PositiveInfinity
    MaxReal = Double.NegativeInfinity
    MinImaginary = Double.PositiveInfinity
    MaxImaginary = Double.NegativeInfinity
  }

  protected def updateExtremes(c: Complex): Unit =
    if (finite(c)) {
      MaxImaginary = max(MaxImaginary, Im(c))
      MinImaginary = min(MinImaginary, Im(c))
      MaxReal = max(MaxReal, Re(c))
      MinReal = min(MinReal, Re(c))
    }

  protected def updateExtremes(piclet: Piclet): Unit = {
    MaxImaginary = max(MaxImaginary, piclet.top)
    MinImaginary = min(MinImaginary, piclet.bottom)
    MaxReal = max(MaxReal, piclet.right)
    MinReal = min(MinReal, piclet.left)
  }

  override def font_=(font: Font): Unit = {
    plane.font = font
    sphere.font = font
  }

  protected def preferences = Preferences.userNodeForPackage(getClass).node(getClass.getSimpleName)

  override def dispose(): Unit = {
    if(visible) {
      val p = preferences
      val Point2(x, y) = locationOnScreen
      p.putInt("x", x)
      p.putInt("y", y)

      p.putInt("width", size.width)
      p.putInt("height", size.height)

      val c = canvas match {
        case `plane` => "plane"
        case `sphere` => "sphere"
      }
      p.put("canvas", c)
      p.putDouble("zoom", canvas.zoom)

      if(interaction!=null)
        p.put("interaction", interaction.toString)
    }
    super.dispose()
  }

  def mousePressed(point: Point)
  def mouseDragged(point: Point)
  def mouseReleased(point: Point)

  def size_= (p: (Int,Int)): Unit = {
    val (width, height) = p
    size = new Dimension(width, height)
  }
}