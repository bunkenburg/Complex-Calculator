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

class ComplexFormatPolarTest extends FunSuite {

  test("format 0: Long polar"){
    val f = new ComplexFormat
    f.polar = true
    val c: Long = 0
    assert( f.format(c) === "0" )
  }

  test("format 0: Double polar"){
    val f = new ComplexFormat
    f.polar = true
    val c: Double = 0
    assert( f.format(c) === "0" )
  }

  test("format 0: Complex polar"){
    val f = new ComplexFormat
    f.polar = true
    val c: Complex = 0
    assert( f.format(c) === "0" )
  }

  test("format ∞ polar"){
    val f = new ComplexFormat
    f.polar = true
    val c: Complex = ∞
    assert( f.format(c) === "∞" )
  }

  test("format 3: Long polar"){
    val f = new ComplexFormat
    f.polar = true
    val c: Long = 3
    val s = f.format( c )
    assert( s === "3" )
  }

  test("format 3: Double polar"){
    val f = new ComplexFormat
    f.polar = true
    val c: Double = 3
    val s = f.format( c )
    assert( s === "3" )
  }

  test("format 3: Complex polar"){
    val f = new ComplexFormat
    f.polar = true
    val c: Complex = 3
    val s = f.format( c )
    assert( s === "3" )
  }

  test("format 3.2: Double polar"){
    val f = new ComplexFormat
    f.polar = true
    val c: Double = 3.2
    val s = f.format( c )
    assert( s === "3.2" )
  }

  test("format 3.2: Complex polar"){
    val f = new ComplexFormat
    f.polar = true
    val c: Complex = 3.2
    val s = f.format( c )
    assert( s === "3.2" )
  }

  test("format 10\\-4: Double polar"){
    val f = new ComplexFormat
    f.polar = true
    val c: Double = 0.0001
    val s = f.format( c )
    assert( s === "10\\-4" )
  }

  test("format 10\\-4: Complex polar"){
    val f = new ComplexFormat
    f.polar = true
    val c: Complex = 10\-4
    val s = f.format( c )
    assert( s === "10\\-4" )
  }

  test("format 1.23 * 10\\-4: Double polar"){
    val f = new ComplexFormat
    f.polar = true
    val c: Double = 1.23 * 0.0001
    val s = f.format( c )
    assert( s === "1.23 * 10\\-4" )
  }

  test("format 1.23 * 10\\-4: Complex polar"){
    val f = new ComplexFormat
    f.polar = true
    val c: Complex = 1.23 * 10\-4
    val s = f.format( c )
    assert( s === "1.23 * 10\\-4" )
  }

  test("format 1.234 * 10\\8: Double polar"){
    val f = new ComplexFormat
    f.polar = true
    val c: Double = 1.234 * 100000000
    val s = f.format( c )
    assert( s === "1.234 * 10\\8" )
  }

  test("format 1.234 * 10\\8: Complex polar"){
    val f = new ComplexFormat
    f.polar = true
    val c: Complex = 1.234 * 10\8
    val s = f.format( c )
    assert( s === "1.234 * 10\\8" )
  }

  // polar: real negative

  test("format -0.0: Double polar"){
    val f = new ComplexFormat
    f.polar = true
    val c: Double = -0.0
    assert( f.format(c) === "0" )
  }

  test("format -0.0: Complex polar"){
    val f = new ComplexFormat
    f.polar = true
    val c: Complex = -0.0
    assert( f.format(c) === "0" )
  }

  test("format -3: Long polar"){
    val f = new ComplexFormat
    f.polar = true
    val c: Long = -3
    val s = f.format( c )
    assert( s === "-3" )
  }

  test("format -3: Double polar"){
    val f = new ComplexFormat
    f.polar = true
    val c: Double = -3
    val s = f.format( c )
    assert( s === "-3" )
  }

  test("format -3: Complex polar"){
    val f = new ComplexFormat
    f.polar = true
    val c: Complex = -3
    val s = f.format( c )
    assert( s === "-3" )
  }

  test("format -3.2: Double polar"){
    val f = new ComplexFormat
    f.polar = true
    val c: Double = -3.2
    val s = f.format( c )
    assert( s === "-3.2" )
  }

  test("format -3.2: Complex polar"){
    val f = new ComplexFormat
    f.polar = true
    val c: Complex = -3.2
    val s = f.format( c )
    assert( s === "-3.2" )
  }

  test("format -10\\-4: Double polar"){
    val f = new ComplexFormat
    f.polar = true
    val c: Double = -0.0001
    val s = f.format( c )
    assert( s === "-1 * 10\\-4" )
  }

  test("format -10\\-4: Complex polar"){
    val f = new ComplexFormat
    f.polar = true
    val c: Complex = -0.0001
    val s = f.format( c )
    assert( s === "-1 * 10\\-4" )
  }

  test("format -1.23 * 10\\-4: Double polar"){
    val f = new ComplexFormat
    f.polar = true
    val c: Double = -1.23 * 0.0001
    val s = f.format( c )
    assert( s === "-1.23 * 10\\-4" )
  }

  test("format -1.23 * 10\\-4: Complex polar"){
    val f = new ComplexFormat
    f.polar = true
    val c: Complex = -1.23 * 0.0001
    val s = f.format( c )
    assert( s === "-1.23 * 10\\-4" )
  }

  test("format -1.234 * 10\\8: Double polar"){
    val f = new ComplexFormat
    f.polar = true
    val c: Double = -1.234 * 100000000
    val s = f.format( c )
    assert( s === "-1.234 * 10\\8" )
  }

  test("format -1.234 * 10\\8: Complex polar"){
    val f = new ComplexFormat
    f.polar = true
    val c: Complex = -1.234 * 100000000
    val s = f.format( c )
    assert( s === "-1.234 * 10\\8" )
  }

  // imaginary positive

  test("format 3i polar"){
    val f = new ComplexFormat
    f.polar = true
    val c: Complex = i * 3
    val s = f.format( c )
    assert( s === "3i" )
  }

  test("format 3.2i polar"){
    val f = new ComplexFormat
    f.polar = true
    val c: Complex = 3.2 * i
    val s = f.format( c )
    assert( s === "3.2i" )
  }

  test("format 10\\-4 i polar"){
    val f = new ComplexFormat
    f.polar = true
    val c: Complex = 10\-4 * i
    val s = f.format( c )
    assert( s === "i * 10\\-4" )
  }

  test("format 1.23i * 10\\-4 polar"){
    val f = new ComplexFormat
    f.polar = true
    val c: Complex = 1.23*i * 10\-4
    val s = f.format( c )
    assert( s === "1.23i * 10\\-4" )
  }

  test("format 1.234i * 10\\8 polar"){
    val f = new ComplexFormat
    f.polar = true
    val c: Complex = 1.234 * i * 10\8
    val s = f.format( c )
    assert( s === "1.234i * 10\\8" )
  }

  // imaginary negative

  test("format -3i polar"){
    val f = new ComplexFormat
    f.polar = true
    val c: Complex = -3 * i
    val s = f.format( c )
    assert( s === "-3i" )
  }

  test("format -3.2i polar"){
    val f = new ComplexFormat
    f.polar = true
    val c: Complex = -3.2 * i
    val s = f.format( c )
    assert( s === "-3.2i" )
  }

  test("format -1 * 10\\-4 i polar"){
    val f = new ComplexFormat
    f.polar = true
    val c: Complex = -1 * 10\-4 * i
    val s = f.format( c )
    assert( s === "-i * 10\\-4" )
  }

  test("format -1.23i * 10\\-4 polar"){
    val f = new ComplexFormat
    f.polar = true
    val c: Complex = -1.23*i * 10\-4
    val s = f.format( c )
    assert( s === "-1.23i * 10\\-4" )
  }

  test("format -1.234i * 10\\8 polar"){
    val f = new ComplexFormat
    f.polar = true
    val c: Complex = -1.234 * i * 10\8
    val s = f.format( c )
    assert( s === "-1.234i * 10\\8" )
  }

  // interesting cases

  test("e\\0.25πi"){
    val f = new ComplexFormat
    f.polar = true
    val m = 1
    val r = 0.25
    val c: Complex = m * e\(r*π*i)
    val s = f.format( c )
    assert( s === "e\\0.25πi" )
  }

  test("e\\-0.25πi"){
    val f = new ComplexFormat
    f.polar = true
    val m = 1
    val r = -0.25
    val c: Complex = m * e\(r*π*i)
    val s = f.format( c )
    assert( s === "e\\-0.25πi" )
  }

  test("1.2 * e\\πi"){
    val f = new ComplexFormat
    f.polar = true
    val m = 1.2
    val r = 1
    val c: Complex = m * e\(r*π*i)
    val s = f.format( c )
    assert( s === "-1.2" )
  }

  test("1.23 * 10\\-4 * e\\πi"){
    val f = new ComplexFormat
    f.polar = true
    val m = 1.23 * 10\-4
    val r = 1
    val c: Complex = m * e\(r*π*i)
    val s = f.format( c )
    assert( s === "-1.23 * 10\\-4" )
  }

  test("10\\-5 * e\\πi"){
    val f = new ComplexFormat
    f.polar = true
    val m = 10\-5
    val r = 1
    val c: Complex = m * e\(r*π*i)
    val s = f.format( c )
    assert( s === "-1 * 10\\-5" )
  }

  test("1.2 * e\\-0.25πi"){
    val f = new ComplexFormat
    f.polar = true
    val m = 1.2
    val r = -0.25
    val c: Complex = m * e\(r*π*i)
    val s = f.format( c )
    assert( s === "1.2 * e\\-0.25πi" )
  }

  test("1.23 * 10\\-4 * e\\-0.25πi"){
    val f = new ComplexFormat
    f.polar = true
    val m = 1.23 * 10\-4
    val r = -0.25
    val c: Complex = m * e\(r*π*i)
    val s = f.format( c )
    assert( s === "1.23 * 10\\-4 * e\\-0.25πi" )
  }

  test("10\\-5 * e\\-0.25πi"){
    val f = new ComplexFormat
    f.polar = true
    val m = 10\-5
    val r = -0.25
    val c: Complex = m * e\(r*π*i)
    val s = f.format( c )
    assert( s === "10\\-5 * e\\-0.25πi" )
  }

}
