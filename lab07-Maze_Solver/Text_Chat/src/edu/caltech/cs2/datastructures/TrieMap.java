package edu.caltech.cs2.datastructures;

import edu.caltech.cs2.interfaces.ICollection;
import edu.caltech.cs2.interfaces.IDeque;
import edu.caltech.cs2.interfaces.ITrieMap;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Function;

public class TrieMap<A, K extends Iterable<A>, V> implements ITrieMap<A, K, V> {
    private TrieNode<A, V> root;
    private Function<IDeque<A>, K> collector;
    private int size;

    public TrieMap(Function<IDeque<A>, K> collector) {
        this.root = null;
        this.collector = collector;
        this.size = 0;
    }
    

    @Override
    public boolean isPrefix(K key) {
        TrieNode<A,V> current = this.root;
        if (current == null){
            return false;
        }
        for (A chr: key){
            if(current.pointers.containsKey(chr)){
                current = current.pointers.get(chr);
            }
            else{
                return false;
            }
        }
        return true;
    }

    @Override
    public ICollection<V> getCompletions(K prefix) {
        TrieNode<A,V> current = this.root;
        if (current == null){
            return new ArrayDeque<V>();
        }
        for (A chr: prefix){
            if (current.pointers.containsKey(chr)){
                current = current.pointers.get(chr);
            }
            else{
                return new ArrayDeque<V>();
            }
        }
        TrieNode<A,V> l = current;
        ICollection<V> c = new ArrayDeque<V>();
        getCompletions(l,c);
        return c;
    }

    private void getCompletions(TrieNode<A,V> current, ICollection<V> coll){
        if (current.value!= null){
            coll.add(current.value);
        }
        for (A chr: current.pointers.keySet()){
            getCompletions(current.pointers.get(chr), coll);
        }
    }

    @Override
    public void clear() {
        this.root = null;
        this.size = 0;
    }

    @Override
    public V get(K key) {
        TrieNode<A, V> current = this.root;
        if (current == null){
            return null;
        }
        for (A chr: key){
            if (current.pointers.containsKey(chr)){
                current = current.pointers.get(chr);
            }
            else{
                return null;
            }
        }
        return current.value;
    }

    @Override
    public V remove(K key) {
        V v = this.get(key);
        if (v == null){
            return null;
        }
        IDeque<A> w = new LinkedDeque<>();
        for (A chr: key){
            w.addBack(chr);
        }
        remove(this.root, w);
        this.size--;
        if(this.size == 0){
            this.root = null;
        }
        return v;
    }

    private boolean remove(TrieNode<A,V> current, IDeque<A> k){
        if (k.size() == 0){
            current.value = null;
            if (current.pointers.keySet().isEmpty()){
                return true;
            }
            return false;
        }
        A f = k.removeFront();
        if (!current.pointers.containsKey(f)){
            return false;
        }
        if (remove(current.pointers.get(f), k)){
            current.pointers.remove(f);
            if (current.value == null && current.pointers.size() == 0){
                return true;
            }
        }
        return false;
    }

    @Override
    public V put(K key, V value) {
        if (this.root == null){
            this.root = new TrieNode<>();
        }
        TrieNode<A,V> current = this.root;
        for (A chr: key){
            if(!current.pointers.containsKey(chr)){
                current.pointers.put(chr, new TrieNode<>());
            }
            current = current.pointers.get(chr);
        }
        V temp = current.value;
        current.value = value;
        if (temp == null){
            this.size++;
        }
        return temp;
    }

    @Override
    public boolean containsKey(K key) {
        if (this.root == null){
            return false;
        }
        TrieNode<A,V> current = this.root;
        for (A chr: key){
            if(current.pointers.containsKey(chr)){
                current = current.pointers.get(chr);
            }
            else{
                return false;
            }
        }
        if (current.value != null){
            return true;
        }
        return false;
    }

    @Override
    public boolean containsValue(V value) {
        return values().contains(value);
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public ICollection<K> keys() {
        ICollection<K> col = new ArrayDeque<>();
        IDeque<A> t = new ArrayDeque<>();
        keys(this.root, col, t);
        return col;
    }

    private void keys(TrieNode<A,V> current, ICollection<K> col, IDeque<A> acc){
        if (current == null){
            return;
        }
        if (current.value != null){
            col.add(this.collector.apply(acc));
        }
        for (A chr: current.pointers.keySet()){
            acc.addBack(chr);
            keys(current.pointers.get(chr), col, acc);
            acc.removeBack();
        }
    }

    @Override
    public ICollection<V> values() {
        ICollection<V> col = new ArrayDeque<>();
        values(this.root, col);
        return col;
    }

    private void values(TrieNode<A,V> current, ICollection<V> col){
        if (current == null){
            return;
        }
        if(current.value != null){
            col.add(current.value);
        }
        for (A chr: current.pointers.keySet()){
            values(current.pointers.get(chr), col);
        }
    }

    @Override
    public Iterator<K> iterator() {
        return keys().iterator();
    }
    
    private static class TrieNode<A, V> {
        public final Map<A, TrieNode<A, V>> pointers;
        public V value;

        public TrieNode() {
            this(null);
        }

        public TrieNode(V value) {
            this.pointers = new HashMap<>();
            this.value = value;
        }

        @Override
        public String toString() {
            StringBuilder b = new StringBuilder();
            if (this.value != null) {
                b.append("[" + this.value + "]-> {\n");
                this.toString(b, 1);
                b.append("}");
            }
            else {
                this.toString(b, 0);
            }
            return b.toString();
        }

        private String spaces(int i) {
            StringBuilder sp = new StringBuilder();
            for (int x = 0; x < i; x++) {
                sp.append(" ");
            }
            return sp.toString();
        }

        protected boolean toString(StringBuilder s, int indent) {
            boolean isSmall = this.pointers.entrySet().size() == 0;

            for (Map.Entry<A, TrieNode<A, V>> entry : this.pointers.entrySet()) {
                A idx = entry.getKey();
                TrieNode<A, V> node = entry.getValue();

                if (node == null) {
                    continue;
                }

                V value = node.value;
                s.append(spaces(indent) + idx + (value != null ? "[" + value + "]" : ""));
                s.append("-> {\n");
                boolean bc = node.toString(s, indent + 2);
                if (!bc) {
                    s.append(spaces(indent) + "},\n");
                }
                else if (s.charAt(s.length() - 5) == '-') {
                    s.delete(s.length() - 5, s.length());
                    s.append(",\n");
                }
            }
            if (!isSmall) {
                s.deleteCharAt(s.length() - 2);
            }
            return isSmall;
        }
    }
}
