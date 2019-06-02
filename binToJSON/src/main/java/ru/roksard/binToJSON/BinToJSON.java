package ru.roksard.binToJSON;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class BinToJSON {
	

	public static void writeBinFile(String hexData, String fileName) throws IOException {
		try ( 
				FileOutputStream out = new FileOutputStream(fileName);
				FileChannel fchan = out.getChannel()) {
			ByteBuffer buff = ByteBuffer.allocate(1024);
			buff.order(ByteOrder.LITTLE_ENDIAN);
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

			fchan.close();
		}
	}

	public static long readNumberByByte(ByteBuffer bb, int amount) {
		long result = 0;
		for (int i = 0; i < amount; i++) {
			byte b = bb.get();
			result += (Byte.toUnsignedInt(b) << i * 8); // приводим байт в нужный порядок и суммируем
		}
		return result;
	}

	public static String processValue(int tag, ByteBuffer valueBuffer) {
		int length = valueBuffer.limit();
		StringBuilder result = new StringBuilder();
		switch (tag) {
		case 1: // дата-время заказа, unix time, UTC (uint32)
			final long hourDelta = 6 * 3600 * 1000; // ms, 6 часов разницы, поправка на часовой пояс, видимо
			Date date = new Date(Integer.toUnsignedLong(valueBuffer.getInt()) * 1000L - hourDelta);
			result = result.append("\n  \"dateTime\": \"");
			result.append(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(date));
			result.append("\",");
			break;
		case 2: // номер заказа, orderNumber, число VLN
			long orderNumber = readNumberByByte(valueBuffer, length);
			result.append("\n  \"orderNumber\": " + orderNumber + ",");
			break;
		case 3: // string (1000)'
			result.append("\n  \"customerName\": \"" + Charset.forName("CP866").decode(valueBuffer).toString() + "\",");
			break;
		case 4: // позиция заказа (вложенная структура TLV)
			result.append("\n  \"items\": [");
			break;
		case 11: // string(200) - name
			result.append("\n      \"name\": \"" + Charset.forName("CP866").decode(valueBuffer).toString() + "\",");
			break;
		case 12: // VLN(6) - price
			long price = readNumberByByte(valueBuffer, length);
			result.append("\n      \"price\": " + price + ",");
			break;
		case 13: // FVLN(8) - quantity
			// 1ый байт = кол-во знаков после запятой
			short dot = (short) Byte.toUnsignedInt(valueBuffer.get());
			long quantityL = readNumberByByte(valueBuffer, length - 1);
			double quantity = quantityL / Math.pow(10, dot);
			result.append("\n      \"quantity\": " + String.format("%." + dot + "f", quantity));
			result.append(",");
			break;
		case 14: // VLN(6) - sum
			long sum = readNumberByByte(valueBuffer, length);
			result.append("\n      \"sum\": " + sum + ",");
			break;
		}
		return result.toString();
	}

	public static int readTLV(ReadableByteChannel fchan, ByteBuffer readTo) throws IOException {
		ByteBuffer inBuffer = readTo;
		inBuffer.position(0);
		inBuffer.limit(4); // будем читать первые 4 байта (tag и length)
		fchan.read(inBuffer);
		inBuffer.flip();
		// т.к в Джаве нет беззнаковых типов, используем тип int, чтоб уместить в нем
		// беззнаковый short
		int tag = Short.toUnsignedInt(inBuffer.getShort()); // tag, 2 байта
		int length = Short.toUnsignedInt(inBuffer.getShort()); // length, 2 байта

		inBuffer.position(0); // вручную выставляем position и limit
		if (tag != 4) { // если поле не является вложенной TLV структурой
			inBuffer.limit(length); // будем читать остатки TLV структуры (VALUE)
			fchan.read(inBuffer); // читаем VALUE
			inBuffer.flip();
		} else {
			// если tag==4, то нам не придется читать данные VALEU, но на всякий
			// случай выставляем лимит = 0
			inBuffer.limit(0);

		}
		return tag;
	}

	public static void main(String[] args) throws IOException {
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
		try (
				FileInputStream fin = new FileInputStream(fileIn);
				FileChannel fchan = fin.getChannel()) {

			ByteBuffer inBuffer = ByteBuffer.allocate(4096);
			inBuffer.order(ByteOrder.LITTLE_ENDIAN);
			;
			try (PrintWriter out = new PrintWriter(args[1])) {

				out.print("{");

				int orderFieldsRead = 0; // считаем кол-во прочитанных полей из "позиции заказа" (при достижении 4,
											// считаем что это конец элемента массива и начало следующего элемента)
				int orderFieldsReadLast = 0; // предыдущее состояние счетчика
				String column = ""; // перед первым элементом массива не будет запятой
				do {
					// У нас прочитаны tag, length, VALUE (VALUE находится в inBuffer)
					// и далее мы работаем с inBuffer, достаем оттуда данные и интерпретируем
					// в зависимости от тэга
					int tag = readTLV(fchan, inBuffer); // limit() буфера установлен так
					// что мы не выйдем за пределы структуры TLV
					orderFieldsReadLast = orderFieldsRead;
					switch (tag) {
					case 11:
					case 12:
					case 13:
					case 14:
						orderFieldsRead++; // считаем кол-во
						break;
					default:
						orderFieldsRead = 0;
						if (orderFieldsReadLast > 0)
							out.print("]");
					}

					// если первое поле из "позиция заказа", то ставим запятую и скобки
					String sym1 = orderFieldsRead == 1 ? (column + "\n    {") : ("");
					String sym2 = "";
					if (orderFieldsRead == 4) {
						sym2 = "\n    }";
						column = ",";
						orderFieldsRead = 0;
					}
					out.print(sym1 + processValue(tag, inBuffer) + sym2);
				} while (fchan.position() < fchan.size());
				if (orderFieldsReadLast > 0)
					out.print("\n  ]");
				out.print("\n}");
				fchan.close();
			}
		}
	}
}
