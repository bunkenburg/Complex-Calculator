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
 * */
package cat.inspiracio.calculator

import java.awt.event.{KeyAdapter, KeyEvent}

import cat.inspiracio.calculator.Mode.CALC

class MyKeyListener private[calculator](var calculator: Calculator) extends KeyAdapter {

  /** LF = Intro = ENTER (on linux) */
  private val LF = 10

  /** All the characters that make sense in the display. */
  private val ALLOWED_CHARS = " !sinhcoshtanhconjoppReImlnexp^modargiepi()789*/456+-123=0.z"
  private def forbidden(c: Char): Boolean = !ALLOWED_CHARS.contains(c)

  /** Only the ENTER key is special: In CALC mode, it is like '='. */
  override def keyPressed(e: KeyEvent): Unit =
    if ( e.getKeyCode == LF ) {
      if ( calculator.mode == CALC ) {
        calculator.display.eraseOldResult()
        calculator.doEquals()
      }
      e.consume() //Don't want a line-break in display
    }

  /** Ignores forbidden characters,
    * erases old result,
    * inserts character, treating '=' special,
    * tells calculator of function change. */
  override def keyTyped(e: KeyEvent): Unit = {
    val c = e.getKeyChar

    if ( !forbidden(c) )
      calculator.paste(c)

    e.consume() //Already handled the character, either inserted or ignored it.
  }

}