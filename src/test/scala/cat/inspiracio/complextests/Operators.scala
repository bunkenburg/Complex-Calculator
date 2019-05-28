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

  test("unary - avoids -0.0"){
    val c: Complex = 0
    val Cartesian(re,im) = -c
    assert( 0.0 equals re )
    assert( 0.0 equals im )
  }

  test("-i"){
    val c: Complex = -i
    val d = 0 - i
    assert( c === d )
  }

  test("-(3+i)"){
    val c: Complex = 3+i
    val z = -c
    val d = -3 - i
    assert( z === d )
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
    val z = a + b
    assert( z === i )
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

  test("∞ + 3"){
    val z = ∞ + 3
    assert( z === ∞ )
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
    val z = a - b
    assert( z === i )
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

  test("∞ - 3"){
    val z = ∞ - 3
    assert( z === ∞ )
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

  test("∞ * 3"){
      val a = ∞
      val b = 3
      assert(a * b === ∞)
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
      val c = a / b
      assert( c === 0)
    }
  }

  test("0 / (0: Double)"){
    assertThrows[ArithmeticException] {
      val a: Complex = 0
      val b: Double = 0
      val c = a / b
      assert( c === 0)
    }
  }

  test("0 / (3: Double)"){
    val a: Complex = 0
    val b: Double = 3
    val c = a / b
    assert( c === 0)
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

  // a \ b ---------------------------

  test("0 \\ 0"){
    assertThrows[ArithmeticException] {
      val a = 0
      val b: Complex = 0
      val c = a\b
      assert( c === 0)
    }
  }

  test("0 \\ (0: Double)"){
    assertThrows[ArithmeticException] {
      val a: Complex = 0
      val b: Double = 0
      val c = a\b
      assert( c === 0)
    }
  }

  test("0 \\ (3: Double)"){
      val a: Complex = 0
      val b: Double = 3
      val c = a\b
      assert( c === 0)
  }

  test("3 \\ (1/0: Double)"){
      val a: Complex = 3
      val b: Double = Double.PositiveInfinity
      val c = a\b
      assert( c === ∞)
  }

  test("0 \\ i"){
    val c = 0\i
    assert( c === 0 )
  }

  test("0 ^ 3"){
    val a: Complex = 0
    val c = a \ 3
    assert( c === 0 )
  }

  test("case Real(0)"){
    val a: Complex = 0
    val b = a match {
      case Real(0) => true
      case _ => false
    }
    assert( b )
  }

  test("a === 0"){
    val a: Int = 3
    val b = a === 0
    assert( !b )
  }

  test("i\\0"){
    val c = i\0
    assert( c === 1 )
  }

  test("1 \\ i"){
    val a = 1
    val b = i
    val c = a\b
    assert( c === 1 )
  }

  test("i\\2"){
    val z = i\2
    assert( z === -1 )
  }

  test("π \\ ∞"){
    val c = π \ ∞
    assert( c === ∞ )
  }

  test("∞ \\ 0.0"){
    assertThrows[ArithmeticException] {
      val a: Complex = ∞
      val c = a \ 0.0
      assert(c === ∞)
    }
  }

  test("∞ \\ 3"){
    val a = ∞
    val c = a \ 3
    assert( c === ∞ )
  }

  test("∞ \\ 3.0"){
    val a = ∞
    val c = a \ 3.0
    assert( c === ∞ )
  }

  test("∞ \\ ∞"){
    val a: Complex = ∞
    val b = ∞
    val c = a\b
    assert( c === ∞ )
  }

  test("∞\\0"){
    assertThrows[ArithmeticException] {
      val c = ∞ \ 0
      assert( c === ∞)
    }
  }

  test("∞ \\ (0: Complex)"){
    assertThrows[ArithmeticException] {
      val a: Complex = ∞
      val b: Complex = 0.0
      val c = a \ b
      assert( c === ∞)
    }
  }

  test("0\\∞"){
    val c = 0 \ ∞
    assert( c === 0)
  }

  /** This is where it all started. It will be one of the first things users will try. */
  test("-1 \\ 0.5"){
    val c = -1 \ 0.5
    assert( c === i )
  }

  /** This is where it all started. It will be one of the first things users will try. */
  test("(-1 : Complex) \\ (0.5 : Complex)"){
    val a: Complex = -1
    val b: Complex = 0.5
    val c = a \ b
    assert( c === i )
  }

  /** This is where it all started. It will be one of the first things users will try. */
  test("(-1) \\ 0.5"){
    val c = (-1) \ 0.5
    assert( c === i )
  }

}