/*	Copyright 2011 Alexander Bunkenburg alex@inspiracio.com
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
package inspiracio.calculator;

import java.awt.*;
import java.awt.event.*;

public class AboutBox extends Dialog {
	
	static final long serialVersionUID = 0;

    public AboutBox(Frame frame, String s)
    {
        super(frame, "About " + s + "...", false);
        setResizable(false);
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent windowevent)
            {
                dispose();
            }

        });
        String s1 = s + "\n" + "6. 1. 1999\n" + "by Alex Bunkenburg\n" + "http://www.dcs.gla.ac.uk/~bunkenba\n";
        TextArea textarea = new TextArea(s1, 5, 34, 3);
        textarea.setEditable(false);
        add("Center", textarea);
        Panel panel = new Panel();
        Button button = new Button("Continue");
        button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent)
            {
                dispose();
            }

        });
        panel.add(button);
        add("South", panel);
        button.requestFocus();
        pack();
        this.setVisible(true);
    }

    public static void main(String args[])
    {
        Frame frame = new Frame("Parent frame");
        frame.setSize(50, 30);
        frame.setVisible(true);
        new AboutBox(frame, "ProgramName");
    }
}
