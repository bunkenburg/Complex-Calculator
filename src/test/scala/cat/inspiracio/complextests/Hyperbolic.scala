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

class Hyperbolic extends FunSuite {

  // sin -----------------------------------------

  test("sinh(0)") {
    assert( sinh(0) === 0 )
  }

  test("sinh(π/2)") {
    assert( sinh(π/2) === 2.3012989023 )
  }

  test("sinh(π)") {
    val z = sinh(π)
    assert( z === 11.5487393573 )
  }

  test("sinh(3π/2)") {
    val c = sinh(3*π/2)
    assert( c === 55.6543975994 )
  }

  test("sinh(2π)") {
    val c = sinh(2*π)
    assert( c === 267.744894041 )
  }

  test("sinh(i)") {
    val c = sinh(i)
    assert( c === 0.8414709848078965 * i  )
  }

  test("sinh(∞)") {
    assertThrows[ArithmeticException] {
      val c = sinh(∞)
      assert(c === 0.0 )
    }
  }

  // cosh -----------------------------------------

  test("cosh(0)") {
    assert( cosh(0) === 1 )
  }

  test("cosh(π/2)") {
    assert( cosh(π/2) === 2.5091784787 )
  }

  test("cosh(π)") {
    assert( cosh(π) === 11.5919532755 )
  }

  test("cosh(3π/2)") {
    val c = cosh(3*π/2)
    assert( c === 55.6633808904 )
  }

  test("cosh(2π)") {
    val c = cosh(2*π)
    assert( c === 267.7467614837 )
  }

  test("cosh(2kπ)") {
    val k = 16
    val z = cosh(2*k*π)
    assert( z === 1)
  }

  test("cosh(i)") {
    val c = cosh(i)
    assert( c === 1.5430806348 )
  }

  test("cosh(∞)") {
    assertThrows[ArithmeticException] {
      val c = cosh(∞)
      assert(c === 0.0 )
    }
  }

  // tanh -----------------------------------------

  test("tanh(0)") {
    assert( tanh(0) === 0 )
  }

  test("tanh(π/4)") {
    assert( tanh(π/4) === 0.6557942026 )
  }

  test("tanh(π/2)") {
    assert( tanh(π/2) === 0.9171523357 )
  }

  test("tanh(3π/2)") {
    val c = tanh(3*π/2)
    assert( c === 0.999838614 )
  }

  test("tanh(2π)") {
    val c = tanh(2*π)
    assert( c === 0.9999930253 )
  }

  test("tanh(2kπ)") {
    val k = 16
    val z = tanh(2*k*π)
    assert( z === 1 )
  }

  test("tanh(i)") {
    val c = tanh(i)
    assert( c === 1.557407724654902 * i )
  }

  test("tanh(∞)") {
    assertThrows[ArithmeticException] {
      val c = tanh(∞)
      assert(c === 0.0 )
    }
  }

}