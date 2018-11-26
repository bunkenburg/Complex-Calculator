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
package cat.inspiracio.numbers

/** Specialisation to Real numbers, because many functions
  * and operations have much simples implementations that
  * are more precise. */
class Real
  (re: Double)
  extends Cartesian(re, 0) {

  override def sin: Real = Math.sin(re)

}

object Real {

  def apply(re: Double): Real = new Real(re)

  def unapply(c: Complex): Option[Double] = {
    c match {
      case Real(re) => Some(re)
      case Cartesian(re, im) if(im==0) => Some(re)
      case _ => None
    }
  }

  //Conversions ---------------------------------

  implicit def byte2Real(n: Byte): Real = Real(n.toDouble)
  implicit def int2Real(n: Int): Real = Real(n.toDouble)
  implicit def long2Real(n: Long): Real = Real(n.toDouble)
  implicit def float2Real(f: Float): Real = Real(f)
  implicit def double2Real(d: Double): Real = Real(d)

}
