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

/** Maybe this can be a singleton object? */
class MyKeyListener private[calculator](var calculator: Calculator) extends KeyAdapter {

  private val ALLOWED_CHARS = " !sinhcoshtanhconjoppReImlnexp^modargiepi()789*/456+-123=0.z"

  /** User pressed a key, including shift key. */
  override def keyPressed(keyevent: KeyEvent): Unit = {

    val m = calculator.mode()
    val i = keyevent.getKeyCode

    //backspace delete
    if (i == 8 || i == 127) {
      calculator.delete()
      keyevent.consume()
    }

    //line feed
    else if (i == 10) {
      if ( m == CALC ) {
        calculator.eraseOldResult()
        calculator.doEquals()
      }
      keyevent.consume()
    }

    //form feed
    else if (i == 12) {
      calculator.clearAll()
      if ( m != CALC ) {
        val s = "f(" + calculator.variable + ") = "
        calculator.prepend(s)
      }
      keyevent.consume()
    }

    // % '
    else if (i != 37 && i != 39) {
      keyevent.consume()
    }
  }

  /** User typed a character. */
  override def keyTyped(keyevent: KeyEvent): Unit = {

    val c = keyevent.getKeyChar

    val forbidden = ALLOWED_CHARS.indexOf(c) == -1
    if ( forbidden ) {
      keyevent.consume()
    }
    else {

      val m = calculator.mode()

      if ( m == CALC )
        calculator.eraseOldResult()

      if (c == '=') {
        if ( m == CALC )
          calculator.doEquals()
        else
          calculator.paste(c)
      }
      else {
        calculator.paste(c)
      }

      if ( m != CALC)
        calculator.functionChange()
    }
    keyevent.consume()
  }

}