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
package cat.inspiracio.complex

import java.util.Formatter

/** Infinity, the one complex number at the north
  * pole of the Riemann sphere. */
object ∞ extends Complex{

  //interface java.lang.Number
  //All methods discard the imaginary part and are therefore useless.
  override def byteValue() = -1 //Double.POSITIVE_INFINITY.byteValue()
  override def shortValue() = -1 //Double.POSITIVE_INFINITY.shortValue()
  override def intValue() = Int.MaxValue  //Double.POSITIVE_INFINITY.intValue()
  override def longValue() = Long.MaxValue //Double.POSITIVE_INFINITY.longValue()
  override def floatValue() = Float.PositiveInfinity
  override def doubleValue() = Double.PositiveInfinity

  override def formatTo(fmt: Formatter, flags: Int, width: Int, precision: Int): Unit = fmt.format("∞")
  override def toString = "∞"

  // Operators ------------------------------------

  override def + (c: Complex): Complex = c match {
    case ∞ => throw new ArithmeticException("∞ + ∞")
    case _ => ∞
  }

  override def - (c: Complex): Complex = c match {
    case ∞ => throw new ArithmeticException("∞ - ∞")
    case _ => ∞
  }

  override def * (c: Complex): Complex = c match {
    case Real(0) => throw new ArithmeticException("∞ * 0")
    case _ => ∞
  }

  override def / (c: Complex): Complex = c match {
    case Real(0) => throw new ArithmeticException("∞/0")
    case ∞ => throw new ArithmeticException("∞/∞")
    case _ => ∞
  }

  /** exponentiation */
  override def \ (c: Complex): Complex = c match {
    case Real(0) => throw new ArithmeticException("∞\\0")
    case _ => ∞
  }

  override def === (c: Byte): Boolean = false
  override def === (c: Int): Boolean = false
  override def === (c: Long): Boolean = false
  override def === (c: Float): Boolean = c.isInfinity
  override def === (c: Double): Boolean = c.isInfinity
  override def === (c: Complex): Boolean = c match {
    case ∞ => true
    case _ => false
  }

}
