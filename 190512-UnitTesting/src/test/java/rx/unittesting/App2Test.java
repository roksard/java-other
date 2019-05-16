package rx.unittesting;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Test;

public class App2Test {
	@Test
	public void MethodA() {
		String currentOS = "Windows 10";
		Assume.assumeTrue(currentOS.toLowerCase().contains("windows")); //disable test if condition is false
		Assert.assertTrue(true);
	}
	@Test
	public void MethodB() {
		//Assume.assumeTrue(false);
		Assert.assertTrue(true);
		Assert.assertFalse(false);
		Assert.fail();
	}
}
