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
 * *//*	Copyright 2011 Alexander Bunkenburg alex@inspiracio.cat
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
package cat.inspiracio.calculator

import java.awt.{Dimension, Font, Graphics, Point}

import cat.inspiracio.complex._
import cat.inspiracio.geometry.Matrix44.{Rz, Txz, Tz, invert}
import cat.inspiracio.geometry.{Matrix44, Point2, Vector3}

// Referenced classes of package bunkenba.calculator:
//            WorldRepresentation, DoubleBuffer, Drawing, Matrix44,
//            Vector3, World

/** A world of complex numbers on the Riemann sphere. */
final class Sphere private[calculator](val world: World) extends WorldRepresentation(world) {
  import MoreGraphics.GraphicsExtended
  import Point2._

  //State -------------------------------------------------------

  private var markLength = 2

  private var R = Matrix44.One
  private var R1 = Matrix44.One

  private var factor: Double = 0

  //Methods ------------------------------------------------------

  override private[calculator] def draw(g: Graphics, c: Complex) = {
    isFrontC(c).foreach{ point =>
      g.drawCross( point, markLength )
      g.drawString(c.toString, point.x+2, point.y+2 )
    }
  }

  override private[calculator] def draw(g: Graphics, zs: List[Complex]) =
    if ( zs != null ) {
      def draw: List[Option[Point]] => Unit = {
        case Nil => ()
        case Some(a) :: Nil => ()
        case None :: xs => draw(xs)
        case Some(a) :: None :: xs => draw(xs)
        case Some(a) :: Some(b) :: xs => { g.drawLine(a, b); draw( Some(b)::xs ) }
      }
      draw(zs map isFrontC)
    }

  /** Maps 3d space to Complex */
  private def f3dC(v: Vector3): Complex =
    if (v.y == 0.5 ) ∞
    else if (v.y == -0.5 ) 0
    else Cartesian(v.x / (0.5 - v.y), v.z / (0.5 - v.y))

  /** Maps a complex number to pixel space.
    * If it is visible on the front, returns the Point,
    * else None. */
  private def isFrontC(c: Complex): Option[Point] = {
    val v = fC3d(c)               // 3d space
    val Vector3(x, y, z) = R * v  // to 3d view space
    if ( z <= 0 )                 // front means z <= 0
      Some( f2dPoint(x, y) )      // to pixel space
    else
      None
  }

  /** Maps (x, y), the first two coordinates of 3d view space, to pixel Point. */
  private def f2dPoint(x: Double, y: Double): Point = {
    Point2(
      (x * factor + width * 0.5).toInt,
      (-y * factor + height * 0.5).toInt
    )
  }

  /** Maps Complex to 3d space */
  private def fC3d(c: Complex): (Double, Double, Double) =
    if (finite(c)) {
      val Cartesian(re, im) = c
      val a = sqr(re) + sqr(im) //sqr(abs(c))
      val d = 1 + a
      (
        re/d,
        a/d - 0.5,
        im/d
      )
    }
    else (0, 0.5, 0)

  /** Called by swing */
  override def paint(g: Graphics): Unit = {
    factor = Math.min(width, height) * 0.8
    g.drawCircle(width / 2, height / 2, 0.5 * factor)
    List[Complex](0, ∞, 1, -1, i, -i ).foreach{ draw(g, _) }
    w.draw(g) //tell the world to draw its stuff (numbers, pictlets, ...)
  }

  /** Maps a point to a complex number, if the point is on the sphere, else None. */
  override private[calculator] def point2Complex(p: Point): Option[Complex] = {
    //inverse to f2dPoint
    val x = (p.x - width * 0.5) / factor
    val y = (height * 0.5 - p.y) / factor
    val z = 0.25 - sqr(x) - sqr(y)

    if ( 0 <= z ) {
      val v = R1 * (x, y, -sqrt(z) )  // from 3d view space to 3d Riemann space
      val c = f3dC(v)                 // from 3d Riemann space to complex number
      Some(c)
    }
    else
      None
  }

  override private[calculator] def reset() = {
    R = Matrix44.One
    R1 = Matrix44.One
    repaint()
  }

  override def setFont(font: Font): Unit = {
    super.setFont(font)
    val i = getFontMetrics(font).getAscent
    markLength = i / 5
  }

  /** Takes two complex numbers on the sphere and returns the
    * x-angle and the y-angle between them.
    * The x-angle is the angle around the x-axis.
    * Angles in radians.
    * @deprecated */
  def angles(a: Complex, b: Complex): (Double, Double) = {
    //to 3d space
    val a3 = Vector3(fC3d(a))
    val b3 = Vector3(fC3d(b))

    //to 3d view space
    val av3 = R * a3
    val bv3 = R * b3

    //make cv3
    //Let's pretend this 3d view space is 3d space
    val apretend = f3dC(av3)
    val bpretend = f3dC(bv3)
    val Polar(_, alpha) = apretend
    val Polar(size, _) = bpretend
    val cpretend = Polar(size, alpha)
    val cv3 = Vector3(fC3d(cpretend))

    // radians
    //XXX Still not good. Does not distinguish orientation.
    //XXX Also, the values are not right.
    val x = asin(2 * (av3-cv3).abs ) //0.toRadians //π
    val y = asin(2 * (bv3-cv3).abs ) //10.toRadians
    (x, y)
  }

  /** The R1 matrix as it was at the start of shifting. */
  private var startR1: Matrix44 = _

  override private[calculator] def startShift(start: Point) = {
    // If the start point is not on the sphere, no shifting.
    startR1 = R1
  }

  override private[calculator] def shift(startPoint: Point, previousPoint: Point, endPoint: Point) = {
    //guess(startPoint,endPoint)
    sixth(startPoint,endPoint)
  }

  /** This is the correct implementation. There are a lot of calculations here.
    * https://sites.google.com/site/glennmurray/Home/rotation-matrices-and-formulas/rotation-about-an-arbitrary-axis-in-3-dimensions
    * */
  private def sixth(startPoint: Point, endPoint: Point) = {
    import Matrix44._
    import Vector3.{angle,unit}

    if(startPoint != endPoint){
      f3dViewSpace(startPoint).foreach{ a =>
        f3dViewSpace(endPoint).foreach{ b =>

          //find rotation angle
          val theta = angle(a, b) // May be very small ... like 0.007

          //find rotation axis
          //Optimise: can we have abs(axis)=1 ?
          //... then simplify multiplications
          val axis = unit(a x b)
          println(s"axis=$axis abs(axis) = ${axis.abs}")  // approx. abs(axis) == 1
          val Vector3(u, v, w) = axis

          if (u == 0 && v == 0) {
            //special case: axis is already z-axis
            R1 = startR1 * Rz(theta)
            R = invert(R1)
          }
          else {

            //(2) Rotate space about the z axis so that the rotation axis lies in the xz plane.
            val tz = Tz(axis)

            //(3) Rotate space about the y axis so that the rotation axis lies along the z axis.
            val txz = Txz(axis)

            //(4) Perform the desired rotation by θ about the z axis.
            val rz = Rz(theta)

            val txz1 = invert(txz)
            val tz1 = invert(tz)

            R1 = startR1 * txz1 * tz1 * rz * tz * txz
            R = invert(R1)
          }
          repaint()
        }
      }
    }//if
  }

  /*
  private def guess(from: Point, to: Point) = {
    f3dViewSpace(from).foreach{ a =>
      f3dViewSpace(to).foreach{ b =>
        //This guessed solution is quite good.
        //XXX Still wrong: x-rotation and y-rotation are not commutative!
        //The more you drag, the more the sphere twists out of shape.
        val dy = (to.y-from.y)/factor
        val dx = (to.x-from.x)/factor
        val x = math.tan(dy) * 2.5 //slow: 2, fast: π
        val y = math.tan(dx) * 2.5
        rotate(x,y)
      }
    }
  }
  */

    /** Maps Point to 3d view space. Similar to point2Complex. */
    def f3dViewSpace(p: Point): Option[Vector3] = {
      val x = (p.x - width * 0.5) / factor
      val y = (p.y - height * 0.5) / factor //I would have thought this should be negative.
      val z = 0.25 - sqr(x) - sqr(y)
      if ( 0 <= z ) Some(Vector3(x, y, -sqrt(z) ))  //front?
      else None
    }

  private def third(ap: Point, bp: Point) =
    point2Complex(ap).foreach{ a =>
      point2Complex(bp).foreach{ b =>

        /* Third attempt:
        The number that was at ap should be at bp,
        only by rotate(x,y).

        We want:
        Choose (x,y) so that

        rotate(x,y)
        val ar: Complex = point2Complex(bp)
        assert( a === ar )
        * */

        val saveR = R
        val saveR1 = R1

        var (xbest,ybest) = (0,0)
        var delta = Double.PositiveInfinity
        for( x <- -179 to 180; y <- -179 to 180){
          //try (x,y)
          rotate(x.toRadians, y.toRadians)
          val ar: Complex = point2Complex(bp).get
          val distance = abs(a - ar)
          if(distance < delta){
            xbest = x
            ybest = y
            delta = distance
          }
        }

        R = saveR
        R1 = saveR1
        rotate(xbest.toRadians, ybest.toRadians)
      }
    }

  def traditional(from: Point, to: Point) = {
    val p: Point = to  - from
    val x = p.y * 2 * π / width
    val y = p.x * 2 * π / height
    //val x = π/4 // π/2: infinity comes forward, turns clockwise, seen from right
    //val y = 0 // π/2: -1 comes forward, turns clockwise, seen from above
    (x,y)
  }

  private def rotate(x: Double, y: Double) = {
    R = R.preRot('x', x)
    R = R.preRot('y', y)

    R1 = R1.postRot('x', -x)
    R1 = R1.postRot('y', -y)

    //They are multiplicative inverses.
    //println(R * R1)
    //println(R1 * R)

    repaint()
  }

}