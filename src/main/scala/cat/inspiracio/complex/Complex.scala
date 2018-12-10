/*	Copyright 2018 Alexander Bunkenburg alex@inspiracio.cat
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
package cat.inspiracio.complex

import java.text.NumberFormat

/** Complex numbers.
  *
  * The only implementations are:
  * - class CartesianComplex(re,im)
  * - object Infinity
  *
  * The client programmer must import this trait
  * and its companion object:
  * - import Complex._
  *
  * */
trait Complex {

  /** maybe can get rid of this? */
  def finite: Boolean

  /** maybe can get rid of this? */
  val isZero: Boolean

  def re: Double
  def im: Double
  def modulus: Double
  def argument: Double

  // Functions ----------------

  def tanh: Complex

  def ln: Complex
  def exp: Complex

  def conj: Complex
  def opp: Complex
  def reciprocal: Complex

  def fac: Complex

  // Operators ---------------------------------

  def unary_+ : Complex = this
  def unary_- : Complex = 0 - this

  def + (d: Double): Complex = this + double2Complex(d)
  def + (c: Complex): Complex

  def - (d: Double): Complex = this - double2Complex(d)
  def - (c: Complex): Complex

  def * (d: Double): Complex = this * double2Complex(d)
  def * (c: Complex): Complex

  def / (d: Double): Complex = this / double2Complex(d)
  def / (c: Complex): Complex

  def ^ (c: Int): Complex = this match {
    case Real(0) =>
      if(c==0) throw new ArithmeticException("0^0")
      else 0
    case Polar(mx,ax) =>
      if(c==0) 1
      else Polar(Math.exp(Math.log(mx) * c), c * ax)
    case Infinity => if(c==0) throw new ArithmeticException("∞^0") else ∞
  }

  def ^ (c: Double): Complex = this match {
    case Real(0) =>
      if(c==0) throw new ArithmeticException("0^0") else 0
    case Polar(mx,ax) =>
      if(c==0) 1
      else if(!c.isInfinite){
        val lnmx = Math.log(mx)
        Polar(Math.exp(lnmx * c), c * ax)
      }
      else ∞
    case Infinity =>
      if(c==0) throw new ArithmeticException("∞^0") else ∞
  }

  def ^ (c: Complex): Complex = this match {
    case Real(0) =>
      if(c.isZero) throw new ArithmeticException("0^0") else 0
    case Polar(mx,ax) =>
      if(c.isZero) 1
      else if(c.finite){
        val Cartesian(cre, cim) = c
        val lnmx = Math.log(mx)
        Polar(Math.exp(lnmx * cre - cim * ax), cim * lnmx + cre * ax)
      }
      else ∞
    case Infinity =>
      if(c.isZero) throw new ArithmeticException("∞^0") else ∞
  }

  def === (c: Byte): Boolean = this match {
    case Integer(c) => true
    case _ => false
  }
  def === (c: Int): Boolean = this match {
    case Integer(c) => true
    case _ => false
  }
  def === (c: Long): Boolean = this match {
    case Integer(c) => true
    case _ => false
  }
  def === (c: Float): Boolean = this match {
    case Real(c) => true
    case _ => false
  }
  def === (c: Double): Boolean = this match {
    case Real(c) => true
    case _ => false
  }
  def === (c: Complex): Boolean = this == c

  def === (c: Circle): Boolean = {
    import RiemannSphere._

    /** too much nicety */
    object sin {
      /** val sin(a) = d */
      def unapply(d: Double): Option[Double] = {
        val a = Math.asin(d)
        Some(a)
      }
    }

    //Are they actually equal?
    if(this == c.centre)
      true

    //Are they neighbours?
    else {

      // (x,y,z) with x² + y² + z² = 1
      val pthis = plane2sphere(this)
      val pc = plane2sphere(c.centre)

      // 0 <= delta <= 2
      val delta = distance(pthis, pc)

      // 0 <= halfDelta <= 1
      val halfDelta = delta / 2

      //For optimisation, could skip asin.
      //On x in [0,1], asin(x) and x are very similar.
      // asin(0) = 0
      // asin(0.5) = 0.5235987755982989
      // asin(1) = π/2
      // 0 <= halfAngle <= π/2
      val sin(halfAngle) = halfDelta / 1

      // 0 <= angle <= π
      val angle = 2 * halfAngle
      angle <= delta
    }

  }

  /** For approximate equality of complex numbers.
    * Write
    *   a === b +- angle
    * to mean: complex numbers a and b and approximately equal.
    *
    * The difference between them is limited to angle.
    * The difference is measured by the angle at the centre
    * of the Riemann sphere between the two complex numbers as
    * points on the Riemann sphere. The angle is measured in
    * radians.
    *
    * Therefore
    *   0 <= angle <= π
    * and therefore
    *   a === b +- π
    * will always be true.
    *
    * To allow a 1⁰ difference, write
    *   a === b +- π/180
    *
    * For reference:
    *   π/180 = 0.017453292519943295
    *
    * So maybe angle = 0.01 or a little less may be a practical
    * value to give.
    *
    * */
  def +- (angle: Double): Circle = Circle(this, angle)

}

case class Circle(centre: Complex, radius: Double)

/** Riemann sphere
  * https://en.wikipedia.org/wiki/Riemann_sphere
  * x² + y² + z² = 1
  * https://math.stackexchange.com/questions/1219406/how-do-i-convert-a-complex-number-to-a-point-on-the-riemann-sphere
  */
object RiemannSphere {
  import java.lang.Math.sqrt

  type Point = (Double, Double, Double)

  /** From sphere to plane: x/(1−z) + i * y/(1−z)
    * Receives point in 3d space. Riemann sphere is centered
    * on (0,0,0) and has radius 1.
    * @param x
    * @param y
    * @param z
    * @return Complex number represented by this point.
    * */
  def sphere2plane(x: Double, y: Double, z: Double): Complex =
    if(z== -1) 0
    else if(z==1) ∞
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
      case Infinity => (0,0,1)
      case Cartesian(re, im) => {
        val re2 = sqr(re) //maybe Double.Infinity
        val im2 = sqr(im) //maybe Double.Infinity
        if(re2.isInfinity || im.isInfinity)
          (0,0,1)
        else {
          val d = 1 + re2 + im2
          (
            2 * re / d,
            2 * im / d,
            (re2 + im2 - 1) / d
          )
        }
      }
    }

  def distance(a: Point, b: Point): Double =
    sqrt(sqr(a._1-b._1) + sqr(a._2-b._2) + sqr(a._3-b._3))

  private def sqr(d: Double) = d*d
}

/** Complex object. Maybe all of this can disappear? */
object Complex {

  // constants ----------------

  val π = Math.PI
  val ∞ = cat.inspiracio.complex.imp.Infinity

  // formatting, maybe disappears ---------------

  /** for formatting */
  private var PRECISION: Int = 4
  def setPrecision(np: Int): Unit = {
    val op = PRECISION
    PRECISION = np
    EPSILON = Math.pow(10D, -PRECISION)
    nf.setMaximumFractionDigits(np)
  }
  def getPrecision: Int = PRECISION

  val nf = NumberFormat.getInstance()
  nf.setGroupingUsed(false)
  nf.setMaximumFractionDigits(10)

  // state, maybe disappears --------------------

  var EPSILON: Double = Math.pow(10D, -PRECISION)

  /** important for curves in polar coordinates */
  var argContinuous: Boolean = false
  var k = 0
  var lastQuad = 0
  private def resetArg(): Unit = {
    lastQuad = 0
    k = 0
  }
  def setArgContinuous(): Unit = argContinuous = true
  def setArgPrincipal(): Unit = argContinuous = false

}