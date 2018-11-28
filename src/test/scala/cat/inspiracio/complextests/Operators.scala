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

class Operators extends FunSuite {

  // -a -----------------------------

  test("-0"){
    val c: Complex = 0
    val d = -c
    assert( (0:Complex) === d )
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
    assertThrows[IllegalArgumentException] {
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
    assertThrows[IllegalArgumentException] {
      val a = ∞
      val b = ∞
      assert(a - b === ∞)
    }
  }

}