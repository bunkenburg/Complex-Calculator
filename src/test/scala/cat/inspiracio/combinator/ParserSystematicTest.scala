/*	Copyright 2019 Alexander Bunkenburg alex@inspiracio.cat
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
package cat.inspiracio.combinator

import cat.inspiracio.complex._
import cat.inspiracio.parsing._
import org.scalatest.FunSuite

/** A systematic suite of all combinations of language features.
  *
  * E ::=
  *   | x | z           variables
  *   | 0 | 15 | 0.15
  *   | i | e | π | ∞   constants
  *   | (E)
  *   | |E|     <-- new better modulus (absolute value)
  *   | +E              prefix
  *   | -E              prefix
  *   | sin(E)          function
  *   | sin E   <-- new naked-function
  *   | E + E           addition
  *   | E - E           subtraction
  *   | E * E           multiplication
  *   | E / E           division
  *   | E \ E           exponentiation
  *   | EE      <-- new invisible multiplication
  *   | E E     <-- new space-multiplication
  *
  * */
class ParserSystematicTest extends FunSuite {

  val p = new Parser

  val X = V()
  val Z = V()
  val I = C(i)
  val E = C(e)
  val Pi = C(π)
  val Inf = C(∞)
  val Zero = C(0)
  val Half = C(0.5)
  val One = C(1)
  val Two = C(2)
  val Three = C(3)

  // single feature -------------------------

  test("x"){ assert( p("x") === X ) }
  test("z"){ assert( p("z") === Z ) }

  test("0"){ assert( p("0") === Zero ) }
  test("15"){ assert( p("15") === C(15) ) }
  test("0.15"){ assert( p("0.15") === C(0.15) ) }
  test("i"){ assert( p("i") === I ) }
  test("e"){ assert( p("e") === E ) }
  test("π"){ assert( p("π") === Pi ) }
  test("∞"){ assert( p("∞") === Inf ) }

  test("(∞)"){ assert( p("(∞)") === Inf ) }
  test("|∞|"){ assert( p("|∞|") === Mod(Inf) ) }
  test("+∞"){ assert( p("+∞") === Inf ) }
  test("-∞"){ assert( p("-∞") === Neg(Inf) ) }
  test("sin(π)"){ assert( p("sin(π)") === Sin(Pi) ) }
  test("sin π"){ assert( p("sin π") === Sin(Pi) ) }

  test("e+e"){ assert( p("e+e") === Plus(E,E) ) }
  test("e-e"){ assert( p("e-e") === Minus(E,E) ) }
  test("e*e"){ assert( p("e*e") === Mult(E,E) ) }
  test("e/e"){ assert( p("e/e") === Div(E,E) ) }
  test("e\\e"){ assert( p("e\\e") === Power(E,E) ) }

  test("ee"){ assert( p("ee") === Mult(E,E) ) }
  test("2i"){ assert( p("2i") === Mult(Two,I) ) }
  test("2πi"){ assert( p("2πi") === Mult(Mult(Two,Pi),I) ) }

  test("e e"){ assert( p("e e") === Mult(E,E) ) }

  // combinations or two features --------------

  // (E)
  test("((e))"){ assert( p("((e))") === E ) }
  test("(|e|)"){ assert( p("(|e|)") === Mod(E) ) }
  test("(+e)"){ assert( p("(+e)") === E ) }
  test("(-e)"){ assert( p("(-e)") === Neg(E) ) }
  test("(sin e)"){ assert( p("(sin e)") === Sin(E) ) }
  test("(e+e)"){ assert( p("(e+e)") === Plus(E,E) ) }
  test("(e-e)"){ assert( p("(e-e)") === Minus(E,E) ) }
  test("(e*e)"){ assert( p("(e*e)") === Mult(E,E) ) }
  test("(e/e)"){ assert( p("(e/e)") === Div(E,E) ) }
  test("(e\\e)"){ assert( p("(e\\e)") === Power(E,E) ) }
  test("(ee)"){ assert( p("(ee)") === Mult(E,E) ) }
  test("(2i)"){ assert( p("(2i)") === Mult(Two,I) ) }

  // |E|
  test("|(e)|"){ assert( p("|(e)|") === Mod(E) ) }
  test("||e||"){ assert( p("||e||") === Mod(Mod(E)) ) }
  test("|+e|"){ assert( p("|+e|") === Mod(E) ) }
  test("|-e|"){ assert( p("|-e|") === Mod(Neg(E)) ) }
  test("|sin e|"){ assert( p("|sin e|") === Mod(Sin(E)) ) }
  test("|e+e|"){ assert( p("|e+e|") === Mod(Plus(E,E)) ) }
  test("|e-e|"){ assert( p("|e-e|") === Mod(Minus(E,E)) ) }
  test("|e*e|"){ assert( p("|e*e|") === Mod(Mult(E,E)) ) }
  test("|e/e|"){ assert( p("|e/e|") === Mod(Div(E,E)) ) }
  test("|e\\e|"){ assert( p("|e\\e|") === Mod(Power(E,E)) ) }
  test("|ee|"){ assert( p("|ee|") === Mod(Mult(E,E)) ) }
  test("|2i|"){ assert( p("|2i|") === Mod(Mult(Two,I)) ) }

  // +E
  test("+(e)"){ assert( p("+(e)") === E ) }
  test("+|e|"){ assert( p("+|e|") === Mod(E) ) }
  test("++e"){ assert( p("++e") === E ) }
  test("+-e"){ assert( p("+-e") === Neg(E) ) }
  test("+sin e"){ assert( p("+sin e") === Sin(E) ) }
  test("+e+e"){ assert( p("+e+e") === Plus(E,E) ) }
  test("+e-e"){ assert( p("+e-e") === Minus(E,E) ) }
  test("+e*e"){ assert( p("+e*e") === Mult(E,E) ) }
  test("+e/e"){ assert( p("+e/e") === Div(E,E) ) }
  test("+e\\e"){ assert( p("+e\\e") === Power(E,E) ) }
  test("+ee"){ assert( p("+ee") === Mult(E,E) ) }
  test("+2i"){ assert( p("+2i") === Mult(Two,I) ) }

  // -E
  test("-(e)"){ assert( p("-(e)") === Neg(E) ) }
  test("-|e|"){ assert( p("-|e|") === Neg(Mod(E)) ) }
  test("-+e"){ assert( p("-+e") === Neg(E) ) }
  test("--e"){ assert( p("--e") === Neg(Neg(E)) ) }
  test("-sin e"){ assert( p("-sin e") === Neg(Sin(E)) ) }
  test("-e+e"){ assert( p("-e+e") === Plus(Neg(E),E) ) }
  test("-e-e"){ assert( p("-e-e") === Minus(Neg(E),E) ) }
  test("-e*e"){ assert( p("-e*e") === Neg(Mult(E,E)) ) }
  test("-e/e"){ assert( p("-e/e") === Neg(Div(E,E)) ) }
  test("-e\\e"){ assert( p("-e\\e") === Neg(Power(E,E)) ) }
  test("-ee"){ assert( p("-ee") === Neg(Mult(E,E)) ) }
  test("-2i"){ assert( p("-2i") === Neg(Mult(Two,I)) ) }

  // sin(E)
  test("sin((e))"){ assert( p("sin((e))") === Sin(E) ) }
  test("sin(|e|)"){ assert( p("sin(|e|)") === Sin(Mod(E)) ) }
  test("sin(+e)"){ assert( p("sin(+e)") === Sin(E) ) }
  test("sin(-e)"){ assert( p("sin(-e)") === Sin(Neg(E)) ) }
  test("sin(sin e)"){ assert( p("sin(sin e)") === Sin(Sin(E)) ) }
  test("sin(e+e)"){ assert( p("sin(e+e)") === Sin(Plus(E,E)) ) }
  test("sin(e-e)"){ assert( p("sin(e-e)") === Sin(Minus(E,E)) ) }
  test("sin(e*e)"){ assert( p("sin(e*e)") === Sin(Mult(E,E)) ) }
  test("sin(e/e)"){ assert( p("sin(e/e)") === Sin(Div(E,E)) ) }
  test("sin(e\\e)"){ assert( p("sin(e\\e)") === Sin(Power(E,E)) ) }
  test("sin(2i)"){ assert( p("sin(2i)") === Sin(Mult(Two,I)) ) }

}
