package PoolPattern;

import java.util.Random;

public class PooledObject {
    public PoolManager _manager;
    public int _index;
    public int _thread;
    public boolean _isFree = true;
    public int _hashCode;

    public PooledObject() {
        this._hashCode = this.hashCode();
    }

    public PooledObject(PooledObject o, PoolManager m) {
        this._index = o._index;
        this._hashCode = o._hashCode;
        this._manager = m;
    }

    public void advancedOperation(int rand_min, int rand_max) {
        Random random = new Random();
        int r = random.ints(rand_min, rand_max).findFirst().getAsInt();

        double result = 0;
        for (int j = 0; j < r; j++) {
            for (int j2 = 0; j2 < j; j2++) {
                for (int j3 = 0; j3 < j2; j3++) {
                    result = Math.sqrt(result + j) * j + Math.sqrt(result + j2) * j2 + Math.sqrt(result + j3) * j3;
                }
            }
        }
    }

    public void emptyOperation() {

    }

    public void reset() {
        this._isFree = true;
    }

    public void printCreated() {
        System.out.printf("Object with process no. %d was created\n", this._index);
    }

    public void printTaken() {
        System.out.printf("Thread %d: Object with process no. %d was taken\n", this._thread, this._index);
    }

    public void printReturned() {
        System.out.printf("Thread %d: Object with process no. %d was returned\n", this._thread, this._index);
    }

    public void printCloned() {
        System.out.printf("Object with process no. %d was cloned\n", this._index);
    }
}
