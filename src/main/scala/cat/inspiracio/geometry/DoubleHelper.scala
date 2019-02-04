package cat.inspiracio.geometry

object DoubleHelper {

    /** Raises a positive number a little bit so that it is 'smooth',
      * that means of the form
      *
      *   n * 10^m
      *
      * where n is 1, 2, 2.5, or 5.
      *
      * @param d0 Must have 0 < d1 */
    def raiseSmooth(d0: Double): Double = {
      require( 0 < d0)
      import scala.math._

      var e = floor(log10(d0))
      val d = d0 / pow(10, e)
      //assert( 1 <= d && d < 10)

      var raised: Double =
        if ( 5 < d ) 10         // 5 < d < 10
        else if ( 2.5 < d ) 5   // 2.5 < d <= 5
        else if ( 2 < d ) 2.5   // 2 < d <= 2.5
        else if ( 1 < d ) 2     // 1 < d <= 2
        else d                  // 1 == d
      // raised in {2, 2.5, 5, 10}
      raised * pow(10, e)
    }

}
