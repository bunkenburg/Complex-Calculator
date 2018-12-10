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

class ToString extends FunSuite {

  test("toString 0") {
    val c: Complex = 0
    assert(c.toString === "0")
  }

  test("toString 1") {
    val c: Complex = 1
    assert(c.toString === "1")
  }

  test("toString 3") {
    val c: Complex = 3
    assert(c.toString === "3")
  }

  test("toString 0.5") {
    val c: Complex = 0.5
    assert(c.toString === "0.5")
  }

  test("toString e") {
    val c: Complex = e
    assert(c.toString === "e")
  }

  test("toString(-e)") {
    val c: Complex = -e
    assert(c.toString === "-e")
  }

  test("toString π") {
    val c: Complex = π
    assert(c.toString === "π")
  }

  test("toString i") {
    val c: Complex = i
    assert(c.toString === "i")
  }

  test("toString 3+i") {
    val c: Complex = 3+i
    assert(c.toString === "3+i")
  }

  test("toString ∞") {
    val c: Complex = ∞
    assert(c.toString === "∞")
  }

  test("toString(1+i)") {
    val c: Complex = 1 + i
    assert(c.toString === "1+i")
  }

  test("toString(1-i)") {
    val c: Complex = 1 - i
    assert(c.toString === "1-i")
  }

}