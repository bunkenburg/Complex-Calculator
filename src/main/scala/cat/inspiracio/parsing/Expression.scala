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
package cat.inspiracio.parsing

import cat.inspiracio.complex._
import java.text.ParseException
import java.text.ParsePosition

import Token._
import cat.inspiracio.combinator.Parser
import cat.inspiracio.complex

object Expression {

  /** The main client method: parses a tree from a String. */
  @throws[ParseException]
  def parse(s: String): Expression = {

    //uses my hand-made parser from 1992
    //val s_ = stripBlanks(s)
    //parse(s_, 0, s_.length)

    // uses a Scala-style combinator parser
    val p = new Parser
    p(s)
  }

  @throws[ParseException]
  private def parse(s: String, start: Int, end: Int): Expression = {

    if ( end <= start ) throw new ParseException("SyntaxTree.parse(empty)", start)

    val k = last('+', s, start, end)
    if ( k.isDefined )
      plusMinus(s, start, k.get, end, '+')

    else {

      val k = last('-', s, start, end)
      if ( k.isDefined )
        plusMinus(s, start, k.get, end, '-')

      else {
        val k = last('*', s, start, end)
        if ( k.isDefined )
          mult(s, start, k.get, end)

        else {
          val k = last('/', s, start, end)
          if ( k.isDefined )
            div(s, start, k.get, end)

          else {
            val p0 = new ParsePosition(start)
            val p1 = new ParsePosition(end)
            var tree = parseFactor(s, p0, p1)
            while ( p0.getIndex != p1.getIndex ) {
              val factor = parseFactor(s, p0, p1)
              tree = Mult(tree, factor )
            }
            tree
          }
        }
      }
    }
  }

  @throws[ParseException]
  private def parseFactor(s: String, start: ParsePosition, end: ParsePosition): Expression = {
    var tree: Expression = null

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
        import cat.inspiracio.parsing
        val a = parse(s, start.getIndex, end.getIndex)
        tree = token.get match {
          case Token.Arg => parsing.Arg(a)
          case Token.Conj => parsing.Conj(a)
          case Token.Cos => parsing.Cos(a)
          case Token.Exp => parsing.Exp(a)
          case Token.Fac => parsing.Fac(a)
          case Token.Im => parsing.Im(a)
          case Token.Ln => parsing.Ln(a)
          case Token.Mod => parsing.Mod(a)
          case Token.Opp => parsing.Opp(a)
          case Token.Re => parsing.Re(a)
          case Token.Sin => parsing.Sin(a)
          case Token.Sinh => parsing.Sinh(a)
          case Token.Tan => parsing.Tan(a)
          case Token.Tanh => parsing.Tanh(a)
          case _ => throw new RuntimeException(token.get.toString)
        }
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
      tree = Fac(tree )
      start.setIndex(start.getIndex + 1)
    }

    //parse \
    if (start.getIndex < end.getIndex && s.charAt(start.getIndex) == '\\') {
      val a = parse(s, start.getIndex + 1, end.getIndex)
      tree = Power( tree, a)
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

  private def readVariable(s: String, start: ParsePosition, end: ParsePosition): V = {
    start.setIndex(start.getIndex + 1)
    new V
  }

  private def recogniseFunction(s: String, p0: ParsePosition, p1: ParsePosition): Option[Token] = {
    val i = p0.getIndex
    if (s.startsWith("acos", i)) {
      p0.setIndex(i + 4)
      Some(Acos)
    }
    else if (s.startsWith("arg", i)) {
      p0.setIndex(i + 3)
      Some(Token.Arg)
    }
    else if (s.startsWith("asin", i)) {
      p0.setIndex(i + 4)
      Some(Asin)
    }
    else if (s.startsWith("atan", i)) {
      p0.setIndex(i + 4)
      Some(Atan)
    }
    else if (s.startsWith("conj", i)) {
      p0.setIndex(i + 4)
      Some(Token.Conj)
    }
    else if (s.startsWith("cosh", i)) {
      p0.setIndex(i + 4)
      Some(Token.Cosh)
    }
    else if (s.startsWith("cos", i)) {
      p0.setIndex(i + 3)
      Some(Token.Cos)
    }
    else if (s.startsWith("exp", i)) {
      p0.setIndex(i + 3)
      Some(Token.Exp)
    }
    else if (s.startsWith("Im", i)) {
      p0.setIndex(i + 2)
      Some(Token.Im)
    }
    else if (s.startsWith("ln", i)) {
      p0.setIndex(i + 2)
      Some(Token.Ln)
    }
    else if (s.startsWith("mod", i)) {
      p0.setIndex(i + 3)
      Some(Token.Mod)
    }
    else if (s.startsWith("opp", i)) {
      p0.setIndex(i + 3)
      Some(Token.Opp)
    }
    else if (s.startsWith("Re", i )) {
      p0.setIndex(i + 2)
      Some(Token.Re)
    }
    else if (s.startsWith("sinh", i )) {
      p0.setIndex(i + 4)
      Some(Token.Sinh)
    }
    else if (s.startsWith("sin", i )) {
      p0.setIndex(i + 3)
      Some(Token.Sin)
    }
    else if (s.startsWith("tanh", i )) {
      p0.setIndex(i + 4)
      Some(Token.Tanh)
    }
    else if (s.startsWith("tan", i )) {
      p0.setIndex(i + 3)
      Some(Token.Tan)
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
  private def readConstant(s: String, start: ParsePosition, end: ParsePosition): C = {
    val i = start.getIndex
    if (s.startsWith("i", i)) {
      start.setIndex(i+1)
      C( complex.i )
    }
    else if (s.startsWith("e", i)) {
      start.setIndex(i+1)
      C( e )
    }
    else if (s.startsWith("π", i)) {
      start.setIndex(i+1)
      C( π )
    }
    else if (s.startsWith("∞", i)) {
      start.setIndex(i+1)
      C( ∞ )
    }
    else
      throw new ParseException("readConstant " + s, i)
  }

  @throws[ParseException]
  private def readDigits(s: String, start: ParsePosition, end: ParsePosition): C = {
    var i = start.getIndex
    while ( i < end.getIndex && digits.contains(s.charAt(i) ) )
      i += 1

    if (end.getIndex == i){
      val c = ( s.substring(start.getIndex, i) ).toDouble
      start.setIndex(i)
      C(c)
    }

    else if (s.charAt(i) == '.') {
      var j = i + 1
      while ( j < end.getIndex && digits.contains(s.charAt(j) ) )
        j += 1

      if (i + 1 <= j - 1) {
        i = j
        val c = (s.substring(start.getIndex, i)).toDouble
        start.setIndex(i)
        C(c)
      }
      else
        throw new ParseException("readDigits: decimal point followed by non-digit", i)
    }

    else{
      val c = (s.substring(start.getIndex, i)).toDouble
      start.setIndex(i)
      C(c)
    }
  }

  @throws[ParseException]
  private def mult(s: String, i: Int, j: Int, k: Int): Expression = {
    val a = parse(s, i, j)
    val b = parse(s, j + 1, k)
    Mult(a, b)
  }

  @throws[ParseException]
  private def div(s: String, i: Int, j: Int, k: Int): Expression = {
    val a = parse(s, i, j)
    val b = parse(s, j + 1, k)
    Div(a, b)
  }

  @throws[ParseException]
  private def plusMinus(s: String, i: Int, j: Int, k: Int, token: Char): Expression =
    if (j == i) {
      val a = parse(s, i + 1, k)
      if(token=='+')
        a
      else
        Neg(a)
    }
    else{
      val a = parse(s, i, j)
      val b = parse(s, j + 1, k)
      if(token=='+')
        Plus(a, b)
      else
        Minus(a, b)
    }

}

abstract class Expression() {

  override def toString: String

  def apply(z: Complex): Complex

  //def apply(p: Pictlet) : Pictlet
}