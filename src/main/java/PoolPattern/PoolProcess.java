package PoolPattern;

public class PoolProcess extends PooledObject implements Prototype<PoolProcess>, Runnable {

    public PoolProcess(PooledObject o, PoolManager m) {
        super(o, m);
    }

    @Override
    public PoolProcess clone(PoolManager m) {
        return new PoolProcess((PooledObject) this, m);
    }

    @Override
    public void run() {
        try {
            if (_manager.isDoAdvancedOperation()) this.advancedOperation(_manager.getRandMin(), _manager.getRandMax());
            else this.emptyOperation();

            if (_manager._pool_get_type == PoolGetType.ARRAY) _manager.release__Array(this);
            else if (_manager._pool_get_type == PoolGetType.FLAG) _manager.release__Flag(this);
            else throw new IllegalStateException("PoolGetType doesn't exist");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
