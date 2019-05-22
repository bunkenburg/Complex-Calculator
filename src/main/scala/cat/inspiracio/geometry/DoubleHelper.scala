/*	Copyright 2019 Alexander Bunkenburg alex@inspiracio.cat
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

object DoubleHelper {

    /** Raises a positive number a little bit so that it is 'smooth',
      * that means of the form
      *
      *   n * 10^m
      *
      * where n is 1, 2, 2.5, or 5.
      *
      * @param d0 Must have 0 < d1 */
    def raiseSmooth(d0: Double): Double = {
      require( 0 < d0)
      import scala.math._

      val e = floor(log10(d0))
      val d = d0 / pow(10, e)
      //assert( 1 <= d && d < 10)

      val raised: Double =
        if ( 5 < d ) 10         // 5 < d < 10
        else if ( 2.5 < d ) 5   // 2.5 < d <= 5
        else if ( 2 < d ) 2.5   // 2 < d <= 2.5
        else if ( 1 < d ) 2     // 1 < d <= 2
        else d                  // 1 == d
      // raised in {2, 2.5, 5, 10}
      raised * pow(10, e)
    }

}
