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
import cat.inspiracio.numbers.ECList;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import static cat.inspiracio.calculator.Interaction.DRAW;

/** The complex world displays results of calculations
 * and allows graphical input of numbers.
 */
final class ComplexWorld extends World{
	
	//State ------------------------------------------------------------------
	
    private ECList numbers;

	//Constructor ------------------------------------------------------------
	
    ComplexWorld(Calculator c){
        super(c);
        setTitle("Complex World");

        //GUI ---

        JButton button = new JButton("Clear");
        button.addActionListener( e -> clear() );
        buttonPanel.add(button);

        JComboBox interactionChoice = new JComboBox();
        interactionChoice.addItem("Draw");
        interactionChoice.addItem("Move");
        interactionChoice.setSelectedItem("Draw");
        interaction = DRAW;
        interactionChoice.addItemListener( e -> {
            int state = e.getStateChange();
        	if( state != ItemEvent.SELECTED ) return;
        	String s = e.getItem().toString();
        	interaction = Interaction.parse(s);
        });
        buttonPanel.add(interactionChoice);

        MouseAdapter mouse = new MouseAdapter() {

        	@Override public void mousePressed(MouseEvent mouseevent){
        	    switch (interaction){
                    case MOVE:
                        prevx = mouseevent.getX();
                        prevy = mouseevent.getY();
                        mouseevent.consume();
                        break;
                    case DRAW:
                        Point p = mouseevent.getPoint();
                        Complex z = canvas.Point2Complex(p);
                        if(z != null){
                            calculator.add(z);
                            add(z);
                        }
                        break;
                }
            }

            @Override public void mouseReleased(MouseEvent mouseevent){
        	    switch (interaction){
                    case MOVE:
                        int i = mouseevent.getX();
                        int j = mouseevent.getY();
                        canvas.shift(prevx - i, prevy - j);
                        canvas.paint(canvas.getGraphics());
                        prevx = i;
                        prevy = j;
                        mouseevent.consume();
                }
            }
        };

        MouseMotionAdapter motion = new MouseMotionAdapter() {
            @Override public void mouseDragged(MouseEvent e){
                switch (interaction){
                    case MOVE:
                        int i = e.getX();
                        int j = e.getY();
                        canvas.shift(prevx - i, prevy - j);
                        canvas.paint(canvas.getGraphics());
                        prevx = i;
                        prevy = j;
                        e.consume();
                }
            }
        };

        plane.addMouseListener(mouse);
        plane.addMouseMotionListener(motion);
        sphere.addMouseListener(mouse);
        sphere.addMouseMotionListener(motion);

        pack();
        setLocation();
        setVisible(true);
    }

    private void setLocation(){
        //setLocationByPlatform(true);
    }

    // event listeners -----------------------------

    private void clear(){
        erase();
        canvas.repaint();
    }

    //Methods --------------------------------------------------------------
    
    @Override void add(Complex c){
        numbers = new ECList(c, numbers);
        updateExtremes(c);
        super.canvas.repaint();
    }

    void drawStuff(Drawing drawing){
        for(ECList eclist = numbers; eclist != null; eclist = eclist.tail())
            super.canvas.drawComplex(drawing, eclist.head());
    }

    void erase(){
        numbers = null;
        resetExtremes();
    }

}