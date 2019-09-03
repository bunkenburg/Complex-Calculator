package cat.inspiracio.expo

object Expo {

  /** like Math.pow(a,b) in
   * https://docs.oracle.com/javase/8/docs/api/index.html?java/lang/Math.html */
  implicit class ExpoInt(a: Int){

    //def \(b: Int): Int = Math.pow(a, b).toInt

    private def even(n: Int): Boolean = n % 2 == 0

    /** No floating-point arithmetic.
     * O(log b).
     *
     * special cases:
     * 1 \ n = 1            not necessary as definition
     * a \ (-n) = 1 / a\n
     * 0 \ p = 0            not necessary as definition
     * induction on second argument:
     * a \ 0 = 1
     * a \ 1 = a            not necessary as definition
     * a \ (2*n) = a*a \ n  not necessary as definition but efficient
     * a \ (n+1) = a * a\n
     *
     * XXX Replace recursion by loop.
     * XXX Error: 0\0 = 0
     * */
    def \(b: Int): Int =
      // special cases
      if(a==1) 1            // not necessary as definition
      else if(b < 0) 1 / a\(-b)
      else if(a==0) 0       // (0<=b) not necessary as definition
      // induction on b
      else if(b==0) 1
      else if(b==1) a       // not necessary as definition
      else if(even(b)) (a*a) \ (b/2)  // efficiency
      else a * (a\(b-1))

  }

  implicit class ExpoDouble(a: Double){
    def \(b: Double): Double = Math.pow(a, b)
  }

}
