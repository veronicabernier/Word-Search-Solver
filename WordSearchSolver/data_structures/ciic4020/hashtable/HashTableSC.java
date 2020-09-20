package ciic4020.hashtable;

import java.io.PrintStream;

import ciic4020.list.*;
import ciic4020.map.Map;

public class HashTableSC<K, V> implements Map<K, V> {

	/**
	 * The values in the linked lists within our buckets will be of this type.
	 * @author Juan O. Lopez
	 */
	private static class BucketNode<K, V> {
		private K key;
		private V value;

		public BucketNode(K key, V value) {
			this.key = key;
			this.value = value;
		}

		public K getKey() {
			return key;
		}

		public V getValue() {
			return value;
		}
	}
	
	
	// private fields
	private int currentSize;
	private List<BucketNode<K, V>>[] buckets;
	private HashFunction<K> hashFunction;
	private final static double loadFactor = 0.75;

	
	@SuppressWarnings("unchecked")
	public HashTableSC(int initialCapacity, HashFunction<K> hashFunction) {
		if (initialCapacity < 1)
			throw new IllegalArgumentException("Capacity must be at least 1");
		if (hashFunction == null)
			throw new IllegalArgumentException("Hash function cannot be null");

		currentSize = 0;
		this.hashFunction = hashFunction;
		buckets = new LinkedList[initialCapacity];
		for (int i = 0; i < initialCapacity; i++)
			buckets[i] = new LinkedList<BucketNode<K, V>>();
	}
	
	//called whenever (currentSize/buckets.length) > loadFactor 
	@SuppressWarnings("unchecked")
	private void rehash() {
		List<BucketNode<K, V>>[] newBuckets = new LinkedList[buckets.length*2];
		for (int i = 0; i < buckets.length*2; i++)
			newBuckets[i] = new LinkedList<BucketNode<K, V>>();
		for (int i = 0; i < buckets.length; i++) {
			if(buckets[i] != null) {
				for (int j = 0; j < buckets[i].size(); j++) {
					if(this.buckets[i].get(j)!= null) {
						if(this.buckets[i].get(j).getKey() != null && this.buckets[i].get(j).getValue() != null) {
							int targetBucket = hashFunction.hashCode(this.buckets[i].get(j).getKey()) % newBuckets.length;
							List<BucketNode<K, V>> L = newBuckets[targetBucket];
							L.add(0, new BucketNode<K, V>(this.buckets[i].get(j).getKey(), this.buckets[i].get(j).getValue()));	
						}
					}
				}
			}
		}
		int oldSize = this.size();
		this.clear();
		this.buckets = newBuckets;
		this.currentSize = oldSize;

	}

	@Override
	public V get(K key) {
		if (key == null)
			throw new IllegalArgumentException("Parameter cannot be null.");

		/* First we determine the bucket corresponding to this key */
		int targetBucket = hashFunction.hashCode(key) % buckets.length;
		/* Within that bucket there is a linked list, since we're using Separate Chaining */
		List<BucketNode<K, V>> L = buckets[targetBucket];
		/* Look for the key within the nodes of that linked list */
		for (BucketNode<K, V> BN : L) {
			if (BN.getKey().equals(key)) // Found it!
				return BN.getValue();
		}

		return null; // Did not find it
	}

	@Override
	public void put(K key, V value) {
		if (key == null || value == null)
			throw new IllegalArgumentException("Parameter cannot be null.");
		if (((double) currentSize/ (double) buckets.length) > loadFactor) {
			rehash();
		}

		/* Can't have two elements with same key,
		 * so remove existing element with the given key (if any) */
		remove(key);
		/* Determine the bucket corresponding to this key */
		int targetBucket = hashFunction.hashCode(key) % buckets.length;
		/* Within that bucket there is a linked list, since we're using Separate Chaining */
		List<BucketNode<K, V>> L = buckets[targetBucket];
		/* Finally, add the key/value to the linked list */
		L.add(0, new BucketNode<K, V>(key, value));
		currentSize++;
	}

	@Override
	public V remove(K key) {
		if (key == null)
			throw new IllegalArgumentException("Parameter cannot be null.");

		/* First we determine the bucket corresponding to this key */
		int targetBucket = hashFunction.hashCode(key) % buckets.length;
		/* Within that bucket there is a linked list, since we're using Separate Chaining */
		List<BucketNode<K, V>> L = buckets[targetBucket];
		/* Iterate over linked list trying to find this the key */
		int pos = 0;
		for (BucketNode<K, V> BN : L) {
			if (BN.getKey().equals(key)) { // Found it!
				L.remove(pos);
				currentSize--;
				return BN.getValue();
			}
			else
				pos++;
		}
		return null;
	}

	@Override
	public boolean containsKey(K key) {
		return get(key) != null;
	}

	@Override
	public List<K> getKeys() {
		List<K> result = new ArrayList<K>(size());
		/* For each bucket in the hash table, get the keys in that linked list */
		for (int i = 0; i < buckets.length; i++)
			for (BucketNode<K, V> BN : buckets[i])
				result.add(0, BN.getKey());
		return result;
	}

	@Override
	public List<V> getValues() {
		List<V> result = new ArrayList<V>(size());
		/* For each bucket in the hash table, get the values in that linked list */
		for (int i = 0; i < buckets.length; i++)
			for (BucketNode<K, V> BN : buckets[i])
				result.add(0, BN.getValue());
		return result;
	}

	@Override
	public int size() {
		return currentSize;
	}

	@Override
	public boolean isEmpty() {
		return size() == 0;
	}

	@Override
	public void clear() {
		currentSize = 0;
		for (int i = 0; i < buckets.length; i++)
			buckets[i].clear();
	}

	@Override
	public void print(PrintStream out) {
		/* For each bucket in the hash table, print the elements in that linked list */
		for (int i = 0; i < buckets.length; i++)
			for (BucketNode<K, V> BN : buckets[i])
				out.printf("(%s, %s)\n", BN.getKey(), BN.getValue());
	}

}