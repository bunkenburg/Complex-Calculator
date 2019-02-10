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
 * A class to do 3D rotations of a point about a line. See
 * <a
 * href="https://sites.google.com/site/glennmurray/Home/rotation-matrices-and-formulas">
 * Rotation Matrices and Formulas</a>
 *
 * @author Glenn Murray
 */
public class RotationMatrix {
	
    // Static initialization---------------------------------------------
    /** For debugging. */
    private static final boolean LOG = false;

    /** How close a double must be to a double to be "equal". */
    public static final double TOLERANCE = 1E-9;


    // Instance variables------------------------------------------------

    /** The rotation matrix.  This is a 4x4 matrix. */
    protected double[][] matrix;

    /** The 1,1 entry in the matrix. */
    protected double m11;
    protected double m12; 
    protected double m13; 
    protected double m14; 
    protected double m21; 
    protected double m22; 
    protected double m23; 
    protected double m24; 
    protected double m31;
    protected double m32;
    protected double m33;
    protected double m34;


    // Constructors------------------------------------------------------

    /**
     * Default constructor.
     */
    public RotationMatrix() {}


    /**
     * Build a rotation matrix for rotations about the line through (a, b, c)
     * parallel to &lt u, v, w &gt by the angle theta.
     *
     * @param a x-coordinate of a point on the line of rotation.
     * @param b y-coordinate of a point on the line of rotation.
     * @param c z-coordinate of a point on the line of rotation.
     * @param uUn x-coordinate of the line's direction vector (unnormalized).
     * @param vUn y-coordinate of the line's direction vector (unnormalized).
     * @param wUn z-coordinate of the line's direction vector (unnormalized).
     * @param theta The angle of rotation, in radians.
     */
    public RotationMatrix(double a,
                          double b,
                          double c,
                          double uUn,
                          double vUn,
                          double wUn,
                          double theta) {
        double l;
        if ( (l = longEnough(uUn, vUn, wUn)) < 0) {
            System.out.println("RotationMatrix: direction vector too short!");
            return;             // Don't bother.
        }

        // In this instance we normalize the direction vector.
        double u = uUn/l;
        double v = vUn/l;
        double w = wUn/l;

        // Set some intermediate values.
        double u2 = u*u;
        double v2 = v*v;
        double w2 = w*w;
        double cosT = Math.cos(theta);
        double oneMinusCosT = 1-cosT;
        double sinT = Math.sin(theta);

        // Build the matrix entries element by element.
        m11 = u2 + (v2 + w2) * cosT;
        m12 = u*v * oneMinusCosT - w*sinT;
        m13 = u*w * oneMinusCosT + v*sinT;
        m14 = (a*(v2 + w2) - u*(b*v + c*w))*oneMinusCosT
                + (b*w - c*v)*sinT;

        m21 = u*v * oneMinusCosT + w*sinT;
        m22 = v2 + (u2 + w2) * cosT;
        m23 = v*w * oneMinusCosT - u*sinT;
        m24 = (b*(u2 + w2) - v*(a*u + c*w))*oneMinusCosT
                + (c*u - a*w)*sinT;

        m31 = u*w * oneMinusCosT - v*sinT;
        m32 = v*w * oneMinusCosT + u*sinT;
        m33 = w2 + (u2 + v2) * cosT;
        m34 = (c*(u2 + v2) - w*(a*u + b*v))*oneMinusCosT
                + (a*v - b*u)*sinT;

        if(LOG) logMatrix();
    }


    // Methods-----------------------------------------------------------

    /**
     * Multiply this {@link RotationMatrix} times the point (x, y, z, 1),
     * representing a point P(x, y, z) in homogeneous coordinates.  The final
     * coordinate, 1, is assumed.
     *
     * @param x The point's x-coordinate.
     * @param y The point's y-coordinate.
     * @param z The point's z-coordinate.
     * @return The product, in a vector <#, #, #>, representing the
     * rotated point.
     */
    public double[] timesXYZ(double x, double y, double z) {
        double[] p = new double[3];
        p[0] = m11*x + m12*y + m13*z + m14;
        p[1] = m21*x + m22*y + m23*z + m24;
        p[2] = m31*x + m32*y + m33*z + m34;

        return p;
    }


    /**
     * Multiply this matrix times the given coordinates (x, y, z, 1),
     * representing a point P(x, y, z).  This delegates to
     * {@link #timesXYZ(double, double, double)}, calling
     * <pre>
     *     timesXYZ(point[0], point[1], point[2]);
     * </pre>
     * thus it works with points given in homogeneous coordinates.
     *
     * @param point The point as double[] {x, y, z}.
     * @return The product, in a vector <#, #, #>, representing the
     * rotated point.
     */
    public double[] timesXYZ(double[] point) {
        return timesXYZ(point[0], point[1], point[2]);
    }


    /**
     * <p>
     * Compute the rotated point from the formula given in the paper, as opposed
     * to multiplying this matrix by the given point.  Theoretically this should
     * give the same answer as {@link #timesXYZ(double[])}.  For repeated
     * calculations this will be slower than using {@link #timesXYZ(double[])}
     * because, in effect, it repeats the calculations done in the constructor.
     * </p>
     * <p>This method is static partly to emphasize that it does not
     * mutate an instance of {@link RotationMatrix}, even though it uses
     * the same parameter names as the the constructor.</p>
     *
     * @param a x-coordinate of a point on the line of rotation.
     * @param b y-coordinate of a point on the line of rotation.
     * @param c z-coordinate of a point on the line of rotation.
     * @param u x-coordinate of the line's direction vector.  This direction
     *          vector will be normalized.
     * @param v y-coordinate of the line's direction vector.
     * @param w z-coordinate of the line's direction vector.
     * @param x The point's x-coordinate.
     * @param y The point's y-coordinate.
     * @param z The point's z-coordinate.
     * @param theta The angle of rotation, in radians.
     * @return The product, in a vector <#, #, #>, representing the
     * rotated point.
     */
    public static double[] rotPointFromFormula(double a, double b, double c,
                                               double u, double v, double w,
                                               double x, double y, double z,
                                               double theta) {
        // We normalize the direction vector.
        double l;
        if ( (l = longEnough(u, v, w)) < 0) {
            System.out.println("RotationMatrix direction vector too short");
            return null;             // Don't bother.
        }
        // Normalize the direction vector.
        u = u/l;  // Note that is not "this.u".
        v = v/l;
        w = w/l;
        // Set some intermediate values.
        double u2 = u*u;
        double v2 = v*v;
        double w2 = w*w;
        double cosT = Math.cos(theta);
        double oneMinusCosT = 1 - cosT;
        double sinT = Math.sin(theta);

        // Use the formula in the paper.
        double[] p = new double[3];
        p[0] = (a*(v2 + w2) - u*(b*v + c*w - u*x - v*y - w*z)) * oneMinusCosT
                + x*cosT
                + (-c*v + b*w - w*y + v*z)*sinT;

        p[1] = (b*(u2 + w2) - v*(a*u + c*w - u*x - v*y - w*z)) * oneMinusCosT
                + y*cosT
                + (c*u - a*w + w*x - u*z)*sinT;

        p[2] = (c*(u2 + v2) - w*(a*u + b*v - u*x - v*y - w*z)) * oneMinusCosT
                + z*cosT
                + (-b*u + a*v - v*x + u*y)*sinT;

        return p;
    }


    /**
     * <p>Compute the rotated point from the formula given in the paper,
     * as opposed to multiplying this matrix by the given point.</p>
     * 
     * <p>This delegates to {@link #rotPointFromFormula(double, double, double,
     * double, double, double, double, double, double, double)}. </p>.
     *
     * @param rInfo The information for the rotation as an array
     * [a,b,c,u,v,w,x,y,z,theta].
     * @return The product, in a vector <#, #, #>, representing the
     * rotated point.
     */
    public double[] rotPointFromFormula(double[] rInfo) {
        return rotPointFromFormula(rInfo[0], rInfo[1], rInfo[2], 
                                   rInfo[3], rInfo[4], rInfo[5], 
                                   rInfo[6], rInfo[7], rInfo[8], rInfo[9]);
    }


    /**
     * Check whether a vector's length is less than {@link #TOLERANCE}.
     *
     * @param u The vector's x-coordinate.
     * @param v The vector's y-coordinate.
     * @param w The vector's z-coordinate.
     * @return length = Math.sqrt(u^2 + v^2 + w^2) if it is greater than
     * {@link #TOLERANCE}, or -1 if not.
     */
    public static double longEnough(double u, double v, double w) {
        double l = Math.sqrt(u*u + v*v + w*w);
        if (l > TOLERANCE) {
            return l;
        } else {
            return -1;
        }
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
 
