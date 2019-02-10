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


/**
 * <p>A class to do 3D rotations of a point about a line.</p>
 *
 * <p>This class checks that the formulas in the paper for an unnormalized
 * direction vector are correct.  It is preferable to use {@link RotationMatrix}
 * instead of this class.</p>
 *
 * @author Glenn Murray
 */
public class RotationMatrixUnnormalizedDirVector extends RotationMatrix {

    // Static initialization---------------------------------------------
    /** For debugging. */
    protected static final boolean LOG = false;


    // Instance variables------------------------------------------------
    // Constructors------------------------------------------------------

    /**
     * Build a rotation matrix for rotations about the line through (a, b, c)
     * parallel to &lt u, v, w &gt by the angle theta.
     *
     * @param a x-coordinate of a point on the line of rotation.
     * @param b y-coordinate of a point on the line of rotation.
     * @param c z-coordinate of a point on the line of rotation.
     * @param u x-coordinate of the line's direction vector.
     * @param v y-coordinate of the line's direction vector.
     * @param w z-coordinate of the line's direction vector.
     * @param theta The angle of rotation, in radians.
     */
    public RotationMatrixUnnormalizedDirVector(double a,
                                               double b,
                                               double c,
                                               double u,
                                               double v,
                                               double w,
                                               double theta) {
        double l;
        if ( (l = longEnough(u, v, w)) < 0) {
            System.out.println("RotationMatrixUnnormalizedDirVector direction "
                    +"vector too short");
            return;             // Don't bother.
        }

        // Set some intermediate values.
        double u2 = u*u;
        double v2 = v*v;
        double w2 = w*w;
        double cosT = Math.cos(theta);
        double oneMinusCosT = 1 - cosT;
        double sinT = Math.sin(theta);
        double l2 = u2 + v2 + w2;

        // Build the matrix entries element by element.
        m11 = (u2 + (v2 + w2) * cosT)/l2;
        m12 = (u*v * oneMinusCosT - w*l*sinT)/l2;
        m13 = (u*w * oneMinusCosT + v*l*sinT)/l2;
        m14 = ((a*(v2 + w2) - u*(b*v + c*w)) * oneMinusCosT
            + (b*w - c*v)*l*sinT)/l2;

        m21 = (u*v * oneMinusCosT + w*l*sinT)/l2;
        m22 = (v2 + (u2 + w2) * cosT)/l2;
        m23 = (v*w * oneMinusCosT - u*l*sinT)/l2;
        m24 = ((b*(u2 + w2) - v*(a*u + c*w)) * oneMinusCosT
            + (c*u - a*w)*l*sinT)/l2;

        m31 = (u*w * oneMinusCosT - v*l*sinT)/l2;
        m32 = (v*w * oneMinusCosT + u*l*sinT)/l2;
        m33 = (w2 + (u2 + v2) * cosT)/l2;
        m34 = ((c*(u2 + v2) - w*(a*u + b*v)) * oneMinusCosT
            + (a*v - b*u)*l*sinT)/l2;

        if(LOG) logMatrix();
    }


    // Methods-----------------------------------------------------------

    /**
     * Compute the rotated point from the formula given in the paper,
     * as opposed to multiplying this matrix by the given point.
     * Theoretically this should give the same answer as {@link
     * #timesXYZ(double[])}.  For repeated calculations this will be
     * slower than using {@link #timesXYZ(double[])} because in effect
     * it repeats the calculations done in the constructor.
     *
     *
     * @param a x-coordinate of a point on the line of rotation.
     * @param b y-coordinate of a point on the line of rotation.
     * @param c z-coordinate of a point on the line of rotation.
     * @param u x-coordinate of the line's direction vector.
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
        double l;
        if ( (l = longEnough(u, v, w)) < 0) {
            System.out.println("RotationMatrixUnnormalizedDirVector direction "
                    +"vector too short");
            return null;             // Don't bother.
        }
        double l2 = l*l;
        
        // Set some intermediate values.
        double u2 = u*u;
        double v2 = v*v;
        double w2 = w*w;
        double cosT = Math.cos(theta);
        double oneMinusCosT = 1 - cosT;
        double sinT = Math.sin(theta);

        // Use the formula in the paper.
        double[] p = new double[3];
        p[0] = ((a*(v2 + w2) - u*(b*v + c*w - u*x - v*y - w*z)) * oneMinusCosT
                + l2*x*cosT
                + l*(-c*v + b*w - w*y + v*z)*sinT)/l2;

        p[1] = ((b*(u2 + w2) - v*(a*u + c*w - u*x - v*y - w*z)) * oneMinusCosT
                + l2*y*cosT
                + l*(c*u - a*w + w*x - u*z)*sinT)/l2;

        p[2] = ((c*(u2 + v2) - w*(a*u + b*v - u*x - v*y - w*z)) * oneMinusCosT
                + l2*z*cosT
                + l*(-b*u + a*v - v*x + u*y)*sinT)/l2;

        return p;
    }


    // Inner classes-----------------------------------------------------
}//end class

