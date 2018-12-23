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
//            ECList

import cat.inspiracio.complex.Complex;
import cat.inspiracio.complex.package$;

public abstract class Piclet {

    public ECList getSamples() {
        if(samples == null)
            sample();
        return samples;
    }

    protected abstract void sample();

    public abstract double top();

    public abstract double bottom();

    public abstract double left();

    public abstract double right();

    public Piclet() { }

    static final int SAMPLE_DENSITY = 30;
    protected ECList samples;

    // helpers -------------------------------

    protected double Im(Complex c){ return cat.inspiracio.complex.package$.MODULE$.Im(c); }

    protected double Re(Complex c){
        return cat.inspiracio.complex.package$.MODULE$.Re(c);
    }

    protected double distance(Complex a, Complex ec){
        if( a.isFinite() && ec.isFinite() )
            return Math.sqrt(sqr(Re(a) - Re(ec)) + sqr(Im(a) - Im(ec) ));
        return a.isFinite() != ec.isFinite() ? (1.0D / 0.0D) : 0.0D;
    }

    protected double sqr(double d){return d*d;}

    protected Complex Cartesian(double re, double im){
        //Complex r = package$.MODULE$.double2Complex(re);
        Complex i = package$.MODULE$.i();
        return i.$times(im).$plus(re);
    }

    protected Complex Real(double re){
        Complex r = package$.MODULE$.double2Complex(re);
        return r;
    }

}
