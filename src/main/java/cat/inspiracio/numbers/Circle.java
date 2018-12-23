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
import cat.inspiracio.complex.package$;

public class Circle extends Piclet {

    public Complex center;
    public double radius;

    public Circle(double c, double r) {
        center = Double.isFinite(c) ? Real(c) : Real(0);
        radius = r;
    }

    public Circle(Complex c, double r) {
        center = c.isFinite() ? c : Real(0);
        radius = r;
    }

    public Circle(Complex c, Complex r) {
        center = c.isFinite() ? c : Real(0);
        radius = r.isFinite() ? distance(center, r) : (1.0D / 0.0D);
    }

    public double top() { return Im(center) + radius; }

    public double bottom() { return Im(center) - radius; }

    public double left() { return Re(center) - radius; }

    public double right() { return Re(center) + radius; }

    protected void sample() {
        double d = 0.20943951023931953D;
        double angle = 0.0D;
        for(int i = 0; i <= 30; i++) {
            Complex z = Polar(radius, angle);
            super.samples = new ECList(center.$plus(z), super.samples);
            angle += d;
        }

    }

    protected Complex Polar(double radius, double angle){
        //return radius * exp( angle * i )
        Complex i = Cartesian(0, 1);
        Complex z = i.$times(angle);
        Complex exp = package$.MODULE$.exp(z);
        return exp.$times(radius);
    }

}
