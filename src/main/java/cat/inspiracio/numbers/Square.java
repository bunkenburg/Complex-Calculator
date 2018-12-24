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
package cat.inspiracio.numbers;


// Referenced classes of package bunkenba.numbers:
//            Rectangle, Circle, EC, PartialException

import cat.inspiracio.complex.Complex;

public class Square extends Rectangle {

    /** A square with c at the centre and side length 2*radius. */
    public Square(double c, double radius){
        Complex center = Real(c);
        botLeft = center.$plus(Cartesian(-radius, -radius));
        topLeft = center.$plus(Cartesian(-radius, radius));
        botRight = center.$plus(Cartesian(radius, -radius));
        topRight = center.$plus(Cartesian(radius, radius));
    }

    public Square(Circle circle) {
        botLeft = circle.center.$plus(Cartesian(-circle.radius, -circle.radius));
        topLeft = circle.center.$plus(Cartesian(-circle.radius, circle.radius));
        botRight = circle.center.$plus(Cartesian(circle.radius, -circle.radius));
        topRight = circle.center.$plus(Cartesian(circle.radius, circle.radius));
    }

    public Square(Complex ec, Complex ec1) {
        double d = Math.abs( Re(ec) - Re(ec1) );
        double d1 = Math.abs( Im(ec) - Im(ec1) );
        double d2 = (d + d1) / 2D;
        botLeft = ec.$plus(Cartesian(-d2, -d2));
        topLeft = ec.$plus(Cartesian(-d2, d2));
        botRight = ec.$plus(Cartesian(d2, -d2));
        topRight = ec.$plus(Cartesian(d2, d2));
    }

    public double getSide() {
        return Math.abs( Re(botLeft) - Re(botRight) );
    }
}
