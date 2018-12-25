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
class CartesianComplex(val re: Double, val im: Double) extends Complex {
  require( !re.isInfinite && !im.isInfinite, "Infinite: " + re + " " + im)

  /** Format real number nicely, with e and π.
    * Cuts off zeros after decimal point.
    * Cuts off decimal point for integers. */
  private def toString(d: Double): String = {

    //Some special real numbers
    if(d == e) "e"
    else if(d == -e) "-e"
    else if(d == π) "π"
    else if(d == -π) "-π"

    //General formatting
    else {
      val s = d.toString
      if (s.contains('.')) {
        val b = new StringBuilder(s)

        //Cuts off trailing zeros.
        while ( b.charAt(b.length - 1) == '0')
          b.setLength(b.length - 1)

        //Maybe cut off trailing '.' too.
        if (b.charAt(b.length - 1) == '.')
          b.setLength(b.length - 1)

        b.toString
      }
      else s  //NaN and other special cases
    }
  }

  /** Format a complex number nicely. */
  override def toString: String = {
    import Math.abs
    import Complex.ε

      //It's just a real number.
      if (abs(im) < ε)
        toString(re)

      //Cartesian x + yi
      else {
        val real = toString(re)

        val imaginary = if (abs(im - 1) < ε)
          "i"
        else if (abs(im + 1) < ε)
          "-i"
        else
          toString(im) + "i"

        if (abs(re) < ε)
          imaginary
        else
          real + (if (im <= 0) "" else "+") + imaginary
      }
  }

  // methods with 0 parameters ------------------------------------


  lazy val argument: Double = {
    import Complex.lastQuad
    import Complex.k

    /** Why results 0, 1, 2, 3, 4 ?
      * I would expect only four results. */
    def quadrant: Int =
    if (0 <= re  && 0 <= im) 1
    else if (re < 0 && 0 <= im ) 2
    else if (re < 0 && im < 0) 3
    else 4

    if (!(this === 0)) {
      val d = Math.atan2(im, re)
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

  lazy val modulus: Double = sqrt(sqr(re) + sqr(im))

  private def sqr(d: Double) = d * d

  private def sqrt(d: Double): Double = Math.sqrt(d)

  private def sqrt: Complex = {
    val Polar(m, _) = this

    if (m == 0 )
      0

    else if (0 < re) {
      val real = sqrt(0.5 * (m + re))
      val imaginary = im / real / 2
      Cartesian(real, imaginary)
    }

    else {
      var imaginary = sqrt(0.5 * (m - re))
      if (im < 0)
        imaginary = -imaginary
      val real = im / imaginary / 2
      Cartesian(real, imaginary)
    }
  }

  // operators of 2 parameters ----------------------------------

  override def unary_- : Complex = Cartesian(-re,-im)

  override def + (d: Double): Complex = Cartesian(re + d, im)

  override def + (c: Complex): Complex =
    c match {
      case ∞ => ∞
      case Cartesian(cre, cim) => Cartesian(re + cre, im + cim)
    }

  override def - (d: Double): Complex = Cartesian(re-d, im)

  override def - (c: Complex): Complex =
    c match {
      case ∞ => ∞
      case Cartesian(cre, cim) => Cartesian (re - cre, im - cim)
    }

  override def * (d: Double): Complex = Cartesian(re * d, im * d)

  override def * (c: Complex): Complex = {
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

  }

  override def / (d: Double): Complex = {
    if(this === 0) {
      if (d == 0) throw new ArithmeticException("0/0")
      else 0
    }
    else Cartesian(re / d, im / d)
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
  }

  /** Copes with null and ∞;
    * accepts Byte, Int, Long, Float, Double,
    * but is not symmetric with these.
    * This comparison uses == on Double and is
    * therefore sometimes not useful.
    * */
  override def equals(other: Any): Boolean = {
    other match {
      case null => false

      case b: Byte => this == byte2Complex(b)
      case n: Int => this == int2Complex(n)
      case l: Long => this == long2Complex(l)
      case f: Float => this == float2Complex(f)
      case d: Double => this == double2Complex(d)

      case c: CartesianComplex =>
        this.re==c.re && this.im==c.im

      case ∞ => false

      case _ => false
    }
  }

  override def hashCode = (re,im).##

}