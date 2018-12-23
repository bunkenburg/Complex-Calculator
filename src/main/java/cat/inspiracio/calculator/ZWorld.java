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

import static cat.inspiracio.calculator.Calculator.Mode.FZ;
import static cat.inspiracio.calculator.Calculator.Mode.MODFZ;
import static cat.inspiracio.calculator.World.Interaction.CIRCLE;
import static cat.inspiracio.calculator.World.Interaction.DRAW;
import static cat.inspiracio.calculator.World.Interaction.GRID;
import static cat.inspiracio.calculator.World.Interaction.LINE;
import static cat.inspiracio.calculator.World.Interaction.MOVE;
import static cat.inspiracio.calculator.World.Interaction.SQUARE;
import cat.inspiracio.calculator.Calculator.Mode;
import cat.inspiracio.complex.Complex;
import cat.inspiracio.complex.Complex$;
import cat.inspiracio.complex.package$;
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
	
    //private static final int GRID_DENSITY = 10;
    private FzWorld fzW;
    private ThreeDWorld modfzW;
    private ECList current;
    private Complex start;
    private Complex end;
    private Piclet currentPiclet;
    private PicletList piclets;
    private Square square;
    private JComboBox interactionChoice;
    private JButton eraseButton;
    private Mode mode;
    
	//Constructor ------------------------------------------

	/** Makes a new z-world.
	 * @param calculator Connected to this calculator. */
    ZWorld(Calculator calculator){
        super(calculator);
        Complex zero = Real(0);
        square = new Square(new Circle(zero, 1.0D));
        super.interaction = DRAW;
        setTitle("z World");
        eraseButton = new JButton("Clear");
        eraseButton.addActionListener( actionevent -> {
                erase();
                canvas.repaint();
            }
        );
        interactionChoice = new JComboBox();
        interactionChoice.addItem("Move");
        interactionChoice.addItem("Draw");
        interactionChoice.setSelectedItem("Draw");
        interactionChoice.addItemListener( itemevent -> {
        	    int state = itemevent.getStateChange();
        	    if(state!=ItemEvent.SELECTED)
        	        return;
                String s = (String)itemevent.getItem();
                if(s == "Move"){
                    interaction =MOVE;
                    return;
                }
                if(s == "Draw"){
                    interaction =DRAW;
                    return;
                }
                if(s == "Line"){
                    interaction =LINE;
                    return;
                }
                if(s == "Circle"){
                    interaction =CIRCLE;
                    return;
                }
                if(s == "Grid"){
                    interaction =GRID;
                    return;
                }
                if(s == "Rectangle"){
                    interaction =GRID;
                    return;
                }
                if(s == "Square")
                    interaction =SQUARE;
            }
        );
        super.buttonPanel.add(eraseButton);
        super.buttonPanel.add(interactionChoice);
        MouseAdapter mouseadapter = new MouseAdapter() {
        	@Override public void mousePressed(MouseEvent mouseevent){
                switch(interaction){
                //case 5: default: break;
                case MOVE: // '\001'
                    prevx = mouseevent.getX();
                    prevy = mouseevent.getY();
                    mouseevent.consume();
                    return;
                case DRAW: // '\0'
                    Complex ec = canvas.Point2Complex(mouseevent.getPoint());
                    if(ec != null){
                        Complex$.MODULE$.resetArg();
                        add(ec);
                        canvas.paint(canvas.getGraphics());
                        return;
                    }
                    break;
                case GRID: // '\006'
                    Complex ec1 = canvas.Point2Complex(mouseevent.getPoint());
                    if(ec1 != null){
                        start = ec1;
                        end = ec1;
                        addCurrent(new Rectangle(start, end));
                        canvas.paint(canvas.getGraphics());
                        return;
                    }
                    break;
                case LINE: // '\002'
                    Complex ec2 = canvas.Point2Complex(mouseevent.getPoint());
                    if(ec2 != null){
                        start = ec2;
                        end = ec2;
                        addCurrent(new Line(start, end));
                        canvas.paint(canvas.getGraphics());
                        return;
                    }
                    break;
                case CIRCLE: // '\004'
                    Complex ec3 = canvas.Point2Complex(mouseevent.getPoint());
                    if(ec3 != null){
                        start = ec3;
                        end = ec3;
                        addCurrent(new Circle(start, end));
                        canvas.paint(canvas.getGraphics());
                        return;
                    }
                    break;
                case RECTANGLE: // '\003'
                    Complex ec4 = canvas.Point2Complex(mouseevent.getPoint());
                    if(ec4 != null){
                        start = ec4;
                        end = ec4;
                        addCurrent(new Rectangle(start, end));
                        canvas.paint(canvas.getGraphics());
                        return;
                    }
                    break;
                case SQUARE: // '\007'
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
                //case 5: default: break;
                case MOVE: // '\001'
                    int i = mouseevent.getX();
                    int j = mouseevent.getY();
                    canvas.shift(prevx - i, prevy - j);
                    repaint();
                    prevx = i;
                    prevy = j;
                    mouseevent.consume();
                    return;
                case DRAW: // '\0'
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
                case LINE: // '\002'
                    if(start != null){
                        Complex ec1 = canvas.Point2Complex(mouseevent.getPoint());
                        if(ec1 != null)
                            end = ec1;
                        add(new Line(start, end));
                        canvas.paint(canvas.getGraphics());
                    }
                    eraseCurrent();
                    return;
                case CIRCLE: // '\004'
                    if(start != null){
                        Complex ec2 = canvas.Point2Complex(mouseevent.getPoint());
                        if(ec2 != null)
                            end = ec2;
                        add(new Circle(start, end));
                        canvas.paint(canvas.getGraphics());
                    }
                    eraseCurrent();
                    return;
                case RECTANGLE: // '\003'
                    if(start != null){
                        Complex ec3 = canvas.Point2Complex(mouseevent.getPoint());
                        if(ec3 != null)
                            end = ec3;
                        add(new Rectangle(start, end));
                        canvas.paint(canvas.getGraphics());
                    }
                    eraseCurrent();
                    return;
                case SQUARE: // '\007'
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
                case GRID: // '\006'
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
                //case 5: default: break;
                case MOVE: // '\001'
                    int i = mouseevent.getX();
                    int j = mouseevent.getY();
                    canvas.shift(prevx - i, prevy - j);
                    canvas.paint(canvas.getGraphics());
                    prevx = i;
                    prevy = j;
                    mouseevent.consume();
                    return;
                case DRAW: // '\0'
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
                        Complex$.MODULE$.resetArg();
                        add(ec);
                        canvas.paint(canvas.getGraphics());
                        return;
                    }
                    break;
                case CIRCLE: // '\004'
                    Complex ec1 = canvas.Point2Complex(mouseevent.getPoint());
                    if(ec1 != null){
                        end = ec1;
                        addCurrent(new Circle(start, end));
                        canvas.paint(canvas.getGraphics());
                        return;
                    }
                    break;
                case GRID: // '\006'
                    Complex ec2 = canvas.Point2Complex(mouseevent.getPoint());
                    if(ec2 != null){
                        end = ec2;
                        addCurrent(new Rectangle(start, end));
                        canvas.paint(canvas.getGraphics());
                        return;
                    }
                    break;
                case LINE: // '\002'
                    Complex ec3 = canvas.Point2Complex(mouseevent.getPoint());
                    if(ec3 != null){
                        end = ec3;
                        addCurrent(new Line(start, end));
                        canvas.paint(canvas.getGraphics());
                        return;
                    }
                    break;
                case RECTANGLE: // '\003'
                    Complex ec4 = canvas.Point2Complex(mouseevent.getPoint());
                    if(ec4 != null){
                        end = ec4;
                        addCurrent(new Rectangle(start, end));
                        canvas.paint(canvas.getGraphics());
                        return;
                    }
                    break;
                case SQUARE: // '\007'
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
        super.plane.addMouseListener(mouseadapter);
        super.plane.addMouseMotionListener(mousemotionadapter);
        super.sphere.addMouseListener(mouseadapter);
        super.sphere.addMouseMotionListener(mousemotionadapter);
        pack();
        setLocationRelativeTo(this.calculator);
        //this.setLocationByPlatform(true);
        setVisible(true);
    }

    @Override void add(Complex ec){
        updateExtremes(ec);
        current = new ECList(ec, current);
        fzW.add(ec);
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
            super.canvas.drawECList(drawing, current);
        if(currentPiclet != null)
            super.canvas.drawPiclet(drawing, currentPiclet);
        for(PicletList picletlist = piclets; picletlist != null; picletlist = picletlist.tail())
            super.canvas.drawPiclet(drawing, picletlist.head());
        if(mode ==MODFZ)
            super.canvas.drawPiclet(drawing, square);
    }

    void erase(){
        eraseCurrent();
        piclets = null;
        resetExtremes();
        if(mode ==MODFZ){
            updateExtremes(square);
            return;
        }
        if(fzW != null)
            fzW.erase();
    }

    void eraseCurrent(){
        current = null;
        currentPiclet = null;
        start = null;
        end = null;
    }

    PicletList getPiclets(){return piclets;}

    Square getSquare(){return square;}

    void setfzWorld(FzWorld fzworld){fzW = fzworld;}

    void setmodfzWorld(ThreeDWorld threedworld){modfzW = threedworld;}

    void setMode(Mode i){
        mode = i;
        interactionChoice.removeAll();
        switch(mode){
        case CALC:
        	setTitle("Complex World");
        	eraseButton.setEnabled(true);
        	interactionChoice.addItem("Move");
        	interactionChoice.addItem("Draw");
        	interactionChoice.setSelectedItem("Draw");
        	super.interaction=DRAW;
        	break;
        case FZ:
        	setTitle("z World");
        	eraseButton.setEnabled(true);
        	interactionChoice.addItem("Move");
        	interactionChoice.addItem("Draw");
        	interactionChoice.addItem("Circle");
        	interactionChoice.addItem("Line");
        	interactionChoice.addItem("Grid");
        	interactionChoice.addItem("Rectangle");
        	interactionChoice.addItem("Square");
        	interactionChoice.setSelectedItem("Draw");
        	super.interaction=DRAW;
        	break;
        case MODFZ:
        	setTitle("z World");
        	eraseButton.setEnabled(false);
        	interactionChoice.addItem("Move");
        	interactionChoice.addItem("Square");
        	interactionChoice.setSelectedItem("Square");
        	super.interaction=SQUARE;
        	break;
        }
        erase();
        super.canvas.repaint();
    }

}