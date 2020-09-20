package ciic4020.map;

public interface KeyExtractor<K, V> {

	K getKey(V value);
}