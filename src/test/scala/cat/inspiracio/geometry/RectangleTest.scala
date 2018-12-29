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
package cat.inspiracio.geometry

import cat.inspiracio.complex._
import org.scalatest.FunSuite

class RectangleTest extends FunSuite {

  test("Rectangle(0, 2+i)"){
    val c = Rectangle(0, 2+i)
    assert( c.center === 0 )
    assert( c.width === 4 )
    assert( c.height === 2 )
    assert( c.top === 1 )
    assert( c.bottom === -1 )
    assert( c.left === -2 )
    assert( c.right === +2 )
  }

  test("Rectangle(3+i, π)"){
    val c = Rectangle(3+i, π)
    assert( c.center === 3+i )
    assert( c.width === 0.28318530717958623 )
    assert( c.height === 2 )
    assert( c.top === 2 )
    assert( c.bottom === 0 )
    assert( c.left === 2.858407346410207 )
    assert( c.right === π )
  }

  test("Rectangle(0, 2+i).toString"){
    val c = Rectangle(0, 2+i)
    assert( c.toString === "Rectangle(0, corner = 2+i )" )
  }

  test("Rectangle(3+i, π).toString"){
    val c = Rectangle(3+i, π)
    assert( c.toString === "Rectangle(3+i, corner = π )" )
  }

}
