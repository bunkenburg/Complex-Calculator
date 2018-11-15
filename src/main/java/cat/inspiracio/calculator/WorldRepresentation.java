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

import cat.inspiracio.numbers.*;

import javax.swing.*;
import java.awt.*;

// Referenced classes of package bunkenba.calculator:
//            DoubleBuffer, World, Drawing

abstract class WorldRepresentation extends JComponent {

	//State ---------------------------------------------------------
	
	/** Initial size wide enough so that the interaction menu is visible. */
    private static final Dimension MIN_SIZE = new Dimension(500, 300);
    
    protected World w;
    protected DoubleBuffer doubleBuffer;

	//Constructors --------------------------------------------------
	
    protected WorldRepresentation(World world){
        w = world;
        doubleBuffer = new DoubleBuffer(this);
        setBackground(Color.white);
    }
    
    //Methods ------------------------------------------------------

    abstract void drawComplex(Drawing drawing, EC ec);

    abstract void drawECList(Drawing drawing, ECList eclist);

    void drawPiclet(Drawing drawing, Piclet piclet){
        drawECList(drawing, piclet.getSamples());
    }

    @Override public Dimension getPreferredSize(){return getMinimumSize();}
    @Override public Dimension getMinimumSize(){return MIN_SIZE;}

    abstract void moveTo(Drawing drawing, EC ec);

    abstract void lineTo(Drawing drawing, EC ec);

    abstract EC Point2Complex(Point point);

    abstract void reset();

    abstract void shift(int i, int j);

    abstract void zoomIn();

    abstract void zoomOut();
}