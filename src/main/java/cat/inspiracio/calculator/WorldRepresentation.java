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
import cat.inspiracio.numbers.ECList;
import cat.inspiracio.numbers.Piclet;

import javax.swing.*;
import java.awt.*;

// Referenced classes of package bunkenba.calculator:
//            DoubleBuffer, World, Drawing

abstract class WorldRepresentation extends JComponent {

    /** Initial size wide enough so that the interaction menu is visible. */
    private static final Dimension MIN_SIZE = new Dimension(550, 300);

    //State ---------------------------------------------------------
	
    protected World w;
    protected DoubleBuffer doubleBuffer;

	//Constructors --------------------------------------------------
	
    protected WorldRepresentation(World world){
        w = world;
        doubleBuffer = new DoubleBuffer(this);
        setBackground(Color.white);
    }
    
    //Methods ------------------------------------------------------

    abstract void drawComplex(Drawing drawing, Complex ec);

    abstract void drawECList(Drawing drawing, ECList eclist);

    void drawPiclet(Drawing drawing, Piclet piclet){
        drawECList(drawing, piclet.getSamples());
    }

    @Override public Dimension getPreferredSize(){return getMinimumSize();}
    @Override public Dimension getMinimumSize(){return MIN_SIZE;}

    abstract void moveTo(Drawing drawing, Complex ec);

    abstract void lineTo(Drawing drawing, Complex ec);

    abstract Complex Point2Complex(Point point);

    abstract void reset();

    abstract void shift(int i, int j);

    abstract void zoomIn();

    abstract void zoomOut();

    // helpers -----------------------------------

    protected boolean finite(Complex z){return package$.MODULE$.finite(z);}
    protected double Im(Complex c){ return cat.inspiracio.complex.package$.MODULE$.Im(c); }
    protected double Re(Complex c) { return cat.inspiracio.complex.package$.MODULE$.Re(c); }
    protected double abs(Complex z){ return package$.MODULE$.abs(z); }

    protected String toString(double d){ return package$.MODULE$.double2Complex(d).toString(); }

    protected static final double sqr(double d){return d * d;}

    protected final Complex infinity = Real(1).$div(0);

    protected Complex Cartesian(double re, double im){
        Complex i = package$.MODULE$.i();
        return i.$times(im).$plus(re);
    }

    protected Complex Real(double re){ return package$.MODULE$.double2Complex(re); }

}