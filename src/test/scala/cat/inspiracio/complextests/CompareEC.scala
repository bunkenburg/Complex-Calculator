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

import scala.util.{Failure, Success, Try}

/** Compares operations on new Complex
  * with old EC. */
class CompareEC extends FunSuite {

  /** How many random runs per comparison test? */
  val N = 20

  // helpers -----------------------------------

  def complex(max: Double): Complex = {
    import java.util.concurrent.ThreadLocalRandom
    val min = 0
    val re = ThreadLocalRandom.current.nextDouble(min, max)
    val im = ThreadLocalRandom.current.nextDouble(min, max)
    Cartesian(re, im)
  }

  def mkEC(c: Complex): EC = c match {
    case Cartesian(re,im) => EC.mkCartesian(re, im)
    case Infinity => EC.INFINITY
  }

  def mkComplex(x: EC): Complex = {
    if(!x.finite()) ∞
    else x.re() + i * x.im()
  }

  def equals(c: Complex, fc: Complex, fe: EC): Unit ={
    val cfe = mkComplex(fe)
    assert(fc === cfe)
  }

  def numbers: Seq[Complex] = {
    val constants: Seq[Complex] = Seq(
      -π, -3,
      //-π/2,
      -1, -π/4, 0, π/4, 1,
      //π/2,
      e, -3, π, -i, i, 3.2+i)

    var small: Seq[Complex] = Seq()
    for(_ <- 1 to N)
      small = complex(5) +: small

    var big: Seq[Complex] = Seq(i)
    for(_ <- 1 to N)
      big = complex(1000) +: big

    constants ++ small ++ big
  }

  def compare(
                f : Complex => Complex,
                g : EC => EC ) = {
    for(c <- numbers) {

      val e = mkEC(c)
      val fc = Try(f(c))
      val fe = Try(g(e))

      fc match {
        case Success(x) => fe match {
          case Success(y) => assert( x === mkComplex(y) , "z=" + c)
          case Failure(ye) => fail("new: " + x, ye)
        }
        case Failure(xe) => fe match {
          case Success(y) => fail("old: " + y, xe)
          case Failure(ye) => succeed
        }
      }
    }
  }

  // trigonometry -------------------------------

  test("sin"){
    compare( sin(_) , _.sin() )
  }

  test("cos"){
    compare( cos(_), _.cos() )
  }

  test("tan"){
    compare( tan(_), _.tan() )
  }

  // hyperbolic --------------------------------

  test("sinh"){
    compare( sinh(_), _.sinh() )
  }

  test("cosh"){
    compare( cosh(_), _.cosh() )
  }

  test("tanh"){
    compare( tanh(_), _.tanh() )
  }

  // exp ln --------------------------------

  test("exp"){
    compare( exp(_), _.exp() )
  }

  test("ln"){
    compare( ln(_), _.ln() )
  }

}