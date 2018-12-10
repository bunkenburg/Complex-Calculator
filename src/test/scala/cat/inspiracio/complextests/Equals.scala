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

class Equals extends FunSuite {

  test("0==0") {
    val c: Complex = 0
    val e: Complex = 0
    assert(c === e )
  }

  test("0==0 Byte") {
    val c: Complex = 0
    val e: Byte = 0
    assert(c === e )
  }

  test("0==0 Int") {
    val c: Complex = 0
    val e: Int = 0
    assert(c === e )
  }

  test("0==0L") {
    val c: Complex = 0
    val e: Long = 0
    assert(c === e )
  }

  test("0==0F") {
    val c: Complex = 0
    val e: Float = 0
    assert(c === e )
  }

  test("0==0D") {
    val c: Complex = 0
    val e: Double = 0
    assert(c === e )
  }

  test("0 == ∞") {
    val c: Complex = 0
    val e: Complex = ∞
    assert( c !== e )
  }

  test("∞ == ∞") {
    val c: Complex = ∞
    val e: Complex = ∞
    assert( c === e )
  }

  test("i == i") {
    val c: Complex = i
    val e: Complex = i
    assert( c === e )
  }

  test("π == π") {
    val c: Complex = π
    val e: Complex = π
    assert( c === e )
  }

  test("π+i == π+i") {
    val c: Complex = π+i
    val e: Complex = π+i
    assert( c === e )
  }

}