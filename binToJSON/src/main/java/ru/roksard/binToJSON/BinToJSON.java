package ru.roksard.binToJSON;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class BinToJSON {
	static final String HEX1 = "01 00 04 00 A8 32 92 56 02 00 03 00 04 71 02 03 "
			+ "00 0B 00 8E 8E 8E 20 90 AE AC A0 E8 AA A0 04 00 " 
			+ "1D 00 0B 00 07 00 84 EB E0 AE AA AE AB 0C 00 02 "
			+ "00 20 4E 0D 00 02 00 00 02 0E 00 02 00 40 9C";

	public static void writeHexFile(String hexData, String fileName) throws IOException {
		FileOutputStream out = new FileOutputStream(fileName);
		FileChannel fchan = out.getChannel();
		ByteBuffer buff = ByteBuffer.allocate(1024);
		List<String> hexList = Arrays.asList(hexData.split(" "));
		Iterator<String> hexer = hexList.iterator();
		do {
			while (buff.hasRemaining() && hexer.hasNext()) {
				buff.put(Short.decode("0x" + hexer.next()).byteValue());
			}
			buff.flip();
			fchan.write(buff);
			buff.flip();
		} while (hexer.hasNext());
	}
	
	public static long readNumberByByte(ByteBuffer bb, int amount) {
		long result = 0;
		for(int i = 0; i< amount; i++) {
			byte b = bb.get();
			result+= (Byte.toUnsignedInt(b) << i*8); //приводим байт в нужный порядок и суммируем
		}
		return result;
	}

	public static void main(String[] args) throws IOException {
		// System.out.println(Short.decode("0xA8"));
		//writeHexFile(HEX1, "file.bin");
		if (args.length != 2) {
			System.out.println("----- binToJSON ------- \n" + "Использование: \n"
					+ "java binToJSON <путь-к-бинарному-файлу> <путь-куда-сохранить-JSON>\n"
					+ "Пример: java binToJSON file.bin file.json");
			System.exit(0);
		}
		File fileIn = new File(args[0]);
		if (!fileIn.exists()) {
			System.out.println("Файл не найден " + fileIn.getAbsolutePath());
			System.exit(1);
		}
		FileInputStream in = new FileInputStream(fileIn);
		ByteBuffer inBuffer = ByteBuffer.allocate(4096);
		inBuffer.order(ByteOrder.LITTLE_ENDIAN);
		FileChannel fchan = in.getChannel();
		do { 
			/*
			 * tag 2 байта (тип поля) length 2 байта (длина значения в байтах) value length
			 * байт (тип зависит от тега)
			 * 
			 * uint32 - целое без знака, 4 байта string - строка в кодировке CP866 VLN -
			 * variable length number FVLN - floating point variable length number, первый
			 * байт определяет положение точки (кол-во знаков после точки)
			 * 
			 */
			inBuffer.clear(); 
			inBuffer.limit(inBuffer.position() + 4); //будем читать первые 4 байта
			fchan.read(inBuffer); 
			inBuffer.flip();
			//т.к в Джаве нет беззнаковых типов, используем тип int, чтоб уместить в нем 
			//беззнаковый short
			int tag = Short.toUnsignedInt(inBuffer.getShort()); //tag, 2 байта
			int length = Short.toUnsignedInt(inBuffer.getShort()); //length, 2 байта
	
			inBuffer.position(0); //вручную выставляем position и limit
			if(tag != 4) {  //если поле не является вложенной TLV структурой
				inBuffer.limit(length); //будем читать остатки TLV структуры (VALUE)
				fchan.read(inBuffer); //читаем VALUE
				inBuffer.flip();
			} else {
				//если tag==4, то нам не придется читать данные VALEU, но на всякий
				//случай выставляем лимит = 0
				inBuffer.limit(0); 
				
			}
			
			//У нас прочитаны tag, length, VALUE (VALUE находится в inBuffer)
			//и далее мы работаем с inBuffer, достаем оттуда данные и интерпретируем
			//в зависимости от тэга
			switch (tag) {
			case 1: //дата-время заказа, unix time, UTC (uint32)
				final long hourDelta = 6*3600*1000; //ms, 6 часов разницы, поправка на часовой пояс, видимо
				Date date = new Date(Integer.toUnsignedLong(inBuffer.getInt())*1000L 
						- hourDelta ); 
				System.out.print("dateTime: ");
				System.out.println(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(date));
				break;
			case 2: //номер заказа, orderNumber, число VLN
				long orderNumber = readNumberByByte(inBuffer, length);
				System.out.println("orderNumber: " + orderNumber);
				break;
			case 3: //string (1000)'
				System.out.println("customerName: " 
						+Charset.forName("CP866").decode(inBuffer).toString());
				break;
			case 4: // позиция заказа (вложенная структура TLV)
				System.out.println("вложенные: ");
				break;
			case 11: // string(200) - name
				System.out.println("name: " 
						+Charset.forName("CP866").decode(inBuffer).toString());
				break;
			case 12: // VLN(6) - price
				long price = readNumberByByte(inBuffer, length);
				System.out.println("price: " + price);
				break;
			case 13: //FVLN(8) - quantity
				//1ый байт = кол-во знаков после запятой
				short dot = (short)Byte.toUnsignedInt(inBuffer.get());
				long quantityL = readNumberByByte(inBuffer, length-1);
				double quantity = quantityL / Math.pow(10, dot);
				System.out.println("quantity: " + quantity);
				break;
			case 14: //VLN(6) - sum
				long sum = readNumberByByte(inBuffer, length);
				System.out.println("sum: " + sum);
				break;
			}
		} while (fchan.position() < fchan.size());

	}
}
