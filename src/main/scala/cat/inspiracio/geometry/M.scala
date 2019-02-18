package cat.inspiracio.geometry

import scala.reflect.ClassTag

/** Not sure what to call this.
  * An (2n+1) * (2n+1) matrix of objects.
  *
  * Mutable.
  *
  * Similar to Array[Array[T]] with fixed sizes.
  * Exists for making initialization and indexing better. */
class M[T : ClassTag](val n: Int) {

  private var data: Array[Array[T]] = new Array[Array[T]](2*n + 1)
  for( i <- 0 to 2*n )
    data(i) = new Array[T](2 * n + 1)

  def update(x: Int, y: Int, t: T) = data(x+n)(y+n) = t

  def apply(x: Int, y: Int): T = data(x+n)(y+n)

}
