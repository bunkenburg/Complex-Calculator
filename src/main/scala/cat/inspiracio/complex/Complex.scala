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
package cat.inspiracio.complex

import java.text.NumberFormat

import cat.inspiracio.numbers.PartialException

/** Complex numbers.
  *
  * The only implementations are:
  * - class Real(re)
  * - class Cartesian(re,im)
  * - object Infinity
  *
  * Could become implementations:
  * - Polar
  * - Imaginary
  *
  * The client programmer must import this trait
  * and its companion object:
  * - import Complex._
  *
  * */
trait Complex {

  /** maybe can get rid of this? */
  def finite = false

  /** maybe can get rid of this? */
  val isZero = false

  def re: Double = 0
  def im: Double = 0
  def modulus: Double = 0
  def argument: Double = 0

  // Functions ----------------

  def sin: Complex
  def sinh: Complex
  def cos: Complex
  def cosh: Complex
  def tan: Complex
  def tanh: Complex

  def ln: Complex
  def exp: Complex

  def conj: Complex
  def opp: Complex
  def reciprocal: Complex

  /** restrict type? */
  def fac: Complex

  // Operators ---------------------------------

  def unary_+ : Complex = this
  def unary_- : Complex = 0 - this

  def + (c: Complex): Complex
  def - (c: Complex): Complex
  def * (c: Complex): Complex
  def / (c: Complex): Complex

  def ^ (c: Byte): Complex = ???
  def ^ (c: Int): Complex = this match {
    case Real(0) =>
      if(c==0) throw new ArithmeticException("0^0")
      else 0
    case Polar(mx,ax) =>
      if(c==0) 1
      else Polar(Math.exp(Math.log(mx) * c), c * ax)
    case Infinity => if(c==0) throw new ArithmeticException("∞^0") else ∞
  }
  def ^ (c: Long): Complex = ???
  def ^ (c: Float): Complex = ???
  def ^ (c: Double): Complex = this / (c: Double)
  def ^ (c: Complex): Complex = this match {
    case Real(0) => if(c.isZero) throw new ArithmeticException("0^0") else 0
    case Polar(mx,ax) =>
      if(c.isZero) 1
      else if(c.finite){
        val Cartesian(cre, cim) = c
        val lnmx = Math.log(mx)
        Polar(Math.exp(lnmx * cre - cim * ax), cim * lnmx + cre * ax)
      }
      else ∞
    case Infinity => if(c.isZero) throw new ArithmeticException("∞^0") else ∞
  }

  def === (c: Byte): Boolean = this match {
    case Integer(c) => true
    case _ => false
  }
  def === (c: Int): Boolean = this match {
    case Integer(c) => true
    case _ => false
  }
  def === (c: Long): Boolean = this match {
    case Integer(c) => true
    case _ => false
  }
  def === (c: Float): Boolean = this match {
    case Real(c) => true
    case _ => false
  }
  def === (c: Double): Boolean = this match {
    case Real(c) => true
    case _ => false
  }
  def === (c: Complex): Boolean = this == c

  def === (c: Circle): Boolean = {
    val delta = (this - c.centre).modulus
    delta <= c.radius
  }

  def +- (eps: Double): Circle = Circle(this, eps)

  // for implementing classes ---------------------

  /** improves Math.sin.
    * sin(0) == 0
    * sin(π) == 0 */
  private def sin(a: Double): Double = {
    if(a==0) 0
    else if(a==π) 0
    else Math.sin(a)
  }

}

case class Circle(centre: Complex, radius: Double)

object Complex {

  // constants ----------------

  val π = Math.PI
  val ∞ = Complex.Infinity

  // formatting, maybe disappears ---------------

  /** for formatting */
  private var PRECISION: Int = 4
  def setPrecision(np: Int): Unit = {
    val op = PRECISION
    PRECISION = np
    EPSILON = Math.pow(10D, -PRECISION)
    nf.setMaximumFractionDigits(np)
  }
  def getPrecision: Int = PRECISION

  val nf = NumberFormat.getInstance()
  nf.setGroupingUsed(false)
  nf.setMaximumFractionDigits(10)

  // state, maybe disappears --------------------

  var EPSILON: Double = Math.pow(10D, -PRECISION)

  /** important for curves in polar coordinates */
  var argContinuous: Boolean = false
  var k = 0
  var lastQuad = 0
  private def resetArg(): Unit = {
    lastQuad = 0
    k = 0
  }
  def setArgContinuous(): Unit = argContinuous = true
  def setArgPrincipal(): Unit = argContinuous = false

  // Constructors --------------------------------

  /** Complex(re) */
  def apply(re: Double): Complex =
    if(re.isInfinite) ∞
    else new CartesianComplex(re, 0)

  /** Complex(re, im) */
  def apply(re: Double, im: Double): Complex =
    if(re.isInfinite || im.isInfinite) ∞
    else new CartesianComplex(re, im)

  def mkPolar(modulus: Double, angle: Double): Complex =
    if (modulus.isInfinite) ∞
    else new CartesianComplex(modulus * cos(angle), modulus * sin(angle))

  // better comparison --------------------------

  /** Specialisation to Real numbers, because many functions
    * and operations have much simples implementations that
    * are more precise. */
  class Real
  (re: Double)
    extends CartesianComplex(re, 0) {

    //Conversions ---------------------------------

    implicit private def byte2Real(n: Byte): Real = Real(n.toDouble)
    implicit private def int2Real(n: Int): Real = Real(n.toDouble)
    implicit private def long2Real(n: Long): Real = Real(n.toDouble)
    implicit private def float2Real(f: Float): Real = Real(f)
    implicit private def double2Real(d: Double): Real = Real(d)

    override def sin: Real = Math.sin(re)

  }

  /** Infinity, the one complex number at the north
    * pole of the Riemann sphere. */
  object Infinity extends Complex{

    override val finite = false
    override val isZero = false
    override def toString = "∞"

    override def sin = throw new ArithmeticException("sin ∞")
    override def sinh = throw new ArithmeticException("sinh ∞")
    override def cos = throw new ArithmeticException("cos ∞")
    override def cosh = throw new ArithmeticException("cosh ∞")
    override def tan = throw new ArithmeticException("tan ∞")
    override def tanh = throw new ArithmeticException("tanh ∞")
    override def ln = ∞
    override def exp = ∞

    override def conj = ∞
    override def opp = 0
    override def reciprocal = 0

    override def fac = ∞

    // Operators ------------------------------------

    override def unary_- = ∞

    override def + (c: Complex) =
      if (c.finite) ∞
      else throw new ArithmeticException("∞ + ∞")

    override def - (c: Complex) =
      if(c.finite) ∞
      else throw new ArithmeticException("∞ - ∞")

    override def * (c: Complex)=
      if (c.isZero) throw new ArithmeticException("∞ * 0")
      else ∞

    override def / (c: Complex) =
      if (c.isZero) throw new ArithmeticException("∞/0")
      else if(c.finite) ∞
      else throw new ArithmeticException("∞/∞")

    override def ^ (c: Complex) = if (c.isZero)
      throw new ArithmeticException("∞^0")  // ∞^0 = undefined
      else ∞ // ∞^y = ∞

  }

}