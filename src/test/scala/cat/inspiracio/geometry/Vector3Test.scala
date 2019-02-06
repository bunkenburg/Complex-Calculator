package cat.inspiracio.geometry

import org.scalatest.FunSuite
import cat.inspiracio.complex._

class Vector3Test extends FunSuite {

  // centre
  test("polar(0, 0, 0)"){
    val v = Vector3(0,0,0)
    val (m, ax, ay) = v.polar
    assert( m === 0)
    assert( ax === π )  //don't care
    assert( ay === π )  //don't care
  }

  // 1
  test("polar(0.5, 0, 0)"){
    val v = Vector3(0.5, 0, 0)
    val (m, ax, ay) = v.polar
    assert( m === 0.5 )
    assert( ax === π )  //don't care
    assert( ay === π/2 )  // right: positive
  }

  // -i
  test("polar(0, 0, -0.5)"){
    val v = Vector3(0, 0, -0.5)
    val (m, ax, ay) = v.polar
    assert( m === 0.5 )
    assert( ax === 0 )
    assert( ay === 0 )
  }

  // -1
  test("polar(-0.5, 0, 0)"){
    val v = Vector3(-0.5, 0, 0)
    val (m, ax, ay) = v.polar
    assert( m === 0.5 )
    assert( ax === π )  //don't care
    assert( ay === -π/2 ) // left: negative
  }

  // i
  test("polar(0, 0, 0.5)"){
    val v = Vector3(0, 0, 0.5)
    val (m, ax, ay) = v.polar
    assert( m === 0.5 )
    assert( ax === π )  //or -π
    assert( ay === π )  //or -π
  }

  // ∞
  test("polar(0, 0.5, 0)"){
    val v = Vector3(0, 0.5, 0)
    val (m, ax, ay) = v.polar
    assert( m === 0.5 )
    assert( ax === π/2 )  //up: positive
    assert( ay === π )  //don't care
  }

  // 0
  test("polar(0, -0.5, 0)"){
    val v = Vector3(0, -0.5, 0)
    val (m, ax, ay) = v.polar
    assert( m === 0.5 )
    assert( ax === -π/2 ) //down: negative
    assert( ay === π )    //don't care
  }

}
