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
package cat.inspiracio.geometry

import cat.inspiracio.complex.Complex

// Referenced classes of package bunkenba.numbers:
//            Piclet, ECList

object Curve {
  def apply(list: List[Complex]): Curve = new Curve(list)
}

class Curve(val list: List[Complex]) extends Piclet {

  samples = list

  //Better: bounding rectangle
  override def top = 0
  override def bottom = 0
  override def left = 0
  override def right = 0

  override protected def sample(): Unit = {}

}