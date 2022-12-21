package edu.caltech.cs2.datastructures;

import edu.caltech.cs2.interfaces.IDeque;
import edu.caltech.cs2.interfaces.IQueue;
import edu.caltech.cs2.interfaces.IStack;

import java.util.Iterator;

public class ArrayDeque<E> implements IDeque<E>, IQueue<E>, IStack<E> {
    private static final int DEFAULT_CAPACITY = 10;
    private static final int GROWTH = 2;
    private E[] data;
    private int size;

    public ArrayDeque(){
        this(DEFAULT_CAPACITY);
    }
    public ArrayDeque(int capacity) {
        this.data = (E[]) new Object[capacity];
        this.size = 0;
    }

    private void ensureCapacity(int size) {
        if (this.data.length < size) {
            E[] newData = (E[])new Object[this.data.length * GROWTH];
            for (int i = 0; i < this.size; i++) {
                newData[i] = this.data[i];
            }
            this.data = newData;
        }
    }
    @Override
    public void addFront(E e) {
        this.ensureCapacity(this.size + 1);
        for (int i = this.size; i > 0; i--) {
            this.data[i] = this.data[i-1];
        }
        this.data[0] = e;
        this.size++;
    }

    @Override
    public void addBack(E e) {
        this.ensureCapacity(this.size + 1);
        this.data[size] = e;
        this.size++;
    }

    @Override
    public E removeFront() {
        if (this.size == 0) {
            return null;
        }
        E removed = this.data[0];
        for (int i = 0; i < this.size - 1; i++) {
            this.data[i] = this.data[i+1];
        }
        this.size--;
        return removed;
    }

    @Override
    public E removeBack() {
        if (this.size == 0) {
            return null;
        }
        E removed = this.data[this.size - 1];
        this.size--;
        return removed;
    }

    @Override
    public boolean enqueue(E e) {
        int orgySize = this.size;
        this.addFront(e);
        if (orgySize < this.size){
            return true;
        }
        return false;
    }

    @Override
    public E dequeue() {
        return this.removeBack();
    }

    @Override
    public boolean push(E e) {
        int orgySize = this.size;
        this.addBack(e);
        if (orgySize < this.size) {
            return true;
        }
        return false;
    }

    @Override
    public E pop() {
        return this.removeBack();
    }

    @Override
    public E peekFront() {
        if(this.size != 0) {
            return this.data[0];
        }else {
            return null;
        }
    }

    @Override
    public E peekBack() {
        if(this.size != 0) {
            return this.data[this.size - 1];
        }else {
            return null;
        }
    }

    @Override
    public E peek() {
        return this.peekBack();
    }

    @Override
    public Iterator<E> iterator() {
        return new ArrayDequeIterator();
    }

    @Override
    public int size() {
        return this.size;
    }
    public String toString() {
        if (this.size == 0) {
            return "[]";
        }
        String holder = "[";
        for (int i = 0; i < this.size; i++){
            holder += this.data[i] + ", ";
        }
        holder = holder.substring(0, holder.length() - 2);
        return holder + "]";
    }
    public class ArrayDequeIterator implements Iterator<E> {
        private int currIdx;
        public ArrayDequeIterator(){
            this.currIdx = 0;
        }
        @Override
        public boolean hasNext() {
            return this.currIdx < (ArrayDeque.this).size;
        }
        @Override
        public E next() {
            E elem = ArrayDeque.this.data[this.currIdx];
            this.currIdx++;
            return elem;
        }
    }
}

