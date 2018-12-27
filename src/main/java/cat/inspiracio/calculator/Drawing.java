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

import java.awt.*;

/** Make it easy to draw lines, circles, crosses, ...
 * Keeps a pen position.
 *
 * These are like extra methods on Graphics. */
final class Drawing{

	//State -------------------------------------------------------
	
    private Point pen;

    private Graphics g;
    Graphics graphics(){return g;}

    //Constructor --------------------------------------------------
    
    Drawing(Graphics g1){
        pen = new Point();
        g = g1;
    }

    //Methods ------------------------------------------------------

    /** cross at (x,y) */
    void cross(int x, int y, int width){
        pen.x = x;
        pen.y = y;
        g.drawLine(pen.x - width, pen.y - width, pen.x + width, pen.y + width);
        g.drawLine(pen.x + width, pen.y - width, pen.x - width, pen.y + width);
    }

    /** circle at (x,y) radius */
    void drawCircle(int x, int y, double radius){
        int topleftx = (int)((double)x - radius);
        int toplefty = (int)((double)y - radius);
        int width = (int)(2D * radius);
        int height = (int)(2D * radius);
        g.drawOval(topleftx, toplefty, width, height);
    }

    /** line from a to b in colour */
    void drawLine(Point a, Point b, Color c){
        Color color1 = g.getColor();
        g.setColor(c);
        g.drawLine(a.x, a.y, b.x, b.y);
        g.setColor(color1);
        pen.x = b.x;
        pen.y = b.y;
    }

    /** draw string at pen position */
    void drawString(String s){
        g.drawString(s, pen.x, pen.y);
    }

    void fillPolygon(Polygon polygon, Color c){
        Color color1 = g.getColor();
        g.setColor(c);
        g.fillPolygon(polygon);
        g.setColor(color1);
    }

    /** line (x,y) at pen */
    void line(int x, int y){
        g.drawLine(pen.x, pen.y, pen.x + x, pen.y + y);
        pen.translate(x, y);
    }

    /** line to (x,y) */
    void lineTo(int x, int y){
        g.drawLine(pen.x, pen.y, x, y);
        pen.x = x;
        pen.y = y;
    }

    /** Makes a triangle.
     * @param point Triangle tip is here.
     * @param direction Points this direction.
     * @param size Size of the triangle. */
    static Polygon mkTriangle(Point point, Direction direction, int size){
        switch(direction){
            case NORTH: return new Polygon(new int[] {point.x - size, point.x, point.x + size}, new int[] {point.y + size, point.y, point.y + size}, 3);
            case EAST: return new Polygon(new int[] {point.x - size, point.x, point.x - size}, new int[] {point.y - size, point.y, point.y + size}, 3);
            case SOUTH: return new Polygon(new int[] {point.x - size, point.x, point.x + size}, new int[] {point.y - size, point.y, point.y - size}, 3);
            case WEST: return new Polygon(new int[] {point.x + size, point.x, point.x + size}, new int[] {point.y - size, point.y, point.y + size}, 3);
        }
        return null;
    }

    /** move pen by (x,y) */
    void move(int x, int y){
        pen.translate(x, y);
    }

    /** move pen to (x,y) */
    void moveTo(int x, int y){
        pen.x = x;
        pen.y = y;
    }

}