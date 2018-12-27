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
//            Piclet, EC, ECList, PartialException

import cat.inspiracio.complex.Complex;

public class Rectangle extends Piclet {


    public Complex botLeft;
    public Complex topLeft;
    public Complex botRight;
    public Complex topRight;

    protected Rectangle(){ }

    public Rectangle(Complex ec, Complex ec1) {
        double d = Math.abs( Re(ec) - Re(ec1));
        double d1 = Math.abs( Im(ec) - Im(ec1));
        botLeft = ec.$plus(Cartesian(-d, -d1));
        topLeft = ec.$plus(Cartesian(-d, d1));
        botRight = ec.$plus(Cartesian(d, -d1));
        topRight = ec.$plus(Cartesian(d, d1));
    }

    public Complex getCenter() {
        double re = ( Re(botLeft) + Re(topRight)) / 2D;
        double im = ( Im(botLeft) + Im(topRight)) / 2D;
        return Cartesian( re, im );
    }

    public double top() {
        return Im(topLeft);
    }

    public double bottom()
    {
        return Im(botLeft);
    }

    public double left()
    {
        return Re(topLeft);
    }

    public double right() {
        return Re(topRight);
    }

    public double getHeight()
    {
        return Math.abs(Im(botLeft) - Im(topLeft));
    }

    public double getWidth()
    {
        return Math.abs( Re(botLeft) - Re(botRight));
    }

    protected void sample() {
            Complex ec = botRight.$minus(botLeft).$div(30D);
            Complex ec1 = botLeft;
            super.samples = new ECList(ec1, super.samples);
            for(int i = 0; i < 30; i++) {
                ec1 = ec1.$plus(ec);
                super.samples = new ECList(ec1, super.samples);
            }

            ec = topRight.$minus(botRight).$div(30D);
            ec1 = botRight;
            super.samples = new ECList(ec1, super.samples);
            for(int j = 0; j < 30; j++) {
                ec1 = ec1.$plus(ec);
                super.samples = new ECList(ec1, super.samples);
            }

            ec = topLeft.$minus(topRight).$div(30D);
            ec1 = topRight;
            super.samples = new ECList(ec1, super.samples);
            for(int k = 0; k < 30; k++) {
                ec1 = ec1.$plus(ec);
                super.samples = new ECList(ec1, super.samples);
            }

            ec = botLeft.$minus(topLeft).$div(30D);
            ec1 = topLeft;
            super.samples = new ECList(ec1, super.samples);
            for(int l = 0; l < 30; l++) {
                ec1 = ec1.$plus(ec);
                super.samples = new ECList(ec1, super.samples);
            }
    }

}
