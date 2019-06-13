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
package cat.inspiracio

import cat.inspiracio.complex.imp.CartesianComplex

/** This is all the client programmer needs:
  *
  * import cat.inspiracio.complex._
  *
  * Syntax of complex numbers should be as in Mathematics books,
  * not as in object-oriented programming.
  * */
package object complex {
  import math.log

  // beautiful constants -----------------------------------

  val e = math.E
  val i = Imaginary(1)
  val π = math.Pi

  // functions ---------------------------------

  def finite(c: Complex): Boolean = ∞ != c

  def abs(d: Double): Double = math.abs(d)

  /** The modulus or absolute value of the number.
    * Only for finite numbers. */
  val abs: Complex => Double = {
    case Real(r) => abs(r)
    case cc: CartesianComplex => cc.modulus
    case ∞ => throw new ArithmeticException("|∞|")
  }

  val Re: Complex => Double = {
    case Cartesian(re, _) => re
    case ∞ => throw new ArithmeticException("Re ∞")
  }

  val Im: Complex => Double = {
    case Cartesian(_, im) => im
    case ∞ => throw new ArithmeticException("Im ∞")
  }

  def sqrt(d: Double): Double = math.sqrt(d)
  def sqr(d: Double): Double = d*d

  /** Improves math.sin for important values */
  def sin(a: Double): Double =
    if(a == -π) 0
    else if(a == -π/2) -1
    else if(a == 0) 0
    else if(a == π/2) 1
    else if(a == π) 0
    else if(a == 2*π) 0
    else math.sin(a)

  val sin: Complex => Complex = {
    case ∞ => throw new ArithmeticException("sin ∞")
    case Real(r) => sin(r)
    case z => {
      val zi = z * i
      (exp(zi) - exp(-zi)) / (i * 2)
    }
  }

  def asin(d: Double): Double = math.asin(d)

  /** Improves math.cos for important values */
  def cos(a: Double): Double =
    if(a == -π) -1
    else if(a == -π/2) 0
    else if(a == 0) 1
    else if(a == π/2) 0
    else if(a == π) -1
    else if(a == 3*π/2) 0
    else if(a == 2*π) 1
    else math.cos(a)

  val cos: Complex => Complex = {
    case ∞ => throw new ArithmeticException("cos ∞")
    case Real(r) => cos(r)
    case z => {
      val zi = z * i
      (exp(zi) + exp(-zi)) / 2
    }
  }

  def acos(d: Double): Double = math.acos(d)

  /** Improves math.tan for important values */
  def tan(a: Double): Complex =
    if( a==0 ) 0
    else if(a == π/4) 1
    else if(a == π/2) ∞
    else if(a == 2*π) 0
    else math.tan(a)

  val tan: Complex => Complex = {
    case Real(r) => tan(r)
    case z => sin(z) / cos (z)
  }

  def atan(d: Double): Double = math.atan(d)

  def cot(a: Double): Complex = 1 / tan(a)
  def acot(m: Double): Double = math.atan(1/m)

  // hyperbolic functions -------------------

  /** sinh x = -i sin(ix) */
  val sinh: Complex => Complex = {
    case ∞ => throw new ArithmeticException("sinh ∞")
    case Real(r) => ( exp(r) - exp(-r) ) / 2
    case z => ( exp(z) - exp(-z) ) / 2
  }

  /** cosh x = cos(ix) */
  val cosh: Complex => Complex = {
    case ∞ => throw new ArithmeticException("cosh ∞")
    case Real(r) => ( exp(r) + exp(-r) ) / 2
    case z => ( exp(z) + exp(-z) ) / 2
  }

  /** tanh x = -i tan(ix) */
  val tanh: Complex => Complex = {
    case Real(r) => sinh(r) / cosh(r)
    case z => sinh(z) / cosh(z)
  }

  /** As method, because for methods can be overloaded.
    *   def exp(d: Double): Complex
    *   def exp(c: Complex): Complex
    * whereas we can only have one
    *   val exp
    * whatever its type is.
    * */
  def exp(d: Double): Double = math.exp(d)

  /** Here as val but could also be a method. */
  val exp: Complex => Complex = {
    case ∞ => ∞
    case Real(r) => exp(r)
    case Cartesian(re,im) => Polar(exp(re), im)
  }

  val ln: Complex => Complex = {
    case ∞ => ∞
    case Real(0) => throw new ArithmeticException("ln 0")
    case Polar(m,a) => Cartesian(log(m), a)
  }

  /** Complex conjugate: negates the imaginary part */
  val conj: Complex => Complex = {
    case ∞ => ∞
    case Cartesian(re,im) => Cartesian(re, -im)
  }

  /** Opposite point on Riemann sphere.
    * def opp(z) = -1 / conj(z)
    * */
  val opp: Complex => Complex = {
    case ∞ => 0
    case Real(0) => ∞
    case Polar(m,a) => Polar(1 / m, a + π)
  }

  /** factorial function for natural numbers
    * @param n Assumes 0 <= n */
  private def f(n: Long): Long =
    if(n<=1) 1
    else n * f(n-1)

  /** Factorial function, for natural numbers only */
  val fac: Complex => Complex = {
    case ∞ => ∞
    case Natural(n) if n <= 20 => f(n)
    case z => throw new ArithmeticException(z +  "!")
  }

  // conversions ---------------------------------

  implicit def byte2Complex(n: Byte): Complex = Real(n.toDouble)
  implicit def int2Complex(n: Int): Complex = Real(n.toDouble)
  implicit def long2Complex(n: Long): Complex = Real(n.toDouble)
  implicit def float2Complex(f: Float): Complex = if(f.isInfinite) ∞ else Real(f)
  implicit def double2Complex(d: Double): Complex = if(d.isInfinite) ∞ else Real(d)

  // pattern matchers -------------------------

  // ugly constants for pattern matching

  val E = e
  val I = i
  val Pi = π
  val Infinity = ∞

  // unapply methods for pattern matching

  object Natural {

    /** val Natural(n) = z */
    def unapply(c: Complex): Option[Long] = c match {
      case Integer(n) if 0<=n => Some(n)
      case _ => None
    }

  }

  object Integer {

    /** val Integer(n) = z */
    def unapply(c: Complex): Option[Long] = c match {
      case Real(re) if re.isWhole() => Some(re.toLong)
      case _ => None
    }

  }

  object Real {

    /** val c = Real(3.2) */
    def apply(re: Double): Complex = Cartesian(re, 0)

    /** val Real(re) = z */
    def unapply(c: Complex): Option[Double] = c match {
      case Cartesian(re, im) if im==0 => Some(re)
      case _ => None
    }

  }

  object Imaginary {

    /** val c = Img(3.2) */
    def apply(im: Double): Complex = Cartesian(0, im)

    /** val Imaginary(im) = z */
    def unapply(c: Complex): Option[Double] = c match {
      case Cartesian(re, im) if re==0 => Some(im)
      case _ => None
    }

  }

  /** Constructing and destructing complex
    * numbers in polar coordinates. */
  object Polar {

    /** val z = Polar(m, a) */
    def apply(m: Double, a: Double): Complex =
      if (m.isInfinity) ∞
      else Cartesian(m * cos(a), m * sin(a))


    /** val Polar(m, a) = z
      * Matches all finite numbers. */
    def unapply(c: Complex): Option[(Double,Double)] = c match {
      case cc: CartesianComplex => {
        val m = cc.modulus
        val a = cc.argument
        Some( (m, a) )
      }
      case ∞ => None
    }

  }

  /** Constructing and destructing complex
    * numbers in Cartesian coordinates. */
  object Cartesian {

    /** val z = Cartesian(re, im) */
    def apply(re: Double, im: Double): Complex =
      if(re.isInfinity || im.isInfinity) ∞
      else {
        //avoids -0.0
        val r = if(re==0.0) 0.0 else re
        val i = if(im==0.0) 0.0 else im
        new CartesianComplex(r, i)
      }

    /** val Cartesian(re, im) = z
      * Matches all finite complex numbers. */
    def unapply(c: Complex): Option[(Double,Double)] = c match {
      case cc: CartesianComplex => Some( (cc.re, cc.im) )
      case ∞ => None
    }

  }

}