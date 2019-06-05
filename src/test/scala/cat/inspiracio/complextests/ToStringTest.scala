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
class ToStringTest extends FunSuite {

  // small integers -----------------------

  test("c -9999999") {
    val c: Complex = -9999999
    assert(c.toString === "-9999999")
  }
  test("l -9999999") {
    val c: Long = -9999999
    assert(c.toString === "-9999999")
  }

  test("-9999999") {
    val c = -9999999  //int
    assert(c.toString === "-9999999")
  }
  test("c -1000000") {
    val c: Complex = -1000000
    assert(c.toString === "-1000000")
  }

  test("c -1000") {
    val c: Complex = -1000
    assert(c.toString === "-1000")
  }

  test("c -3") {
    val c: Complex = -3
    assert(c.toString === "-3")
  }

  test("-1") {
    val c = -1
    assert(c.toString === "-1")
  }
  test("c -1") {
    val c: Complex = -1
    assert(c.toString === "-1")
  }

  test("-0") {
    val c = -0
    assert(c.toString === "0")
  }
  test("c -0") {
    val c: Complex = -0
    assert(c.toString === "0")
  }

  test("0") {
    val c = 0
    assert(c.toString === "0")
  }
  test("c 0") {
    val c: Complex = 0
    assert(c.toString === "0")
  }

  test("1") {
    val c = 1
    assert(c.toString === "1")
  }
  test("c 1") {
    val c = 1
    assert(c.toString === "1")
  }

  test("16") {
    val c = 16
    assert(c.toString === "16")
  }
  test("c 16") {
    val c: Complex = 16
    assert(c.toString === "16")
  }

  test("1000") {
    val c = 1000
    assert(c.toString === "1000")
  }
  test("c 1000") {
    val c: Complex = 1000
    assert(c.toString === "1000")
  }

  test("1000000") {
    val c = 1000000
    assert(c.toString === "1000000")
  }
  test("c 1000000") {
    val c: Complex = 1000000
    assert(c.toString === "1000000")
  }

  test("9999999") {
    val c = 9999999
    assert(c.toString === "9999999")
  }
  test("c 9999999") {
    val c: Complex = 9999999
    assert(c.toString === "9999999")
  }

  // real -> special --------------------

  test("NaN") {
    val c = Double.NaN
    assert(c.toString === "NaN")
  }
  test("c NaN") {
    val c: Complex = Double.NaN
    assert(c.toString === "NaN")
  }

  test("c 0.0") {
    val c: Complex = 0.0
    assert(c.toString === "0")
  }

  test("c -0.0") {
    val c: Complex = -0.0
    assert(c.toString === "0")
  }

  test("c Double.PositiveInfinity") {
    val c: Complex = Double.PositiveInfinity
    assert(c.toString === "∞")
  }

  test("c Double.NegativeInfinity") {
    val c: Complex = Double.NegativeInfinity
    assert(c.toString === "∞")
  }

  test("c Float.PositiveInfinity") {
    val c: Complex = Float.PositiveInfinity
    assert(c.toString === "∞")
  }

  test("c Float.NegativeInfinity") {
    val c: Complex = Float.NegativeInfinity
    assert(c.toString === "∞")
  }

  test("∞") {
    val c = ∞
    assert(c.toString === "∞")
  }

  test("c e") {
    val c: Complex = e
    assert(c.toString === "e")
  }

  test("c -e") {
    val c: Complex = -e
    assert(c.toString === "-e")
  }

  test("c π") {
    val c: Complex = π
    assert(c.toString === "π")
  }

  test("c -π") {
    val c: Complex = -π
    assert(c.toString === "-π")
  }

  // real -> decimal representation

  test("c -1655.0") {
    val c: Complex = -1655.0
    assert(c.toString === "-1655")
  }

  test("7856.05") {
    val c = 7856.05
    assert(c.toString === "7856.05")
  }
  test("c 7856.05") {
    val c: Complex = 7856.05
    assert(c.toString === "7856.05")
  }

  test("0.001") {
    val c = 0.001
    assert(c.toString === "0.001")
  }
  test("c 0.001") {
    val c: Complex = 0.001
    assert(c.toString === "0.001")
  }

  test("-0.001") {
    val c = -0.001
    assert(c.toString === "-0.001")
  }
  test("c -0.001") {
    val c: Complex = -0.001
    assert(c.toString === "-0.001")
  }

  // real -> scientific notation

  test("c 0.0003") {
    val c: Complex = 0.0003
    assert(c.toString === "3 * 10\\-4")
  }

  test("c 0.00034") {
    val c: Complex = 0.00034
    assert(c.toString === "3.4 * 10\\-4")
  }

  test("c 0.0001") {
    val c: Complex = 0.0001
    assert(c.toString === "10\\-4")
  }

  test("c -0.0003") {
    val c: Complex = -0.0003
    assert(c.toString === "-3 * 10\\-4")
  }

  test("c -0.00034") {
    val c: Complex = -0.00034
    assert(c.toString === "-3.4 * 10\\-4")
  }

  test("c -0.0001") {
    val c: Complex = -0.0001
    assert(c.toString === "-10\\-4")
  }

  test("c 12345678.9") {
    val c: Complex = 12345678.9
    assert(c.toString === "1.23456789 * 10\\7")
  }

  test("c -12345678.9") {
    val c: Complex = -12345678.9
    assert(c.toString === "-1.23456789 * 10\\7")
  }

  test("c -100000000") {
    val c: Complex = -100000000
    assert(c.toString === "-10\\8")
  }

  // imaginary numbers

  test("NaN i") {
    val c = Double.NaN * i
    assert(c.toString === "NaN")
  }

  test("Double.PosivitiveInfinity i") {
    val c = Double.PositiveInfinity * i
    assert(c.toString === "∞")
  }

  test("0i") {
    val c = 0 * i
    assert(c.toString === "0")
  }

  test("ei") {
    val c = i * e
    assert(c.toString === "ei")
  }

  test("-ei") {
    val c = -i * e
    assert(c.toString === "-ei")
  }

  test("πi") {
    val c = i * π
    assert(c.toString === "πi")
  }

  test("-πi") {
    val c = -i * π
    assert(c.toString === "-πi")
  }

  test("-1655.0i") {
    val c = -1655.0 * i
    assert(c.toString === "-1655i")
  }

  test("7856.05i") {
    val c = 7856.05 * i
    assert(c.toString === "7856.05i")
  }

  test("0.0003i") {
    val c = i * 0.0003
    assert(c.toString === "3i * 10\\-4")
  }

  test("0.00034i") {
    val c = i * 0.00034
    assert(c.toString === "3.4i * 10\\-4")
  }

  test("0.0001i") {
    val c = i * 0.0001
    assert(c.toString === "i * 10\\-4")
  }

  test("-0.0003i") {
    val c = i * -0.0003
    assert(c.toString === "-3i * 10\\-4")
  }

  test("-0.00034i") {
    val c = i * -0.00034
    assert(c.toString === "-3.4i * 10\\-4")
  }

  test("-0.0001i") {
    val c = i * -0.0001
    assert(c.toString === "-i * 10\\-4")
  }

  test("12345678.9i") {
    val c = i * 12345678.9
    assert(c.toString === "1.23456789i * 10\\7")
  }

  test("-12345678.9i") {
    val c = i * -12345678.9
    assert(c.toString === "-1.23456789i * 10\\7")
  }

  test("100000000i") {
    val c = i * 100000000
    assert(c.toString === "i * 10\\8")
  }

  test("-100000000i") {
    val c = i * -100000000
    assert(c.toString === "-i * 10\\8")
  }

  // complex numbers

  test("1+i") {
    val c = i+1
    assert(c.toString === "1+i")
  }

  test("-1+i") {
    val c = i-1
    assert(c.toString === "-1+i")
  }

  test("1-i") {
    val c = -i+1
    assert(c.toString === "1-i")
  }

  test("123456 +123456i") {
    val c = 123456 +123456*i
    assert(c.toString === "123456 +123456i")
  }

  test("1.234 * 10\\7 +1.234i * 10\\7"){
    val c = 1.234 * 10\7 +1.234*i * 10\7
    assert( c.toString === "1.234 * 10\\7 +1.234i * 10\\7" )
  }

  test("-1.234 * 10\\-4 -1.234i * 10\\-4"){
    val c = -1.234 * 10\-4 -1.234*i * 10\-4
    assert( c.toString === "-1.234 * 10\\-4 -1.234i * 10\\-4" )
  }

}