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

import static inspiracio.calculator.World.Interaction.DRAW;
import static inspiracio.calculator.World.Interaction.MOVE;
import inspiracio.numbers.EC;
import inspiracio.numbers.ECList;

import java.awt.Button;
import java.awt.Choice;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

// Referenced classes of package bunkenba.calculator:
//            World, WorldRepresentation, Calculator, Drawing

final class ComplexWorld extends World {
	
	//State ------------------------------------------------------------------
	
    private ECList numbers;

	//Constructor ------------------------------------------------------------
	
    ComplexWorld(final Calculator calculator){
        super(calculator);
        super.interaction =DRAW;
        setTitle("Complex World");
        Button button = new Button("Clear");
        button.addActionListener(new ActionListener() {
        	@Override public void actionPerformed(ActionEvent actionevent){
                erase();
                canvas.repaint();
            }
        });
        Choice choice = new Choice();
        choice.add("Draw");
        choice.add("Move");
        choice.select("Draw");
        choice.addItemListener(new ItemListener() {
        	@Override public void itemStateChanged(ItemEvent itemevent){
                if(itemevent.getItem() == "Draw"){
                    interaction =DRAW;
                    return;
                } else{
                    interaction =MOVE;
                    return;
                }
            }
        });
        super.buttonPanel.add(button);
        super.buttonPanel.add(choice);
        MouseAdapter mouseadapter = new MouseAdapter() {
        	@Override public void mousePressed(MouseEvent mouseevent){
                if(interaction==MOVE){
                    prevx = mouseevent.getX();
                    prevy = mouseevent.getY();
                    mouseevent.consume();
                    return;
                }
                EC ec = canvas.Point2Complex(mouseevent.getPoint());
                if(ec != null){
                    calculator.add(ec);
                    add(ec);
                }
            }
            @Override public void mouseReleased(MouseEvent mouseevent){
                if(interaction ==MOVE){
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
        MouseMotionAdapter mousemotionadapter = new MouseMotionAdapter() {
            @Override public void mouseDragged(MouseEvent mouseevent){
                if(interaction ==Interaction.MOVE){
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
        super.plane.addMouseListener(mouseadapter);
        super.plane.addMouseMotionListener(mousemotionadapter);
        super.sphere.addMouseListener(mouseadapter);
        super.sphere.addMouseMotionListener(mousemotionadapter);
        pack();
        //this.setLocationByPlatform(true);
        this.setVisible(true);
    }

    //Methods --------------------------------------------------------------
    
    void add(EC ec){
        numbers = new ECList(ec, numbers);
        updateExtremes(ec);
        super.canvas.repaint();
    }

    /** Close and remove the window. */
    @Override public void dispose(){
    	MMenuBar mb=(MMenuBar)this.getMenuBar();
    	mb.dispose();
    	super.dispose();
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