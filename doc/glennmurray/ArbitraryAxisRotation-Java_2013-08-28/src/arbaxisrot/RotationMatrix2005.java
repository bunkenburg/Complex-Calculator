/*   Copyright 2011 Glenn Murray
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package arbaxisrot;

import java.text.DecimalFormat;


/**
 * This is the 2005 version of a class to do 3D rotations of a point about a
 * line.  The formulas give the correct answers but are not in an optimal
 * format.  Use {@link RotationMatrix}.
 * 
 * @version $Revision: 1.2 $ <BR>
 *          $Date: 2005/09/07 20:25:19 $
 */
@Deprecated
public class RotationMatrix2005 {
	
    // Static initialization---------------------------------------------
    /** For debugging. */
    private static final boolean LOG = false;

    // Instance variables------------------------------------------------

    /** The rotation matrix.  This is a 4x4 matrix. */
    private double[][] matrix;

    // The parameters for the rotation.
    /** x-coordinate of a point on the line of rotation. */
    private double a;

    /** y-coordinate of a point on the line of rotation. */
    private double b;

    /** z-coordinate of a point on the line of rotation. */
    private double c;

    /** x-coordinate of the line's direction vector. */
    private double u;

    /** y-coordinate of the line's direction vector. */
    private double v;

    /** z-coordinate of the line's direction vector. */
    private double w;

    // Some intermediate values...
    /** An intermediate value used in computations (u^2). */
    private double u2;     
    private double v2;     
    private double w2;     
    private double cosT;   
    private double sinT;   
    private double l2;
    /** The length of the direction vector. */
    private double l;      
    
    /** The 1,1 entry in the matrix. */
    private double m11;
    private double m12; 
    private double m13; 
    private double m14; 
    private double m21; 
    private double m22; 
    private double m23; 
    private double m24; 
    private double m31;
    private double m32;
    private double m33;
    private double m34;


    // Constructors------------------------------------------------------

    /**
     * Build a rotation matrix for rotations about the line through
     * $P_1(a, b, c)$ parallel to $\langle u, v, w\rangle$ by the
     * angle $\theta$.
     *
     * @param a x-coordinate of a point on the line of rotation.
     * @param b y-coordinate of a point on the line of rotation.
     * @param c z-coordinate of a point on the line of rotation.
     * @param u x-coordinate of the line's direction vector.
     * @param v y-coordinate of the line's direction vector.
     * @param w z-coordinate of the line's direction vector.
     * @param theta The angle of rotation.
     */
    public RotationMatrix2005(double a,
                          double b,
                          double c,
                          double u,
                          double v,
                          double w,
                          double theta) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.u = u;
        this.v = v;
        this.w = w;
        // Set some intermediate values.
        u2 = u*u;
        v2 = v*v;
        w2 = w*w;
        cosT = Math.cos(theta);
        sinT = Math.sin(theta);
        l2 = u2 + v2 + w2;
        l =  Math.sqrt(l2);

        if(l2 < 0.000000001) {
            System.out.println("RotationMatrix: direction vector too short!");
            return;             // Don't bother.
        }

        // Build the matrix entries element by element. 
        m11 = (u2 + (v2 + w2) * cosT)/l2;
        m12 = (u*v * (1 - cosT) - w*l*sinT)/l2;
        m13 = (u*w * (1 - cosT) + v*l*sinT)/l2;
        m14 = (a*(v2 + w2) - u*(b*v + c*w) 
            + (u*(b*v + c*w) - a*(v2 + w2))*cosT + (b*w - c*v)*l*sinT)/l2;
        
        m21 = (u*v * (1 - cosT) + w*l*sinT)/l2;
        m22 = (v2 + (u2 + w2) * cosT)/l2;
        m23 = (v*w * (1 - cosT) - u*l*sinT)/l2;
        m24 = (b*(u2 + w2) - v*(a*u + c*w) 
            + (v*(a*u + c*w) - b*(u2 + w2))*cosT + (c*u - a*w)*l*sinT)/l2;

        m31 = (u*w * (1 - cosT) - v*l*sinT)/l2;
        m32 = (v*w * (1 - cosT) + u*l*sinT)/l2;
        m33 = (w2 + (u2 + v2) * cosT)/l2;
        m34 = (c*(u2 + v2) - w*(a*u + b*v) 
            + (w*(a*u + b*v) - c*(u2 + v2))*cosT + (a*v - b*u)*l*sinT)/l2;

        if(LOG) logMatrix();
    }


    // Methods-----------------------------------------------------------

    /**
     * Multiply this matrix times the given coordinates (x, y, z, 1),
     * representing a point P(x, y, z)..
     *
     * @param x The point's x-coordinate.
     * @param y The point's x-coordinate.
     * @param z The point's x-coordinate.
     * @return The product, in a vector [#, #, #, 1], representing the
     * rotated point.
     */
    public double[] timesXYZ(double x, double y, double z) {
        double[] p = new double[4];
        p[0] = m11*x + m12*y + m13*z + m14;
        p[1] = m21*x + m22*y + m23*z + m24;
        p[2] = m31*x + m32*y + m33*z + m34;
        p[3] = 1;

        return p;
    }

    /**
     * Multiply this matrix times the given coordinates (x, y, z, 1),
     * representing a point P(x, y, z)..
     *
     * @param point The point as double[] {x, y, z}. 
     * @return The product, in a vector [#, #, #, 1], representing the
     * rotated point.
     */
    public double[] timesXYZ(double[] point) {
        return timesXYZ(point[0], point[1], point[2]);
    }

    /**
     * Compute the rotated point from the formula given in the paper,
     * as opposed to multiplying this matrix by the given point.
     * Theoretically this should give the same answer as {@link
     * #timesXYZ(double[])}.  For repeated calculations this will be
     * slower than using {@link #timesXYZ(double[])} because in effect
     * it repeats the calculations done in the constructor.
     *
     * @param x The point's x-coordinate.
     * @param y The point's x-coordinate.
     * @param z The point's x-coordinate.
     * @return The product, in a vector [#, #, #], representing the
     * rotated point.
     */
    public double[] rotPointFromFormula(double x, double y, double z) {

        // Compute the formula
        double[] f = new double[3];
        f[0] = a*(v2 + w2) + u*(-b*v - c*w + u*x + v*y + w*z) 
            + (-a*(v2 + w2) + u*(b*v + c*w - v*y - w*z) + (v2 + w2)*x)*cosT
            + l*(-c*v + b*w - w*y + v*z)*sinT;
        f[0] = f[0]/l2;

        f[1] = b*(u2 + w2) + v*(-a*u - c*w + u*x + v*y + w*z) 
            + (-b*(u2 + w2) + v*(a*u + c*w - u*x - w*z) + (u2 + w2)*y)*cosT
            + l*(c*u - a*w + w*x - u*z)*sinT;
        f[1] = f[1]/l2;

        f[2] = c*(u2 + v2) + w*(-a*u - b*v + u*x + v*y + w*z) 
            + (-c*(u2 + v2) + w*(a*u + b*v - u*x - v*y) + (u2 + v2)*z)*cosT
            + l*(-b*u + a*v - v*x + u*y)*sinT;
        f[2] = f[2]/l2;

        return f;
    }

    /**
     * Compute the rotated point from the formula given in the paper,
     * as opposed to multiplying this matrix by the given point.
     * Theoretically this should give the same answer as {@link
     * #timesXYZ(double[])}.  For repeated calculations this will be
     * slower than using {@link #timesXYZ(double[])} because in effect
     * it repeats the calculations done in the constructor.
     *
     * @param point A point (x, y, z).
     * @return The product, in a vector [#, #, #], representing the
     * rotated point.
     */
    public double[] rotPointFromFormula(double[] point) {
        return rotPointFromFormula(point[0], point[1], point[2]);
    }

    /**
     * Find the distance from the line representing the axis of
     * rotation of this matrix and a given point (x, y, z).  This is
     * useful for testing.  We use the ff. formula for the distance
     * (cf. "Point-Line distance in MathWorld).
     *
     * <pre>
     *                |(u, v, w) x (a-x, b-y, c-z)|
     *                -----------------------------
     *                        |(u, v, w)|
     * </pre>
     *
     * @param x The point's x-coordinate.
     * @param y The point's x-coordinate.
     * @param z The point's x-coordinate.
     * @return The distance from the point (x, y, z) to the axis of
     * rotation ( a line through (a, b, c) with direction (u, v, w)).
     */
    public double distanceFromAxis(double x, double y, double z) {
        if(l2 < 0.000000001) {
            System.out.println("RotationMatrix: direction vector too short!");
            return 0;             // Don't bother.
        }

        // Some intermediate values.
        double n1 = v*(c - z) - w*(b - y);
        double n2 = -(u*(c - z) - w*(a - x));
        double n3 = u*(b - y) - v*(a - x);

        return Math.sqrt(n1*n1 + n2*n2 + n3*n3)/l;
    }
    

    /**
     * Get the matrix.
     * @return The matrix as a 4x4 double[][].
     */
    public double[][] getMatrix() {
        if (matrix==null) {
            matrix = new double[][] {{m11, m12, m13, m14}, 
                                     {m21, m22, m23, m24},
                                     {m31, m32, m33, m34},
                                     {0,   0,   0,   1}}; 
        }
        return matrix;
    }

    /**
     * Print out the matrix.
     */
    public void logMatrix() {
        DecimalFormat df = new DecimalFormat("#.###");
        System.out.println();
        System.out.println("\t"+df.format(m11)+"\t"+df.format(m12)
                           +"\t"+df.format(m13)+"\t"+df.format(m14));
        System.out.println("\t"+df.format(m21)+"\t"+df.format(m22)
                           +"\t"+df.format(m23)+"\t"+df.format(m24));
        System.out.println("\t"+df.format(m31)+"\t"+df.format(m32)
                           +"\t"+df.format(m33)+"\t"+df.format(m34));
        System.out.println("\t0\t0\t0\t1");
        System.out.println();
    }

    // Inner classes-----------------------------------------------------
}//end class
 
