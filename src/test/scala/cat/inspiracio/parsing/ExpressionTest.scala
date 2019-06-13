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
package cat.inspiracio.parsing

import cat.inspiracio.complex
import cat.inspiracio.complex._
import cat.inspiracio.parsing
import org.scalatest.FunSuite

/** Tests evaluation of expressions */
class ExpressionTest extends FunSuite {

  val I = C(i)
  val Two = C(2)
  val Four = C(4)

  // -----------------------------------

  test("i"){
    val e = C(i)
    assert( e(0) === i )
  }

  // -----------------------------------

  test("arg i"){
    val e = Arg(I)
    assert( e(0) === π/2 )
  }

  test("conj i"){
    val e = Conj(I)
    assert( e(0) === conj(i) )
  }

  test("cos i"){
    val e = Cos(I)
    assert( e(0) === cos(i) )
  }

  test("cosh i"){
    val e = Cosh(I)
    assert( e(0) === cosh(i) )
  }

  test("exp i"){
    val e = Exp(I)
    assert( e(0) === exp(i) )
  }

  test("4!"){
    val e = Fac(Four)
    assert( e(0) === 24 )
  }

  test("Im(2+i)"){
    val e = parsing.Im(Plus(Two,I))
    assert( e(0) === 1 )
  }

  test("ln(2+i)"){
    val e = Ln(Plus(Two,I))
    assert( e(0) === ln(2+i) )
  }

  test("|2+i|"){
    val e = Mod(Plus(Two,I))
    assert( e(0) === abs(2+i) )
  }

  test("opp(2+i)"){
    val e = Opp(Plus(Two,I))
    assert( e(0) === opp(2+i) )
  }

  test("-(2+i)"){
    val e = Neg(Plus(Two,I))
    assert( e(0) === -(2+i) )
  }

  test("Re(2+i)"){
    val e = parsing.Re(Plus(Two,I))
    assert( e(0) === complex.Re(2+i) )
  }

  test("Sin(2+i)"){
    val e = Sin(Plus(Two,I))
    assert( e(0) === sin(2+i) )
  }

  test("Sinh(2+i)"){
    val e = Sinh(Plus(Two,I))
    assert( e(0) === sinh(2+i) )
  }

  test("Tan(2+i)"){
    val e = Tan(Plus(Two,I))
    assert( e(0) === tan(2+i) )
  }

  test("Tanh(2+i)"){
    val e = Tanh(Plus(Two,I))
    assert( e(0) === tanh(2+i) )
  }

  // -----------------------------------

  test("2+i"){
    val e = Plus(Two,I)
    assert( e(0) === 2+i )
  }

  test("2-i"){
    val e = Minus(Two,I)
    assert( e(0) === 2-i )
  }

  test("2*i"){
    val e = Mult(Two,I)
    assert( e(0) === 2*i )
  }

  test("2/i"){
    val e = Div(Two,I)
    assert( e(0) === 2/i )
  }

  test("2\\i"){
    val e = Power(Two,I)
    assert( e(0) === 2\i )
  }

}
