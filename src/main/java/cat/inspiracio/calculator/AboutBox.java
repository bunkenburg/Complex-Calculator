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
package cat.inspiracio.calculator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AboutBox extends JDialog {
	
	static final long serialVersionUID = 0;

    public AboutBox(JFrame frame, String title)
    {
        super(frame, title, false);
        setResizable(false);
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent windowevent)
            {
                dispose();
            }

        });
        String text = title + "\n" + "6. 1. 1999\n" + "by Alexander Bunkenburg\n" + "http://www.inspiracio.cat\n";
        JTextArea textarea = new JTextArea(text, 5, 34);//, TextArea.SCROLLBARS_NONE);
        textarea.setEditable(false);
        add("Center", textarea);
        JPanel panel = new JPanel();
        JButton button = new JButton("Continue");
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
        setVisible(true);
    }

}
