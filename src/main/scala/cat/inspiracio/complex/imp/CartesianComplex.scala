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
package cat.inspiracio.complex.imp

import cat.inspiracio.complex._

/** Finite complex numbers in Cartesian representation */
class CartesianComplex
  (override val re: Double, override val im: Double)  //XXX reduce visibility
  extends Complex
{
  require( !re.isInfinite && !im.isInfinite, "Infinite: " + re + " " + im)

  /** Format real number nicely, with e and π. */
  private def toString(d: Double): String = {

    //Some special real numbers
    if(d == e) "e"
    else if(d == -e) "-e"
    else if(d == π) "π"
    else if(d == -π) "-π"

    //General formatting, XXX improve
    else {
      //val s = nf.format(d)
      val s = d.toString
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

  /** Format a complex number nicely.
    * XXX improve */
  override def toString: String = {

      //It's just a real number.
      if (Math.abs(im) < Complex.EPSILON)
        toString(re)

      //Cartesian x + yi
      else {
        val s = toString(re)

        var s1: String = null
        if (Math.abs(im - 1) < Complex.EPSILON)
          s1 = "i"
        else if (Math.abs(im + 1) < Complex.EPSILON)
          s1 = "-i"
        else
          s1 = toString(im) + "i"
        if (Math.abs(re) < Complex.EPSILON)
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

  def ln: Complex =
    if (isZero) throw new ArithmeticException("ln 0")
    else Cartesian(Math.log(modulus), argument)

  override def argument: Double = if (finite && !isZero) {
    val d = Math.atan2(im, re)
    if (Complex.argContinuous) {
      val q = quadrant
      if (Complex.lastQuad == 2 && q == 3)
        Complex.k += 1
      else if (Complex.lastQuad == 3 && q==2)
        Complex.k -= 1
      Complex.lastQuad = q
      d + 2 * Complex.k * Math.PI
    }
    else d
  }
  else 0

  override def modulus: Double = Math.sqrt(sqr(re) + sqr(im))

  private def sqr(d: Double) = d * d

  /** complex conjugate */
  def conj: Complex = Cartesian(re, -im)

  override def unary_- : Complex = Cartesian(-re, -im)

  def opp: Complex =
    if (this.isZero) ∞
    else Polar(1 / this.modulus, this.argument + π)

  def reciprocal: Complex =
    if (isZero) ∞
    else Polar(1 / this.modulus, this.argument + π)

  /** factorial function for natural numbers
    * @param n Assumes 0 <= n */
  private def f(n: Long): Long =
    if(n<=1) 1
    else n * f(n-1)

  /** Factorial function, for natural numbers only */
  def fac: Complex = {
    this match {
      case Natural(n) => f(n)
      case _ => throw new ArithmeticException(this +  "!")
    }
  }

  protected def sqrt: Complex = {
    val m = this.modulus

    if (m == 0 )
      0

    else if (0 < re) {
      val d1 = Math.sqrt(0.5 * (m + re))
      val d2 = im / d1 / 2
      Cartesian(d1, d2)
    }

    else {
      var d2 = Math.sqrt(0.5 * (m - re))
      if (im < 0)
        d2 = -d2
      val d1 = im / d2 / 2D
      Cartesian(d1, d2)
    }
  }

  // operators of 2 parameters ----------------------------------

  override def + (c: Complex): Complex =
    c match {
      case ∞ => ∞
      case Cartesian(cre, cim) => Cartesian(re + cre, im + cim)
    }

  override def - (c: Complex): Complex =
    c match {
      case ∞ => ∞
      case Cartesian(cre, cim) => Cartesian (re - cre, im - cim)
    }

  def * (c: Complex): Complex = {

    if (this.isZero) {
      if (c.finite) 0
      else throw new ArithmeticException("0 * ∞")
    }

    else {
      if (c.isZero) int2Complex(0)
      else if (c.finite) {
        val Cartesian(cre, cim) = c
        Cartesian(re * cre - im * cim, re * cim + cre * im)
      }
      else ∞
    }

  }

  override def / (d: Double): Complex = {
    if (isZero) {
      if (d == 0) throw new ArithmeticException("0/0")
      else 0
    }
    else {
      if (d == 0) ∞
      else Cartesian(re / d, im / d)
    }
  }

  /** Division for two Cartesian
    *
    * (ar + ai*i) / (br + bi*i)
    * */
  private def div(ar: Double, ai: Double, br: Double, bi: Double): Complex = {
    //I don't understand this anymore.
    //It is from decompiled code.
    val d4 = Math.abs(br)
    val d5 = Math.abs(bi)
    var d6 = .0
    var d7 = .0
    var d10 = .0

    if (d4 <= d5) {
      val d8 = br / bi
      d10 = bi * (1.0D + d8 * d8)
      d6 = ar * d8 + ai
      d7 = ai * d8 - ar
    }

    else {
      val d9 = bi / br
      d10 = br * (1.0D + d9 * d9)
      d6 = ar + ai * d9
      d7 = ai - ar * d9
    }

    Cartesian(d6 / d10, d7 / d10)
  }

  override def / (c: Complex): Complex = {
    // See http://www.mesacc.edu/~scotz47781/mat120/notes/complex/dividing/dividing_complex.html
    // for a better algorithm.

    if (isZero) {
      if (c.isZero) throw new ArithmeticException("0/0")
      if (c.finite) 0
      else throw new ArithmeticException("0/∞")
    }

    else {
      if (c.isZero) ∞
      else if (c.finite) {
        val Cartesian(cre, cim) = c
        div(re, im, cre, cim)
      }
      else int2Complex(0)
    }
  }

  /** Copes with null and ∞;
    * accepts Byte, Int, Long, Float, Double,
    * but is not symmetric with these.
    * This comparison uses == on Double and is
    * therefore sometimes not useful.
    * */
  override def equals(other: Any): Boolean = {
    val z = this
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