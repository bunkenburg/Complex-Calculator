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

/** Tests Complex implements Formattable */
class FormattableTest extends FunSuite {

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

}
