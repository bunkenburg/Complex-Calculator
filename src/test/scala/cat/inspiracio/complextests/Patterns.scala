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

class Patterns extends FunSuite {

  def parse(z: Complex): String = z match {

    case Natural(20) => "Natural(20)"
    case Natural(n) => "Natural(n)"

    case Integer(18) => "Integer(18)"
    case Integer(n) => "Integer(n)"

    case I => "i"

    case Real(0) => "Real(0)"
    case Real(3.2) => "Real(3.2)"
    case Real(re) => "Real(re) " + re

    case Imaginary(4.2) => "Imaginary(4.2)"
    case Imaginary(x) => x + "i"

    case Cartesian(14.7, -9.3) => "Cartesian(14.7, -9.3)"
    case Cartesian(x, y) => x + " + " + y + "i"

    case Polar(m, a) => m + " e^i" + a

    case ∞ => "Infinity"

    case _ => "not recognised"
  }

  // constants ---------------------------------------------

  test("i"){
    val s = parse(i)
    assert( s === "i" )
  }

  // natural ------------------------------------------------

  test("Natural(20)") {
    val c: Complex = 20
    val s = parse(c)
    assert( s === "Natural(20)" )
  }

  test("Natural(n)") {
    val c: Complex = 21
    val s = parse(c)
    assert( s === "Natural(n)" )
  }


  // integer ------------------------------------------------

  test("Integer(18)") {
    val c: Complex = 18
    val s = c match {
      case Integer(18) => "Integer(18)"
      case _ => "fail"
    }
    assert( s === "Integer(18)" )
  }

  test("Integer(n)") {
    val c: Complex = 19
    val s = c match {
      case Integer(n) => s"Integer($n)"
      case _ => "fail"
    }
    assert( s === "Integer(19)" )
  }

  // real ---------------------------------------------------

  test("Real(0)") {
    val c: Complex = 0
    val s = c match {
      case Real(0) => s"Real(0)"
      case _ => "fail"
    }
    assert( s === "Real(0)" )
  }

  test("Real(re)") {
    val c = 3.4
    val s = parse(c)
    assert( s === "Real(re) 3.4" )
  }

  test("Real 0") {
    val c: Complex = 0
    val s = c match {
      case Real(re) => "zero"
      case _ => "fail"
    }
    assert( s === "zero" )
  }

  test("Real 3.2") {
    val s = parse(3.2)
    assert( s === "Real(3.2)" )
  }

  test("Real 2+i") {
    val c = 2+i
    val s = c match {
      case Real(re) => "real"
      case _ => "fail"
    }
    assert( s === "fail" )
  }

  test("Real ∞") {
    val c = ∞
    val s = c match {
      case Real(re) => "real"
      case _ => "fail"
    }
    assert( s === "fail" )
  }

  // imaginary -----------------------------------------------

  test("Imaginary(4.2)") {
    val c = 4.2 * i
    val s = parse(c)
    assert( s === "Imaginary(4.2)" )
  }

  test("I") {
    assert( parse(i) === "i" )
  }

  test("Imaginary 3.2i") {
    assert( parse(3.2 * i) === "3.2i" )
  }

  test("Imaginary 0") {
    val c: Complex = 0
    val s = c match {
      case Imaginary(im) => "zero"
      case _ => "fail"
    }
    assert( s === "zero" )
  }

  test("Imaginary 2+i") {
    val c: Complex = 2+i
    val s = c match {
      case Imaginary(im) => "zero"
      case _ => "fail"
    }
    assert( s === "fail" )
  }

  test("Imaginary ∞") {
    val c = ∞
    val s = c match {
      case Imaginary(im) => "∞"
      case _ => "fail"
    }
    assert( s === "fail" )
  }

  // polar --------------------------------------------------------

  test("Polar(3, Math.PI)") {
    val c = Polar(3, Math.PI)
    val s = c match {
      case Polar(3, Math.PI) => "Polar(3, Math.PI)"
      case _ => "fail"
    }
    assert( s === "Polar(3, Math.PI)" )
  }

  test("Polar(m, a)") {
    val c = Polar(3, 2)
    val s = c match {
      case Polar(m,a) => s"$m * e^i$a"
      case _ => "fail"
    }
    assert( s === "3.0 * e^i2.0" )
  }

  test("polar ∞") {
    val c = ∞
    val s = c match {
      case Polar(m,a) => s"$m * e^i$a"
      case _ => "fail"
    }
    assert( s === "fail" )
  }

  test("polar 3") {
    val c = Real(3)
    val s = c match {
      case Polar(m,a) => s"Polar($m, $a)"
      case _ => "fail"
    }
    assert( s === "Polar(3.0, 0.0)" )
  }

  test("Polar 1+i") {
    val c = 1+i
    val s = c match {
      case Polar(m,a) => s"Polar($m, $a)"
      case _ => "fail"
    }
    val m = Math.sqrt(2)
    val a = π/4
    assert( s === s"Polar($m, $a)" )
  }

  test("Polar 0") {
    val c = Real(0)
    val s = c match {
      case Polar(m,a) => s"Polar($m, $a)"
      case _ => "fail"
    }
    assert( s === "Polar(0.0, 0.0)" )
  }

  // Cartesian -------------------

  test("Cartesian(14.7 -9.3)") {
    val c = 14.7 - 9.3*i
    val s = c match {
      case Cartesian(14.7, -9.3) => "Cartesian(14.7, -9.3)"
      case _ => "fail"
    }
    assert( s === "Cartesian(14.7, -9.3)" )
  }

  test("Cartesian 0") {
    val c = Real(0)
    val s = c match {
      case Cartesian(re,im) => s"$re + $im*i)"
      case _ => "fail"
    }
    assert( s === "0.0 + 0.0*i)" )
  }

  test("Cartesian 3 + 2i") {
    val c = 3 + 2*i
    val s = parse(c)
    assert( s === "3.0 + 2.0i" )
  }

  test("Cartesian ∞") {
    val c: Complex = ∞
    val s = c match {
      case Cartesian(re,im) => s"$re + $im*i)"
      case _ => "fail"
    }
    assert( s === "fail" )
  }

  // Infinity ------------------------

  test("Infinity") {
    val c = ∞
    val s = parse(c)
    assert( s === "Infinity" )
  }

}