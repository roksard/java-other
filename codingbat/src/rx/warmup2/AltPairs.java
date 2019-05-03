package rx.warmup2;

public class AltPairs {
	/*
	 * Given a string, return a string made of the chars at indexes 0,1, 4,5, 8,9 ... 
	 * so "kittens" yields "kien".
		altPairs("kitten") → "kien"
		altPairs("Chocolate") → "Chole"
		altPairs("CodingHorror") → "Congrr"
	 */
	public static String altPairs(String str) {
		int len = str.length();
		int i = 0;
		StringBuilder sb = new StringBuilder();
		while(i < len) {
			sb.append(str.charAt(i));
			if(i+1 < len)
				sb.append(str.charAt(i+1));
			i += 4;
		}
		return sb.toString();
	}

	public static void main(String[] args) {
		System.out.println(altPairs("kitten").equals("kien"));
	}

}
