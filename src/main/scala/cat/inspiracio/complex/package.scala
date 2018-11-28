package cat.inspiracio

import cat.inspiracio.complex.Complex.Real

/** This is all the client programmer needs. */
package object complex {

  // beautiful constants -----------------------------------

  val e = Math.E
  val i = new CartesianComplex(0, 1)
  val π = Math.PI
  val ∞ = Complex.Infinity

  // functions ---------------------------------

  def sin(z: Complex) = z.sin
  def sinh(z: Complex) = z.sinh
  def cos(z: Complex) = z.cos
  def cosh(z: Complex) = z.cosh
  def tan(z: Complex) = z.tan
  def tanh(z: Complex) = z.tanh
  def exp(z: Complex) = z.exp
  def ln(z: Complex) = z.ln

  // conversions ---------------------------------

  implicit def byte2Complex(n: Byte): Complex = Real(n.toDouble)
  implicit def int2Complex(n: Int): Complex = Real(n.toDouble)
  implicit def long2Complex(n: Long): Complex = Real(n.toDouble)
  implicit def float2Complex(f: Float): Complex = Real(f)
  implicit def double2Complex(d: Double): Complex = Real(d)

  // pattern matchers -------------------------

  // ugly constants for pattern matching

  val E: Complex = e
  val I = i
  val Pi: Complex = π
  val Infinity = Complex.Infinity

  // unapply methods for patter matching

  object Natural {

    /** val Natural(n) = z */
    def unapply(c: Complex): Option[Long] = {
      c match {
        case Integer(n) if (0 <= n) => Some(n)
        case _ => None
      }
    }

  }

  object Integer {

    /** val Integer(n) = z */
    def unapply(c: Complex): Option[Int] = {
      c match {
        case Real(re) if (re.isWhole()) => Some(re.toInt)
        case _ => None
      }
    }

  }

  object Real {

    /** val c = Real(3.2) */
    def apply(re: Double): Real = new Real(re)

    /** val Real(re) = z */
    def unapply(c: Complex): Option[Double] = {
      c match {
        case c: Real => Some(c.re)
        case Cartesian(re, im) if im == 0 => Some(re)
        case _ => None
      }
    }

  }

  object Imaginary {

    /** val Imaginary(im) = z */
    def unapply(c: Complex): Option[Double] = {
      c match {
        case Cartesian(re, im) if(re==0) => Some(im)
        case _ => None
      }
    }

  }

  /** Constructing and destructing complex
    * numbers in polar coordinates. */
  object Polar {

    /** val z = Polar(m, a) */
    def apply(m: Double, a: Double): Complex = Complex.mkPolar(m, a)

    /** val Polar(m, a) = z
      * Matches all finite numbers. */
    def unapply(c: Complex): Option[(Double,Double)] = {
      if(!c.finite)
        None
      else {
        val m = c.modulus
        val a = c.argument
        Some( (m, a) )
      }
    }

  }

  /** Constructing and destructing complex
    * numbers in Cartesian coordinates. */
  object Cartesian {

    /** val z = Cartesian(re, im) */
    def apply(re: Double, im: Double): Complex = new CartesianComplex(re, im)

    /** val Cartesian(re, im) = z
      * Matches all finite complex numbers. */
    def unapply(c: Complex): Option[(Double,Double)] = {
      if(c.finite)
        Some( (c.re, c.im) )
      else
        None
    }

  }

}