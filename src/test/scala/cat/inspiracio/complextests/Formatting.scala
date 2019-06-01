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

import java.text.NumberFormat

import cat.inspiracio.complex._
import org.scalatest.FunSuite

class Formatting extends FunSuite {

  // parsing -------------------------------------------

  test("parse i+5"){
    val f = new ComplexFormat
    val c = f.parse("i + 5")
    assert( c === i+5 )
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
    val c: Complex = 3.21 * e\(1*π*i)
    val s = f"${c}%#s"
    assert( s === "3.21 e^πi" )
  }

  test("3.21 e^0.75πi polar"){
    val c: Complex = 3.21 * e\(0.75*π*i)
    val s = f"${c}%#s"
    assert( s === "3.21 e^0.75πi" )
  }

  test("3.21 e^-0.75πi polar"){
    val c: Complex = 3.21 * e\(-0.75*π*i)
    val s = f"${c}%#s"
    assert( s === "3.21 e^-0.75πi" )
  }

  // testing Formattable -----------------------------------------------------

  // tests for ComplexFormat ===================================================

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

  test("format 1.0 minfraction 3"){
    val f = new ComplexFormat
    f.minimumFractionDigits = 3
    val c = 1.0
    val s = f.format( c )
    assert( s === "1.000" )
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
