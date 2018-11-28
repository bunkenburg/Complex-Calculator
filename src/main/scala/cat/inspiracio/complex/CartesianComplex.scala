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

//the object
import Complex._

//the package object
import cat.inspiracio.numbers._

/** Finite complex numbers in Cartesian representation */
class CartesianComplex
  (override val re: Double, override val im: Double)  //XXX reduce visibility
  extends Complex
{
  require( !re.isInfinite && !im.isInfinite, "Infinite: " + re + " " + im)

  /** just to make nicer formulas in this class */
  private val z = this

  /** Format a complex number nicely.
    * XXX improve */
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

  override val finite = true
  override val isZero = re==0 && im==0

  /** Why results 0, 1, 2, 3, 4 ?
    * I would expect only four results. */
  private def quadrant: Int =
    if (0 <= re  && 0 <= im) 1
    else if (re < 0 && 0 <= im ) 2
    else if (re < 0 && im < 0) 3
    else if (re < 0 || 0 <= im) 0
    else 4

  protected def acos = throw new PartialException("acos not implemented.")
  protected def asin: CartesianComplex = throw new PartialException("asin not implemented")
  protected def atan: CartesianComplex = throw new PartialException("atan not implemented")

  override def sin: Complex = {
    val zi = z * i
    (zi.exp - (-zi).exp) / (2 * i)
  }

  def sinh: Complex = ( z.exp - (-z).exp ) / 2

  def cos: Complex = {
    val zi = z * i
    (zi.exp + (-zi).exp) / 2
    //( e^zi + e^(-zi) ) / 2
  }

  def cosh: Complex =
      ( z.exp + ( (-z).exp)) / 2
      //(e^z + e^(-z)) / 2

  def tan: Complex = z.sin / z.cos

  def tanh: Complex = z.sinh / z.cosh

  def exp: Complex =
    mkPolar(Math.exp(re), im)

  def ln: Complex =
    if (isZero) throw new PartialException("ln 0")
    else Complex(Math.log(z.modulus), z.argument)

  override def argument: Double = if (finite && !isZero) {
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

  override def modulus: Double =
    if (z.finite)
      Math.sqrt(sqr(re) + sqr(im))
    else
      java.lang.Double.POSITIVE_INFINITY

  private def sqr(d: Double) = d * d

  /** complex conjugate */
  def conj: Complex = Complex(re, -im)

  def unary_- : Complex = Cartesian(-re, -im)

  def opp: Complex =
    if (z.isZero) ∞
    else mkPolar(1 / z.modulus, z.argument + Math.PI)

  def reciprocal: Complex =
    if (isZero) ∞
    else mkPolar(1 / z.modulus, z.argument + Math.PI)

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

    double2Complex(f(z.re))
  }

  protected def sqrt: Complex = {
    if (!finite) ∞
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

  override def + (c: Complex): Complex =
    c match {
      case ∞ => ∞
      case Cartesian(cre, cim) => Cartesian(re + cre, im + cim)
    }

  override def - (c: Complex): Complex = {
    if (z.finite) {
      val Cartesian(cre, cim) = c
      if (c.finite) Cartesian(z.re - cre, z.im - cim) else ∞
    } else {
      if (c.finite) ∞ else throw new PartialException("∞ - ∞")
    }
  }

  def * (c: Complex): Complex = {

    if (z.isZero) {
      if (c.finite) int2Complex(0)
      else throw new PartialException("0 * ∞")
    }

    else if (z.finite) {
      if (c.isZero) int2Complex(0)
      else if (c.finite) {
        val Cartesian(cre, cim) = c
        Cartesian(z.re * cre - z.im * cim, z.re * cim + cre * z.im)
      }
      else ∞
    }

    else {
      if (c.isZero) throw new PartialException("∞ * 0")
      ∞
    }
  }

  def / (d: Double): Complex = {
    if (z.isZero) {
      if (d == 0) throw new PartialException("0/0")
      else int2Complex(0)
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

  override def / (c: Complex): Complex = {

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
      if (c.isZero) throw new PartialException("0/0")
      if (c.finite) int2Complex(0)
      else throw new PartialException("0/∞")
    }

    else if (z.finite) {
      if (c.isZero) ∞
      else if (c.finite) {
        val Cartesian(cre, cim) = c
        val ad = div(re, im, cre, cim)
        Cartesian(ad(0), ad(1))
      }
      else int2Complex(0)
    }

    else {
      if (c.isZero) throw new PartialException("∞/0")
      if (c.finite) ∞ else throw new PartialException("∞/∞")
    }
  }

  override def ^ (c: Complex): Complex = {
    if (z.isZero) {
      if (c.isZero) throw new PartialException("0^0")
      else int2Complex(0)
    }

    else if (z.finite) {
      if (c.isZero) int2Complex(1)     // x^0 = 1
      else if (c.finite) { // x^y

        val Cartesian(cre, cim) = c

        val mx = Math.log(z.modulus)
        // ln(mod(x))
        var ax = Math.atan2(z.im, z.re) // angle(x). Angle(-1) should be pi, not -pi. I want -pi < ax <= pi.
        if (ax == -Math.PI) ax = Math.PI //Prefer pi over -pi
        val mr = Math.exp(mx * cre - cim * ax)
        // mod(x^y) = e^(mod(x)*re(y) - angle(x)*im(y))
        val ar = cim * mx + cre * ax // angle(x^y) = im(y)*mod(x) + re(y)*angle(x)
        mkPolar(mr, ar)
      }
      else ∞ // x^∞ = ∞
    }

    else
      if (c.isZero) throw new PartialException("∞^0")  // ∞^0 = undefined
      else ∞ // ∞^y = ∞
  }

  def distance(c: Infinity.type ): Double =
    if (!z.finite) Double.PositiveInfinity else 0

  def distance(c: CartesianComplex): Double = Math.sqrt(sqr(z.re - c.re) + sqr(z.im - c.im))

  /** Copes with null and ∞;
    * accepts Byte, Int, Long, Float, Double,
    * but is not symmetric with these.
    * This comparison uses == on Double and is
    * therefore sometimes not useful.
    * */
  override def equals(other: Any): Boolean = {
    other match {
      case null => false

      case b: Byte => z == byte2Complex(b)
      case n: Int => z == int2Complex(n)
      case l: Long => z == long2Complex(l)
      case f: Float => z == float2Complex(f)
      case d: Double => z == double2Complex(d)

      case c: CartesianComplex =>
        if (!z.finite && !c.finite) true
        else if (z.finite != c.finite) false
        else z.re==c.re && z.im==c.im

      case ∞ => false

      case _ => false
    }
  }

  override def hashCode = (re,im).##

}

