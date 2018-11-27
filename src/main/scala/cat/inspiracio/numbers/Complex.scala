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

import java.text.NumberFormat

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

  val finite = false
  val isZero = false
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

  def fac: Complex

  // Operators ---------------------------------

  def unary_- : Complex

  def + (c: Complex): Complex
  def - (c: Complex): Complex
  def * (c: Complex): Complex
  def / (c: Complex): Complex
  def ^ (c: Complex): Complex

}

object Complex {

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
    else new Cartesian(re, 0)

  /** Complex(re, im) */
  def apply(re: Double, im: Double): Complex =
    if(re.isInfinite || im.isInfinite) ∞
    else new Cartesian(re, im)

  def mkPolar(modulus: Double, angle: Double): Complex =
    if (java.lang.Double.isInfinite(modulus)) ∞
    else new Cartesian(modulus * Math.cos(angle), modulus * Math.sin(angle))

  //Constants -----------------------------------

  val e = Math.E
  val i = new Cartesian(0,1)
  val π = Math.PI
  val ∞ = Infinity

  // ugly constants for pattern matching

  val E: Complex = e
  val I = i
  val Pi: Complex = π

  //Conversions ---------------------------------

  implicit def byte2Complex(n: Byte): Complex = Real(n.toDouble)
  implicit def int2Complex(n: Int): Complex = Real(n.toDouble)
  implicit def long2Complex(n: Long): Complex = Real(n.toDouble)
  implicit def float2Complex(f: Float): Complex = Real(f)
  implicit def double2Complex(d: Double): Cartesian = Real(d)

  // Trigonometry ---------------------------------

  def sin(z: Complex) = z.sin
  def sinh(z: Complex) = z.sinh
  def cos(z: Complex) = z.cos
  def cosh(z: Complex) = z.cosh
  def tan(z: Complex) = z.tan
  def tanh(z: Complex) = z.tanh

  def exp(z: Complex) = z.exp
  def ln(z: Complex) = z.ln

  // better comparison --------------------------


  /*
  import org.scalactic.Tolerance._
  implicit val complexEq =
       new org.scalactic.Equality[Complex] {
         def areEqual(a: Complex, b: Any): Boolean =
             b match {
               case p: Complex => true
                 case _ => false
               }
       }
       */

  // pattern matchers -------------------------

  object Natural {

    /** Matches natural numbers */
    def unapply(c: Complex): Option[Int] = {
      c match {
        case Integer(n) if(0<=n) => Some(n)
        case _ => None
      }
    }

  }

  object Integer {

    /** Matches integer numbers */
    def unapply(c: Complex): Option[Int] = {
      c match {
        case Real(re) if(re.isWhole()) => Some(re.toInt)
        case _ => None
      }
    }

  }

  /** Specialisation to Real numbers, because many functions
    * and operations have much simples implementations that
    * are more precise. */
  class Real
  (re: Double)
    extends Cartesian(re, 0) {

    override def sin: Real = Math.sin(re)

  }

  object Real {

    /** val c = Real(3.2) */
    def apply(re: Double): Real = new Real(re)

    /** Provides Real(re) as a pattern.
      *
      * Only matches if it's a finite real number.
      * */
    def unapply(c: Complex): Option[Double] = {
      c match {
        case c: Real => Some(c.re)
        case Cartesian(re, im) if im==0 => Some(re)
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

  object Imaginary {

    /** Matches imaginary numbers */
    def unapply(c: Complex): Option[Double] = {
      c match {
        case Cartesian(re, im) if(re==0) => Some(im)
        case _ => None
      }
    }

  }

  object Polar {

    /** val z = Polar(m, a) */
    def apply(m: Double, a: Double): Complex = mkPolar(m, a)

    /** val Polar(m, a) = z */
    def unapply(c: Complex): Option[(Double,Double)] = {
      if(!c.finite)
        None
      else {
        val m = c.modulus
        val a = c.argument
        Some( (m, a) )
      }
    }

  }

  object Cartesian {

    /** val z = Cartesian(re, im) */
    def apply(re: Double, im: Double): Cartesian = new Cartesian(re, im)

    /** val Cartesian(re, im) = z */
    def unapply(c: Cartesian): Option[(Double,Double)] = Some( (c.re, c.im) )

  }

  /** Infinity, the one complex number at the north
    * pole of the Riemann sphere. */
  object Infinity extends Complex{

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

}