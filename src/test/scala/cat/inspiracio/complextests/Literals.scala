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

  test("Byte"){
    val b: Byte = 17
    val c: Complex = b
    assert( c === 17 )
  }

  test("Int"){
    val i: Int = 18
    val c: Complex = i
    assert( c === 18 )
  }

  test("Long"){
    val l: Long = 19L
    val c: Complex = l
    assert( c === 19 )
  }

  test("Float"){
    val f: Float = 20.3f
    val c: Complex = f
    assert( c === 20.3 )
  }

  test("Double"){
    val d: Double = -3.14d
    val c: Complex = d
    assert( c === -3.14 )
  }

}