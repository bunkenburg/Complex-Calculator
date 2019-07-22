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

import cat.inspiracio.combinator.Parser

import scala.swing._
import scala.swing.event.{KeyPressed, KeyTyped}

/** The text area of the complex calculator.
  *
  * This class just adds some convenient methods
  * for copy/paste functionality to TextArea.
  *
  * This could really be a lot better,
  * taking into account the syntax tree rather than just displaying a String. */
class Display private[calculator](calculator: Calculator, fontSize: Int) extends TextArea("", 3, 1) {
  wordWrap = true
  lineWrap = true
  listenTo(keys)
  reactions += new MyKeyListener(calculator)

  private[calculator] def clear() = {
    text = ""
    calculator.editor.show(null)
  }

  private[calculator] def eraseOldResult() = {
    val equals = text.lastIndexOf('=')
    if (equals != -1) {
      text = text.substring(0, equals)
      caret.position = min(caret.position, text.length)
    }

    tellEditor
  }

  /** deletes selected text or backspaces */
  private[calculator] def delete() = {
    val start = selectionStart
    val end = selectionEnd
    val cp = caret.position
    if (start == end) {
      replaceRange("", cp - 1, cp)
      select(cp - 1, cp - 1)
      caret.position = cp - 1
    }
    else {
      replaceRange("", start, end)
      selectionEnd = start
      caret.position = start
    }

    tellEditor
  }

  private[calculator] def paste(c: Char): Unit = paste(String.valueOf(c) )

  private[calculator] def paste(s: String): Unit = {
    val start = selectionStart
    val end = selectionEnd
    if (start == end)
      insert(s, start)
    else
      replaceRange(s, start, end)
    caret.position = start + s.length
    select(start + s.length, start + s.length)

    tellEditor
  }

  private def tellEditor =
    try {
      val p = new Parser
      val e = p(text)
      calculator.editor.show(e)
    }catch{
      case ex => println(ex)
    }

  /** Put s in from of displayed string */
  private[calculator] def prepend(s: String) = {
    val start = selectionStart
    val end = selectionEnd
    val cp = caret.position
    replaceRange(s, 0, 0)
    selectionStart = start + s.length
    selectionEnd = end + s.length
    caret.position = cp + s.length

    tellEditor
  }

  /** Replace one char by another. */
  private[calculator] def replace(z: Char, x: Char) = {
    val start = selectionStart
    val end = selectionEnd
    val cp = caret.position
    text = text.replace(z, x)
    select(start, end)
    caret.position = cp

    tellEditor
  }

  //Exposes a couple more methods of JTextArea
  private def selectionStart: Int = peer.getSelectionStart
  private def selectionStart_=(i: Int) = peer.setSelectionStart(i)
  private def selectionEnd: Int = peer.getSelectionEnd
  private def selectionEnd_=(i: Int) = peer.setSelectionEnd(i)
  private def insert(str: String, pos: Int) = peer.insert(str, pos)
  private def select(selectionStart: Int, selectionEnd: Int) = peer.select(selectionStart, selectionEnd)
  private def replaceRange(str: String, start: Int, end: Int) = peer.replaceRange(str, start, end)

  override def cut(): Unit = {
    super.cut()
    tellEditor
  }

  override def paste(): Unit = {
    super.paste()
    tellEditor
  }

}