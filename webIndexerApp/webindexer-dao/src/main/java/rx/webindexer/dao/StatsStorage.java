package rx.webindexer.dao;

import java.io.IOException;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("unchecked")
public class StatsStorage implements Serializable {
	private static final long serialVersionUID = -648790947802015386L;
	protected static ConcurrentHashMap<String,LinkedList<StatsUnit>> webStats = null; 
	
	static {
		try {
			webStats = (ConcurrentHashMap<String,LinkedList<StatsUnit>>)Keeper.loadFromExternal(Keeper.STATS_DEFAULT_FILE);
		} catch (ClassNotFoundException | IOException e) {
			webStats = new ConcurrentHashMap<String,LinkedList<StatsUnit>>();
		}
	}
	
	public static ConcurrentHashMap<String,LinkedList<StatsUnit>> getWebStats() {
		return webStats;
	}
	
	public static LinkedList<StatsUnit> getWebStatsUser(String userName) {
		LinkedList<StatsUnit> result = webStats.get(userName);
		if(result == null) 
			result = new LinkedList<StatsUnit>(); //не возвращаем null для упрощения дальнейшей обработки
		return result;
	}
	
	public static void addWebStatsUser(String userName, StatsUnit stat) {
		LinkedList<StatsUnit> list = webStats.get(userName);
		if(list == null) 
			list = new LinkedList<StatsUnit>();
		list.add(stat);
		webStats.put(userName, list);
	}
	
}
