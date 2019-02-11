package cat.inspiracio.geometry

import cat.inspiracio.complex.π
import org.scalatest.FunSuite

class Vector3Test extends FunSuite {
  import Vector3._
  import math.{cos,sqrt}

  val zero = Vector3(0,0,0)

  // Laws:
  // a dot b === a.abs * b.abs * cos(angle)
  // abs(a) === sqrt(a dot a)
  // cos(angle) ===  (a dot b) / ( abs(a)*abs(b) )

  test("dot "){
    val a: Vector3 = Vector3(0.4, 0.5, 1.2)
    val b: Vector3 = Vector3(1, 0.5, 0.7)
    val d = a dot b
    assert( d === 1.49 )
  }

  test("a dot b === a.abs * b.abs * cos(angle)"){
    val a: Vector3 = Vector3(0.4, 0.5, 1.2)
    val b: Vector3 = Vector3(1, 0.5, 0.7)
    val d = a dot b
    val an = angle(a,b)
    assert( d === a.abs * b.abs * cos(an) )
  }

  test("abs(a) === sqrt(a dot a)"){
    val a: Vector3 = Vector3(0.4, 0.5, 1.2)
    val left = a.abs
    val right = sqrt(a dot a)
    assert( left === right )
  }

  // Laws:
  // a cross b is perpendicular to a and b
  // a cross b === 0 if a==0 or b==b or same direction or opposed

  test("cross perpendicular"){
    val a: Vector3 = Vector3(0.4, 0.5, 1.2)
    val b: Vector3 = Vector3(1, 0.5, 0.7)
    val cr = a x b
    assert( angle(a, cr) === π/2 )
    assert( angle(b, cr) === π/2 )
  }

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

  test("a cross b === 0 if a==0"){
    val a: Vector3 = zero
    val b: Vector3 = Vector3(16, -3.5, 8.1)
    val c = a x b
    assert( c === zero )
  }

  test("a cross b === 0 if b==0"){
    val a: Vector3 = Vector3(-3.4, 2.3, -0.8)
    val b: Vector3 = zero
    val c = a x b
    assert( c === zero )
  }

  test("a cross b === 0 if same direction"){
    val a: Vector3 = Vector3(-3.4, 2.3, -0.8)
    val b: Vector3 = Vector3(-34, 23, -8)
    val c = a x b
    assert( c.abs <= 0.000001  )
  }

  test("a cross b === 0 if opposed"){
    val a: Vector3 = Vector3(-3.4, 2.3, -0.8)
    val b: Vector3 = Vector3(34, -23, 8)
    val c = a x b
    assert( c.abs <= 0.000001 )
  }

}
