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
package cat.inspiracio.parsing

import cat.inspiracio.complex._
import org.scalatest.FunSuite

class SyntaxTreeTest extends FunSuite {

  test("3"){
    val t = SyntaxTree.parse("3")
    assert( t.toString === "3")
  }

  test("i"){
    val t = SyntaxTree.parse("i")
    assert( t.toString === "i")
  }

  test("∞"){
    val t = SyntaxTree.parse("∞")
    assert( t.toString === "∞")
  }

  test("π"){
    val t = SyntaxTree.parse("π")
    assert( t.toString === "π")
  }

  // --------------------------------

  test("z"){
    val t = SyntaxTree.parse("z")
    assert( t.toString === "z")
  }

  test("x"){
    val t = SyntaxTree.parse("x")
    assert( t.toString === "x")
  }

  // --------------------------------

  test("-3"){
    val t = SyntaxTree.parse("-3")
    assert( t.toString === "-3")
  }

  test("+3"){
    val t = SyntaxTree.parse("+3")
    assert( t.toString === "+3")
  }

  test("3!"){
    val t = SyntaxTree.parse("3!")
    assert( t.toString === "3!")
  }

  test("ln(3)"){
    val t = SyntaxTree.parse("ln(3)")
    assert( t.toString === "ln(3)")
  }

  test("ln z"){
    val t = SyntaxTree.parse("ln z ")
    assert( t.toString === "ln z")
  }




}
