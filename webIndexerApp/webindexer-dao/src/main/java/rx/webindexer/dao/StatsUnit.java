package rx.webindexer.dao;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class StatsUnit implements Serializable {
	private static final long serialVersionUID = 6768679810558495158L;
	public enum LetterType {cyrillic, latin}
	
	public StatsUnit() {
		super();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (calculated ? 1231 : 1237);
		result = prime * result + ((letterFrequency == null) ? 0 : letterFrequency.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + Float.floatToIntBits(nonCyrillicWordsPercent);
		result = prime * result + ((topWords == null) ? 0 : topWords.hashCode());
		result = prime * result + Float.floatToIntBits(uniqueWordsPercent);
		result = prime * result + wordsCount;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StatsUnit other = (StatsUnit) obj;
		if (calculated != other.calculated)
			return false;
		if (letterFrequency == null) {
			if (other.letterFrequency != null)
				return false;
		} else if (!letterFrequency.equals(other.letterFrequency))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (Float.floatToIntBits(nonCyrillicWordsPercent) != Float.floatToIntBits(other.nonCyrillicWordsPercent))
			return false;
		if (topWords == null) {
			if (other.topWords != null)
				return false;
		} else if (!topWords.equals(other.topWords))
			return false;
		if (Float.floatToIntBits(uniqueWordsPercent) != Float.floatToIntBits(other.uniqueWordsPercent))
			return false;
		if (wordsCount != other.wordsCount)
			return false;
		return true;
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
	protected List<TreeMap<Character,Float>> letterFrequency = new LinkedList<>();
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
	public List<TreeMap<Character,Float>> getLetterFrequency() {
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
	public void setLetterFrequency(List<TreeMap<Character,Float>> letterFrequency) {
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
