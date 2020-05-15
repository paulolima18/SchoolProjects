package controller;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class BoundedBuffer {

    private int[] values;
    private int poswrite;

    Condition isEmpty;
    Condition isFull;
    ReentrantLock lock;

    public BoundedBuffer(int size) {

        this.values = new int[size];
        this.poswrite = 0;
    }

    public synchronized void put (int v) {
        while (this.poswrite == this.values.length) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        this.values[poswrite] = v;
        this.poswrite++;

        this.notifyAll();
    }

    //LIFO
    public synchronized int get() {

        while (this.poswrite == 0) {

            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        this.notifyAll();

        int save_value;

        save_value = this.values[poswrite-1];
        this.poswrite--;

        return save_value;
    }

    public synchronized boolean isEmpty() {

        return this.values.length == 0;
    }

    public synchronized boolean isFull() {

        return this.poswrite == this.values.length;
    }

    public synchronized int getSize() {

        return this.poswrite;
    }
}