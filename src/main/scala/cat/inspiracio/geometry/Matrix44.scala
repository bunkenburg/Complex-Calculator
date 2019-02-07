/*	Copyright 2011 Alexander Bunkenburg alex@inspiracio.cat
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
 * *//*	Copyright 2011 Alexander Bunkenburg alex@inspiracio.cat
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

import math.{cos,sin}

/** Makes matrices. */
object Matrix44 {

  def translate(v: Vector3): Matrix44 = translate(v.x, v.y, v.z)

  def translate(x: Double, y: Double, z: Double): Matrix44 = {
    val m = unit.clone()
    m.data(0)(3) = -x
    m.data(1)(3) = -y
    m.data(2)(3) = -z
    m
  }

  def rotateX(a: Double): Matrix44 = {
    val m = unit.clone()
    m(1,1) = cos(a)
    m(1,2) = sin(a)
    m(2,1) = -sin(a)
    m(2,2) = cos(a)
    m
  }

  def rotateY(a: Double): Matrix44 = {
    val m = unit.clone()
    m(0,0) = cos(a)
    m(0,2) = -sin(a)
    m(2,0) = sin(a)
    m(2,2) = cos(a)
    m
  }

  def rotateZ(a: Double): Matrix44 = {
    val m = unit.clone()
    m(0,0) = cos(a)
    m(0,1) = sin(a)
    m(1,0) = -sin(a)
    m(1,1) = cos(a)
    m
  }

  /** Make a matrix from doubles. */
  def apply(
             d00: Double, d01: Double, d02: Double, d03: Double,
             d10: Double, d11: Double, d12: Double, d13: Double,
             d20: Double, d21: Double, d22: Double, d23: Double,
             d30: Double, d31: Double, d32: Double, d33: Double
           ): Matrix44 = new Matrix44(
    Array(
      Array(d00, d01, d02, d03),
      Array(d10, d11, d12, d13),
      Array(d20, d21, d22, d23),
      Array(d30, d31, d32, d33)
    )
  )

  val zero: Matrix44 = new Matrix44()

  val unit: Matrix44 = {
    val m = new Matrix44()
    for( i <- 0 to 3 )
      m(i,i) = 1
    m
  }

}

/** 4*4 matrices for 3d graphical transformations.
  * These matrices are immutable. */
final class Matrix44 {

  private var data = Array(
    new Array[Double](4),
    new Array[Double](4),
    new Array[Double](4),
    new Array[Double](4)
  )

  /** look up a value in the matrix */
  def apply(x: Int, y: Int): Double = data(x)(y)

  /** This makes matrices mutable.
    * Therefore it is private to Matrix44.
    * From the outside, matrices are immutable and inside, we have top be careful. */
  private def update(x: Int, y: Int, v: Double) = data(x)(y) = v

  /** Keeps the array. If client still changes array, this makes matrices mutable. */
  private def this(ad: Array[Array[Double]]) {
    this()
    data = ad
  }

  /** Since matrices are immutable, a client programmer should never need this. */
  override def clone(): Matrix44 = {
    val m = new Matrix44()
    for( x <- 0 to 3; y <- 0 to 3 )
      m(x,y) = this(x,y)
    m
  }

  /** With exact equality for doubles. */
  override def equals(other: Any): Boolean = other match {
    case null => false
    case m: Matrix44 => {
      var b = true
      for (x <- 0 to 3; y <- 0 to 3)
        b = b && ( this(x,y) == m(x,y) )
      b
    }
    case _ => false
  }

  override def hashCode(): Int = data.##

  def * (x: Double, y: Double, z: Double): Vector3 =
    Vector3(
      data(0)(0) * x + data(0)(1) * y + data(0)(2) * z + data(0)(3),
      data(1)(0) * x + data(1)(1) * y + data(1)(2) * z + data(1)(3),
      data(2)(0) * x + data(2)(1) * y + data(2)(2) * z + data(2)(3)
    )

  def * ( v: Vector3 ): Vector3 = this * (v.x, v.y, v.z)

  def * ( v: (Double, Double, Double) ): Vector3 = this * (v._1, v._2, v._3)

  def * (b: Matrix44): Matrix44 = {
    val a = this
    val m = new Matrix44()
    for(x <- 0 to 3; y <- 0 to 3){
      var sum = 0.0
      for(k <- 0 to 3)
        sum += a(x, k)*b(k,y)
      m(x,y) = sum
    }
    m
  }

  /** Puts a rotation on axis by angle before this matrix. */
  def preRot(axis: Char, angle: Double): Matrix44 = {

    val b = axis match {
      case 'x' => 1
      case 'y' => 2
      case 'z' => 0
      case _ => throw new IllegalArgumentException("preRot(" + axis + "," + angle + ")")
    }

    val i = (b + 1) % 3

    val r: Matrix44 = clone()

    for ( j <- 0 to 3 ) {
      val d3 = cos(angle) * r.data(b)(j) + sin(angle) * r.data(i)(j)
      val d4 = cos(angle) * r.data(i)(j) - sin(angle) * r.data(b)(j)
      r.data(b)(j) = d3
      r.data(i)(j) = d4
    }

    r
  }

  /** Puts a rotation on axis by angle after this matrix. */
  def postRot(axis: Char, angle: Double): Matrix44 = {

    val b = axis match {
      case 'x' => 1
      case 'y' => 2
      case 'z' => 0
      case _ => throw new IllegalArgumentException("postRot(" + axis + "," + angle + ")")
    }

    val i = (b + 1) % 3

    val r: Matrix44 = clone()

    for ( j <- 0 to 3 ) {
      val d3 = cos(angle) * r.data(j)(b) - sin(angle) * r.data(j)(i)
      val d4 = sin(angle) * r.data(j)(b) + cos(angle) * r.data(j)(i)
      r.data(j)(b) = d3
      r.data(j)(i) = d4
    }

    r
  }

  override def toString: String = "\n" +
    "[" + data(0)(0) + ", " + data(0)(1) + ", " + data(0)(2) + ", " + data(0)(3) + "\n" +
    " " + data(1)(0) + ", " + data(1)(1) + ", " + data(1)(2) + ", " + data(1)(3) + "\n" +
    " " + data(2)(0) + ", " + data(2)(1) + ", " + data(2)(2) + ", " + data(2)(3) + "\n" +
    " " + data(3)(0) + ", " + data(3)(1) + ", " + data(3)(2) + ", " + data(3)(3) + "]"

}
