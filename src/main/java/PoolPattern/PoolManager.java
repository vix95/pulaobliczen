package PoolPattern;

import java.util.ArrayList;
import java.util.concurrent.*;
import java.util.stream.IntStream;

public class PoolManager {
    private final PoolProcess poolProcess = new PoolProcess(new PooledObject(), this);
    private volatile ArrayList<PoolProcess> pooled_objects;
    private volatile ArrayList<PoolProcess> free_objects;
    private volatile ArrayList<PoolProcess> busy_objects;
    private PoolType _pool_type;
    protected PoolGetType _pool_get_type;
    private int _pooled_n;
    private int _n;
    private int _n_max;
    private int _loop;
    private int _rand_min;
    private int _rand_max;
    private boolean _do_print;
    private boolean _do_track;
    private boolean _do_advanced_operation;
    private volatile int done_threads = 0;

    public static Builder builder() {
        return new Builder();
    }

    public PoolManager() {
    }

    public synchronized void add(PoolProcess o) {
        try {
            pooled_objects.add(o);
            free_objects.add(o);
        } catch (Exception ignore) {
        }
    }

    public synchronized void init() {
        this._pooled_n = _n;

        for (int i = 0; i < _n; i++) {
            PoolProcess clone = poolProcess.clone(this);
            clone._index = i;
            clone._hashCode = clone.hashCode();
            this.add(clone);

            if (this._do_track) clone.printCreated();
        }
    }

    public void utilize() {
        if (_pool_type == PoolType.PARALLEL) this.utilizeParallel();
        else if (_pool_type == PoolType.THREADS) this.utilizeThreads();
        else throw new IllegalStateException("PoolType doesn't exist");
    }

    private void utilizeParallel() {
        IntStream range = IntStream.rangeClosed(1, _loop);
        range.parallel().forEach(i -> {
            int size = this.getSize();

            PoolProcess o;
            if (_pool_get_type == PoolGetType.ARRAY) o = this.execute_get__Array(i);
            else if (_pool_get_type == PoolGetType.FLAG) o = this.execute_get__Flag(i);
            else throw new IllegalStateException("PoolGetType doesn't exist");

            if (this._do_advanced_operation) o.advancedOperation(_rand_min, _rand_max);
            else o.emptyOperation();

            if (this._do_print)
                System.out.printf("%d \t i = %3d \t n = %3d \t index = %3d\n", o._hashCode, i, size, o._index);

            if (_pool_get_type == PoolGetType.ARRAY) this.release__Array(o);
            else if (_pool_get_type == PoolGetType.FLAG) this.release__Flag(o);
            else throw new IllegalStateException("PoolGetType doesn't exist");
        });
    }

    private void utilizeThreads() {
        ExecutorService executor = Executors.newFixedThreadPool(4);

        for (int i = 1; i <= this._loop; i++) {
            if (_pool_get_type == PoolGetType.ARRAY) executor.execute(this.execute_get__Array(i));
            else if (_pool_get_type == PoolGetType.FLAG) executor.execute(this.execute_get__Flag(i));
            else throw new IllegalStateException("PoolGetType doesn't exist");
        }

        executor.shutdown();

        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private PoolProcess execute_get__Flag(int thread) {
        PoolProcess o = get__Flag(thread);

        while (o == null) {
            o = get__Flag(thread);
        }

        return o;
    }

    private PoolProcess get__Flag(int thread) {
        synchronized (PoolManager.class) {
            for (PoolProcess o : pooled_objects) {
                if (o._isFree) {
                    o._isFree = false;
                    o._thread = thread;
                    if (this._do_track) o.printTaken();
                    return o;
                }
            }

            if (this._pooled_n < this._n_max) {
                PoolProcess clone = poolProcess.clone(this);
                clone._index = this._pooled_n;
                clone._hashCode = clone.hashCode();
                clone._thread = thread;
                this.add(clone);
                this._pooled_n += 1;

                if (this._do_track) clone.printCloned();
                return clone;
            }

            for (PoolProcess o : pooled_objects) {
                if (o._isFree) {
                    o._isFree = false;
                    o._thread = thread;
                    if (this._do_track) o.printTaken();
                    return o;
                }
            }

            return null;
        }
    }

    protected void release__Flag(PoolProcess o) {
        synchronized (PoolManager.class) {
            o.reset();
            if (this._do_track) o.printReturned();
            done_threads++;
        }
    }

    private PoolProcess execute_get__Array(int thread) {
        PoolProcess o = get__Array(thread);

        while (o == null) {
            o = get__Array(thread);
        }

        return o;
    }

    private PoolProcess get__Array(int thread) {
        synchronized (PoolManager.class) {
            if (free_objects.isEmpty() && this._pooled_n < this._n_max) {
                PoolProcess clone = poolProcess.clone(this);
                clone._index = this._pooled_n;
                clone._hashCode = clone.hashCode();
                this.add(clone);
                this._pooled_n += 1;

                if (this._do_track) clone.printCloned();
            }

            if (free_objects.isEmpty()) return null;

            PoolProcess o = free_objects.get(0);
            free_objects.remove(o);
            o._isFree = false;
            o._thread = thread;
            busy_objects.add(o);
            if (this._do_track) o.printTaken();
            return o;
        }
    }

    public void release__Array(PoolProcess o) {
        synchronized (PoolManager.class) {
            this.busy_objects.remove(o);
            o.reset();
            this.free_objects.add(o);
            if (this._do_track) o.printReturned();
            done_threads++;
        }
    }

    public PoolType getPoolType() {
        return _pool_type;
    }

    public PoolGetType getPoolGetType() {
        return _pool_get_type;
    }

    public int getPooled_n() {
        return _pooled_n;
    }

    public int getNObjMax() {
        return _n_max;
    }

    private int getSize() {
        return pooled_objects.size();
    }

    public int getLoop() {
        return _loop;
    }

    public int getRandMin() {
        return _rand_min;
    }

    public int getRandMax() {
        return _rand_max;
    }

    public boolean isDoPrint() {
        return _do_print;
    }

    public boolean isDoTrack() {
        return _do_track;
    }

    public boolean isDoAdvancedOperation() {
        return _do_advanced_operation;
    }

    public ArrayList<PoolProcess> getPooled_objects() {
        return pooled_objects;
    }

    public ArrayList<PoolProcess> getFree_objects() {
        return free_objects;
    }

    public ArrayList<PoolProcess> getBusy_objects() {
        return busy_objects;
    }

    public int getDoneThreads() {
        return done_threads;
    }

    public void printSummary() {
        System.out.printf("\nTotal objects = %d \t Free objects = %d \t Busy objects = %d \t Done threads = %d\n",
                pooled_objects.size(), free_objects.size(), busy_objects.size(), done_threads);
    }

    public static final class Builder {
        private PoolType _pool_type;
        private PoolGetType _pool_get_type;
        private int _n;
        private int _n_max;
        private int _loop;
        private int _rand_min;
        private int _rand_max;
        private boolean _do_print;
        private boolean _do_track;
        private boolean _do_advanced_operation;

        public Builder pool_type(PoolType type) {
            this._pool_type = type;
            return this;
        }

        public Builder pool_get_type(PoolGetType type) {
            this._pool_get_type = type;
            return this;
        }

        public Builder n_obj(int i) {
            this._n = i;
            return this;
        }

        public Builder n_max_obj(int i) {
            if (this._n > i) {
                throw new IllegalStateException("'n' cannot be not higher than 'n_max");
            } else this._n_max = i;
            return this;
        }

        public Builder loop(int i) {
            this._loop = i;
            return this;
        }

        public Builder rand_min(int i) {
            this._rand_min = i;
            return this;
        }

        public Builder rand_max(int i) {
            this._rand_max = i;
            return this;
        }

        public Builder do_print(boolean bool) {
            this._do_print = bool;
            return this;
        }

        public Builder do_track(boolean bool) {
            this._do_track = bool;
            return this;
        }

        public Builder do_advanced_operation(boolean b) {
            this._do_advanced_operation = b;
            return this;
        }

        public PoolManager build() {
            boolean choiceExists = false;
            for (PoolType type : PoolType.values()) {
                if (type.equals(this._pool_type)) {
                    choiceExists = true;
                    break;
                }
            }

            if (!choiceExists) throw new IllegalStateException("'PoolType' is not exist");

            choiceExists = false;
            for (PoolGetType type : PoolGetType.values()) {
                if (type.equals(this._pool_get_type)) {
                    choiceExists = true;
                    break;
                }
            }

            if (!choiceExists) throw new IllegalStateException("'PoolGetType' is not exist");

            if (this._n == 0) throw new IllegalStateException("'n' cannot be not defined");

            if (this._n_max == 0) this._n_max = 9999;

            if (this._loop == 0) throw new IllegalStateException("'loop' cannot be not defined");

            if (this._rand_max == 0) throw new IllegalStateException("'rand_max' cannot be not defined");

            PoolManager manager = new PoolManager();
            manager._pool_type = this._pool_type;
            manager._pool_get_type = this._pool_get_type;
            manager._n = this._n;
            manager._n_max = this._n_max;
            manager._loop = this._loop;
            manager._rand_min = this._rand_min;
            manager._rand_max = this._rand_max;
            manager._do_print = this._do_print;
            manager._do_track = this._do_track;
            manager._do_advanced_operation = this._do_advanced_operation;
            manager.pooled_objects = new ArrayList();
            manager.free_objects = new ArrayList();
            manager.busy_objects = new ArrayList();
            manager.init();
            return manager;
        }
    }
}
