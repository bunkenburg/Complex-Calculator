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
import javax.swing.KeyStroke

import scala.swing._
import scala.swing.event.Key

/** A menu bar for the Calculator. */
final class Menus private[calculator](calculator: Calculator) extends MenuBar {

    val meFile = new Menu("File"){
      mnemonic = Key.F
    }

    val acAbout = Action("About ..."){
      new About(calculator)
    }
    meFile.contents += new MenuItem(acAbout)

    val acQuit = Action("Quit"){
      calculator.quit()
    }
    acQuit.mnemonic = java.awt.event.KeyEvent.VK_Q
    val mask = Toolkit.getDefaultToolkit.getMenuShortcutKeyMask
    val CtrlQ = KeyStroke.getKeyStroke(KeyEvent.VK_Q, mask)
    acQuit.accelerator = Option(CtrlQ)
    meFile.contents += new MenuItem(acQuit)

    contents += meFile

    val meMode = new Menu("Mode")

    //https://github.com/ingoem/scala-swing/blob/master/scala/swing/test/UIDemo.scala
    val miCalc = new RadioMenuItem("Calculate")
    miCalc.action = Action("Calculate"){
      calculator.mode = CALC
    }
    miCalc.selected = true

    val miFz = new RadioMenuItem("z -> f(z)")
    miFz.action = Action("z -> f(z)"){
      calculator.mode = FZ
    }

    val miModFz = new RadioMenuItem("z -> |f(z)|")
    miModFz.action = Action("z -> |f(z)|"){
      calculator.mode = MODFZ
    }

    val miReFx = new RadioMenuItem("x -> Re(f(x))")
    miReFx.action = Action("x -> Re(f(x))"){
      calculator.mode = REFX
    }

    meMode.contents ++= new ButtonGroup(miCalc, miFz, miModFz, miReFx).buttons

    contents += meMode

  private[calculator] def select(m: Mode)= {
    val item = m match {
      case CALC => miCalc
      case FZ => miFz
      case MODFZ => miModFz
      case REFX => miReFx
    }
    item.selected = true
  }

}