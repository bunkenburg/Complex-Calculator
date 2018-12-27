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


// Referenced classes of package bunkenba.calculator:
//            Matrix44

final class Vector3{

    double x;
    double y;
    double z;

	//Constructors ----------------------------------------
	
    Vector3(double d, double d1, double d2){
        x = d;
        y = d1;
        z = d2;
    }

    //Methods ----------------------------------------------
    
    public String toString(){return "(" + x + ", " + y + ", " + z + ")";}

}