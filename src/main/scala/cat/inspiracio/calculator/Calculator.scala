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
import cat.inspiracio.display.Editor
import cat.inspiracio.geometry.Point2
import cat.inspiracio.geometry.Point2._
import cat.inspiracio.parsing.Expression
import cat.inspiracio.parsing.Expression.parse

import scala.swing._
import scala.swing.event.WindowClosing

/** The Calculator application. This is a frame, the main window. */
object Calculator {
  /** Run the Calculator as stand-alone application. */
  def main(args: Array[String]) { new Calculator }
}

final class Calculator() extends MainFrame {
  visible = false //make visible after locate()
  title = "Complex Calculator"
  resizable = false
  menuBar = new Menus(this)

  /** For all formatting of complex numbers that will be visible to the user */
  private val cf = new ComplexFormat
  cf.minimumFractionDigits = 0
  cf.maximumFractionDigits = 3

  /** The mode that the program is in: Calculation, z->fz mapping, z->|fz| mapping, or Re(fz). */
  private var _mode = CALC

  private[calculator] val display: Display = new Display(this,12)
  val editor = new Editor(this)

  private val equalsButton: Button = button("=", "evaluate"){ paste("=") }
  private val zButton: Button = button("z"){ paste(variable) }

  private var variable = 'z'

  private var cW: ComplexWorld = null
  private[calculator] var zW: ZWorld = null
  private[calculator] var fzW: FzWorld = null
  private[calculator] var modfzW: ThreeDWorld = null
  private var refxW: RefxWorld = null

  private[calculator] var f: Expression = null

  init()

  private def button(label: String, tooltip: String)(op: => Unit): Button = {
    val b = Button(label){op}
    b.tooltip = tooltip
    b
  }
  private def button(label: String)(op: => Unit): Button = button(label, label){op}

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

      var y = 0
      layout(editor) = constraints(0, y, 5, 3)

      y += 3
      layout(display) = constraints(0, y, 5, 3)

      y += 3
      layout(button("!", "factorial"){ paste("!") }) = constraints(0, y)
      layout(button("del", "delete one character"){ delete() }) = constraints(3, y)
      layout(button("C", "clear all"){ clear() }) = constraints(4, y)

      y += 1
      layout(button("sinh"){ paste("sinh") }) = constraints(0, y)
      layout(button("cosh"){ paste("cosh") }) = constraints(1, y)
      layout(button("tanh"){ paste("tanh") }) = constraints(2, y)
      layout(button("conj", "complex conjugate"){ paste("conj") }) = constraints(3, y)
      layout(button("opp", "opposite on Riemann sphere"){ paste("opp") }) = constraints(4, y)

      y += 1
      layout(button("sin"){ paste("sin") }) = constraints(0, y)
      layout(button("cos"){ paste("cos") }) = constraints(1, y)
      layout(button("tan"){ paste("tan") }) = constraints(2, y)
      layout(button("Re", "Real part"){ paste("Re") }) = constraints(3, y)
      layout(button("Im", "Imaginary part"){ paste("Im") }) = constraints(4, y)

      y += 2
      layout(button("ln", "natural logarithm"){ paste("ln") }) = constraints(0, y)
      layout(button("exp", "e raised by"){ paste("exp") }) = constraints(1, y)
      layout(button("\\", "exponentiation"){ paste("\\") }) = constraints(2, y)
      layout(button("|", "modulus"){ paste("|") }) = constraints(3, y)
      layout(button("arg", "argument or angle"){ paste("arg") }) = constraints(4, y)

      y += 1
      layout(button("i"){ paste("i") }) = constraints(0, y)
      layout(button("e"){ paste("e") }) = constraints(1, y)
      layout(button("π"){ paste("π") }) = constraints(2, y)
      layout(button("("){ paste("(") }) = constraints(3, y)
      layout(button(")"){ paste(")") }) = constraints(4, y)

      y += 1
      layout(button("7"){ paste("7") }) = constraints(0, y)
      layout(button("8"){ paste("8") }) = constraints(1, y)
      layout(button("9"){ paste("9") }) = constraints(2, y)
      layout(button("*", "multiplication"){ paste("*") }) = constraints(3, y)
      layout(button("/", "division"){ paste("/") }) = constraints(4, y)

      y += 1
      layout(button("4"){ paste("4") }) = constraints(0, y)
      layout(button("5"){ paste("5") }) = constraints(1, y)
      layout(button("6"){ paste("6") }) = constraints(2, y)
      layout(button("+", "addition"){ paste("+") }) = constraints(3, y)
      layout(button("-", "subtraction"){ paste("-") }) = constraints(4, y)

      y += 1
      layout(button("1"){ paste("1") }) = constraints(0, y)
      layout(button("2"){ paste("2") }) = constraints(1, y)
      layout(button("3"){ paste("3") }) = constraints(2, y)
      layout(zButton) = constraints(3, y)
      layout(equalsButton) = constraints(4, y, gridheight = 2)

      y += 1
      layout(button("0"){ paste("0") }) = constraints(0, y)
      layout(button(".", "decimal point"){ paste(".") }) = constraints(1, y)
      layout(button("∞", "infinity"){ paste("∞") }) = constraints(2, y)
      layout(button(" ", "space"){ paste(" ") }) = constraints(3, y)
    }
  }

  private def becomeVisible() = {
    reactions += {
      case WindowClosing(source) => quit()
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
    val s = "(" + cf(c) + ")"
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
      val s = c.toString  // maybe should depend on configured precision
      display.append(s)
      editor.show(f)
      cW.add(c)
    } catch {
      case e: Exception => e.printStackTrace()
    }
  }

  /** Event listener: f(z)=... has changed. */
  private[calculator] def functionChanged() = try {
    val text = stripBlanks(display.text)
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

  /** removes all white space from the string */
  private def stripBlanks(s: String): String = {
    val builder = new StringBuilder(s.length)
    for (c <- s)
      if (!Character.isWhitespace(c))
        builder.append(c)
    builder.toString
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
    equalsButton.tooltip = "evaluate"
    zButton.enabled = false
    zButton.text = "z"
    zButton.tooltip = "z"
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
    zButton.tooltip = "z"
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
    zButton.tooltip = "z"
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
    zButton.tooltip = "x"
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