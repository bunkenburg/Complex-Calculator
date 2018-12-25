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

  def alt(z: Complex) = -1 / conj(z)

  test("alt 0"){
    val z = 0
    assert( ∞ === alt(z) )
    assert( opp(z) === alt(z) )
  }

  test("alt ∞"){
    val z = ∞
    assert( 0 === alt(z) )
    assert( opp(z) === alt(z) )
  }

  test("alt 1"){
    val z = 1
    assert( -1 === alt(z) )
    assert( opp(z) === alt(z) )
  }

  test("alt i"){
    val z = i
    assert( -i === alt(z) )
    assert( opp(z) === alt(z) )
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

  // abs -----------------------------------------

  test("abs(0)") {
    val c: Complex = 0
    assert( abs(c) === 0 )
  }

  test("abs(1)") {
    val c: Complex = 1
    assert( abs(c) === 1 )
  }

  test("abs(-1)") {
    val c: Complex = -1
    assert( abs(c) === 1 )
  }

  test("abs(i)") {
    val c: Complex = i
    assert( abs(i) === 1 )
  }

  test("abs(-i)") {
    val c = -i
    assert( abs(c) === 1 )
  }

  test("abs(3+4i)") {
    val z = 3+4*i
    assert( abs(z) === 5 )
  }

  test("abs(∞)") {
    assertThrows[ArithmeticException] {
      val c = abs(∞)
      assert(c === 8)
    }
  }

  // lazy vals ------------------------

  test("lazy argument"){
    val z = 3 + 4*i
    val Polar(m,a) = z  //first evaluation
    val Polar(m1,a1) = z  //should be free
    assert( m == m1)
    assert( a == a1 )
  }

  def arg(z: Complex) = {
    val Polar(_, a) = z
    a
  }

  def f(a: Double): Double = arg(Polar(1, a))

  test("principal argument"){
    Complex.setArgPrincipal()

    val as = List(0, 1, 2, 3, π, 4)
    val as1 = as map f

    assert( as1 === List(0, 1, 2, 3, π, -2.28318530717958670) )
  }

  test("continuous argument"){
    Complex.setArgContinuous()

    val as = List(0, 1, 2, 3, π, 4)
    val as1 = as map f

    assert( as1 === List(0, 1, 2, 3, π, 3.9999999999999996) )

    Complex.setArgPrincipal()
  }
}