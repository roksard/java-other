package rx;

import java.math.BigDecimal;

public class FNPC {
	public static void main(String[] args) {
		//what is the problem
		System.out.println(0.1 + 0.2);
		//a way to solve
		BigDecimal a = new BigDecimal("0.1");
		BigDecimal b = new BigDecimal("0.2");
		System.out.println(a.add(b));
	}
}
