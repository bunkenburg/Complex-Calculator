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
    * @param p = (x,y,z) In 3d space on the unit sphere
    * @return Complex number represented by this point. */
  def sphere2plane(p: Point): Complex =
    p match { case (x,y,z) => sphere2plane(x,y,z) }

  /** From plane to sphere (x,y,z) = ( 2X/(1+X²+Y²) , 2Y/(1+X²+Y²) , (X²+Y²−1)/(1+X²+Y²) )
    * @param c Complex number
    * @return 3d point on unit sphere */
  def plane2sphere(c: Complex): Point =
    c match {
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

  def distance(a: Point, b: Point): Double = sqrt(sqr(a._1-b._1) + sqr(a._2-b._2) + sqr(a._3-b._3))

}
