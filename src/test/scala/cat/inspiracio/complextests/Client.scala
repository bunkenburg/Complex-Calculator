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

//This is the only import. The package object.
import cat.inspiracio.complex._

/** This class shows what the client programmer
  * can do with Complex. */
class Clients extends FunSuite {

  test("client") {

    // one type ---------------------------

    var z: Complex = 0

    // constants ----------------------

    var d: Double = 0

    d = e
    d = π
    z = i
    z = ∞

    // conversions from Byte, Int, Long, Float, Double --------------------

    z = (16: Byte)
    z = 17
    z = 18L
    z = 19.0f
    z = 20.0

    // operators ---------------------

    z = -z
    z = 3 + i
    z = π * i
    z = 17 / ∞

    // exponentiation
    z = i \ 0.5
    z = 1 \ 0.5

    z = 15.3 * e\(π * i)

    // functions ---------------------

    d = Re(z)
    d = Im(z)

    z = sin(z)
    z = cos(z)
    z = tan(z)

    z = sinh(z)
    z = cosh(z)
    z = tanh(z)

    z = exp(z)
    z = ln(z)

    // comparison ----------------------

    var a: Complex = 0
    var b: Complex = 0

    a == b    //precise for doubles: sometimes not practical
    a === b   //approximate: sometimes more practical
    a === b +- 0.01 //approximate, angle on Riemann sphere

    // pattern matching ----------------

    val s = z match {

      case Natural(17) => ???
      case Natural(n) => ???   //binding n

      case Integer(18) => ???
      case Integer(n) => ???   //binding n

      case I => ???  // matches i

      case Real(0) => ???   //matches a finite real number
      case Real(re) => "???"  //binding re

      case Imaginary(4.2) => ???  //imaginary
      case Imaginary(im) => ???   //binding im

      case Polar(5, Math.PI) => ???
      case Polar(m, a) => 0     //binding m and a

      case Cartesian(3.2, 4.1) => ???
      case Cartesian(re, im) => 0   //binding re and im

      case ∞ => ???
    }

    val Cartesian(re, im) = z   //only finite
    val Polar(m, arg) = z       //only finite

  }

}