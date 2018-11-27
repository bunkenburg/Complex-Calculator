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
import org.scalactic.Tolerance._
import cat.inspiracio.numbers.Complex._

class Trigonometry extends FunSuite {

  test("sin 0") {
    val e = sin(0)
    assert( e === 0 )
  }

  test("sin π/2") { assert( sin(π/2) === 1 ) }

  test("sin π") {
    // Math.sin(Math.PI) == 1.2246467991473532E-16
    val Cartesian(re,im) = sin(π)
    assert( re === 0.0 +- 1.0E-15 )
    assert( im === 0.0 +- 1.0E-15 )
  }

  test("sin 3π/2") { assert( sin(3*π/2) === -1 ) }

}