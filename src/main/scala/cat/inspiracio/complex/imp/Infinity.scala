package cat.inspiracio.complex.imp

import cat.inspiracio.complex.{Complex, Real}
import cat.inspiracio.complex.Complex.∞

/** Infinity, the one complex number at the north
  * pole of the Riemann sphere. */
object Infinity extends Complex{

  override def argument: Double = throw new ArithmeticException("argument(inf)") //0 //arbitrary
  override def modulus = throw new ArithmeticException("modulus(inf)") //Double.PositiveInfinity  //I'd rather not go there
  override def re = throw new ArithmeticException("re(inf)") //Double.PositiveInfinity //I'd rather not go there
  override def im = throw new ArithmeticException("im(inf)") //Double.PositiveInfinity //I'd rather not go there

  override val isZero = false
  override def toString = "∞"

  // Operators ------------------------------------

  override def + (c: Complex) = c match {
    case ∞ => throw new ArithmeticException("∞ + ∞")
    case _ => ∞
  }

  override def - (c: Complex) = c match {
    case ∞ => throw new ArithmeticException("∞ - ∞")
    case _ => ∞
  }

  override def * (c: Complex)=
    if (c.isZero) throw new ArithmeticException("∞ * 0")
    else ∞

  override def / (c: Complex) = c match {
    case Real(0) => throw new ArithmeticException("∞/0")
    case ∞ => throw new ArithmeticException("∞/∞")
    case _ => ∞
  }

  override def ^ (c: Complex)=
    if (c.isZero) throw new ArithmeticException("∞^0")
    else ∞

}
