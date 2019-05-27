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

  // Complex extends Number -------------------------------------------

  test("byteValue"){
    val n: Number = 1.9 + i*9.2
    val b = n.byteValue()
    val one: Byte = 1
    assert( b === one )
  }

  test("shortValue"){
    val n: Number = 1.9 + i*9.2
    val s = n.shortValue()
    val one: Short = 1
    assert( s === one )
  }

  test("intValue"){
    val n: Number = 1.9 + i*9.2
    val s = n.intValue()
    val one: Int = 1
    assert( s === one )
  }

  test("longValue"){
    val n: Number = 1.9 + i*9.2
    val s = n.longValue()
    val one: Long = 1L
    assert( s === one )
  }

  test("floatValue"){
    val n: Number = 1.9 + i*9.2
    val f = n.floatValue()
    assert( f === 1.9f )
  }

  test("doubleValue"){
    val n: Number = 1.9 + i*9.2
    val d = n.doubleValue()
    assert( d === 1.9d )
  }

  test("∞.byteValue"){
    val b = ∞.byteValue()
    val one: Byte = -1
    assert( b === one )
  }

  test("∞.shortValue"){
    val s = ∞.shortValue()
    val one: Short = -1
    assert( s === one )
  }

  test("∞.intValue"){
    val s = ∞.intValue()
    assert( s === Int.MaxValue )
  }

  test("∞.longValue"){
    val s = ∞.longValue()
    assert( s === Long.MaxValue )
  }

  test("∞.floatValue"){
    val f = ∞.floatValue()
    assert( f === Float.PositiveInfinity )
  }

  test("∞.doubleValue"){
    val d = ∞.doubleValue()
    assert( d === Double.PositiveInfinity )
  }

  // parsing -------------------------------------------

  test("parse i+5"){
    val f = new ComplexFormat
    val c = f.parse("i + 5")
    assert( c === i+5 )
  }

  test("parse i+5 p"){
    val f = new ComplexFormat
    val p = new java.text.ParsePosition(0)
    val c = f.parse("i + 5", p)
    assert( c === i+5 )
  }

  // formatting integers ========================

  test("0.toString"){
    val c: Complex = 0
    assert( c.toString === "0" )
  }

  test("-0 toString"){
    val c: Complex = -0
    assert( c.toString === "0" )
  }

  test("∞ toString"){
    val c: Complex = ∞
    assert( c.toString === "∞" )
  }

  test("-9999999 toString") {
    val c: Complex = -9999999
    assert( c.toString === "-9999999" )
  }

  test("-1000000 toString") {
    val c: Complex = -1000000
    assert( c.toString === "-1000000" )
  }

  test("-1000 toString") {
    val c: Complex = -1000
    assert( c.toString === "-1000" )
  }

  test("-1 toString") {
    val c: Complex = -1
    assert( c.toString === "-1" )
  }

  test("1 toString") {
    val c: Complex = 1
    assert( c.toString === "1" )
  }

  test("1000 toString") {
    val c: Complex = 1000
    assert( c.toString === "1000" )
  }

  test("1000000 toString") {
    val c: Complex = 1000000
    assert( c.toString === "1000000" )
  }

  test("9999999 toString") {
    val c: Complex = 9999999
    assert( c.toString === "9999999" )
  }

  // formatting reals ========================

  test("0.0 toString"){
    val c: Complex = 0.0
    assert( c.toString === "0" )
  }

  test("e toString"){
    val c: Complex = e
    assert( c.toString === "e" )
  }

  test("-e toString"){
    val c: Complex = -e
    assert( c.toString === "-e" )
  }

  test("π toString"){
    val c: Complex = π
    assert( c.toString === "π" )
  }

  test("-π toString"){
    val c: Complex = -π
    assert( c.toString === "-π" )
  }

  // 10⁻³ <= m < 10⁷ => 1655.0 or 7856.05     //ok as it, maybe cut trailing 0 and .
  // 0.001 <= m < 10000000

  //minimal magnitude
  test("0.001 toString"){
    val c: Complex = 0.001
    assert( c.toString === "0.001" )
  }

  //minimal magnitude
  test("-0.001 toString"){
    val c: Complex = -0.001
    assert( c.toString === "-0.001" )
  }

  test("1655.03 toString"){
    val c: Complex = 1655.03
    assert( c.toString === "1655.03" )
  }

  test("-7856.05 toString"){
    val c: Complex = -7856.05
    assert( c.toString === "-7856.05" )
  }

  //maximal magnitude
  test("9999999.9 toString"){
    val c: Complex = 9999999.9
    assert( c.toString === "9999999.9" )
  }

  //maximal magnitude
  test("-9999999.9 toString"){
    val c: Complex = -9999999.9
    assert( c.toString === "-9999999.9" )
  }

  // m < 10⁻³ or 10⁷ < m => "computerized scientific notation"

  //minimal magnitude
  test("0.0009 toString"){
    val c: Complex = 0.0009
    assert( c.toString === "9.0 * 10^-4" )
  }

  //minimal magnitude
  test("-0.0009 toString"){
    val c: Complex = -0.0009
    assert( c.toString === "-9.0 * 10^-4" )
  }

  test("-3.153 * 10^-8 toString"){
    val c: Complex = -3.153 * 10^(-8)
    assert( c.toString === "-3.153 * 10^-8" )
  }

  //maximal magnitude
  test("10000000 toString"){
    val c: Complex = 10000000
    assert( c.toString === "1.0 * 10^8" )
  }

  //maximal magnitude
  test("-10000000 toString"){
    val c: Complex = -10000000
    assert( c.toString === "-1.0 ^ 10^8" )
  }

  // imaginary toString ---------------------------------------------

  test("0.0i toString"){
    val c: Complex = 0.0*i
    assert( c.toString === "0" )
  }

  test("ei toString"){
    val c: Complex = e*i
    assert( c.toString === "ei" )
  }

  test("-ei toString"){
    val c: Complex = -e*i
    assert( c.toString === "-ei" )
  }

  test("πi toString"){
    val c: Complex = π * i
    assert( c.toString === "πi" )
  }

  test("-πi toString"){
    val c: Complex = -π*i
    assert( c.toString === "-πi" )
  }

  // 10⁻³ <= m < 10⁷ => 1655.0 or 7856.05     //ok as it, maybe cut trailing 0 and .

  //minimal magnitude
  test("0.001i toString"){
    val c: Complex = 0.001*i
    assert( c.toString === "0.001i" )
  }

  //minimal magnitude
  test("-0.001i toString"){
    val c: Complex = -0.001*i
    assert( c.toString === "-0.001i" )
  }

  test("1655.03i toString"){
    val c: Complex = 1655.03*i
    assert( c.toString === "1655.03i" )
  }

  test("-7856.05i toString"){
    val c: Complex = -7856.05 * i
    assert( c.toString === "-7856.05i" )
  }

  //maximal magnitude
  test("9999999.9i toString"){
    val c: Complex = 9999999.9 * i
    assert( c.toString === "9999999.9i" )
  }

  //maximal magnitude
  test("-9999999.9i toString"){
    val c: Complex = -9999999.9 * i
    assert( c.toString === "-9999999.9i" )
  }

  // m < 10⁻³ or 10⁷ < m => "computerized scientific notation"

  //minimal magnitude
  test("0.0009i toString"){
    val c: Complex = 0.0009 * i
    assert( c.toString === "9.0i * 10^-4" )
  }

  //minimal magnitude
  test("-0.0009i toString"){
    val c: Complex = -0.0009 * i
    assert( c.toString === "-9.0i * 10^-4" )  //drop trailing 0 ?
  }

  test("-3.153 * 10^-8  * i toString"){
    val c: Complex = -3.153 * 10^(-8) * i
    assert( c.toString === "-3.153i * 10^-8" )
  }

  //maximal magnitude
  test("10000000i toString"){
    val c: Complex = 10000000 * i
    assert( c.toString === "1.0i * 10^8" )  //drop 1.0 ?
  }

  //maximal magnitude
  test("-10000000i toString"){
    val c: Complex = -10000000 * i
    assert( c.toString === "-1.0i ^ 10^8" ) //drop 1.0 ?
  }

  // complex ---------------------------------------------------

  test("13 + 5i toString"){
    val c: Complex = 13 + 5*i
    assert( c.toString === "13 + 5i" )
  }

  test("13.3 + 5.7i toString"){
    val c: Complex = 13.3 + 5.7*i
    assert( c.toString === "13.3 + 5.7i" )
  }

  test("-13.3 - 5.7i toString"){
    val c: Complex = -13.3 - 5.7*i
    assert( c.toString === "-13.3 - 5.7i" )
  }

  test("-0.0002 - 5.7i toString"){
    val c: Complex = -0.0002 - 5.7*i
    assert( c.toString === "-2.0 * 10^-4 - 5.7i" )
  }

  test("-13.3 - 0.0003i toString"){
    val c: Complex = -13.3 - 0.0003*i
    assert( c.toString === "-13.3 - 3.0i * 10^-4" )
  }

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
    val c: Complex = 3.2 * e^(0*π*i)
    val s = f"${c}%#s"
    assert( s === "3.2" )
  }

  /** positive real */
  test("3.2 e^2πi polar"){
    val c: Complex = 3.2 * e^(2*π*i)
    val s = f"${c}%#s"
    assert( s === "3.2" )
  }

  /** negative real */
  test("3.21 polar"){
    val c: Complex = 3.21 * e^(1*π*i)
    val s = f"${c}%#s"
    assert( s === "3.21 e^πi" )
  }

  test("3.21 e^0.75πi polar"){
    val c: Complex = 3.21 * e^(0.75*π*i)
    val s = f"${c}%#s"
    assert( s === "3.21 e^0.75πi" )
  }

  test("3.21 e^-0.75πi polar"){
    val c: Complex = 3.21 * e^(-0.75*π*i)
    val s = f"${c}%#s"
    assert( s === "3.21 e^-0.75πi" )
  }

  // tests for ComplexFormat ===================================================
  
}
