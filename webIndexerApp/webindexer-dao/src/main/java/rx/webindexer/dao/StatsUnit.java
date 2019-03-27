package rx.webindexer.dao;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class StatsUnit implements Serializable {
	private static final long serialVersionUID = 6810977218324303586L;
	public enum LetterType {cyrillic, latin}
	
	public StatsUnit() {
		super();
	}
	
	public StatsUnit(String name) {
		super();
		this.name = name;
	}
	
	protected String name = "";
	protected int wordsCount = 0;
	protected float uniqueWordsPercent = 0;
	protected float nonCyrillicWordsPercent = 0;
	protected Map<String,Integer> topWords = new LinkedHashMap<>(); 
	protected boolean calculated = false;
	  //<кол-во,слово> (кол-во = сколько раз слово встречается в тексте)
	protected List<TreeMap<Character,Integer>> letterFrequency = new LinkedList<>();
	public String getName() {
		return name;
	}
	public int getWordsCount() {
		return wordsCount;
	}
	public float getUniqueWordsPercent() {
		return uniqueWordsPercent;
	}
	public float getNonCyrillicWordsPercent() {
		return nonCyrillicWordsPercent;
	}
	public Map<String, Integer> getTopWords() {
		return topWords;
	}
	public List<TreeMap<Character, Integer>> getLetterFrequency() {
		return letterFrequency;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setWordsCount(int wordsCount) {
		this.wordsCount = wordsCount;
	}
	public void setUniqueWordsPercent(float uniqueWordsP) {
		this.uniqueWordsPercent = uniqueWordsP;
	}
	public void setNonCyrillicWordsPercent(float nonCyrillicWordsP) {
		this.nonCyrillicWordsPercent = nonCyrillicWordsP;
	}
	public void setTopWords(Map<String, Integer> topWords) {
		this.topWords = topWords;
	}
	public void setLetterFrequency(List<TreeMap<Character, Integer>> letterFrequency) {
		this.letterFrequency = letterFrequency;
	}
	public boolean isCalculated() {
		return calculated;
	}
	public void setCalculated(boolean calculated) {
		this.calculated = calculated;
	}
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append("name: "+ this.name + "\n");
		result.append("wordsCount: "+ this.wordsCount + "\n");
		result.append("uniqueWordsPercent: "+ this.uniqueWordsPercent + "\n");
		result.append("nonCyrillicWordsPercent: "+ this.nonCyrillicWordsPercent + "\n");
		result.append("topWords: "+ this.topWords + "\n");
		result.append("letterFrequency: "+ this.letterFrequency + "\n");
		return result.toString();
	}
}
