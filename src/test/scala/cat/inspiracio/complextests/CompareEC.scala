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

import cat.inspiracio.numbers.EC

import cat.inspiracio.complex._

/** Compares operations on new Complex
  * with old EC. */
class CompareEC extends FunSuite {

  def complex(): Complex = {
    import java.util.concurrent.ThreadLocalRandom
    val min = 0
    val max = 100
    val re = ThreadLocalRandom.current.nextDouble(min, max)
    val im = ThreadLocalRandom.current.nextDouble(min, max)
    Cartesian(re, im)
  }

  def ec(c: Complex): EC = c match {
    case Cartesian(re,im) => EC.mkCartesian(re, im)
    case Infinity => EC.INFINITY
  }

  def complex(x: EC): Complex = {
    if(!x.finite()) ∞
    else x.re() + i * x.im()
  }

  test("sin"){
    for( _ <- 1 to 10) {
      val c = complex()
      val e = ec(c)

      val x = e.sin()
      val alt = complex(x)

      val neu = sin(c)

      assert( alt === neu )
    }
  }

  test("cos"){
    for( _ <- 1 to 10) {
      val c = complex()
      val e = ec(c)

      val x = e.cos()
      val alt = complex(x)

      val neu = cos(c)

      assert( alt === neu )
    }
  }

  test("tan"){
    for( _ <- 1 to 10) {
      val c = complex()
      val e = ec(c)

      val x = e.tan()
      val alt = complex(x)

      val neu = tan(c)

      assert( alt === neu )
    }
  }

}