package ciic4020.hashtable;

import ciic4020.map.Map;

public class HashTableSCFactory<K, V> implements HashTableFactory<K, V> {

	@Override
	public Map<K, V> getInstance(int initialCapacity) {
		return new HashTableSC<K, V>(initialCapacity, new SimpleHashFunction<K>());
	}

}