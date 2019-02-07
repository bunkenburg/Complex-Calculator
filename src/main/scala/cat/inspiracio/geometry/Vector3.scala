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
    if(divisor == 0)
      ???
    else
      acos( d / divisor )
  }

}
case class Vector3(val x: Double, val y: Double, val z: Double) {
  import math.atan2

  def - (v: Vector3) = Vector3(x - v.x, y - v.y, z - v.z)

  /** https://en.wikipedia.org/wiki/Dot_product */
  def dot (v: Vector3): Double = x*v.x + y*v.y + z*v.z

  /** https://en.wikipedia.org/wiki/Cross_product */
  def cross (c: Vector3): Vector3 = {
    val b = this
    Vector3(
      b.y*c.z - b.z*c.y,
      b.z*c.x - b.x*c.z,
      b.x*c.y - b.y*c.x
    )
  }

  def abs: Double = sqrt(sqr(x) + sqr(y) + sqr(z))

  override def equals(other: Any): Boolean = other match {
      case Vector3(vx, vy, vz) => x == vx && y == vy && z == vz
      case _ => false
    }

  override def hashCode(): Int = (x, y, z).##

  override def toString: String = "(" + x + ", " + y + ", " + z + ")"

}