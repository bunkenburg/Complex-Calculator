/*	Copyright 2019 Alexander Bunkenburg alex@inspiracio.cat
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

class NumberTest extends FunSuite {

  // Complex extends Number -------------------------------------------

  test("byteValue"){
    val n: Number = 1.9 + i*9.2
    val b = n.byteValue()
    val one: Byte = 1
    assert( b === one )
  }

    test("shortValue"){
    val n: Number = 1.9 + i*9.2
    val s = n.shortValue()
    val one: Short = 1
    assert( s === one )
  }

    test("intValue"){
    val n: Number = 1.9 + i*9.2
    val s = n.intValue()
    val one: Int = 1
    assert( s === one )
  }

    test("longValue"){
    val n: Number = 1.9 + i*9.2
    val s = n.longValue()
    val one: Long = 1L
    assert( s === one )
  }

    test("floatValue"){
    val n: Number = 1.9 + i*9.2
    val f = n.floatValue()
    assert( f === 1.9f )
  }

    test("doubleValue"){
    val n: Number = 1.9 + i*9.2
    val d = n.doubleValue()
    assert( d === 1.9d )
  }

    test("∞.byteValue"){
    val b = ∞.byteValue()
    val one: Byte = -1
    assert( b === one )
  }

    test("∞.shortValue"){
    val s = ∞.shortValue()
    val one: Short = -1
    assert( s === one )
  }

    test("∞.intValue"){
    val s = ∞.intValue()
    assert( s === Int.MaxValue )
  }

    test("∞.longValue"){
    val s = ∞.longValue()
    assert( s === Long.MaxValue )
  }

    test("∞.floatValue"){
    val f = ∞.floatValue()
    assert( f === Float.PositiveInfinity )
  }

    test("∞.doubleValue"){
    val d = ∞.doubleValue()
    assert( d === Double.PositiveInfinity )
  }

}
