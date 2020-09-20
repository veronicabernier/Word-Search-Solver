package ciic4020.hashtable;

import ciic4020.map.Map;

public interface HashTableFactory<K, V> {

	Map<K, V> getInstance(int initialCapacity);

}