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
import java.awt.event.{ActionListener, WindowAdapter, WindowEvent}
import java.text.ParseException
import java.util.prefs.Preferences

import cat.inspiracio.calculator.Mode._
import cat.inspiracio.complex._
import cat.inspiracio.geometry.Point2
import cat.inspiracio.parsing.Syntax
import cat.inspiracio.parsing.Syntax.parse
//import javax.swing._
import scala.swing._

/** The Calculator application. This is a frame, the main window.
  * Run the Calculator as stand-alone application. */
object Calculator extends App {
  new Calculator
}

final class Calculator() extends MainFrame {
  title = "Complex Calculator"

  /** The mode that the program is in: Calculation, z->fz mapping, z->|fz| mapping, or Re(fz). */
  private[calculator] var mode = CALC

  private[calculator] val display: Display = new Display(12)
  private var equalsButton: Button = null
  private var zButton: Button = null

  private var variable = 'z'

  private var cW: ComplexWorld = null
  private[calculator] var zW: ZWorld = null
  private[calculator] var fzW: FzWorld = null
  private[calculator] var modfzW: ThreeDWorld = null
  private var refxW: RefxWorld = null

  private[calculator] var f: Syntax = null

  init()

  private def init() = {
    buildFrame()
    buildButtons()
    menuBar = new Menus(this)
    pack()
    becomeVisible()
  }

  private def buildFrame() = {
    resizable = false
  }

  private def buildButtons() = {
    contents = new GridBagPanel{
      def constraints(x: Int, y: Int,
                      gridwidth: Int = 1, gridheight: Int = 1,
                      weightx: Double = 1, weighty: Double = 1,
                      fill: GridBagPanel.Fill.Value = GridBagPanel.Fill.None): Constraints = {
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

      //display.addKeyListener(new MyKeyListener(Calculator.this))
      println(GridBagPanel.Fill.None)//1?
      layout(display) = constraints(0, 0, 5, 3)//fill=1

      //listener for a 'normal' button
      val paster: ActionListener = e => paste(e.getActionCommand)

      /** Makes a button and adds it. */
      def bx(label: String, x: Int, y: Int, height: Int, listener: Action): Button = {
        val button = new Button(label)
        button.text = label
        button.action = listener

        val constraints = new GridBagPanel.Constraints
        constraints.gridwidth = 1
        constraints.gridheight = 1
        constraints.weightx = 1
        constraints.weighty = 1
        constraints.fill = 1
        constraints.gridx = x
        constraints.gridy = y
        constraints.gridheight = height

        layout(button) = constraints
        button
      }

      bx("!", 0, 3, 1, paster)
      bx("del", 3, 3, 1, _ => delete() )
      bx("C", 4, 3, 1, _ => clear() )
      bx("sinh", 0, 4, 1, paster)
      bx("cosh", 1, 4, 1, paster)
      bx("tanh", 2, 4, 1, paster)
      bx("conj", 3, 4, 1, paster)
      bx("opp", 4, 4, 1, paster)
      bx("sin", 0, 5, 1, paster)
      bx("cos", 1, 5, 1, paster)
      bx("tan", 2, 5, 1, paster)
      bx("Re", 3, 5, 1, paster)
      bx("Im", 4, 5, 1, paster)
      bx("ln", 0, 7, 1, paster)
      bx("exp", 1, 7, 1, paster)
      bx("^", 2, 7, 1, paster)
      bx("mod", 3, 7, 1, paster)
      bx("arg", 4, 7, 1, paster)
      bx("i", 0, 8, 1, paster)
      bx("e", 1, 8, 1, paster)
      bx("π", 2, 8, 1, paster)
      bx("(", 3, 8, 1, paster)
      bx(")", 4, 8, 1, paster)
      bx("7", 0, 9, 1, paster)
      bx("8", 1, 9, 1, paster)
      bx("9", 2, 9, 1, paster)
      bx("*", 3, 9, 1, paster)
      bx("/", 4, 9, 1, paster)
      bx("4", 0, 10, 1, paster)
      bx("5", 1, 10, 1, paster)
      bx("6", 2, 10, 1, paster)
      bx("+", 3, 10, 1, paster)
      bx("-", 4, 10, 1, paster)
      bx("1", 0, 11, 1, paster)
      bx("2", 1, 11, 1, paster)
      bx("3", 2, 11, 1, paster)
      zButton = bx("z", 3, 11, 1, paster)
      equalsButton = bx("=", 4, 11, 2, paster)
      bx("0", 0, 12, 1, paster)
      bx(".", 1, 12, 1, paster)
      bx("∞", 2, 12, 1, paster)

      //layout(myComponent) = myConstraints
    }
  }

  private def becomeVisible() = {
    addWindowListener(new WindowAdapter() {
      override def windowClosing(windowevent: WindowEvent): Unit = quit()
    })
    pack()
    locate()
    visible = true
    mode_=(mode)
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

    val text = p.get("text", "")
    paste(text)

    val m = p.get("mode", "CALC")
    mode = Mode.withName(m)
  }

  /** Adds a complex number to the display. */
  final private[calculator] def add(c: Complex) = {
    //Could improve: parenthesis only when necessary
    val s = "(" + c + ")"
    display.paste(s)
  }

  /** Gets the expression from the display, parses it, evaluates it,
    * and appends the result to the display. */
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

  /** Event listener: f(z)=... has changed. */
  private[calculator] def functionChanged() = try {
    val text = Syntax.stripBlanks(display.getText)
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

      p.put("text", display.getText)
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
  private[calculator] def mode_=(m: Mode) = {
    m match {
      case CALC => calc()
      case FZ => fz()
      case MODFZ => modfz()
      case REFX => refx()
    }
    mode = m
    functionChanged()

    menuBar.asInstanceOf[Menus].select(mode)
  }

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
    if (mode == REFX)
      display.replace('x', 'z')
    else if (mode == CALC) {
      display.clear()
      display.prepend("f(z) = ")
    }
    equalsButton.enabled = false
    zButton.enabled = true
    zButton.text = "z"
    disposeComplexWorld()
    zWorld()
    zW.setMode(FZ)
    fzWorld()
    disposeModFZWorld()
    disposeReFXWorld()
  }

  private def modfz() = {
    Complex.setArgContinuous()
    variable = 'z'
    if (mode == REFX) display.replace('x', 'z')
    else if (mode == CALC) {
      display.clear()
      display.prepend("f(z) = ")
    }
    equalsButton.enabled = false
    zButton.enabled = true
    zButton.text = "z"
    disposeComplexWorld()
    zWorld()
    zW.mode = MODFZ
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
    else display.replace('z', 'x')
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
    if (mode == CALC)
      display.eraseOldResult()
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

    if ( mode != CALC )
      functionChanged()
  }

}