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
 * */
package cat.inspiracio.geometry;


// Referenced classes of package bunkenba.numbers:
//            Rectangle, Circle, EC, PartialException

import cat.inspiracio.complex._

object Square {

    /** Makes a square.
      * @param center of the square
      * @param corner if this can be a corner to this center, otherwise approximate */
    def apply(center: Complex, corner: Complex): Square = {
        val width = abs( Re(center) - Re(corner) )
        val height = abs( Im(center) - Im(corner) )
        val r = (width + height) / 2.0
        new Square(center, r)
    }

}

/** A square with c at the centre and side length 2*radius. */
class Square private (center: Complex, radius: Double) extends Rectangle(center, center + radius + radius*i ) {

    def side: Double = abs( Re(botLeft) - Re(botRight) )

    override def toString: String = s"Square($center, radius = $radius )"

}