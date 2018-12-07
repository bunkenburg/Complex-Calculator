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

class Trigonometry extends FunSuite {

  // sin -----------------------------------------

  test("sin(0)") {
    assert( sin(0) === 0 )
  }

  test("sin(π/2)") {
    assert( sin(π/2) === 1 )
  }

  test("sin(π)") {
    val z = sin(π)
    assert( z === 0 )
  }

  test("sin(3π/2)") {
    val c = sin(3*π/2)
    assert( c === -1 )
  }

  test("sin(2π)") {
    val c = sin(2*π)
    assert( c === 0 )
  }

  test("sin(2kπ)") {
    val k = 16
    val z = sin(2*k*π)
    assert( z === 0 +- 1e-14)
  }

  test("sin(i)") {
    val c = sin(i)
    assert( c === 1.1752011936438014 * i  )
  }

  test("sin(∞)") {
    assertThrows[ArithmeticException] {
      val c = sin(∞)
      assert(c === 0.0 )
    }
  }

  // cos -----------------------------------------

  test("cos(-π)") {
    assert( cos(-π) === -1 )
  }

  test("cos(-π : Complex)") {
    val z: Complex = -π
    assert( cos(z) === -1 )
  }

  test("cos(-π/2)") {
    val z: Complex = -π/2
    val y = cos(z)
    assert( y === 0 )
  }

  test("cos(0)") {
    assert( cos(0) === 1 )
  }

  test("cos(0 : Complex)") {
    assert( cos(0 : Complex) === 1 )
  }

  test("cos(π/2)") {
    assert( cos(π/2) === 0 )
  }

  test("cos(π)") {
    assert( cos(π) === -1 )
  }

  test("cos(3π/2)") {
    val c = cos(3*π/2)
    assert( c === 0 )
  }

  test("cos(2π)") {
    val c = cos(2*π)
    assert( c === 1 )
  }

  test("cos(2kπ)") {
    val k = 16
    val z = cos(2*k*π)
    assert( z === 1)
  }

  test("cos(i)") {
    val c = cos(i)
    assert( c === 1.5430806348 )
  }

  test("cos(∞)") {
    assertThrows[ArithmeticException] {
      val c = cos(∞)
      assert(c === 0.0 )
    }
  }

  // tan -----------------------------------------

  test("tan(0)") {
    assert( tan(0) === 0 )
  }

  test("tan(π/4)") {
    assert( tan(π/4) === 1 )
  }

  test("tan(π/2)") {
    assert( tan(π/2) === ∞ )
  }

  test("tan(3π/2)") {
    val c = tan(3*π/2)
    assert( c === 0 )
  }

  test("tan(2π)") {
    val c = tan(2*π)
    assert( c === 0 )
  }

  test("tan(2kπ)") {
    val k = 16
    val z = tan(2*k*π)
    assert( z === 0 +- 1e-14 )
  }

  test("tan(i)") {
    val c = tan(i)
    assert( c === 0.7615941559557649 * i )
  }

  test("tan(∞)") {
    assertThrows[ArithmeticException] {
      val c = tan(∞)
      assert(c === 0.0 )
    }
  }

}