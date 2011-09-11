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

import inspiracio.numbers.EC;
import inspiracio.numbers.ECList;

import java.awt.*;

// Referenced classes of package bunkenba.calculator:
//            WorldRepresentation, DoubleBuffer, Drawing, Matrix44, 
//            Vector3, World

final class Sphere extends WorldRepresentation{

	//State -------------------------------------------------------
	
    private static final EC marks[];
    private Matrix44 R;
    private Matrix44 R1;
    private double xyscale;
    private static int MARKLENGTH = 2;

    static {
        marks = (new EC[] {EC.ZERO, EC.INFINITY, EC.ONE, EC.mkCartesian(-1D, 0.0D), EC.I, EC.mkCartesian(0.0D, -1D)});
    }

	//Constructor -------------------------------------------------
	
    Sphere(World world){
        super(world);
        R = new Matrix44();
        R1 = new Matrix44();
        reset();
    }
    
    //Methods ------------------------------------------------------

    void drawComplex(Drawing drawing, EC ec){
        double d;
        double d1;
        double d2;
        if(ec.finite()){
            double d3 = EC.sqr(ec.mod());
            double d5 = 1.0D + d3;
            d = ec.re() / d5;
            d1 = d3 / d5 - 0.5D;
            d2 = ec.im() / d5;
        } else{
            d = 0.0D;
            d1 = 0.5D;
            d2 = 0.0D;
        }
        double d4 = R.data[2][0] * d + R.data[2][1] * d1 + R.data[2][2] * d2 + R.data[2][3];
        if(d4 <= 0.0D){
            double d6 = R.data[0][0] * d + R.data[0][1] * d1 + R.data[0][2] * d2 + R.data[0][3];
            double d7 = R.data[1][0] * d + R.data[1][1] * d1 + R.data[1][2] * d2 + R.data[1][3];
            drawing.cross((int)(d6 * xyscale + (double)getSize().width * 0.5D), (int)(-d7 * xyscale + (double)getSize().height * 0.5D), MARKLENGTH);
            drawing.move(2, -2);
            drawing.drawString(ec.toString());
        }
    }

    void drawECList(Drawing drawing, ECList eclist){
        Point point = new Point();
        if(eclist != null){
            boolean flag = isFrontC(eclist.head(), point);
            drawing.moveTo(point.x, point.y);
            for(; eclist != null; eclist = eclist.tail())
                if(isFrontC(eclist.head(), point)){
                    if(flag)
                        drawing.lineTo(point.x, point.y);
                    else
                        drawing.moveTo(point.x, point.y);
                    flag = true;
                } else{
                    flag = false;
                }
        }
    }

    private static EC f3dC(Vector3 vector3){
        if(vector3.y == 0.5D)
            return EC.INFINITY;
        if(vector3.y == -0.5D)
            return EC.ZERO;
        else
            return EC.mkCartesian(vector3.x / (0.5D - vector3.y), vector3.z / (0.5D - vector3.y));
    }

    private boolean isFrontC(EC ec, Point point){
        double d;
        double d1;
        double d2;
        if(ec.finite()){
            double d3 = EC.sqr(ec.mod());
            double d5 = 1.0D + d3;
            d = ec.re() / d5;
            d1 = d3 / d5 - 0.5D;
            d2 = ec.im() / d5;
        } else{
            d = 0.0D;
            d1 = 0.5D;
            d2 = 0.0D;
        }
        double d4 = R.data[2][0] * d + R.data[2][1] * d1 + R.data[2][2] * d2 + R.data[2][3];
        if(d4 <= 0.0D){
            double d6 = R.data[0][0] * d + R.data[0][1] * d1 + R.data[0][2] * d2 + R.data[0][3];
            double d7 = R.data[1][0] * d + R.data[1][1] * d1 + R.data[1][2] * d2 + R.data[1][3];
            point.x = (int)(d6 * xyscale + (double)getSize().width * 0.5D);
            point.y = (int)(-d7 * xyscale + (double)getSize().height * 0.5D);
            return true;
        } else{
            return false;
        }
    }

    void lineTo(Drawing drawing, EC ec){
        double d;
        double d1;
        double d2;
        if(ec.finite()){
            double d3 = EC.sqr(ec.mod());
            double d5 = 1.0D + d3;
            d = ec.re() / d5;
            d1 = d3 / d5 - 0.5D;
            d2 = ec.im() / d5;
        } else{
            d = 0.0D;
            d1 = 0.5D;
            d2 = 0.0D;
        }
        double d4 = R.data[2][0] * d + R.data[2][1] * d1 + R.data[2][2] * d2 + R.data[2][3];
        if(d4 <= 0.0D){
            double d6 = R.data[0][0] * d + R.data[0][1] * d1 + R.data[0][2] * d2 + R.data[0][3];
            double d7 = R.data[1][0] * d + R.data[1][1] * d1 + R.data[1][2] * d2 + R.data[1][3];
            drawing.lineTo((int)(d6 * xyscale + (double)getSize().width * 0.5D), (int)(-d7 * xyscale + (double)getSize().height * 0.5D));
        }
    }

    void moveTo(Drawing drawing, EC ec){
        double d;
        double d1;
        double d2;
        if(ec.finite()){
            double d3 = EC.sqr(ec.mod());
            double d5 = 1.0D + d3;
            d = ec.re() / d5;
            d1 = d3 / d5 - 0.5D;
            d2 = ec.im() / d5;
        } else{
            d = 0.0D;
            d1 = 0.5D;
            d2 = 0.0D;
        }
        double d4 = R.data[2][0] * d + R.data[2][1] * d1 + R.data[2][2] * d2 + R.data[2][3];
        if(d4 <= 0.0D){
            double d6 = R.data[0][0] * d + R.data[0][1] * d1 + R.data[0][2] * d2 + R.data[0][3];
            double d7 = R.data[1][0] * d + R.data[1][1] * d1 + R.data[1][2] * d2 + R.data[1][3];
            drawing.moveTo((int)(d6 * xyscale + (double)getSize().width * 0.5D), (int)(-d7 * xyscale + (double)getSize().height * 0.5D));
        }
    }

    public void paint(Graphics g){
        g = super.doubleBuffer.offScreen(g);
        xyscale = (double)Math.min(getSize().width, getSize().height) * 0.80000000000000004D;
        Drawing drawing = new Drawing(g);
        drawing.drawCircle(getSize().width / 2, getSize().height / 2, 0.5D * xyscale);
        for(int i = 0; i < marks.length; i++)
            drawComplex(drawing, marks[i]);
        super.w.drawStuff(drawing);
        super.doubleBuffer.onScreen();
    }

    EC Point2Complex(Point point){
        double d = ((double)point.x - (double)getSize().width * 0.5D) / xyscale;
        double d1 = ((double)getSize().height * 0.5D - (double)point.y) / xyscale;
        double d2 = 0.25D - d * d - d1 * d1;
        if(d2 >= 0.0D){
            Vector3 vector3 = R1.multiply(d, d1, -Math.sqrt(d2));
            return f3dC(vector3);
        } else{
            return null;
        }
    }

    void reset(){
        R.unit();
        R1.unit();
    }

    public void setFont(Font font){
        super.setFont(font);
        int i = getFontMetrics(font).getAscent();
        MARKLENGTH = i / 5;
    }

    void shift(int i, int j){
        double d = (double)j * (6.2831853071795862D / (double)getSize().width);
        double d1 = (double)i * (6.2831853071795862D / (double)getSize().height);
        R.preRot('x', -d);
        R.preRot('y', -d1);
        R1.postRot('x', d);
        R1.postRot('y', d1);
    }

    void zoomIn(){}
    void zoomOut(){}

}