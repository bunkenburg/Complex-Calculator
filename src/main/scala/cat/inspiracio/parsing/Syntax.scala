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
object Syntax {

  /** The main client method: parses a tree from a String. */
  @throws[ParseException]
  def parse(s: String): Syntax = {
    val s_ = stripBlanks(s)
    parse(s_, 0, s_.length)
  }

  @throws[ParseException]
  private def parse(s: String, start: Int, end: Int): Syntax = {

    if ( end <= start ) throw new ParseException("SyntaxTree.parse(empty)", start)

    var k = last('+', s, start, end)
    if ( -1 < k )
      plusMinus(s, start, k, end, 0)

    else {

      k = last('-', s, start, end)
      if ( -1 < k )
        plusMinus(s, start, k, end, 1)

      else {
        k = last('*', s, start, end)
        if ( -1 < k )
          binary(s, start, k, end, PRODUCTTOKEN)

        else {
          k = last('/', s, start, end)
          if ( -1 < k )
            binary(s, start, k, end, 3)

          else {
            val p0 = new ParsePosition(start)
            val p1 = new ParsePosition(end)
            var tree = parseFactor(s, p0, p1)
            while ( p0.getIndex != p1.getIndex ) {
              val factor = parseFactor(s, p0, p1)
              tree = new Binary(PRODUCTTOKEN, tree, factor )
            }
            tree
          }
        }
      }
    }
  }

  @throws[ParseException]
  private def parseFactor(s: String, start: ParsePosition, end: ParsePosition): Syntax = {

    var tree: Syntax = null
    var i = 0

    //parse (
    if (s.charAt(start.getIndex) == '(') {
      val j = first(')', s, start.getIndex + 1, end.getIndex)
      if (j == -1) throw new ParseException("unmatched bracket", start.getIndex)
      tree = parse(s, start.getIndex + 1, j)
      start.setIndex(j + 1)
    }

    //parse function
    else {
      i = recogniseFunction(s, start, end)
      if (i != -1) {
        tree = new Unary(i, parse(s, start.getIndex, end.getIndex))
        start.setIndex(end.getIndex)
      }

      //parse digits
      else if ( digits contains (s.charAt(start.getIndex)) )
        tree = readDigits(s, start, end)

      //parse constants
      else if ( constants contains (s.charAt(start.getIndex) ))
        tree = readConstant(s, start, end)

      //parse variable
      else if ( variables  contains (s.charAt(start.getIndex) ))
        tree = readVariable(s, start, end)
    }

    //What's this?
    if (tree == null) throw new ParseException(s, start.getIndex)

    //parse trailing !
    while ( start.getIndex < end.getIndex && s.charAt(start.getIndex) == '!' ) {
      tree = new Unary( FACTOKEN, tree )
      start.setIndex(start.getIndex + 1)
    }

    //parse ^
    if (start.getIndex < end.getIndex && s.charAt(start.getIndex) == '^') {
      tree = new Binary( POWERTOKEN, tree, parse(s, start.getIndex + 1, end.getIndex))
      start.setIndex(end.getIndex)
    }

    tree
  }

  /** Index of the first instance of c i s(i..j). */
  private def first(c: Char, s: String, i: Int, j: Int): Int = {
    var k = i
    var parenthesis = 0
    while ( k < j && (parenthesis != 0 || s.charAt(k) != c) ) {
      if (s.charAt(k) == '(')
        parenthesis += 1
      else if (s.charAt(k) == ')')
        parenthesis -= 1
      k += 1
    }
    if (k < j) k else -1
  }

  /** Index of the last instance of c i s(i..j). */
  private def last(c: Char, s: String, i: Int, j: Int): Int = {
    var k = j - 1
    var parenthesis = 0
    while ( i <= k && (parenthesis != 0 || s.charAt(k) != c) ) {
      if (s.charAt(k) == '(')
        parenthesis += 1
      else if (s.charAt(k) == ')')
        parenthesis -= 1
      k -= 1
    }
    if (i <= k) k else -1
  }

  private def readVariable(s: String, start: ParsePosition, end: ParsePosition): Variable = {
    start.setIndex(start.getIndex + 1)
    new Variable
  }

  private def recogniseFunction(s: String, p0: ParsePosition, p1: ParsePosition): Int = {
    val i = p0.getIndex
    if (s.startsWith("acos", i)) {
      p0.setIndex(i + 4)
      ACOSTOKEN
    }
    else if (s.startsWith("arg", i)) {
      p0.setIndex(i + 3)
      ARGTOKEN
    }
    else if (s.startsWith("asin", i)) {
      p0.setIndex(i + 4)
      ASINTOKEN
    }
    else if (s.startsWith("atan", i)) {
      p0.setIndex(i + 4)
      ATANTOKEN
    }
    else if (s.startsWith("conj", i)) {
      p0.setIndex(i + 4)
      CONJTOKEN
    }
    else if (s.startsWith("cosh", i)) {
      p0.setIndex(i + 4)
      COSHTOKEN
    }
    else if (s.startsWith("cos", i)) {
      p0.setIndex(i + 3)
      COSTOKEN
    }
    else if (s.startsWith("D", i)) {
      p0.setIndex(i + 1)
      DTOKEN
    }
    else if (s.startsWith("exp", i)) {
      p0.setIndex(i + 3)
      EXPTOKEN
    }
    else if (s.startsWith("Im", i)) {
      p0.setIndex(i + 2)
      IMTOKEN
    }
    else if (s.startsWith("ln", i)) {
      p0.setIndex(i + 2)
      LNTOKEN
    }
    else if (s.startsWith("mod", i)) {
      p0.setIndex(i + 3)
      MODTOKEN
    }
    else if (s.startsWith("opp", i)) {
      p0.setIndex(i + 3)
      OPPTOKEN
    }
    else if (s.startsWith("Re", i )) {
      p0.setIndex(i + 2)
      RETOKEN
    }
    else if (s.startsWith("sinh", i )) {
      p0.setIndex(i + 4)
      SINHTOKEN
    }
    else if (s.startsWith("sin", i )) {
      p0.setIndex(i + 3)
      SINTOKEN
    }
    else if (s.startsWith("tanh", i )) {
      p0.setIndex(i + 4)
      TANHTOKEN
    }
    else if (s.startsWith("tan", i )) {
      p0.setIndex(i + 3)
      TANTOKEN
    }
    else NOTOKEN
  }

  /** removes all white space from the string */
  def stripBlanks(s: String): String = {
    val builder = new StringBuilder(s.length)
    for ( i <- 0 until s.length )
      if ( !Character.isWhitespace(s.charAt(i)) )
        builder.append(s.charAt(i))
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

  private final val constants = "iepπ∞"
  private final val variables = "zx"
  private final val digits = "0123456789"
  private final val functionInitials = "acDeIlmoRst"

  val NOTOKEN: Int = -1
  val SUMTOKEN = 0
  val DIFFERENCETOKEN = 1
  val PRODUCTTOKEN = 2
  val QUOTIENTTOKEN = 3
  val POWERTOKEN = 4
  val CONJTOKEN = 5
  val COSHTOKEN = 6
  val TANHTOKEN = 7
  val ARGTOKEN = 8
  val COSTOKEN = 9
  val EXPTOKEN = 10
  val MODTOKEN = 11
  val OPPTOKEN = 12
  val SINTOKEN = 13
  val TANTOKEN = 14
  val IMTOKEN = 15
  val LNTOKEN = 16
  val RETOKEN = 17
  val DTOKEN = 18
  val SINHTOKEN = 19
  val FACTOKEN = 20
  val ASINTOKEN = 21
  val ACOSTOKEN = 22
  val ATANTOKEN = 23

  @throws[ParseException]
  private def readConstant(s: String, start: ParsePosition, end: ParsePosition): Constant = {
    var c: Complex = null
    var i = start.getIndex

    if (s.startsWith("i", i)) {
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
    else
      throw new ParseException("readConstant " + s, i)

    start.setIndex(i)
    new Constant(c)
  }

  @throws[ParseException]
  private def readDigits(s: String, start: ParsePosition, end: ParsePosition): Constant = {

    var i = start.getIndex
    while ( i < end.getIndex && digits.contains(s.charAt(i) ) )
      i += 1

    var c: Complex = null

    if (end.getIndex == i)
      c = ( s.substring(start.getIndex, i) ).toDouble

    else if (s.charAt(i) == '.') {
      var j = i + 1
      while ( j < end.getIndex && digits.contains(s.charAt(j) ) )
        j += 1

      if (i + 1 <= j - 1) {
        i = j
        c = (s.substring(start.getIndex, i)).toDouble
      }
      else
        throw new ParseException("readDigits: decimal point followed by non-digit", i)
    }
    else
      c = (s.substring(start.getIndex, i)).toDouble

    start.setIndex(i)
    new Constant(c)
  }

  @throws[ParseException]
  private def binary(s: String, i: Int, j: Int, k: Int, token: Int): Binary = {
    val a = parse(s, i, j)
    val b = parse(s, j + 1, k)
    new Binary(token, a, b)
  }

  @throws[ParseException]
  private def plusMinus(s: String, i: Int, j: Int, k: Int, token: Int): Syntax =
    if (j == i) new Unary(token, parse(s, i + 1, k)) else binary(s, i, j, k, token)

}

abstract class Syntax() {

  override def toString: String

  def apply(z: Complex): Complex

}