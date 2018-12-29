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

class SquareTest extends FunSuite {

  test("Square(0, 1+i)"){
    val c = Square(0, 1+i)
    assert( c.center === 0 )
    assert( c.side === 2 )
    assert( c.top === 1 )
    assert( c.bottom === -1 )
    assert( c.left === -1 )
    assert( c.right === +1 )
  }

  test("Square(3+i, π)"){
    val c = Square(3+i, π)
    assert( c.center === 3+i )
    assert( c.side === 1.1415926535897931 )
    assert( c.top === 1.5707963267948966 )
    assert( c.bottom === 0.42920367320510344 )
    assert( c.left === 2.4292036732051034 )
    assert( c.right === 3.57079632679489664 )
  }

  test("Square(0, 1+i).toString"){
    val c = Square(0, 1+i)
    assert( c.toString === "Square(0, radius = 1.0 )" )
  }

  test("Square(3+i, π).toString"){
    val c = Square(3+i, π)
    assert( c.toString === "Square(3+i, radius = 0.5707963267948966 )" )
  }

}
