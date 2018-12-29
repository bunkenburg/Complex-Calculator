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
import cat.inspiracio.numbers.Rectangle

object Square {

    def apply(c: Complex, corner: Complex): Square = {
        val width = abs( Re(c) - Re(corner) )
        val height = abs( Im(c) - Im(corner) )
        val r = (width + height) / 2.0
        new Square(c, r)
    }

}

/** A square with c at the centre and side length 2*radius. */
class Square(c: Complex, r: Double) extends Rectangle(c, c + r + r*i ) {

    def getSide: Double = abs( Re(botLeft) - Re(botRight) )

}