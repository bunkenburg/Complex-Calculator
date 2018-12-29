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
import cat.inspiracio.numbers.{ECList, Piclet}

object Circle {

  /** val c = Circle(c, r) */
  def apply(c: Complex, r: Complex): Circle = {
    val center: Complex = if (finite(c)) c else 0
    val radius = if (finite(r)) distance(center, r) else Double.PositiveInfinity
    new Circle(center, radius)
  }

  private def distance(a: Complex, b: Complex): Double = {
    if (finite(a) && finite(b))
      sqrt(sqr(Re(a) - Re(b)) + sqr(Im(a) - Im(b)))
    else if (finite(a) != finite(b))
      Double.PositiveInfinity
    else
      0
  }

}

class Circle (val c: Complex, val r: Double) extends Piclet {

  def center: Complex = c
  def radius: Double = r

  override def top: Double = Im(c) + r
  override def bottom: Double = Im(c) - r
  override def left: Double = Re(c) - r
  override def right: Double = Re(c) + r

  override protected def sample(): Unit = {
    val d = 0.20943951023931953D
    var angle = 0.0D
    for ( i <- 0 to 30 ) {
      val z = Polar(r, angle)
      samples = new ECList( c + z, samples)
      angle += d
    }
  }

}