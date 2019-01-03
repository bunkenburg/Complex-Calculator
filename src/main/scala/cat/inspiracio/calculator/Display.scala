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

import javax.swing._

/** The text area of the complex calculator.
  *
  * This class just adds some convenient methods
  * for copy/paste functionality to TextArea.
  *
  * This could really be a lot better. */
class Display private[calculator](val fontSize: Int) extends JTextArea("", 3, 1) {

  private[calculator] def clear() = setText("")

  /** delete selected or backspace */
  private[calculator] def delete() = {
    val start = getSelectionStart
    val end = getSelectionEnd
    val caret = getCaretPosition
    if (start == end) if (0 < caret) {
      replaceRange("", caret - 1, caret)
      select(caret - 1, caret - 1)
      setCaretPosition(caret - 1)
    }
    else {
      replaceRange("", start, end)
      setSelectionEnd(start)
      setCaretPosition(start)
    }
  }

  private[calculator] def paste(c: Char): Unit = paste(String.valueOf(c) )

  private[calculator] def paste(s: String): Unit = {
    val start = getSelectionStart
    val end = getSelectionEnd
    val caret = getCaretPosition
    if (start == end) {
      insert(s, caret)
      setCaretPosition(caret + s.length)
      select(caret + s.length, caret + s.length)
    }
    else {
      replaceRange(s, start, end)
      setCaretPosition(start + s.length)
      select(start + s.length, start + s.length)
    }
  }

  /** Put s in from of displayed string */
  private[calculator] def prepend(s: String) = {
    val start = getSelectionStart
    val end = getSelectionEnd
    val caret = getCaretPosition
    replaceRange(s, 0, 0)
    setSelectionStart(start + s.length)
    setSelectionEnd(end + s.length)
    setCaretPosition(caret + s.length)
  }

  /** Replace one char by another. */
  private[calculator] def replace(c: Char, c1: Char) = {
    val start = getSelectionStart
    val end = getSelectionEnd
    val caret = getCaretPosition
    setText(getText.replace(c, c1))
    select(start, end)
    setCaretPosition(caret)
  }
}