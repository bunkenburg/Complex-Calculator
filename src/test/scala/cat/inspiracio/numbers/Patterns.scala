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
    case z if z.isZero => "zero"

    //case n: Int => n.toString

    //case d: Double => d.toString
    case Real(d) => d.toString

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

  test("0") {
    val c = 0
    val s = parse(c)
    assert( s === "zero" )
  }

  test("int 17") {
    assert( parse(17) === "17.0" )
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
    assert( parse(3.2) === "3.2" )
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

  test("Imaginary i") {
    assert( parse(i) === "1.0i" )
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

  test("Polar(3, 2)") {
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

  test("pattern ∞") {
    assert( parse(∞) === "∞" )
  }

}