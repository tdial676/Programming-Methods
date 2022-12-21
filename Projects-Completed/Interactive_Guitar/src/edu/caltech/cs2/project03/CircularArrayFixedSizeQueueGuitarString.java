package edu.caltech.cs2.project03;

import edu.caltech.cs2.datastructures.CircularArrayFixedSizeQueue;
import edu.caltech.cs2.interfaces.IFixedSizeQueue;
import edu.caltech.cs2.interfaces.IQueue;

import java.util.Random;


public class CircularArrayFixedSizeQueueGuitarString {
    private IFixedSizeQueue<Double> string;
    private static final Random rand = new Random();
    private static final int sample = 44100;
    private static final double eFactor = 0.996;

    public CircularArrayFixedSizeQueueGuitarString(double frequency) {
        int freq = (int)Math.ceil(44100/frequency);
        this.string = new CircularArrayFixedSizeQueue<>(freq);
        for(int i = 0; i <= this.string.size(); i++){
            this.string.enqueue(0.0);
        }
    }

    public int length() {
        return this.string.capacity();
    }

    public void pluck() {
        for (int i = 0; i < this.length(); i++){
            double displacement = rand.nextDouble() - .5;
            this.string.dequeue();
            this.string.enqueue(displacement);
        }
    }

    public void tic() {
        this.string.enqueue ((this.string.dequeue() + this.sample()) * (eFactor * .5));
    }

    public double sample() {
        return this.string.peek();
    }
}
