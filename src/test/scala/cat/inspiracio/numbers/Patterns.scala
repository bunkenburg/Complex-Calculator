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
package cat.inspiracio.numbers

import org.scalatest.FunSuite
import cat.inspiracio.numbers.Complex._

/**
  * pattern matching:
  *   z match {
  *     case 0 =>
  *     case i: Int =>    //real
  *     case d: Double => //real
  *     case e =>
  *     case π =>
  *     case xi => ...  //imaginary
  *     case x + yi =>  //finite
  *     case ∞ =>
  *   }
  * Let's see how much of this can be done.
  * */
class Patterns extends FunSuite {

  def parse(z: Complex): String = z match {

    //case 0 => "0"
    case z if(z.isZero) => "0"

    //case n: Int => n + ": Int"

    //case d: Double => d + ": Double"
    case Real(d) => d + ": Double"

    //case e => "e"
    //case π => "π"
    //case i => "i"

    case Imaginary(x) => x + "i"

    case Cartesian(x, y) => x + " + " + y + "i"

    case Polar(m, a) => m + " e^i" + a

    //case ∞ => "∞"
    case z if(!z.finite) => "∞"

    case _ => "not recognised"
  }

  test("pattern polar") {
    val c = Polar(3, 2)
    val s = parse( c )
    assert( s === "3 e^i2" )
  }

  test("pattern 0") {
    assert( parse(0) === "0" )
  }

  test("pattern 17") {
    assert( parse(17) === "17" )
  }

  test("pattern 3.2") {
    assert( parse(3.2) === "3.2" )
  }

  test("pattern e") {
    assert( parse(e) === "e" )
  }

  test("pattern π") {
    assert( parse(π) === "π" )
  }

  test("pattern i") {
    assert( parse(i) === "i" )
  }

  test("pattern 3 + 2i") {
    assert( parse(3 + 2*1) === "3+2i" )
  }

  test("pattern ∞") {
    assert( parse(∞) === "∞" )
  }

}