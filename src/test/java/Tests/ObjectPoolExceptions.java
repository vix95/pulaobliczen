package Tests;

import PoolPattern.PoolGetType;
import PoolPattern.PoolManager;
import PoolPattern.PoolType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ObjectPoolExceptions {
    @Test
    public void ExceptionBuilderPoolTypeTest() {
        Exception exception = assertThrows(RuntimeException.class, () -> {
            PoolManager pool = PoolManager.builder()
                    .build();
        });

        assertTrue(exception.getMessage().contains("'PoolType' is not exist"));
    }

    @Test
    public void ExceptionBuilderPoolGetTypeTest() {
        Exception exception = assertThrows(RuntimeException.class, () -> {
            PoolManager pool = PoolManager.builder()
                    .pool_type(PoolType.PARALLEL)
                    .build();
        });

        assertTrue(exception.getMessage().contains("'PoolGetType' is not exist"));
    }

    @Test
    public void ExceptionBuilderNObjTest() {
        Exception exception = assertThrows(RuntimeException.class, () -> {
            PoolManager pool = PoolManager.builder()
                    .pool_type(PoolType.PARALLEL)
                    .pool_get_type(PoolGetType.ARRAY)
                    .build();
        });

        assertTrue(exception.getMessage().contains("'n' cannot be not defined"));
    }

    @Test
    public void ExceptionBuilderLoopTest() {
        Exception exception = assertThrows(RuntimeException.class, () -> {
            PoolManager pool = PoolManager.builder()
                    .pool_type(PoolType.PARALLEL)
                    .pool_get_type(PoolGetType.ARRAY)
                    .n_obj(5)
                    .build();
        });

        assertTrue(exception.getMessage().contains("'loop' cannot be not defined"));
    }

    @Test
    public void ExceptionBuilderRandMaxTest() {
        Exception exception = assertThrows(RuntimeException.class, () -> {
            PoolManager pool = PoolManager.builder()
                    .pool_type(PoolType.PARALLEL)
                    .pool_get_type(PoolGetType.ARRAY)
                    .n_obj(5)
                    .loop(50)
                    .build();
        });

        assertTrue(exception.getMessage().contains("'rand_max' cannot be not defined"));
    }

    @Test
    public void NotHandledPoolTypeTest() {
        Exception exception = assertThrows(RuntimeException.class, () -> {
            PoolManager pool = PoolManager.builder()
                    .pool_type(PoolType.NOT_EXISTS)
                    .pool_get_type(PoolGetType.ARRAY)
                    .n_obj(5)
                    .loop(50)
                    .rand_max(50)
                    .build();

            pool.utilize();
        });

        assertTrue(exception.getMessage().contains("PoolType doesn't exist"));
    }

    @Test
    public void NotHandledPoolGetTypeTest() {
        Exception exception = assertThrows(RuntimeException.class, () -> {
            PoolManager pool = PoolManager.builder()
                    .pool_type(PoolType.PARALLEL)
                    .pool_get_type(PoolGetType.NOT_EXISTS)
                    .n_obj(5)
                    .loop(50)
                    .rand_max(50)
                    .build();

            pool.utilize();
        });

        assertTrue(exception.getMessage().contains("PoolGetType doesn't exist"));
    }
}
