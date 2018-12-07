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

  // helpers -----------------------------------

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

  def random() = {
    val c = complex()
    val e = ec(c)
    (c, e)
  }

  def equals(c: Complex, fc: Complex, fe: EC): Unit ={
    val cfe = complex(fe)
    assert(fc === cfe)
  }

  // trigonometry -------------------------------

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

  // hyperbolic --------------------------------

  test("sinh"){
    for( _ <- 1 to 10) {
      val (c,e) = random()
      equals(c, sinh(c), e.sinh())
    }
  }

  test("cosh"){
    for( _ <- 1 to 10) {
      val (c,e) = random()
      equals(c, cosh(c), e.cosh())
    }
  }

  test("tanh"){
    for( _ <- 1 to 10) {
      val (c,e) = random()
      equals(c, tanh(c), e.tanh())
    }
  }

  // exp ln --------------------------------

  test("exp"){
    for( _ <- 1 to 10) {
      val (c,e) = random()
      equals(c, exp(c), e.exp())
    }
  }

  test("ln"){
    for( _ <- 1 to 10) {
      val (c,e) = random()
      equals(c, ln(c), e.ln())
    }
  }

}