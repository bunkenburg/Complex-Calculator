/*	Copyright 2011 Alexander Bunkenburg alex@cat.inspiracio.com
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
 * *//*	Copyright 2011 Alexander Bunkenburg alex@cat.inspiracio.com
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

// Referenced classes of package bunkenba.numbers:
//            Piclet, EC, ECList, PartialException
import cat.inspiracio.complex._
import java.lang.Math.{max,min}

object Line {
  def apply(a: Complex, b: Complex): Line = new Line(a,b)
}

class Line(var a: Complex, var b: Complex) extends Piclet {

  override def top: Double = max(Im(a), Im(b))
  override def bottom: Double = min(Im(a), Im(b))
  override def left: Double = max(Re(a), Re(b))
  override def right: Double = min(Re(a), Re(b))

  def length: Double = abs(a-b)

  override protected def sample(): Unit = {
    samples = b :: Nil

    //30 steps from b to a
    val step = ( a - b) / 30
    var z = b
    for ( i <- 0 until 30 ) {
      z = z + step
      samples = z :: samples
    }

    //For precision, add a rather than b + 30*step which should be equal to a,
    //but sometimes is not, because of imprecise doubles.
    samples = a :: samples
  }

  override def toString: String = s"Line( $a, $b )"

}