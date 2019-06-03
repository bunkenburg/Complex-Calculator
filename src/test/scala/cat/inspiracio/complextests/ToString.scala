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
package cat.inspiracio.complextests

import org.scalatest.FunSuite

import cat.inspiracio.complex._

/** Tests Complex.toString */
class ToString extends FunSuite {

  // small integers -----------------------

  test("-9999999") {
    val c: Complex = -9999999
    assert(c.toString === "-9999999")
  }

  test("-1000000") {
    val c: Complex = -1000000
    assert(c.toString === "-1000000")
  }

  test("-1000") {
    val c: Complex = -1000
    assert(c.toString === "-1000")
  }

  test("-3") {
    val c: Complex = -3
    assert(c.toString === "-3")
  }

  test("-1") {
    val c: Complex = -1
    assert(c.toString === "-1")
  }

  test("-0") {
    val c: Complex = -0
    assert(c.toString === "0")
  }

  test("0") {
    val c: Complex = 0
    assert(c.toString === "0")
  }

  test("1") {
    val c: Complex = 1
    assert(c.toString === "1")
  }

  test("16") {
    val c: Complex = 16
    assert(c.toString === "16")
  }

  test("1000") {
    val c: Complex = 1000
    assert(c.toString === "1000")
  }

  test("1000000") {
    val c: Complex = 1000000
    assert(c.toString === "1000000")
  }

  test("9999999") {
    val c: Complex = 9999999
    assert(c.toString === "9999999")
  }

  // real -> special --------------------

  test("NaN") {
    val c: Complex = Double.NaN
    assert(c.toString === "NaN")
  }

  test("0.0") {
    val c = 0.0
    assert(c.toString === "0")
  }

  test("-0.0") {
    val c = -0.0
    assert(c.toString === "0")
  }

  test("Double.PositiveInfinity") {
    val c = Double.PositiveInfinity
    assert(c.toString === "∞")
  }

  test("Double.NegativeInfinity") {
    val c = Double.NegativeInfinity
    assert(c.toString === "∞")
  }

  test("Float.PositiveInfinity") {
    val c = Float.PositiveInfinity
    assert(c.toString === "∞")
  }

  test("Float.NegativeInfinity") {
    val c = Float.NegativeInfinity
    assert(c.toString === "∞")
  }

  test("∞") {
    val c = ∞
    assert(c.toString === "∞")
  }

  test("e") {
    val c: Complex = e
    assert(c.toString === "e")
  }

  test("-e") {
    val c: Complex = -e
    assert(c.toString === "-e")
  }

  test("π") {
    val c: Complex = π
    assert(c.toString === "π")
  }

  test("-π") {
    val c: Complex = -π
    assert(c.toString === "-π")
  }

  // real -> decimal representation

  test("-1655.0") {
    val c: Complex = -1655.0
    assert(c.toString === "-1655")
  }

  test("7856.05") {
    val c: Complex = 7856.05
    assert(c.toString === "7856.05")
  }

  test("0.001") {
    val c: Complex = 0.001
    assert(c.toString === "0.001")
  }

  test("-0.001") {
    val c: Complex = -0.001
    assert(c.toString === "-0.001")
  }

  // real -> scientific notation

  test("0.0003") {
    val c: Complex = 0.0003
    assert(c.toString === "3 * 10^-4")
  }




  test("toString 3+i") {
    val c: Complex = 3+i
    assert(c.toString === "3+i")
  }

  test("toString ∞") {
    val c: Complex = ∞
    assert(c.toString === "∞")
  }

  test("toString(1+i)") {
    val c: Complex = i+1
    assert(c.toString === "1+i")
  }

  test("toString(1-i)") {
    val c: Complex = -i+1
    assert(c.toString === "1-i")
  }

  test("0.toString"){
    val c: Complex = 0
    assert( c.toString === "0" )
  }

  test("-0 toString"){
    val c: Complex = -0
    assert( c.toString === "0" )
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

  test("13 toString"){
    val c: Complex = 13
    val s = c.toString
    assert( s === "13" )
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
    val c: Complex = -3.153 * 10 \ -8
    val s = c.toString
    assert( s === "-3.153 * 10^-8" )
  }

  //maximal magnitude
  test("10000000 toString"){
    val c: Complex = 10000000
    assert( c.toString === "1.0 * 10^7" )
  }

  //maximal magnitude
  test("-10000000 toString"){
    val c: Complex = -10000000
    assert( c.toString === "-1.0 * 10^7" )
  }

  // imaginary toString ---------------------------------------------

  test("0.0i toString"){
    val c: Complex = 0.0*i
    assert( c.toString === "0" )
  }

  test("i toString"){
    val c = i
    assert( c.toString === "i" )
  }

  test("-i toString"){
    val c: Complex = -i
    assert( c.toString === "-i" )
  }

  test("ei toString"){
    val c = i*e
    assert( c.toString === "ei" )
  }

  test("-ei toString"){
    val c: Complex = -i*e
    assert( c.toString === "-ei" )
  }

  test("πi toString"){
    val c: Complex = i*π
    assert( c.toString === "πi" )
  }

  test("-πi toString"){
    val c: Complex = -i*π
    assert( c.toString === "-πi" )
  }

  test("5i toString"){
    val c = i * 5
    val s = c.toString
    assert( s === "5i" )
  }

  // 10⁻³ <= m < 10⁷ => 1655.0 or 7856.05     //ok as it, maybe cut trailing 0 and .

  //minimal magnitude
  test("0.001i toString"){
    val c: Complex = i * 0.001
    assert( c.toString === "0.001i" )
  }

  //minimal magnitude
  test("-0.001i toString"){
    val c: Complex = -0.001 * i
    assert( c.toString === "-0.001i" )
  }

  test("1655.03i toString"){
    val c: Complex = 1655.03 * i
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
    val c: Complex = -3.153 * 10\(-8) * i
    assert( c.toString === "-3.153i * 10^-8" )
  }

  //maximal magnitude
  test("10000000i toString"){
    val c: Complex = 10000000 * i
    assert( c.toString === "1.0i * 10^7" )  //drop 1.0 ?
  }

  //maximal magnitude
  test("-10000000i toString"){
    val c: Complex = -10000000 * i
    assert( c.toString === "-1.0i * 10^7" ) //drop 1.0 ?
  }

  // complex ---------------------------------------------------

  test("13 + 5i toString"){
    val c = i*5 + 13
    val s = c.toString
    assert( s === "13+5i" )
  }

  test("13.3 + 5.7i toString"){
    val c: Complex = 13.3 + 5.7*i
    assert( c.toString === "13.3 + 5.7i" )
  }

  test("-13.3 - 5.7i toString"){
    val c: Complex = -i*5.7 -13.3
    assert( c.toString === "-13.3 -5.7i" )
  }

  test("-0.0002 -5.7i toString"){
    val c: Complex = -0.0002 - 5.7*i
    assert( c.toString === "-2.0 * 10^-4 -5.7i" )
  }

  test("-13.3 - 0.0003i toString"){
    val c: Complex = -13.3 - 0.0003*i
    assert( c.toString === "-13.3 -3.0i * 10^-4" )
  }

}