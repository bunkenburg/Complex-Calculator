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
package cat.inspiracio.numbers

import java.text.NumberFormat

import scala.xml.Equality

/** Complex numbers.
  *
  * The only implementations are:
  * - class Real(re)
  * - class Cartesian(re,im)
  * - object Infinity
  *
  * Could become implementations:
  * - Polar
  * - Imaginary
  *
  * The client programmer must import this trait
  * and its companion object:
  * - import Complex._
  *
  * */
trait Complex {

  val finite = false
  val isZero = false
  def modulus: Double = 0
  def argument: Double = 0

  // Functions ----------------

  def sin: Complex
  def sinh: Complex
  def cos: Complex
  def cosh: Complex
  def tan: Complex
  def tanh: Complex

  def ln: Complex
  def exp: Complex

  def conj: Complex
  def opp: Complex
  def reciprocal: Complex

  def fac: Complex

  // Operators ---------------------------------

  def unary_- : Complex

  def + (c: Complex): Complex
  def - (c: Complex): Complex
  def * (c: Complex): Complex
  def / (c: Complex): Complex
  def ^ (c: Complex): Complex

}

object Complex {

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

  // Constructors --------------------------------

  /** Complex(re) */
  def apply(re: Double): Complex =
    if(re.isInfinite) ∞
    else new Cartesian(re, 0)

  /** Complex(re, im) */
  def apply(re: Double, im: Double): Complex =
    if(re.isInfinite || im.isInfinite) ∞
    else new Cartesian(re, im)

  def mkPolar(modulus: Double, angle: Double): Complex =
    if (java.lang.Double.isInfinite(modulus)) ∞
    else new Cartesian(modulus * Math.cos(angle), modulus * Math.sin(angle))

  //Constants -----------------------------------

  val e = Math.E
  val i = new Cartesian(0,1)
  val π = Math.PI
  val ∞ = Infinity

  // ugly constants for pattern matching

  val E: Complex = e
  val I = i
  val Pi: Complex = π

  //Conversions ---------------------------------

  implicit def byte2Complex(n: Byte): Complex = Real(n.toDouble)
  implicit def int2Complex(n: Int): Complex = Real(n.toDouble)
  implicit def long2Complex(n: Long): Complex = Real(n.toDouble)
  implicit def float2Complex(f: Float): Complex = Real(f)
  implicit def double2Complex(d: Double): Cartesian = Real(d)

  // Trigonometry ---------------------------------

  def sin(z: Complex) = z.sin
  def sinh(z: Complex) = z.sinh
  def cos(z: Complex) = z.cos
  def cosh(z: Complex) = z.cosh
  def tan(z: Complex) = z.tan
  def tanh(z: Complex) = z.tanh

  def exp(z: Complex) = z.exp
  def ln(z: Complex) = z.ln

  // better comparison --------------------------


  /*
  import org.scalactic.Tolerance._
  implicit val complexEq =
       new org.scalactic.Equality[Complex] {
         def areEqual(a: Complex, b: Any): Boolean =
             b match {
               case p: Complex => true
                 case _ => false
               }
       }
       */
}