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

import cat.inspiracio.numbers.EC;
import cat.inspiracio.numbers.Piclet;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/** A frame that shows complex number on the complex plane or the Riemann sphere. */
abstract class World extends JFrame {

    protected enum Interaction{DRAW, MOVE, LINE, RECTANGLE, CIRCLE, GRID, SQUARE}

    //State -------------------------------------------------------------
	
    protected Calculator calculator;
    protected JPanel buttonPanel;
    protected int prevx;
    protected int prevy;
    protected Interaction interaction;
    double MaxImaginary;
    double MinImaginary;
    double MaxReal;
    double MinReal;
    protected final Plane plane = new Plane(this);
    protected final Sphere sphere = new Sphere(this);
    protected WorldRepresentation canvas;

    //Constructor -------------------------------------------------------
    
	/** Make a new world. 
	 * @param calculator1 In the same application as this calculator. */
    protected World(Calculator calculator1){
        canvas = plane;
        calculator = calculator1;
        resetExtremes();
        final JButton zInButton = new JButton("Zoom In");
        zInButton.addActionListener(new ActionListener(){
        	@Override public void actionPerformed(ActionEvent actionevent){
                canvas.zoomIn();
                canvas.repaint();
            }
        });
        final JButton zOutButton = new JButton("Zoom Out");
        zOutButton.addActionListener(new ActionListener(){
        	@Override public void actionPerformed(ActionEvent actionevent){
                canvas.zoomOut();
                canvas.repaint();
            }
        });
        JComboBox choice = new JComboBox();
        choice.addItem("Plane");
        choice.addItem("Sphere");
        choice.setSelectedItem("Plane");
        choice.addItemListener(new ItemListener(){
        	@Override public void itemStateChanged(ItemEvent itemevent){
        	    int state = itemevent.getStateChange();
        	    if(state!=ItemEvent.SELECTED)
        	        return;
                if(itemevent.getItem() == "Plane"){
                    remove(sphere);
                    add("Center", plane);
                    canvas = plane;
                    zInButton.setEnabled(true);
                    zOutButton.setEnabled(true);
                } else{
                    remove(plane);
                    add("Center", sphere);
                    canvas = sphere;
                    zInButton.setEnabled(false);
                    zOutButton.setEnabled(false);
                }
                validate();
                canvas.repaint();
            }
        });
        JButton button = new JButton("Reset");
        button.addActionListener(new ActionListener() {
        	@Override public void actionPerformed(ActionEvent actionevent){
                canvas.reset();
                canvas.repaint();
            }
        });
        buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.lightGray);
        buttonPanel.setLayout(new FlowLayout(0));
        buttonPanel.add(choice);
        buttonPanel.add(zInButton);
        buttonPanel.add(zOutButton);
        buttonPanel.add(button);
        setLayout(new BorderLayout());
        add("North", buttonPanel);
        add("Center", canvas);
        addWindowListener(new WindowAdapter() {
        	@Override public void windowClosing(WindowEvent windowevent){calculator.quit();}
        });
        //Use the same menu bar as the calculator.
        //We use the same menu bar for all windows.
        JMenuBar mb=this.calculator.makeMenuBar();
        setJMenuBar(mb);
        pack();
        setLocationRelativeTo(this.calculator);
        setLocationByPlatform(true);
        setVisible(true);
    }

    abstract void add(EC ec);

    abstract void drawStuff(Drawing drawing);

    abstract void erase();

    protected void resetExtremes(){
        MinReal = (1.0D / 0.0D);
        MaxReal = (-1.0D / 0.0D);
        MinImaginary = (1.0D / 0.0D);
        MaxImaginary = (-1.0D / 0.0D);
    }

    public void update(Graphics g){paint(g);}

    protected void updateExtremes(EC ec){
        if(ec.finite()){
            MaxImaginary = Math.max(MaxImaginary, ec.im());
            MinImaginary = Math.min(MinImaginary, ec.im());
            MaxReal = Math.max(MaxReal, ec.re());
            MinReal = Math.min(MinReal, ec.re());
        }
    }

    protected void updateExtremes(Piclet piclet){
        MaxImaginary = Math.max(MaxImaginary, piclet.top());
        MinImaginary = Math.min(MinImaginary, piclet.bottom());
        MaxReal = Math.max(MaxReal, piclet.right());
        MinReal = Math.min(MinReal, piclet.left());
    }

    public void setFont(Font font){
        super.setFont(font);
        plane.setFont(font);
        sphere.setFont(font);
    }

}