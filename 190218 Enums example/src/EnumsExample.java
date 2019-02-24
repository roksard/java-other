
enum TransportX {
	MOTO, CAR, BCYCLE
}

enum Transport {
	MOTO(2, "more fun", "CAR"),
	CAR(4, "more comfortable", "MOTO"),
	BCYCLE(2, "more eco", "MOTO");
	private String description;
	Transport(int wheels, String differs, String diffAgainst) {
		description = "Has " + wheels + " wheels. Is " + differs + " than " 
				+ Enum.valueOf(TransportX.class, diffAgainst)
		.toString();
	}
	public String getDescription() {
		return description;
	}
}

public class EnumsExample {

	public static void main(String[] args) {
		for(Transport t : Transport.values()) {
			System.out.println(t + ": " + t.getDescription());
		}
	}

}
