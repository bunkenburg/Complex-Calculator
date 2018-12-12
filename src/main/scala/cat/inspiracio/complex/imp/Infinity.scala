package cat.inspiracio.complex.imp

import cat.inspiracio.complex.{Complex, Real}
import cat.inspiracio.complex.Complex.∞

/** Infinity, the one complex number at the north
  * pole of the Riemann sphere. */
object Infinity extends Complex{

  override def argument = throw new ArithmeticException("argument(∞)")

  override def re = throw new ArithmeticException("re(∞)")
  override def im = throw new ArithmeticException("im(∞)")

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

  override def * (c: Complex): Complex = c match {
    case Real(0) => throw new ArithmeticException("∞ * 0")
    case _ => ∞
  }

  override def / (c: Complex) = c match {
    case Real(0) => throw new ArithmeticException("∞/0")
    case ∞ => throw new ArithmeticException("∞/∞")
    case _ => ∞
  }

  override def ^ (c: Complex): Complex = c match {
    case Real(0) => throw new ArithmeticException("∞^0")
    case _ => ∞
  }

}
