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

import java.awt.event._

import cat.inspiracio.calculator.Interaction._
import cat.inspiracio.calculator.Mode.{FZ, MODFZ}
import cat.inspiracio.complex._
import cat.inspiracio.geometry._
import cat.inspiracio.numbers._
import javax.swing._

final class ZWorld private[calculator](override val calculator: Calculator) extends World(calculator) {

  //State ------------------------------------------------

  private var interactionChoice: JComboBox[String] = null
  private var eraseButton: JButton = null
  private var mode: Mode = null
  private var fzW: FzWorld = null
  private var modfzW: ThreeDWorld = null
  private var current: ECList = null
  private var start: Complex = null
  private var end: Complex = null
  private var currentPiclet: Piclet = null
  private var piclets: PicletList = null
  private var square = Square(0, 1+i)

  init()

  // method ----------------------------------------------

  private def init()= {

    setTitle("z World")

    eraseButton = new JButton("Clear")
    eraseButton.addActionListener((e: ActionEvent) => erase())
    buttonPanel.add(eraseButton)

    interaction = DRAW
    interactionChoice = new JComboBox[String]
    interactionChoice.addItem("Move")
    interactionChoice.addItem("Square")
    interactionChoice.addItemListener( e => {
      val state = e.getStateChange
      if (state == ItemEvent.SELECTED) {
        val s = e.getItem.asInstanceOf[String]
        interaction = Interaction.parse(s)
      }
    })
    buttonPanel.add(interactionChoice)

    val mouseadapter: MouseAdapter = new MouseAdapter() {

      override def mousePressed(mouseevent: MouseEvent): Unit = interaction match {

        case MOVE =>
          prevx = mouseevent.getX
          prevy = mouseevent.getY
          mouseevent.consume()

        case DRAW =>
          val ec = canvas.Point2Complex(mouseevent.getPoint)
          if (ec != null) {
            resetArg()
            add(ec)
            canvas.paint(canvas.getGraphics)
          }

        case GRID =>
          val ec1 = canvas.Point2Complex(mouseevent.getPoint)
          if (ec1 != null) {
            start = ec1
            end = ec1
            addCurrent(Rectangle(start, end))
            canvas.paint(canvas.getGraphics)
          }

        case LINE =>
          val ec2 = canvas.Point2Complex(mouseevent.getPoint)
          if (ec2 != null) {
            start = ec2
            end = ec2
            addCurrent(new Line(start, end))
            canvas.paint(canvas.getGraphics)
          }

        case CIRCLE =>
          val ec3 = canvas.Point2Complex(mouseevent.getPoint)
          if (ec3 != null) {
            start = ec3
            end = ec3
            addCurrent(Circle(start, end))
            canvas.paint(canvas.getGraphics)
          }

        case RECTANGLE =>
          val ec4 = canvas.Point2Complex(mouseevent.getPoint)
          if (ec4 != null) {
            start = ec4
            end = ec4
            addCurrent(Rectangle(start, end))
            canvas.paint(canvas.getGraphics)
          }

        case SQUARE =>
          val ec5 = canvas.Point2Complex(mouseevent.getPoint)
          if (ec5 != null) {
            start = ec5
            end = ec5
            square = Square(start, end)
            if (mode eq FZ) addCurrent(square)
            else if (mode eq MODFZ) modfzW.change(square)
            canvas.paint(canvas.getGraphics)
            return
          }

      }//match

      override def mouseReleased(mouseevent: MouseEvent): Unit = interaction match {

        case MOVE =>
          val i = mouseevent.getX
          val j = mouseevent.getY
          canvas.shift(prevx - i, prevy - j)
          repaint()
          prevx = i
          prevy = j
          mouseevent.consume()

        case DRAW =>
          if (current != null) {
            val ec = canvas.Point2Complex(mouseevent.getPoint)
            if (ec != null) add(ec)
            piclets = new PicletList(Freeline(current), piclets)
            current = null
            canvas.paint(canvas.getGraphics)
            fzW.stopDynamicMap()
          }

        case LINE =>
          if (start != null) {
            val ec1 = canvas.Point2Complex(mouseevent.getPoint)
            if (ec1 != null) end = ec1
            add(new Line(start, end))
            canvas.paint(canvas.getGraphics)
          }
          eraseCurrent()

        case CIRCLE =>
          if (start != null) {
            val ec2 = canvas.Point2Complex(mouseevent.getPoint)
            if (ec2 != null) end = ec2
            add(Circle(start, end))
            canvas.paint(canvas.getGraphics)
          }
          eraseCurrent()

        case RECTANGLE =>
          if (start != null) {
            val ec3 = canvas.Point2Complex(mouseevent.getPoint)
            if (ec3 != null) end = ec3
            add(Rectangle(start, end))
            canvas.paint(canvas.getGraphics)
          }
          eraseCurrent()

        case SQUARE =>
          if (start != null) {
            val ec4 = canvas.Point2Complex(mouseevent.getPoint)
            if (ec4 != null)
              end = ec4
            square = Square(start, end)
            if (mode == FZ)
              add(square)
            else if (mode == MODFZ)
              modfzW.change(square)
            updateExtremes(square)
            canvas.paint(canvas.getGraphics)
          }
          eraseCurrent()

        case GRID =>
          if (start != null) {
            val ec5 = canvas.Point2Complex(mouseevent.getPoint)
            if (ec5 != null) end = ec5
            addGrid(Rectangle(start, end))
            canvas.paint(canvas.getGraphics)
          }
          eraseCurrent()

      }//match

    }//mouseadapter

    val mousemotionadapter: MouseMotionAdapter = new MouseMotionAdapter() {

      override def mouseDragged(mouseevent: MouseEvent): Unit = interaction match {
        case MOVE =>
          val i = mouseevent.getX
          val j = mouseevent.getY
          canvas.shift(prevx - i, prevy - j)
          canvas.paint(canvas.getGraphics)
          prevx = i
          prevy = j
          mouseevent.consume()

        case DRAW =>
          val ec = canvas.Point2Complex(mouseevent.getPoint)
          if (current != null) {
            if (ec != null) add(ec)
            else {
              piclets = new PicletList(Freeline(current), piclets)
              current = null
              fzW.stopDynamicMap()
            }
            canvas.paint(canvas.getGraphics)
          }
          else if (ec != null) {
            resetArg()
            add(ec)
            canvas.paint(canvas.getGraphics)
          }

        case CIRCLE =>
          val ec1 = canvas.Point2Complex(mouseevent.getPoint)
          if (ec1 != null) {
            end = ec1
            addCurrent(Circle(start, end))
            canvas.paint(canvas.getGraphics)
          }

        case GRID =>
          val ec2 = canvas.Point2Complex(mouseevent.getPoint)
          if (ec2 != null) {
            end = ec2
            addCurrent(Rectangle(start, end))
            canvas.paint(canvas.getGraphics)
          }

        case LINE =>
          val ec3 = canvas.Point2Complex(mouseevent.getPoint)
          if (ec3 != null) {
            end = ec3
            addCurrent(new Line(start, end))
            canvas.paint(canvas.getGraphics)
          }

        case RECTANGLE =>
          val ec4 = canvas.Point2Complex(mouseevent.getPoint)
          if (ec4 != null) {
            end = ec4
            addCurrent(Rectangle(start, end))
            canvas.paint(canvas.getGraphics)
          }

        case SQUARE =>
          val ec5 = canvas.Point2Complex(mouseevent.getPoint)
          if (ec5 != null) {
            end = ec5
            square = Square(start, end)
            if (mode eq FZ) addCurrent(square)
            else if (mode eq MODFZ) modfzW.change(square)
            canvas.paint(canvas.getGraphics)
            return
          }

      }//match

    }//MouseMotionAdapter

    plane.addMouseListener(mouseadapter)
    plane.addMouseMotionListener(mousemotionadapter)
    sphere.addMouseListener(mouseadapter)
    sphere.addMouseMotionListener(mousemotionadapter)
    pack()
    setLocationRelativeTo(calculator)
    setVisible(true)

  }//init

      override private[calculator] def add(c: Complex) = {
        updateExtremes(c)
        current = new ECList(c, current)
        fzW.add(c)
      }

      private[calculator] def add(piclet: Piclet) = {
        updateExtremes(piclet)
        piclets = new PicletList(piclet, piclets)
        fzW.add(piclet)
      }

      private[calculator] def addCurrent(piclet: Piclet) = {
        currentPiclet = piclet
        fzW.addCurrent(piclet)
      }

      private[calculator] def addGrid(rectangle: Rectangle) = {
        val d = Im(rectangle.topLeft)
        val d1 = Im(rectangle.botLeft)
        val d2 = Re(rectangle.botRight)
        val d3 = Re(rectangle.botLeft)
        val d4 = rectangle.width / 10
        val d5 = rectangle.height / 10
        var d6 = d3
        var d7 = d1
        var i = 0
        while ( i <= 10 ) {
          add(new Line(Cartesian(d6, d), Cartesian(d6, d1)))
          d6 += d4
          add(new Line(Cartesian(d3, d7), Cartesian(d2, d7)))
          d7 += d5
          i = i+1
        }
      }

      override private[calculator] def drawStuff(drawing: Drawing) = {
        if (current != null)
          canvas.drawECList(drawing, current)
        if (currentPiclet != null)
          canvas.drawPiclet(drawing, currentPiclet)
        var picletlist = piclets
        while ( picletlist != null ) {
          canvas.drawPiclet(drawing, picletlist.head)
          picletlist = picletlist.tail
        }
        if (mode == MODFZ)
          canvas.drawPiclet(drawing, square)
      }

      override private[calculator] def erase(): Unit = {
        eraseCurrent()
        piclets = null
        resetExtremes()
        if (mode == MODFZ) {
          updateExtremes(square)
          return
        }
        if (fzW != null)
          fzW.erase()
        canvas.repaint()
      }

      private[calculator] def eraseCurrent() = {
        current = null
        currentPiclet = null
        start = null
        end = null
      }

      private[calculator] def getPiclets = piclets

      private[calculator] def getSquare = square

      private[calculator] def setfzWorld(w: FzWorld) = fzW = w

      private[calculator] def setmodfzWorld(w: ThreeDWorld) = modfzW = w

      private[calculator] def setMode(m: Mode) = {
        mode = m
        mode match {

          case FZ =>
            eraseButton.setEnabled(true)
            //interactionChoice.removeAll();
            //keep interactionChoice.addItem("Move");
            interactionChoice.addItem("Draw")
            interactionChoice.addItem("Circle")
            interactionChoice.addItem("Line")
            interactionChoice.addItem("Grid")
            interactionChoice.addItem("Rectangle")
            //keep interactionChoice.addItem("Square");
            interactionChoice.setSelectedItem("Draw")
            interaction = DRAW

          case MODFZ =>
            eraseButton.setEnabled(false)
            interactionChoice.removeItem("Draw")
            interactionChoice.removeItem("Circle")
            interactionChoice.removeItem("Line")
            interactionChoice.removeItem("Grid")
            interactionChoice.removeItem("Rectangle")
            interactionChoice.setSelectedItem("Square")
            interaction = SQUARE

          case _ =>
        }
        erase()
        canvas.repaint()
      }

}