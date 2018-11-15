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

import cat.inspiracio.numbers.Circle;
import cat.inspiracio.numbers.EC;
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
    
    private void cartesian2Point(double d, double d1, Point point){
        point.x = (int)((d - LeftReal) * ScaleFactor);
        point.y = -(int)((d1 - TopImaginary) * ScaleFactor);
    }

    void drawComplex(Drawing drawing, EC ec){
        if(ec.finite()){
            drawing.cross((int)((ec.re() - LeftReal) * ScaleFactor), -(int)((ec.im() - TopImaginary) * ScaleFactor), MARKLENGTH);
            drawing.move(2, 2);
            drawing.drawString(ec.toString());
        }
    }

    void drawECList(Drawing drawing, ECList eclist){
        if(eclist != null){
            moveTo(drawing, eclist.head());
            lineTo(drawing, eclist.head());
            for(eclist = eclist.tail(); eclist != null; eclist = eclist.tail())
                lineTo(drawing, eclist.head());
        }
    }

    void drawPiclet(Drawing drawing, Piclet piclet){
        if(piclet instanceof Line){
            moveTo(drawing, ((Line)piclet).start);
            lineTo(drawing, ((Line)piclet).end);
            return;
        }
        if(piclet instanceof Circle){
            Circle circle = (Circle)piclet;
            drawing.drawCircle((int)((circle.center.re() - LeftReal) * ScaleFactor), -(int)((circle.center.im() - TopImaginary) * ScaleFactor), Math2Pix(circle.radius));
            return;
        }
        if(piclet instanceof Rectangle){
            Rectangle rectangle = (Rectangle)piclet;
            moveTo(drawing, rectangle.botLeft);
            lineTo(drawing, rectangle.botRight);
            lineTo(drawing, rectangle.topRight);
            lineTo(drawing, rectangle.topLeft);
            lineTo(drawing, rectangle.botLeft);
            return;
        } else{
            drawECList(drawing, piclet.getSamples());
            return;
        }
    }

    void lineTo(Drawing drawing, EC ec){
        drawing.lineTo((int)((ec.re() - LeftReal) * ScaleFactor), -(int)((ec.im() - TopImaginary) * ScaleFactor));
    }

    void moveTo(Drawing drawing, EC ec){
        drawing.moveTo((int)((ec.re() - LeftReal) * ScaleFactor), -(int)((ec.im() - TopImaginary) * ScaleFactor));
    }

    EC Point2Complex(Point point){
        return EC.mkCartesian(LeftReal + (double)point.x / ScaleFactor, TopImaginary - (double)point.y / ScaleFactor);
    }

    public void paint(Graphics g){
        g = super.doubleBuffer.offScreen(g);
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
        java.awt.Polygon polygon = Drawing.mkTriangle(point1, 1, TRIANGLESIZE);
        g.drawPolygon(polygon);
        if(RightReal <= super.w.MaxReal)
            g.fillPolygon(polygon);
        polygon = Drawing.mkTriangle(point, 3, TRIANGLESIZE);
        g.drawPolygon(polygon);
        if(super.w.MinReal <= LeftReal)
            g.fillPolygon(polygon);
        int j = point2.y;
        double d7 = Math.ceil(d3 / d);
        d3 = d7 * d;
        int i = real2Pix(d3);
        for(; d3 < d4; d3 += d){
            drawing.moveTo(i, j);
            drawing.line(0, MARKLENGTH);
            g.drawString(EC.toString(d3), i + MARKLENGTH, j + FONTHEIGHT);
            i += l;
        }
        cartesian2Point(d1, d5, point);
        cartesian2Point(d1, d6, point1);
        drawing.drawLine(point, point1, Color.lightGray);
        polygon = Drawing.mkTriangle(point1, 0, TRIANGLESIZE);
        g.drawPolygon(polygon);
        if(TopImaginary <= super.w.MaxImaginary)
            g.fillPolygon(polygon);
        polygon = Drawing.mkTriangle(point, 2, TRIANGLESIZE);
        g.drawPolygon(polygon);
        if(super.w.MinImaginary <= BottomImaginary)
            g.fillPolygon(polygon);
        i = point2.x;
        d7 = Math.ceil(d5 / d);
        d5 = d7 * d;
        for(int k = imag2Pix(d5); d5 < d6; k -= l){
            if(d5 != 0.0D || d1 != 0.0D){
                String s = EC.toString(d5) + "i";
                drawing.moveTo(i, k);
                drawing.line(-MARKLENGTH, 0);
                g.drawString(s, i - MARKLENGTH - g.getFontMetrics().stringWidth(s), k + FONTHEIGHT);
            }
            d5 += d;
        }
        super.w.drawStuff(drawing);
        super.doubleBuffer.onScreen();
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

    void shift(int i, int j){
        CenterImaginary -= Pix2Math(j);
        CenterReal += Pix2Math(i);
    }

    void zoomIn(){ScaleFactor *= 2D;}

    void zoomOut(){ScaleFactor /= 2D;}
}