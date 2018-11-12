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

// Referenced classes of package bunkenba.calculator:
//            Vector3

final class Matrix44{

	/** Not private because it's used from Sphere. */
    double data[][] = {new double[4], new double[4], new double[4], new double[4]};

    //Constructors -------------------------------------------------
	
    Matrix44(){}

    Matrix44(Matrix44 matrix44){
        this();
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++)
                data[i][j] = matrix44.data[i][j];
        }
    }

    Matrix44(double ad[][]){
        data = ad;
    }

    //Methods -----------------------------------------------------
    
    Vector3 multiply(double d, double d1, double d2){
        return new Vector3(data[0][0] * d + data[0][1] * d1 + data[0][2] * d2 + data[0][3], data[1][0] * d + data[1][1] * d1 + data[1][2] * d2 + data[1][3], data[2][0] * d + data[2][1] * d1 + data[2][2] * d2 + data[2][3]);
    }

    Vector3 multiply(Vector3 vector3){
        return new Vector3(data[0][0] * vector3.x + data[0][1] * vector3.y + data[0][2] * vector3.z + data[0][3], data[1][0] * vector3.x + data[1][1] * vector3.y + data[1][2] * vector3.z + data[1][3], data[2][0] * vector3.x + data[2][1] * vector3.y + data[2][2] * vector3.z + data[2][3]);
    }

    Matrix44 multiply(Matrix44 matrix44){
        double ad[][] = data;
        double ad1[][] = matrix44.data;
        Matrix44 matrix44_1 = new Matrix44();
        double ad2[][] = matrix44_1.data;
        for(int i = 0; i <= 3; i++){
            for(int j = 0; j <= 3; j++){
                double d = 0.0D;
                for(int k = 0; k <= 3; k++)
                    d += ad[i][k] * ad1[k][j];
                ad2[i][j] = d;
            }
        }
        return matrix44_1;
    }

    static Matrix44 ONE(){
        return new Matrix44(new double[][] {
            new double[] {1.0D, 0, 0, 0}, 
            new double[] {0, 1.0D, 0, 0}, 
            new double[] {0, 0, 1.0D, 0}, 
            new double[] {0, 0, 0, 1.0D}
        });
    }

    void preRot(char c, double d){
        double d1 = Math.cos(d);
        double d2 = Math.sin(d);
        byte byte0;
        switch(c){
        case 120: // 'x'
            byte0 = 1;
            break;
        case 121: // 'y'
            byte0 = 2;
            break;
        case 122: // 'z'
            byte0 = 0;
            break;
        default:
            byte0 = 0;
            break;
        }
        int i = (byte0 + 1) % 3;
        for(int j = 0; j <= 3; j++){
            double d3 = d1 * data[byte0][j] + d2 * data[i][j];
            double d4 = d1 * data[i][j] - d2 * data[byte0][j];
            data[byte0][j] = d3;
            data[i][j] = d4;
        }
    }

    void postRot(char c, double d){
        double d1 = Math.cos(d);
        double d2 = Math.sin(d);
        byte byte0;
        switch(c){
        case 120: // 'x'
            byte0 = 1;
            break;
        case 121: // 'y'
            byte0 = 2;
            break;
        case 122: // 'z'
            byte0 = 0;
            break;
        default:
            byte0 = 0;
            break;
        }
        int i = (byte0 + 1) % 3;
        for(int j = 0; j <= 3; j++){
            double d3 = d1 * data[j][byte0] - d2 * data[j][i];
            double d4 = d2 * data[j][byte0] + d1 * data[j][i];
            data[j][byte0] = d3;
            data[j][i] = d4;
        }
    }

    static Matrix44 rot3(char c, double d){
        Matrix44 matrix44 = new Matrix44();
        double ad[][] = matrix44.data;
        byte byte0;
        switch(c){
        case 120: // 'x'
            byte0 = 0;
            break;
        case 121: // 'y'
            byte0 = 1;
            break;
        case 122: // 'z'
            byte0 = 2;
            break;
        default:
            byte0 = 0;
            break;
        }
        ad[byte0][byte0] = 1.0D;
        ad[3][3] = 1.0D;
        int i = (byte0 + 1) % 3;
        int j = (i + 1) % 3;
        double d1 = Math.cos(d);
        double d2 = Math.sin(d);
        ad[i][i] = d1;
        ad[j][j] = d1;
        ad[i][j] = d2;
        ad[j][i] = -d2;
        return matrix44;
    }

    public String toString(){
        return "\n[" + data[0][0] + ", " + data[0][1] + ", " + data[0][2] + ", " + data[0][3] + ";\n" + data[1][0] + ", " + data[1][1] + ", " + data[1][2] + ", " + data[1][3] + ";\n" + data[2][0] + ", " + data[2][1] + ", " + data[2][2] + ", " + data[2][3] + ";\n" + data[3][0] + ", " + data[3][1] + ", " + data[3][2] + ", " + data[3][3] + "]";
    }

    static Matrix44 tran3(Vector3 vector3){
        return tran3(vector3.x, vector3.y, vector3.z);
    }

    static Matrix44 tran3(double d, double d1, double d2){
        Matrix44 matrix44 = new Matrix44();
        double ad[][] = matrix44.data;
        for(int i = 0; i <= 3; i++){
            for(int j = 0; j <= 3; j++){
                ad[i][j] = 0.0D;
                ad[i][i] = 1.0D;
            }
            ad[0][3] = -d;
            ad[1][3] = -d1;
            ad[2][3] = -d2;
        }
        return matrix44;
    }

    void unit(){
        for(int i = 0; i <= 3; i++){
            for(int j = 0; j <= 3; j++)
                data[i][j] = i != j ? 0 : 1;
        }
    }

}