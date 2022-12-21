package edu.caltech.cs2.datastructures;

import edu.caltech.cs2.interfaces.IDeque;
import edu.caltech.cs2.interfaces.IQueue;
import edu.caltech.cs2.interfaces.IStack;

import java.util.Iterator;

public class LinkedDeque<E> implements IDeque<E>, IQueue<E>, IStack<E> {
    private Node<E> head;
    private int size;
    private Node<E> tail;
    private static class Node<E> {
        public final E data;
        public Node<E> next;
        public Node<E> previous;

        public Node(E data) {
            this(data, null);
        }

        public Node(E data, Node<E> next) {
            this.data = data;
            this.next = next;
            if (this.next != null) {
                this.previous = this;
            }else{
                this.previous = null;
            }

        }
    }

    public LinkedDeque(){
        this.head = null;
        this.tail = null;
        this.size = 0;
    }
    @Override
    public void addFront(E e) {
        Node<E> holder = new Node<>(e);
        if(this.head == null) {
            this.head = holder;
            this.tail = this.head;
        } else{
            holder.next = this.head;
            this.head.previous = holder;
            this.head = holder;
        }
        this.size++;
    }

    @Override
    public void addBack(E e) {
        Node<E> holder = new Node<>(e);
        if (this.head == null) {
            this.head = holder;
            this.tail = this.head;
        } else {
            holder.previous = this.tail;
            this.tail.next = holder;
            this.tail = holder;
        }
        this.size++;
    }

    @Override
    public E removeFront() {
        if (this.size == 0) {
            return null;
        }
        Node<E> removed = this.head;
        this.head = this.head.next;
        if(this.head == null) {
            this.tail = null;
        }else{
            this.head.previous = null;
        }
        this.size--;
        return removed.data;
    }

    @Override
    public E removeBack() {
        if (this.size == 0) {
            return null;
        }
        Node<E> removed = this.tail;
        this.tail = this.tail.previous;
        if (this.tail == null) {
            this.head = null;
        }
        else{
            this.tail.next = null;
        }
        this.size--;
        return removed.data;
    }

    @Override
    public boolean enqueue(E e) {
        int orgySize = this.size;
        this.addFront(e);
        return orgySize < this.size;
    }

    @Override
    public E dequeue() {
        return this.removeBack();
    }

    @Override
    public boolean push(E e) {
        int orgySize = this.size;
        this.addBack(e);
        return orgySize < this.size;
    }

    @Override
    public E pop() {
        return this.removeBack();
    }

    @Override
    public E peekFront() {
        if (this.size != 0) {
            return this.head.data;
        }
        return null;
    }

    @Override
    public E peekBack() {
        if (this.size != 0) {
            return this.tail.data;
        }
        return null;
    }

    @Override
    public E peek() {
        return this.peekBack();
    }

    @Override
    public Iterator<E> iterator() {
        return new LinkedDequeIterator();
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public String toString() {
        if (this.size == 0) {
            return "[]";
        }
        String holder = "[";
        for (E item: this){
            holder += item + ", ";
        }
        holder = holder.substring(0, holder.length() - 2);
        return holder + "]";
    }

    private class LinkedDequeIterator implements Iterator<E> {
        private Node<E> curr;

        public LinkedDequeIterator(){
            this.curr = head;
        }
        @Override
        public boolean hasNext() {
            return this.curr != null;
        }

        @Override
        public E next() {
            E element = this.curr.data;
            this.curr = this.curr.next;
            return element;
        }
    }
}
