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

/** I'm sure this can be a nice class in Scala.
 * Check it's really immutable. */
final class Matrix44{

	/** Not private because it's used from Sphere and others. */
    double data[][] = {new double[4], new double[4], new double[4], new double[4]};

    //Constructors -------------------------------------------------

    /** The 0 matrix */
    Matrix44(){}

    private Matrix44(double ad[][]){
        data = ad;
    }

    //Methods -----------------------------------------------------

    /** rename to * */
    Vector3 multiply(double x, double y, double z){
        return new Vector3(
                data[0][0] * x + data[0][1] * y + data[0][2] * z + data[0][3],
                data[1][0] * x + data[1][1] * y + data[1][2] * z + data[1][3],
                data[2][0] * x + data[2][1] * y + data[2][2] * z + data[2][3]
        );
    }

    /** rename to * */
    Vector3 multiply(Vector3 v){
        return new Vector3(
                data[0][0] * v.x + data[0][1] * v.y + data[0][2] * v.z + data[0][3],
                data[1][0] * v.x + data[1][1] * v.y + data[1][2] * v.z + data[1][3],
                data[2][0] * v.x + data[2][1] * v.y + data[2][2] * v.z + data[2][3]
        );
    }

    void preRot(char c, double d){
        double d1 = Math.cos(d);
        double d2 = Math.sin(d);
        byte b;
        switch(c){
            case 'x':
                b = 1;
                break;
            case 'y':
                b = 2;
                break;
            case 'z':
                b = 0;
                break;
            default:
                b = 0;
        }
        int i = (b + 1) % 3;
        for(int j = 0; j <= 3; j++){
            double d3 = d1 * data[b][j] + d2 * data[i][j];
            double d4 = d1 * data[i][j] - d2 * data[b][j];
            data[b][j] = d3;
            data[i][j] = d4;
        }
    }

    void postRot(char c, double d){
        double d1 = Math.cos(d);
        double d2 = Math.sin(d);
        byte b;
        switch(c){
            case 'x':
                b = 1;
                break;
            case 'y':
                b = 2;
                break;
            case 'z':
                b = 0;
                break;
            default:
                b = 0;
        }
        int i = (b + 1) % 3;
        for(int j = 0; j <= 3; j++){
            double d3 = d1 * data[j][b] - d2 * data[j][i];
            double d4 = d2 * data[j][b] + d1 * data[j][i];
            data[j][b] = d3;
            data[j][i] = d4;
        }
    }

    @Override public String toString(){
        return "\n[" + data[0][0] + ", " + data[0][1] + ", " + data[0][2] + ", " + data[0][3] + ";\n" + data[1][0] + ", " + data[1][1] + ", " + data[1][2] + ", " + data[1][3] + ";\n" + data[2][0] + ", " + data[2][1] + ", " + data[2][2] + ", " + data[2][3] + ";\n" + data[3][0] + ", " + data[3][1] + ", " + data[3][2] + ", " + data[3][3] + "]";
    }

    static Matrix44 tran3(Vector3 v){
        return tran3(v.x, v.y, v.z);
    }

    static Matrix44 tran3(double x, double y, double z){
        Matrix44 m = new Matrix44();
        double ad[][] = m.data;
        for(int i = 0; i <= 3; i++){
            for(int j = 0; j <= 3; j++){
                ad[i][j] = 0.0D;
                ad[i][i] = 1.0D;
            }
            ad[0][3] = -x;
            ad[1][3] = -y;
            ad[2][3] = -z;
        }
        return m;
    }

    /** Make this matrix be the uunit matrix. */
    void unit(){
        for(int i = 0; i <= 3; i++){
            for(int j = 0; j <= 3; j++)
                data[i][j] = i != j ? 0 : 1;
        }
    }

}