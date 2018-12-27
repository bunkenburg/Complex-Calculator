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

import java.awt.event._

import cat.inspiracio.calculator.Interaction.MOVE
import cat.inspiracio.complex.Complex
import cat.inspiracio.numbers._
import cat.inspiracio.parsing.SyntaxTree

final class FzWorld private[calculator](override val calculator: Calculator) extends World(calculator) {

  // connections -----------------------------

  private var zW: ZWorld = null

  //State --------------------------------------------------------------------

  private var current: ECList = null
  private var piclets: PicletList = null
  private var f: SyntaxTree = null

  init()

  private def init() = {
    setTitle("f(z) World")
    interaction = MOVE

    val mouse: MouseAdapter = new MouseAdapter() {

      override def mousePressed(mouseevent: MouseEvent): Unit = {
        prevx = mouseevent.getX
        prevy = mouseevent.getY
        mouseevent.consume()
      }

      override def mouseReleased(mouseevent: MouseEvent): Unit = {
        val i = mouseevent.getX
        val j = mouseevent.getY
        canvas.shift(prevx - i, prevy - j)
        canvas.paint(canvas.getGraphics)
        prevx = i
        prevy = j
        mouseevent.consume()
      }

    }

    val motion: MouseMotionAdapter = new MouseMotionAdapter() {

      override def mouseDragged(mouseevent: MouseEvent): Unit = {
        val i = mouseevent.getX
        val j = mouseevent.getY
        canvas.shift(prevx - i, prevy - j)
        canvas.paint(canvas.getGraphics)
        prevx = i
        prevy = j
        mouseevent.consume()
      }

    }
    plane.addMouseListener(mouse)
    plane.addMouseMotionListener(motion)
    sphere.addMouseListener(mouse)
    sphere.addMouseMotionListener(motion)
    pack()
    setLocationRelativeTo(calculator)
    setVisible(true)
  }

  override def setLocation() = setLocationRelativeTo(this.calculator)

  override private[calculator] def add(c: Complex) = if (f != null) {
    try {
      val z = f.evaluate(c)
      current = new ECList(z, current)
      updateExtremes(z)
    } catch {
      case _ex: Exception =>
    }
    canvas.paint(canvas.getGraphics)
  }

  private[calculator] def add(piclet: Piclet) = if (f != null) {
    resetArg()
    var samples = piclet.getSamples
    current = null
    while ( samples != null ) {
      try {
        val z = samples.head
        val c = f.evaluate(z)
        updateExtremes(c)
        current = new ECList(c, current)
      } catch {
        case _ex: Exception =>
      }
      samples = samples.tail
    }
    stopDynamicMap()
    canvas.repaint()
  }

  private[calculator] def add( list: PicletList): Unit = {
    var ps: PicletList = list
    while ( ps != null ) {
      add(ps.head)
      ps = ps.tail
    }
  }

  private[calculator] def addCurrent(piclet: Piclet) =
    if (f != null) {
      resetArg()
      current = null
      var samples = piclet.getSamples
      while ( samples != null ) {
      try {
        val z = samples.head
        val c = f.evaluate(z)
        current = new ECList(c, current)
      } catch {
        case _ex: Exception =>
      }
      samples = samples.tail
    }
    canvas.paint(canvas.getGraphics)
  }

  override final private[calculator] def drawStuff(drawing: Drawing) = {
    if (current != null) canvas.drawECList(drawing, current)
    var ps = piclets
    while ( ps != null ) {
      canvas.drawPiclet(drawing, ps.head)
      ps = ps.tail
    }
  }

  override private[calculator] def erase() = {
    current = null
    piclets = null
    resetExtremes()
    canvas.repaint()
  }

  private[calculator] def functionChange(syntaxtree: SyntaxTree) = {
    f = syntaxtree
    erase()
    add(zW.getPiclets)
  }

  private[calculator] def setzWorld(zworld: ZWorld) = zW = zworld

  private[calculator] def stopDynamicMap() = {
    piclets = new PicletList(new Freeline(current), piclets)
    current = null
  }
}