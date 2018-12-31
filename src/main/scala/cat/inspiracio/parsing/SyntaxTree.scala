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
 * *//*	Copyright 2011 Alexander Bunkenburg alex@cat.inspiracio.com
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
package cat.inspiracio.parsing

import cat.inspiracio.complex._
import java.text.ParseException
import java.text.ParsePosition

// Referenced classes of package bunkenba.parsing:
//            SyntaxTreeBinary, SyntaxTreeConstant, SyntaxTreeUnary, SyntaxTreeVariable
object SyntaxTree {

  /** The main client method: parses a tree from a String. */
  @throws[ParseException]
  def parse(s: String): SyntaxTree = {
    val s_ = SyntaxTree.stripBlanks(s)
    parse(s_, 0, s_.length)
  }

  @throws[ParseException]
  private def parse(s: String, start: Int, end: Int): SyntaxTree = {

    if ( end <= start) throw new ParseException("SyntaxTree.parse(empty)", start)

    var k = SyntaxTree.last('+', s, start, end)
    if ( -1 < k )
      bracketPlusMinus(s, start, k, end, 0)

    else {

      k = SyntaxTree.last('-', s, start, end)
      if ( -1 < k )
        bracketPlusMinus(s, start, k, end, 1)

      else {
        k = SyntaxTree.last('*', s, start, end)
        if ( -1 < k )
          bracketBin(s, start, k, end, 2)

        else {
          k = SyntaxTree.last('/', s, start, end)
          if ( -1 < k )
            bracketBin(s, start, k, end, 3)

          else {
            val p0 = new ParsePosition(start)
            val p1 = new ParsePosition(end)
            var tree = getFactor(s, p0, p1)
            while ( p0.getIndex != p1.getIndex ) {
              val factor = getFactor(s, p0, p1)
              tree = new SyntaxTreeBinary(2, tree, factor )
            }
            tree
          }
        }
      }
    }
  }

  @throws[ParseException]
  private def getFactor(s: String, start: ParsePosition, end: ParsePosition): SyntaxTree = {

    var tree: SyntaxTree = null
    var i = 0

    //parse (
    if (s.charAt(start.getIndex) == '(') {
      val j = SyntaxTree.first(')', s, start.getIndex + 1, end.getIndex)
      if (j == -1) throw new ParseException("unmatched bracket", start.getIndex)
      tree = parse(s, start.getIndex + 1, j)
      start.setIndex(j + 1)
    }

    //parse function
    else {
      i = SyntaxTree.recogniseFunction(s, start, end)
      if (i != -1) {
        tree = new SyntaxTreeUnary(i, parse(s, start.getIndex, end.getIndex))
        start.setIndex(end.getIndex)
      }

      //parse digits
      else if ( SyntaxTree.digits contains (s.charAt(start.getIndex)) )
        tree = readDigits(s, start, end)

      //parse constants
      else if ( SyntaxTree.constants contains (s.charAt(start.getIndex) ))
        tree = readConstant(s, start, end)

      //parse variable
      else if ( SyntaxTree.variables  contains (s.charAt(start.getIndex) ))
        tree = SyntaxTree.readVariable(s, start, end)
    }

    //What's this?
    if (tree == null) throw new ParseException(s, start.getIndex)

    //parse trailing !
    while ( start.getIndex < end.getIndex && s.charAt(start.getIndex) == '!' ) {
      tree = new SyntaxTreeUnary(20, tree )
      start.setIndex(start.getIndex + 1)
    }

    //parse ^
    if (start.getIndex < end.getIndex && s.charAt(start.getIndex) == '^') {
      tree = new SyntaxTreeBinary(4, tree, parse(s, start.getIndex + 1, end.getIndex))
      start.setIndex(end.getIndex)
    }

    tree
  }

  private def first(c: Char, s: String, i: Int, j: Int): Int = {
    var k = i
    var l = 0
    while ( k < j && (l != 0 || s.charAt(k) != c) ) {
      if (s.charAt(k) == '(') {
        l += 1; l - 1
      }
      else if (s.charAt(k) == ')') {
        l -= 1; l + 1
      }
      {
        k += 1; k - 1
      }
    }
    if (k < j) k
    else -1
  }

  private def last(c: Char, s: String, i: Int, j: Int) = {
    var k = j - 1
    var l = 0
    while ( {
      i <= k && (l != 0 || s.charAt(k) != c)
    }) {
      if (s.charAt(k) == '(') {
        l += 1; l - 1
      }
      else if (s.charAt(k) == ')') {
        l -= 1; l + 1
      }
      {
        k -= 1; k + 1
      }
    }
    if (i <= k) k
    else -1
  }

  private def readVariable(s: String, start: ParsePosition, end: ParsePosition) = {
    start.setIndex(start.getIndex + 1)
    new SyntaxTreeVariable
  }

  private def recogniseFunction(s: String, p0: ParsePosition, p1: ParsePosition): Int = {
    val i = p0.getIndex
    if (s.startsWith("acos", i)) {
      p0.setIndex(i + 4)
      22
    }

    else if (s.startsWith("arg", i)) {
      p0.setIndex(i + 3)
      8
    }

    else if (s.startsWith("asin", i)) {
      p0.setIndex(i + 4)
      21
    }

    else if (s.startsWith("atan", i)) {
      p0.setIndex(i + 4)
      23
    }

    else if (s.startsWith("conj", i)) {
      p0.setIndex(i + 4)
      5
    }

    else if (s.startsWith("cosh", i)) {
      p0.setIndex(i + 4)
      6
    }

    else if (s.startsWith("cos", i)) {
      p0.setIndex(i + 3)
      9
    }
    else if (s.startsWith("D", i)) {
      p0.setIndex(i + 1)
      18
    }
    else if (s.startsWith("exp", i)) {
      p0.setIndex(i + 3)
      10
    }
    else if (s.startsWith("Im", i)) {
      p0.setIndex(i + 2)
      15
    }
    else if (s.startsWith("ln", i)) {
      p0.setIndex(i + 2)
      16
    }
    else if (s.startsWith("mod", i)) {
      p0.setIndex(i + 3)
      11
    }
    else if (s.startsWith("opp", p0.getIndex)) {
      p0.setIndex(i + 3)
      12
    }
    else if (s.startsWith("Re", p0.getIndex)) {
      p0.setIndex(i + 2)
      17
    }
    else if (s.startsWith("sinh", p0.getIndex)) {
      p0.setIndex(i + 4)
      19
    }
    else if (s.startsWith("sin", p0.getIndex)) {
      p0.setIndex(i + 3)
      13
    }
    else if (s.startsWith("tanh", p0.getIndex)) {
      p0.setIndex(i + 4)
      7
    }
    else if (s.startsWith("tan", p0.getIndex)) {
      p0.setIndex(i + 3)
      14
    }
    else -1
  }

  def stripBlanks(s: String): String = {
    val builder = new StringBuilder(s.length)
    var i = 0
    while ( i < s.length ) {
      if (!Character.isWhitespace(s.charAt(i))) builder.append(s.charAt(i)) {
        i += 1; i - 1
      }
    }
    builder.toString
  }

  def token2String(token: Int): String = token match {
      case -1 => "NOTOKEN"
      case 0 => "+"
      case 1 => "-"
      case 2 => "*"
      case 3 => "/"
      case 4 => "^"
      case 22 => "acos"
      case 21 => "asin"
      case 23 => "atan"
      case 5 => "conj"
      case 6 => "cosh"
      case 7 => "tanh"
      case 19 => "sinh"
      case 8 => "arg"
      case 9 => "cos"
      case 10 => "exp"
      case 11 => "mod"
      case 12 => "opp"
      case 13 => "sin"
      case 14 => "tan"
      case 15 => "Im"
      case 16 => "ln"
      case 17 => "Re"
      case 18 => "D"
      case 20 => "!"
      case _ => "a token"
    }

  private final val constants = "iep\u03C0\u221E"
  private final val variables = "zx"
  private final val digits = "0123456789"
  private final val functionInitials = "acDeIlmoRst"

  protected val NOTOKEN: Int = -1
  protected val SUMTOKEN = 0
  protected val DIFFERENCETOKEN = 1
  protected val PRODUCTTOKEN = 2
  protected val QUOTIENTTOKEN = 3
  protected val POWERTOKEN = 4
  protected val CONJTOKEN = 5
  protected val COSHTOKEN = 6
  protected val TANHTOKEN = 7
  protected val ARGTOKEN = 8
  protected val COSTOKEN = 9
  protected val EXPTOKEN = 10
  protected val MODTOKEN = 11
  protected val OPPTOKEN = 12
  protected val SINTOKEN = 13
  protected val TANTOKEN = 14
  protected val IMTOKEN = 15
  protected val LNTOKEN = 16
  protected val RETOKEN = 17
  protected val DTOKEN = 18
  protected val SINHTOKEN = 19
  protected val FACTOKEN = 20
  protected val ASINTOKEN = 21
  protected val ACOSTOKEN = 22
  protected val ATANTOKEN = 23

  @throws[ParseException]
  private def readConstant(s: String, start: ParsePosition, end: ParsePosition): SyntaxTreeConstant = {
    var c: Complex = null
    var i = start.getIndex

    if (s.startsWith("start", i)) {
      c = Cartesian(0, 1)
      i += 1
    }

    else if (s.startsWith("e", i)) {
      c = e
      i += 1
    }

    else if (s.startsWith("π", i)) {
      c = π
      i += 1
    }

    else if (s.startsWith("∞", i)) {
      c = ∞
      i += 1
    }

    else throw new ParseException("readConstant " + s, i)

    start.setIndex(i)
    new SyntaxTreeConstant(c)
  }

  @throws[ParseException]
  private def readDigits(s: String, start: ParsePosition, end: ParsePosition): SyntaxTreeConstant = {

    var i = start.getIndex
    while ( i < end.getIndex && SyntaxTree.digits.contains(s.charAt(i) ) ) {
      i += 1; i - 1
    }

    var c: Complex = null

    if (end.getIndex == i)
      c = ( s.substring(start.getIndex, i) ).toDouble

    else if (s.charAt(i) == '.') {
      var j = 0
      j = i + 1
      while ( j < end.getIndex && SyntaxTree.digits.contains(s.charAt(j) ) ) {
        j += 1; j - 1
      }

      if (i + 1 <= j - 1) {
        i = j
        c = (s.substring(start.getIndex, i)).toDouble
      }
      else throw new ParseException("SyntaxTree.readDigits: decimal point followed by non-digit", i)
    }
    else c = (s.substring(start.getIndex, i)).toDouble

    start.setIndex(i)
    new SyntaxTreeConstant(c)
  }

  @throws[ParseException]
  private def bracketBin(s: String, i: Int, j: Int, k: Int, l: Int): SyntaxTreeBinary = {
    val a = parse(s, i, j)
    val b = parse(s, j + 1, k)
    new SyntaxTreeBinary(l, a, b)
  }

  @throws[ParseException]
  private def bracketPlusMinus(s: String, i: Int, j: Int, k: Int, l: Int): SyntaxTree = if (j == i) {
    val a = parse(s, i + 1, k)
    new SyntaxTreeUnary(l, a)
  }
  else bracketBin(s, i, j, k, l)

}

abstract class SyntaxTree() {

  def unparse: String

  override def toString: String = unparse

  //XXX rename "apply" to make it disappear
  def evaluate(z: Complex): Complex

}