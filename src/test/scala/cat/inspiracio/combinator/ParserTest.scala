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

class ParserTest extends FunSuite {

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

  // simple things --------------------------

  test("i"){assert( p("i") === I )}
  test("e"){assert( p("e") === E )}
  test("π"){assert( p("π") === Pi )}
  test("∞"){ assert( p("∞") === Inf )}

  test("3.16"){
    val r = p("3.16")
    assert( r === C(3.16) )
  }

  test("316"){
    val r = p("316")
    assert( r === C(316) )
  }

  test("z"){
    val r = p("z")
    assert( r === Z )
  }

  test("x"){
    val r = p("x")
    assert( r === X )
  }

  test("3!"){
    val r = p("3!")
    assert( r === Fac(Three) )
  }

  test("+3"){
    val r = p("+3")
    assert( r === Three )
  }

  test("-3"){
    val r = p("-3")
    assert( r === Neg(Three) )
  }

  test("arg(i)"){
    val r = p("arg(i)")
    assert( r === Arg(I) )
  }

  test("conj i"){
    val r = p("conj i")
    assert( r === Conj(I) )
  }

  test("cos 0.5"){
    val r = p("cos 0.5 ")
    assert( r === Cos(Half) )
  }

  test("cosh π"){
    val r = p("cosh π")
    assert( r === Cosh(Pi) )
  }

  test("exp 0"){
    val r = p("exp 0")
    assert( r === Exp(Zero) )
  }

  test("Im(z)"){
    val r = p("Im(z)")
    assert( r === parsing.Im(Z) )
  }

  test("ln(z)"){
    val r = p("ln(z)")
    assert( r === Ln(Z) )
  }

  test("mod(z)"){
    val r = p("mod(z)")
    assert( r === Mod(Z) )
  }

  test("|z|"){
    val r = p("|z|")
    assert( r === Mod(Z) )
  }

  test("opp z"){
    val r = p("opp z")
    assert( r === Opp(Z) )
  }

  test("Re z"){
    val r = p("Re z")
    assert( r === parsing.Re(Z) )
  }

  test("sin π"){
    val r = p("sin π")
    assert( r === Sin(Pi) )
  }

  test("sinh π"){
    val r = p("sinh π")
    assert( r === Sinh(Pi) )
  }

  test("tan π "){
    val r = p("tan π ")
    assert( r === Tan(Pi) )
  }

  test("tanh π "){
    val r = p("tanh π ")
    assert( r === Tanh(Pi) )
  }

  test("3+i"){
    val r = p("3+i")
    assert( r === Plus(Three, I) )
  }

  test("3-i"){
    val r = p("3-i")
    assert( r === Minus(Three, I) )
  }

  test("3*i"){
    val r = p("3*i")
    assert( r === Mult(Three, I) )
  }

  test("3/i"){
    val r = p("3/i")
    assert( r === Div(Three, I) )
  }

  test("e\\i"){
    val r = p("e\\i")
    assert( r === Power(E, I) )
  }

  test("πi"){
    val r = p("πi")
    assert( r === Mult(Pi, I) )
  }

  // combinations

  test("  π with white space"){
    val r = p("  π  ")
    assert( r === Pi )
  }

  test("  3π  "){
    val r = p("  3π  ")
    assert( r === Mult(Three, Pi) )
  }

  test("  ( π)  "){
    val r = p("  ( π)  ")
    assert( r === Pi )
  }

  test("(2z)!"){
    val r = p("(2z)!")
    assert( r === Fac(Mult(Two, Z)) )
  }

  test("(2x + 1)!"){
    val r = p("(2x + 1)!")
    assert( r === Fac(Plus(Mult(Two, X), One)) )
  }

  /** needs space-multiplication */
  //XXX test("3 sin 0"){ assert( p("3 sin 0") === Mult(Three, Sin(Zero)) )}

  test("3 * sin 0"){
    val r = p("3 * sin 0")
    assert( r === Mult(Three, Sin(Zero)) )
  }

  test("sin ei"){
    val r = p("sin ei")
    assert( r === Sin(Mult(E, I)) )
  }

  // various expressions from Maths books -----

  test("e\\ix*z"){
    val r = p("e\\ix*z")
    assert( r === Mult(Power(E, Mult(I,X)), Z) )
  }

  test("2πi"){
    val r = p("2πi")
    assert( r === Mult(Mult(Two, Pi), I) )
  }

  test("sin z"){
    val r = p("sin z")
    assert( r === Sin(Z) )
  }

  test("e\\iz"){
    val r = p("e\\iz")
    assert( r === Power(E, Mult(I,Z)) )
  }

  /** needs better-prefix */
  test("e\\-iz"){assert( p("e\\-iz") === Power(E, Neg(Mult(I,Z))) )}

  test("e\\(-iz)"){
    val r = p("e\\(-iz)")
    assert( r === Power(E, Neg(Mult(I,Z))) )
  }

  /** Needs space-multiplication   */
  //XXX test("sin xz sin zx"){ assert( p("sin xz sin zx") === Mult(Sin(Mult(X,Z)), Sin(Mult(Z,X))) )}

  test("sin(xz)*sin(zx)"){
    val r = p("sin(xz)*sin(zx)")
    assert( r === Mult(Sin(Mult(X,Z)), Sin(Mult(Z,X))) )
  }

  /** needs space-multiplication */
  //XXX test("cos z + i sin z"){ assert( p("cos z + i sin z") === Plus(Cos(Z), Mult(I, Sin(Z))) )}

  test("cos z + i*sin z"){
    val r = p("cos z + i*sin z")
    assert( r === Plus(Cos(Z), Mult(I, Sin(Z))) )
  }

  test("cos 2z"){
    val r = p("cos 2z")
    assert( r === Cos(Mult(Two,Z)) )
  }

  /* needs space-multiplication and functions without parentheses
  test("2 cos z sin z"){
    val r = parse("2 cos z sin z")
    assert( r === Mult(Mult(Two, Cos(Z)), Sin(Z)) )
  }
   */

  test("2 * cos z * sin z"){
    val r = p("2 * cos z * sin z")
    assert( r === Mult(Mult(Two, Cos(Z)), Sin(Z)) )
  }

  test("1*2*3"){
    val r = p("1*2*3")
    assert( r === Mult(Mult(One, Two), Three) )
  }

  /** needs more implicit multiplication */
  //XXX test("ln z + (2z + 1)πi"){ assert( p("ln z + (2z + 1)πi") === Plus(Ln(Z),Mult(Plus(Mult(Two,Z),One),Mult(Pi,I))) ) }

  /** needs more implicit multiplication */
  //XXX test("(2z + 1)πi"){assert( p("(2z + 1)πi") === Mult(Plus(Mult(Two,Z),One),Mult(Pi,I)) )}

  /** needs more implicit multiplication */
  //XXX test("(2)πi"){assert( p("(2)πi") === Mult(Two,Mult(Pi,I)) )}

  /** needs space-multiplication */
  //XXX test("(cos z + i sin z)\\x"){assert( p("(cos z + i sin z)\\x") === Power(Plus(Cos(Z), Mult(I, Sin(Z))), X) )}

  test("(cos z + i * sin z)\\x"){
    val r = p("(cos z + i * sin z)\\x")
    assert( r === Power(Plus(Cos(Z), Mult(I, Sin(Z))), X) )
  }

  /** needs space-multiplication */
  //XXX test("cos zx +  i sin zx"){assert( p("cos zx +  i sin zx") === Plus(Cos(Mult(X,Z)), Mult(I,Sin(Mult(Z,X)))) )}

  test("cos zx + i * sin zx"){
    val r = p("cos zx + i * sin zx")
    assert( r === Plus(Cos(Mult(X,Z)), Mult(I,Sin(Mult(Z,X)))) )
  }

  /** needs invisible-multiplication */
  //XXX test("exp(x)exp(z)"){ assert( p("exp(x)exp(z)") === Mult(Exp(X), Exp(Z)) )}

  test("exp(x)*exp(z)"){
    val r = p("exp(x)*exp(z)")
    assert( r === Mult(Exp(X), Exp(Z)) )
  }

  /** needs space-multiplication */
  //XXX test("sin x cos z + cos x sin z"){ assert( p("sin x cos z + cos x sin z") === Plus(Mult(Sin(X),Cos(Z)),Mult(Cos(X),Sin(Z))) )}

  test("sin x * cos z + cos x * sin z"){
    val r = p("sin x * cos z + cos x * sin z")
    assert( r === Plus(Mult(Sin(X),Cos(Z)),Mult(Cos(X),Sin(Z))) )
  }

  test("cos 2π"){
    val r = p("cos 2π")
    assert( r === Cos(Mult(Two,Pi)) )
  }

  test("cos ix"){
    val r = p("cos ix")
    assert( r === Cos(Mult(I,X)) )
  }

  test("sin πz"){
    val r = p("sin πz")
    assert( r === Sin(Mult(Pi,Z)) )
  }

  test("sinh|πz|"){
    val r = p("sinh|πz|")
    assert( r === Sinh(Mod(Mult(Pi,Z))) )
  }

  /** needs more implicit multiplication */
  //XXX test("2πi(e+1)"){assert( p("2πi(e+1)") === Mult(Mult(Mult(Two,Pi),I), Plus(E,One)) )}

  // from my big table -------------------------

  test("3!!"){
    val r = p("3!!")
    assert( r === Fac(Fac(Three)) )
  }

  test("+3!"){
    val r = p("+3!")
    assert( r === Fac(Three) )
  }

  test("++3"){
    val r = p("++3")
    assert( r === Three )
  }

  test("+-3"){
    val r = p("+-3")
    assert( r === Neg(Three) )
  }

  test("+sin 3"){
    val r = p("+sin 3")
    assert( r === Sin(Three) )
  }

  test("+1+2"){
    val r = p("+1+2")
    assert( r === Plus(One,Two) )
  }

  test("+1-2"){
    val r = p("+1-2")
    assert( r === Minus(One,Two) )
  }

  test("+1*2"){
    val r = p("+1*2")
    assert( r === Mult(One,Two) )
  }

  test("+1/2"){
    val r = p("+1/2")
    assert( r === Div(One,Two) )
  }

  test("+1\\2"){
    val r = p("+1\\2")
    assert( r === Power(One,Two) )
  }

  test("+2i"){
    val r = p("+2i")
    assert( r === Mult(Two,I) )
  }

  test("-3!"){
    val r = p("-3!")
    assert( r === Neg(Fac(Three)) )
  }

  test("-+3"){
    val r = p("-+3")
    assert( r === Neg(Three) )
  }

  test("--3"){
    val r = p("--3")
    assert( r === Neg(Neg(Three)) )
  }

  test("-sin 3"){
    val r = p("-sin 3")
    assert( r === Neg(Sin(Three)) )
  }

  test("-1+2"){
    val r = p("-1+2")
    assert( r === Plus(Neg(One),Two) )
  }

  test("-1-2"){
    val r = p("-1-2")
    assert( r === Minus(Neg(One),Two) )
  }

  test("-1*2"){ assert( p("-1*2") === Mult(Neg(One),Two) )}
  test("-1/2"){ assert( p("-1/2") === Div(Neg(One),Two) ) }
  test("-1\\2"){ assert( p("-1\\2") === Power(Neg(One),Two) )}

  test("-2i"){
    val r = p("-2i")
    assert( r === Neg(Mult(Two,I)) )
  }

  test("sin e!"){
    val r = p("sin e!")
    assert( r === Sin(Fac(E)) )
  }

  /** needs better-prefix */
  test("sin +e"){ assert( p("sin +e") === Sin(E) )}

  test("sin(+e)"){
    val r = p("sin(+e)")
    assert( r === Sin(E) )
  }

  test("sin -e"){ assert( p("sin -e") === Sin(Neg(E)) )}

  test("sin(-e)"){
    val r = p("sin(-e)")
    assert( r === Sin(Neg(E)) )
  }

  /** needs functions without parentheses */
  test("sin sin e"){ assert( p("sin sin e") === Sin(Sin(E)) )}

  test("sin(sin e)"){
    val r = p("sin(sin e)")
    assert( r === Sin(Sin(E)) )
  }

  test("sin e + e"){
    val r = p("sin e + e")
    assert( r === Plus(Sin(E), E) )
  }

  test("sin e - e"){
    val r = p("sin e - e")
    assert( r === Minus(Sin(E), E) )
  }

  test("sin e * e"){
    val r = p("sin e * e")
    assert( r === Mult(Sin(E), E) )
  }

  test("sin e / e"){
    val r = p("sin e / e")
    assert( r === Div(Sin(E), E) )
  }

  test("sin e \\ e"){
    val r = p("sin e \\ e")
    assert( r === Power(Sin(E), E) )
  }

  test("1+2!"){
    val r = p("1+2!")
    assert( r === Plus(One,Fac(Two)) )
  }

  test("1++2"){
    val r = p("1++2")
    assert( r === Plus(One,Two) )
  }

  test("1+-2"){
    val r = p("1+-2")
    assert( r === Plus(One,Neg(Two)) )
  }

  test("1+sin 2"){
    val r = p("1+sin 2")
    assert( r === Plus(One,Sin(Two)) )
  }

  test("1+2+3"){
    val r = p("1+2+3")
    assert( r === Plus(Plus(One,Two),Three) )
  }

  test("1+2-3"){
    val r = p("1+2-3")
    assert( r === Minus(Plus(One,Two),Three) )
  }

  test("1+2*3"){
    val r = p("1+2*3")
    assert( r === Plus(One,Mult(Two,Three)) )
  }

  test("1+2/3"){
    val r = p("1+2/3")
    assert( r === Plus(One,Div(Two,Three)) )
  }

  test("1+2\\3"){
    val r = p("1+2\\3")
    assert( r === Plus(One,Power(Two,Three)) )
  }

  test("1+2i"){
    val r = p("1+2i")
    assert( r === Plus(One,Mult(Two,I)) )
  }

  test("1-2!"){
    val r = p("1-2!")
    assert( r === Minus(One,Fac(Two)) )
  }

  test("1-+2"){
    val r = p("1-+2")
    assert( r === Minus(One,Two) )
  }

  test("1--2"){
    val r = p("1--2")
    assert( r === Minus(One,Neg(Two)) )
  }

  test("1-sin 2"){
    val r = p("1-sin 2")
    assert( r === Minus(One,Sin(Two)) )
  }

  test("1-2+3"){
    val r = p("1-2+3")
    assert( r === Plus(Minus(One,Two),Three) )
  }

  test("1-2-3"){
    val r = p("1-2-3")
    assert( r === Minus(Minus(One,Two),Three) )
  }

  test("1-2*3"){
    val r = p("1-2*3")
    assert( r === Minus(One,Mult(Two,Three)) )
  }

  test("1-2/3"){
    val r = p("1-2/3")
    assert( r === Minus(One,Div(Two,Three)) )
  }

  test("1-2\\3"){
    val r = p("1-2\\3")
    assert( r === Minus(One,Power(Two,Three)) )
  }

  test("1-2i"){
    val r = p("1-2i")
    assert( r === Minus(One,Mult(Two,I)) )
  }

  test("1*2!"){
    val r = p("1*2!")
    assert( r === Mult(One,Fac(Two)) )
  }

  /* needs better-prefix
  test("1*+2"){
    val r = parse("1*+2")
    assert( r === Mult(One,PrePlus(Two)) )
  }
   */

  test("1*(+2)"){
    val r = p("1*(+2)")
    assert( r === Mult(One,Two) )
  }

  /** needs better-prefix */
  test("1*-2"){ assert( p("1*-2") === Mult(One,Neg(Two)) )}

  test("1*(-2)"){
    val r = p("1*(-2)")
    assert( r === Mult(One,Neg(Two)) )
  }

  test("1*sin 2"){
    val r = p("1*sin 2")
    assert( r === Mult(One,Sin(Two)) )
  }

  test("1*2+3"){
    val r = p("1*2+3")
    assert( r === Plus(Mult(One,Two),Three) )
  }

  test("1*2-3"){
    val r = p("1*2-3")
    assert( r === Minus(Mult(One,Two),Three) )
  }

  test("1*2/3"){
    val r = p("1*2/3")
    assert( r === Div(Mult(One,Two),Three) )
  }

  test("1*2\\3"){
    val r = p("1*2\\3")
    assert( r === Mult(One,Power(Two,Three)) )
  }

  test("1*2i"){
    val r = p("1*2i")
    assert( r === Mult(One,Mult(Two,I)) )
  }

  test("1/2!"){
    val r = p("1/2!")
    assert( r === Div(One,Fac(Two)) )
  }

  /** needs better-prefix */
  test("1/+2"){assert( p("1/+2") === Div(One,Two) )}

  test("1/(+2)"){
    val r = p("1/(+2)")
    assert( r === Div(One,Two) )
  }

  /** needs better-prefix */
  test("1/-2"){assert( p("1/-2") === Div(One,Neg(Two)) )}

  test("1/(-2)"){
    val r = p("1/(-2)")
    assert( r === Div(One,Neg(Two)) )
  }

  test("1/sin 2"){
    val r = p("1/sin 2")
    assert( r === Div(One,Sin(Two)) )
  }

  test("1/2+3"){
    val r = p("1/2+3")
    assert( r === Plus(Div(One,Two),Three) )
  }

  test("1/2-3"){
    val r = p("1/2-3")
    assert( r === Minus(Div(One,Two),Three) )
  }

  test("1/2*3"){
    val r = p("1/2*3")
    assert( r === Mult(Div(One,Two),Three) )
  }

  test("1/2/3"){
    val r = p("1/2/3")
    assert( r === Div(Div(One,Two),Three) )
  }

  test("1/2\\3"){
    val r = p("1/2\\3")
    assert( r === Div(One,Power(Two,Three)) )
  }

  test("1/2i"){
    val r = p("1/2i")
    assert( r === Div(One,Mult(Two,I)) )
  }

  test("1\\2!"){
    val r = p("1\\2!")
    assert( r === Power(One,Fac(Two)) )
  }

  /** needs better-prefix */
  test("1\\+2"){assert(  p("1\\+2") === Power(One,Two) )}

  test("1\\(+2)"){
    val r = p("1\\(+2)")
    assert( r === Power(One,Two) )
  }

  /** needs better-prefix */
  test("1\\-2"){assert( p("1\\-2") === Power(One,Neg(Two)) )}

  test("1\\(-2)"){
    val r = p("1\\(-2)")
    assert( r === Power(One,Neg(Two)) )
  }

  test("1\\sin 2"){
    val r = p("1\\sin 2")
    assert( r === Power(One,Sin(Two)) )
  }

  test("1\\2+3"){
    val r = p("1\\2+3")
    assert( r === Plus(Power(One,Two),Three) )
  }

  test("1\\2-3"){
    val r = p("1\\2-3")
    assert( r === Minus(Power(One,Two),Three) )
  }

  test("1\\2*3"){
    val r = p("1\\2*3")
    assert( r === Mult(Power(One,Two),Three) )
  }

  test("1\\2/3"){
    val r = p("1\\2/3")
    assert( r === Div(Power(One,Two),Three) )
  }

  test("1\\2\\3"){
    val r = p("1\\2\\3")
    assert( r === Power(Power(One,Two),Three) )
  }

  test("1\\2i"){
    val r = p("1\\2i")
    assert( r === Power(One,Mult(Two,I)) )
  }

  //XXX test("iz!"){assert( p("iz!") === Mult(I, Fac(Z)) )}

  /** needs space-multiplication */
  //XXX test("i sin π"){assert( p("i sin π") === Mult(I, Sin(Pi)) )}

  test("i * sin π"){
    val r = p("i * sin π")
    assert( r === Mult(I, Sin(Pi)) )
  }

  test("iz + π"){
    val r = p("iz + π")
    assert( r === Plus(Mult(I, Z), Pi) )
  }

  test("iz - π"){
    val r = p("iz - π")
    assert( r === Minus(Mult(I, Z), Pi) )
  }

  test("iz * π"){
    val r = p("iz * π")
    assert( r === Mult(Mult(I, Z), Pi) )
  }

  test("iz / π"){
    val r = p("iz / π")
    assert( r === Div(Mult(I, Z), Pi) )
  }

  test("iz \\ π"){
    val r = p("iz \\ π")
    assert( r === Power(Mult(I, Z), Pi) )
  }

  test("3iπ"){
    val r = p("3iπ")
    assert( r === Mult(Mult(Three, I), Pi) )
  }

  test("e\\-πi"){
    assert( p("e\\-πi") === Power(E,Neg(Mult(Pi,I))) )
  }

}
