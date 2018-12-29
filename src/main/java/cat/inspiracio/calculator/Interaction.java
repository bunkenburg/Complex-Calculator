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

enum Interaction{

    DRAW("Draw"),
    MOVE("Move"),
    LINE("Line"),
    RECTANGLE("Rectangle"),
    CIRCLE("Circle"),
    GRID("Grid"),
    SQUARE("Square");

    /** visible in menu */
    private String n;

    Interaction(String n){this.n = n;}

    @Override public String toString() { return n; }

    public static Interaction parse(String n){
        for( Interaction i : Interaction.values() ){
            if(n.equals(i.n))
                return i;
        }
        throw new IllegalArgumentException(n);
    }

}

