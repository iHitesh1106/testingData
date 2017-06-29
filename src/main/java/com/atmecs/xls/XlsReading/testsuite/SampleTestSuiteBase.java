/***
 *
 */
package com.atmecs.xls.XlsReading.testsuite;

import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

/**
 *
 *         USE THIS CLASS TO WRITE FUNCTIONS THAT ARE REQUIRED TO EXECUTE BEFORE
 *         SUITE AND AFTER SUITE AND ALSO FUNCTIONS IF ANY TO SET AS BASE FOR
 *         THE SUITE
 *
 */
public class SampleTestSuiteBase {


	@BeforeSuite
	public void preSetup() {
		// USE THIS METHOD TO WRITE PRESETUP OPERATIONS IF ANY TO DO BEFORE
		// SUITE RUNS
	}

	@AfterSuite
	public void teardown() {
		// USE THIS METHOD TO WRITE OPERATIONS IF ANY TO DO AFTER SUITE
	}

}