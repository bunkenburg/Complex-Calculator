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

import static cat.inspiracio.calculator.World.Interaction.MOVE;

import cat.inspiracio.complex.Complex;
import cat.inspiracio.complex.Complex$;
import cat.inspiracio.numbers.*;
import cat.inspiracio.parsing.SyntaxTree;

import java.awt.event.*;

final class FzWorld extends World {

	//State --------------------------------------------------------------------
	
    private ECList current;
    private PicletList piclets;
    private SyntaxTree f;
    private ZWorld zW;

	//Constructor --------------------------------------------------------------

	/** Makes a new f(z) world.
	 * @param calculator Connected to this calculator. */
    FzWorld(Calculator calculator){
        super(calculator);
        super.interaction =MOVE;
        setTitle("f(z) World");

        MouseAdapter mouseadapter = new MouseAdapter() {
            public void mousePressed(MouseEvent mouseevent){
                prevx = mouseevent.getX();
                prevy = mouseevent.getY();
                mouseevent.consume();
            }
            public void mouseReleased(MouseEvent mouseevent){
                int i = mouseevent.getX();
                int j = mouseevent.getY();
                canvas.shift(prevx - i, prevy - j);
                canvas.paint(canvas.getGraphics());
                prevx = i;
                prevy = j;
                mouseevent.consume();
            }
        };
        MouseMotionAdapter mousemotionadapter = new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent mouseevent){
                int i = mouseevent.getX();
                int j = mouseevent.getY();
                canvas.shift(prevx - i, prevy - j);
                canvas.paint(canvas.getGraphics());
                prevx = i;
                prevy = j;
                mouseevent.consume();
            }
        };
        super.plane.addMouseListener(mouseadapter);
        super.plane.addMouseMotionListener(mousemotionadapter);
        super.sphere.addMouseListener(mouseadapter);
        super.sphere.addMouseMotionListener(mousemotionadapter);
        pack();
        setLocationRelativeTo(this.calculator);
        setVisible(true);
    }

    private void setLocation(){
        setLocationRelativeTo(this.calculator);
    }

    //Methods ------------------------------------------------------------
    
    @Override void add(Complex c){
        if(f != null){
            try{
                Complex ec1 = f.evaluate(c);
                current = new ECList(ec1, current);
                updateExtremes(ec1);
            }
            catch(Exception _ex) { }
            super.canvas.paint(super.canvas.getGraphics());
        }
    }

    void add(Piclet piclet){
        if(f != null){
            //EC.resetArg();
            Complex$.MODULE$.resetArg();
            ECList eclist = piclet.getSamples();
            current = null;
            for(; eclist != null; eclist = eclist.tail())
                try{
                    Complex c = f.evaluate(eclist.head());
                    updateExtremes(c);
                    current = new ECList(c, current);
                }
                catch(Exception _ex) { }
            stopDynamicMap();
            super.canvas.repaint();
        }
    }

    void add(PicletList picletlist){
        for(; picletlist != null; picletlist = picletlist.tail())
            add(picletlist.head());
    }

    void addCurrent(Piclet piclet){
        if(f != null){
            //EC.resetArg();
            Complex$.MODULE$.resetArg();
            current = null;
            for(ECList eclist = piclet.getSamples(); eclist != null; eclist = eclist.tail())
                try{
                    Complex c = f.evaluate(eclist.head());
                    current = new ECList(c, current);
                }
                catch(Exception _ex) { }
            super.canvas.paint(super.canvas.getGraphics());
        }
    }

    final void drawStuff(Drawing drawing){
        if(current != null)
            super.canvas.drawECList(drawing, current);
        for(PicletList picletlist = piclets; picletlist != null; picletlist = picletlist.tail())
            super.canvas.drawPiclet(drawing, picletlist.head());
    }

    void erase(){
        current = null;
        piclets = null;
        resetExtremes();
        super.canvas.repaint();
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