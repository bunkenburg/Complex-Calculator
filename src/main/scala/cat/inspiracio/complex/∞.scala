package cat.inspiracio.complex

/** Infinity, the one complex number at the north
  * pole of the Riemann sphere. */
object ∞ extends Complex{

  override def toString = "∞"

  // Operators ------------------------------------

  override def + (c: Complex): Complex = c match {
    case ∞ => throw new ArithmeticException("∞ + ∞")
    case _ => ∞
  }

  override def - (c: Complex): Complex = c match {
    case ∞ => throw new ArithmeticException("∞ - ∞")
    case _ => ∞
  }

  override def * (c: Complex): Complex = c match {
    case Real(0) => throw new ArithmeticException("∞ * 0")
    case _ => ∞
  }

  override def / (c: Complex): Complex = c match {
    case Real(0) => throw new ArithmeticException("∞/0")
    case ∞ => throw new ArithmeticException("∞/∞")
    case _ => ∞
  }

  override def ^ (c: Complex): Complex = c match {
    case Real(0) => throw new ArithmeticException("∞^0")
    case _ => ∞
  }

}
