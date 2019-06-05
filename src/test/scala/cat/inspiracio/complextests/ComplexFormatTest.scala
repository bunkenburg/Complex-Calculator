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

class ComplexFormatTest extends FunSuite {

  // parsing -------------------------------------------

  test("parse i+5"){
    val f = new ComplexFormat
    val c = f.parse("i + 5")
    assert( c === i+5 )
  }

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

  // ComplexFormat.format(long)

  test("format(-9999999: Long)"){
    val f = new ComplexFormat
    val s = f.format(-9999999)
    assert( s === "-9999999" )
  }

  test("format(-1000000: Long)"){
    val f = new ComplexFormat
    val s = f.format(-1000000)
    assert( s === "-1000000" )
  }

  test("format(-1000: Long)"){
    val f = new ComplexFormat
    val s = f.format(-1000)
    assert( s === "-1000" )
  }

  test("format(-3: Long)"){
    val f = new ComplexFormat
    val s = f.format(-3)
    assert( s === "-3" )
  }

  test("format(-1: Long)"){
    val f = new ComplexFormat
    val s = f.format(-1)
    assert( s === "-1" )
  }

  test("format(0: Long)"){
    val f = new ComplexFormat
    val s = f.format(0)
    assert( s === "0" )
  }

  test("format(-0: Long)"){
    val f = new ComplexFormat
    val s = f.format(-0)
    assert( s === "0" )
  }

  test("format(1: Long)"){
    val f = new ComplexFormat
    val s = f.format(1)
    assert( s === "1" )
  }

  test("format(16: Long)"){
    val f = new ComplexFormat
    assert( f.format(16) === "16" )
  }

  test("format(1000: Long)"){
    val f = new ComplexFormat
    assert( f.format(1000) === "1000" )
  }

  test("format(1000000: Long)"){
    val f = new ComplexFormat
    val s = f.format(10000000)
    assert( s ===   "10000000" )
  }

  test("format(9999999: Long)"){
    val f = new ComplexFormat
    assert( f.format(9999999) === "9999999" )
  }

  test("format(Long.MaxValue: Long)"){
    val f = new ComplexFormat
    val s = f.format(Long.MaxValue)
    assert( s === "9223372036854775807" )
  }

  test("format(Long.MinValue: Long)"){
    val f = new ComplexFormat
    val s = f.format(Long.MinValue)
    assert( s === "-9223372036854775808" )
  }

  // ComplexFormat.format(double)

  test("format(-9999999: Double)"){
    val f = new ComplexFormat
    val d: Double = -9999999
    val s = f.format(d)
    assert( s === "-9999999" )
  }

  test("format(-1000000: Double)"){
    val f = new ComplexFormat
    val d: Double = -1000000
    val s = f.format(d)
    assert( s === "-1000000" )
  }

  test("format(-1000: Double)"){
    val f = new ComplexFormat
    val d: Double = -1000
    val s = f.format(d)
    assert( s === "-1000" )
  }

  test("format(-3: Double)"){
    val f = new ComplexFormat
    val d: Double = -3
    val s = f.format(d)
    assert( s === "-3" )
  }

  test("format(-1: Double)"){
    val f = new ComplexFormat
    val d: Double = -1
    val s = f.format(d)
    assert( s === "-1" )
  }

  test("format(0: Double)"){
    val f = new ComplexFormat
    val d: Double = 0.0
    val s = f.format(d)
    assert( s === "0" )
  }

  test("format(-0: Double)"){
    val f = new ComplexFormat
    val d: Double = -0.0
    val s = f.format(d)
    assert( s === "0" )
  }

  test("format(1: Double)"){
    val f = new ComplexFormat
    val d: Double = 1
    val s = f.format(d)
    assert( s === "1" )
  }

  test("format(16: Double)"){
    val f = new ComplexFormat
    val d: Double = 16
    assert( f.format(d) === "16" )
  }

  test("format(1000: Double)"){
    val f = new ComplexFormat
    val d: Double = 1000
    assert( f.format(d) === "1000" )
  }

  test("format(1000000: Double)"){
    val f = new ComplexFormat
    val d: Double = 10000000
    val s = f.format(d)
    assert( s === "10\\7" )
  }

  test("format(9999999: Double)"){
    val f = new ComplexFormat
    val d: Double = 9999999
    assert( f.format(d) === "9999999" )
  }

  test("format(Long.MaxValue: Double)"){
    val f = new ComplexFormat
    val d: Double = Long.MaxValue
    val s = f.format(d)
    assert( s === "9223372036854775807" )
  }

  test("format(Long.MinValue: Double)"){
    val f = new ComplexFormat
    val d: Double = Long.MinValue
    val s = f.format(d)
    assert( s === "-9223372036854775808" )
  }

  test("format(NaN: double)"){
    val f = new ComplexFormat
    val d: Double = Double.NaN
    val s = f.format(d)
    assert( s === "NaN" )
  }

  test("format(Double.PositiveInfinity)"){
    val f = new ComplexFormat
    val d: Double = Double.PositiveInfinity
    val s = f.format(d)
    assert( s === "∞" )
  }

  test("format(Double.NegativeInfinity)"){
    val f = new ComplexFormat
    val d: Double = Double.NegativeInfinity
    val s = f.format(d)
    assert( s === "∞" )
  }

  test("format(e: double)"){
    val f = new ComplexFormat
    val d: Double = e
    val s = f.format(d)
    assert( s === "e" )
  }

  test("format(-e: double)"){
    val f = new ComplexFormat
    val d: Double = -e
    val s = f.format(d)
    assert( s === "-e" )
  }

  test("format(π: double)"){
    val f = new ComplexFormat
    val d: Double = π
    val s = f.format(d)
    assert( s === "π" )
  }

  test("format(-π: double)"){
    val f = new ComplexFormat
    val d: Double = -π
    val s = f.format(d)
    assert( s === "-π" )
  }

  test("format(0.001: double)"){
    val f = new ComplexFormat
    f.maximumFractionDigits = 3
    val d: Double = 0.001
    val s = f.format(d)
    assert( s === "0.001" )
  }

  test("format(-0.001: double)"){
    val f = new ComplexFormat
    f.maximumFractionDigits = 3
    val d: Double = -0.001
    val s = f.format(d)
    assert( s === "-0.001" )
  }

  test("format(-1655.0: double)"){
    val f = new ComplexFormat
    f.maximumFractionDigits = 3
    val d: Double = -1655.0
    val s = f.format(d)
    assert( s === "-1655" )
  }

  test("format(7856.05: double)"){
    val f = new ComplexFormat
    f.maximumFractionDigits = 3
    val d: Double = 7856.05
    val s = f.format(d)
    assert( s === "7856.05" )
  }

  test("format(0.0003: double)"){
    val f = new ComplexFormat
    f.maximumFractionDigits = 3
    val d: Double = 0.0003
    val s = f.format(d)
    assert( s === "3 * 10\\-4" )
  }

  test("format(0.00034: double)"){
    val f = new ComplexFormat
    f.maximumFractionDigits = 3
    val d: Double = 0.00034
    val s = f.format(d)
    assert( s === "3.4 * 10\\-4" )
  }

  test("format(0.0001: double)"){
    val f = new ComplexFormat
    f.maximumFractionDigits = 3
    val d: Double = 0.0001
    val s = f.format(d)
    assert( s === "10\\-4" )
  }

  test("format(-0.0003: double)"){
    val f = new ComplexFormat
    f.maximumFractionDigits = 3
    val d: Double = -0.0003
    val s = f.format(d)
    assert( s === "-3 * 10\\-4" )
  }

  test("format(-0.00034: double)"){
    val f = new ComplexFormat
    f.maximumFractionDigits = 3
    val d: Double = -0.00034
    val s = f.format(d)
    assert( s === "-3.4 * 10\\-4" )
  }

  test("format(-0.0001: double)"){
    val f = new ComplexFormat
    f.maximumFractionDigits = 3
    val d: Double = -0.0001
    val s = f.format(d)
    assert( s === "-10\\-4" )
  }

  test("format(12345678.9: double)"){
    val f = new ComplexFormat
    f.maximumFractionDigits = 3
    val d: Double = 12345678.9
    val s = f.format(d)
    assert( s === "1.234 * 10\\7" )
  }

  test("format(-12345678.9: double)"){
    val f = new ComplexFormat
    f.maximumFractionDigits = 3
    val d: Double = -12345678.9
    val s = f.format(d)
    assert( s === "-1.234 * 10\\7" )
  }

  test("format(100000000: double)"){
    val f = new ComplexFormat
    f.maximumFractionDigits = 3
    val d: Double = 100000000
    val s = f.format(d)
    assert( s === "10\\8" )
  }

  test("format(-100000000: double)"){
    val f = new ComplexFormat
    f.maximumFractionDigits = 3
    val d: Double = -100000000
    val s = f.format(d)
    assert( s === "-10\\8" )
  }

  // ComplexFormat.format(Complex) --------------

  test("format -9999999 : Complex"){
    val f = new ComplexFormat
    val c: Complex = -9999999
    val s = f.format(c)
    assert( s === "-9999999" )
  }

  test("format -1000000 : Complex"){
    val f = new ComplexFormat
    val c: Complex = -1000000
    val s = f.format(c)
    assert( s === "-1000000" )
  }

  test("format -1000 : Complex"){
    val f = new ComplexFormat
    val c: Complex = -1000
    val s = f.format(c)
    assert( s === "-1000" )
  }

  test("format -3 : Complex"){
    val f = new ComplexFormat
    val c: Complex = -3
    val s = f.format(c)
    assert( s === "-3" )
  }

  test("format -1 : Complex"){
    val f = new ComplexFormat
    val c: Complex = -1
    val s = f.format(c)
    assert( s === "-1" )
  }

  test("format -0 : Complex"){
    val f = new ComplexFormat
    val c: Complex = -0.0
    val s = f.format(c)
    assert( s === "0" )
  }

  test("format 0 : Complex"){
    val f = new ComplexFormat
    val c: Complex = 0
    val s = f.format(c)
    assert( s === "0" )
  }

  test("format 1 : Complex"){
    val f = new ComplexFormat
    val c: Complex = 1
    val s = f.format(c)
    assert( s === "1" )
  }

  test("format 16 : Complex"){
    val f = new ComplexFormat
    val c: Complex = 16
    val s = f.format(c)
    assert( s === "16" )
  }

  test("format 1000 : Complex"){
    val f = new ComplexFormat
    val c: Complex = 1000
    val s = f.format(c)
    assert( s === "1000" )
  }

  test("format 1000000 : Complex"){
    val f = new ComplexFormat
    val c: Complex = 1000000
    val s = f.format(c)
    assert( s === "1000000" )
  }

  test("format 9999999 : Complex"){
    val f = new ComplexFormat
    val c: Complex = 9999999
    val s = f.format(c)
    assert( s === "9999999" )
  }

  test("format NaN : Complex"){
    val f = new ComplexFormat
    val c: Complex = Double.NaN
    val s = f.format(c)
    assert( s === "NaN" )
  }

  test("format Double.PositiveInfinity : Complex"){
    val f = new ComplexFormat
    val c: Complex = Double.PositiveInfinity
    val s = f.format(c)
    assert( s === "∞" )
  }

  test("format Double.NegativeInfinity : Complex"){
    val f = new ComplexFormat
    val c: Complex = Double.NegativeInfinity
    val s = f.format(c)
    assert( s === "∞" )
  }

  test("format e : Complex"){
    val f = new ComplexFormat
    val c: Complex = e
    val s = f.format(c)
    assert( s === "e" )
  }

  test("format -e : Complex"){
    val f = new ComplexFormat
    val c: Complex = -e
    val s = f.format(c)
    assert( s === "-e" )
  }

  test("format π : Complex"){
    val f = new ComplexFormat
    val c: Complex = π
    val s = f.format(c)
    assert( s === "π" )
  }

  test("format -π : Complex"){
    val f = new ComplexFormat
    val c: Complex = -π
    val s = f.format(c)
    assert( s === "-π" )
  }

  test("format 0.001 : Complex"){
    val f = new ComplexFormat
    val c: Complex = 0.001
    val s = f.format(c)
    assert( s === "0.001" )
  }

  test("format -0.001 : Complex"){
    val f = new ComplexFormat
    val c: Complex = -0.001
    val s = f.format(c)
    assert( s === "-0.001" )
  }

  test("format -1655.0 : Complex"){
    val f = new ComplexFormat
    val c: Complex = -1655.0
    val s = f.format(c)
    assert( s === "-1655" )
  }

  test("format 7856.05 : Complex"){
    val f = new ComplexFormat
    val c: Complex = 7856.05
    val s = f.format(c)
    assert( s === "7856.05" )
  }

  test("format 0.0003 : Complex"){
    val f = new ComplexFormat
    val c: Complex = 0.0003
    val s = f.format(c)
    assert( s === "3 * 10\\-4" )
  }

  test("format 0.00034 : Complex"){
    val f = new ComplexFormat
    val c: Complex = 0.00034
    val s = f.format(c)
    assert( s === "3.4 * 10\\-4" )
  }

  test("format 0.0001 : Complex"){
    val f = new ComplexFormat
    val c: Complex = 0.0001
    val s = f.format(c)
    assert( s === "10\\-4" )
  }

  test("format -0.0003 : Complex"){
    val f = new ComplexFormat
    val c: Complex = -0.0003
    val s = f.format(c)
    assert( s === "-3 * 10\\-4" )
  }

  test("format -0.00034 : Complex"){
    val f = new ComplexFormat
    val c: Complex = -0.00034
    val s = f.format(c)
    assert( s === "-3.4 * 10\\-4" )
  }

  test("format -0.0001 : Complex"){
    val f = new ComplexFormat
    val c: Complex = -0.0001
    val s = f.format(c)
    assert( s === "-10\\-4" )
  }

  test("format 12345678.9 : Complex"){
    val f = new ComplexFormat
    val c: Complex = 12345678.9
    val s = f.format(c)
    assert( s === "1.234 * 10\\7" )
  }

  test("format -12345678.9 : Complex"){
    val f = new ComplexFormat
    val c: Complex = -12345678.9
    val s = f.format(c)
    assert( s === "-1.234 * 10\\7" )
  }

  test("format 100000000 : Complex"){
    val f = new ComplexFormat
    val c: Complex = 100000000
    val s = f.format(c)
    assert( s === "10\\8" )
  }

  test("format -100000000 : Complex"){
    val f = new ComplexFormat
    val c: Complex = -100000000
    val s = f.format(c)
    assert( s === "-10\\8" )
  }

  // imaginary -------------------------------

  test("format NaN i : Complex"){
    val f = new ComplexFormat
    val c: Complex = i * Double.NaN
    val s = f.format(c)
    assert( s === "NaN" )
  }

  test("format PositiveInfinity i : Complex"){
    val f = new ComplexFormat
    val c: Complex = i * Double.PositiveInfinity
    val s = f.format(c)
    assert( s === "∞" )
  }

  test("format NegativeInfinity i : Complex"){
    val f = new ComplexFormat
    val c: Complex = i * Double.NegativeInfinity
    val s = f.format(c)
    assert( s === "∞" )
  }

  test("format 0 i : Complex"){
    val f = new ComplexFormat
    val c: Complex = i * -0.0
    val s = f.format(c)
    assert( s === "0" )
  }

  test("format ei : Complex"){
    val f = new ComplexFormat
    val c: Complex = i * e
    val s = f.format(c)
    assert( s === "ei" )
  }

  test("format -ei : Complex"){
    val f = new ComplexFormat
    val c: Complex = i * -e
    val s = f.format(c)
    assert( s === "-ei" )
  }

  test("format πi : Complex"){
    val f = new ComplexFormat
    val c: Complex = i * π
    val s = f.format(c)
    assert( s === "πi" )
  }

  test("format -πi : Complex"){
    val f = new ComplexFormat
    val c: Complex = i * -π
    val s = f.format(c)
    assert( s === "-πi" )
  }

  test("format -1655.0i : Complex"){
    val f = new ComplexFormat
    val c: Complex = -1655.0 * i
    val s = f.format(c)
    assert( s === "-1655.0i" )
  }

  test("format 7856.05i : Complex"){
    val f = new ComplexFormat
    val c: Complex = 7856.05 * i
    val s = f.format(c)
    assert( s === "7856.05i" )
  }

  test("format 0.0003i : Complex"){
    val f = new ComplexFormat
    val c: Complex = 0.0003 * i
    val s = f.format(c)
    assert( s === "3i * 10\\-4" )
  }

  test("format 0.00034i : Complex"){
    val f = new ComplexFormat
    val c: Complex = 0.00034 * i
    val s = f.format(c)
    assert( s === "3.4i * 10\\-4" )
  }

  test("format 0.0001i : Complex"){
    val f = new ComplexFormat
    val c: Complex = 0.0001 * i
    val s = f.format(c)
    assert( s === "i * 10\\-4" )
  }

  test("format -0.0003i : Complex"){
    val f = new ComplexFormat
    val c: Complex = -0.0003 * i
    val s = f.format(c)
    assert( s === "-3i * 10\\-4" )
  }

  test("format -0.00034i : Complex"){
    val f = new ComplexFormat
    val c: Complex = -0.00034 * i
    val s = f.format(c)
    assert( s === "-3.4i * 10\\-4" )
  }

  test("format -0.0001i : Complex"){
    val f = new ComplexFormat
    val c: Complex = -0.0001 * i
    val s = f.format(c)
    assert( s === "-i * 10\\-4" )
  }

  test("format 12345678.9i : Complex"){
    val f = new ComplexFormat
    val c: Complex = 12345678.9 * i
    val s = f.format(c)
    assert( s === "1.234i * 10\\7" )
  }

  test("format -12345678.9i : Complex"){
    val f = new ComplexFormat
    val c: Complex = -12345678.9 * i
    val s = f.format(c)
    assert( s === "-1.234i * 10\\7" )
  }

  test("format 100000000i : Complex"){
    val f = new ComplexFormat
    val c: Complex = 100000000 * i
    val s = f.format(c)
    assert( s === "i * 10\\8" )
  }

  test("format -100000000i : Complex"){
    val f = new ComplexFormat
    val c: Complex = -100000000 * i
    val s = f.format(c)
    assert( s === "i * -10\\8" )
  }

  // complex number -------------------------

  test("format 1+i : Complex"){
    val f = new ComplexFormat
    val c: Complex = 1+i
    val s = f.format(c)
    assert( s === "1+i" )
  }

  test("format -1+i : Complex"){
    val f = new ComplexFormat
    val c: Complex = -1+i
    val s = f.format(c)
    assert( s === "-1+i" )
  }

  test("format 1-i : Complex"){
    val f = new ComplexFormat
    val c: Complex = 1-i
    val s = f.format(c)
    assert( s === "1-i" )
  }

  test("format 123456 +123456i : Complex"){
    val f = new ComplexFormat
    val c: Complex = 123456 +123456*i
    val s = f.format(c)
    assert( s === "123456 +123456i" )
  }

  test("format 1.234 * 10\\7 +1.234i * 10\\7 : Complex"){
    val f = new ComplexFormat
    val c: Complex = 1.234 * 10\7 +1.234*i * 10\7
    val s = f.format(c)
    assert( s === "1.234 * 10\\7 +1.234i * 10\\7" )
  }

  test("format -1.234 * 10\\-4 -1.234i * 10\\-4 : Complex"){
    val f = new ComplexFormat
    val c: Complex = -1.234 * 10\-4 -1.234*i * 10\-4
    val s = f.format(c)
    assert( s === "-1.234 * 10\\-4 -1.234i * 10\\-4" )
  }

  // max fraction digits -----------------------

  test("format 1/3 : Double"){
    val f = new ComplexFormat
    val c: Double = 1.0/3.0
    val s = f.format( c )
    assert( s === "0.3333333333333333" )
  }

  test("format 1.0/3.0 : Double maxfraction 3"){
    val f = new ComplexFormat
    f.maximumFractionDigits = 3
    val c: Double = 1.0/3.0
    val s = f.format( c )
    assert( s === "0.333" )
  }

  test("format 1/3 : Complex"){
    val f = new ComplexFormat
    val c: Complex = 1.0/3.0
    val s = f.format( c )
    assert( s === "0.3333333333333333" )
  }

  test("format 1.0/3.0 : Complex maxfraction 3"){
    val f = new ComplexFormat
    f.maximumFractionDigits = 3
    val c: Complex = 1.0/3.0
    val s = f.format( c )
    assert( s === "0.333" )
  }

  test("format 1/3 i"){
    val f = new ComplexFormat
    val c = i/3
    val s = f.format( c )
    assert( s === "0.3333333333333333i" )
  }

  test("format 1/3 i maxfraction=3"){
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

  test("format 1.0: Double minfraction 3"){
    val f = new ComplexFormat
    f.minimumFractionDigits = 3
    val c: Double = 1.0
    val s = f.format( c )
    assert( s === "1.000" )
  }

  test("format 1.0: Complex minfraction 3"){
    val f = new ComplexFormat
    f.minimumFractionDigits = 3
    val c: Complex = 1.0
    val s = f.format( c )
    assert( s === "1.000" )
  }

}
