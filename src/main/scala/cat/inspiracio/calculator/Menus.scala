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

import java.awt.Toolkit
import java.awt.event.KeyEvent

import cat.inspiracio.calculator.Mode._
import scala.swing._

/** A menu bar for the Calculator. */
final class Menus private[calculator](var calculator: Calculator) extends MenuBar {

  private val precisionListener = null
  private val mask = Toolkit.getDefaultToolkit.getMenuShortcutKeyMask

  init()

  private def init()= {
    file()
    mode()
  }

  private def file(): Unit = {
    val menu = new Menu("File")
    //menu.mnemonic = KeyEvent.VK_F

    val miAbout = new MenuItem("About ..."){ new About(calculator) }
    menu.contents += miAbout

    val miQuit = new MenuItem("Quit"){ calculator.quit() }
    //miQuit.mnemonic = KeyEvent.VK_Q
    //val CtrlQ = KeyStroke.getKeyStroke(KeyEvent.VK_Q, mask)
    //miQuit.accelerator = CtrlQ
    menu.contents += miQuit

    contents += menu
  }

  private def mode(): Unit = {
    val menu = new Menu("Mode")

    //selected = true
    val miCalc = new RadioMenuItem("Calculate"){ calculator.mode_=(CALC) }
    val miFz = new RadioMenuItem("z -> f(z)"){ calculator.mode_=(FZ) }
    val miModFz = new RadioMenuItem("z -> |f(z)|"){ calculator.mode_=(MODFZ) }
    val miReFx = new RadioMenuItem("x -> Re(f(x))"){ calculator.mode_=(REFX) }

    menu.contents += miCalc
    menu.contents += miFz
    menu.contents += miModFz
    menu.contents += miReFx

    val group = new ButtonGroup
    group.buttons += miCalc
    group.buttons += miFz
    group.buttons += miModFz
    group.buttons += miReFx

    contents += menu
  }

  private[calculator] def select(m: Mode)= {
    val index = m match {
      case CALC => 0
      case FZ => 1
      case MODFZ => 2
      case REFX => 3
    }
    val menu = menus(1)
    //val item = menu.getItem(index)
    //item.setSelected(true)
  }

}