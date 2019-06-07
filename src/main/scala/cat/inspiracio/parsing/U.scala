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
 * */
package cat.inspiracio.parsing

import cat.inspiracio.complex._
import cat.inspiracio.complex

/** An expression that is a unary function application */

case class Arg(a: Expression) extends Expression {
  override def toString: String = s"arg($a)"
  override def apply(z: Complex): Complex = { val Polar(_, angle) = a(z); angle }
}

case class Conj(a: Expression) extends Expression {
  override def toString: String = s"conj($a)"
  override def apply(z: Complex): Complex = conj(a(z))
}

case class Cos(a: Expression) extends Expression {
  override def toString: String = s"cos($a)"
  override def apply(z: Complex): Complex = cos(a(z))
}

case class Cosh(a: Expression) extends Expression {
  override def toString: String = s"cosh($a)"
  override def apply(z: Complex): Complex = cosh(a(z))
}

case class Exp(a: Expression) extends Expression {
  override def toString: String = s"exp($a)"
  override def apply(z: Complex): Complex = exp(a(z))
}

case class Fac(a: Expression) extends Expression {
  override def toString: String = s"($a)!"
  override def apply(z: Complex): Complex = fac(a(z))
}

case class Im(a: Expression) extends Expression {
  override def toString: String = s"Im($a)"
  override def apply(z: Complex): Complex = complex.Im(a(z))
}

case class Ln(a: Expression) extends Expression {
  override def toString: String = s"ln($a)"
  override def apply(z: Complex): Complex = ln(a(z))
}

case class Mod(a: Expression) extends Expression {
  override def toString: String = s"|$a|"
  override def apply(z: Complex): Complex = abs(a(z))
}

case class Opp(a: Expression) extends Expression {
  override def toString: String = s"opp($a)"
  override def apply(z: Complex): Complex = opp(a(z))
}

case class PreMinus(a: Expression) extends Expression {
  override def toString: String = s"-($a)"
  override def apply(z: Complex): Complex = -a(z)
}

case class PrePlus(a: Expression) extends Expression {
  override def toString: String = s"+($a)"
  override def apply(z: Complex): Complex = a(z)
}

case class Re(a: Expression) extends Expression {
  override def toString: String = s"Re($a)"
  override def apply(z: Complex): Complex = complex.Re(a(z))
}

case class Sin(a: Expression) extends Expression {
  override def toString: String = s"sin($a)"
  override def apply(z: Complex): Complex = sin(a(z))
}

case class Sinh(a: Expression) extends Expression {
  override def toString: String = s"sinh($a)"
  override def apply(z: Complex): Complex = sinh(a(z))
}

case class Tan(a: Expression) extends Expression {
  override def toString: String = s"tan($a)"
  override def apply(z: Complex): Complex = tan(a(z))
}

case class Tanh(a: Expression) extends Expression {
  override def toString: String = s"tanh($a)"
  override def apply(z: Complex): Complex = tanh(a(z))
}
