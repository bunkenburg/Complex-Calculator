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

import static cat.inspiracio.calculator.Mode.FZ;
import static cat.inspiracio.calculator.Mode.MODFZ;
import static cat.inspiracio.calculator.Interaction.*;

import cat.inspiracio.calculator.Mode;
import cat.inspiracio.complex.Complex;
import cat.inspiracio.complex.Complex$;
import cat.inspiracio.numbers.Circle;
import cat.inspiracio.numbers.ECList;
import cat.inspiracio.numbers.Freeline;
import cat.inspiracio.numbers.Line;
import cat.inspiracio.numbers.Piclet;
import cat.inspiracio.numbers.PicletList;
import cat.inspiracio.numbers.Rectangle;
import cat.inspiracio.numbers.Square;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

final class ZWorld extends World{

	//State ------------------------------------------------

    private JComboBox interactionChoice;
    private JButton eraseButton;
    private Mode mode;

    private FzWorld fzW;
    private ThreeDWorld modfzW;

    private ECList current;
    private Complex start;
    private Complex end;
    private Piclet currentPiclet;
    private PicletList piclets;
    private Square square;

	//Constructor ------------------------------------------

    ZWorld(Calculator calculator){
        super(calculator);
        setTitle("z World");

        square = new Square(0, 1);

        eraseButton = new JButton("Clear");
        eraseButton.addActionListener( e -> erase() );
        buttonPanel.add(eraseButton);

        interaction = DRAW;
        interactionChoice = new JComboBox();
        interactionChoice.addItem("Move");
        interactionChoice.addItem("Square");
        interactionChoice.addItemListener(e -> {
            int state = e.getStateChange();
            if (state != ItemEvent.SELECTED) return;
            String s = (String) e.getItem();
            interaction = Interaction.parse(s);
        });
        buttonPanel.add(interactionChoice);

        MouseAdapter mouseadapter = new MouseAdapter() {

        	@Override public void mousePressed(MouseEvent mouseevent){
                switch(interaction){

                case MOVE:
                    prevx = mouseevent.getX();
                    prevy = mouseevent.getY();
                    mouseevent.consume();
                    return;

                case DRAW:
                    Complex ec = canvas.Point2Complex(mouseevent.getPoint());
                    if(ec != null){
                        resetArg();
                        add(ec);
                        canvas.paint(canvas.getGraphics());
                        return;
                    }
                    break;

                case GRID:
                    Complex ec1 = canvas.Point2Complex(mouseevent.getPoint());
                    if(ec1 != null){
                        start = ec1;
                        end = ec1;
                        addCurrent(new Rectangle(start, end));
                        canvas.paint(canvas.getGraphics());
                        return;
                    }
                    break;

                case LINE:
                    Complex ec2 = canvas.Point2Complex(mouseevent.getPoint());
                    if(ec2 != null){
                        start = ec2;
                        end = ec2;
                        addCurrent(new Line(start, end));
                        canvas.paint(canvas.getGraphics());
                        return;
                    }
                    break;

                case CIRCLE:
                    Complex ec3 = canvas.Point2Complex(mouseevent.getPoint());
                    if(ec3 != null){
                        start = ec3;
                        end = ec3;
                        addCurrent(new Circle(start, end));
                        canvas.paint(canvas.getGraphics());
                        return;
                    }
                    break;

                case RECTANGLE:
                    Complex ec4 = canvas.Point2Complex(mouseevent.getPoint());
                    if(ec4 != null){
                        start = ec4;
                        end = ec4;
                        addCurrent(new Rectangle(start, end));
                        canvas.paint(canvas.getGraphics());
                        return;
                    }
                    break;

                case SQUARE:
                    Complex ec5 = canvas.Point2Complex(mouseevent.getPoint());
                    if(ec5 == null)
                        break;
                    start = ec5;
                    end = ec5;
                    square = new Square(start, end);
                    if(mode ==FZ)
                        addCurrent(square);
                    else if(mode ==MODFZ)
                        modfzW.squareChange(square);
                    canvas.paint(canvas.getGraphics());
                    return;
                }
            }

        	@Override public void mouseReleased(MouseEvent mouseevent){
                switch(interaction){

                case MOVE:
                    int i = mouseevent.getX();
                    int j = mouseevent.getY();
                    canvas.shift(prevx - i, prevy - j);
                    repaint();
                    prevx = i;
                    prevy = j;
                    mouseevent.consume();
                    return;

                case DRAW:
                    if(current != null){
                        Complex ec = canvas.Point2Complex(mouseevent.getPoint());
                        if(ec != null)
                            add(ec);
                        piclets = new PicletList(new Freeline(current), piclets);
                        current = null;
                        canvas.paint(canvas.getGraphics());
                        fzW.stopDynamicMap();
                        return;
                    }
                    break;

                case LINE:
                    if(start != null){
                        Complex ec1 = canvas.Point2Complex(mouseevent.getPoint());
                        if(ec1 != null)
                            end = ec1;
                        add(new Line(start, end));
                        canvas.paint(canvas.getGraphics());
                    }
                    eraseCurrent();
                    return;

                case CIRCLE:
                    if(start != null){
                        Complex ec2 = canvas.Point2Complex(mouseevent.getPoint());
                        if(ec2 != null)
                            end = ec2;
                        add(new Circle(start, end));
                        canvas.paint(canvas.getGraphics());
                    }
                    eraseCurrent();
                    return;

                case RECTANGLE:
                    if(start != null){
                        Complex ec3 = canvas.Point2Complex(mouseevent.getPoint());
                        if(ec3 != null)
                            end = ec3;
                        add(new Rectangle(start, end));
                        canvas.paint(canvas.getGraphics());
                    }
                    eraseCurrent();
                    return;

                case SQUARE:
                    if(start != null){
                        Complex ec4 = canvas.Point2Complex(mouseevent.getPoint());
                        if(ec4 != null)
                            end = ec4;
                        square = new Square(start, end);
                        if(mode ==FZ)
                            add(square);
                        else if(mode ==MODFZ)
                            modfzW.squareChange(square);
                        updateExtremes(square);
                        canvas.paint(canvas.getGraphics());
                    }
                    eraseCurrent();
                    return;

                case GRID:
                    if(start != null){
                        Complex ec5 = canvas.Point2Complex(mouseevent.getPoint());
                        if(ec5 != null)
                            end = ec5;
                        addGrid(new Rectangle(start, end));
                        canvas.paint(canvas.getGraphics());
                    }
                    eraseCurrent();
                    return;
                }
            }
        };

        MouseMotionAdapter mousemotionadapter = new MouseMotionAdapter() {
        	@Override public void mouseDragged(MouseEvent mouseevent){
                switch(interaction){

                case MOVE:
                    int i = mouseevent.getX();
                    int j = mouseevent.getY();
                    canvas.shift(prevx - i, prevy - j);
                    canvas.paint(canvas.getGraphics());
                    prevx = i;
                    prevy = j;
                    mouseevent.consume();
                    return;

                case DRAW:
                    Complex ec = canvas.Point2Complex(mouseevent.getPoint());
                    if(current != null){
                        if(ec != null){
                            add(ec);
                        } else{
                            piclets = new PicletList(new Freeline(current), piclets);
                            current = null;
                            fzW.stopDynamicMap();
                        }
                        canvas.paint(canvas.getGraphics());
                        return;
                    }
                    if(ec != null){
                        resetArg();
                        add(ec);
                        canvas.paint(canvas.getGraphics());
                        return;
                    }
                    break;

                case CIRCLE:
                    Complex ec1 = canvas.Point2Complex(mouseevent.getPoint());
                    if(ec1 != null){
                        end = ec1;
                        addCurrent(new Circle(start, end));
                        canvas.paint(canvas.getGraphics());
                        return;
                    }
                    break;

                case GRID:
                    Complex ec2 = canvas.Point2Complex(mouseevent.getPoint());
                    if(ec2 != null){
                        end = ec2;
                        addCurrent(new Rectangle(start, end));
                        canvas.paint(canvas.getGraphics());
                        return;
                    }
                    break;

                case LINE:
                    Complex ec3 = canvas.Point2Complex(mouseevent.getPoint());
                    if(ec3 != null){
                        end = ec3;
                        addCurrent(new Line(start, end));
                        canvas.paint(canvas.getGraphics());
                        return;
                    }
                    break;

                case RECTANGLE:
                    Complex ec4 = canvas.Point2Complex(mouseevent.getPoint());
                    if(ec4 != null){
                        end = ec4;
                        addCurrent(new Rectangle(start, end));
                        canvas.paint(canvas.getGraphics());
                        return;
                    }
                    break;

                case SQUARE:
                    Complex ec5 = canvas.Point2Complex(mouseevent.getPoint());
                    if(ec5 == null)
                        break;
                    end = ec5;
                    square = new Square(start, end);
                    if(mode ==FZ)
                        addCurrent(square);
                    else if(mode ==MODFZ)
                        modfzW.squareChange(square);
                    canvas.paint(canvas.getGraphics());
                    return;
                }
            }
        };
        plane.addMouseListener(mouseadapter);
        plane.addMouseMotionListener(mousemotionadapter);
        sphere.addMouseListener(mouseadapter);
        sphere.addMouseMotionListener(mousemotionadapter);
        pack();
        setLocationRelativeTo(calculator);
        setVisible(true);
    }

    private void resetArg(){Complex$.MODULE$.resetArg();}

    @Override void add(Complex c){
        updateExtremes(c);
        current = new ECList(c, current);
        fzW.add(c);
    }

    void add(Piclet piclet){
        updateExtremes(piclet);
        piclets = new PicletList(piclet, piclets);
        fzW.add(piclet);
    }

    void addCurrent(Piclet piclet){
        currentPiclet = piclet;
        fzW.addCurrent(piclet);
    }

    void addGrid(Rectangle rectangle){
        double d  = Im( rectangle.topLeft );
        double d1 = Im( rectangle.botLeft );
        double d2 = Re( rectangle.botRight );
        double d3 = Re( rectangle.botLeft );
        double d4 = rectangle.getWidth() / 10D;
        double d5 = rectangle.getHeight() / 10D;
        double d6 = d3;
        double d7 = d1;
        for(int i = 0; i <= 10; i++){
            add(new Line(Cartesian(d6, d), Cartesian(d6, d1)));
            d6 += d4;
            add(new Line( Cartesian(d3, d7), Cartesian(d2, d7)));
            d7 += d5;
        }
    }

    void drawStuff(Drawing drawing){
        if(current != null)
            canvas.drawECList(drawing, current);
        if(currentPiclet != null)
            canvas.drawPiclet(drawing, currentPiclet);
        for(PicletList picletlist = piclets; picletlist != null; picletlist = picletlist.tail())
            canvas.drawPiclet(drawing, picletlist.head());
        if(mode == MODFZ)
            canvas.drawPiclet(drawing, square);
    }

    void erase(){
        eraseCurrent();
        piclets = null;
        resetExtremes();
        if(mode == MODFZ){
            updateExtremes(square);
            return;
        }
        if(fzW != null)
            fzW.erase();
        canvas.repaint();
    }

    void eraseCurrent(){
        current = null;
        currentPiclet = null;
        start = null;
        end = null;
    }

    PicletList getPiclets(){return piclets;}

    Square getSquare(){return square;}

    void setfzWorld(FzWorld w){fzW = w;}

    void setmodfzWorld(ThreeDWorld w){modfzW = w;}

    void setMode(Mode m){
        mode = m;
        switch(mode){

        case FZ:
        	eraseButton.setEnabled(true);
            //interactionChoice.removeAll();
        	//keep interactionChoice.addItem("Move");
        	interactionChoice.addItem("Draw");
        	interactionChoice.addItem("Circle");
        	interactionChoice.addItem("Line");
        	interactionChoice.addItem("Grid");
        	interactionChoice.addItem("Rectangle");
        	//keep interactionChoice.addItem("Square");
        	interactionChoice.setSelectedItem("Draw");
        	interaction=DRAW;
        	break;

        case MODFZ:
        	eraseButton.setEnabled(false);
            //interactionChoice.removeAll();
        	//keep interactionChoice.addItem("Move");
            interactionChoice.removeItem("Draw");
        	//keep interactionChoice.addItem("Square");
            interactionChoice.removeItem("Circle");
            interactionChoice.removeItem("Line");
            interactionChoice.removeItem("Grid");
            interactionChoice.removeItem("Rectangle");
        	interactionChoice.setSelectedItem("Square");
        	interaction=SQUARE;
        	break;
        }
        erase();
        canvas.repaint();
    }

}