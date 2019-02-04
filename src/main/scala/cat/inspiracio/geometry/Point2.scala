package cat.inspiracio.geometry

import java.awt.Point

/** convenient calculations on 2d Points */
object Point2 {

  implicit def pair2Point2(p: (Int,Int) ) = new Point2(p._1, p._2)
  implicit def point2Point2(p: Point) = new Point2(p.x, p.y)

  def apply(x: Int, y: Int): Point2 = new Point2(x, y)
  def apply(p: Point): Point2 = new Point2(p.x, p.y)
  def unapply(p: Point): Option[(Int, Int)] = Some(p.x, p.y)
}
class Point2(x: Int, y: Int) extends Point(x,y) {

  def + (a: Int, b: Int) = new Point2(x + a, y + b)
  def - (p: Point) = new Point2(x - p.x, y - p.y)

}
