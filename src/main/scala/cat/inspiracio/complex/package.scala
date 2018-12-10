package cat.inspiracio

import cat.inspiracio.complex.imp.CartesianComplex

/** This is all the client programmer needs:
  *
  * import cat.inspiracio.complex._
  *
  * */
package object complex {

  // beautiful constants -----------------------------------

  val e = Math.E
  val i = new CartesianComplex(0, 1)
  val π = Math.PI
  val ∞ = cat.inspiracio.complex.imp.Infinity

  // functions ---------------------------------

  /** Improves Math.sin for important values */
  def sin(a: Double): Double =
    if(a== -π) 0
    else if(a== -π/2) -1
    else if(a==0) 0
    else if(a== π/2) 1
    else if(a==π) 0
    else if(a==2*π) 0
    else Math.sin(a)

  def sin(z: Complex): Complex = z match {
    case ∞ => throw new ArithmeticException("sin ∞")
    case Cartesian(re,im) => {
      val zi = z * i
      (exp(zi) - exp(-zi)) / (2 * i)
    }
  }

  /** Improves Math.cos for important values */
  def cos(a: Double): Double =
    if(a== -π) -1
    else if(a== -π/2) 0
    else if(a==0) 1
    else if(a==π/2) 0
    else if(a==π) -1
    else if(a==3*π/2) 0
    else if(a==2*π) 1
    else Math.cos(a)

  def cos(z: Complex) = z match {
    case ∞ => throw new ArithmeticException("cos ∞")
    case Cartesian(re,im) => {
      val zi = z * i
      (exp(zi) + exp(-zi)) / 2
    }
  }

  /** Improves Math.tan for important values */
  def tan(a: Double): Complex =
    if(a==0) 0
    else if(a==π/4) 1
    else if(a==π/2) ∞
    else if(a==2*π) 0
    else Math.tan(a)

  def tan(z: Complex): Complex = sin(z) / cos(z)

  def cot(a: Double) = 1 / tan(a)

  def acot(m: Double) = Math.atan(1/m)

  // hyperbolic functions -------------------

  /** sinh x = -i sin(ix) */
  def sinh(z: Complex) = z.sinh

  /** cosh x = cos(ix) */
  def cosh(z: Complex) = z.cosh

  /** tanh x = -i tan(ix) */
  def tanh(z: Complex) = z.tanh

  def exp(z: Complex) = z.exp
  def exp(d: Double) = Math.exp(d)

  def ln(z: Complex) = z.ln

  //Maybe not, because Math.log(d) is very partial.
  //Gives NaN for negative values.
  //def ln(d: Double) = Math.log(d)

  def conj(z: Complex) = z.conj
  def opp(z: Complex) = z.opp
  def reciprocal(z: Complex) = z.reciprocal

  def fac(n: Complex): Complex = n.fac

  // conversions ---------------------------------

  implicit def byte2Complex(n: Byte): Complex = Real(n.toDouble)
  implicit def int2Complex(n: Int): Complex = Real(n.toDouble)
  implicit def long2Complex(n: Long): Complex = Real(n.toDouble)
  implicit def float2Complex(f: Float): Complex = if(f.isInfinite) ∞ else Real(f)
  implicit def double2Complex(d: Double): Complex = if(d.isInfinite) ∞ else Real(d)

  // pattern matchers -------------------------

  // ugly constants for pattern matching

  val E: Complex = e
  val I = i
  val Pi: Complex = π
  val Infinity = cat.inspiracio.complex.imp.Infinity

  // unapply methods for pattern matching

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
    def unapply(c: Complex): Option[Long] = {
      c match {
        case Real(re) if (re.isWhole()) => Some(re.toLong)
        case _ => None
      }
    }

  }

  object Real {

    /** val c = Real(3.2) */
    def apply(re: Double): Complex = Cartesian(re, 0)

    /** val Real(re) = z */
    def unapply(c: Complex): Option[Double] = {
      c match {
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
    def apply(m: Double, a: Double): Complex =
      if (m.isInfinite) ∞
      else Cartesian(m * cos(a), m * sin(a))


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