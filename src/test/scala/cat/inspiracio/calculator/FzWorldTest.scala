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
package cat.inspiracio.calculator

import cat.inspiracio.complex._
import cat.inspiracio.geometry.Square
import cat.inspiracio.parsing.Variable
import org.scalatest.FunSuite

class FzWorldTest extends FunSuite {

  test("loop"){

    val calculator = new Calculator
    val zW = new ZWorld(calculator)
    val fzW = new FzWorld(calculator)
    fzW.setzWorld(zW)
    val tree = new Variable
    fzW.functionChanged(tree)
    val z = 3+i
    val p = Square(z, z)
    fzW.add(p)

    calculator.quit()
    fzW.dispose()
    zW.dispose()
  }

}
