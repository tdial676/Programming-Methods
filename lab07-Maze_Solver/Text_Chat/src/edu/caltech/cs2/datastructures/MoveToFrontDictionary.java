package edu.caltech.cs2.datastructures;

import edu.caltech.cs2.interfaces.ICollection;
import edu.caltech.cs2.interfaces.IDictionary;

import java.util.Iterator;

public class MoveToFrontDictionary<K, V> implements IDictionary<K,V> {

    private Node<K, V> root;
    private int size;

    private static class Node<K, V> {
        public final K key;
        public V value;
        public Node<K, V> next;

        public Node(K key, V value){
            this(key, value, null);
        }

        public Node(K key, V value, Node<K, V> next){
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    public MoveToFrontDictionary() {
        this.root = null;
        this.size = 0;
    }

    @Override
    public V remove(K key) {
        V removed = this.get(key);
        if (removed != null){
            this.root = this.root.next;
            this.size--;
        }
        return removed;
    }

    @Override
    public V put(K key, V value) {
        V hold = this.get(key);
        if (hold == null){
            Node<K, V> hent = new Node<>(key, value);
            hent.next = this.root;
            this.root = hent;
            this.size++;
        } else {
            this.root.value = value;
        }
        return hold;
    }

    @Override
    public boolean containsKey(K key) {
        return this.get(key) != null;
    }

    @Override
    public boolean containsValue(V value) {
        return this.values().contains(value);
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public ICollection<K> keys() {
        LinkedDeque<K> keys = new LinkedDeque<>();
        if (this.root == null){
            return keys;
        }
        Node<K, V> curr = this.root;
        for (int i = 0; i < this.size; i++){
            keys.add(curr.key);
            curr = curr.next;
        }
        return keys;
    }

    @Override
    public ICollection<V> values() {
        LinkedDeque<V> vals = new LinkedDeque<>();
        if (this.root == null){
            return vals;
        }
        Node<K, V> curr = this.root;
        for (int i = 0; i < this.size; i++){
            vals.add(curr.value);
            curr = curr.next;
        }
        return vals;
    }

    public V get(K key) {
        if (this.root == null) {
            return null;
        }
        Node<K, V> prev = null;
        Node<K, V> curr = this.root;
        for (int i = 0; i < this.size; i++){
            if (curr.key.equals(key)) {
                if (prev == null){
                    return curr.value;
                }
                prev.next = curr.next;
                curr.next = this.root;
                this.root = curr;
                return curr.value;
            }
            prev = curr;
            curr = curr.next;
        }
        return null;
    }

    @Override
    public Iterator<K> iterator() {
        return keys().iterator();
    }
}
