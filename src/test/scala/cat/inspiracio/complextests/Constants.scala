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

class Constants extends FunSuite {

  // constants ---------------------------------------------

  test("e"){
    val r: Double = e
    val c: Complex = r
    assert( c === Math.E )
  }

  test("π"){
    val r: Double = π
    val c: Complex = r
    assert( c === Math.PI )
  }

  test("i"){
    val c: Complex = i
    assert( c.toString === "i" )
  }

  test("∞"){
    val c:Complex = ∞
    assert( c.toString === "∞" )
  }
}