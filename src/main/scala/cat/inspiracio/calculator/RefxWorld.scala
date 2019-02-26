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
import java.util.prefs.Preferences

import javax.swing._
import cat.inspiracio.complex._
import cat.inspiracio.geometry.Point2

// Referenced classes of package bunkenba.calculator:
//            Calculator, DoubleBuffer, Drawing

/** Shows Re(f(x)) */
final class RefxWorld private[calculator](calculator: Calculator) extends JFrame("Re(f(x))") {

  // GUI -----------------------------------------------------

  private val toolbar = new JToolBar()
  private val canvas = new RefxCanvas(calculator)

  init()

  private def init()= {
    import icon._

    canvas.resetExtremes()

    val btnZoomIn = button(zoomInIcon, "Zoom in")
    btnZoomIn.addActionListener( _ => zoomIn() )
    toolbar.add(btnZoomIn)

    val btnZoomOut = button(zoomOutIcon, "Zoom out")
    btnZoomOut.addActionListener( _ => zoomOut() )
    toolbar.add(btnZoomOut)

    val btnReset = button(resetIcon, "Reset")
    btnReset.addActionListener( _ => reset() )
    toolbar.add(btnReset)
    toolbar.addSeparator()

    //move button always selected: just there to show user that they can move
    val moveButton = new JToggleButton(handIcon)
    moveButton.setToolTipText("Move")
    moveButton.setSelected(true)
    moveButton.setEnabled(false)
    toolbar.add(moveButton)

    add(toolbar, BorderLayout.PAGE_START)
    add(canvas, BorderLayout.CENTER)

    addWindowListener(new WindowAdapter() {
      override def windowClosing(windowevent: WindowEvent): Unit = calculator.quit()
    })

    pack()
    locate()
    setVisible(true)
  }

  /** to the right of the calculator */
  private def locate() = {
    //calculator
    val calculatorDimension: Dimension = calculator.getSize // 319 x 328
    val calculatorPosition: Point = calculator.getLocationOnScreen  // 77 38

    val p = preferences
    val x = p.getInt("x", calculatorPosition.x + calculatorDimension.width + 10 )
    val y = p.getInt("y", calculatorPosition.y )
    setLocation( x, y )

    val width = p.getInt("width", 560)
    val height = p.getInt("height", 365)
    setSize(width,height)
  }

  private def reset() = {
    canvas.reset()
    canvas.repaint()
  }

  private def zoomOut() = {
    canvas.zoomOut()
    canvas.repaint()
  }

  private def zoomIn() = {
    canvas.zoomIn()
    canvas.repaint()
  }

  /** Event listener: the function has changed. */
  private[calculator] def functionChanged() = {
    canvas.repaint()
  }

  override def setFont(font: Font): Unit = {
    super.setFont(font)
    canvas.setFont(font)
  }

  // helpers -----------------------------------

  private def toString(d: Double): String = double2Complex(d).toString

  protected def preferences = Preferences.userNodeForPackage(getClass).node(getClass.getSimpleName)

  override def dispose(): Unit = {
    if(isVisible){
      val p = preferences
      val Point2(x,y) = getLocationOnScreen
      p.putInt("x", x )
      p.putInt("y", y )

      val size = getSize
      p.putInt("width", size.width)
      p.putInt("height", size.height)
    }
    super.dispose()
  }

}