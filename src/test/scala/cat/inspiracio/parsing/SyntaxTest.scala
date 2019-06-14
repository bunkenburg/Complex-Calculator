/*	Copyright 2018 Alexander Bunkenburg alex@inspiracio.cat
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
import Expression._
import org.scalatest.FunSuite

class SyntaxTest extends FunSuite {

  val I = C(i)
  val E = C(e)
  val Three = C(3)
  val Pi = C(π)

  // -------------------------------------------------

  test("3_"){assert( parse("3 ") === C(3) )}
  test("i"){assert( parse("i") === C(i) )}
  test("∞"){assert( parse("∞") === C(∞) )}
  test("π"){assert( parse("π") === C(π) )}
  test("z"){ assert( parse("z") === V() )}

  // --------------------------------

  test("-3"){assert( parse("-3") === Neg(C(3)) )}
  test("- 3"){assert( parse("-3") === Neg(C(3)) )}
  test("+3"){assert( parse("+3") === C(3) )}
  test("3!"){assert( parse("3!") === Fac( C(3) ))}
  test("ln(3)"){ assert( parse("ln(3)") === Ln( C(3) ) ) }
  test("sin z") {assert(parse("sin z ") === Sin( V()))}
  test("sin π"){assert( parse("sin π") === Sin( C(π)) )}

  // -------------------------------------

  test("π + i "){ assert( parse("π + i ") === Plus( C(π), C(i) ) )}
  test(" π - i "){ assert( parse(" π - i ") === Minus(C(π), C(i)) )}
  test("  π * i "){ assert( parse("  π * i ") === Mult(C(π), C(i) ) )}
  test("π / i "){ assert( parse("π / i ") === Div(C(π), C(i)) )}
  test(" π \\ i "){ assert( parse("π \\ i ") === Power(C(π), C(i) ) )}

  // ---------------------------------------

  /** may surprise: maybe better invisible multiplication */
  test("3e\\πi"){
    val expected = Mult(C(3), Power(C(e), Mult(Pi, C(i))))
    val real = Power(Mult(Three,E), Mult(Pi,I))
    assert( parse("3e\\πi") === real )
  }

  // ----------------------------------------

  test("e\\πiz"){
    val f = parse("e\\πiz")
    val fz = f(1)
    assert( fz === -1 )
  }

  test("e\\πi + 1"){
    val f = parse("e\\πi + 1")
    val fz = f(7863)
    assert( fz === 0 )
  }

  test("-1 \\ 0.5"){
    val f = parse("-1 \\ 0.5")
    val fz = f(7863)
    assert( fz === i )
  }

  /** This is where it all started. */
  test("(-1) \\ 0.5"){
    val a: Complex = -1
    val b: Complex = 0.5
    val c = a \ b
    assert( c === i )

    val f = parse("(-1) \\ 0.5")
    val fz = f(7863)    //Polar(1, - pi/2)
    assert( fz === i )
  }

  test("bla"){
    val t: Complex = Cartesian(-1.0, -0.0)   //im=-0
    val Polar(mx,ax) = t
    assert( mx === 1 )
    assert( ax === π )
  }

}