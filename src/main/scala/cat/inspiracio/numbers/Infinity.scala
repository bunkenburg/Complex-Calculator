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

object Infinity extends Complex{

  import Complex._

  override val finite = false
  override val isZero = false
  override def toString: String = "∞"

  override def sin: Cartesian = throw new PartialException("sin ∞")
  override def sinh: Cartesian = throw new PartialException("sinh ∞")
  override def cos: Cartesian = throw new PartialException("cos ∞")
  override def cosh: Cartesian = throw new PartialException("cosh ∞")
  override def tan: Cartesian = throw new PartialException("tan ∞")
  override def tanh: Cartesian = throw new PartialException("tanh ∞")
  override def ln: Complex = ∞
  override def exp = ∞

  override def conj = ∞
  override def opp = 0
  override def reciprocal = 0

  override def fac = ∞

  // Operators ------------------------------------

  override def unary_- = ∞

  override def + (c: Complex): Complex =
    if (c.finite) ∞
    else throw new PartialException("∞ + ∞")

  override def - (c: Complex): Complex = ???
  override def * (c: Complex): Complex = ???
  override def / (c: Complex): Complex = ???
  override def ^ (c: Complex): Complex = ???

}
