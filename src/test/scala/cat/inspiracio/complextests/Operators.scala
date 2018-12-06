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
import cat.inspiracio.numbers.EC

class Operators extends FunSuite {

  // +a -----------------------------

  test("+0"){
    val c: Complex = 0
    val d = +c
    assert( 0 === d )
  }

  test("+i"){
    val c: Complex = +i
    val d = 0 + i
    assert( c === d )
  }

  test("+∞"){
    val c = + ∞
    assert( c === ∞ )
  }

  // -a -----------------------------

  test("-0"){
    val c: Complex = 0
    val d = -c
    assert( 0 === d )
  }

  test("-i"){
    val c: Complex = -i
    val d = 0 - i
    assert( c === d )
  }

  test("-∞"){
    val c = - ∞
    assert( c === ∞ )
  }

  // a + b ---------------------------

  test("0 + i"){
    val a = 0
    val b = i
    assert( a+b === i )
  }

  test("i + 0"){
    val a = i
    val b = 0
    assert( a+b === i )
  }

  test("3 + i"){
    val a = 3
    val b = i
    val s = (a+b).toString
    assert( s === "3+i" )
  }

  test("π + ∞"){
    val a = π
    val b = ∞
    assert( a+b === ∞ )
  }

  test("∞ + ∞"){
    assertThrows[ArithmeticException] {
      val a = ∞
      val b = ∞
      assert(a + b === ∞)
    }
  }

  // a - b ---------------------------

  test("0 - i"){
    val a = 0
    val b = i
    assert( a-b === -i )
  }

  test("i - 0"){
    val a = i
    val b = 0
    assert( a-b === i )
  }

  test("3 - i"){
    val a = 3
    val b = i
    val s = (a-b).toString
    assert( s === "3-i" )
  }

  test("π - ∞"){
    val a = π
    val b = ∞
    assert( a-b === ∞ )
  }

  test("∞ - ∞"){
    assertThrows[ArithmeticException] {
      val a = ∞
      val b = ∞
      assert(a - b === ∞)
    }
  }

  // a * b ---------------------------

  test("0 * i"){
    val a = 0
    val b = i
    assert( a*b === 0 )
  }

  test("i * 0"){
    val a = i
    val b = 0
    assert( a*b === 0 )
  }

  test("3 * i"){
    val a = 3
    val b = i
    val s = (a*b).toString
    assert( s === "3i" )
  }

  test("π * ∞"){
    val a = π
    val b = ∞
    assert( a*b === ∞ )
  }

  test("∞ * ∞"){
    val a = ∞
    val b = ∞
  }

  test("∞ * 0"){
    assertThrows[ArithmeticException] {
      val a = ∞
      val b = 0
      assert(a * b === ∞)
    }
  }

  test("0 * ∞"){
    assertThrows[ArithmeticException] {
      val a = 0
      val b = ∞
      assert(a * b === ∞)
    }
  }

  // a / b ---------------------------

  test("0 / 0"){
    assertThrows[ArithmeticException] {
      val a = 0
      val b: Complex = 0
      assert(a / b === 0)
    }
  }

  test("0 / i"){
    val a = 0
    val b = i
    assert( a/b === 0 )
  }

  test("i / 0"){
    val a = i
    val b = 0
    assert( a/b === ∞ )
  }

  test("1 / i"){
    val a = 1
    val b = i
    val c = a/b
    assert( c === -i )
  }

  test("3 / i"){
    val a = 3
    val b = i
    val s = (a/b).toString
    assert( s === "-3i" )
  }

  test("π / ∞"){
    val a = π
    val b = ∞
    assert( a/b === 0 )
  }

  test("∞ / ∞"){
    assertThrows[ArithmeticException] {
      val a = ∞
      val b = ∞
      assert(a / b === 0)
    }
  }

  test("∞ / 0"){
    assertThrows[ArithmeticException] {
      val a = ∞
      val b = 0
      assert(a / b === ∞)
    }
  }

  test("0 / ∞"){
    assertThrows[ArithmeticException] {
      val a = 0
      val b = ∞
      val c = a / b
      assert( c === ∞)
    }
  }

  // a ^ b ---------------------------

  test("0 ^ 0"){
    assertThrows[ArithmeticException] {
      val a = 0
      val b: Complex = 0
      val c = a^b
      assert( c === 0)
    }
  }

  test("0 ^ i"){
    val c = 0^i
    assert( c === 0 )
  }

  test("i^0"){
    val c = i^0
    assert( c === 1 )
  }

  test("1 ^ i"){
    val a = 1
    val b = i
    val c = a^b
    assert( c === 1 )
  }

  test("i^2"){
    val z = i^2
    assert( z === -1 )
  }

  test("π ^ ∞"){
    val c = π ^ ∞
    assert( c === ∞ )
  }

  test("∞ ^ ∞"){
    val a = ∞
    val b = ∞
    val c = a^b
    assert( c === ∞ )
  }

  test("∞^0"){
    assertThrows[ArithmeticException] {
      val c = ∞ ^ 0
      assert( c === ∞)
    }
  }

  test("0^∞"){
      val c = 0 ^ ∞
      assert( c === 0)
  }

}