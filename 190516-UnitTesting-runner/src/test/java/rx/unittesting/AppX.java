package rx.unittesting;

import java.io.IOException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AppX {
	@Before
	public void methodX() {
		System.out.println("  methodX before");
	}
	@After
	public void methodZ() {
		System.out.println("  methodZ after\n");
	}
	
	@Test
	public void methodB() {
		System.out.println("    methodB in");
		Assert.assertTrue(true);
		Assert.assertFalse(false);
	}
	
	@Test
	public void methodA() {
		System.out.println("    methodA in");
		boolean result = new App().methodA() == 'f';
		Assert.assertTrue(result);
	}
	
	@BeforeClass
	public static void methodBC() {
		System.out.println("methodBC before class");
	}
	
	@AfterClass		
	public static void methodAC() {
		System.out.println("methodAC after class");
	}
	
	@Test
	@Ignore("because someone said so!")
	public void ignoredMethod() {
		System.out.println("    ignoredMethod in");
	}
	
	@Test(expected=IOException.class)
	public void exceptionMethod() throws IOException{
		System.out.println("    exceptionMethod in");
		throw new IOException();
	}
}
