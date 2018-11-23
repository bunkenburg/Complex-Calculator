/*	Copyright 2018 Alexander Bunkenburg alex@cat.inspiracio.com
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

/** Complex numbers plus infinity, as on the Riemann sphere.
  * Construction and state is two doubles, Cartesian coordinates. */

//XX define == === equals correctly

//XXX prove that a Complex is never infinite,
// and then simplify, moving infinite cases to subclass Infinity

class Complex protected (val re: Double, val im: Double){
  require( !re.isInfinite && !im.isInfinite, "Infinite: " + re + " " + im)

  import Complex._

  /** just to make nicer formulas in this class */
  private val z = this

  /** Format a complex number nicely */
  override def toString: String = {

    /** Format real number nicely, with e and π. */
    def toString(d: Double): String = {

      //Some special real numbers
      if(d == e) "e"
      else if(d == -e) "-e"
      else if(d == π) "π"
      else if(d == -π) "-π"

      //General formatting, XXX improve
      else {
        val s = nf.format(d)
        if (s.contains('.')) { //Cuts off trailing zeros.
          val b = new StringBuilder(s)
          while ( b.charAt(b.length - 1) == '0')
            b.setLength(b.length - 1)
          //Maybe cut off trailing '.' too.
          if (b.charAt(b.length - 1) == '.')
            b.setLength(b.length - 1)
          b.toString
        }
        else s
      }
    }

      //It's just a real number.
      if (Math.abs(im) < EPSILON)
        toString(re)
      else {
        val s = toString(re)

        var s1: String = null
        if (Math.abs(im - 1) < EPSILON)
          s1 = "i"
        else if (Math.abs(im + 1) < EPSILON)
          s1 = "-i"
        else
          s1 = toString(im) + "i"
        if (Math.abs(re) < EPSILON)
          s1
        else
          s + (if (im <= 0) "" else "+") + s1
      }
  }

  // methods with 0 parameters ------------------------------------

  val finite = true
  val isZero = re==0 && im==0

  /** I define that so that I can write 0 of type Complex like as 0.toComplex */
  def toComplex: Complex = z

  /** Why results 0, 1, 2, 3, 4 ?
    * I would expect only four results. */
  private def quadrant: Int =
    if (0 <= re  && 0 <= im) 1
    else if (re < 0 && 0 <= im ) 2
    else if (re < 0 && im < 0) 3
    else if (re < 0 || 0 <= im) 0
    else 4

  def acos = throw new PartialException("acos not implemented.")
  def asin: Complex = throw new PartialException("asin not implemented")
  def atan: Complex = throw new PartialException("atan not implemented")

  def sin: Complex = {
      val zi = z * i
      (exp(zi) - exp(-zi)) / (2*i)
  }

  def sinh: Complex = {
    if (finite)
      ( exp(z) - exp(-z) ) / 2
    else throw new PartialException("sinh ∞")
  }

  def cos: Complex =
    if (finite) {
      val zi = z * i
      ( exp(zi) + exp(-zi) ) / 2
      //( e^zi + e^(-zi) ) / 2
    }
    else throw new PartialException("cos ∞")

  def cosh: Complex =
    if (finite)
      (exp(z) + (exp(-z))) / 2
      //(e^z + e^(-z)) / 2
    else throw new PartialException("cosh ∞")

  def tan: Complex = z.sin / z.cos

  def tanh: Complex = z.sinh / z.cosh

  def exp: Complex = if (z.finite) mkPolar(Math.exp(re), im) else ∞
  private def exp(z: Complex): Complex = z.exp

  def ln: Complex =
    if (!z.finite) ∞
    else if (isZero) throw new PartialException("ln 0")
    else Complex(Math.log(z.modulus), z.argument)

  def argument: Double = if (finite && !isZero) {
    val d = Math.atan2(im, re)
    if (argContinuous) {
      val q = quadrant
      if (lastQuad == 2 && q == 3)
        k += 1
      else if (lastQuad == 3 && q==2)
        k -= 1
      lastQuad = q
      d + 2 * k * Math.PI
    }
    else d
  }
  else 0

  def modulus: Double =
    if (z.finite)
      Math.sqrt(sqr(re) + sqr(im))
    else
      java.lang.Double.POSITIVE_INFINITY

  private def sqr(d: Double) = d * d

  /** complex conjugate */
  def conj: Complex = if (finite) Complex(re, -im) else ∞

  def unary_- : Complex = if (finite) Complex(-re, -im) else ∞

  def opp: Complex =
    if (!z.finite) 0
    else if (z.isZero) ∞
    else mkPolar(1 / z.modulus, z.argument + Math.PI)

  def reciprocal: Complex = {
    if (isZero) ∞
    else if (!finite) 0
    else mkPolar(1 / z.modulus, z.argument + Math.PI)
  }

  /** Factorial function, extended to complex numbers with infinity.
    * ∞ -> ∞
    * Then look at real part only.
    * r <= 1  --> 1
    * r       --> r * f(r-1)
    * */
  def fac: Complex = {
    def f(r: Double): Double =
      if(r<=1) 1
      else r * f(r-1)

    if(!z.finite) ∞ else f(z.re)
  }

  def sqrt: Complex = {
    if (!finite) Complex.∞
    else {
      val d = z.modulus
      var d1 = .0
      var d2 = .0
      if (d == 0.0D) {
        d1 = d
        d2 = d
      }
      else if (0 < re) {
        d1 = Math.sqrt(0.5 * (d + re))
        d2 = im / d1 / 2D
      }
      else {
        d2 = Math.sqrt(0.5 * (d - re))
        if (im < 0)
          d2 = -d2
        d1 = im / d2 / 2D
      }
      Complex(d1, d2)
    }
  }

  // operators of 2 parameters ----------------------------------

  def + (ec: Complex): Complex = {
    if (z.finite) {
      if (ec.finite) Complex(re + ec.re, im + ec.im) else ∞
    } else {
      if (ec.finite) ∞ else throw new PartialException("∞ + ∞")
    }
  }

  def - (c: Complex): Complex = {
    if (z.finite) {
      if (c.finite) Complex(z.re - c.re, z.im - c.im) else ∞
    } else {
      if (c.finite) ∞ else throw new PartialException("∞ - ∞")
    }
  }

  def * (c: Complex): Complex = {
    if (z.isZero) {
      if (c.finite) 0
      else throw new PartialException("0 * ∞")
    }
    else if (z.finite) {
      if (c.isZero) 0
      else if (c.finite) Complex(z.re * c.re - z.im * c.im, z.re * c.im + c.re * z.im)
      else ∞
    }
    else {
      if (c.isZero) throw new PartialException("∞ * 0")
      ∞
    }
  }

  def / (d: Double): Complex = {
    if (z.isZero) {
      if (d == 0) throw new PartialException("0/0") else 0
    }
    else if (finite) {
      if (d == 0) ∞
      else Complex(re / d, im / d)
    }
    else {
      if (d == 0) throw new PartialException("∞/0")
      else ∞
    }
  }

  def / (ec: Complex): Complex = {

    def div(d: Double, d1: Double, d2: Double, d3: Double): Array[Double] = {
      val d4 = Math.abs(d2)
      val d5 = Math.abs(d3)
      var d6 = .0
      var d7 = .0
      var d10 = .0
      if (d4 <= d5) {
        val d8 = d2 / d3
        d10 = d3 * (1.0D + d8 * d8)
        d6 = d * d8 + d1
        d7 = d1 * d8 - d
      }
      else {
        val d9 = d3 / d2
        d10 = d2 * (1.0D + d9 * d9)
        d6 = d + d1 * d9
        d7 = d1 - d * d9
      }
      Array(d6 / d10, d7 / d10)
    }

    if (z.isZero) {
      if (ec.isZero) throw new PartialException("0/0")
      if (ec.finite) 0
      else throw new PartialException("0/∞")
    }
    else if (z.finite) if (ec.isZero) ∞
    else if (ec.finite) {
      val ad = div(re, im, ec.re, ec.im)
      Complex(ad(0), ad(1))
    }
    else 0
    else {
      if (ec.isZero) throw new PartialException("∞/0")
      if (ec.finite) ∞
      else throw new PartialException("∞/∞")
    }
  }

  /** Raises this number to the power of another, z^y. */
  def ^ (y: Complex): Complex = {
    if (z.isZero) {
      if (y.isZero) throw new PartialException("0^0")
      else 0
    }
    else if (z.finite) {
      if (y.isZero) 1     // x^0 = 1
      else if (y.finite) { // x^y
        val mx = Math.log(z.modulus)
        // ln(mod(x))
        var ax = Math.atan2(z.im, z.re) // angle(x). Angle(-1) should be pi, not -pi. I want -pi < ax <= pi.
        if (ax == -Math.PI) ax = Math.PI //Prefer pi over -pi
        val mr = Math.exp(mx * y.re - y.im * ax)
        // mod(x^y) = e^(mod(x)*re(y) - angle(x)*im(y))
        val ar = y.im * mx + y.re * ax // angle(x^y) = im(y)*mod(x) + re(y)*angle(x)
        mkPolar(mr, ar)
      }
      else ∞ // x^∞ = ∞
    }
    else if (y.isZero) throw new PartialException("∞^0")  // ∞^0 = undefined
    else ∞ // ∞^y = ∞
  }

  def distance(c: Complex): Double =
    if (z.finite && c.finite) Math.sqrt(sqr(re - c.re) + sqr(im - c.im))
    else if (z.finite != c.finite) 1.0D / 0.0D
    else 0.0D

  /** XXX revise */
  def equals(ec: Complex): Boolean = {
    if (!z.finite && !ec.finite) true
    else if (z.finite || ec.finite) false
    else z.re==ec.re && z.im==ec.im
  }

}

object Complex{

  // formatting, maybe disappears ---------------

  val piString = "π"
  val infinityString = "∞";

  /** for formatting */
  private var PRECISION: Int = 4
  def setPrecision(np: Int): Unit = {
    val op = PRECISION
    PRECISION = np
    EPSILON = Math.pow(10D, -PRECISION)
    nf.setMaximumFractionDigits(np)
  }
  def getPrecision: Int = PRECISION

  private val nf = NumberFormat.getInstance()
  nf.setGroupingUsed(false)
  nf.setMaximumFractionDigits(10)

  // state, maybe disappears --------------------

  private var EPSILON: Double = Math.pow(10D, -PRECISION)

  /** important for curves in polar coordinates */
  private var argContinuous: Boolean = false
  private var k = 0
  private var lastQuad = 0
  private def resetArg(): Unit = {
    lastQuad = 0
    k = 0
  }
  def setArgContinuous(): Unit = {
    argContinuous = true
  }
  def setArgPrincipal(): Unit = {
    argContinuous = false
  }

  // Constructors --------------------------------

  def apply(re: Double): Complex = if(re.isInfinite) ∞ else new Complex(re, 0d)

  def apply(re: Double, im: Double): Complex =
    if(re.isInfinite || im.isInfinite) ∞ else new Complex(re, im)

  def mkPolar(d: Double, d1: Double): Complex =
    if (java.lang.Double.isInfinite(d)) ∞ else Complex(d * Math.cos(d1), d * Math.sin(d1))

  //Constants -----------------------------------

  val e = Math.E
  val i = Complex(0,1)
  val π = Math.PI
  val ∞ = Infinity

  //Conversions ---------------------------------

  implicit def int2Complex(n: Int): Complex = Complex(n.toDouble)
  implicit def long2Complex(n: Long): Complex = Complex(n.toDouble)
  implicit def double2Complex(d: Double): Complex = Complex(d)

}