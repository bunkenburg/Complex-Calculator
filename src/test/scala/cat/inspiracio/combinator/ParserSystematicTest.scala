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
  *   | E!
  *   | E + E           addition
  *   | E - E           subtraction
  *   | E * E           multiplication
  *   | E / E           division
  *   | E \ E           exponentiation
  *   | EE      <-- new invisible multiplication
  *   | E E     <-- new space-multiplication
  *
  * roughly, precedence:
  *   + -
  *   * /
  *   \
  *   +E -E sin E       pre-unary
  *   EE                strongest of almost all
  *   E!
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
  val Four = C(4)

  // single feature -------------------------

  test("x"){ assert( p("x") === X ) }
  test("_x"){ assert( p(" x") === X ) }
  test("z"){ assert( p("z") === Z ) }

  test("0"){ assert( p("0") === Zero ) }
  test("_0"){ assert( p(" 0") === Zero ) }
  test("15"){ assert( p("15") === C(15) ) }
  test("0.15"){ assert( p("0.15") === C(0.15) ) }
  test("_0.15"){ assert( p(" 0.15") === C(0.15) ) }
  test("i"){ assert( p("i") === I ) }
  test("_i"){ assert( p(" i") === I ) }
  test("e"){ assert( p("e") === E ) }
  test("π"){ assert( p("π") === Pi ) }
  test("∞"){ assert( p("∞") === Inf ) }
  test("_∞"){ assert( p(" ∞") === Inf ) }

  test("(∞)"){ assert( p("(∞)") === Inf ) }
  test(" ( ∞ ) "){ assert( p("(∞)") === Inf ) }
  test("|∞|"){ assert( p("|∞|") === Mod(Inf) ) }
  test(" | ∞ | "){ assert( p("|∞|") === Mod(Inf) ) }
  test("+∞"){ assert( p("+∞") === Inf ) }
  test("-∞"){ assert( p("-∞") === Neg(Inf) ) }
  test("sin(π)"){ assert( p("sin(π)") === Sin(Pi) ) }
  test(" sin( π )"){ assert( p("sin(π)") === Sin(Pi) ) }
  test("sin π"){ assert( p("sin π") === Sin(Pi) ) }
  test("3!"){ assert( p("3!") === Fac(Three) ) }

  test("e+e"){ assert( p("e+e") === Plus(E,E) ) }
  test(" e + e "){ assert( p("e+e") === Plus(E,E) ) }
  test("e-e"){ assert( p("e-e") === Minus(E,E) ) }
  test(" e - e "){ assert( p("e-e") === Minus(E,E) ) }
  test("e*e"){ assert( p("e*e") === Mult(E,E) ) }
  test(" e * e "){ assert( p("e*e") === Mult(E,E) ) }
  test("e/e"){ assert( p("e/e") === Div(E,E) ) }
  test(" e / e "){ assert( p("e/e") === Div(E,E) ) }
  test("e\\e"){ assert( p("e\\e") === Power(E,E) ) }
  test(" e \\ e "){ assert( p("e\\e") === Power(E,E) ) }
  test("ee"){ assert( p("ee") === Mult(E,E) ) }
  test("2i"){ assert( p("2i") === Mult(Two,I) ) }
  test("2πi"){ assert( p("2πi") === Mult(Mult(Two,Pi),I) ) }

  /** needs space multiplication */
  //test("e e"){ assert( p("e e") === Mult(E,E) ) }

  // combinations or two features --------------

  // (E)
  test("((e))"){ assert( p("((e))") === E ) }
  test("(|e|)"){ assert( p("(|e|)") === Mod(E) ) }
  test("(+e)"){ assert( p("(+e)") === E ) }
  test("(-e)"){ assert( p("(-e)") === Neg(E) ) }
  test("(sin e)"){ assert( p("(sin e)") === Sin(E) ) }
  test("(3!)"){ assert( p("(3!)") === Fac(Three) ) }
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
  test("|3!|"){ assert( p("|3!|") === Mod(Fac(Three)) ) }
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
  test("+3!"){ assert( p("+3!") === Fac(Three) ) }
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
  test("-3!"){ assert( p("-3!") === Neg(Fac(Three)) ) }
  test("-e+e"){ assert( p("-e+e") === Plus(Neg(E),E) ) }
  test("-e-e"){ assert( p("-e-e") === Minus(Neg(E),E) ) }
  test("-e*e"){ assert( p("-e*e") === Mult(Neg(E),E) ) }
  test("-e/e"){ assert( p("-e/e") === Div(Neg(E),E) ) }
  test("-e\\e"){ assert( p("-e\\e") === Power(Neg(E),E) ) }
  test("-ee"){ assert( p("-ee") === Neg(Mult(E,E)) ) }
  test("-2i"){ assert( p("-2i") === Neg(Mult(Two,I)) ) }

  // sin(E)
  test("sin((e))"){ assert( p("sin((e))") === Sin(E) ) }
  test("sin(|e|)"){ assert( p("sin(|e|)") === Sin(Mod(E)) ) }
  test("sin(+e)"){ assert( p("sin(+e)") === Sin(E) ) }
  test("sin(-e)"){ assert( p("sin(-e)") === Sin(Neg(E)) ) }
  test("sin(sin e)"){ assert( p("sin(sin e)") === Sin(Sin(E)) ) }
  test("sin(3!)"){ assert( p("sin(3!)") === Sin(Fac(Three)) ) }
  test("sin(e+e)"){ assert( p("sin(e+e)") === Sin(Plus(E,E)) ) }
  test("sin(e-e)"){ assert( p("sin(e-e)") === Sin(Minus(E,E)) ) }
  test("sin(e*e)"){ assert( p("sin(e*e)") === Sin(Mult(E,E)) ) }
  test("sin(e/e)"){ assert( p("sin(e/e)") === Sin(Div(E,E)) ) }
  test("sin(e\\e)"){ assert( p("sin(e\\e)") === Sin(Power(E,E)) ) }
  test("sin(2i)"){ assert( p("sin(2i)") === Sin(Mult(Two,I)) ) }

  // sin E    interesting
  test("sin (e)"){ assert( p("sin (e)") === Sin(E) ) }
  test("sin |e|"){ assert( p("sin |e|") === Sin(Mod(E)) ) }
  test("sin +e"){ assert( p("sin +e") === Sin(E) ) }
  test("sin -e"){ assert( p("sin -e") === Sin(Neg(E)) ) }
  test("sin sin e"){ assert( p("sin sin e") === Sin(Sin(E)) ) }
  test("sin 3!"){ assert( p("sin 3!") === Sin(Fac(Three)) ) }
  test("sin e+e"){ assert( p("sin e+e") === Plus(Sin(E),E) ) }
  test("sin e-e"){ assert( p("sin e-e") === Minus(Sin(E),E) ) }
  test("sin e*e"){ assert( p("sin e*e") === Mult(Sin(E),E) ) }
  test("sin e/e"){ assert( p("sin e/e") === Div(Sin(E),E) ) }
  test("sin e\\e"){ assert( p("sin e\\e") === Power(Sin(E),E) ) }
  test("sin 2i"){ assert( p("sin 2i") === Sin(Mult(Two,I)) ) }

  // E!    interesting
  test("(e)!"){ assert( p("(e)!") === Fac(E) ) }
  test("|e|!"){ assert( p("|e|!") === Fac(Mod(E)) ) }
  test("+e!"){ assert( p("+e!") === Fac(E) ) }
  test("-e!"){ assert( p("-e!") === Neg(Fac(E)) ) }
  test("sin e!"){ assert( p("sin e!") === Sin(Fac(E)) ) }
  test("3!!"){ assert( p("3!!") === Fac(Fac(Three)) ) }
  test("e!+e!"){ assert( p("e!+e!") === Plus(Fac(E),Fac(E)) ) }
  test("e!-e!"){ assert( p("e!-e!") === Minus(Fac(E),Fac(E)) ) }
  test("e!*e!"){ assert( p("e!*e!") === Mult(Fac(E),Fac(E)) ) }
  test("e!/e!"){ assert( p("e!/e!") === Div(Fac(E),Fac(E)) ) }
  test("e!\\e!"){ assert( p("e!\\e!") === Power(Fac(E),Fac(E)) ) }
  //XXX test("2i!"){ assert( p("2i!") === Mult(Two,Fac(I)) ) }

  // E + E
  test("(e)+(e)"){ assert( p("(e)+(e)") === Plus(E,E) ) }
  test("|e|+|e|"){ assert( p("|e|+|e|") === Plus(Mod(E),Mod(E)) ) }
  test("+e++e"){ assert( p("+e++e") === Plus(E,E) ) }
  test("-e+-e"){ assert( p("-e+-e") === Plus(Neg(E),Neg(E)) ) }
  test("sin e + sin e"){ assert( p("sin e + sin e") === Plus(Sin(E),Sin(E)) ) }
  test("sin(e) + sin(e)"){ assert( p("sin(e) + sin(e)") === Plus(Sin(E), Sin(E)) ) }
  test("3!+2!"){ assert( p("3!+2!") === Plus(Fac(Three),Fac(Two)) ) }
  test("1+2+3"){ assert( p("1+2+3") === Plus(Plus(One,Two),Three) ) }
  test("0+1-2+3"){ assert( p("0+1-2+3") === Plus(Minus(Plus(Zero,One),Two),Three) ) }
  test("0+e*e+3"){ assert( p("0+e*e+3") === Plus(Plus(Zero,Mult(E,E)),Three) ) }
  test("0+1/2+3"){ assert( p("0+1/2+3") === Plus(Plus(Zero,Div(One,Two)),Three) ) }
  test("0+e\\e+3"){ assert( p("0+e\\e+3") === Plus(Plus(Zero,Power(E,E)),Three) ) }
  test("2i+3e"){ assert( p("2i+3e") === Plus(Mult(Two,I),Mult(Three,E)) ) }

  // E - E
  test("(e)-(e)"){ assert( p("(e)-(e)") === Minus(E,E) ) }
  test("|e|-|e|"){ assert( p("|e|-|e|") === Minus(Mod(E),Mod(E)) ) }
  test("+e-+e"){ assert( p("+e-+e") === Minus(E,E) ) }
  test("-e--e"){ assert( p("-e--e") === Minus(Neg(E),Neg(E)) ) }
  test("sin e - sin e"){ assert( p("sin e - sin e") === Minus(Sin(E),Sin(E)) ) }
  test("sin(e) - sin(e)"){ assert( p("sin(e) - sin(e)") === Minus(Sin(E), Sin(E)) ) }
  test("3!-2!"){ assert( p("3!-2!") === Minus(Fac(Three),Fac(Two)) ) }
  test("1-2+3-4"){ assert( p("1-2+3-4") === Minus(Plus(Minus(One,Two),Three),Four) ) }
  test("0-1-2-3"){ assert( p("0-1-2-3") === Minus(Minus(Minus(Zero,One),Two),Three) ) }
  test("0-e*e-3"){ assert( p("0-e*e-3") === Minus(Minus(Zero,Mult(E,E)),Three) ) }
  test("0-1/2-3"){ assert( p("0-1/2-3") === Minus(Minus(Zero,Div(One,Two)),Three) ) }
  test("0-e\\e-3"){ assert( p("0-e\\e-3") === Minus(Minus(Zero,Power(E,E)),Three) ) }
  test("2i-3e"){ assert( p("2i-3e") === Minus(Mult(Two,I),Mult(Three,E)) ) }

  // E * E
  test("(e)*(e)"){ assert( p("(e)*(e)") === Mult(E,E) ) }
  test("|e|*|e|"){ assert( p("|e|*|e|") === Mult(Mod(E),Mod(E)) ) }
  test("+e * +e"){assert( p("+e * +e") === Mult(E,E) ) }
  test("-e * -e"){ assert( p("-e * -e") === Mult(Neg(E),Neg(E)) ) }
  test("sin e * sin e"){ assert( p("sin e * sin e") === Mult(Sin(E),Sin(E)) ) }
  test("sin(e) * sin(e)"){ assert( p("sin(e) * sin(e)") === Mult(Sin(E), Sin(E)) ) }
  test("3!*2!"){ assert( p("3!*2!") === Mult(Fac(Three),Fac(Two)) ) }
  test("1*2+3*4"){ assert( p("1*2+3*4") === Plus(Mult(One,Two),Mult(Three,Four)) ) }
  test("0*1-2*3"){ assert( p("0*1-2*3") === Minus(Mult(Zero,One),Mult(Two,Three)) ) }
  test("0*1*2*3"){ assert( p("0*1*2*3") === Mult(Mult(Mult(Zero,One),Two),Three)  ) }
  test("0*1/2*3"){ assert( p("0*1/2*3") === Mult(Div(Mult(Zero,One),Two),Three) ) }
  test("0*1\\2*3"){ assert( p("0*1\\2*3") === Mult(Mult(Zero,Power(One,Two)),Three) ) }
  test("2i*3e"){ assert( p("2i*3e") === Mult(Mult(Two,I),Mult(Three,E)) ) }

  // E / E
  test("(e)/(e)"){ assert( p("(e)/(e)") === Div(E,E) ) }
  test("|e|/|e|"){ assert( p("|e|/|e|") === Div(Mod(E),Mod(E)) ) }
  test("+e/+e"){ assert( p("+e/+e") === Div(E,E) ) }
  test("-e/-e"){ assert( p("-e/-e") === Div(Neg(E),Neg(E)) ) }
  test("sin e / sin e"){ assert( p("sin e / sin e") === Div(Sin(E),Sin(E)) ) }
  test("sin(e) / sin(e)"){ assert( p("sin(e) / sin(e)") === Div(Sin(E), Sin(E)) ) }
  test("3!/2!"){ assert( p("3!/2!") === Div(Fac(Three),Fac(Two)) ) }
  test("1/2+3/4"){ assert( p("1/2+3/4") === Plus(Div(One,Two),Div(Three,Four)) ) }
  test("0/1-2/3"){ assert( p("0/1-2/3") === Minus(Div(Zero,One),Div(Two,Three)) ) }
  test("0/1*2/3"){ assert( p("0/1*2/3") === Div(Mult(Div(Zero,One),Two),Three)  ) }
  test("0/1/2/3"){ assert( p("0/1/2/3") === Div(Div(Div(Zero,One),Two),Three) ) }
  test("0/1\\2/3"){ assert( p("0/1\\2/3") === Div(Div(Zero,Power(One,Two)),Three) ) }
  test("2i/3e"){ assert( p("2i/3e") === Div(Mult(Two,I),Mult(Three,E)) ) }

  // E \ E
  test("(e)\\(e)"){ assert( p("(e)\\(e)") === Power(E,E) ) }
  test("|e|\\|e|"){ assert( p("|e|\\|e|") === Power(Mod(E),Mod(E)) ) }
  test("+e\\+e"){ assert( p("+e\\+e") === Power(E,E) ) }
  test("-e\\-e"){ assert( p("-e\\-e") === Power(Neg(E),Neg(E)) ) }
  test("sin e \\ sin e"){ assert( p("sin e \\ sin e") === Power(Sin(E),Sin(E)) ) }
  test("sin(e) \\ sin(e)"){ assert( p("sin(e) \\ sin(e)") === Power(Sin(E), Sin(E)) ) }
  test("3!\\2!"){ assert( p("3!\\2!") === Power(Fac(Three),Fac(Two)) ) }
  test("1\\2+3\\4"){ assert( p("1\\2+3\\4") === Plus(Power(One,Two),Power(Three,Four)) ) }
  test("0\\1-2\\3"){ assert( p("0\\1-2\\3") === Minus(Power(Zero,One),Power(Two,Three)) ) }
  test("0\\1*2\\3"){ assert( p("0\\1*2\\3") === Mult(Power(Zero,One),Power(Two,Three))  ) }
  test("0\\1/2\\3"){ assert( p("0\\1/2\\3") === Div(Power(Zero,One),Power(Two,Three)) ) }
  test("0\\1\\2\\3"){ assert( p("0\\1\\2\\3") === Power(Power(Power(Zero,One),Two),Three) ) }
  test("2i\\3e"){ assert( p("2i\\3e") === Power(Mult(Two,I),Mult(Three,E)) ) }

  // EE      interesting: many needs more implicit multiplication
  //XXX test("(e)(e)"){ assert( p("(e)(e)") === Mult(E,E) ) }
  //XXX test("|e||e|"){ assert( p("|e||e|") === Mult(Mod(E),Mod(E)) ) }
  //repeated test("+ee"){ assert( p("+ee") === Mult(E,E) ) }
  //repeated test("-ee"){ assert( p("-ee") === Neg(Mult(E,E)) ) }
  //XXX test("sin(e)sin(e)"){ assert( p("sin(e)sin(e)") === Mult(Sin(E), Sin(E)) ) }
  //XXX test("3!2!"){ assert( p("3!2!") ===Mult(Fac(Three),Fac(Two)) ) }
  test("2i+3i"){ assert( p("2i+3i") === Plus(Mult(Two,I),Mult(Three,I)) ) }
  test("2i-3i"){ assert( p("2i-3i") === Minus(Mult(Two,I),Mult(Three,I)) ) }
  test("2i*3i"){ assert( p("2i*3i") === Mult(Mult(Two,I),Mult(Three,I))  ) }
  test("2i/3i"){ assert( p("2i/3i") === Div(Mult(Two,I),Mult(Three,I)) ) }
  test("2i\\3i"){ assert( p("2i\\3i") === Power(Mult(Two,I),Mult(Three,I)) ) }
  test("2ize"){ assert( p("2ize") === Mult(Mult(Mult(Two,I),Z),E) ) }

  // E E     interesting: needs space multiplication
  //XXX test("(e) (e)"){ assert( p("(e) (e)") === Mult(E,E) ) }
  //XXX test("|e| |e|"){ assert( p("|e| |e|") === Mult(Mod(E),Mod(E)) ) }
  //XXX test("+e e"){ assert( p("+e e") === Mult(E,E) ) }
  //XXX test("-e e"){ assert( p("-e e") === Mult(Neg(E),E) ) }
  //XXX test("sin(e) sin(e)"){ assert( p("sin(e) sin(e)") === Mult(Sin(E), Sin(E)) ) }
  //XXX test("3! 2!"){ assert( p("3! 2!") ===Mult(Fac(Three),Fac(Two)) ) }
  //XXX test("2 i + 3 i"){ assert( p("2 i + 3 i") === Plus(Mult(Two,I),Mult(Three,I)) ) }
  //XXX test("2 i - 3 i"){ assert( p("2 i - 3 i") === Minus(Mult(Two,I),Mult(Three,I)) ) }
  //Not necessary. test("2 i * 3 i"){ assert( p("2 i * 3 i") === Mult(Mult(Mult(Two,I),Three),I)  ) }
  //No. test("2 i / 3 i"){ assert( p(" 2 i / 3 i") === Mult(Div(Mult(Two,I),Three),I) ) }
  //XXX test("2 i \\ 3 i"){assert( p("2 i \\ 3 i") === Mult(Mult(Two,Power(I,Three)),I) )}
  //XXX test("2 i \\ 3"){assert( p("2 i \\ 3") === Mult(Two,Power(I,Three)) )}
  //No. test("2i ze"){ assert( p("2ize") === Mult(Mult(Two,I),Mult(Z,E)) ) }


}
