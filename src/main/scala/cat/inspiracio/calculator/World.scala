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
import javax.swing._

import cat.inspiracio.complex._
import cat.inspiracio.geometry.{Piclet, Point2}

/** A frame that shows complex number on the complex plane or the Riemann sphere. */
abstract class World protected(val calculator: Calculator) extends JFrame {

  /** where I show my buttons */
  protected var buttonPanel: JPanel = null

  private val zInButton = new JButton("Zoom In")
  private val zOutButton = new JButton("Zoom Out")

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

    zInButton.addActionListener(_ => canvas.zoomIn())
    zOutButton.addActionListener(_ => canvas.zoomOut())

    val choice = new JComboBox[String]
    choice.addItem("Plane")
    choice.addItem("Sphere")
    choice.setSelectedItem("Plane")
    choice.addItemListener(e => {
      val state = e.getStateChange
      if (state == ItemEvent.SELECTED) {
        val item = e.getItem.toString
        item match {
          case "Plane" => useSphere()
          case "Sphere" => usePlane()
        }
      }
    })

    val button = new JButton("Reset")
    button.addActionListener(_ => canvas.reset() )

    buttonPanel = new JPanel
    buttonPanel.setBackground(Color.lightGray)
    buttonPanel.setLayout(new FlowLayout(0))
    buttonPanel.add(choice)
    buttonPanel.add(zInButton)
    buttonPanel.add(zOutButton)
    buttonPanel.add(button)

    setLayout(new BorderLayout)
    add("North", buttonPanel)
    add("Center", canvas)

    addWindowListener(new WindowAdapter() {
      override def windowClosing(windowevent: WindowEvent): Unit = calculator.quit()
    })

    pack()
  }//init


  private def useSphere() = {
    remove(sphere)
    add("Center", plane)
    canvas = plane
    zInButton.setEnabled(true)
    zOutButton.setEnabled(true)
    validate()
    canvas.repaint()
  }

  private def usePlane() = {
    remove(plane)
    add("Center", sphere)
    canvas = sphere
    zInButton.setEnabled(false)
    zOutButton.setEnabled(false)
    validate()
    canvas.repaint()
  }

  private[calculator] def add(c: Complex)

  private[calculator] def drawStuff(drawing: Drawing)

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
    }
    super.dispose()
  }

}