/*	Copyright 2011 Alexander Bunkenburg alex@cat.inspiracio.com
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

import javax.swing.text.{SimpleAttributeSet, StyleConstants}
import scala.swing._

/** Modal dialog from "About" */
class About(val frame: Frame) extends Dialog(frame) {
  title = "About ..."
  modal = true

  fill()
  ready()

  private def fill(): Unit ={
    contents = new BorderPanel {
      val t = text()
      layout(t) = BorderPanel.Position.Center

      val button = new Button("Continue") { dispose() }

      //in order to focus on the button automatically. Different?
      //val root = getRootPane
      //root.setDefaultButton(button)

      //why this?
      //val panel = new Panel { contents += button }
      //layout(panel) = BorderPanel.Position.South

      layout(button) = BorderPanel.Position.South
    }
  }

  private def text(): Component = {
    val s = "\nComplex Calculator\n" + "6. 1. 2019\n" + "by Alexander Bunkenburg\n" + "http://www.inspiracio.cat\n"

    val textpane = new TextPane {
      text = s
      editable = false
      focusable = false //necessary so that enter and space dismiss dialog
    }

    //align pane
    //https://stackoverflow.com/questions/3213045/centering-text-in-a-jtextarea-or-jtextpane-horizontal-text-alignment
    //val doc = textpane.getStyledDocument
    //val center = new SimpleAttributeSet
    //StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER)
    //doc.setParagraphAttributes(0, doc.getLength, center, false)

    textpane
  }

  private def ready(): Unit ={
    resizable = false
    pack()
    setLocationRelativeTo(frame)
    visible = true
  }

}