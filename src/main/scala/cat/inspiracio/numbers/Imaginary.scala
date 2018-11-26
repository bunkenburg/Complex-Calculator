package cat.inspiracio.numbers

object Imaginary {

  def unapply(c: Complex): Option[Double] = {
    c match {
      case Cartesian(_, im) => Some(im)
      case _ => None
    }
  }

}