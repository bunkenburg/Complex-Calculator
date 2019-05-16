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

import java.awt._
import java.awt.event._
import java.lang.Math.{max}
import java.util.prefs.Preferences

import scala.swing._
import cat.inspiracio.complex._
import cat.inspiracio.geometry._
import cat.inspiracio.parsing.{Constant, Syntax}

// Referenced classes of package bunkenba.calculator:
//            Calculator, DoubleBuffer, Drawing, Matrix44,
//            Vector2, Vector3

final class ThreeDWorld private[calculator](calculator: Calculator) extends Frame("|f(z)| World") {

  //State ---------------------------------------------

  private val canvas = new ThreeDCanvas(this)

  /** The absolute values: |f(z)|
    * N * N Doubles.
    * In both axes, ten to either side from the center.
    * The range from 0 to 1. */
  private[calculator] val m = new M[Double](10)

  init()

  private def init()= {
    import icon._

    val toolbar = new ToolBar()
    val resetButton = button(resetIcon, "Reset")
    resetButton.addActionListener( _ => canvas.reset() )
    toolbar.add(resetButton)
    toolbar.addSeparator()

    //move button always selected: just there to show user that they can move
    val moveButton = toggle(handIcon, "Move")
    moveButton.selected = true
    moveButton.enabled = false
    toolbar.add(moveButton)

    add(toolbar, BorderLayout.PAGE_START)
    add(canvas, BorderLayout.CENTER)

    addWindowListener(new WindowAdapter() {
      override def windowClosing(windowevent: WindowEvent): Unit = calculator.quit()
    })

    pack()
    locate()
    visible = true
  }

  //Methods -----------------------------------------

  /** the square for which to show |f(z)| */
  private[calculator] def square = calculator.zW.square

  /** to the right of z-world */
  private def locate() = {
    val zW = calculator.zW
    val zWorldDimension: Dimension = zW.size //550 372
    val zWorldPosition: Point = zW.locationOnScreen  //77 414

    val p = preferences
    val x = p.getInt("x", zWorldPosition.x + zWorldDimension.width + 10 )
    val y = p.getInt("y", zWorldPosition.y )
    setLocation( x, y )

    val width = p.getInt("width", 560)
    val height = p.getInt("height", 365)
    setSize(width,height)
  }

  /** Event listener: the function has changed. */
  private[calculator] def functionChanged() = {
    setNeighbourhood()
    canvas.repaint()
  }

  /** Assigns to M values |f(z)|. */
  private[calculator] def setNeighbourhood(): Unit = {
    val N = m.n
    val f: Syntax = if(calculator.f!=null) calculator.f else new Constant(0)
    val step: Double = square.side / (N * 2.0)
    val center: Complex = square.center

    /** |f(z)| but -0.2 in case of exception */
    def absf(z: Complex): Double =
      try {
        val fz = f( center + z )
        if(!finite(fz)) Double.PositiveInfinity else abs(fz)
      } catch {
        case _ : Exception => -0.2
      }

    // Find all |f(z)|
    for( i <- -N to N ; j <- -N to N ){
      val z = Cartesian(step * i, step * j)
      m(i,j) = absf(z)
    }

    //Find the maximum for scaling
    var maximum = 0.0
    for( i <- -N to N ; j <- -N to N ){
      val r = m(i,j)
      if (! r.isInfinite() )
        maximum = max( maximum, r )
    }

    //Maximum must not be zero because we will divide by it.
    if (maximum < 0.0001) maximum = 1.0

    //scale to [-0.2, 1.2]
    def scale(r: Double): Double =
      if ( r.isInfinite() ) 1.2   //Infinity peaks above the box.
      else if ( r < 0.0 ) -0.2    //A singularity peaks below the box.
      else r / maximum

    for( i <- -N to N ; j <- -N to N )
      m(i,j) = scale( m(i,j) )
  }

  private[calculator] def change() = {
    setNeighbourhood()
    canvas.repaint()
  }

  override def font_=(font: Font): Unit = {
    super.font = font
    canvas.font = font
  }

  protected def preferences = Preferences.userNodeForPackage(getClass).node(getClass.getSimpleName)

  override def dispose(): Unit = {
    if(isVisible){
      val p = preferences
      val Point2(x,y) = getLocationOnScreen
      p.putInt("x", x )
      p.putInt("y", y )

      p.putInt("width", size.width)
      p.putInt("height", size.height)
    }
    super.dispose()
  }
}