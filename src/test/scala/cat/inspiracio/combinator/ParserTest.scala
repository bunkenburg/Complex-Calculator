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
package cat.inspiracio.combinator

import cat.inspiracio.complex._
import cat.inspiracio.parsing.{C, Expression}
import org.scalatest.FunSuite

class ParserTest extends FunSuite {

  test("i"){
    val in = "i"
    val p = new Parser
    val r: Expression = p.parseAll(p.numb, in).get
    assert( r === C(i) )
  }

  test("e"){
    val in = "e"
    val p = new Parser
    val r: Expression = p.parseAll(p.numb, in).get
    assert( r === C(e) )
  }

  test("π"){
    val in = "π"
    val p = new Parser
    val r: Expression = p.parseAll(p.numb, in).get
    assert( r === C(π) )
  }

  test("∞"){
    val in = "∞"
    val p = new Parser
    val r: Expression = p.parseAll(p.numb, in).get
    assert( r === C(∞) )
  }

}
