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
 * *//*	Copyright 2011 Alexander Bunkenburg alex@cat.inspiracio.com
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

import javax.swing._
import javax.swing.text.{SimpleAttributeSet, StyleConstants}

@SerialVersionUID(0)
class About(val frame: JFrame) extends JDialog(frame, "About ...", true) {
  setResizable(false)
  addWindowListener(new WindowAdapter() {
    override def windowClosing(windowevent: WindowEvent): Unit = dispose()
  })
  add("Center", pane)
  val panel = new JPanel
  val button = new JButton("Continue")
  button.addActionListener((actionevent: ActionEvent) => dispose())
  panel.add(button)
  add("South", panel)
  button.requestFocus()
  pack()
  setLocationRelativeTo(frame)
  setVisible(true)

  private def pane = {
    val text = "\nComplex Calculator\n" + "6. 1. 1999\n" + "by Alexander Bunkenburg\n" + "http://www.inspiracio.cat\n"
    val textpane = new JTextPane //, 5, 34);
    textpane.setText(text)
    textpane.setEditable(false)
    //align pane
    //https://stackoverflow.com/questions/3213045/centering-text-in-a-jtextarea-or-jtextpane-horizontal-text-alignment
    val doc = textpane.getStyledDocument
    val center = new SimpleAttributeSet
    StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER)
    doc.setParagraphAttributes(0, doc.getLength, center, false)
    textpane
  }
}