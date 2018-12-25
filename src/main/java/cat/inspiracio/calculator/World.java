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
 * */
package cat.inspiracio.calculator;

import cat.inspiracio.complex.Complex;
import cat.inspiracio.complex.package$;
import cat.inspiracio.numbers.Piclet;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/** A frame that shows complex number on the complex plane or the Riemann sphere. */
abstract class World extends JFrame {

    //State -------------------------------------------------------------

    //connections ---

    /** serving this calculator */
    protected Calculator calculator;

    //GUI ---

    /** where I show my buttons */
    protected JPanel buttonPanel;

    private JButton zInButton = new JButton("Zoom In");
    private JButton zOutButton = new JButton("Zoom Out");;

    /** selected interaction */
    protected Interaction interaction;

    /** canvas: where I show stuff */
    protected final Plane plane = new Plane(this);
    protected final Sphere sphere = new Sphere(this);
    protected WorldRepresentation canvas;

    //logic ---

    /** the previous mouse position */
    protected int prevx;
    protected int prevy;

    /** maximal displayed stuff */
    double MaxImaginary;
    double MinImaginary;
    double MaxReal;
    double MinReal;

    //Constructor -------------------------------------------------------

    void say(Object o){System.out.println(o);}

    protected World(Calculator c){
        //connections ---

        calculator = c;

        //logic ---

        resetExtremes();

        //gui ---

        canvas = plane;

        zInButton.addActionListener( e -> zoomIn() );
        zOutButton.addActionListener( e -> zoomOut() );

        JComboBox choice = new JComboBox();
        choice.addItem("Plane");
        choice.addItem("Sphere");
        choice.setSelectedItem("Plane");
        choice.addItemListener( e -> {
            int state = e.getStateChange();
            if (state != ItemEvent.SELECTED) return;
            String item = e.getItem().toString();
            switch (item) {
                case "Plane":
                    useSphere();
                    break;
                case "Sphere":
                    usePlane();
                    break;
            }
        });

        JButton button = new JButton("Reset");
        button.addActionListener( e -> reset() );

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

        pack();
        setLocation();
        setVisible(true);
    }

    // event listeners -------------------------

    private void zoomIn(){
        canvas.zoomIn();
        canvas.repaint();
    }

    private void zoomOut(){
        canvas.zoomOut();
        canvas.repaint();
    }

    private void useSphere(){
        remove(sphere);
        add("Center", plane);
        canvas = plane;
        zInButton.setEnabled(true);
        zOutButton.setEnabled(true);
        validate();
        canvas.repaint();
    }

    private void usePlane(){
        remove(plane);
        add("Center", sphere);
        canvas = sphere;
        zInButton.setEnabled(false);
        zOutButton.setEnabled(false);
        validate();
        canvas.repaint();
    }

    private void reset(){
        canvas.reset();
        canvas.repaint();
    }

    private void setLocation(){
        setLocationRelativeTo(this.calculator);
        setLocationByPlatform(true);
    }

    // logic ---------------------

    abstract void add(Complex c);

    abstract void drawStuff(Drawing drawing);

    abstract void erase();

    protected void resetExtremes(){
        MinReal = (1.0D / 0.0D);
        MaxReal = (-1.0D / 0.0D);
        MinImaginary = (1.0D / 0.0D);
        MaxImaginary = (-1.0D / 0.0D);
    }

    public void update(Graphics g){paint(g);}

    protected void updateExtremes(Complex c){
        if( finite(c) ){
            MaxImaginary = Math.max(MaxImaginary, Im(c) );
            MinImaginary = Math.min(MinImaginary, Im(c) );
            MaxReal = Math.max(MaxReal, Re(c) );
            MinReal = Math.min(MinReal, Re(c) );
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

    // helpers -----------------------------------

    protected boolean finite(Complex z){return package$.MODULE$.finite(z);}
    protected double Im(Complex z){ return package$.MODULE$.Im(z); }
    protected double Re(Complex z){ return package$.MODULE$.Re(z); }

    protected Complex Cartesian(double re, double im){
        Complex i = package$.MODULE$.i();
        return i.$times(im).$plus(re);
    }

    protected Complex Real(double re){ return package$.MODULE$.double2Complex(re); }
}