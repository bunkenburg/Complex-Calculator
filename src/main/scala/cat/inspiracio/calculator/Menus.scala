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
import cat.inspiracio.complex.Complex
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
    menu.mnemonic = KeyEvent.VK_F

    val miAbout = new MenuItem("About ...")
    miAbout.addActionListener( _ => new About(calculator) )
    menu.add(miAbout)

    val miQuit = new MenuItem("Quit")
    miQuit.mnemonic = KeyEvent.VK_Q
    val CtrlQ = KeyStroke.getKeyStroke(KeyEvent.VK_Q, mask)
    miQuit.accelerator = CtrlQ
    miQuit.addActionListener( _ => calculator.quit() )
    menu.add(miQuit)

    add(menu)
  }

  private def mode(): Unit = {
    val menu = new Menu("Mode")

    val miCalc = new RadioButtonMenuItem("Calculate", true)
    val miFz = new RadioButtonMenuItem("z -> f(z)")
    val miModFz = new RadioButtonMenuItem("z -> |f(z)|")
    val miReFx = new RadioButtonMenuItem("x -> Re(f(x))")

    menu.add(miCalc)
    menu.add(miFz)
    menu.add(miModFz)
    menu.add(miReFx)

    val group = new ButtonGroup
    group.add(miCalc)
    group.add(miFz)
    group.add(miModFz)
    group.add(miReFx)

    miCalc.addActionListener( _ => calculator.setMode(CALC) )
    miFz.addActionListener( _ => calculator.setMode(FZ) )
    miModFz.addActionListener( _ => calculator.setMode(MODFZ) )
    miReFx.addActionListener( _ => calculator.setMode(REFX) )

    add(menu)
  }

  private[calculator] def select(m: Mode)= {
    val index = m match {
      case CALC => 0
      case FZ => 1
      case MODFZ => 2
      case REFX => 3
    }
    val menu = getMenu(1)
    val item = menu.getItem(index)
    item.setSelected(true)
  }

}