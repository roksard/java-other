package rx.webindexer.service;

import static rx.webindexer.dao.StatsUnit.LetterType.cyrillic;
import static rx.webindexer.dao.StatsUnit.LetterType.latin;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import rx.webindexer.dao.StatsUnit;

public class Service {
	/**
	 * loads specified web page and stores it's source code as a String
	 * 
	 * @param url
	 *            - the address of a page to be loaded, in a format, e.g.:
	 *            www.abc.com or http://www.abc.com
	 * @return returns html source code of loaded page, or null if load failed
	 * @throws IOException
	 *             when load failed due to malformed url or network error.
	 */
	public static String loadPage(String url) throws IOException {
		URL ur = null;
		try {
			ur = new URL(url);
		} catch (MalformedURLException e) {
			ur = new URL("https://" + url);
		}
		HttpURLConnection yc = (HttpURLConnection) ur.openConnection();
		BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream(), "UTF-8"));
		String inputLine;
		StringBuilder textData = new StringBuilder();

		while ((inputLine = in.readLine()) != null)
			textData.append(inputLine);
		in.close();
		return textData.toString();
	}

	public static String loadPageFile(String fileName) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF-8"));
		String inputLine;
		StringBuilder textData = new StringBuilder();

		while ((inputLine = in.readLine()) != null)
			textData.append(inputLine);
		in.close();

		return textData.toString();
	}

	/**
	 * Parses pure html source text into an array of cyrillic and latin words.
	 * Leaves only what is inside of <body> tag, removes all html tags, <script>,
	 * <style>, comments. Removes html character codes (such as &tl; &#35; etc)
	 * without converting them into their character representation. Words can only
	 * contain latin characters (a..z and A..Z) and cyrillic characters (а..я, А-Я)
	 * 
	 * @param pageData
	 *            pure html source text data
	 * @return array of words extracted from given html data
	 */
	public static String[] parseHtml(String pageData) {
		Pattern pattern = Pattern.compile("<body.*?>(.*)</body>", Pattern.DOTALL);
		Matcher match = pattern.matcher(pageData);
		if (!match.find()) // не найден тег <body>, похоже это не html
			return new String[0];

		// ?s = DOTALL mode, .*? reluctant(non-greedy) quantifier
		String wordsHeap = match.group(1).replaceAll("(?s)<!--.*?-->", " ") // удаляем комментарии
				.replaceAll("(?s)<style.*?>.*?</style>", " ") // удаляем <style>..</style>
				.replaceAll("(?s)<script.*?>.*?</script>", " ") // удаляем <script>..</script>
				.replaceAll("(?s)<.+?>", " ") // удаляем html тэги
				.replaceAll("(?s)(&[\\w,#\\d]{2,6};)", " ") // удаляем HTML character codes
				.replaceAll("(?s)[^a-zA-Zа-яА-Я]", " ") // удаляем non-alphabetic символы
				.toLowerCase();

		return wordsHeap.split("[ ]+"); // массив всех слов страницы
	}

	public static StatsUnit calcStats(String[] words) {
		Map<String, Integer> wordCountMap = new HashMap<>(); // <слово, сколько раз встречается в тексте>
		int emptyWordsCount = 0; // пустые строки, которые не нужно учитывать как слова при подсчете кол-ва слов
		int nonCyrillic = 0; // кол-во слов не кириллицы
		StatsUnit result = new StatsUnit();

		int totalLetterCount = 0;

		HashMap<Character, Integer> letterCountMap = new HashMap<Character, Integer>();
		for(Character c = 'a'; c <= 'z'; c++)  //инициализируем латинскими буквами
			letterCountMap.put(c, 0);
		for(Character c = 'а'; c <= 'я'; c++) //инициализируем кириллицей
			letterCountMap.put(c, 0);		

		for (String word : words) {
			if (word.equals("")) {
				emptyWordsCount++;
				continue;
			}
			totalLetterCount += word.length();

			Integer count = wordCountMap.get(word);
			wordCountMap.put(word, count == null ? 1 : count + 1);

			if (Pattern.matches("[^а-яА-Я]", word.charAt(0) + "")) // слово не на кириллице
				nonCyrillic++;

			for (char letter : word.toCharArray()) { // посчитать буквы
				Integer countLetter = letterCountMap.get(letter);
				letterCountMap.put(letter, countLetter == null ? 1 : countLetter + 1);
			}
		}
		
		int wordsCount = words.length - emptyWordsCount; //число слов в тексте. сохраним сюда чтоб использовать

		LinkedList<TreeMap<Character, Float>> letterFreq = new LinkedList<TreeMap<Character, Float>>();
		letterFreq.add(new TreeMap<Character, Float>()); // подсчет букв
		letterFreq.add(new TreeMap<Character, Float>()); // подсчет букв

		for (Map.Entry<Character, Integer> entry : letterCountMap.entrySet()) {
			TreeMap<Character, Float> map = null;
			if (Pattern.matches("[а-яА-Я]", entry.getKey() + "")) //буква кириллицы
				map = letterFreq.get(cyrillic.ordinal());
			else
				map = letterFreq.get(latin.ordinal());
			map.put(entry.getKey(), entry.getValue() / (float)totalLetterCount * 100);
		}

		result.setWordsCount(wordsCount);
		result.setUniqueWordsPercent(wordCountMap.size() / ((float) result.getWordsCount()) * 100);
		result.setNonCyrillicWordsPercent(nonCyrillic / ((float) result.getWordsCount()) * 100);
		result.setTopWords(MapUtil.getTopByValue(wordCountMap, 10));
		result.setLetterFrequency(letterFreq);
		result.setCalculated(true);
		return result;
	}

}
