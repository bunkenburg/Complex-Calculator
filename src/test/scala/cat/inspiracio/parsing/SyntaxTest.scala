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
//import Token._
import org.scalatest.FunSuite

class SyntaxTest extends FunSuite {

  test("3"){
    val t = parse("3")
    assert( t === C(3) )
  }

  test("i"){
    val t = parse("i")
    assert( t === C(i) )
  }

  test("∞"){
    val t = parse("∞")
    assert( t === C(∞) )
  }

  test("π"){
    val t = parse("π")
    assert( t === C(π) )
  }

  // --------------------------------

  test("z"){
    val t = parse("z")
    assert( t === V() )
  }

  // --------------------------------

  test("-3"){
    val t = parse("-3")
    assert( t === Neg(C(3)) )
  }

  test("+3"){
    val t = parse("+3")
    assert( t === C(3) )
  }

  test("3!"){
    val t = parse("3!")
    assert( t === Fac( C(3) ))
  }

  test("ln(3)"){
    val t = parse("ln(3)")
    assert( t === Ln( C(3) ) )
  }

  test("sinz") {
    val t = parse("sinz ")
    assert(t === Sin( V()))
  }

  test("sinπ"){
    val t = parse("sinπ")
    assert( t === Sin( C(π)) )
  }

  // -------------------------------------

  test("π + i "){
    val t = parse("π + i ")
    assert( t === Plus( C(π), C(i) ) )
  }

  test(" π - i "){
    val t = parse(" π - i ")
    assert( t === Minus(C(π), C(i)) )
  }

  test("  π * i "){
    val t = parse("  π * i ")
    assert( t === Mult(C(π), C(i) ) )
  }

  test("π / i "){
    val t = parse("π / i ")
    assert( t === Div(C(π), C(i)) )
  }

  test(" π \\ i "){
    val t = parse("π \\ i ")
    assert( t === Power(C(π), C(i) ) )
  }

  // ---------------------------------------

  /**may surprise: maybe better invisible multiplication */
  //XXX test("3e\\πi"){assert( parse("3e\\πi") === Mult(C(3), Power(C(e), Mult(C(Pi), C(i)))) )}

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
    assert( ax === Pi )
  }

}