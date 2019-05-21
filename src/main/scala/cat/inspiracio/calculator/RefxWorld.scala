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

//import java.awt._
import java.awt.event._
import java.util.prefs.Preferences

import scala.swing._
import cat.inspiracio.complex._
import cat.inspiracio.geometry.Point2

// Referenced classes of package bunkenba.calculator:
//            Calculator, DoubleBuffer, Drawing

/** Shows Re(f(x)) */
final class RefxWorld private[calculator](calculator: Calculator) extends Frame() {
  title = "Re(f(x))"

  // GUI -----------------------------------------------------

  private val toolbar = new ToolBar()
  private val canvas = new RefxCanvas(calculator)

  init()

  private def init()= {
    import icon._

    canvas.resetExtremes()

    val btnZoomIn = button(zoomInIcon, "Zoom in")
    //btnZoomIn.addActionListener( _ => zoomIn() )
    toolbar.contents += btnZoomIn

    val btnZoomOut = button(zoomOutIcon, "Zoom out")
    //btnZoomOut.addActionListener( _ => zoomOut() )
    toolbar.contents += btnZoomOut

    val btnReset = button(resetIcon, "Reset")
    //btnReset.addActionListener( _ => reset() )
    toolbar.contents += btnReset
    //toolbar.addSeparator()

    //move button always selected: just there to show user that they can move
    val moveButton = new ToggleButton{ icon = handIcon}
    moveButton.tooltip = "Move"
    moveButton.selected = true
    moveButton.enabled = false
    toolbar.contents += moveButton

    //add(toolbar, BorderLayout.PAGE_START)
    //XXX layout(toolbar) = BorderPanel.Position.West
    //add(canvas, BorderLayout.CENTER)
    //XXX layout(canvas) = BorderPanel.Position.Center

    /*
    addWindowListener(new WindowAdapter() {
      override def windowClosing(windowevent: WindowEvent): Unit = calculator.quit()
    })
     */

    pack()
    locate()
    visible = true
  }

  /** to the right of the calculator */
  private def locate(): Unit = {

    //Exception in thread "main" java.awt.IllegalComponentStateException: component must be showing on the screen to determine its location
    if(!calculator.visible)
      return ()

    //calculator
    val calculatorDimension: Dimension = calculator.size // 319 x 328
    val calculatorPosition: Point = calculator.locationOnScreen  // 77 38

    val p = preferences
    val x = p.getInt("x", calculatorPosition.x + calculatorDimension.width + 10 )
    val y = p.getInt("y", calculatorPosition.y )
    location = Point2( x, y )

    val width = p.getInt("width", 560)
    val height = p.getInt("height", 365)
    size = new Dimension(width,height)
    canvas.zoom = p.getDouble("zoom", 1)
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

  override def font_=(font: Font): Unit = {
    super.font = font
    canvas.font = font
  }

  // helpers -----------------------------------

  private def toString(d: Double): String = double2Complex(d).toString

  protected def preferences = Preferences.userNodeForPackage(getClass).node(getClass.getSimpleName)

  override def dispose(): Unit = {
    if(visible){
      val p = preferences
      val Point2(x,y) = locationOnScreen
      p.putInt("x", x )
      p.putInt("y", y )

      p.putInt("width", size.width)
      p.putInt("height", size.height)
      p.putDouble("zoom", canvas.zoom)
    }
    super.dispose()
  }

}