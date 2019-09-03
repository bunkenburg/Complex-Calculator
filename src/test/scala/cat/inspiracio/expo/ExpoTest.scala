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
import cat.inspiracio.expo.Expo._

class ExpoTest extends FunSuite {

  // https://docs.oracle.com/javase/8/docs/api/index.html?java/lang/Math.html

  test("3 \\ 2"){
    val n: Int = 3 \ 2
    assert( n === 9 )
  }

  /** If the second argument is positive or negative zero, then the result is 1.0. */
  test("3\\ 0"){
    val n: Int = 3 \ 0
    assert( n === 1 )
  }

  /** If the second argument is 1.0, then the result is the same as the first argument.  */
  test("3\\ 1"){
    val n: Int = 3 \ 1
    assert( n === 3 )
  }

  /** If the second argument is 1.0, then the result is the same as the first argument.  */
  test("-3\\ 1"){
    val n: Int = -3 \ 1
    assert( n === -3 )
  }

  /** If the first argument is positive zero and the second argument is greater than zero, then the result is positive zero.  */
  test("0 \\ 3"){
    val n: Int = 0 \ 3
    assert( n === 0 )
  }

  /** If the first argument is positive zero and the second argument is less than zero, then the result is positive infinity.  */
  test("0 \\ -3"){
    assertThrows[ArithmeticException] {
      val n: Int = 0 \ -3
    }
  }

  /** If the first argument is negative zero and the second argument is greater than zero but not a finite odd integer,
   * then the result is positive zero.  */
  test("0 \\ 4"){
    val n: Int = 0 \ 4
    assert( n === 0 )
  }

  /** If the first argument is negative zero and the second argument is a positive finite odd integer,
   * then the result is negative zero.  */
  test("-0 \\ 3"){
    val n: Int = -0 \ 3
    assert( n === 0 )
  }

  /** If the first argument is negative zero and the second argument is less than zero but not a finite odd integer,
   * then the result is positive infinity.  */
  test("0 \\ -4"){
    assertThrows[ArithmeticException] {
      val n: Int = 0 \ -4
    }
  }

  /** If the first argument is negative zero and the second argument is a negative finite odd integer,
   * then the result is negative infinity. */
  test("-0 \\ -3"){
    assertThrows[ArithmeticException] {
      val n: Int = -0 \ -3
    }
  }

  /** If the first argument is finite and less than zero and
   * if the second argument is a finite even integer,
   * the result is equal to the result of raising the absolute value of the first argument to the power of the second argument. */
  test("-3 \\ 4"){
    val n: Int = -3 \ 4
    assert( n === 3 \ 4 )
  }

  /** If the first argument is finite and less than zero and
   * if the second argument is a finite odd integer,
   * the result is equal to the negative of the result of raising the absolute value of the first argument to the power of the second argument. */
  test("-3 \\ 3"){
    val n: Int = -3 \ 3
    assert( n === -( 3 \ 3) )
  }

  test("0 \\ 0"){
    val n: Int = 0 \ 0
    assert( n === 0 )
  }

}