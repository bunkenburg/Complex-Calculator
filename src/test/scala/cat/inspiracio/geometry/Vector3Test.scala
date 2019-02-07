package cat.inspiracio.geometry

import cat.inspiracio.complex.π
import org.scalatest.FunSuite

class Vector3Test extends FunSuite {
  import Vector3._

  // Laws:
  // a dot b === a.abs * b.abs * cos(angle)
  // abs(a) === sqrt(a dot a)
  // cos(angle) ===  (a dot b) / ( abs(a)*abs(b) )

  // Laws:
  // a cross b is perpendicular to a and b
  // a cross b === 0 if a==0 or b==b or same direction or opposed

  test("angle(inf, inf)"){
    val a: Vector3 = Vector3(0, 0.5, 0)
    val b: Vector3 = Vector3(0, 0.5, 0)
    val an = angle(a,b)
    assert( an === 0 )
  }

  test("angle(inf, 1)"){
    val a: Vector3 = Vector3(0, 0.5, 0)
    val b: Vector3 = Vector3(0.5, 0, 0)
    val an = angle(a,b)
    assert( an === π/2 )
  }

  test("angle(inf, 0)"){
    val a: Vector3 = Vector3(0, 0.5, 0)
    val b: Vector3 = Vector3(0, -0.5, 0)
    val an = angle(a,b)
    assert( an === π )
  }

}
