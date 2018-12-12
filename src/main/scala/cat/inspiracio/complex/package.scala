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
  val i = Cartesian(0, 1)
  val π = Math.PI
  val ∞ = cat.inspiracio.complex.imp.Infinity

  // functions ---------------------------------

  def abs(d: Double): Double = Math.abs(d)

  /** The modulus or absolute value of the number.
    * Only for finite numbers. */
  def abs(c: Complex): Double = c match {
    case Real(r) => abs(r)
    //case Cartesian(re, im) => sqrt(sqr(re) + sqr(im))
    case cc: CartesianComplex => cc.modulus
    case ∞ => throw new ArithmeticException("|∞|")
  }

  def Re(c: Complex): Double = c match {
    case Cartesian(re, _) => re
    case ∞ => throw new ArithmeticException("Re(∞)")
  }

  def Im(c: Complex): Double = c match {
    case Cartesian(_, im) => im
    case ∞ => throw new ArithmeticException("Im(∞)")
  }

  private def sqrt(d: Double): Double = Math.sqrt(d)
  private def sqr(d: Double): Double = d*d

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
    case Real(r) => sin(r)
    case _ => {
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

  def cos(z: Complex): Complex = z match {
    case ∞ => throw new ArithmeticException("cos ∞")
    case Real(r) => cos(r)
    case _ => {
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

  def tan(z: Complex): Complex = z match {
    case Real(r) => tan(r)
    case _ => sin(z) / cos (z)
  }

  def cot(a: Double): Complex = 1 / tan(a)

  def acot(m: Double): Double = Math.atan(1/m)

  // hyperbolic functions -------------------

  /** sinh x = -i sin(ix) */
  def sinh(z: Complex): Complex = z match {
    case ∞ => throw new ArithmeticException("sinh ∞")
    case Real(r) => ( exp(r) - exp(-r) ) / 2
    case _ => ( exp(z) - exp(-z) ) / 2
  }

  /** cosh x = cos(ix) */
  def cosh(z: Complex): Complex = z match {
    case ∞ => throw new ArithmeticException("cosh ∞")
    case Real(r) => ( exp(r) + exp(-r) ) / 2
    case _ => ( exp(z) + exp(-z) ) / 2
  }

  /** tanh x = -i tan(ix) */
  def tanh(z: Complex): Complex = z match {
    case Real(r) => sinh(r) / cosh(r)
    case _ => sinh(z) / cosh(z)
  }

  def exp(d: Double): Double = Math.exp(d)

  def exp(z: Complex): Complex = z match {
    case ∞ => ∞
    case Real(r) => exp(r)
    case Cartesian(re,im) => Polar(exp(re), im)
  }

  def ln(z: Complex) = z match {
    case ∞ => ∞
    case Real(0) => throw new ArithmeticException("ln 0")
    case Polar(m,a) => Cartesian(Math.log(m), a)
  }

  /** Complex conjugate: negates the imaginary part */
  def conj(z: Complex) = z match {
    case ∞ => ∞
    case Cartesian(re,im) => Cartesian(re, -im)
  }

  /** Opposite point on Riemann sphere.
    * def opp(z) = -1 / conj(z)
    * */
  def opp(z: Complex): Complex = z match {
    case ∞ => 0
    case Real(0) => ∞
    case Polar(m,a) => Polar(1 / m, a + π)
  }

  /** factorial function for natural numbers
    * @param n Assumes 0 <= n */
  private def f(n: Long): Long =
    if(n<=1) 1
    else n * f(n-1)

  /** Factorial function, for natural numbers only */
  def fac(z: Complex): Complex = z match {
    case ∞ => ∞
    case Natural(n) if n <= 20 => f(n)
    case _ => throw new ArithmeticException(z +  "!")
  }

  // conversions ---------------------------------

  implicit def byte2Complex(n: Byte): Complex = Real(n.toDouble)
  implicit def int2Complex(n: Int): Complex = Real(n.toDouble)
  implicit def long2Complex(n: Long): Complex = Real(n.toDouble)
  implicit def float2Complex(f: Float): Complex = if(f.isInfinite) ∞ else Real(f)
  implicit def double2Complex(d: Double): Complex = if(d.isInfinite) ∞ else Real(d)

  // pattern matchers -------------------------

  // ugly constants for pattern matching

  val E = e
  val I = i
  val Pi = π

  // unapply methods for pattern matching

  object Natural {

    /** val Natural(n) = z */
    def unapply(c: Complex): Option[Long] = {
      c match {
        case Integer(n) if 0<=n => Some(n)
        case _ => None
      }
    }

  }

  object Integer {

    /** val Integer(n) = z */
    def unapply(c: Complex): Option[Long] = {
      c match {
        case Real(re) if re.isWhole() => Some(re.toLong)
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
        case Cartesian(re, im) if im==0 => Some(re)
        case _ => None
      }
    }

  }

  object Imaginary {

    /** val Imaginary(im) = z */
    def unapply(c: Complex): Option[Double] = {
      c match {
        case Cartesian(re, im) if re==0 => Some(im)
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
    def unapply(c: Complex): Option[(Double,Double)] =
      c match {
        case ∞ => None
        case cc: CartesianComplex => {
          val m = cc.modulus
          val a = cc.argument
          Some( (m, a) )
        }
      }

  }

  /** Constructing and destructing complex
    * numbers in Cartesian coordinates. */
  object Cartesian {

    /** val z = Cartesian(re, im) */
    def apply(re: Double, im: Double): Complex =
      if(re.isInfinity || im.isInfinity) ∞
      else new CartesianComplex(re, im)

    /** val Cartesian(re, im) = z
      * Matches all finite complex numbers. */
    def unapply(c: Complex): Option[(Double,Double)] = c match {
      case ∞ => None
      case cc: CartesianComplex => Some( (cc.re, cc.im) )
    }

  }

}