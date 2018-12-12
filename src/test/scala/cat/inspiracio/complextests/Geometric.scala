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
package cat.inspiracio.complextests

import org.scalatest.FunSuite
import cat.inspiracio.complex._

class Geometric extends FunSuite {

  // complex conjugate -----------------------------------------

  test("conj(0)") {
    assert( conj(0) === 0 )
  }

  test("conj(π)") {
    val z = conj(π)
    assert( z === π )
  }

  test("conj(i)") {
    val c = conj(i)
    assert( c === -i  )
  }

  test("conj(3+i)") {
    val c = conj(3+i)
    assert( c === 3-i  )
  }

  test("conj(∞)") {
      val c = conj(∞)
      assert(c === ∞ )
  }

  // opp -----------------------------------------

  test("opp(-π)") {
    assert( opp(-π) === 0.3183098861837907 )
  }

  test("opp(-π/2)") {
    val z: Complex = -π/2
    val y = opp(z)
    assert( y === 0.6366197723675814 )
  }

  test("opp(0)") {
    assert( opp(0) === ∞ )
  }

  test("opp(π/2)") {
    assert( opp(π/2) === -0.6366197723675814 )
  }

  test("opp(π)") {
    assert( opp(π) === -0.3183098861837907 )
  }

  test("opp(2 + 3.2i)") {
    val c = opp(2 + 3.2*i)
    assert( c === -0.14044943820224712 -0.22471910112359553*i )
  }

  test("opp(2kπ)") {
    val k = 16
    val z = opp(2*k*π)
    assert( z === -0.009947183943243459 )
  }

  test("opp(i)") {
    val c = opp(i)
    assert( c === -i )
  }

  test("opp(∞)") {
      val c = opp(∞)
      assert(c === 0 )
  }

  // reciprocal -----------------------------------------

  test("reciprocal(0)") {
    assert( reciprocal(0) === ∞ )
  }

  test("reciprocal(π/4)") {
    assert( reciprocal(π/4) === -1.2732395447351628 )
  }

  test("reciprocal(π/2)") {
    assert( reciprocal(π/2) === -0.6366197723675814 )
  }

  test("reciprocal(3π/2)") {
    val c = reciprocal(3*π/2)
    assert( c === -0.2122065907891938 )
  }

  test("reciprocal(3+2i)") {
    val z = reciprocal(3 + 2*i)
    assert( z === -0.2307692307692308 -0.1538461538461538*i )
  }

  test("reciprocal(i)") {
    val c = reciprocal(i)
    assert( c === -i )
  }

  test("reciprocal(∞)") {
      val c = reciprocal(∞)
      assert(c === 0 )
  }

  // factorial -----------------------------------------

  test("fac(0)") {
    assert( fac(0) === 1 )
  }

  test("fac(1)") {
    assert( fac(1) === 1 )
  }

  test("fac(2)") {
    assert( fac(2) === 2 )
  }

  test("fac(3)") {
    val c = fac(3)
    assert( c === 6 )
  }

  test("fac(4)") {
    val c = fac(4)
    assert( c === 24 )
  }

  test("fac(5)") {
    val z = fac(5)
    assert( z === 120 )
  }

  test("fac(20)") {
    val c = fac(20)
    assert( c === 2.43290200817664E18 )
  }

  test("fac(∞)") {
      val c = fac(∞)
      assert(c === ∞ )
  }

}