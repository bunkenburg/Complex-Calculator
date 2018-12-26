/*	Copyright 2018 Alexander Bunkenburg alex@inspiracio.cat
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
 * *//*	Copyright 2018 Alexander Bunkenburg alex@inspiracio.cat
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

import java.awt.event.{KeyAdapter, KeyEvent}

import cat.inspiracio.calculator.Mode.CALC

object MyKeyListener {
  private val ALLOWED_CHARS = " !sinhcoshtanhconjoppReImlnexp^modargiepi()789*/456+-123=0.z"
}

class MyKeyListener private[calculator](var calculator: Calculator) extends KeyAdapter {
  override def keyPressed(keyevent: KeyEvent): Unit = {
    val i = keyevent.getKeyCode
    //backspace delete
    if (i == 8 || i == 127) {
      calculator.delete()
      keyevent.consume()
      return
    }
    //line feed
    if (i == 10) {
      if (calculator.mode eq CALC) {
        calculator.eraseOldResult()
        calculator.doEquals()
      }
      keyevent.consume()
      return
    }
    //form feed
    if (i == 12) {
      calculator.clearAll()
      if (calculator.mode ne CALC) {
        val s = "f(" + calculator.variable + ") = "
        calculator.prepend(s)
      }
      keyevent.consume()
      return
    }
    // % '
    if (i != 37 && i != 39) keyevent.consume()
  }

  override def keyTyped(keyevent: KeyEvent): Unit = {
    val c = keyevent.getKeyChar
    if (MyKeyListener.ALLOWED_CHARS.indexOf(c) == -1) keyevent.consume()
    else {
      if (calculator.mode eq CALC) calculator.eraseOldResult()
      if (c == '=') if (calculator.mode eq CALC) calculator.doEquals()
      else calculator.paste(c)
      if (calculator.mode ne CALC) calculator.functionChange()
    }
    keyevent.consume()
  }
}