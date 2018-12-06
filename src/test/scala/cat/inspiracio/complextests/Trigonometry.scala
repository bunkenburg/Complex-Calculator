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

class Trigonometry extends FunSuite {

  test("sin(0)") {
    assert( sin(0) === 0 )
  }

  test("sin(π/2)") {
    assert( sin(π/2) === 1 )
  }

  test("sin(π)") {
    val z = sin(π)
    assert( z === 0 )
  }

  test("sin(3π/2)") {
    val c = sin(3*π/2)
    assert( c === -1 )
  }

  test("sin(2π)") {
    val c = sin(2*π)
    assert( c === 0 )
  }

  test("sin(2kπ)") {
    val k = 16
    val z = sin(2*k*π)
    assert( z === 0 +- 1e-14)
  }

  test("sin(i)") {
    val c = sin(i)
    assert( c === 1.1752011936438014 * i  )
  }

  test("sin(∞)") {
    assertThrows[ArithmeticException] {
      val c = sin(∞)
      assert(c === 0.0 )
    }
  }

  test("EC.sin"){
    import cat.inspiracio.numbers.EC
    import java.util.concurrent.ThreadLocalRandom

    val min = 0
    val max = 100

    for( _ <- 1 to 10) {
      val re = ThreadLocalRandom.current.nextDouble(min, max)
      val im = ThreadLocalRandom.current.nextDouble(min, max)

      val ec = EC.mkCartesian(re, im)
      val x = ec.sin()
      val alt = x.re() + i * x.im()

      val c = Cartesian(re, im)
      val neu = sin(c)

      assert( alt === neu )
    }
  }

}