package rx.unittesting;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class App2Test {
	private int m1;
	private int m2;
	private int res;
	
	public App2Test(int cm1, int cm2, int cres2) {
		m1 = cm1;
		m2 = cm2;
		res = cres2;
	}
	
	@Parameters
	public static Collection<Object[]> data() {
		Object[][] data = new Object[][] { {2, 3, 6}, {10, 3, 30}, {9, 7, 63} };
		return Arrays.asList(data);
	}
	
	@Test
	public void usesParameters() {
		App app = new App();
		System.out.println("m1:" + m1 + " * m2:" + m2 + "  =  " + res);
		Assert.assertEquals(res, app.multiply(m1, m2));
	}
	
	
	@Test
	public void MethodA() {
		System.out.println("MethodA in");
	}
	@Test
	public void MethodB() {
		System.out.println("MethodB in");
	}
}
