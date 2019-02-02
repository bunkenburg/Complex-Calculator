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

import cat.inspiracio.geometry.DoubleHelper.raiseSmooth
import org.scalatest.FunSuite

class DoubleHelperTest extends FunSuite {

  test("raiseSmooth -1"){
    assertThrows[IllegalArgumentException] {
      assert( raiseSmooth(-1) === 0.5 )
    }
  }

  test("raiseSmooth 0"){
    assertThrows[IllegalArgumentException] {
      assert( raiseSmooth(0) === 0.5 )
    }
  }

  test("raiseSmooth 0.000347"){
    assert( raiseSmooth(0.000347) === 0.0005 )
  }

  test("raiseSmooth 0.0047"){
    assert( raiseSmooth(0.0047) === 0.005 )
  }

  test("raiseSmooth 0.07"){
    assert( raiseSmooth(0.07) === 0.1 )
  }

  test("raiseSmooth 0.1"){
    assert( raiseSmooth(0.1) === 0.1 )
  }

  test("raiseSmooth 0.2"){
    assert( raiseSmooth(0.2) === 0.2 )
  }

  test("raiseSmooth 0.3"){
    assert( raiseSmooth(0.3) === 0.5 )
  }

  test("raiseSmooth 0.4"){
    assert( raiseSmooth(0.4) === 0.5 )
  }

  test("raiseSmooth 0.5"){
    assert( raiseSmooth(0.5) === 0.5 )
  }

  test("raiseSmooth 0.6"){
    assert( raiseSmooth(0.6) === 1 )
  }

  test("raiseSmooth 0.9"){
    assert( raiseSmooth(0.9) === 1 )
  }

  test("raiseSmooth 1"){
    assert( raiseSmooth(1) === 1 )
  }

  test("raiseSmooth 1.5"){
    assert( raiseSmooth(1.5) === 2 )
  }

  test("raiseSmooth 3.7"){
    assert( raiseSmooth(3.7) === 5 )
  }

  test("raiseSmooth 13.7"){
    assert( raiseSmooth(13.7) === 20 )
  }

  test("raiseSmooth 10"){
    assert( raiseSmooth(10 ) === 10 )
  }

  test("raiseSmooth 213.7"){
    assert( raiseSmooth(213.7) === 250 )
  }

  test("raiseSmooth 4213.7"){
    assert( raiseSmooth(4213.7) === 5000 )
  }

  test("raiseSmooth 84213.7"){
    assert( raiseSmooth(84213.7) === 100000 )
  }

}
