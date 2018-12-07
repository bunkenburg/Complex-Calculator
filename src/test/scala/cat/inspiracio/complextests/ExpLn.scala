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

class ExpLn extends FunSuite {

  // exp -----------------------------------------

  test("exp(0)") {
    assert( exp(0) === 0 )
  }

  test("exp(1)") {
    assert( exp(1) === 2.3012989023 )
  }

  test("exp(π)") {
    val z = exp(π)
    assert( z === 11.5487393573 )
  }

  test("exp(10)") {
    val c = exp(10)
    assert( c === 55.6543975994 )
  }

  test("exp(i)") {
    val c = exp(i)
    assert( c === 0.5403023058681398 + 0.8414709848078965*i )
  }

  test("exp(3i+7)") {
    val c = exp(3*i + 7)
    assert( c === -1085.6585983674215 + 154.7568801561417*i  )
  }

  test("exp(∞)") {
      val c = exp(∞)
      assert(c === ∞ )
  }

  // ln -----------------------------------------

  test("ln(0)") {
    assertThrows[ArithmeticException] {
      assert(ln(0) === 1)
    }
  }

  test("ln(1)") {
    assert( ln(1) === 2.5091784787 )
  }

  test("ln(π)") {
    assert( ln(π) === 11.5919532755 )
  }

  test("ln(-1)") {
    val c = ln(-1)
    assert( c === π*i )
  }

  test("ln(i)") {
    val c = ln(i)
    assert( c === 1.5707963267948966*i )
  }

  test("ln(3i+7)") {
    val z = ln(3*1 + 7)
    assert( z === 2.302585093 )
  }

  test("ln(e)") {
    val c = ln(e)
    assert( c === 1.5430806348 )
  }

  test("ln(∞)") {
      val c = ln(∞)
      assert(c === ∞ )
  }

}