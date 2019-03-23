package rx.webindexer.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class MapUtil {
  public static <K, V extends Comparable<? super V>> Map<K, V> getTopByValue(Map<K, V> map, int topAmount) {
      List<Entry<K, V>> list = new ArrayList<>(map.entrySet());
      list.sort(Entry.comparingByValue(Collections.reverseOrder()));

      Map<K, V> result = new LinkedHashMap<>();
      for (Entry<K, V> entry : list) {
          result.put(entry.getKey(), entry.getValue());
          if(result.size() >= topAmount)
          	break;
      }

      return result;
  }
}
