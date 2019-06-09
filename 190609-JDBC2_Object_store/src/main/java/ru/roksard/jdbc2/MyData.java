package ru.roksard.jdbc2;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class MyData implements Serializable {
	private String name = "";
	private String description = "";
	private int count = 0;
	private Map<String,Integer> wordsCount = new HashMap<String,Integer>();
	
	public Map<String, Integer> getWordsCount() {
		return wordsCount;
	}
	public void setWordsCount(Map<String, Integer> wordsCount) {
		this.wordsCount = wordsCount;
	}
	
	public String getName() {
		return name;
	}
	public String getDescription() {
		return description;
	}
	public int getCount() {
		return count;
	}

	public void setName(String name) {
		this.name = name;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Override
	public String toString() {
		return "name: "+ name + " description: " + description + " count: " + count 
				+ " wordsCount: " + wordsCount.toString();
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + count;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((wordsCount == null) ? 0 : wordsCount.hashCode());
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
		MyData other = (MyData) obj;
		if (count != other.count)
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (wordsCount == null) {
			if (other.wordsCount != null)
				return false;
		} else if (!wordsCount.equals(other.wordsCount))
			return false;
		return true;
	}
}
