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

import java.text.{FieldPosition, NumberFormat, ParsePosition}
import java.util.{Formatter, Locale}

class ComplexFormat extends NumberFormat {

  override def format(v: Double, buffer: StringBuffer, position: FieldPosition): StringBuffer = {
    //simplify?
    val c: Complex = v
    format(c, buffer, position)
  }

  override def format(l: Long, buffer: StringBuffer, position: FieldPosition): StringBuffer = {
    //simplify?
    val c: Complex = l
    format(c, buffer, position)
  }

  /** Formats a complex number to a buffer */
  def format(c: Complex, buffer: StringBuffer, pos: FieldPosition): StringBuffer = {
    val s = format(c)
    buffer append s
  }

  /** Formats a complex number */
  def format(c: Complex): String = {
    import java.util.FormattableFlags.{ALTERNATE, LEFT_JUSTIFY, UPPERCASE}

    //maximumFractionDigits
    //minimumFractionDigits
    //maximumIntegerDigits
    //minimumIntegerDigits
    //currency
    //roundingMode
    //groupingUsed: Boolean
    //parseIntegerOnly: Boolean

    val polar = false

    val locale: Locale = Locale.getDefault
    val alternate = polar
    val left = false
    val upper = true
    val width = 0
    val precision = 3

    val a: Appendable = new java.lang.StringBuilder
    val fmt = new Formatter(a, locale)
    val flags =
      (if(alternate) ALTERNATE else 0) +
      (if(left) LEFT_JUSTIFY else 0) +
      (if(upper) UPPERCASE else 0 )
    c.formatTo(fmt, flags, width, precision)
    a.toString
  }

  /** Parses a complex number from a buffer.
    * Does nothing wit the parse position. */
  override def parse(s: String, position: ParsePosition): Complex = parse(s)

  import cat.inspiracio.parsing.Syntax
  override def parse(s: String): Complex = Syntax.parse(s).apply(null)
}
