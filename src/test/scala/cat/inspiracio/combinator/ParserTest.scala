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
import cat.inspiracio.parsing
import cat.inspiracio.parsing._
import org.scalatest.FunSuite
import sun.nio.cs.ext.MacUkraine

class ParserTest extends FunSuite {

  def parse(s: String): Expression = {
    val p = new Parser
    p.parseAll(p.expr, s).get
  }

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

  // simple things --------------------------

  test("i"){
    val r = parse("i")
    assert( r === I )
  }

  test("e"){
    val r = parse("e")
    assert( r === E )
  }

  test("π"){
    val r = parse("π")
    assert( r === Pi )
  }

  test("∞"){
    val r = parse("∞")
    assert( r === Inf )
  }

  test("3.16"){
    val r = parse("3.16")
    assert( r === C(3.16) )
  }

  test("316"){
    val r = parse("316")
    assert( r === C(316) )
  }

  test("z"){
    val r = parse("z")
    assert( r === Z )
  }

  test("x"){
    val r = parse("x")
    assert( r === X )
  }

  test("3!"){
    val r = parse("3!")
    assert( r === Fac(Three) )
  }

  test("+3"){
    val r = parse("+3")
    assert( r === PrePlus(Three) )
  }

  test("-3"){
    val r = parse("-3")
    assert( r === PreMinus(Three) )
  }

  test("arg(i)"){
    val r = parse("arg(i)")
    assert( r === Arg(I) )
  }

  test("conj i"){
    val r = parse("conj i")
    assert( r === Conj(I) )
  }

  test("cos 0.5"){
    val r = parse("cos 0.5 ")
    assert( r === Cos(Half) )
  }

  test("cosh π"){
    val r = parse("cosh π")
    assert( r === Cosh(Pi) )
  }

  test("exp 0"){
    val r = parse("exp 0")
    assert( r === Exp(Zero) )
  }

  test("Im(z)"){
    val r = parse("Im(z)")
    assert( r === parsing.Im(Z) )
  }

  test("ln(z)"){
    val r = parse("ln(z)")
    assert( r === Ln(Z) )
  }

  test("mod(z)"){
    val r = parse("mod(z)")
    assert( r === Mod(Z) )
  }

  test("|z|"){
    val r = parse("|z|")
    assert( r === Mod(Z) )
  }

  test("opp z"){
    val r = parse("opp z")
    assert( r === Opp(Z) )
  }

  test("Re z"){
    val r = parse("Re z")
    assert( r === parsing.Re(Z) )
  }

  test("sin π"){
    val r = parse("sin π")
    assert( r === Sin(Pi) )
  }

  test("sinh π"){
    val r = parse("sinh π")
    assert( r === Sinh(Pi) )
  }

  test("tan π "){
    val r = parse("tan π ")
    assert( r === Tan(Pi) )
  }

  test("tanh π "){
    val r = parse("tanh π ")
    assert( r === Tanh(Pi) )
  }

  test("3+i"){
    val r = parse("3+i")
    assert( r === Plus(Three, I) )
  }

  test("3-i"){
    val r = parse("3-i")
    assert( r === Minus(Three, I) )
  }

  test("3*i"){
    val r = parse("3*i")
    assert( r === Mult(Three, I) )
  }

  test("3/i"){
    val r = parse("3/i")
    assert( r === Div(Three, I) )
  }

  test("e\\i"){
    val r = parse("e\\i")
    assert( r === Power(E, I) )
  }

  test("πi"){
    val r = parse("πi")
    assert( r === Mult(Pi, I) )
  }

  // combinations

  test("  π with white space"){
    val r = parse("  π  ")
    assert( r === Pi )
  }

  test("  3π  "){
    val r = parse("  3π  ")
    assert( r === Mult(Three, Pi) )
  }

  test("  ( π)  "){
    val r = parse("  ( π)  ")
    assert( r === Pi )
  }

  test("(2z)!"){
    val r = parse("(2z)!")
    assert( r === Fac(Mult(Two, Z)) )
  }

  test("(2x + 1)!"){
    val r = parse("(2x + 1)!")
    assert( r === Fac(Plus(Mult(Two, X), One)) )
  }

  test("3 sin 0"){
    val r = parse("3 sin 0")
    assert( r === Mult(Three, Sin(Zero)) )
  }

  test("sin ei"){
    val r = parse("sin ei")
    assert( r === Sin(Mult(E, I)) )
  }

  // \

  // various expressions from Maths books -----

  test("e\\ix*z"){
    val r = parse("e\\ix*z")
    assert( r === Mult(Power(E, Mult(I,X)), Z) )
  }

  test("2πi"){
    val r = parse("2πi")
    assert( r === Mult(Mult(Two, Pi), I) )
  }

  test("sin z"){
    val r = parse("sin z")
    assert( r === Sin(Z) )
  }

  test("e\\iz"){
    val r = parse("e\\iz")
    assert( r === Power(E, Mult(I,Z)) )
  }

  test("e\\-iz"){
    val r = parse("e\\-iz")
    assert( r === Power(E, PreMinus(Mult(I,Z))) )
  }

  test("sin xz sin zx"){
    val r = parse("sin xz sin zx")
    assert( r === Mult(Sin(Mult(X,Z)), Sin(Mult(Z,X))) )
  }

  test("cos z + i sin z"){
    val r = parse("cos z + i sin z")
    assert( r === Plus(Cos(Z), Mult(I, Sin(Z))) )
  }

  test("cos 2z"){
    val r = parse("cos 2z")
    assert( r === Cos(Mult(Two,Z)) )
  }

  test("2 cos z sin z"){
    val r = parse("2 cos z sin z")
    assert( r === Mult(Mult(Two, Cos(Z)), Sin(Z)) )
  }

  test("1*2*3"){
    val r = parse("1*2*3")
    assert( r === Mult(Mult(One, Two), Three) )
  }

  test("ln z + (2z + 1)πi"){
    val r = parse("ln z + (2z + 1)πi")
    assert( r === Plus(Ln(Z), Mult(Mult(Plus(Mult(Two,Z), One), Pi), I)) )
  }

  test("(cos z + i sin z)\\x"){
    val r = parse("(cos z + i sin z)\\x")
    assert( r === Power(Plus(Cos(Z), Mult(I, Sin(Z))), X) )
  }

  test("cos zx +  i sin zx"){
    val r = parse("cos zx +  i sin zx")
    assert( r === Plus(Cos(Mult(X,Z)), Mult(I,Sin(Mult(Z,X)))) )
  }

  test("exp(x)exp(z)"){
    val r = parse("exp(x)exp(z)")
    assert( r === Mult(Exp(X), Exp(Z)) )
  }

  test("sin x cos z + cos x sin z"){
    val r = parse("sin x cos z + cos x sin z")
    assert( r === Plus(Mult(Sin(X),Cos(Z)),Mult(Cos(X),Sin(Z))) )
  }

  test("cos 2π"){
    val r = parse("cos 2π")
    assert( r === Cos(Mult(Two,Pi)) )
  }

  test("cos ix"){
    val r = parse("cos ix")
    assert( r === Cos(Mult(I,X)) )
  }

  test("sin πz"){
    val r = parse("sin πz")
    assert( r === Sin(Mult(Pi,Z)) )
  }

  test("sinh|πz|"){
    val r = parse("sinh|πz|")
    assert( r === Sinh(Mod(Mult(Pi,Z))) )
  }

  test("2πi(e+1)"){
    val r = parse("2πi(e+1)")
    assert( r === Mult(Mult(Mult(Two,Pi),I), Plus(E,One)) )
  }

  // from my big table -------------------------

  test("3!!"){
    val r = parse("3!!")
    assert( r === Fac(Fac(Three)) )
  }

  test("+3!"){
    val r = parse("+3!")
    assert( r === PrePlus(Fac(Three)) )
  }

  test("++3"){
    val r = parse("++3")
    assert( r === PrePlus(PrePlus(Three)) )
  }

  test("+-3"){
    val r = parse("+-3")
    assert( r === PrePlus(PreMinus(Three)) )
  }

  test("+sin 3"){
    val r = parse("+sin 3")
    assert( r === PrePlus(Sin(Three)) )
  }

  test("+1+2"){
    val r = parse("+1+2")
    assert( r === Plus(PrePlus(One),Two) )
  }

  test("+1-2"){
    val r = parse("+1-2")
    assert( r === Minus(PrePlus(One),Two) )
  }

  test("+1*2"){
    val r = parse("+1*2")
    assert( r === PrePlus(Mult(One,Two)) )
  }

  test("+1/2"){
    val r = parse("+1/2")
    assert( r === PrePlus(Div(One,Two)) )
  }

  test("+1\\2"){
    val r = parse("+1\\2")
    assert( r === PrePlus(Power(One,Two)) )
  }

  test("+2i"){
    val r = parse("+2i")
    assert( r === PrePlus(Mult(Two,I)) )
  }

  test("-3!"){
    val r = parse("-3!")
    assert( r === PreMinus(Fac(Three)) )
  }

  test("-+3"){
    val r = parse("-+3")
    assert( r === PreMinus(PrePlus(Three)) )
  }

  test("--3"){
    val r = parse("--3")
    assert( r === PreMinus(PreMinus(Three)) )
  }

  test("-sin 3"){
    val r = parse("-sin 3")
    assert( r === PreMinus(Sin(Three)) )
  }

  test("-1+2"){
    val r = parse("-1+2")
    assert( r === Plus(PreMinus(One),Two) )
  }

  test("-1-2"){
    val r = parse("-1-2")
    assert( r === Minus(PreMinus(One),Two) )
  }

  test("-1*2"){
    val r = parse("-1*2")
    assert( r === PreMinus(Mult(One,Two)) )
  }

  test("-1/2"){
    val r = parse("-1/2")
    assert( r === PreMinus(Div(One,Two)) )
  }

  test("-1\\2"){
    val r = parse("-1\\2")
    assert( r === PreMinus(Power(One,Two)) )
  }

  test("-2i"){
    val r = parse("-2i")
    assert( r === PreMinus(Mult(Two,I)) )
  }

  test("sin e!"){
    val r = parse("sin e!")
    assert( r === Sin(Fac(E)) )
  }

  test("sin +e"){
    val r = parse("sin +e")
    assert( r === Sin(PrePlus(E)) )
  }

  test("sin -e"){
    val r = parse("sin -e")
    assert( r === Sin(PreMinus(E)) )
  }

  test("sin sin e"){
    val r = parse("sin sin e")
    assert( r === Sin(Sin(E)) )
  }

  test("sin e + e"){
    val r = parse("sin e + e")
    assert( r === Plus(Sin(E), E) )
  }

  test("sin e - e"){
    val r = parse("sin e - e")
    assert( r === Minus(Sin(E), E) )
  }

  test("sin e * e"){
    val r = parse("sin e * e")
    assert( r === Mult(Sin(E), E) )
  }

  test("sin e / e"){
    val r = parse("sin e / e")
    assert( r === Div(Sin(E), E) )
  }

  test("sin e \\ e"){
    val r = parse("sin e \\ e")
    assert( r === Power(Sin(E), E) )
  }

  test("1+2!"){
    val r = parse("1+2!")
    assert( r === Plus(One,Fac(Two)) )
  }

  test("1++2"){
    val r = parse("1++2")
    assert( r === Plus(One,PrePlus(Two)) )
  }

  test("1+-2"){
    val r = parse("1+-2")
    assert( r === Plus(One,PreMinus(Two)) )
  }

  test("1+sin 2"){
    val r = parse("1+sin 2")
    assert( r === Plus(One,Sin(Two)) )
  }

  test("1+2+3"){
    val r = parse("1+2+3")
    assert( r === Plus(Plus(One,Two),Three) )
  }

  test("1+2-3"){
    val r = parse("1+2-3")
    assert( r === Minus(Plus(One,Two),Three) )
  }

  test("1+2*3"){
    val r = parse("1+2*3")
    assert( r === Plus(One,Mult(Two,Three)) )
  }

  test("1+2/3"){
    val r = parse("1+2/3")
    assert( r === Plus(One,Div(Two,Three)) )
  }

  test("1+2\\3"){
    val r = parse("1+2\\3")
    assert( r === Plus(One,Power(Two,Three)) )
  }

  test("1+2i"){
    val r = parse("1+2i")
    assert( r === Plus(One,Mult(Two,I)) )
  }

  test("1-2!"){
    val r = parse("1-2!")
    assert( r === Minus(One,Fac(Two)) )
  }

  test("1-+2"){
    val r = parse("1-+2")
    assert( r === Minus(One,PrePlus(Two)) )
  }

  test("1--2"){
    val r = parse("1--2")
    assert( r === Minus(One,PreMinus(Two)) )
  }

  test("1-sin 2"){
    val r = parse("1-sin 2")
    assert( r === Minus(One,Sin(Two)) )
  }

  test("1-2+3"){
    val r = parse("1-2+3")
    assert( r === Plus(Minus(One,Two),Three) )
  }

  test("1-2-3"){
    val r = parse("1-2-3")
    assert( r === Minus(Minus(One,Two),Three) )
  }

  test("1-2*3"){
    val r = parse("1-2*3")
    assert( r === Minus(One,Mult(Two,Three)) )
  }

  test("1-2/3"){
    val r = parse("1-2/3")
    assert( r === Minus(One,Div(Two,Three)) )
  }

  test("1-2\\3"){
    val r = parse("1-2\\3")
    assert( r === Minus(One,Power(Two,Three)) )
  }

  test("1-2i"){
    val r = parse("1-2i")
    assert( r === Minus(One,Mult(Two,I)) )
  }

  test("1*2!"){
    val r = parse("1*2!")
    assert( r === Mult(One,Fac(Two)) )
  }

  test("1*+2"){
    val r = parse("1*+2")
    assert( r === Mult(One,PrePlus(Two)) )
  }

  test("1*-2"){
    val r = parse("1*-2")
    assert( r === Mult(One,PreMinus(Two)) )
  }

  test("1*sin 2"){
    val r = parse("1*sin 2")
    assert( r === Mult(One,Sin(Two)) )
  }

  test("1*2+3"){
    val r = parse("1*2+3")
    assert( r === Plus(Mult(One,Two),Three) )
  }

  test("1*2-3"){
    val r = parse("1*2-3")
    assert( r === Minus(Mult(One,Two),Three) )
  }

  test("1*2/3"){
    val r = parse("1*2/3")
    assert( r === Div(Mult(One,Two),Three) )
  }

  test("1*2\\3"){
    val r = parse("1*2\\3")
    assert( r === Mult(One,Power(Two,Three)) )
  }

  test("1*2i"){
    val r = parse("1*2i")
    assert( r === Mult(One,Mult(Two,I)) )
  }

  test("1/2!"){
    val r = parse("1/2!")
    assert( r === Div(One,Fac(Two)) )
  }

  test("1/+2"){
    val r = parse("1/+2")
    assert( r === Div(One,PrePlus(Two)) )
  }

  test("1/-2"){
    val r = parse("1/-2")
    assert( r === Div(One,PreMinus(Two)) )
  }

  test("1/sin 2"){
    val r = parse("1/sin 2")
    assert( r === Div(One,Sin(Two)) )
  }

  test("1/2+3"){
    val r = parse("1/2+3")
    assert( r === Plus(Div(One,Two),Three) )
  }

  test("1/2-3"){
    val r = parse("1/2-3")
    assert( r === Minus(Div(One,Two),Three) )
  }

  test("1/2*3"){
    val r = parse("1/2*3")
    assert( r === Mult(Div(One,Two),Three) )
  }

  test("1/2/3"){
    val r = parse("1/2/3")
    assert( r === Div(Div(One,Two),Three) )
  }

  test("1/2\\3"){
    val r = parse("1/2\\3")
    assert( r === Div(One,Power(Two,Three)) )
  }

  test("1/2i"){
    val r = parse("1/2i")
    assert( r === Div(One,Mult(Two,I)) )
  }

  test("1\\2!"){
    val r = parse("1\\2!")
    assert( r === Power(One,Fac(Two)) )
  }

  test("1\\+2"){
    val r = parse("1\\+2")
    assert( r === Power(One,PrePlus(Two)) )
  }

  test("1\\-2"){
    val r = parse("1\\-2")
    assert( r === Power(One,PreMinus(Two)) )
  }

  test("1\\sin 2"){
    val r = parse("1\\sin 2")
    assert( r === Power(One,Sin(Two)) )
  }

  test("1\\2+3"){
    val r = parse("1\\2+3")
    assert( r === Plus(Power(One,Two),Three) )
  }

  test("1\\2-3"){
    val r = parse("1\\2-3")
    assert( r === Minus(Power(One,Two),Three) )
  }

  test("1\\2*3"){
    val r = parse("1\\2*3")
    assert( r === Mult(Power(One,Two),Three) )
  }

  test("1\\2/3"){
    val r = parse("1\\2/3")
    assert( r === Div(Power(One,Two),Three) )
  }

  test("1\\2\\3"){
    val r = parse("1\\2\\3")
    assert( r === Power(Power(One,Two),Three) )
  }

  test("1\\2i"){
    val r = parse("1\\2i")
    assert( r === Power(One,Mult(Two,I)) )
  }

  test("iz!"){
    val r = parse("iz!")
    assert( r === Mult(I, Fac(Z)) )
  }

  test("i sin π"){
    val r = parse("i sin π")
    assert( r === Mult(I, Sin(Pi)) )
  }

  test("iz + π"){
    val r = parse("iz + π")
    assert( r === Plus(Mult(I, Z), Pi) )
  }

  test("iz - π"){
    val r = parse("iz - π")
    assert( r === Minus(Mult(I, Z), Pi) )
  }

  test("iz * π"){
    val r = parse("iz * π")
    assert( r === Mult(Mult(I, Z), Pi) )
  }

  test("iz / π"){
    val r = parse("iz / π")
    assert( r === Div(Mult(I, Z), Pi) )
  }

  test("iz \\ π"){
    val r = parse("iz \\ π")
    assert( r === Power(Mult(I, Z), Pi) )
  }

  test("3iπ"){
    val r = parse("3iπ")
    assert( r === Mult(Mult(Three, I), Pi) )
  }

}
