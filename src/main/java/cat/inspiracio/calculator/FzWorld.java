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
import cat.inspiracio.complex.Complex$;
import cat.inspiracio.numbers.*;
import cat.inspiracio.parsing.SyntaxTree;

import java.awt.event.*;

import static cat.inspiracio.calculator.Interaction.MOVE;

final class FzWorld extends World {

    // connections -----------------------------

    private ZWorld zW;

	//State --------------------------------------------------------------------
	
    private ECList current;
    private PicletList piclets;
    private SyntaxTree f;

	//Constructor --------------------------------------------------------------

    FzWorld(Calculator calculator){
        super(calculator);
        setTitle("f(z) World");

        // GUI --------------------

        super.interaction = MOVE;

        MouseAdapter mouse = new MouseAdapter() {

            @Override public void mousePressed(MouseEvent mouseevent){
                prevx = mouseevent.getX();
                prevy = mouseevent.getY();
                mouseevent.consume();
            }

            @Override public void mouseReleased(MouseEvent mouseevent){
                int i = mouseevent.getX();
                int j = mouseevent.getY();
                canvas.shift(prevx - i, prevy - j);
                canvas.paint(canvas.getGraphics());
                prevx = i;
                prevy = j;
                mouseevent.consume();
            }
        };

        MouseMotionAdapter motion = new MouseMotionAdapter() {

            @Override public void mouseDragged(MouseEvent mouseevent){
                int i = mouseevent.getX();
                int j = mouseevent.getY();
                canvas.shift(prevx - i, prevy - j);
                canvas.paint(canvas.getGraphics());
                prevx = i;
                prevy = j;
                mouseevent.consume();
            }

        };

        plane.addMouseListener(mouse);
        plane.addMouseMotionListener(motion);
        sphere.addMouseListener(mouse);
        sphere.addMouseMotionListener(motion);
        pack();
        setLocationRelativeTo(calculator);
        setVisible(true);
    }

    private void setLocation(){
        setLocationRelativeTo(this.calculator);
    }

    //Methods ------------------------------------------------------------
    
    @Override void add(Complex c){
        if(f != null){
            try{
                Complex z = f.evaluate(c);
                current = new ECList(z, current);
                updateExtremes(z);
            }
            catch(Exception _ex) { }
            canvas.paint(canvas.getGraphics());
        }
    }

    void add(Piclet piclet){
        if(f != null){
            resetArg();
            ECList samples = piclet.getSamples();
            current = null;
            for(; samples != null; samples = samples.tail() )
                try{
                    Complex z = samples.head();
                    Complex c = f.evaluate(z);
                    updateExtremes(c);
                    current = new ECList(c, current);
                }
                catch(Exception _ex) { }
            stopDynamicMap();
            super.canvas.repaint();
        }
    }

    void add(PicletList ps){
        for(; ps != null; ps = ps.tail())
            add(ps.head());
    }

    void addCurrent(Piclet piclet){
        if(f != null){
            resetArg();
            current = null;
            for(ECList samples = piclet.getSamples(); samples != null; samples = samples.tail())
                try{
                    Complex z = samples.head();
                    Complex c = f.evaluate(z);
                    current = new ECList(c, current);
                }
                catch(Exception _ex) { }
            canvas.paint(canvas.getGraphics());
        }
    }

    private void resetArg(){Complex$.MODULE$.resetArg();}

    final void drawStuff(Drawing drawing){
        if(current != null)
            canvas.drawECList(drawing, current);
        for(PicletList ps = piclets; ps != null; ps = ps.tail())
            canvas.drawPiclet(drawing, ps.head());
    }

    void erase(){
        current = null;
        piclets = null;
        resetExtremes();
        canvas.repaint();
    }

    void functionChange(SyntaxTree syntaxtree){
        f = syntaxtree;
        erase();
        add(zW.getPiclets());
    }

    void setzWorld(ZWorld zworld){zW = zworld;}

    void stopDynamicMap(){
        piclets = new PicletList(new Freeline(current), piclets);
        current = null;
    }

}