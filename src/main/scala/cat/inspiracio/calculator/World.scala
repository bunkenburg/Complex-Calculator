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
import java.awt.event._
import java.lang.Math.{max, min}
import java.util.prefs.Preferences

import cat.inspiracio.calculator.Interaction.Interaction
import javax.swing._
import cat.inspiracio.complex._
import cat.inspiracio.geometry.{Piclet, Point2}

/** A frame that shows complex number on the complex plane or the Riemann sphere. */
abstract class World protected(val calculator: Calculator) extends JFrame {
  import icon._

  protected val toolbar: JToolBar = new JToolBar()
  //toolbar.setBackground(Color.lightGray)

  private val planeButton = new JToggleButton(planeIcon)
  planeButton.setToolTipText("Complex plane")
  planeButton.setOpaque(false)
  //planeButton.setContentAreaFilled(false) //need for signalling selected
  planeButton.setBorderPainted(false)

  private val sphereButton = new JToggleButton(sphereIcon)
  sphereButton.setToolTipText("Riemann sphere")
  sphereButton.setOpaque(false)
  sphereButton.setBorderPainted(false)

  private val zInButton = new JButton(zoomInIcon)
  zInButton.setToolTipText("Zoom in")

  private val zOutButton = new JButton(zoomOutIcon)
  zOutButton.setToolTipText("Zoom out")

  /** Menu for Plane or Sphere */
  private val choice = new ButtonGroup()

  /** selected interaction */
  protected var interaction: Interaction = null

  /** canvas: where I show stuff */
  final protected val plane = new Plane(this)
  final protected val sphere = new Sphere(this)
  protected var canvas: WorldRepresentation = plane

  /** maximal displayed stuff */
  private[calculator] var MaxImaginary = 0.0
  private[calculator] var MinImaginary = 0.0
  private[calculator] var MaxReal = 0.0
  private[calculator] var MinReal = 0.0

  init()

  private def init() = {
    resetExtremes()

    //plane/sphere
    planeButton.addActionListener( _ => usePlane() )
    sphereButton.addActionListener( _ => useSphere() )
    choice.add(planeButton)
    choice.add(sphereButton)
    toolbar.add(planeButton)
    toolbar.add(sphereButton)
    toolbar.addSeparator()

    //zoom in / out
    zInButton.addActionListener(_ => canvas.zoomIn())
    zOutButton.addActionListener(_ => canvas.zoomOut())
    toolbar.add(zInButton)
    toolbar.add(zOutButton)
    toolbar.addSeparator()

    val resetButton = new JButton(resetIcon)
    resetButton.setToolTipText("Reset")
    resetButton.addActionListener(_ => canvas.reset() )
    toolbar.add(resetButton)

    add(toolbar, BorderLayout.PAGE_START)
    add(canvas, BorderLayout.CENTER)

    addWindowListener(new WindowAdapter() {
      override def windowClosing(windowevent: WindowEvent): Unit = calculator.quit()
    })

    pack()
  }

  protected def useSphere() = {
    if(canvas!=sphere) {
      remove(plane)
      add("Center", sphere)
      canvas = sphere
      zInButton.setEnabled(false)
      zOutButton.setEnabled(false)
      validate()
      canvas.repaint()
    }
    choice.setSelected(sphereButton.getModel, true)
  }

  protected def usePlane() = {
    if(canvas!=plane) {
      remove(sphere)
      add("Center", plane)
      canvas = plane
      zInButton.setEnabled(true)
      zOutButton.setEnabled(true)
      validate()
      canvas.repaint()
    }
    choice.setSelected(planeButton.getModel, true)
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

  override def update(g: Graphics): Unit = paint(g)

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

  override def setFont(font: Font): Unit = {
    super.setFont(font)
    plane.setFont(font)
    sphere.setFont(font)
  }

  protected def preferences = Preferences.userNodeForPackage(getClass).node(getClass.getSimpleName)

  override def dispose(): Unit = {
    if(isVisible) {
      val p = preferences
      val Point2(x, y) = getLocationOnScreen
      p.putInt("x", x)
      p.putInt("y", y)

      val size = getSize
      p.putInt("width", size.width)
      p.putInt("height", size.height)

      val c = canvas match {
        case `plane` => "plane"
        case `sphere` => "sphere"
      }
      p.put("canvas", c)

      if(interaction!=null)
        p.put("interaction", interaction.toString)
    }
    super.dispose()
  }

}