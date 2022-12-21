package edu.caltech.cs2.datastructures;

import edu.caltech.cs2.interfaces.ICollection;
import edu.caltech.cs2.interfaces.IDeque;
import edu.caltech.cs2.interfaces.IDictionary;
import edu.caltech.cs2.interfaces.IQueue;

import java.util.Iterator;
import java.util.function.Supplier;

public class ChainingHashDictionary<K, V> implements IDictionary<K, V> {
    private Supplier<IDictionary<K, V>> chain;
    private static final int[] primes = {5, 11, 23, 47, 97, 193, 389, 773, 1549, 3089, 6173, 12347, 24697, 49393, 98779, 197551, 395107, 400009};
    private int size;
    private IQueue<Integer> prime;
    private IDictionary<K, V>[] table;

    public ChainingHashDictionary(Supplier<IDictionary<K, V>> chain) {
        this.chain = chain;
        this.prime = new LinkedDeque<>();
        this.size = 0;
        for (int j : primes) {
            prime.enqueue(j);
        }
        this.table = new IDictionary[prime.dequeue()];
        for (int i = 0; i < this.table.length; i++){
            this.table[i] = this.chain.get();
        }
    }

    /**
     * @param key
     * @return value corresponding to key
     */
    @Override
    public V get(K key) {
        int i = hashindex(key);
        return this.table[i].get(key);
    }
    private int hashindex(K key){
        int store = key.hashCode() % this.table.length;
        return store < 0 ? store + this.table.length: store;
    }
    @Override
    public V remove(K key) {
        V got = this.get(key);
        if (got != null) {
            this.table[hashindex(key)].remove(key);
            this.size--;
        }
        return got;
    }

    @Override
    public V put(K key, V value) {
        V got = this.get(key);
        this.table[hashindex(key)].put(key, value);
        if (got == null){
            this.size++;
        }
        this.rehash();
        return got;
    }

    private void rehash(){
        if (this.size >= this.table.length) {
            if(prime.size() != 0) {
                IDictionary<K, V>[] old = this.table;
                this.table = new IDictionary[prime.dequeue()];
                for (int i = 0; i < this.table.length; i++) {
                    this.table[i] = this.chain.get();
                }
                this.size = 0;
                for (IDictionary<K, V> dic : old) {
                    for (K key : dic) {
                        this.put(key, dic.get(key));
                    }
                }
            }
        }
    }

    @Override
    public boolean containsKey(K key) {
        return this.get(key) != null;
    }

    /**
     * @param value
     * @return true if the HashDictionary contains a key-value pair with
     * this value, and false otherwise
     */
    @Override
    public boolean containsValue(V value) {
        return this.values().contains(value);
    }

    /**
     * @return number of key-value pairs in the HashDictionary
     */
    @Override
    public int size() {
        return this.size;
    }

    @Override
    public ICollection<K> keys() {
        IDeque<K> keys = new LinkedDeque<>();
        for (IDictionary<K, V> dic : this.table){
            for(K key: dic){
                keys.add(key);
            }
        }
        return keys;
    }

    @Override
    public ICollection<V> values() {
        IDeque<V> vals = new LinkedDeque<>();
        for (IDictionary<K, V> dic : this.table){
            for(K key: dic){
                vals.add(dic.get(key));
            }
        }
        return vals;
    }

    /**
     * @return An iterator for all entries in the HashDictionary
     */
    @Override
    public Iterator<K> iterator() {
        return keys().iterator();
    }
}
