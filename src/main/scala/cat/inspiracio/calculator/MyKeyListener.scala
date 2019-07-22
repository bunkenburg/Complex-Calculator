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

import cat.inspiracio.calculator.Mode.CALC

import scala.swing.event.{Event, Key, KeyPressed, KeyTyped}

class MyKeyListener (calculator: Calculator) extends PartialFunction[Event, Unit] {

  /** All the characters that make sense in the display. */
  private val ALLOWED_CHARS = " |!sinhcoshtanhconjoppReImlnexp\\modargiepi()789*/456+-123=0.z"
  private def forbidden(c: Char): Boolean = !ALLOWED_CHARS.contains(c)

  override def isDefinedAt(e: Event): Boolean = e match {
    case KeyPressed(source, c, modifiers, location) => true
    case KeyTyped(source, c, modifiers, location) => true
    case _ => false
  }

  override def apply(e: Event): Unit = e match {
    case kp@KeyPressed(_, _, _, _) => keyPressed(kp)
    case kt@KeyTyped(_, _, _, _) => keyTyped(kt)
  }

  /** Only the ENTER key is special: In CALC mode, it is like '='. */
  private def keyPressed(e: KeyPressed): Unit = {
    val KeyPressed(s, c, m, l) = e
    if (c == Key.Enter) {
      if (calculator.mode == CALC) {
        calculator.display.eraseOldResult()
        calculator.doEquals()
      }
      e.consume() //Don't want a line-break in display
    }
  }

  /** Ignores forbidden characters,
    * erases old result,
    * inserts character, treating '=' special,
    * tells calculator of function change.
    *
    * If you want to catch Ctrl-Z:
    * modifiers = 128,
    * c paste = 22 Synchronous idle,
    * c cut = 24 Cancel,
    * c undo = 26 substitute,
    * c copy = 3 end-of-text
    * But called here before the actual action,
    * so the text I see here is still old.
    * */
  private def keyTyped(e: KeyTyped): Unit = {
    val KeyTyped(source, c, modifiers, location) = e
    if ( !forbidden(c) )
      calculator.paste(c)
    e.consume() //Already handled the character, either inserted or ignored it.
  }

}