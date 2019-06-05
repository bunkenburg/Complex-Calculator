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

import java.util.Formatter

import cat.inspiracio.complex._

/** Finite complex numbers in Cartesian representation */
class CartesianComplex(val re: Double, val im: Double) extends Complex {
  require( !re.isInfinite && !im.isInfinite, "Infinite: " + re + " " + im)

  //interface java.lang.Number
  //All methods discard the imaginary part and are therefore useless.
  override def byteValue() = re.toByte
  override def shortValue() = re.toShort
  override def intValue() = re.toInt
  override def longValue() = re.toLong
  override def floatValue() = re.toFloat
  override def doubleValue() = re

  /** Gives a practical precise String representation of the complex number.
    * No loss of information: different complex numbers map to different strings.
    * If you want to round or polar representation, use ComplexFormat.
    *
    * If the number is infinity, "∞".
    *
    * If the number is integer and |c| < 10⁷ = 10 000 000, just all digits.
    *
    * For e and π, "e" and "π".
    *
    * If the number is real and 10⁻³ <= |c| < 10⁷, digits, decimal point, digits.
    * If the number is real, like "-3.153 * 10\-8".
    *
    * If the number is imaginary:
    *   special values: i, -i, ei, -ei, πi, -πi.
    *   10⁻³ <= |c| < 10⁷ then like "-7856.05i".
    *   otherwise like "-3.153i * 10\-87".
    *
    * Otherwise, cartesian representation.
    *
    * */
  override def toString: String = {
    val f = new ComplexFormat
    f.format(this)
  }

  /** @param flags UPPERCASE ALTERNATE LEFT_JUSTIFY */
  override def formatTo(fmt: Formatter, flags: Int, width: Int, precision: Int) = {
    import java.util.FormattableFlags.{ALTERNATE, LEFT_JUSTIFY, UPPERCASE}

    //val locale = fmt.locale
    val alternate = (flags & ALTERNATE) == ALTERNATE
    //val left = (flags & LEFT_JUSTIFY) == LEFT_JUSTIFY
    //val upper = (flags & UPPERCASE) == UPPERCASE
    //width
    //precision

    val f = new ComplexFormat
    f.polar = alternate
    val s = f.format(this)

    fmt.format(s)
  }

  // methods with 0 parameters ------------------------------------

  override def unary_- : Complex = {
    //Avoids -0.0. Methods == can not distinguish 0.0 and -0.0 but equals can.
    val r = if(re==0.0) 0.0 else -re
    val i = if(im==0.0) 0.0 else -im
    Cartesian(r,i)
  }

  lazy val argument: Double = {
    import Complex.lastQuad
    import Complex.k
    import scala.math.atan2

    def quadrant: Int =
      if (0 <= re  && 0 <= im) 1
      else if (re < 0 && 0 <= im ) 2
      else if (re < 0 && im < 0) 3
      else 4

    if (!(this === 0)) {
      //Avoids -0.0. Methods == can not distinguish 0.0 and -0.0 but equals can.
      //atan2 treats 0.0 and -0.0 differently but we always prefer 0.0.
      val r = if(re==0.0) 0.0 else re
      val i = if(im==0.0) 0.0 else im
      val d = atan2(i,r)

      if (Complex.isArgContinuous) {
        val q = quadrant
        if (lastQuad == 2 && q == 3)
          k += 1
        else if (lastQuad == 3 && q == 2)
          k -= 1
        lastQuad = q
        d + 2 * k * π
      }
      else d
    }
    else 0
  }

  lazy val modulus: Double = scala.math.hypot(re, im) //sqrt(sqr(re) + sqr(im))

  // operators of 2 parameters ----------------------------------

  override def + (d: Double): Complex = Cartesian(re + d, im)

  override def + (c: Complex): Complex = c match {
    case ∞ => ∞
    case Cartesian(cre, cim) => new CartesianComplex(re + cre, im + cim)
  }

  override def - (d: Double): Complex = Cartesian(re-d, im)

  override def - (c: Complex): Complex = c match {
    case ∞ => ∞
    case Cartesian(cre, cim) => new CartesianComplex(re - cre, im - cim)
  }

  override def * (d: Double): Complex = Cartesian(re * d, im * d)

  override def * (c: Complex): Complex =
    if (this === 0) {
      c match {
        case ∞ => throw new ArithmeticException("0 * ∞")
        case _ => 0
      }
    }
    else {
      c match {
        case ∞ => ∞
        case Cartesian(cre, cim) => Cartesian(re * cre - im * cim, re * cim + cre * im)
      }
    }

  override def / (d: Double): Complex =
    if(this === 0) {
      if (d == 0) throw new ArithmeticException("0/0")
      else 0
    }
    else Cartesian(re / d, im / d)

  /** Division for two Cartesian
    *
    * (ar + ai*i) / (br + bi*i)
    * */
  private def div(ar: Double, ai: Double, br: Double, bi: Double): Complex = {
    //I don't understand this anymore.
    //It is from decompiled code.
    //XXX Better multiply divisor by conjugate.
    val d4 = abs(br)
    val d5 = abs(bi)

    val (d10, d6, d7) = if (d4 <= d5) {
      val d8 = br / bi
      (
        bi * (1.0D + d8 * d8),
        ar * d8 + ai,
        ai * d8 - ar
      )
    }
    else {
      val d9 = bi / br
      (
        br * (1.0D + d9 * d9),
        ar + ai * d9,
        ai - ar * d9
      )
    }

    Cartesian(d6 / d10, d7 / d10)
  }

  override def / (c: Complex): Complex =
    // See http://www.mesacc.edu/~scotz47781/mat120/notes/complex/dividing/dividing_complex.html
    // for a better algorithm.

    if (this === 0) {
      c match {
        case Real(0) => throw new ArithmeticException("0/0")
        case ∞ => throw new ArithmeticException("0/∞")
        case _ => 0
      }
    }

    else {
      c match {
        case Real(0) => ∞
        case ∞ => 0
        case Cartesian(cre, cim) => div(re, im, cre, cim)
      }
    }

  override def === (c: Int): Boolean = re==c && im==0

  /** Copes with null and ∞;
    * accepts Byte, Int, Long, Float, Double,
    * but is not symmetric with these.
    * This comparison uses == on Double and is
    * therefore sometimes not useful.
    * */
  override def equals(other: Any): Boolean = other match {
      case null => false
      case b: Byte => re==b && im==0
      case n: Int =>  re==n && im==0
      case l: Long => re==l && im==0
      case f: Float => re==f && im==0
      case d: Double => re==d && im==0
      case c: CartesianComplex => this.re==c.re && this.im==c.im
      case ∞ => false
      case _ => false
    }

  override def hashCode = (re,im).##

}