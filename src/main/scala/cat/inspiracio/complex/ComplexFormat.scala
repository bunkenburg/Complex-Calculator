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
import java.util.{Currency, Formatter, Locale}

import cat.inspiracio.complex.imp.CartesianComplex

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

  /** Formats a real number.
    * @param pos Ignored.
    *            On input: an alignment field, if desired.
    *            On output: the offsets of the alignment field. */
  override def format(v: Double, buffer: StringBuffer, pos: FieldPosition): StringBuffer = nf.format(v, buffer, pos)

  /** Formats a real number.
    * @param pos Ignored.
    *            On input: an alignment field, if desired.
    *            On output: the offsets of the alignment field. */
  override def format(l: Long, buffer: StringBuffer, pos: FieldPosition): StringBuffer = nf.format(l, buffer, pos)

  /** Formats a complex number to a buffer. New method.
    * @pos Ignored. */
  def format(c: Complex, buffer: StringBuffer, pos: FieldPosition): StringBuffer = {
    val s = format(c)
    buffer append s
  }

  /** Formats as real number:
    * NaN
    * 0
    * ∞
    * e -e
    * π -π
    * 10⁻³ <= m < 10⁷ => 1655.0 or 7856.05
    * m < 10⁻³ or 10⁷ < m => "-3.153 * 10^-8"
    * */
  private def formatReal(d: Double): String = {

    /** Cleans computerized scientific notation.
      * From "9.0E-4" makes "9.0 * 10⁻4". */
    def clean(s: String) = s.replace("E", " * 10^")

    if(d.isNaN)
      "NaN"
    else if(d==0)
      "0"
    else if(d.isInfinite)
      "∞"
    else {
      val sign = if(0 <= d) "" else "-"
      val m = d.abs
      if(m==e)
        sign + "e"
      else if(m==π)
        sign + "π"
      else{
        val MIN = 0.001       // 10⁻³
        val MAX = 10000000    // 10⁷
        //decimal notation
        if(MIN <= m && m < MAX){
          if( d.isWhole )
            d.toInt.toString
          else {
            nf.format(d)
          }
        }
        //scientific notation
        else{
          clean(d.toString)
        }
      }
    }
  }

  /** Formats as imaginary number:
    * NaN
    * 0
    * ∞
    * ei -ei
    * πi -πi
    * 10⁻³ <= m < 10⁷ => 1655.0i or 7856.05i
    * m < 10⁻³ or 10⁷ < m => "-3.153i * 10^-8"
    * */
  private def formatImaginary(d: Double): String = {

    /** Cleans computerized scientific notation.
      * From "9.0E-4" makes "9.0i * 10⁻4". */
    def clean(s: String) = s.replace("E", "i * 10^")

    if(d.isNaN)
      "NaN"
    else if(d==0)
      "0"
    else if(d.isInfinite)
      "∞"
    else {
      val sign = if(0 <= d) "" else "-"
      val m = d.abs
      if(m==1)
        sign + "i"
      else if(m==e)
        sign + "ei"
      else if(m==π)
        sign + "πi"
      else{
        val MIN = 0.001       // 10⁻³
        val MAX = 10000000    // 10⁷
        if(MIN <= m && m < MAX) {
          if( d.isWhole )
            d.toInt.toString + "i"
          else
            d.toString + "i"
        } else {
          clean(d.toString)
        }
      }
    }
  }

  private def formatCartesian(re: Double, im: Double) = {
    val r = formatReal(re)
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
    *     ${m}e^${rho}πi  like    3.21e^0.75πi. Configurable precision.
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
        nf.format(m) + "e^" + nf.format(rho) + "πi"
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
    * If the number is real, like "-3.153 * 10^-8". Configurable precision.
    *
    * If the number is imaginary:
    *   special values: i, -i, ei, -ei, πi, -πi.
    *   10⁻³ <= |c| < 10⁷ then like "-7856.05i".
    *   otherwise like "-3.153i * 10^-87". Configurable precision.
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
    *     ${m}e^${rho}πi  like    3.21e^0.75πi. Configurable precision.
    *
    * New method.
    * */
  def format(c: Complex): String = {
    val MIN = -10000000
    val MAX = 10000000

    if(polar)
      formatPolar(c)
    else
      c match {
        case ∞ => "∞"
        case Integer(n) if(MIN < n && n < MAX) => n.toString
        case Real(re) => formatReal(re)
        case Imaginary(im) => formatImaginary(im)
        case Cartesian(re,im) => formatCartesian(re,im)
      }
  }

  // parsing ------------------------------------------------------------------

  /** Parses a complex number from a buffer.
    * XXX Does nothing with the parse position. */
  override def parse(s: String, position: ParsePosition): Complex = parse(s)

  import cat.inspiracio.parsing.Syntax
  override def parse(s: String): Complex = Syntax.parse(s).apply(null)
}
