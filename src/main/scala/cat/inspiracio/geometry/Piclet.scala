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
 * */
package cat.inspiracio.geometry

import cat.inspiracio.complex._

// Referenced classes of package bunkenba.numbers:
//            ECList

/** Immutable, even if it has a var inside. */
abstract class Piclet() {

  protected var _samples: List[Complex] = null
  protected def sample(): Unit

  def samples: List[Complex] = {
    if (_samples == null) sample()
    _samples
  }

  def top: Double
  def bottom: Double
  def left: Double
  def right: Double
}