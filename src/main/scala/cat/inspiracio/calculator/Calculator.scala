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
import java.awt.event.{ActionListener, WindowAdapter, WindowEvent}
import java.text.ParseException
import java.lang.Math.min
import java.util.prefs.Preferences

import cat.inspiracio.calculator.Mode._
import cat.inspiracio.complex._
import cat.inspiracio.geometry.Point2
import cat.inspiracio.parsing.Syntax
import cat.inspiracio.parsing.Syntax.parse
import javax.swing._

/** The Calculator application.
  * This is a frame, the main window.
  * This class has the main method. */
object Calculator {

  /** Run the Calculator as stand-alone application. */
    def main(args: Array[String]): Unit = {
      val calculator = new Calculator
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
  private[calculator] var zW: ZWorld = null
  private[calculator] var fzW: FzWorld = null
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

    //listener for a 'normal' button
    val listener: ActionListener = e => paste(e.getActionCommand)

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
    bx("del", 3, 3, 1, _ => delete() )
    bx("C", 4, 3, 1, _ => clear() )
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
    zButton = bx("z", 3, 11, 1, e => paste(e.getActionCommand) )
    equalsButton = bx("=", 4, 11, 2, e => paste(e.getActionCommand) )
    bx("0", 0, 12, 1, listener)
    bx(".", 1, 12, 1, listener)
    bx("∞", 2, 12, 1, listener)
  }

  private def becomeVisible() = {
    addWindowListener(new WindowAdapter() {
      override def windowClosing(windowevent: WindowEvent): Unit = quit()
    })
    pack()
    locate()
    setVisible(true)
    setMode(CALC)
    display.requestFocus()
  }

  private def preferences = Preferences.userNodeForPackage(getClass).node(getClass.getSimpleName)

  /** Locates the Calculator where it was last time the program was run.
    * By default near effective topleft. */
  private def locate() = {

    // effective topleft on ubuntu standard: x=67 y=28
    val p = preferences
    val x: Int = p.getInt("x", 67 + 10 )
    val y: Int = p.getInt("y", 28 + 10 )
    setLocation( x , y )
  }

  /** Adds a complex number to the display. */
  final private[calculator] def add(c: Complex) = {
    val s = "(" + c + ")"
    display.paste(s)
  }

  /** Gets the expression from the display, parses it, evaluates it,
    * and append the result to the display. */
  private[calculator] def doEquals() = {
    val text = display.getText
    display.append(" = ")
    try {
      val f = parse(text)
      val c = f(null)
      display.append(c.toString)
      cW.add(c)
    } catch {
      case e: Exception =>
        e.printStackTrace()
    }
  }

  private[calculator] def eraseOldResult() = {
    var text = display.getText
    val equals = text.lastIndexOf('=')
    if (equals != -1) {
      var caret = display.getCaretPosition
      text = text.substring(0, equals)
      display.setText(text)
      caret = min(caret, text.length)
      display.setCaretPosition(caret)
    }
  }

  /** Event listener: f(z)=... has changed. */
  private[calculator] def functionChanged() = try {
    val text = Syntax.stripBlanks(display.getText)
    if (text.startsWith("f(" + variable + ")=")) {
      f = parse(text.substring(5))
      mode match {
        case MODFZ => modfzW.functionChanged(f)
        case REFX => refxW.functionChanged(f)
        case FZ => fzW.functionChanged(f)
        case _ =>
      }
    }
  } catch {
    case _ex: ParseException =>
  }

  private[calculator] def getSquare = zW.getSquare

  def quit(): Unit = if (inAnApplet) {

    //save location
    val p = preferences
    val Point2(x,y) = getLocationOnScreen
    p.putInt("x", x)
    p.putInt("y", y)

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
        display.clear()
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
        if (mode == REFX)
          display.replace('x', 'z')
        else if (mode == CALC) {
          display.clear()
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
        if (mode == REFX) display.replace('x', 'z')
        else if (mode == CALC) {
          display.clear()
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
        if (mode == CALC) {
          display.clear()
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
    functionChanged()
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

  /** event listener for DELETE button */
  private def delete(): Unit = {
    if (mode == CALC)
      eraseOldResult()
    display.delete()
    display.requestFocus()
    if (mode != CALC)
      functionChanged()
  }

  /** Event listener for CLEAR button. */
  private def clear(): Unit = {
    display.clear()
    if (mode != CALC)
      display.prepend("f(" + variable + ") = ")
    display.requestFocus()
  }

  private[calculator] def prepend(s: String) = display.prepend(s)

  /** Inserts a character to the calculator.
    * Called from MyKeyListener. */
  private[calculator] def paste(c: Char): Unit = paste(c.toString)

  /** Inserts a string to the calculator.
    * Called from button listeners. */
  private[calculator] def paste(s: String): Unit = {

    if ( mode == CALC )
      eraseOldResult()

    if (s == "=" && mode == CALC )
      doEquals()
    else
      display.paste(s)

    display.requestFocus()  //Because the focus may stay on the button

    if ( mode != CALC )
      functionChanged()
  }

}