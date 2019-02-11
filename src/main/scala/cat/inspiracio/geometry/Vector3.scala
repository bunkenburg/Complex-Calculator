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

import cat.inspiracio.complex.{sqr, sqrt}

/** Immutable 3d vectors.
  *
  * With exact equality for doubles.
  *
  * What is the use of this class when compared to (Double, Double, Double)?
  * The names of the fields:
  *
  *   val v: Vector3 = ...
  *   v.x + v.y + v.y
  *
  * */
object Vector3{
  import math.acos

  def apply(v: (Double,Double,Double)): Vector3 = Vector3(v._1,v._2,v._3)

  /** The angle between the two vectors.
    * cos(angle) ===  (a dot b) / ( abs(a)*abs(b) )
    * */
  def angle(a: Vector3, b: Vector3): Double = {
    val d = a dot b
    val divisor = a.abs * b.abs
    if(divisor == 0) {
      //At least one of the vectors is 0.
      //The angle between them is not defined.
      if(a.abs==0)
        throw new IllegalArgumentException("a is (0,0,0)")
      throw new IllegalArgumentException("b is (0,0,0)")
    } else {
      val x = d / divisor
      if(x < -1 || 1 < x)
        throw new IllegalArgumentException(s"(a dot b)/(a.abs * b.abs) == $x")
      acos(x)
    }
  }

  /** Makes a unit vector with the same direction and orientation. */
  def unit(v: Vector3): Vector3 = {
    val m = v.abs
    require(m != 0)
    v / m
  }
}
case class Vector3(val x: Double, val y: Double, val z: Double) {

  def - (v: Vector3) = Vector3(x - v.x, y - v.y, z - v.z)

  /** https://en.wikipedia.org/wiki/Dot_product */
  def dot (v: Vector3): Double = x*v.x + y*v.y + z*v.z

  /** https://en.wikipedia.org/wiki/Cross_product
    *
    * Maybe "x" is not such a good method name for this
    * because "x" is often also a variable name in the same scope. */
  def x(c: Vector3): Vector3 = {
    Vector3(
      y*c.z - z*c.y,
      z*c.x - x*c.z,
      x*c.y - y*c.x
    )
  }

  def abs: Double = sqrt(sqr(x) + sqr(y) + sqr(z))

  def * (d: Double) = Vector3(x * d, y * d, z * d)
  def / (d: Double) = Vector3(x / d, y / d, z / d)

  override def equals(other: Any): Boolean = other match {
      case Vector3(vx, vy, vz) => x == vx && y == vy && z == vz
      case _ => false
    }

  override def hashCode(): Int = (x, y, z).##

  override def toString: String = "(" + x + ", " + y + ", " + z + ")"

}