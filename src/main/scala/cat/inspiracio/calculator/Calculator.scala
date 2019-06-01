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

import java.text.ParseException
import java.util.prefs.Preferences

import cat.inspiracio.calculator.Mode._
import cat.inspiracio.complex._
import cat.inspiracio.geometry.Point2
import cat.inspiracio.geometry.Point2._
import cat.inspiracio.parsing.Syntax.parse

import scala.swing._
import scala.swing.event.WindowClosing

/** The Calculator application. This is a frame, the main window.
  * Run the Calculator as stand-alone application. */
object Calculator {
  def main(args: Array[String]) { new Calculator }
}

final class Calculator() extends MainFrame {
  visible = false //make visible after locate()
  title = "Complex Calculator"
  resizable = false
  menuBar = new Menus(this)

  /** The mode that the program is in: Calculation, z->fz mapping, z->|fz| mapping, or Re(fz). */
  private var _mode = CALC

  private[calculator] val display: Display = new Display(this,12)
  private val equalsButton: Button = Button("="){ paste("=") }
  private val zButton: Button = Button("z"){ paste(variable) }

  private var variable = 'z'

  private var cW: ComplexWorld = null
  private[calculator] var zW: ZWorld = null
  private[calculator] var fzW: FzWorld = null
  private[calculator] var modfzW: ThreeDWorld = null
  private var refxW: RefxWorld = null

  private[calculator] var f: cat.inspiracio.parsing.Syntax = null

  init()

  private def init() = {
    buildButtons()
    pack()
    becomeVisible()
  }

  private def buildButtons() = {

    contents = new GridBagPanel{

      def constraints(x: Int,
                      y: Int,
                      gridwidth: Int = 1,
                      gridheight: Int = 1,
                      weightx: Double = 1,
                      weighty: Double = 1,
                      fill: GridBagPanel.Fill.Value = GridBagPanel.Fill.Both
                     ): Constraints = {
        val c = new Constraints
        c.gridx = x
        c.gridy = y
        c.gridwidth = gridwidth
        c.gridheight = gridheight
        c.weightx = weightx
        c.weighty = weighty
        c.fill = fill
        c
      }

      //display.reactions += new MyKeyListener(Calculator.this)
      layout(display) = constraints(0, 0, 5, 3)

      layout(Button("!"){ paste("!") }) = constraints(0, 3)
      layout(Button("del"){ delete() }) = constraints(3, 3)
      layout(Button("C"){ clear() }) = constraints(4, 3)
      layout(Button("sinh"){ paste("sinh") }) = constraints(0, 4)
      layout(Button("cosh"){ paste("cosh") }) = constraints(1, 4)
      layout(Button("tanh"){ paste("tanh") }) = constraints(2, 4)
      layout(Button("conj"){ paste("conj") }) = constraints(3, 4)
      layout(Button("opp"){ paste("opp") }) = constraints(4, 4)
      layout(Button("sin"){ paste("sin") }) = constraints(0, 5)
      layout(Button("cos"){ paste("cos") }) = constraints(1, 5)
      layout(Button("tan"){ paste("tan") }) = constraints(2, 5)
      layout(Button("Re"){ paste("Re") }) = constraints(3, 5)
      layout(Button("Im"){ paste("Im") }) = constraints(4, 5)
      layout(Button("ln"){ paste("ln") }) = constraints(0, 7)
      layout(Button("exp"){ paste("exp") }) = constraints(1, 7)
      layout(Button("^"){ paste("^") }) = constraints(2, 7)
      layout(Button("mod"){ paste("mod") }) = constraints(3, 7)
      layout(Button("arg"){ paste("arg") }) = constraints(4, 7)
      layout(Button("i"){ paste("i") }) = constraints(0, 8)
      layout(Button("e"){ paste("e") }) = constraints(1, 8)
      layout(Button("π"){ paste("π") }) = constraints(2, 8)
      layout(Button("("){ paste("(") }) = constraints(3, 8)
      layout(Button(")"){ paste(")") }) = constraints(4, 8)
      layout(Button("7"){ paste("7") }) = constraints(0, 9)
      layout(Button("8"){ paste("8") }) = constraints(1, 9)
      layout(Button("9"){ paste("9") }) = constraints(2, 9)
      layout(Button("*"){ paste("*") }) = constraints(3, 9)
      layout(Button("/"){ paste("/") }) = constraints(4, 9)
      layout(Button("4"){ paste("4") }) = constraints(0, 10)
      layout(Button("5"){ paste("5") }) = constraints(1, 10)
      layout(Button("6"){ paste("6") }) = constraints(2, 10)
      layout(Button("+"){ paste("+") }) = constraints(3, 10)
      layout(Button("-"){ paste("-") }) = constraints(4, 10)
      layout(Button("1"){ paste("1") }) = constraints(0, 11)
      layout(Button("2"){ paste("2") }) = constraints(1, 11)
      layout(Button("3"){ paste("3") }) = constraints(2, 11)

      layout(zButton) = constraints(3, 11)

      layout(equalsButton) = constraints(4, 11, gridheight = 2)

      layout(Button("0"){ paste("0") }) = constraints(0, 12)
      layout(Button("."){ paste(".") }) = constraints(1, 12)
      layout(Button("∞"){ paste("∞") }) = constraints(2, 12)
    }
  }

  private def becomeVisible() = {
    reactions += {
      case WindowClosing(source) =>
        quit()
    }
    pack()  //needed?
    locate()
    mode = _mode  //trigger windows
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
    location = (x, y)

    val text = p.get("text", "")
    paste(text)

    val m = p.get("mode", "CALC")
    _mode = Mode.withName(m)  //direct set

    visible = true
  }

  /** Adds a complex number to the display. */
  final private[calculator] def add(c: Complex) = {
    //Could improve: parenthesis only when necessary
    val s = "(" + c + ")"   //XXX precision
    display.paste(s)
  }

  /** Gets the expression from the display, parses it, evaluates it,
    * and appends the result to the display. */
  private[calculator] def doEquals() = {
    val text = display.text
    display.append(" = ")
    try {
      val f = parse(text)
      val c = f(null)
      display.append(c.toString)
      cW.add(c)
    } catch {
      case e: Exception => e.printStackTrace()
    }
  }

  /** Event listener: f(z)=... has changed. */
  private[calculator] def functionChanged() = try {
    val text = cat.inspiracio.parsing.Syntax.stripBlanks(display.text)
    if (text.startsWith("f(" + variable + ")=")) {
      f = parse(text.substring(5))
      mode match {
        case MODFZ => modfzW.functionChanged()
        case REFX => refxW.functionChanged()
        case FZ => fzW.functionChanged()
        case _ =>
      }
    }
  } catch {
    case _ex: ParseException =>
  }

  private[calculator] def quit(): Unit = {
    if(visible){
      val p = preferences
      val Point2(x,y) = locationOnScreen
      p.putInt("x", x)
      p.putInt("y", y)

      p.put("text", display.text)
      p.put("mode", mode.toString)
    }
    dispose()
    if (cW != null) cW.dispose()
    if (zW != null) zW.dispose()
    if (fzW != null) fzW.dispose()
    if (modfzW != null) modfzW.dispose()
    if (refxW != null) refxW.dispose()
  }

  /** Sets the mode, opening and closing windows. */
  def mode_=(m: Mode): Unit = {
    m match {
      case CALC => calc()
      case FZ => fz()
      case MODFZ => modfz()
      case REFX => refx()
    }
    _mode = m
    functionChanged()

    menuBar.asInstanceOf[Menus].select(m)
  }

  def mode: Mode = _mode

  private def calc() = {
    Complex.setArgPrincipal()
    display.clear()
    equalsButton.enabled = true
    zButton.enabled = false
    complexWorld()
    disposeZWorld()
    disposeFZWorld()
    disposeModFZWorld()
    disposeReFXWorld()
  }

  private def fz() = {
    Complex.setArgContinuous()
    variable = 'z'
    if (_mode == REFX)
      display.replace('x', 'z')
    else if (_mode == CALC) {
      display.clear()
      display.prepend("f(z) = ")
    }
    equalsButton.enabled = false
    zButton.enabled = true
    zButton.text = "z"
    disposeComplexWorld()
    zWorld()
    zW.mode_=(FZ)
    fzWorld()
    disposeModFZWorld()
    disposeReFXWorld()
  }

  private def modfz() = {
    Complex.setArgContinuous()
    variable = 'z'
    if (_mode == REFX)
      display.replace('x', 'z')
    else if (_mode == CALC) {
      display.clear()
      display.prepend("f(z) = ")
    }
    equalsButton.enabled = false
    zButton.enabled = true
    zButton.text = "z"
    disposeComplexWorld()
    zWorld()
    zW.mode_=(MODFZ)
    disposeFZWorld()
    modFZWorld()
    disposeReFXWorld()
  }

  private def refx() = {
    Complex.setArgContinuous()
    variable = 'x'
    if (mode == CALC) {
      display.clear()
      display.prepend("f(x) = ")
    }
    else
      display.replace('z', 'x')
    equalsButton.enabled = false
    zButton.enabled = true
    zButton.text = "x"
    disposeComplexWorld()
    disposeZWorld()
    disposeFZWorld()
    disposeModFZWorld()
    refxWorld()
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
    if (_mode == CALC)
      display.eraseOldResult()
    display.delete()
    display.requestFocus()
    if (_mode != CALC)
      functionChanged()
  }

  /** Event listener for CLEAR button. */
  private def clear(): Unit = {
    display.clear()
    if (mode != CALC)
      display.prepend("f(" + variable + ") = ")
    display.requestFocus()
  }

  /** Inserts a character to the calculator.
    * Called from MyKeyListener. */
  private[calculator] def paste(c: Char): Unit = paste(c.toString)

  /** Inserts a string to the calculator.
    * Called from button listeners. */
  private[calculator] def paste(s: String): Unit = {

    if ( mode == CALC )
      display.eraseOldResult()

    if (s == "=" && mode == CALC )
      doEquals()
    else
      display.paste(s)

    display.requestFocus()  //Because the focus may stay on the button

    if ( _mode != CALC )
      functionChanged()
  }

}