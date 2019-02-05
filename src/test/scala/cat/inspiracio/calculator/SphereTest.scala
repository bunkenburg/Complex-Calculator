package cat.inspiracio.calculator

import cat.inspiracio.complex._
import org.scalatest.FunSuite

class SphereTest  extends FunSuite {

  val world: World = null
  val sphere = new Sphere(world)

  test("angles(-i, 0)") {
    val a = -i
    val b = 0
    val (x,y) = sphere.angles(a,b)
    assert( x === π/2   +- 0.000001 ) //ok
    assert( y === 0   +- 0.000001 )   //ko
  }

}
