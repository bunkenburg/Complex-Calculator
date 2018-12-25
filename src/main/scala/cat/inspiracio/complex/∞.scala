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

  override def === (c: Byte): Boolean = false
  override def === (c: Int): Boolean = false
  override def === (c: Long): Boolean = false
  override def === (c: Float): Boolean = c.isInfinity
  override def === (c: Double): Boolean = c.isInfinity
  override def === (c: Complex): Boolean = c match {
    case ∞ => true
    case _ => false
  }

}
