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
package cat.inspiracio.say;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Say extends Frame
    implements ActionListener
{
	static final long serialVersionUID = 0;

    public Say(String s)
    {
        super("Say...");
        add("Center", new Label(s));
        Panel panel = new Panel();
        QuitButton = new Button("Quit");
        panel.add(QuitButton);
        ContinueButton = new Button("Continue");
        panel.add(ContinueButton);
        add("South", panel);
        ContinueButton.addActionListener(this);
        QuitButton.addActionListener(this);
        pack();
        this.setVisible(true);
    }

    public Say(Frame frame, String s, boolean flag)
    {
        this(s);
    }

    public void actionPerformed(ActionEvent actionevent)
    {
        Object obj = actionevent.getSource();
        if(obj instanceof Button)
        {
            actionevent.getActionCommand();
            Button button = (Button)obj;
            if(button.equals(ContinueButton))
            {
                dispose();
                return;
            }
            if(button.equals(QuitButton))
            {
                quit();
                return;
            }
        } else
        if(obj instanceof MenuItem)
        {
            //MenuItem _tmp = (MenuItem)obj;
            actionevent.getActionCommand();
        }
    }

    public static void main(String args[])
    {
        new Say("Testing: here goes the message");
    }

    public void quit()
    {
        System.exit(0);
    }

    //private final boolean inAnApplet = false;
    //private Frame parent;
    private Button QuitButton;
    //private final String QuitString = "Quit";
    private Button ContinueButton;
    //private final String ContinueString = "Continue";
}
