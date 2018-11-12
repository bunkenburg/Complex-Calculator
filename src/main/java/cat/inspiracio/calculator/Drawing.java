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

import java.awt.*;

final class Drawing{

	//State -------------------------------------------------------
	
    public static final int NORTH = 0;
    public static final int EAST = 1;
    public static final int SOUTH = 2;
    public static final int WEST = 3;
    private Point pen;
    Graphics g;

    //Constructor --------------------------------------------------
    
    Drawing(Graphics g1){
        pen = new Point();
        g = g1;
    }

    //Methods ------------------------------------------------------
    
    void cross(){
        g.drawLine(pen.x - 2, pen.y - 2, pen.x + 2, pen.y + 2);
        g.drawLine(pen.x + 2, pen.y - 2, pen.x - 2, pen.y + 2);
    }

    void cross(int i, int j, int k){
        pen.x = i;
        pen.y = j;
        g.drawLine(pen.x - k, pen.y - k, pen.x + k, pen.y + k);
        g.drawLine(pen.x + k, pen.y - k, pen.x - k, pen.y + k);
    }

    void cross(Point point){
        g.drawLine(point.x - 2, point.y - 2, point.x + 2, point.y + 2);
        g.drawLine(point.x + 2, point.y - 2, point.x - 2, point.y + 2);
        pen.x = point.x;
        pen.y = point.y;
    }

    void drawCircle(int i, int j, double d){
        g.drawOval((int)((double)i - d), (int)((double)j - d), (int)(2D * d), (int)(2D * d));
    }

    void drawCircle(Point point, double d){
        g.drawOval((int)((double)point.x - d), (int)((double)point.y - d), (int)(2D * d), (int)(2D * d));
    }

    void drawLine(Point point, Point point1){
        g.drawLine(point.x, point.y, point1.x, point1.y);
        pen.x = point1.x;
        pen.y = point1.y;
    }

    void drawLine(Point point, Point point1, Color color){
        Color color1 = g.getColor();
        g.setColor(color);
        g.drawLine(point.x, point.y, point1.x, point1.y);
        g.setColor(color1);
        pen.x = point1.x;
        pen.y = point1.y;
    }

    void drawString(String s){
        g.drawString(s, pen.x, pen.y);
    }

    void fillPolygon(Polygon polygon, Color color){
        Color color1 = g.getColor();
        g.setColor(color);
        g.fillPolygon(polygon);
        g.setColor(color1);
    }

    void line(Point point){
        g.drawLine(pen.x, pen.y, pen.x + point.x, pen.y + point.y);
        pen.translate(point.x, point.y);
    }

    void line(int i, int j){
        g.drawLine(pen.x, pen.y, pen.x + i, pen.y + j);
        pen.translate(i, j);
    }

    void lineTo(int i, int j){
        g.drawLine(pen.x, pen.y, i, j);
        pen.x = i;
        pen.y = j;
    }

    void lineTo(Point point){
        g.drawLine(pen.x, pen.y, point.x, point.y);
        pen.x = point.x;
        pen.y = point.y;
    }

    /** Makes a triangle.
     * @param point Triangle tip is here.
     * @param i Points this direction.
     * @param j Size of the triangle. */
    static Polygon mkTriangle(Point point, int i, int j){
        switch(i){
        case 0: // '\0'
            return new Polygon(new int[] {
                point.x - j, point.x, point.x + j
            }, new int[] {
                point.y + j, point.y, point.y + j
            }, 3);
        case 1: // '\001'
            return new Polygon(new int[] {
                point.x - j, point.x, point.x - j
            }, new int[] {
                point.y - j, point.y, point.y + j
            }, 3);
        case 2: // '\002'
            return new Polygon(new int[] {
                point.x - j, point.x, point.x + j
            }, new int[] {
                point.y - j, point.y, point.y - j
            }, 3);
        case 3: // '\003'
            return new Polygon(new int[] {
                point.x + j, point.x, point.x + j
            }, new int[] {
                point.y - j, point.y, point.y + j
            }, 3);
        }
        return null;
    }

    void move(int i, int j){
        pen.translate(i, j);
    }

    void moveTo(Point point){
        pen.x = point.x;
        pen.y = point.y;
    }

    void moveTo(int i, int j){
        pen.x = i;
        pen.y = j;
    }

}