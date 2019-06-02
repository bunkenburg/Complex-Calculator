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
package cat.inspiracio.complextests

import cat.inspiracio.complex._
import org.scalatest.FunSuite

class Formatting extends FunSuite {

  // parsing -------------------------------------------

  test("parse i+5"){
    val f = new ComplexFormat
    val c = f.parse("i + 5")
    assert( c === i+5 )
  }

  // testing Formattable -----------------------------------------------------
  // format polar ---------------------------------------

  test("0 polar"){
    val c: Complex = 0
    val s = f"${c}%#s"
    assert( s === "0" )
  }

  test("∞ polar"){
    val c: Complex = ∞
    val s = f"${c}%#s"
    assert( s === "∞" )
  }

  /** positive real */
  test("3.2 polar"){
    val c: Complex = 3.2 * e\(0*π*i)
    val s = f"${c}%#s"
    assert( s === "3.2" )
  }

  /** positive real */
  test("3.2 e^2πi polar"){
    val c: Complex = 3.2 * e\(2*π*i)
    val s = f"${c}%#s"
    assert( s === "3.2" )
  }

  /** negative real */
  test("3.21 polar"){
    val c: Complex = 3.21 * e\(π*i)
    val s = f"${c}%#s"
    assert( s === "-3.21" )
  }

  test("3.21 e^0.75πi polar"){
    val c: Complex = 3.21 * e\(0.75*π*i)
    val s = f"${c}%#s"
    assert( s === "3.21e^0.75πi" )
  }

  test("3.21 e^-0.75πi polar"){
    val c: Complex = 3.21 * e\(-0.75*π*i)
    val s = f"${c}%#s"
    assert( s === "3.21e^-0.75πi" )
  }

  // tests for ComplexFormat ===================================================

  // ComplexFormat.format(Object)

  test("format(Complex < Object)"){
    val f = new ComplexFormat
    val c: Object = i+5
    val s = f.format(c)
    assert( s === "5+i" )
  }

  test("format(double < Object)"){
    val f = new ComplexFormat
    f.maximumFractionDigits = 3
    val d: Double = (1.0 / 3.0)
    val o: Any = d
    val s = f.format(o)
    assert( s === "0.333" )
  }

  test("format(Long.MaxValue < Object)"){
    val f = new ComplexFormat
    f.maximumFractionDigits = 3
    val l: Long = Long.MaxValue
    val o: Any = l
    val s = f.format(o)
    assert( s === "9223372036854775807" )
  }

  // ComplexFormat.format(double)

  test("format(0: double)"){
    val f = new ComplexFormat
    f.maximumFractionDigits = 3
    val d: Double = 0
    val s = f.format(d)
    assert( s === "0" )
  }

  test("format(e: double)"){
    val f = new ComplexFormat
    f.maximumFractionDigits = 3
    val d: Double = e
    val s = f.format(d)
    assert( s === "e" )
  }

  test("format(-e: double)"){
    val f = new ComplexFormat
    f.maximumFractionDigits = 3
    val d: Double = -e
    val s = f.format(d)
    assert( s === "-e" )
  }

  test("format(π: double)"){
    val f = new ComplexFormat
    f.maximumFractionDigits = 3
    val d: Double = π
    val s = f.format(d)
    assert( s === "π" )
  }

  test("format(-π: double)"){
    val f = new ComplexFormat
    f.maximumFractionDigits = 3
    val d: Double = -π
    val s = f.format(d)
    assert( s === "-π" )
  }

  test("format(Double.PositiveInfinity: double)"){
    val f = new ComplexFormat
    f.maximumFractionDigits = 3
    val d: Double = Double.PositiveInfinity
    val s = f.format(d)
    assert( s === "∞" )
  }

  test("format(Double.NegativeInfinity: double)"){
    val f = new ComplexFormat
    f.maximumFractionDigits = 3
    val d: Double = Double.NegativeInfinity
    val s = f.format(d)
    assert( s === "∞" )
  }

  test("format(1/3: double)"){
    val f = new ComplexFormat
    f.maximumFractionDigits = 3
    val d = 1.0 / 3.0
    val s = f.format(d)
    assert( s === "0.333" )
  }

  // ComplexFormat.format(long)

  test("format(0: Long)"){
    val f = new ComplexFormat
    f.maximumFractionDigits = 3
    val l: Long = 0
    val s = f.format(l)
    assert( s === "0" )
  }

  test("format(Long.MaxValue: Long)"){
    val f = new ComplexFormat
    f.maximumFractionDigits = 3
    val l: Long = Long.MaxValue
    val s = f.format(l)
    assert( s === "9223372036854775807" )
  }

  test("format(Long.MinValue: Long)"){
    val f = new ComplexFormat
    f.maximumFractionDigits = 3
    val l: Long = Long.MinValue
    val s = f.format(l)
    assert( s === "-9223372036854775808" )
  }

  // ComplexFormat.format(Complex)

  test("format 3+i"){
    val f = new ComplexFormat
    val s = f.format(i+3)
    assert( s === "3+i" )
  }

  test("format 1.0/3.0"){
    val f = new ComplexFormat
    val c = 1.0/3.0
    val s = f.format( c )
    assert( s === "0.3333333333333333" )
  }

  test("format 1.0/3.0 maxfraction 3"){
    val f = new ComplexFormat
    f.maximumFractionDigits = 3
    val c = 1.0/3.0
    val s = f.format( c )
    assert( s === "0.333" )
  }

  test("format 1.0/3.0 i"){
    val f = new ComplexFormat
    val c = i/3
    val s = f.format( c )
    assert( s === "0.3333333333333333i" )
  }

  test("1.0 * 10^7"){
    val c: Complex = 1.0 * 10\7
    val f = new ComplexFormat
    f.minimumFractionDigits = 0

    assert( f.format(1.0) === "1" )

    val s = f.format(c)
    assert( s === "10000000" )

  }

  test("1.0 * 10^7 * i"){
    val c: Complex = 1.0 * 10\7 * i
    val f = new ComplexFormat
    f.minimumFractionDigits = 0
    val s = f.format(c)
    assert( s === "10000000i" )

  }

  test("1.0 * 10^8"){
    val c: Complex = 1.0 * 10\8
    val f = new ComplexFormat
    f.minimumFractionDigits = 0
    val s = f.format(c)
    assert( s === "100000000" )
  }

  test("1.0 * 10^8 * i"){
    val c: Complex = 1.0 * 10\8 * i
    val f = new ComplexFormat
    f.minimumFractionDigits = 0
    val s = f.format(c)
    assert( s === "100000000i" )
  }

  test("format 1.0/3.0 i maxfraction 3"){
    val f = new ComplexFormat
    f.maximumFractionDigits = 3
    val c = i/3
    val s = f.format( c )
    assert( s === "0.333i" )
  }

  test("format 1/3 + i/3"){
    val f = new ComplexFormat
    val c = 1.0/3.0 + i/3
    val s = f.format( c )
    assert( s === "0.3333333333333333 + 0.3333333333333333i" )
  }

  test("format 1/3 + i/3 maxfraction 3"){
    val f = new ComplexFormat
    f.maximumFractionDigits = 3
    val c = 1.0/3.0 + i/3
    val s = f.format( c )
    assert( s === "0.333 + 0.333i" )
  }

  test("format 1.0 minfraction 3"){
    val f = new ComplexFormat
    f.minimumFractionDigits = 3
    val c = 1.0
    val s = f.format( c )
    assert( s === "1" )
  }

  test("format 0 polar"){
    val f = new ComplexFormat
    f.polar = true
    val c: Complex = 0
    assert( f.format(c) === "0" )
  }

  test("format 1+i polar"){
    val f = new ComplexFormat
    f.maximumFractionDigits = 3
    f.polar = true
    val c = i+1
    val s = f.format( c )
    assert( s === "1.414e^0.25πi" )
  }

  test("format ∞ polar"){
    val f = new ComplexFormat
    f.polar = true
    assert( f.format(∞) === "∞" )
  }

}
