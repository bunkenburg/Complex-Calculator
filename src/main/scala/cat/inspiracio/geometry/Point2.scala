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

import java.awt.Point

/** convenient calculations on 2d Points */
object Point2 {

  implicit def pair2Point2(p: (Int,Int) ) = new Point2(p._1, p._2)
  implicit def point2Point2(p: Point) = new Point2(p.x, p.y)

  def apply(x: Int, y: Int): Point2 = new Point2(x, y)
  def apply(p: Point): Point2 = new Point2(p.x, p.y)
  def unapply(p: Point): Option[(Int, Int)] = Some(p.x, p.y)
}
class Point2(x: Int, y: Int) extends Point(x,y) {

  def + (a: Int, b: Int) = new Point2(x + a, y + b)
  def - (p: Point) = new Point2(x - p.x, y - p.y)

}
