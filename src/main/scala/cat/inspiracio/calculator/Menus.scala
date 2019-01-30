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

import java.awt.Toolkit
import java.awt.event.KeyEvent

import cat.inspiracio.calculator.Mode.{CALC, FZ, MODFZ, REFX}
import cat.inspiracio.complex.Complex
import javax.swing._

/** A menu bar for the Calculator.
  *
  * Keyboard accelerators don't work.
  * */
final class Menus private[calculator](var calculator: Calculator) extends JMenuBar {

  private val precisionListener = null
  private val mask = Toolkit.getDefaultToolkit.getMenuShortcutKeyMask

  init()

  private def init()= {
    file()
    mode()
    precision()
  }

  private def file(): Unit = {
    val menu = new JMenu("File")
    menu.setMnemonic(KeyEvent.VK_F)

    val miAbout = new JMenuItem("About ...")
    miAbout.addActionListener( _ => new About(calculator) )
    menu.add(miAbout)

    val miQuit = new JMenuItem("Quit")
    miQuit.setMnemonic(KeyEvent.VK_Q)
    val CtrlQ = KeyStroke.getKeyStroke(KeyEvent.VK_Q, mask)
    miQuit.setAccelerator( CtrlQ )
    miQuit.addActionListener( _ => calculator.quit() )
    menu.add(miQuit)

    add(menu)
  }

  private def mode(): Unit = {
    val menu = new JMenu("Mode")

    val miCalc = new JRadioButtonMenuItem("Calculate", true)
    val miFz = new JRadioButtonMenuItem("z -> f(z)")
    val miModFz = new JRadioButtonMenuItem("z -> |f(z)|")
    val miReFx = new JRadioButtonMenuItem("x -> Re(f(x))")

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

  private def precision(): Unit = {
    val menu = new JMenu("Precision")
    val group = new ButtonGroup
    val precision = getPrecision
    for ( j <- Array(2, 4, 6, 8, 10) ) {
      val s = Integer.toString(j)
      val selected = precision == j
      val item = new JRadioButtonMenuItem(s, selected)
      item.addActionListener( _ => setPrecision(j))
      menu.add(item)
      group.add(item)
    }
    add(menu)
  }

  private def getPrecision: Int = Complex.getPrecision
  private def setPrecision(n: Int): Unit = Complex.setPrecision(n)
}