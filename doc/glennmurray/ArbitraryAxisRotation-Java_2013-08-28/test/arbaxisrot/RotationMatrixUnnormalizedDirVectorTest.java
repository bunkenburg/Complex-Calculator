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

import junit.framework.*;


/**
 * <p>A class to test the RotationMatrixUnnormalizedDirVector class. Run with:
 * </p>
  * <pre>
 * java -cp classes:lib/junit_3.8.1-mac.jar junit.swingui.TestRunner    \
 *                     arbaxisrot.RotationMatrixUnnormalizedDirVectorTest
 * </pre>
 *
 * @author Glenn Murray
 */
public class RotationMatrixUnnormalizedDirVectorTest extends TestCase {

    // Static Initialization--------------------------------------------
    /** For debugging. */
    private static final boolean LOG = false;

    /** How close a double must be to a double to be "equal". */
    private static final double TOLERANCE = RotationMatrix.TOLERANCE;


    // Instance Variables-----------------------------------------------

    private double pi = Math.PI;
    /** See {@link RotationMatrixUnnormalizedDirVector}. */
    private double a = 0;
    private double b = 0;
    private double c = 0;
    private double u = 1;
    private double v = 0;
    private double w = 0;
    private double theta = 0;

    // Constructors-----------------------------------------------------

    /**
     * For JUnit.
     */
    public RotationMatrixUnnormalizedDirVectorTest(String name) {
        super(name);
    }

    // JUnit Methods----------------------------------------------------
    protected void setUp() {
        pi = Math.PI;
        a = 0;
        b = 0;
        c = 0;
        u = 0;
        v = 0;
        w = 0;
        theta = 0;
    }

    /**
     * For JUnit
     */
    public static Test suite() {
        return new TestSuite(arbaxisrot.RotationMatrixUnnormalizedDirVectorTest.class);
    }

    // Methods----------------------------------------------------------

    /**
     * Test the constructor.
     */
    public void testRotationMatrixFull() {
        u = 2;
        RotationMatrixUnnormalizedDirVector
                rM = new RotationMatrixUnnormalizedDirVector(a, b, c, u, v, w, theta);
        // This should be the identity matrix
        double[][] m = rM.getMatrix();
        for(int i=0; i<m.length; i++) {
            for(int j=0; j<m.length; j++) {
                if(j==i) {
                    assertEquals(m[i][j], 1, TOLERANCE);
                } else {
                    assertEquals(m[i][j], 0, TOLERANCE);
                }
            }
        }
    }

    /**
     * Test rotation about the x-axis.
     */
    public void testXAxisRotation() {
        u = 3;
        theta = pi;
        double[] point = new double[] {1, 1, 0};
        RotationMatrixUnnormalizedDirVector
                rM = new RotationMatrixUnnormalizedDirVector(a, b, c, u, v, w, theta);
        double[] result = rM.timesXYZ(point);
        double[] expect = new double[] {1, -1, 0, 1};
        for(int i=0; i<result.length; i++) {
            assertEquals(expect[i], result[i], TOLERANCE);
        }

        theta = -pi;
        rM = new RotationMatrixUnnormalizedDirVector(a, b, c, u, v, w, theta);
        result = rM.timesXYZ(point);
        expect = new double[] {1, -1, 0, 1};
        for(int i=0; i<result.length; i++) {
            assertEquals("coord: "+i, expect[i], result[i], TOLERANCE);
        }

        theta = pi/2;
        rM = new RotationMatrixUnnormalizedDirVector(a, b, c, u, v, w, theta);
        result = rM.timesXYZ(point);
        expect = new double[] {1, 0, 1, 1};
        for(int i=0; i<result.length; i++) {
            assertEquals("coord: "+i, expect[i], result[i], TOLERANCE);
        }

        theta = -pi/2;
        rM = new RotationMatrixUnnormalizedDirVector(a, b, c, u, v, w, theta);
        result = rM.timesXYZ(point);
        expect = new double[] {1, 0, -1, 1};
        for(int i=0; i<result.length; i++) {
            assertEquals("coord: "+i, expect[i], result[i], TOLERANCE);
        }

        theta = pi/4;
        rM = new RotationMatrixUnnormalizedDirVector(a, b, c, u, v, w, theta);
        result = rM.timesXYZ(point);
        expect = new double[] {1, Math.sqrt(2)/2, Math.sqrt(2)/2, 1};
        for(int i=0; i<result.length; i++) {
            assertEquals("coord: "+i, expect[i], result[i], TOLERANCE);
        }

        theta = -3*pi/4;
        rM = new RotationMatrixUnnormalizedDirVector(a, b, c, u, v, w, theta);
        result = rM.timesXYZ(point);
        expect = new double[] {1, -Math.sqrt(2)/2, -Math.sqrt(2)/2, 1};
        for(int i=0; i<result.length; i++) {
            assertEquals("coord: "+i, expect[i], result[i], TOLERANCE);
        }

        u = -1;
        theta = 3*pi/4;
        rM = new RotationMatrixUnnormalizedDirVector(a, b, c, u, v, w, theta);
        result = rM.timesXYZ(point);
        expect = new double[] {1, -Math.sqrt(2)/2, -Math.sqrt(2)/2, 1};
        for(int i=0; i<result.length; i++) {
            assertEquals("coord: "+i, expect[i], result[i], TOLERANCE);
        }
    }

    /**
     * Test rotation about the y-axis.
     */
    public void testYAxisRotation() {
        v = 1;
        theta = 2*pi;
        double[] point = new double[] {1, 1, 0};
        RotationMatrixUnnormalizedDirVector
                rM = new RotationMatrixUnnormalizedDirVector(a, b, c, u, v, w, theta);
        double[] result = rM.timesXYZ(point);
        double[] expect = new double[] {1, 1, 0, 1};
        for(int i=0; i<result.length; i++) {
            assertEquals(expect[i], result[i], TOLERANCE);
        }

        theta = 12*pi;
        rM = new RotationMatrixUnnormalizedDirVector(a, b, c, u, v, w, theta);
        //rM.logMatrix();
        result = rM.timesXYZ(point);
        expect = new double[] {1, 1, 0, 1};
        for(int i=0; i<result.length; i++) {
            assertEquals("coord: "+i, expect[i], result[i], TOLERANCE);
        }

        theta = pi;
        rM = new RotationMatrixUnnormalizedDirVector(a, b, c, u, v, w, theta);
        //rM.logMatrix();
        result = rM.timesXYZ(point);
        expect = new double[] {-1, 1, 0, 1};
        for(int i=0; i<result.length; i++) {
            assertEquals("coord: "+i, expect[i], result[i], TOLERANCE);
        }

        theta = pi/2;
        rM = new RotationMatrixUnnormalizedDirVector(a, b, c, u, v, w, theta);
        result = rM.timesXYZ(point);
        expect = new double[] {0, 1, -1, 1};
        for(int i=0; i<result.length; i++) {
            assertEquals("coord: "+i, expect[i], result[i], TOLERANCE);
        }

        theta = pi/4;
        rM = new RotationMatrixUnnormalizedDirVector(a, b, c, u, v, w, theta);
        result = rM.timesXYZ(point);
        expect = new double[] {Math.sqrt(2)/2, 1, -Math.sqrt(2)/2, 1};
        for(int i=0; i<result.length; i++) {
            assertEquals("coord: "+i, expect[i], result[i], TOLERANCE);
        }
    }

    /**
     * Test rotation about the z-axis.
     */
    public void testZAxisRotation() {
        w = 1;
        theta = 2*pi;
        double[] point = new double[] {1, 1, 0};
        RotationMatrixUnnormalizedDirVector
                rM = new RotationMatrixUnnormalizedDirVector(a, b, c, u, v, w, theta);
        double[] result = rM.timesXYZ(point);
        double[] expect = new double[] {1, 1, 0, 1};
        for(int i=0; i<result.length; i++) {
            assertEquals(expect[i], result[i], TOLERANCE);
        }

        theta = pi;
        rM = new RotationMatrixUnnormalizedDirVector(a, b, c, u, v, w, theta);
        //rM.logMatrix();
        result = rM.timesXYZ(point);
        expect = new double[] {-1, -1, 0, 1};
        for(int i=0; i<result.length; i++) {
            assertEquals("coord: "+i, expect[i], result[i], TOLERANCE);
        }

        theta = pi/2;
        rM = new RotationMatrixUnnormalizedDirVector(a, b, c, u, v, w, theta);
        //rM.logMatrix();
        result = rM.timesXYZ(point);
        expect = new double[] {-1, 1, 0, 1};
        for(int i=0; i<result.length; i++) {
            assertEquals("coord: "+i, expect[i], result[i], TOLERANCE);
        }

        theta = pi/4;
        rM = new RotationMatrixUnnormalizedDirVector(a, b, c, u, v, w, theta);
        result = rM.timesXYZ(point);
        expect = new double[] {0, Math.sqrt(2), 0, 1};
        for(int i=0; i<result.length; i++) {
            assertEquals("coord: "+i, expect[i], result[i], TOLERANCE);
        }

        theta = 3*pi/4;
        rM = new RotationMatrixUnnormalizedDirVector(a, b, c, u, v, w, theta);
        result = rM.timesXYZ(point);
        expect = new double[] {-Math.sqrt(2), 0, 0, 1};
        for(int i=0; i<result.length; i++) {
            assertEquals("coord: "+i, expect[i], result[i], TOLERANCE);
        }

        theta = -3*pi/4;
        rM = new RotationMatrixUnnormalizedDirVector(a, b, c, u, v, w, theta);
        result = rM.timesXYZ(point);
        expect = new double[] {0, -Math.sqrt(2), 0, 1};
        for(int i=0; i<result.length; i++) {
            assertEquals("coord: "+i, expect[i], result[i], TOLERANCE);
        }
    }

    /**
     * Test rotation about a line in the xy-plane.
     */
    public void testXYLineRotation() {
        b = 1;                  // A line along y=1 and z=0.
        u = 1;
        theta = 2*pi;
        double[] point = new double[] {1, 2, 0};
        RotationMatrixUnnormalizedDirVector
                rM = new RotationMatrixUnnormalizedDirVector(a, b, c, u, v, w, theta);
        double[] result = rM.timesXYZ(point);
        double[] expect = new double[] {1, 2, 0, 1};
        for(int i=0; i<result.length; i++) {
            assertEquals(expect[i], result[i], TOLERANCE);
        }

        theta = pi;
        rM = new RotationMatrixUnnormalizedDirVector(a, b, c, u, v, w, theta);
        //rM.logMatrix();
        result = rM.timesXYZ(point);
        expect = new double[] {1, 0, 0, 1};
        for(int i=0; i<result.length; i++) {
            assertEquals("coord: "+i, expect[i], result[i], TOLERANCE);
        }

        theta = pi/2;
        rM = new RotationMatrixUnnormalizedDirVector(a, b, c, u, v, w, theta);
        //rM.logMatrix();
        result = rM.timesXYZ(point);
        expect = new double[] {1, 1, 1, 1};
        for(int i=0; i<result.length; i++) {
            assertEquals("coord: "+i, expect[i], result[i], TOLERANCE);
        }

        theta = -pi/2;
        rM = new RotationMatrixUnnormalizedDirVector(a, b, c, u, v, w, theta);
        result = rM.timesXYZ(point);
        expect = new double[] {1, 1, -1, 1};
        for(int i=0; i<result.length; i++) {
            assertEquals("coord: "+i, expect[i], result[i], TOLERANCE);
        }

        theta = pi/4;
        rM = new RotationMatrixUnnormalizedDirVector(a, b, c, u, v, w, theta);
        result = rM.timesXYZ(point);
        expect = new double[] {1, 1+Math.sqrt(2)/2, Math.sqrt(2)/2, 1};
        for(int i=0; i<result.length; i++) {
            assertEquals("coord: "+i, expect[i], result[i], TOLERANCE);
        }
    }

    /**
     * Test the product formula (simplified matrix * xyz) from the
     * paper.
     */
    public void testRotPointFromFormula() {
        // We compare to multiplication using timesXYZ().
        for(int j=0; j<20; j++) {
            // Get some initial values.
            a = getRandom();
            b = getRandom();
            c = getRandom();
            u = getRandom();
            v = getRandom();
            w = getRandom();
            double x = getRandom();
            double y = getRandom();
            double z = getRandom();
            theta = getRandom();

            // Build the initialized matrix.
            RotationMatrixUnnormalizedDirVector rMInit
                = new RotationMatrixUnnormalizedDirVector(a, b, c, u, v, w, theta);

            // Compare formula to matrix multiplication.
            double[] expect = rMInit.timesXYZ(new double[] {x, y, z});
            double[] result = RotationMatrixUnnormalizedDirVector
                    .rotPointFromFormula(a, b, c, u, v, w, x, y, z, theta);

            log("testRotPointFromFormula() expect: "+arrayToString(expect));
            log("testRotPointFromFormula() result: "+arrayToString(result));

            for(int i=0; i<result.length; i++) {
                assertEquals("coord: "+i, expect[i], result[i], TOLERANCE);
            }
        }
    }


    /**
     * Test distanceFromAxis().
     */
    public void testDistanceFromAxis() {

        u = 1;
        double[] point = new double[] {0, 0, 0};
        double dist = distanceFromAxis(a, b, c, u, v, w, point[0], point[1],
                point[2]);
        assertEquals(0, dist, TOLERANCE);

        point = new double[] {1, 1, 0};
        dist = distanceFromAxis(a, b, c, u, v, w, point[0], point[1], point[2]);
        assertEquals(1, dist, TOLERANCE);

        b = 1;                  // Line along y=1.
        point = new double[] {0, 0, 1};
        dist = distanceFromAxis(a, b, c, u, v, w, point[0], point[1], point[2]);
        assertEquals(Math.sqrt(2), dist, TOLERANCE);
    }

    /**
     * Test some random rotations.  The idea here is that if you fix
     * the axis and the point to be rotated and vary the angle of
     * rotation, then the rotated points are always the same distance
     * from the axis.  Moreover, they are the same distance to a fixed
     * point on the axis.
     */
    public void testStableRotations() {

        // Get a random point (x, y, z) and axis through (a, b, c).
        double x = getRandom();
        double y = getRandom();
        double z = getRandom();
        a = getRandom();
        b = getRandom();
        c = getRandom();
        u = getRandom();
        v = getRandom();
        w = getRandom();

        double distAxis = distanceFromAxis(a, b, c, u, v, w, x, y, z);
        double distPt2 = (a-x)*(a-x) + (b-y)*(b-y) + (c-z)*(c-z);

        double[] result;
        double resultDistAxis;
        double resultDistPt2;
        for(int i=0; i<200; i++) {
            theta = getRandom()*pi;
            RotationMatrixUnnormalizedDirVector rM =
                    new RotationMatrixUnnormalizedDirVector(a, b, c, u, v, w, theta);
            result = rM.timesXYZ(x, y, z);
            resultDistAxis = distanceFromAxis(a, b, c, u, v, w,
                    result[0], result[1], result[2]);
            // Rotations shouldn't change the distance from the axis.
            assertEquals(distAxis, resultDistAxis, TOLERANCE);

            // Rotations shouldn't change the distance from a fixed
            // point (a, b, c) on the axis.
            resultDistPt2 = (a-result[0])*(a-result[0])
                + (b-result[1])*(b-result[1]) + (c-result[2])*(c-result[2]);
            assertEquals(distPt2, resultDistPt2, TOLERANCE);
        }
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
     * This method assumes that constructors in derived classes check that the
     * length of the direction vector <u,v,w> is nonzero.
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
     */
    public double distanceFromAxis(double a, double b, double c,
                                   double u, double v, double w,
                                   double x, double y, double z) {
        // Some intermediate values.
        double n1 = v *(c - z) - w *(b - y);
        double n2 = -(u *(c - z) - w *(a - x));
        double n3 = u *(b - y) - v *(a - x);
        double l = Math.sqrt(
                u * u + v * v + w * w);

        return Math.sqrt(n1*n1 + n2*n2 + n3*n3)/l;
    }


    /**
     * Helper method to get a vector of random number in a range.
     * @return A random number from (-x, x), x = 10.
     */
    public double getRandom() {
        return 20*Math.random() - 10;
    }

    /**
     * Helper method for logging.
     * @param a An array to print out.
     * @return The array contents as a String.
     */
    private String arrayToString(double[] a) {
        StringBuilder buffy = new StringBuilder();
        for (int i=0; i<a.length; i++) {
            buffy.append(a[i]).append("\t");
        }
        return buffy.toString();
    }

    /**
     * Private method for logging.
     *
     * @param l What to print out.
     */
    private void log(String l) {
        if (LOG) {
            System.out.println("RotationMatrixFullTest." + l);
        }
    }
}
