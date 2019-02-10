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

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


/**
 * <p>The root JUnit tests for all tests.</p>
 *
 * <pre>
 * java -cp classes:lib/junit_3.8.1-mac.jar junit.swingui.TestRunner \
 *                     arbaxisrot.AllTests
 * </pre>
 *
 * <p>Note when running from Eclipse, you may have to add the root
 * folder for this project to the classpath for this test in the
 * "Run..." menu.</p>
 */
public class AllTests extends TestCase {

	// Static initialization--------------------------------------------
	// Instance variables-----------------------------------------------
    // Constructors-----------------------------------------------------
    // JUnit Methods----------------------------------------------------

	public static Test suite() {
		TestSuite suite = new TestSuite("arbaxisrot.AllTests");

        // Add by calling suite().
		suite.addTest(arbaxisrot.RotationMatrix2005Test.suite());
		
        // Equivalently add a test as class
        suite.addTest(new TestSuite(RotationMatrixTest.class));
        suite.addTest(new TestSuite(RotationMatrixUnnormalizedDirVectorTest.class));
        suite.addTest(new TestSuite(RotationMatrixProgressionTest.class));

		return suite;
	}
}
