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
 * <p>A class to test {@link RotationMatrix} by comparing it to the previous
 * version {@link RotationMatrix2005}. Run with: </p>
 *
 * <pre>
 * java -cp classes:lib/junit_3.8.1-mac.jar junit.swingui.TestRunner \ 
 *                     arbaxisrot.RotationMatrixProgressionTest
 * </pre>
 */
public class RotationMatrixProgressionTest extends TestCase {
	
    // Static Initialization--------------------------------------------
    /** For debugging. */
    private static final boolean LOG = false;

    /** How close a double must be to a double to be "equal". */
    private static final double TOLERANCE = RotationMatrix.TOLERANCE;


    // Instance Variables-----------------------------------------------

    private double pi = Math.PI;
    /** See {@link RotationMatrix}. */
    private double a;
	private double b;
	private double c;
	private double u;
	private double v;
	private double w;
    private double angle;


    // Constructors-----------------------------------------------------

    /**
     * For JUnit.
     */
    public RotationMatrixProgressionTest(String name) {
        super(name);
    }


    // JUnit Methods----------------------------------------------------
    protected void setUp() {}

    /**
     * For JUnit
     */
    public static Test suite() {
        return new TestSuite(arbaxisrot.RotationMatrixProgressionTest.class);
    }


    // Methods----------------------------------------------------------

    /**
     * Test that newer {@link RotationMatrix} instances give the same
     * results as {@link RotationMatrix2005}.
     */
    public void testRotationMatrixProgression() {

        int numTrials = 100;
        while (numTrials > 0) {
            numTrials--;

            randomRotationNumbers();
            RotationMatrix2005 rM2005 
                = new RotationMatrix2005(a, b, c, u, v, w, angle);
            double[][] matrix2005 = rM2005.getMatrix();
            double x = getRandom();
            double y = getRandom();
            double z = getRandom();
            double[] rotPoint2005 = rM2005.rotPointFromFormula(x, y, z);
            double[] rInfo =  new double[] {a, b, c, u, v, w, x, y, z, angle};

            RotationMatrix rM = new RotationMatrix(a, b, c, u, v, w, angle);
            double[][] matrix = rM.getMatrix();
            compareMatrices(matrix2005, matrix);
            double[] rotPoint = rM.rotPointFromFormula(rInfo);
            comparePoints(rotPoint2005, rotPoint);

            RotationMatrixUnnormalizedDirVector rMFull
                = new RotationMatrixUnnormalizedDirVector(a, b, c, u, v, w, angle);
            double[][] matrixFull = rMFull.getMatrix();
            compareMatrices(matrix2005, matrixFull);
            double[] rotPointFull = rM.rotPointFromFormula(rInfo);
            comparePoints(rotPoint2005, rotPointFull);

            // Note that whether we compute the matrices by normalized vectors
            // or not, the final matrices should be the same (as we know from
            // the previous tests against the 2005 matrix).
            compareMatrices(matrix, matrixFull);
        }
    }


    /**
     * Helper method to check that two double[][] matrices are equal.
     */
    private void compareMatrices(double[][] firstM, double[][] secondM) {
        assertEquals(firstM.length, secondM.length);

        for(int i=0; i<firstM.length; i++) {
            assertEquals(firstM[i].length, secondM[i].length);

            for(int j=0; j<firstM[0].length; j++) {
                assertEquals("i,j = "+i+","+j, 
                             firstM[i][j], secondM[i][j], TOLERANCE);
            }
        }
    }


    /**
     * Helper method to check that two double[] matrices are equal.
     */
    private void comparePoints(double[] firstP, double[] secondP) {
        assertEquals(firstP.length, secondP.length);

        for(int j=0; j<firstP.length; j++) {
            assertEquals("j = "+j, firstP[j], secondP[j], TOLERANCE);
        }
    }


    /**
     * Helper method to initialize the rotation numbers {@link #a}, etc., using
     * {@link #getRandom()}.  The value of {@link #angle} is initialized in
     * (-360, 360);
     */
    private void randomRotationNumbers() {
        a = getRandom();
        b = getRandom();
        c = getRandom();
        angle = 360 * getRandom();
        while (u*u + v*v + w*w < TOLERANCE) {
            u = getRandom();
            v = getRandom();
            w = getRandom();
        }
    }


    /**
     * Helper method to get a vector of random number in a range.
     * @return A random number from (-x, x), x = 1.
     */
    private double getRandom() {
        return 2*Math.random() - 1;
    }


    /**
     * Helper method for logging. 
     * @param a An array to print out.
     * @return The array contents as a String.
     */
    private String arrayToString(double[] a) {
        StringBuffer buffy = new StringBuffer();
        for (int i=0; i<a.length; i++) {
            buffy.append(a[i]+"\t");
        }
        return buffy.toString();
    }

}
