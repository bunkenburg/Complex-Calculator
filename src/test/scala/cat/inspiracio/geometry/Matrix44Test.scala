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

import org.scalatest.FunSuite

class Matrix44Test extends FunSuite {
  import Matrix44._

  // translation ------------------------

  test("translate(1, 0, 0)"){
    val m = Matrix44.translate(1, 0, 0)
    val result = Matrix44(
      1, 0, 0, -1,
      0, 1, 0, 0,
      0, 0, 1, 0,
      0, 0, 0, 1
    )
    assert( m === result )
  }

  test("translate(3.8, -2.1, 18.9)"){
    val m = Matrix44.translate(3.8, -2.1, 18.9)
    val result = Matrix44(
      1, 0, 0, -3.8,
      0, 1, 0, 2.1,
      0, 0, 1, -18.9,
      0, 0, 0, 1
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
    val r = Vector3(1, 1, 1)
    assert( v === r )
  }

  // rotation -------------------------------------------------------

  test("rotateX(0)"){
    val m = Matrix44.Rx(0)
    val result = Matrix44(
      1, 0, 0, 0,
      0, 1, 0, 0,
      0, 0, 1, 0,
      0, 0, 0, 1
    )
    assert( m === result )
  }

  test("rotateX(1.2)"){
    val m = Matrix44.Rx(1.2)
    val result = Matrix44(
      1, 0, 0, 0,
      0, 0.3623577544766736, 0.9320390859672263, 0,
      0, -0.9320390859672263, 0.3623577544766736, 0,
      0, 0, 0, 1
    )
    assert( m === result )
  }

  test("rotateY(0)"){
    val m = Matrix44.Ry(0)
    val result = Matrix44(
      1, 0, 0, 0,
      0, 1, 0, 0,
      0, 0, 1, 0,
      0, 0, 0, 1
    )
    assert( m === result )
  }

  test("rotateY(1.2)"){
    val m = Matrix44.Ry(1.2)
    val result = Matrix44(
      0.3623577544766736, 0, -0.9320390859672263, 0,
      0, 1, 0, 0,
      0.9320390859672263, 0, 0.3623577544766736, 0,
      0, 0, 0, 1
    )
    assert( m === result )
  }

  test("rotateZ(0)"){
    val m = Matrix44.Rz(0)
    val result = Matrix44(
      1, 0, 0, 0,
      0, 1, 0, 0,
      0, 0, 1, 0,
      0, 0, 0, 1
    )
    assert( m === result )
  }

  test("rotateZ(1.2)"){
    val m = Matrix44.Rz(1.2)
    val result = Matrix44(
      0.3623577544766736, 0.9320390859672263, 0, 0,
      -0.9320390859672263, 0.3623577544766736, 0, 0,
      0, 0, 1, 0,
      0, 0, 0, 1
    )
    assert( m === result )
  }

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

  test("det unit"){
    val m = Matrix44.One
    val d = det(m)
    assert( d === 1 )
  }

  test("det zero"){
    val m = Matrix44.Zero
    val d = det(m)
    assert( d === 0 )
  }

  test("det random"){
    val m = Matrix44(
      1, 0, 2.3, -1.82,
      54.874, 0.7880107544762174, 0.6156614742127549, 0,
      0, -0.6156614742127549, 0.7880107544762174, 0,
      -0.11, 63.542, 0.853, 5
    )
    val d = det(m)
    assert( d === -5436.864652707013 )
  }

  test("M * 2"){
    val M = Matrix44(
      1, 0, 2.3, -1.82,
      54.8, 0.78, 0.61, 0,
      0, -0.61, 0.7, 0,
      -0.11, 3.54, 0.85, 5
    )
    val R = Matrix44(
      2, 0, 4.6, -3.64,
      109.6, 1.56, 1.22, 0,
      0, -1.22, 1.4, 0,
      -0.22, 7.08, 1.7, 10
    )
    assert( M * 2 === R )
  }

  test("invert(One)"){
    val R = invert(One)
    assert( One === R )
  }

  test("invert(bla)"){
    val M = Rz(1.23)
    val M1 = invert(M)
    assert( One === M * M1 )
    assert( One === M1 * M )
  }

  test("invert(di)"){
    val M = Rz(1.23) * Rz(0.32) * Rx(2.0)
    val M1 = invert(M)
    equals(One, M * M1)
    equals(One, M1 * M)
  }

  def equals(A: Matrix44, B: Matrix44) = {
    for(x <- 0 to 3; y <- 0 to 3){
      val a: Double = A(x,y)
      val b: Double = B(x,y)
      assert( math.abs(a - b)  <= 0.001 )
    }
  }

}
