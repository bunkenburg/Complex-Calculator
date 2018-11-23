package cat.inspiracio.numbers

/** Better way to do it? */
object Infinity extends Complex(0,0){
  override val re = java.lang.Double.POSITIVE_INFINITY
  override val im = java.lang.Double.POSITIVE_INFINITY
  override val finite = false
  override val isZero = false
  override def toString: String = "∞"

  override def sin: Complex = throw new PartialException("sin ∞")

}
