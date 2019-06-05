/*	Copyright 2019 Alexander Bunkenburg alex@inspiracio.cat
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

import java.math.RoundingMode
import java.text.{FieldPosition, NumberFormat, ParsePosition}
import java.util.{Currency, Locale}

import cat.inspiracio.complex.imp.CartesianComplex

/** Formatting and parsing of complex numbers.
  *
  * Complex.toString gives a string with maximal precision in a notation
  * that should be familiar to mathematicians and useful.
  *
  * For more control over the format, use this class.
  *
  * Features:
  * - Favours special numbers: e, π, ∞
  * - cartesian or polar representation
  * - ComplexFormat extends NumberFormat, and all methods of NumberFormat relating
  *   to Object, double, and long, can be used similar to NumberFormat.
  * - minimum/maximumFractionDigits: how many digits after decimal point
  *   in real part, imaginary part, and magnitude part of polar representation
  * - minimum/maximumIntegerDigits: like in NumberFormat, not really useful for complex
  * - polar: Boolean: whether to generate a polar representation of the number
  *
  * */
class ComplexFormat extends NumberFormat {

  // state ----------------------------------------------

  private val nf = NumberFormat.getInstance(Locale.US);

  var polar: Boolean = false

  // initial configuration

  minimumFractionDigits = 0
  maximumFractionDigits = 340
  minimumIntegerDigits = 1
  maximumIntegerDigits = 309
  // decimalSeparatorAlwaysShown = false
  parseIntegerOnly = false
  roundingMode = RoundingMode.HALF_EVEN
  groupingUsed = false

  // Make configurable limits for scientific notation?

  // Scala-style configuration methods ------------------

  def maximumFractionDigits: Int = getMaximumFractionDigits
  def maximumFractionDigits_=(i: Int) = setMaximumFractionDigits(i)
  override def setMaximumFractionDigits(i: Int): Unit = {
    super.setMaximumFractionDigits(i)
    nf.setMaximumFractionDigits(i)
  }

  def minimumFractionDigits: Int = getMinimumFractionDigits
  def minimumFractionDigits_=(i: Int) = setMinimumFractionDigits(i)
  override def setMinimumFractionDigits(i: Int): Unit = {
    super.setMinimumFractionDigits(i)
    nf.setMinimumFractionDigits(i)
  }

  def maximumIntegerDigits: Int = getMaximumIntegerDigits
  def maximumIntegerDigits_=(i: Int) = setMaximumIntegerDigits(i)
  override def setMaximumIntegerDigits(i: Int): Unit = {
    super.setMaximumIntegerDigits(i)
    nf.setMaximumIntegerDigits(i)
  }

  def minimumIntegerDigits: Int = getMinimumIntegerDigits
  def minimumIntegerDigits_=(i: Int) = setMinimumIntegerDigits(i)
  override def setMinimumIntegerDigits(i: Int): Unit = {
    super.setMinimumIntegerDigits(i)
    nf.setMinimumIntegerDigits(i)
  }

  def roundingMode = getRoundingMode
  def roundingMode_=(r: RoundingMode) = setRoundingMode(r)
  override def setRoundingMode(r: RoundingMode): Unit = {
    //super.setRoundingMode(r) throws UnsupportedOperationException
    nf.setRoundingMode(r)
  }

  def groupingUsed = isGroupingUsed
  def groupingUsed_=(b: Boolean) = setGroupingUsed(b)
  override def setGroupingUsed(b: Boolean): Unit = {
    super.setGroupingUsed(b)
    nf.setGroupingUsed(b)
  }

  def parseIntegerOnly = isParseIntegerOnly
  def parseIntegerOnly_=(b: Boolean) = setParseIntegerOnly(b)
  override def setParseIntegerOnly(b: Boolean): Unit = {
    super.setParseIntegerOnly(b)
    nf.setParseIntegerOnly(b)
  }

  override def setCurrency(currency: Currency): Unit = throw new UnsupportedOperationException

  // formatting ----------------------------------------------------------

  /** If number is a Complex, calls specialised method,
    * rather than treating Complex as Number.
    * @param pos
    *            On input: an alignment field, if desired.
    *            On output: the offsets of the alignment field. */
  override def format(number: Object, toAppendTo: StringBuffer, pos: FieldPosition) =
    if ( number.isInstanceOf[Complex] )
      format( number.asInstanceOf[Complex], toAppendTo, pos)
    else
      super.format( number, toAppendTo, pos )

  /** Formats a real number:
    * NaN
    * 0
    * ∞
    * e -e
    * π -π
    * 10⁻³ <= m < 10⁷ => 1655.0 or 7856.05
    * m < 10⁻³ or 10⁷ < m => "-3.153 * 10\-8"
    * @param pos Ignored.
    *            On input: an alignment field, if desired.
    *            On output: the offsets of the alignment field. */
  override def format(d: Double, buffer: StringBuffer, pos: FieldPosition): StringBuffer = {

    if(d.isNaN)
      buffer append "NaN"
    else if(d==0.0 || d== -0.0 )
      buffer append "0"
    else if(d.isInfinite)
      buffer append "∞"
    else {
      val sign = if(0 <= d) "" else "-"
      val m = d.abs
      if(m==e)
        buffer append (sign + "e")
      else if(m==π)
        buffer append (sign + "π")
      else{
        val MIN = 0.001       // 10⁻³
        val MAX = 10000000    // 10⁷
        //decimal notation
        if(MIN <= m && m < MAX){
          if( d.isWhole )
            buffer append format(d.toLong)
          else {
            nf.format(d, buffer, pos)
          }
        }
        //scientific notation
        else{
          val s = formatScientific(d)
          buffer append s
        }
      }
    }

  }

  private def formatScientific(d: Double): String = {
    //maybe this has to be more robust
    val s = d.toString
    val parts = s.split("E")
    //val sign = if( d < 0 ) "-" else ""
    val mantissa = parts(0).toDouble
    val exponent = parts(1).toLong
    if(mantissa==1.0)
      "10\\" + format(exponent)
    else
      format(mantissa) + " * 10\\" + format(exponent)
  }

  /** Formats a real number.
    * @param pos Ignored.
    *            On input: an alignment field, if desired.
    *            On output: the offsets of the alignment field. */
  override def format(l: Long, buffer: StringBuffer, pos: FieldPosition): StringBuffer = {
    //separate method in case later this becomes configurable
    val s = l.toString
    buffer append s
  }

  /** Formats a complex number to a buffer. New method.
    * @pos Ignored. */
  def format(c: Complex, buffer: StringBuffer, pos: FieldPosition): StringBuffer = {
    val s = format(c)
    buffer append s
  }

  /** Formats as imaginary number:
    * NaN
    * 0
    * ∞
    * ei -ei
    * πi -πi
    * 10⁻³ <= m < 10⁷ => 1655.0i or 7856.05i
    * m < 10⁻³ or 10⁷ < m => "-3.153i * 10\-8"
    * */
  private def formatImaginary(d: Double): String = {
    if(d == 1)
      "i"
    else if(d == -1)
      "-i"
    else {
      val s = format(d)
      s + "i"
    }
  }

  private def formatCartesian(re: Double, im: Double) = {
    val r = format(re)
    val i = formatImaginary(im)
    val length = r.length + i.length
    val SHORT = 7
    if(i startsWith "-" ) {
      if( length <= SHORT )
        r + i
      else
        r + " " + i
    }
    else {
      if( length <= 7)
        r + "+" + i
      else
        r + " + " + i
    }
  }

  /** Formats a complex number in polar representation.
    * - special cases 0 and ∞
    * - special cases: real and pure imaginary numbers (Good decision?)
    * - otherwise:
    *     val m = |c|
    *     val rho = principal value / π
    *     ${m}e\${rho}πi  like    3.21e\0.75πi. Configurable precision.
    * */
  private def formatPolar(c: Complex): String = {
    c match {
      case Real(0) => "0"
      case ∞ => "∞"
      case _ => {
        val m = abs(c)
        val rho: Double = c match {
          case c: CartesianComplex => c.argument / π
        }
        if(rho == 0 )
          nf.format(m)
        else if(rho == 1 )
          nf.format(-m)
        else
          nf.format(m) + "e\\" + nf.format(rho) + "πi"
      }
    }
  }

  /** Gives a practical String representation of the complex number.
    *
    * Depending on configuration of this ComplexFormat, there may be loss of information
    * due to rounding.
    *
    * If the number is infinity, "∞".
    *
    * If the number is integer and |c| < 10⁷ = 10 000 000, just all digits.
    *
    * For e and π, "e" and "π".
    *
    * If the number is real and 10⁻³ <= |c| < 10⁷, digits, decimal point, digits.
    * If the number is real, like "-3.153 * 10\-8". Configurable precision.
    *
    * If the number is imaginary:
    *   special values: i, -i, ei, -ei, πi, -πi.
    *   10⁻³ <= |c| < 10⁷ then like "-7856.05i".
    *   otherwise like "-3.153i * 10\-87". Configurable precision.
    *
    * Otherwise, cartesian representation. Configurable precision.
    *
    * If you configure polar representation:
    *
    * - special cases 0 and ∞
    * - special cases: real and pure imaginary numbers (Good decision?)
    * - otherwise:
    *     val m = |c|
    *     val rho = principal value
    *     ${m}e\${rho}πi  like    3.21e\0.75πi. Configurable precision.
    *
    * New method.
    * */
  def format(c: Complex): String = {
    val MIN = -10000000
    val MAX = 10000000
    c match {
      case Integer(n) if(MIN < n && n < MAX) =>
        format(n)
      case Real(re) =>
        format(re)
      case Imaginary(im) =>
        formatImaginary(im)
      case Cartesian(re,im) =>
        formatCartesian(re,im)
      case _ => {
        val s = "ComplexFormat.format " + Re(c) + " + " + Im(c) + "i"
        println(s)
        s
      }
    }
  }

  // parsing ------------------------------------------------------------------

  /** Parses a complex number from a buffer.
    * XXX Does nothing with the parse position. */
  override def parse(s: String, position: ParsePosition): Complex = parse(s)

  import cat.inspiracio.parsing.Syntax
  override def parse(s: String): Complex = Syntax.parse(s).apply(null)
}
