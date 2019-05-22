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
package cat.inspiracio.geometry

import cat.inspiracio.complex._
import org.scalatest.FunSuite

class CircleTest extends FunSuite {

  test("Circle(0, 1)"){
    val c = Circle(0, 1)
    assert( c.center === 0 )
    assert( c.radius === 1 )
    assert( c.top === 1 )
    assert( c.bottom === -1 )
    assert( c.left === -1 )
    assert( c.right === +1 )
    assert( c.toString === "Circle(0, radius = 1.0 )" )
  }

  test("Circle(3+i, π)"){
    val c = Circle(3+i, π)
    assert( c.center === 3+i )
    assert( c.radius === 1.0099744945049844 )
    assert( c.top === 2.009974494504984 )
    assert( c.bottom === -0.009974494504984444 )
    assert( c.left === 1.9900255054950156 )
    assert( c.right === 4.009974494504984 )
    assert( c.toString === "Circle(3+i, radius = 1.0099744945049844 )" )
  }

  test("sample points on circle"){
    val centre: Complex = 3+i
    val b: Complex = π + 3*π*i
    val c = Circle(centre, b)
    val radius = c.radius
    val samples: List[Complex] = c.samples
    samples.foreach{ z =>

      val d = abs( c.center - z )
      assert( radius === d +- 1e-10 )
      //assert( on(c, z) )
    }
  }

  def on(c: Circle, z: Complex): Boolean = {
    val radius = c.radius
    val d = abs( c.center - z )
    radius === d
  }

}
