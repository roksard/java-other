package ru.roksard.binToJSON;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

public class BinToJSONTest extends BinToJSON {
	static final String HEX1 = 
			"01 00 04 00 A8 32 92 56 02 00 03 00 04 71 02 03 "
			+ "00 0B 00 8E 8E 8E 20 90 AE AC A0 E8 AA A0 04 00 " 
			+"1D 00 0B 00 07 00 84 EB E0 AE AA AE AB 0C 00 02 "
			+ "00 20 4E 0D 00 02 00 00 02 0E 00 02 00 40 9C";

	@Test
	public void testReadNumberByByte() {
		ByteBuffer bb = ByteBuffer.allocate(10);
		bb.order(ByteOrder.LITTLE_ENDIAN);
		
		final int x = 31;
		bb.putInt(x);
		bb.flip();
		long n = readNumberByByte(bb, 4);
		Assert.assertEquals(x, n);
	}
	
	@Test
	public void testMain() {
		final String binFile = "testfile.bin";
		final String jsonFile = "testfile.json";
		
		Map<String,String> original = new HashMap<String,String>();
		original.put("dateTime", "2016-01-10T10:30:00");
		original.put("orderNumber","160004");
		original.put("customerName","ООО Ромашка");
		original.put("name","Дырокол");
		original.put("price","20000");
		original.put("quantity","2");
		original.put("sum","40000");
		
		try {
			writeBinFile(HEX1, binFile);
			main(new String[] {binFile, jsonFile});
			try (   FileReader frd = new FileReader("testfile.json");
					BufferedReader rd = new BufferedReader(frd); ) {
				String s;
				while((s = rd.readLine()) != null) {
					String[] pair = s.split(": ");
					Assert.assertTrue(pair.length <= 2);
					if(pair.length == 2) {
						for(String key : original.keySet()) {
							if(pair[0].contains(key)) 
								Assert.assertTrue(pair[1].contains(original.get(key)));
						}
					}
				}
				rd.close();
				try {
					new File(binFile).delete();
					new File(jsonFile).delete();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}
}
