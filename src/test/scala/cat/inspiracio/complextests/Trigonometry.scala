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

  test("sin(0)") {
    assert( sin(0) === 0 )
  }

  test("sin(π/2)") {
    assert( sin(π/2) === 1 )
  }

  test("sin(π)") {
    // Math.sin(Math.PI) == 1.2246467991473532E-16
    val z = sin(π)
    assert( z === 0.0 +- 1.0E-15 )
  }

  test("sin(3π/2)") {
    val b = sin(3*π/2) === -1
    assert( b )
    assertResult(-1){
      sin(3*π/2)
    }
  }

  test("sin(2π)") {
    // Math.sin(Math.PI) == 1.2246467991473532E-16
    val Real(re) = sin(2*π)
    assert( re === 0.0 +- 1.0E-15 )
  }

  test("sin(2kπ)") {
    val k = 16
    val z = sin(2*k*π)
    assert( z === 0.0 +- 1.0E-14 )
  }

  test("sin(i)") {
    val c = sin(i)
    assert( c.toString === "1.1752011936i"  )
  }

  test("sin(∞)") {
    assertThrows[ArithmeticException] {
      val c = sin(∞)
      assert(c === 0.0 +- 1.0E-15)
    }
  }



}