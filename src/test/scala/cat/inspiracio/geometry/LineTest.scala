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

class LineTest extends FunSuite {

  test("Line(0, 2+i)"){
    val c = Line(0, 2+i)
    assert( c.length === 2.23606797749979 )
    assert( c.top === 1 )
    assert( c.bottom === 0 )
    assert( c.left === 2 )
    assert( c.right === 0 )
  }

  test("Line(0, 2+i).toString"){
    val c = Line(0, 2+i)
    assert( c.toString === "Line( 0, 2+i )" )
  }

  test("Line(3+i, π).toString"){
    val c = Line(3+i, π)
    assert( c.toString === "Line( 3+i, π )" )
  }

}
