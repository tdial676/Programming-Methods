package edu.caltech.cs2.datastructures;

import edu.caltech.cs2.interfaces.ICollection;
import edu.caltech.cs2.interfaces.IDeque;
import edu.caltech.cs2.interfaces.IQueue;
import edu.caltech.cs2.interfaces.IDictionary;

import java.util.Iterator;

public class BSTDictionary<K extends Comparable<? super K>, V>
        implements IDictionary<K, V> {

    protected BSTNode<K, V> root;
    protected int size;

    /**
     * Class representing an individual node in the Binary Search Tree
     */
    protected static class BSTNode<K, V> {
        public final K key;
        public V value;

        public BSTNode<K, V> left;
        public BSTNode<K, V> right;

        /**
         * Constructor initializes this node's key, value, and children
         */
        public BSTNode(K key, V value) {
            this.key = key;
            this.value = value;
            this.left = null;
            this.right = null;
        }

        public BSTNode(BSTNode<K, V> o) {
            this.key = o.key;
            this.value = o.value;
            this.left = o.left;
            this.right = o.right;
        }

        public boolean isLeaf() {
            return this.left == null && this.right == null;
        }

        public boolean hasBothChildren() {
            return this.left != null && this.right != null;
        }
    }

    /**
     * Initializes an empty Binary Search Tree
     */
    public BSTDictionary() {
        this.root = null;
        this.size = 0;
    }


    @Override
    public V get(K key) {
        BSTNode<K, V> holder = helper(this.root, key);
        if (holder != null){
            return holder.value;
        }
        return null;
    }
    private BSTNode<K, V> helper(BSTNode<K, V> node, K key){
        if(node == null){
            return null;
        }
        else if (node.key.compareTo(key) == 0){
            return node;
        }
        else if (node.key.compareTo(key) < 0){
           return helper(node.right, key);
        }
        else{
            return helper(node.left, key);
        }
    }

    @Override
    public V remove(K key) {
        V holder = this.get(key);
        if (holder == null){
            return null;
        }
        this.root = helper5(this.root, key);
        this.size --;
        return holder;
    }

    private BSTNode<K, V> helper5(BSTNode<K, V> node, K key){
        if (node == null){
            return null;
        }
        else if (node.key.compareTo(key) < 0){
            node.right = helper5(node.right, key);
        }
        else if (node.key.compareTo(key) > 0) {
            node.left = helper5(node.left, key);
        }
        else {
            if (node.isLeaf()){
                return null;
            }
            if (node.hasBothChildren()){
                BSTNode<K, V> smd = node.right;
                while (smd.left != null){
                    smd = smd.left;
                }
                node.right = helper5(node.right, smd.key);
                smd.left = node.left;
                smd.right = node.right;
                node = new BSTNode<>(smd);
            }else if (node.right != null){
                return node.right;
            }else{
                return node.left;
            }
        }
        return node;
    }

    @Override
    public V put(K key, V value) {
        V holder = this.get(key);
        this.root = helper2(this.root, value, key);
        if (holder == null){
            size ++;
        }
        return holder;
    }
    private BSTNode<K, V> helper2(BSTNode<K, V> node, V value, K key){
        if (node == null){
            node = new BSTNode<K, V>(key, value);
        }
       else if (node.key.compareTo(key) < 0){
            node.right =  helper2(node.right, value, key);
        }
        else if (node.key.compareTo(key) > 0){
            node.left =  helper2(node.left, value, key);
        }else {
            node.value = value;
        }
        return node;
    }

    @Override
    public boolean containsKey(K key) {
        return this.get(key) != null;
    }

    @Override
    public boolean containsValue(V value) {
        return this.values().contains(value);
    }

    /**
     * @return number of nodes in the BST
     */
    @Override
    public int size() {
        return this.size;
    }

    @Override
    public ICollection<K> keys() {
        IDeque<K> keys = new LinkedDeque<>();
        helper3(this.root, keys);
        return keys;
    }

    private void helper3 (BSTNode<K, V> node, IDeque<K> nuts){
        if (node == null){
            return;
        }
        nuts.add(node.key);
        if (node.isLeaf()){
            return;
        }
        if(node.hasBothChildren()){
            helper3(node.left, nuts);
            helper3(node.right, nuts);
        }
        else if (node.left != null){
            helper3(node.left, nuts);
        } else{
            helper3(node.right, nuts);
        }
    }
    @Override
    public ICollection<V> values() {
        IDeque<V> vals = new LinkedDeque<>();
        helper4(this.root, vals);
        return vals;
    }

    private void helper4 (BSTNode<K, V> node, IDeque<V> nuts){
        if (node == null){
            return;
        }
        nuts.add(node.value);
        if (node.isLeaf()){
            return;
        }
        if(node.hasBothChildren()){
            helper4(node.left, nuts);
            helper4(node.right, nuts);
        }
        else if (node.left != null){
            helper4(node.left, nuts);
        } else{
            helper4(node.right, nuts);
        }
    }
    /**
     * Implementation of an iterator over the BST
     */

    @Override
    public Iterator<K> iterator() {
        return keys().iterator();
    }

    @Override
    public String toString() {
        if (this.root == null) {
            return "{}";
        }

        StringBuilder contents = new StringBuilder();

        IQueue<BSTNode<K, V>> nodes = new ArrayDeque<>();
        BSTNode<K, V> current = this.root;
        while (current != null) {
            contents.append(current.key + ": " + current.value + ", ");

            if (current.left != null) {
                nodes.enqueue(current.left);
            }
            if (current.right != null) {
                nodes.enqueue(current.right);
            }

            current = nodes.dequeue();
        }

        return "{" + contents.toString().substring(0, contents.length() - 2) + "}";
    }
}
