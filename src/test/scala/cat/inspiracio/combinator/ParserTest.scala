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

  def parse(s: String): Expression = {
    val p = new Parser
    p.parseAll(p.expr, s).get
  }

  // simple things --------------------------

  test("i"){
    val r = parse("i")
    assert( r === C(i) )
  }

  test("e"){
    val r = parse("e")
    assert( r === C(e) )
  }

  test("π"){
    val r = parse("π")
    assert( r === C(π) )
  }

  test("∞"){
    val r = parse("∞")
    assert( r === C(∞) )
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
    assert( r === V() )
  }

  test("x"){
    val r = parse("x")
    assert( r === V() )
  }

  test("3!"){
    val r = parse("3!")
    assert( r === Fac(C(3)) )
  }

  test("+3"){
    val r = parse("+3")
    assert( r === PrePlus(C(3)) )
  }

  test("-3"){
    val r = parse("-3")
    assert( r === PreMinus(C(3)) )
  }

  test("arg(i)"){
    val r = parse("arg(i)")
    assert( r === Arg(C(i)) )
  }

  test("conj i"){
    val r = parse("conj i")
    assert( r === Conj(C(i)) )
  }

  test("cos 0.5"){
    val r = parse("cos 0.5 ")
    assert( r === Cos(C(0.5)) )
  }

  test("cosh π"){
    val r = parse("cosh π")
    assert( r === Cosh(C(π)) )
  }

  test("exp 0"){
    val r = parse("exp 0")
    assert( r === Exp(C(0)) )
  }

  test("Im(z)"){
    val r = parse("Im(z)")
    assert( r === parsing.Im(V()) )
  }

  test("ln(z)"){
    val r = parse("ln(z)")
    assert( r === Ln(V()) )
  }

  test("mod(z)"){
    val r = parse("mod(z)")
    assert( r === Mod(V()) )
  }

  test("opp z"){
    val r = parse("opp z")
    assert( r === Opp(V()) )
  }

  test("Re z"){
    val r = parse("Re z")
    assert( r === parsing.Re(V()) )
  }

  test("sinπ"){
    val r = parse("sinπ")
    assert( r === Sin(C(π)) )
  }

  test("sinhπ"){
    val r = parse("sinhπ")
    assert( r === Sinh(C(π)) )
  }

  test("tanπ"){
    val r = parse("tanπ")
    assert( r === Tan(C(π)) )
  }

  test("tanhπ"){
    val r = parse("tanhπ")
    assert( r === Tanh(C(π)) )
  }

  test("3+i"){
    val r = parse("3+i")
    assert( r === Plus(C(3), C(i)) )
  }

  test("3-i"){
    val r = parse("3-i")
    assert( r === Minus(C(3), C(i)) )
  }

  test("3*i"){
    val r = parse("3*i")
    assert( r === Mult(C(3), C(i)) )
  }

  test("3/i"){
    val r = parse("3/i")
    assert( r === Div(C(3), C(i)) )
  }

  test("e\\i"){
    val r = parse("e\\i")
    assert( r === Power(C(e), C(i)) )
  }

  test("πi"){
    val r = parse("πi")
    assert( r === Mult(C(π), C(i)) )
  }

  // combinations

  test("  π with white space"){
    val r = parse("  π  ")
    assert( r === C(π) )
  }

  test("  3π  "){
    val r = parse("  3π  ")
    assert( r === Mult(C(3), C(π)) )
  }

  test("  ( π)  "){
    val r = parse("  ( π)  ")
    assert( r === C(π) )
  }

  test("i3!"){
    val r = parse("i3!")
    assert( r === Fac(Mult(C(i), C(3))) )
  }

  test("3!i"){
    val r = parse("3!i")
    assert( r === Mult(Fac(C(3)), C(i)) )
  }

  test("3sin 0"){
    val r = parse("3sin 0")
    assert( r === Mult(C(3), Sin(C(0))) )
  }

  test("sin ei"){
    val r = parse("sin ei")
    assert( r === Sin(Mult(C(e), C(i))) )
  }

  // \

}
