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
import cat.inspiracio.complex.RiemannSphere._

// Riemann sphere ---------------------------------------
// https://en.wikipedia.org/wiki/Riemann_sphere
// x² + y² + z² = 1
// https://math.stackexchange.com/questions/1219406/how-do-i-convert-a-complex-number-to-a-point-on-the-riemann-sphere
class RiemannSphereTest extends FunSuite {

  /** How many random runs per comparison test? */
  val N = 20

  def complex(max: Double): Complex = {
    import java.util.concurrent.ThreadLocalRandom
    val min = 0
    val re = ThreadLocalRandom.current.nextDouble(min, max)
    val im = ThreadLocalRandom.current.nextDouble(min, max)
    Cartesian(re, im)
  }

  test("sphere ∞"){
    val z = sphere2plane(0,0,1)
    assert( z === ∞ )
  }

  test("sphere 0"){
    val z = sphere2plane(0,0, -1)
    assert( z === 0 )
  }

  test("sphere 1"){
    val z = sphere2plane(1, 0, 0)
    assert( z === 1 )
  }

  test("plane ∞"){
    val z = plane2sphere(∞)
    assert( z === (0,0,1) )
  }

  test("plane large") {
    val c = 6.863146616520403E201 + 2.1823605310174247E201 * i
    val (x, y, z) = plane2sphere(c) //(0.0,0.0,NaN)
    assert( (x, y, z) === (0, 0, 1))
  }

  test("plane 0"){
    val z = plane2sphere(0)
    assert( z === (0,0, -1) )
  }

  test("plane 1"){
    val z = plane2sphere(1)
    assert( z === (1, 0, 0) )
  }

  test("many"){
    val MAX = 16
    for(_ <- 1 to N){
      val c = complex(MAX)
      val p = plane2sphere(c)
      val z = sphere2plane(p)
      assert( c === z +- 1e-15 )
    }
  }

}