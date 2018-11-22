/*	Copyright 2018 Alexander Bunkenburg alex@cat.inspiracio.com
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
package cat.inspiracio.numbers

import java.text.NumberFormat

/** start with Cartesian */
//XXX make constructor private
class Complex(val re: Double, val im: Double){

  import Complex._

  // methods with 0 parameters ------------------------------------

  def finite = true
  def isZero = re==0 && im==0

  /** Why results 0, 1, 2, 3, 4 ?
    * I would expect only four results. */
  def quadrant: Int =
    if (0 <= re  && 0 <= im) 1
    else if (re < 0 && 0 <= im ) 2
    else if (re < 0 && im < 0) 3
    else if (re < 0 || 0 <= im) 0
    else 4

  def acos: Complex = throw new PartialException("acos not implemented.")
  def asin: Complex = throw new PartialException("asin not implemented")
  def atan: Complex = throw new PartialException("atan not implemented")

  def cos: Complex =
    if (finite) {
      val z = this
      val c = z * i
      (c.exp + (-c).exp) / 2
    }
    else throw new PartialException("cos ∞")

  def cosh: Complex =
    if (finite) {
      val z = this
      (z.exp + ((-z).exp)) / 2
    }
    else throw new PartialException("cosh ∞")

  def exp: Complex = ???

  def argument: Double = if (finite && !isZero) {
    val d = Math.atan2(im, re)
    if (argContinuous) {
      val q = quadrant
      if (lastQuad == 2 && q == 3)
        k += 1
      else if (lastQuad == 3 && q==2)
        k -= 1
      lastQuad = q
      d + (2 * k).toDouble * Math.PI
    }
    else d
  }
  else 0

  /** complex conjugate */
  def conj: Complex = if (finite) Complex(re, -im) else ∞

  def unary_- : Complex = ???

  // operators of 2 parameters ----------------------------------

  def + (ec: Complex): Complex = {
    if (finite) {
      if (ec.finite) Complex(re + ec.re, im + ec.im) else ∞
    } else {
      if (ec.finite) ∞ else throw new PartialException("∞ + ∞")
    }
  }

  def * (ec: Complex): Complex = ???
  def / (ec: Complex): Complex = ???

}

/** Better way to do it? */
object Infinity extends Complex(java.lang.Double.POSITIVE_INFINITY,java.lang.Double.POSITIVE_INFINITY){
  override def toString: String = "∞"
}

object Complex{

  // formatting, maybe disappears ---------------

  val piString = "\u03C0"
  val infinityString = "\u221E" // "inf";

  /** for formatting */
  private var PRECISION: Int = 4
  def setPrecision(np: Int): Unit = {
    val op = PRECISION
    PRECISION = np
    EPSILON = Math.pow(10D, -PRECISION)
    nf.setMaximumFractionDigits(np)
  }
  def getPrecision: Int = PRECISION

  private val nf = NumberFormat.getInstance()
  nf.setGroupingUsed(false)
  nf.setMaximumFractionDigits(10)

  // state, maybe disappears --------------------

  private var EPSILON: Double = Math.pow(10D, -PRECISION)

  /** important for curves in polar coordinates */
  private var argContinuous: Boolean = false
  private var k = 0
  private var lastQuad = 0

  // Constructors --------------------------------

  def apply(re: Double): Complex = if(re.isInfinite) ∞ else new Complex(re, 0d)

  def apply(re: Double, im: Double): Complex =
    if(re.isInfinite || im.isInfinite) ∞ else new Complex(re, im)

  def mkPolar(d: Double, d1: Double): Complex =
    if (java.lang.Double.isInfinite(d)) ∞ else Complex(d * Math.cos(d1), d * Math.sin(d1))

  //Constants -----------------------------------

  val e = Math.E
  val i = Complex(0,1)
  val π = Math.PI
  val ∞ = Infinity

  //Conversions ---------------------------------

  implicit def int2Complex(n: Int): Complex = Complex(n.toDouble)
  implicit def double2Complex(d: Double): Complex = Complex(d)

}