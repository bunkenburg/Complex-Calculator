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
import Token._

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

    val k = last('+', s, start, end)
    if ( k.isDefined )
      plusMinus(s, start, k.get, end, SUMTOKEN)

    else {

      val k = last('-', s, start, end)
      if ( k.isDefined )
        plusMinus(s, start, k.get, end, DIFFERENCETOKEN)

      else {
        val k = last('*', s, start, end)
        if ( k.isDefined )
          binary(s, start, k.get, end, PRODUCTTOKEN)

        else {
          val k = last('/', s, start, end)
          if ( k.isDefined )
            binary(s, start, k.get, end, QUOTIENTTOKEN)

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

    //parse (
    if (s.charAt(start.getIndex) == '(') {
      val j = first(')', s, start.getIndex + 1, end.getIndex)
      if (j.isEmpty)
        throw new ParseException("unmatched bracket", start.getIndex)
      tree = parse(s, start.getIndex + 1, j.get)
      start.setIndex(j.get + 1)
    }

    //parse function
    else {
      val token = recogniseFunction(s, start, end)
      if (token.isDefined) {
        tree = new Unary(token.get, parse(s, start.getIndex, end.getIndex))
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
  private def first(c: Char, s: String, i: Int, j: Int): Option[Int] = {
    var k = i
    var parenthesis = 0
    while ( k < j && (parenthesis != 0 || s.charAt(k) != c) ) {
      val ch = s.charAt(k)
      if ( ch == '(')
        parenthesis += 1
      else if ( ch == ')')
        parenthesis -= 1
      k += 1
    }
    if (k < j)
      Some(k)
    else
      None
  }

  /** Index of the last instance of c i s(i..j). */
  private def last(c: Char, s: String, i: Int, j: Int): Option[Int] = {
    var k = j - 1
    var parenthesis = 0
    while ( i <= k && (parenthesis != 0 || s.charAt(k) != c) ) {
      val ch = s.charAt(k)
      if ( ch == '(')
        parenthesis += 1
      else if ( ch == ')')
        parenthesis -= 1
      k -= 1
    }
    if (i <= k)
      Some(k)
    else
      None
  }

  private def readVariable(s: String, start: ParsePosition, end: ParsePosition): Variable = {
    start.setIndex(start.getIndex + 1)
    new Variable
  }

  private def recogniseFunction(s: String, p0: ParsePosition, p1: ParsePosition): Option[Token] = {
    val i = p0.getIndex
    if (s.startsWith("acos", i)) {
      p0.setIndex(i + 4)
      Some(ACOSTOKEN)
    }
    else if (s.startsWith("arg", i)) {
      p0.setIndex(i + 3)
      Some(ARGTOKEN)
    }
    else if (s.startsWith("asin", i)) {
      p0.setIndex(i + 4)
      Some(ASINTOKEN)
    }
    else if (s.startsWith("atan", i)) {
      p0.setIndex(i + 4)
      Some(ATANTOKEN)
    }
    else if (s.startsWith("conj", i)) {
      p0.setIndex(i + 4)
      Some(CONJTOKEN)
    }
    else if (s.startsWith("cosh", i)) {
      p0.setIndex(i + 4)
      Some(COSHTOKEN)
    }
    else if (s.startsWith("cos", i)) {
      p0.setIndex(i + 3)
      Some(COSTOKEN)
    }
    else if (s.startsWith("D", i)) {
      p0.setIndex(i + 1)
      Some(DTOKEN)
    }
    else if (s.startsWith("exp", i)) {
      p0.setIndex(i + 3)
      Some(EXPTOKEN)
    }
    else if (s.startsWith("Im", i)) {
      p0.setIndex(i + 2)
      Some(IMTOKEN)
    }
    else if (s.startsWith("ln", i)) {
      p0.setIndex(i + 2)
      Some(LNTOKEN)
    }
    else if (s.startsWith("mod", i)) {
      p0.setIndex(i + 3)
      Some(MODTOKEN)
    }
    else if (s.startsWith("opp", i)) {
      p0.setIndex(i + 3)
      Some(OPPTOKEN)
    }
    else if (s.startsWith("Re", i )) {
      p0.setIndex(i + 2)
      Some(RETOKEN)
    }
    else if (s.startsWith("sinh", i )) {
      p0.setIndex(i + 4)
      Some(SINHTOKEN)
    }
    else if (s.startsWith("sin", i )) {
      p0.setIndex(i + 3)
      Some(SINTOKEN)
    }
    else if (s.startsWith("tanh", i )) {
      p0.setIndex(i + 4)
      Some(TANHTOKEN)
    }
    else if (s.startsWith("tan", i )) {
      p0.setIndex(i + 3)
      Some(TANTOKEN)
    }
    else None
  }

  /** removes all white space from the string */
  def stripBlanks(s: String): String = {
    val builder = new StringBuilder(s.length)
    for( c <- s )
      if ( !Character.isWhitespace(c) )
        builder.append(c)
    builder.toString
  }

  private final val constants = "iepπ∞"
  private final val variables = "zx"
  private final val digits = "0123456789"
  private final val functionInitials = "acDeIlmoRst"

  @throws[ParseException]
  private def readConstant(s: String, start: ParsePosition, end: ParsePosition): Constant = {
    val i = start.getIndex
    if (s.startsWith("i", i)) {
      start.setIndex(i+1)
      new Constant( Cartesian(0, 1) )
    }
    else if (s.startsWith("e", i)) {
      start.setIndex(i+1)
      new Constant( e )
    }
    else if (s.startsWith("π", i)) {
      start.setIndex(i+1)
      new Constant( π )
    }
    else if (s.startsWith("∞", i)) {
      start.setIndex(i+1)
      new Constant( ∞ )
    }
    else
      throw new ParseException("readConstant " + s, i)
  }

  @throws[ParseException]
  private def readDigits(s: String, start: ParsePosition, end: ParsePosition): Constant = {
    var i = start.getIndex
    while ( i < end.getIndex && digits.contains(s.charAt(i) ) )
      i += 1

    if (end.getIndex == i){
      val c = ( s.substring(start.getIndex, i) ).toDouble
      start.setIndex(i)
      new Constant(c)
    }

    else if (s.charAt(i) == '.') {
      var j = i + 1
      while ( j < end.getIndex && digits.contains(s.charAt(j) ) )
        j += 1

      if (i + 1 <= j - 1) {
        i = j
        val c = (s.substring(start.getIndex, i)).toDouble
        start.setIndex(i)
        new Constant(c)
      }
      else
        throw new ParseException("readDigits: decimal point followed by non-digit", i)
    }

    else{
      val c = (s.substring(start.getIndex, i)).toDouble
      start.setIndex(i)
      new Constant(c)
    }
  }

  @throws[ParseException]
  private def binary(s: String, i: Int, j: Int, k: Int, token: Token): Binary = {
    val a = parse(s, i, j)
    val b = parse(s, j + 1, k)
    new Binary(token, a, b)
  }

  @throws[ParseException]
  private def plusMinus(s: String, i: Int, j: Int, k: Int, token: Token): Syntax =
    if (j == i)
      new Unary(token, parse(s, i + 1, k))
    else
      binary(s, i, j, k, token)

}

abstract class Syntax() {

  override def toString: String

  def apply(z: Complex): Complex

  //def apply(p: Pictlet) : Pictlet
}