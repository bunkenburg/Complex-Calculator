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
package cat.inspiracio.complex.imp

import cat.inspiracio.complex.{Cartesian, Complex, sqr, sqrt, ∞, i}

/** Riemann sphere
  * https://en.wikipedia.org/wiki/Riemann_sphere
  * x² + y² + z² = 1
  * https://math.stackexchange.com/questions/1219406/how-do-i-convert-a-complex-number-to-a-point-on-the-riemann-sphere
  *
  */
object RiemannSphere {

  private type Point = (Double, Double, Double)

  /** From sphere to plane: x/(1−z) + i * y/(1−z)
    * Receives point in 3d space. Riemann sphere is centered
    * on (0,0,0) and has radius 1.
    * @param x
    * @param y
    * @param z
    * @return Complex number represented by this point.
    * */
  def sphere2plane(x: Double, y: Double, z: Double): Complex =
    if(z == -1) 0
    else if(z == 1) ∞
    else x/(1-z) + (y/(1-z))*i

  /** From sphere to plane: x/(1−z) + i * y/(1−z).
    *
    * param p = (x,y,z) In 3d space on the unit sphere
    * @return Complex number represented by this point.
    */
  val sphere2plane: Point => Complex = {
    case (x,y,z) => sphere2plane(x,y,z)
  }

  /** From plane to sphere (x,y,z) = ( 2X/(1+X²+Y²) , 2Y/(1+X²+Y²) , (X²+Y²−1)/(1+X²+Y²) )
    * @return 3d point on unit sphere */
  val plane2sphere: Complex => Point = {
      case ∞ => (0,0,1)
      case Cartesian(re, im) => {
        val re2 = sqr(re) //maybe Double.Infinity
        val im2 = sqr(im) //maybe Double.Infinity
        if(re2.isInfinity || im.isInfinity)
          (0,0,1)
        else {
          val d = 1 + re2 + im2
          val x = 2 * re / d
          val y = 2 * im / d
          val z = (re2 + im2 - 1) / d
          ( x, y, z )
        }
      }
    }

  val distance: (Point,Point) => Double = {
    case ((ax,ay,az),(bx,by,bz)) => sqrt(sqr(ax-bx) + sqr(ay-by) + sqr(az-bz))
  }

}
