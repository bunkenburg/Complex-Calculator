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

class Literals extends FunSuite {

  // constants ---------------------------------------------

  test("Byte"){
    val b: Byte = 0
    val c: Complex = b
    assert( c === 0 )
  }

  test("Int"){
    val i: Int = 0
    val c: Complex = i
    assert( c === 0 )
  }

  test("Long"){
    val l: Long = 0L
    val c: Complex = l
    assert( c === 0 )
  }

  test("Float"){
    val f: Float = 0f
    val c: Complex = f
    assert( c === 0 )
  }

  test("Double"){
    val d: Double = 0d
    val c: Complex = d
    assert( c === 0 )
  }

}