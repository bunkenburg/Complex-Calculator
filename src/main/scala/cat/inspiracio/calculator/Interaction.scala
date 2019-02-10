package cat.inspiracio.calculator

object Interaction extends Enumeration {
  type Interaction = Value

  val DRAW = Value("Draw")
  val MOVE = Value("Move")
  val LINE = Value("Line")
  val RECTANGLE = Value("Rectangle")
  val CIRCLE = Value("Circle")
  val GRID = Value("Grid")
  val SQUARE = Value("Square")

}
