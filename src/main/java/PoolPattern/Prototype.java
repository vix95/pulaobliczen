package PoolPattern;

public interface Prototype<T> {

    T clone(PoolManager m);
}
