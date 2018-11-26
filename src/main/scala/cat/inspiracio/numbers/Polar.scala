package cat.inspiracio.numbers

object Polar {

  /** val z = Polar(m, a) */
  def apply(m: Double, a: Double) = Complex.mkPolar(m, a)

  /** val Polar(m, a) = z */
  def unapply(c: Complex): Option[(Double,Double)] = {
    val m = c.modulus
    val a = c.argument
    Some( (m, a) )
  }

}
