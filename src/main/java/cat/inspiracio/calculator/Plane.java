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
import cat.inspiracio.numbers.Circle;
import cat.inspiracio.numbers.ECList;
import cat.inspiracio.numbers.Line;
import cat.inspiracio.numbers.Piclet;
import cat.inspiracio.numbers.Rectangle;

import java.awt.*;

final class Plane extends WorldRepresentation{

    private static int AXISSPACE = 30;
    private static int AXISMARKING = 40;
    private static int FONTHEIGHT = 10;
    private static int TRIANGLESIZE = 5;
    private static int MARKLENGTH = 2;

    private double ScaleFactor;
    private double CenterReal;
    private double CenterImaginary;
    private double TopImaginary;
    private double LeftReal;
    private double BottomImaginary;
    private double RightReal;

	//Constructors -------------------------------------------------------------
	
    Plane(World world){
        super(world);
        ScaleFactor = 40D;
    }

    //Methods ------------------------------------------------------------------
    
    private void cartesian2Point(double re, double im, Point point){
        point.x = (int)((re - LeftReal) * ScaleFactor);
        point.y = -(int)((im - TopImaginary) * ScaleFactor);
    }

    @Override void drawComplex(Drawing drawing, Complex z){
        if( finite(z) ){
            int x = (int)(( Re(z) - LeftReal) * ScaleFactor);
            int y = -(int)(( Im(z) - TopImaginary) * ScaleFactor);
            drawing.cross( x, y, MARKLENGTH );
            drawing.move(2, 2);
            drawing.drawString(z.toString());
        }
    }

    void drawECList(Drawing drawing, ECList list){
        if(list != null){
            moveTo(drawing, list.head());
            lineTo(drawing, list.head());
            for(list = list.tail(); list != null; list = list.tail())
                lineTo(drawing, list.head());
        }
    }

    void drawPiclet(Drawing drawing, Piclet piclet){
        if(piclet instanceof Line){
            moveTo(drawing, ((Line)piclet).start);
            lineTo(drawing, ((Line)piclet).end);
        }

        else if(piclet instanceof Circle){
            Circle circle = (Circle)piclet;
            drawing.drawCircle(
                    (int)((Re(circle.center) - LeftReal) * ScaleFactor),
                    -(int)((Im(circle.center) - TopImaginary) * ScaleFactor),
                    Math2Pix(circle.radius)
            );
        }

        else if(piclet instanceof Rectangle){
            Rectangle rectangle = (Rectangle)piclet;
            moveTo(drawing, rectangle.botLeft);
            lineTo(drawing, rectangle.botRight);
            lineTo(drawing, rectangle.topRight);
            lineTo(drawing, rectangle.topLeft);
            lineTo(drawing, rectangle.botLeft);
        }

        else{
            drawECList(drawing, piclet.getSamples());
        }
    }

    void lineTo(Drawing drawing, Complex c){
        int x = (int)(( Re(c) - LeftReal) * ScaleFactor);
        int y = -(int)(( Im(c) - TopImaginary) * ScaleFactor);
        drawing.lineTo(x, y );
    }

    void moveTo(Drawing drawing, Complex c){
        int x = (int)(( Re(c) - LeftReal) * ScaleFactor);
        int y = -(int)(( Im(c) - TopImaginary) * ScaleFactor);
        drawing.moveTo( x, y );
    }

    @Override Complex Point2Complex(Point point){
        double re = LeftReal + (double)point.x / ScaleFactor;
        double im = TopImaginary - (double)point.y / ScaleFactor;
        return Cartesian(re, im);
    }

    @Override public void paint(Graphics g){
        g = doubleBuffer.offScreen(g);
        Point point = new Point();
        Point point1 = new Point();
        Drawing drawing = new Drawing(g);
        Point point2 = new Point();
        TopImaginary = CenterImaginary + Pix2Math(getSize().height / 2);
        BottomImaginary = CenterImaginary - Pix2Math(getSize().height / 2);
        LeftReal = CenterReal - Pix2Math(getSize().width / 2);
        RightReal = CenterReal + Pix2Math(getSize().width / 2);
        double d = raiseSmooth(Pix2Math(AXISMARKING));
        int l = Math2Pix(d);
        double d1 = 0.0D;
        double d2 = 0.0D;
        double d3 = LeftReal + Pix2Math(AXISSPACE);
        double d4 = RightReal - Pix2Math(AXISSPACE);
        double d5 = BottomImaginary + Pix2Math(AXISSPACE);
        double d6 = TopImaginary - Pix2Math(AXISSPACE);
        if(d3 <= 0.0D && d4 >= 0.0D)
            d1 = 0.0D;
        else if(d3 > 0.0D)
            d1 = d3;
        else if(d4 < 0.0D)
            d1 = d4;
        if(d5 <= 0.0D && d6 >= 0.0D)
            d2 = 0.0D;
        else if(d5 > 0.0D)
            d2 = d5;
        else if(d6 < 0.0D)
            d2 = d6;
        cartesian2Point(d1, d2, point2);
        cartesian2Point(d3, d2, point);
        cartesian2Point(d4, d2, point1);
        drawing.drawLine(point, point1, Color.lightGray);
        Polygon polygon = drawing.mkTriangle(point1, Direction.EAST, TRIANGLESIZE);
        g.drawPolygon(polygon);
        if(RightReal <= w.MaxReal)
            g.fillPolygon(polygon);
        polygon = drawing.mkTriangle(point, Direction.WEST, TRIANGLESIZE);
        g.drawPolygon(polygon);
        if(w.MinReal <= LeftReal)
            g.fillPolygon(polygon);
        int j = point2.y;
        double d7 = Math.ceil(d3 / d);
        d3 = d7 * d;
        int i = real2Pix(d3);
        for(; d3 < d4; d3 += d){
            drawing.moveTo(i, j);
            drawing.line(0, MARKLENGTH);
            g.drawString(toString(d3), i + MARKLENGTH, j + FONTHEIGHT);
            i += l;
        }
        cartesian2Point(d1, d5, point);
        cartesian2Point(d1, d6, point1);
        drawing.drawLine(point, point1, Color.lightGray);
        polygon = drawing.mkTriangle(point1, Direction.NORTH, TRIANGLESIZE);
        g.drawPolygon(polygon);
        if(TopImaginary <= w.MaxImaginary)
            g.fillPolygon(polygon);
        polygon = drawing.mkTriangle(point, Direction.SOUTH, TRIANGLESIZE);
        g.drawPolygon(polygon);
        if(w.MinImaginary <= BottomImaginary)
            g.fillPolygon(polygon);
        i = point2.x;
        d7 = Math.ceil(d5 / d);
        d5 = d7 * d;
        for(int k = imag2Pix(d5); d5 < d6; k -= l){
            if(d5 != 0.0D || d1 != 0.0D){
                String s = toString(d5) + "i";
                drawing.moveTo(i, k);
                drawing.line(-MARKLENGTH, 0);
                g.drawString(s, i - MARKLENGTH - g.getFontMetrics().stringWidth(s), k + FONTHEIGHT);
            }
            d5 += d;
        }
        w.drawStuff(drawing);
        doubleBuffer.onScreen();
    }

    private static double raiseSmooth(double d){
        int i;
        for(i = 0; d < 1.0D; i--)
            d *= 10D;
        while(d >= 10D){
            d /= 10D;
            i++;
        }
        if(d > 5D)
            d = 10D;
        else if(d > 2.5D)
            d = 5D;
        else if(d > 2D)
            d = 2.5D;
        else if(d > 1.0D)
            d = 2D;
        for(; i < 0; i++)
            d /= 10D;
        for(; i > 0; i--)
            d *= 10D;
        return d;
    }

    void reset(){
        CenterReal = 0.0D;
        CenterImaginary = 0.0D;
    }

    private double Pix2Math(int i){
        return (double)i / ScaleFactor;
    }

    private int Math2Pix(double d){
        return (int)(d * ScaleFactor);
    }

    private int real2Pix(double d){
        return (int)((d - LeftReal) * ScaleFactor);
    }

    private int imag2Pix(double d){
        return (int)((TopImaginary - d) * ScaleFactor);
    }

    public void setFont(Font font){
        super.setFont(font);
        FONTHEIGHT = getFontMetrics(font).getAscent();
        AXISSPACE = 3 * FONTHEIGHT;
        AXISMARKING = 4 * FONTHEIGHT;
        TRIANGLESIZE = FONTHEIGHT / 2;
        MARKLENGTH = FONTHEIGHT / 5;
    }

    void shift(int x, int y){
        CenterReal += Pix2Math(x);
        CenterImaginary -= Pix2Math(y);
    }

    void zoomIn(){ScaleFactor *= 2D;}

    void zoomOut(){ScaleFactor /= 2D;}
}