/*	Copyright 2011 Alexander Bunkenburg alex@inspiracio.cat
 *
 * This file is part of Complex Calculator.
 *
 * Complex Calculator is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Complex Calculator is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Complex Calculator. If not, see <http://www.gnu.org/licenses/>.
 * */
package cat.inspiracio.geometry

/** Makes matrices. */
object Matrix44 {
  import math.{cos,sin,sqrt}

  def translate(v: Vector3): Matrix44 = translate(v.x, v.y, v.z)

  def translate(x: Double, y: Double, z: Double): Matrix44 = {
    val m = One.clone()
    m(0,3) = -x
    m(1,3) = -y
    m(2,3) = -z
    m
  }

  /** rotation around x axis. Unused. */
  def Rx(a: Double): Matrix44 = {
    val m = One.clone()
    m(1,1) = cos(a)
    m(1,2) = sin(a)
    m(2,1) = -sin(a)
    m(2,2) = cos(a)
    m
  }

  /** rotation around y axis. Unused. */
  def Ry(a: Double): Matrix44 = {
    val m = One.clone()
    m(0,0) = cos(a)
    m(0,2) = -sin(a)
    m(2,0) = sin(a)
    m(2,2) = cos(a)
    m
  }

  /** rotation around z axis. Unused. */
  def Rz(a: Double): Matrix44 = {
    val m = One.clone()
    m(0,0) = cos(a)
    m(0,1) = sin(a)
    m(1,0) = -sin(a)
    m(1,1) = cos(a)
    m
  }

  /** The matrix to rotate a vector about the z-axis to the xz-plane.
    * https://sites.google.com/site/glennmurray/Home/rotation-matrices-and-formulas/rotation-about-an-arbitrary-axis-in-3-dimensions
    * Unused. */
  def Txz(r: Vector3): Matrix44 = {
    val Vector3(u,v,w) = r
    require( u != 0 || v != 0)
    val m = One.clone()
    val divisor = sqrt(sqr(u)+sqr(v))
    m(0,0) = u/divisor
    m(0,1) = -v/divisor
    m(1,0) = v/divisor
    m(1,1) = u/divisor
    m
  }

  /** The matrix to rotate the vector in the xz-plane to the z-axis.
    * https://sites.google.com/site/glennmurray/Home/rotation-matrices-and-formulas/rotation-about-an-arbitrary-axis-in-3-dimensions
    * Unused. */
  def Tz(r: Vector3): Matrix44 = {
    val Vector3(u,v,w) = r
    require( u != 0 || v != 0)
    val m = One.clone()
    val uv = sqrt(sqr(u) + sqr(v))
    val uvw = sqrt(sqr(u) + sqr(v) + sqr(w))
    m(0,0) = w / uvw
    m(0,2) = uv / uvw
    m(2,0) = -uv / uvw
    m(2,2) = w / uvw
    m
  }

  /** Combines Txz1 * Tz1 * Rz(theta) * Tz * Txz
    * in terms of (u,v,w) and theta,
    * assuming abs(u,v,w) == 1,
    *
    * as in section "5.2 The normalized matrix for rotations about the origin" in
    * https://sites.google.com/site/glennmurray/Home/rotation-matrices-and-formulas/rotation-about-an-arbitrary-axis-in-3-dimensions
    *
    * @param axis Rotation vector. Must be unit vector.
    * @param theta Rotation angle in radians
    * */
  def rotation(axis: Vector3, theta: Double): Matrix44 = {
    val Vector3(u, v, w) = axis

    val c = cos(theta)
    val c1 = 1 - c
    val s = sin(theta)
    val u2 = u * u
    val v2 = v * v
    val w2 = w * w
    val uvc1 = u * v * c1
    val uwc1 = u * w * c1
    val vwc1 = v * w * c1
    val us = u * s
    val vs = v * s
    val ws = w * s

    val M = One.clone()

    M(0,0) = u2 + (1-u2)*c
    M(0,1) = uvc1 + ws
    M(0,2) = uwc1 - vs

    M(1,0) = uvc1 - ws
    M(1,1) = v2 + (1-v2)*c
    M(1,2) = vwc1 + us

    M(2,0) = uwc1 + vs
    M(2,1) = vwc1 - us
    M(2,2) = w2 + (1-w2)*c

    M
  }

  /** Make a matrix from doubles. */
  def apply(
             d00: Double, d01: Double, d02: Double, d03: Double,
             d10: Double, d11: Double, d12: Double, d13: Double,
             d20: Double, d21: Double, d22: Double, d23: Double,
             d30: Double, d31: Double, d32: Double, d33: Double
           ): Matrix44 = {
    val m = new Matrix44()
    m(0,0) = d00
    m(0,1) = d01
    m(0,2) = d02
    m(0,3) = d03
    m(1,0) = d10
    m(1,1) = d11
    m(1,2) = d12
    m(1,3) = d13
    m(2,0) = d20
    m(2,1) = d21
    m(2,2) = d22
    m(2,3) = d23
    m(3,0) = d30
    m(3,1) = d31
    m(3,2) = d32
    m(3,3) = d33
    m
  }

  def unapply(m: Matrix44): Option[(Double, Double, Double, Double,
    Double, Double, Double, Double,
    Double, Double, Double, Double,
    Double, Double, Double, Double)] = Some((
    m(0,0), m(0,1), m(0,2), m(0,3),
    m(1,0), m(1,1), m(1,2), m(1,3),
    m(2,0), m(2,1), m(2,2), m(2,3),
    m(3,0), m(3,1), m(3,2), m(3,3)))

  val Zero: Matrix44 = new Matrix44()

  val One: Matrix44 = {
    val m = new Matrix44()
    for( i <- 0 to 3 )
      m(i,i) = 1
    m
  }

  /** https://en.wikipedia.org/wiki/Determinant */
  def det(M: Matrix44): Double = {

    /** determinant of a 3x3 matrix */
    def det(a: Double, b: Double, c: Double,
            d: Double, e: Double, f: Double,
            g: Double, h: Double, i: Double): Double = a*e*i + b*f*g + c*d*h - c*e*g - b*d*i - a*f*h

    val Matrix44(
    a, b, c, d,
    e, f, g, h,
    i, j, k, l,
    m, n, o, p
    ) = M

    a * det(f, g, h, j, k, l, n, o, p) - b * det(e, g, h, i, k, l, m, o, p) + c * det(e, f, h, i, j, l, m, n, p) - d * det(e, f, g, i, j, k, m, n, o)
  }

  /** https://en.wikipedia.org/wiki/Transpose */
  def tr(A: Matrix44): Matrix44 = {
    val Matrix44(
    a, b, c, d,
    e, f, g, h,
    i, j, k, l,
    m, n, o, p
    ) = A
    Matrix44(
      a, e, i, m,
      b, f, j, n,
      c, g, k, o,
      d, h, l, p
    )
  }

  /** Inverts the matrix.
    *
    * Assumes this 4*4 matrix is really a 3*3 matrix extended
    * with 0 and 1, for graphical operations.
    *
    * The determinant must not be 0, otherwise fail.
    * Assumes the determinant is 1.
    *
    * https://en.wikipedia.org/wiki/Invertible_matrix */
  def invert(M: Matrix44): Matrix44 = {
    val determinant = det(M)

    if(determinant == 0) {
      println(M)
      println(s"det(M) == $determinant == 0")
      require (determinant != 0)
    }

    /** Is the 4x4 matrix M really a 3x3 matrix extended with 0 and 1
      * for graphical operations?
      * I would expect this always to be the case. */
    def is3x3: Boolean = {
      M(3,0)==0 &&
        M(3,1)==0 &&
        M(3,2)==0 &&
        M(3,3)==1 &&
        M(2,3)==0 &&
        M(1,3)==0 &&
        M(0,3)==0
    }

    if(is3x3){

      val Matrix44(
      a, b, c, _,
      d, e, f, _,
      g, h, i, _,
      _, _, _, _
      ) = M

      val A: Double = e*i - f*h
      val B: Double = -(d*i - f*g)
      val C: Double = d*h - e*g
      val D: Double = -(b*i - c*h)
      val E: Double = a*i - c*g
      val F: Double = -(a*h - b*g)
      val G: Double = b*f - c*e
      val H: Double = -(a*f - c*d)
      val I: Double = a*e - b*d
      val detM = a*A + b*B + c*C

      val m = Matrix44(
        A, D, G, 0,
        B, E, H, 0,
        C, F, I, 0,
        0, 0, 0, 1
      )

      m   //real result is m/detM but I assume that detM=1.
    }
    else {
      //Brutally cleans the matrix and tries again.
      println("invert cleans")
      println(M)
      M(3,0) = 0
      M(3,1) = 0
      M(3,2) = 0
      M(3,3) = 1
      M(2,3) = 0
      M(1,3) = 0
      M(0,3) = 0
      invert(M)

      //Cayley-Hamilton method
      //Not sure whether this is correct, because it gives invert(One) == Zero.
      //val trA = tr(A)
      //val A2 = A.sqr
      //val A3 = A.cube
      //val trA2 = tr(A2)
      //val one: Matrix44 = ( trA.cube - trA * trA2 * 3 + tr(A.cube) * 2 ) / 6
      //val two: Matrix44 = A * ( trA.sqr - trA2 ) / 2
      //val block = one - two + A2 * trA - A3
      //block / det(A)
    }
  }

  // helpers --------------------------------------------

  def sqr(d: Double): Double = d*d
}

/** 4*4 matrices for 3d graphical transformations.
  * These matrices are immutable. */
final class Matrix44 {
  import math.{cos,sin}

  private val data = Array(
    new Array[Double](4),
    new Array[Double](4),
    new Array[Double](4),
    new Array[Double](4)
  )

  /** look up a value in the matrix */
  def apply(x: Int, y: Int): Double = data(x)(y)

  /** This makes matrices mutable.
    * Therefore it is private to Matrix44.
    * From the outside, matrices are immutable and inside, we have to be careful. */
  private def update(x: Int, y: Int, v: Double) = data(x)(y) = v

  /** Since matrices are immutable, a client programmer should never need this. */
  override def clone(): Matrix44 = {
    val m = new Matrix44()
    for( x <- 0 to 3; y <- 0 to 3 )
      m(x,y) = this(x,y)
    m
  }

  /** With exact equality for doubles. */
  override def equals(other: Any): Boolean = other match {
    case null => false
    case m: Matrix44 => {
      this(0,0)==m(0,0) && this(0,1)==m(0,1) && this(0,2)==m(0,2) && this(0,3)==m(0,3) &&
        this(1,0)==m(1,0) && this(1,1)==m(1,1) && this(1,2)==m(1,2) && this(1,3)==m(1,3) &&
        this(2,0)==m(2,0) && this(2,1)==m(2,1) && this(2,2)==m(2,2) && this(2,3)==m(2,3) &&
        this(3,0)==m(3,0) && this(3,1)==m(3,1) && this(3,2)==m(3,2) && this(3,3)==m(3,3)
    }
    case _ => false
  }

  override def hashCode(): Int = data.##

  def * (x: Double, y: Double, z: Double): Vector3 =
    Vector3(
      data(0)(0) * x + data(0)(1) * y + data(0)(2) * z + data(0)(3),
      data(1)(0) * x + data(1)(1) * y + data(1)(2) * z + data(1)(3),
      data(2)(0) * x + data(2)(1) * y + data(2)(2) * z + data(2)(3)
    )

  def * ( v: Vector3 ): Vector3 = this * (v.x, v.y, v.z)

  def * ( v: (Double, Double, Double) ): Vector3 = this * (v._1, v._2, v._3)

  def * (b: Matrix44): Matrix44 = {
    val a = this
    val m = new Matrix44()
    for(x <- 0 to 3; y <- 0 to 3){
      var sum = 0.0
      for(k <- 0 to 3)
        sum += a(x, k)*b(k,y)
      m(x,y) = sum
    }
    m
  }

  def * (x: Double): Matrix44 = {
    val Matrix44(
    a, b, c, d,
    e, f, g, h,
    i, j, k, l,
    m, n, o, p
    ) = this
    Matrix44(
      a*x, b*x, c*x, d*x,
      e*x, f*x, g*x, h*x,
      i*x, j*x, k*x, l*x,
      m*x, n*x, o*x, p*x
    )
  }

  def / (x: Double): Matrix44 = {
    require{ x!=0 }
    val Matrix44(
    a, b, c, d,
    e, f, g, h,
    i, j, k, l,
    m, n, o, p
    ) = this
    Matrix44(
      a/x, b/x, c/x, d/x,
      e/x, f/x, g/x, h/x,
      i/x, j/x, k/x, l/x,
      m/x, n/x, o/x, p/x
    )
  }

  def + (b: Matrix44): Matrix44 = {
    val a = this
    val m = new Matrix44()
    for(x <- 0 to 3; y <- 0 to 3){
      for(k <- 0 to 3)
        m(x,y) = a(x, y) + b(x,y)
    }
    m
  }

  def - (b: Matrix44): Matrix44 = {
    val a = this
    val m = new Matrix44()
    for(x <- 0 to 3; y <- 0 to 3){
      for(k <- 0 to 3)
        m(x,y) = a(x, y) - b(x,y)
    }
    m
  }

  /** Puts a rotation on axis by angle before this matrix. */
  def preRot(axis: Char, angle: Double): Matrix44 = {

    val b = axis match {
      case 'x' => 1
      case 'y' => 2
      case 'z' => 0
      case _ => throw new IllegalArgumentException("preRot(" + axis + "," + angle + ")")
    }

    val i = (b + 1) % 3

    val r: Matrix44 = clone()

    for ( j <- 0 to 3 ) {
      val d3 = cos(angle) * r.data(b)(j) + sin(angle) * r.data(i)(j)
      val d4 = cos(angle) * r.data(i)(j) - sin(angle) * r.data(b)(j)
      r.data(b)(j) = d3
      r.data(i)(j) = d4
    }

    r
  }

  /** Puts a rotation on axis by angle after this matrix. */
  def postRot(axis: Char, angle: Double): Matrix44 = {

    val b = axis match {
      case 'x' => 1
      case 'y' => 2
      case 'z' => 0
      case _ => throw new IllegalArgumentException("postRot(" + axis + "," + angle + ")")
    }

    val i = (b + 1) % 3

    val r: Matrix44 = clone()

    for ( j <- 0 to 3 ) {
      val d3 = cos(angle) * r.data(j)(b) - sin(angle) * r.data(j)(i)
      val d4 = sin(angle) * r.data(j)(b) + cos(angle) * r.data(j)(i)
      r.data(j)(b) = d3
      r.data(j)(i) = d4
    }

    r
  }

  def sqr: Matrix44 = this * this
  def cube: Matrix44 = this * this * this

  override def toString: String = "\n" +
    "[" + data(0)(0) + ", " + data(0)(1) + ", " + data(0)(2) + ", " + data(0)(3) + "\n" +
    " " + data(1)(0) + ", " + data(1)(1) + ", " + data(1)(2) + ", " + data(1)(3) + "\n" +
    " " + data(2)(0) + ", " + data(2)(1) + ", " + data(2)(2) + ", " + data(2)(3) + "\n" +
    " " + data(3)(0) + ", " + data(3)(1) + ", " + data(3)(2) + ", " + data(3)(3) + "]"

}
