package edu.caltech.cs2.datastructures;

import edu.caltech.cs2.interfaces.IFixedSizeQueue;

import java.util.Iterator;

public class CircularArrayFixedSizeQueue<E> implements IFixedSizeQueue<E> {
    private final E[] data;
    private int front;
    private int back;
    public CircularArrayFixedSizeQueue(int capacity) {
        this.data = (E[])new Object[capacity];
        this.front = this.back = -1;
    }
    @Override
    public boolean isFull() {
        if (this.front == 0 && this.back == this.capacity() - 1){
            return true;
        }
        return this.front == this.back + 1;
    }

    @Override
    public int capacity() {
        return this.data.length;
    }

    @Override
    public boolean enqueue(E e) {
        if (this.isFull()) {
            return false;
        } else {
            if (this.front == -1) {
                this.front = 0;
            }
            this.back = (this.back + 1) % this.capacity();
            this.data[this.back] = e;
            return true;
        }
    }

    @Override
    public E dequeue() {
        if (this.front == -1) {
            return null;
        }else {
            E removed = this.data[front];
            if (this.front == this.back){
                this.front = -1;
                this.back = -1;
            } else {
                this.front = (this.front + 1) % this.capacity();
            }
            return removed;
        }
    }

    @Override
    public E peek() {
        if (this.size() == 0) {
            return null;
        } else{
            return this.data[this.front % this.capacity()];
        }
    }

    @Override
    public int size() {
        if (this.front == -1 || this.back == -1) {
            return 0;
        }
        if (this.back >= this.front){
            return this.back - this.front + 1;
        }else  {
            return this.capacity() - this.front + this.back + 1;
        }
    }

    @Override
    public Iterator<E> iterator() {
        return new CircularArrayFixedSizeQueueIterator();
    }
    public String toString() {
        if(this.front == -1 && this.back == -1){
            return "[]";
        }
        StringBuilder holder = new StringBuilder("[");
        for(int i = 0; i < this.size(); i++) {
            holder.append(this.data[(this.front + i) % this.capacity()]).append(", ");
        }
        holder = new StringBuilder(holder.substring(0, holder.length() - 2));
        return holder + "]";
    }
    private class CircularArrayFixedSizeQueueIterator implements Iterator<E> {
        private int currIdx;

        public CircularArrayFixedSizeQueueIterator(){
            this.currIdx = 0;
        }
        @Override
        public boolean hasNext() {
            return this.currIdx < size();
        }

        @Override
        public E next() {
            E elem = CircularArrayFixedSizeQueue.this.data[(front + this.currIdx) % capacity()];
            this.currIdx++;
            return elem;
        }

    }
}
