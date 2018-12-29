/*	Copyright 2011 Alexander Bunkenburg alex@inspiracio.cat
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
 * *//*	Copyright 2011 Alexander Bunkenburg alex@inspiracio.cat
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
import cat.inspiracio.complex.{Im, Re, _}
import cat.inspiracio.numbers.{ECList, Piclet}

object Rectangle {

  def apply(c: Complex, corner: Complex): Rectangle = new Rectangle(c, corner)

}

class Rectangle (val center: Complex, corner: Complex) extends Piclet {

  val width = 2 * abs( Re(center) - Re(corner) )
  val height = 2 * abs( Im(center) - Im(corner) )
  val botLeft = center + Cartesian( -width/2.0, -height/2.0 )
  val topLeft = center + Cartesian( -width/2.0, height/2.0 )
  val botRight = center + Cartesian( width/2.0, -height/2.0 )
  val topRight = center + Cartesian( width/2.0, height/2.0 )

  override def top: Double = Im(topLeft)
  override def bottom: Double = Im(botLeft)
  override def left: Double = Re(topLeft)
  override def right: Double = Re(topRight)

  override protected def sample(): Unit = {
    var ec = (botRight - botLeft) / 30
    var ec1 = botLeft
    samples = new ECList(ec1, samples)
    var i = 0
    for ( i <- 0 to 30 ) {
      ec1 = ec1 + ec
      samples = new ECList(ec1, samples)
    }

    ec = ( topRight - botRight) / 30
    ec1 = botRight
    samples = new ECList(ec1, samples)
    var j = 0
    for ( j <- 0 to 30 ) {
      ec1 = ec1 + ec
      samples = new ECList(ec1, samples)
    }

    ec = (topLeft - topRight) / 30
    ec1 = topRight
    samples = new ECList(ec1, samples)
    var k = 0
    for ( k <- 0 to 30 ) {
      ec1 = ec1 + ec
      samples = new ECList(ec1, samples)
    }

    ec = ( botLeft - topLeft) / 30
    ec1 = topLeft
    samples = new ECList(ec1, samples)
    for ( l <- 0 to 30 ) {
      ec1 = ec1 + ec
      samples = new ECList(ec1, samples)
    }

  }
}