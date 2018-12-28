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

import cat.inspiracio.calculator.Vector3
import org.scalatest.FunSuite

class Matrix44Test extends FunSuite {

  // translation ------------------------

  test("translate(1, 0, 0)"){
    val m = Matrix44.translation(1, 0, 0)
    val result = Matrix44(
      1, 0, 0, -1,
      0, 1, 0, 0,
      0, 0, 1, 0,
      0, 0, 0, 0
    )
    assert( m === result )
  }

  test("translate(3.8, -2.1, 18.9)"){
    val m = Matrix44.translation(3.8, -2.1, 18.9)
    val result = Matrix44(
      1, 0, 0, -3.8,
      0, 1, 0, 2.1,
      0, 0, 1, -18.9,
      0, 0, 0, 0
    )
    assert( m === result )
  }

  // matrix * vector = vector ----------------------------------

  test("m * v3 = v3"){
    val m = Matrix44(
      1, 0, 0, 0,
      0, 1, 0, 0,
      0, 0, 1, 0,
      0, 0, 0, 0
    )
    val v = m * (1, 1, 1)
    val r = new Vector3(1, 1, 1)
    assert( v === r )
  }

  // rotation -------------------------------------------------------

  test("1.preRot(x, 38)"){
    val angle = 38.toRadians
    val m: Matrix44 = Matrix44(
      1, 0, 0, 0,
      0, 1, 0, 0,
      0, 0, 1, 0,
      0, 0, 0, 0
    )
    val rotated = m.preRot('x', angle)
    val result = Matrix44(
      1, 0, 0, 0,
      0, 0.7880107544762174, 0.6156614742127549, 0,
      0, -0.6156614742127549, 0.7880107544762174, 0,
      0, 0, 0, 0
    )
    assert( rotated === result )
  }

  test("1.postRot(x, 38)"){
    val angle = 38.toRadians
    val m: Matrix44 = Matrix44(
      1, 0, 0, 0,
      0, 1, 0, 0,
      0, 0, 1, 0,
      0, 0, 0, 0
    )
    val rotated = m.postRot('x', angle)
    val result = Matrix44(
      1, 0, 0, 0,
      0, 0.7880107544762174, 0.6156614742127549, 0,
      0, -0.6156614742127549, 0.7880107544762174, 0,
      0, 0, 0, 0
    )
    assert( rotated === result )
  }

}
