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

import java.awt.{GridBagConstraints, GridBagLayout}
import java.awt.event.{ActionListener, WindowAdapter, WindowEvent}
import java.text.ParseException

import cat.inspiracio.calculator.Mode._
import cat.inspiracio.complex._
import cat.inspiracio.parsing.Syntax
import cat.inspiracio.parsing.Syntax._

import javax.swing._

/** The Calculator application.
  * This is a frame, the main window.
  * This class has the main method. */
object Calculator {

  /** Run the Calculator as stand-alone application. */
    def main(args: Array[String]): Unit = {
      val calculator = new Calculator
      //calculator.inAnApplet = false
    }

}

final class Calculator() extends JFrame("Complex Calculator") {

  /** The mode that the program is in: Calculation, z->fz mapping, z->|fz| mapping, or Re(fz). */
  var mode = CALC

  private val display: Display = new Display(12)
  private var equalsButton: JButton = null
  private var zButton: JButton = null

  var variable = 'z'

  private var cW: ComplexWorld = null
  private var zW: ZWorld = null
  private var fzW: FzWorld = null
  private var modfzW: ThreeDWorld = null
  private var refxW: RefxWorld = null
  private var f: Syntax = null
  private var inAnApplet = true

  init()

  private def init() = {
    buildFrame()
    buildButtons()
    setJMenuBar(new Menus(this))
    pack()
    becomeVisible()
  }

  private def buildFrame() = {
    setResizable(false)
    Complex.setPrecision(4)
  }

  private def buildButtons() = {

    val layout = new GridBagLayout
    setLayout(layout)

    display.addKeyListener(new MyKeyListener(this))

    val constraints = new GridBagConstraints
    constraints.gridx = 0
    constraints.gridy = 0
    constraints.gridwidth = 5
    constraints.gridheight = 3
    constraints.weightx = 1
    constraints.weighty = 1
    constraints.fill = 1

    layout.setConstraints(display, constraints)
    add(display)

    val listener: ActionListener = e => {
        if (mode == CALC)
          eraseOldResult()
        val command = e.getActionCommand
        display.paste(command)
        display.requestFocus()
        if (mode != CALC)
          functionChange()
    }

    /** Makes a button and adds it. */
    def bx(label: String, gridx: Int, gridy: Int, gridheight: Int, listener: ActionListener): JButton = {
      val button = new JButton(label)
      button.addActionListener(listener)

      val constraints = new GridBagConstraints
      constraints.gridwidth = 1
      constraints.gridheight = 1
      constraints.weightx = 1
      constraints.weighty = 1
      constraints.fill = 1
      constraints.gridx = gridx
      constraints.gridy = gridy
      constraints.gridheight = gridheight

      layout.setConstraints(button, constraints)
      add(button)
      button
    }

    bx("!", 0, 3, 1, listener)

    bx("del", 3, 3, 1, _ => {
        if (mode == CALC)
          eraseOldResult()
        display.delete()
        display.requestFocus()
        if (mode != CALC)
          functionChange()
    })

    bx("C", 4, 3, 1, _ => {
        display.clearAll()
        if (mode != CALC)
          display.prepend("f(" + variable + ") = ")
        display.requestFocus()
    })

    bx("sinh", 0, 4, 1, listener)
    bx("cosh", 1, 4, 1, listener)
    bx("tanh", 2, 4, 1, listener)
    bx("conj", 3, 4, 1, listener)
    bx("opp", 4, 4, 1, listener)
    bx("sin", 0, 5, 1, listener)
    bx("cos", 1, 5, 1, listener)
    bx("tan", 2, 5, 1, listener)
    bx("Re", 3, 5, 1, listener)
    bx("Im", 4, 5, 1, listener)
    bx("ln", 0, 7, 1, listener)
    bx("exp", 1, 7, 1, listener)
    bx("^", 2, 7, 1, listener)
    bx("mod", 3, 7, 1, listener)
    bx("arg", 4, 7, 1, listener)
    bx("i", 0, 8, 1, listener)
    bx("e", 1, 8, 1, listener)
    bx("π", 2, 8, 1, listener)
    bx("(", 3, 8, 1, listener)
    bx(")", 4, 8, 1, listener)
    bx("7", 0, 9, 1, listener)
    bx("8", 1, 9, 1, listener)
    bx("9", 2, 9, 1, listener)
    bx("*", 3, 9, 1, listener)
    bx("/", 4, 9, 1, listener)
    bx("4", 0, 10, 1, listener)
    bx("5", 1, 10, 1, listener)
    bx("6", 2, 10, 1, listener)
    bx("+", 3, 10, 1, listener)
    bx("-", 4, 10, 1, listener)
    bx("1", 0, 11, 1, listener)
    bx("2", 1, 11, 1, listener)
    bx("3", 2, 11, 1, listener)

    zButton = bx("z", 3, 11, 1, _ => {
        display.paste(variable)
        display.requestFocus()
        if (mode != CALC)
          functionChange()
    })

    equalsButton = bx("=", 4, 11, 2, _ => {
        doEquals()
        display.requestFocus()
    })

    bx("0", 0, 12, 1, listener)
    bx(".", 1, 12, 1, listener)
    bx("∞", 2, 12, 1, listener)
  }

  private def becomeVisible() = {
    addWindowListener(new WindowAdapter() {
      override def windowClosing(windowevent: WindowEvent): Unit = quit()
    })
    setMode(CALC)
    pack()
    locate()
    setVisible(true)
    display.requestFocus()
  }

  /** Find a good place on the screen for the new Calculator frame. */
  private def locate() = setLocationByPlatform(true)

  /** Adds a complex number to the display. */
  final private[calculator] def add(c: Complex) = {
    val s = "(" + c + ")"
    display.paste(s)
  }

  /** Gets the expression from the display, parses it, evaluates it,
    * and append the result to the display. */
  private[calculator] def doEquals() = {
    val s = display.getText
    display.append(" = ")
    try {
      val f = Syntax.parse(s)
      val c = f(null)
      display.append(c.toString)
      cW.add(c)
    } catch {
      case e: Exception =>
        e.printStackTrace()
    }
  }

  private[calculator] def eraseOldResult() = {
    var s = display.getText
    val i = s.lastIndexOf('=')
    if (i != -1) {
      var j = display.getCaretPosition
      s = s.substring(0, i)
      val k = s.length
      display.setText(s)
      j = if (j >= k) k
      else j
      display.setCaretPosition(j)
    }
  }

  /** Callback: f(z)=... has changed. */
  private[calculator] def functionChange() = try {
    val s = Syntax.stripBlanks(display.getText)
    if (s.startsWith("f(" + variable + ")=")) {
      f = Syntax.parse(s.substring(5))
      mode match {
        case MODFZ => modfzW.functionChange(f)
        case REFX => refxW.functionChange(f)
        case FZ => fzW.functionChange(f)
        case _ =>
      }
    }
  } catch {
    case _ex: ParseException =>
  }

  private[calculator] def getSquare = zW.getSquare

  def quit(): Unit = if (inAnApplet) {
    dispose()
    if (cW != null) cW.dispose()
    if (zW != null) zW.dispose()
    if (fzW != null) fzW.dispose()
    if (modfzW != null) modfzW.dispose()
    if (refxW != null) refxW.dispose()
  }
  else System.exit(0)

  /** Sets the mode, opening and closing windows. */
  private[calculator] def setMode(m: Mode) = {
    m match {

      case CALC =>
        Complex.setArgPrincipal()
        display.clearAll()
        equalsButton.setEnabled(true)
        zButton.setEnabled(false)
        complexWorld()
        disposeZWorld()
        disposeFZWorld()
        disposeModFZWorld()
        disposeReFXWorld()

      case FZ =>
        Complex.setArgContinuous()
        variable = 'z'
        if (mode eq REFX) display.replace('x', 'z')
        else if (mode eq CALC) {
          display.clearAll()
          display.prepend("f(z) = ")
        }
        equalsButton.setEnabled(false)
        zButton.setEnabled(true)
        zButton.setText("z")
        disposeComplexWorld()
        zWorld()
        zW.setMode(FZ)
        fzWorld()
        fzW.setzWorld(zW)
        zW.setfzWorld(fzW)
        disposeModFZWorld()
        disposeReFXWorld()

      case MODFZ =>
        Complex.setArgContinuous()
        variable = 'z'
        if (mode eq REFX) display.replace('x', 'z')
        else if (mode eq CALC) {
          display.clearAll()
          display.prepend("f(z) = ")
        }
        equalsButton.setEnabled(false)
        zButton.setEnabled(true)
        zButton.setText("z")
        disposeComplexWorld()
        zWorld()
        zW.setMode(MODFZ)
        disposeFZWorld()
        modFZWorld()
        zW.setmodfzWorld(modfzW)
        disposeReFXWorld()

      case REFX =>
        Complex.setArgContinuous()
        variable = 'x'
        if (mode eq CALC) {
          display.clearAll()
          display.prepend("f(x) = ")
        }
        else display.replace('z', 'x')
        equalsButton.setEnabled(false)
        zButton.setEnabled(true)
        zButton.setText("x")
        disposeComplexWorld()
        disposeZWorld()
        disposeFZWorld()
        disposeModFZWorld()
        refxWorld()
    }

    mode = m
    functionChange()
  }

  private def complexWorld() = if (cW == null) cW = new ComplexWorld(this)

  private def disposeComplexWorld() = if (cW != null) {
    cW.dispose()
    cW = null
  }

  private def zWorld() = if (zW == null) zW = new ZWorld(this)

  private def disposeZWorld() = if (zW != null) {
    zW.dispose()
    zW = null
  }

  private def fzWorld() = if (fzW == null) fzW = new FzWorld(this)

  private def disposeFZWorld() = if (fzW != null) {
    fzW.dispose()
    fzW = null
  }

  private def modFZWorld() = if (modfzW == null) modfzW = new ThreeDWorld(this)

  private def disposeModFZWorld() = if (modfzW != null) {
    modfzW.dispose()
    modfzW = null
  }

  private def refxWorld() = if (refxW == null) refxW = new RefxWorld(this)

  private def disposeReFXWorld() = if (refxW != null) {
    refxW.dispose()
    refxW = null
  }

  private[calculator] def delete() = display.delete()
  private[calculator] def clearAll() = display.clearAll()
  private[calculator] def prepend(s: String) = display.prepend(s)
  private[calculator] def paste(c: Char) = display.paste(c)
}