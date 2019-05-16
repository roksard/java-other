package rx.unittesting;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class TestRunner {

	public static void main(String[] args) {
		Result result = JUnitCore.runClasses(AllTestsSuite.class);
		
		System.out.println("Tests run: " + result.getRunCount());
		System.out.println("Failures: " + result.getFailureCount());
		for(Failure failure : result.getFailures()) {
			System.out.println("  " + failure.toString());
		}
	}

}
