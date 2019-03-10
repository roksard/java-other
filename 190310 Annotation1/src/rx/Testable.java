package rx;

public class Testable {
	public void execute() {
		System.out.println("executing");
	}
	@Test(id = 1, description = "wat")
	public void test() {
		System.out.println("testing..");
		execute();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
