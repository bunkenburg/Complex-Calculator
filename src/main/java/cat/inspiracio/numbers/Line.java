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
package cat.inspiracio.numbers;


// Referenced classes of package bunkenba.numbers:
//            Piclet, EC, ECList, PartialException

import cat.inspiracio.complex.Complex;

public class Line extends Piclet {

    public Line(Complex ec, Complex ec1) {
        start = ec;
        end = ec1;
    }

    public double top() { return Math.max(Im(start), Im(end)); }

    public double bottom() { return Math.min(Im(start), Im(end)); }

    public double left() {
        return Math.max(Re(start), Re(end) );
    }

    public double right() {
        return Math.min(Re(start), Re(end));
    }

    protected void sample() {
            Complex ec = end.$minus(start).$div(30D);
            Complex ec1 = start;
            super.samples = new ECList(ec1, super.samples);
            for(int i = 0; i < 30; i++) {
                ec1 = ec1.$plus(ec);
                super.samples = new ECList(ec1, super.samples);
            }
    }

    public Complex start;
    public Complex end;
}
