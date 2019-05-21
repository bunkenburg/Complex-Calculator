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

import java.lang.Math.min

import scala.swing._
import scala.swing.event.KeyTyped

/** The text area of the complex calculator.
  *
  * This class just adds some convenient methods
  * for copy/paste functionality to TextArea.
  *
  * This could really be a lot better,
  * taking into account the syntax tree rather than just displaying a String. */
class Display private[calculator](val fontSize: Int) extends TextArea("", 3, 1) {

  //display.addKeyListener(new MyKeyListener(Calculator.this))
  listenTo(keys)
  reactions += {
    case KeyTyped(sourceComponent, c, modifiers, location) =>
      println("KeyTyped " + c)
  }

  private[calculator] def clear() = text = ""

  private[calculator] def eraseOldResult() = {
    val equals = text.lastIndexOf('=')
    if (equals != -1) {
      val newText = text.substring(0, equals)
      text = newText
      val newCaret = min(caret.position, newText.length)
      caret.position = newCaret
    }
  }

  /** deletes selected text or backspaces */
  private[calculator] def delete() = {
    /*
    val start = selectionStart
    val end = selectionEnd
    val caret = caretPosition
    if (start == end) if (0 < caret) {
      replaceRange("", caret - 1, caret)
      select(caret - 1, caret - 1)
      caretPosition = caret - 1
    }
    else {
      replaceRange("", start, end)
      selectionEnd = start
      caretPosition = start
    }
     */
  }

  private[calculator] def paste(c: Char): Unit = paste(String.valueOf(c) )

  private[calculator] def paste(s: String): Unit = {
    /*
    val start = selectionStart
    val end = selectionEnd
    val caret = caretPosition
    if (start == end) {
      insert(s, caret)
      caretPosition = caret + s.length
      select(caret + s.length, caret + s.length)
    }
    else {
      replaceRange(s, start, end)
      caretPosition = start + s.length
      select(start + s.length, start + s.length)
    }
     */
  }

  /** Put s in from of displayed string */
  private[calculator] def prepend(s: String) = {
    /*
    val start = selectionStart
    val end = selectionEnd
    val caret = caretPosition
    replaceRange(s, 0, 0)
    selectionStart = start + s.length
    selectionEnd = end + s.length
    caretPosition = caret + s.length
     */
  }

  /** Replace one char by another. */
  private[calculator] def replace(c: Char, c1: Char) = {
    /*
    val start = selectionStart
    val end = selectionEnd
    val caret = caretPosition
    text = text.replace(c, c1)
    select(start, end)
    caretPosition = caret
     */
  }
}