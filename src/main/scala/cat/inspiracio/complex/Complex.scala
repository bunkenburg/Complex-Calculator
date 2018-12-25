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

import cat.inspiracio.complex.imp.Circle

/** Complex numbers.
  *
  * The only implementations are:
  * - class CartesianComplex(re,im)
  * - object ∞
  *
  * */
trait Complex {

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
      if(c == 0) throw new ArithmeticException("0^0")
      else 0
    case Polar(mx,ax) =>
      if(c == 0) 1
      else Polar(exp(log(mx) * c), c * ax)
    case ∞ =>
      if(c == 0) throw new ArithmeticException("∞^0")
      else ∞
  }

  def ^ (c: Double): Complex = this match {
    case Real(0) =>
      if(c == 0) throw new ArithmeticException("0^0")
      else 0
    case Polar(mx,ax) =>
      if(c == 0) 1
      else if(!c.isInfinite){
        val lnmx = log(mx)
        Polar(exp(lnmx * c), c * ax)
      }
      else ∞
    case ∞ =>
      if(c == 0) throw new ArithmeticException("∞^0")
      else ∞
  }

  def ^ (c: Complex): Complex = this match {
    case Real(0) =>
      if(c === 0) throw new ArithmeticException("0^0")
      else 0
    case Polar(mx,ax) =>
      c match {
        case Real(0) => 1
        case Cartesian(cre,cim) => {
          val lnmx = log(mx)
          Polar(
            exp(lnmx * cre - cim * ax),
            cim * lnmx + cre * ax)
        }
        case _ => ∞
      }
  }

  def === (c: Byte): Boolean = this match {
    case Integer(n) => n == c.toLong
    case _ => false
  }

  def === (c: Int): Boolean = this match {
    case Integer(n) => n == c.toLong
    case _ => false
  }

  def === (c: Long): Boolean = this match {
    case Integer(n) => n == c
    case _ => false
  }

  def === (c: Float): Boolean = this match {
    case Real(d) => d == c.toDouble
    case _ => false
  }

  def === (c: Double): Boolean = this match {
    case Real(d) => d == c
    case _ => false
  }

  def === (c: Complex): Boolean = this == c

  def === (c: Circle): Boolean = {
    import imp.RiemannSphere._

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
      val halfAngle = Math.asin(halfDelta / 1)

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

  private def log(d: Double): Double = Math.log(d)
}

/** Complex object. Maybe all of this can disappear? */
object Complex {

  // formatting, maybe disappears ---------------

  /** for formatting */
  private var precision: Int = 4
  def setPrecision(p: Int): Unit = {
    precision = p
    ε = Math.pow(10D, -precision)
  }
  def getPrecision: Int = precision

  // state, maybe disappears --------------------

  var ε: Double = Math.pow(10D, -precision)

  /** Should the argument function be continuous
    * or always return the principal value?
    *
    * Important for curves that use some functions
    * that use some functions implemented with
    * polar coordinates.
    *
    * But this is very bad:
    * I don't control all cases of strange behaviour.
    * And of course not threadsafe. */
  private var argContinuous: Boolean = false
  var k = 0
  var lastQuad = 0
  def resetArg(): Unit = {
    lastQuad = 0
    k = 0
  }
  def setArgContinuous(): Unit = argContinuous = true
  def setArgPrincipal(): Unit = argContinuous = false
  def isArgContinuous : Boolean = argContinuous

}