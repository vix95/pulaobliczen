package Tests;

import PoolPattern.PoolGetType;
import PoolPattern.PoolManager;
import PoolPattern.PoolType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ObjectPoolTest {
    private final int N_OBJ = 2;
    private final int N_OBJ_MAX = 10;
    private final int LOOP = 1000;
    private final int RAND_MIN = 2;
    private final int RAND_MAX = 100;
    private final boolean DO_TRACK = false;
    private final boolean DO_PRINT = false;
    private final boolean DO_ADVANCED_OPERATION = true;

    @Test
    public void TwoInstancesManagerTest() {
        // check if objects are different
        PoolManager manager1 = PoolManager.builder()
                .pool_type(PoolType.PARALLEL)
                .pool_get_type(PoolGetType.ARRAY)
                .n_obj(N_OBJ)
                .loop(LOOP)
                .rand_min(RAND_MIN)
                .rand_max(RAND_MAX)
                .build();

        PoolManager manager2 = PoolManager.builder()
                .pool_type(PoolType.PARALLEL)
                .pool_get_type(PoolGetType.ARRAY)
                .n_obj(N_OBJ)
                .loop(LOOP)
                .rand_min(RAND_MIN)
                .rand_max(RAND_MAX)
                .build();

        assertInstanceOf(PoolManager.class, manager1);
        assertInstanceOf(PoolManager.class, manager2);
        assertNotEquals(manager1, manager2);
    }

    @Test
    public void ManagerEqualsBuilderTest() {
        PoolManager pool = PoolManager.builder()
                .pool_type(PoolType.PARALLEL)
                .pool_get_type(PoolGetType.ARRAY)
                .n_obj(2)
                .loop(50)
                .rand_min(20)
                .rand_max(500)
                .do_print(false)
                .do_track(true)
                .do_advanced_operation(true)
                .build();

        assertEquals(PoolType.PARALLEL, pool.getPoolType());
        assertEquals(PoolGetType.ARRAY, pool.getPoolGetType());
        assertEquals(2, pool.getPooled_n());
        assertEquals(50, pool.getLoop());
        assertEquals(20, pool.getRandMin());
        assertEquals(9999, pool.getNObjMax());
        assertFalse(pool.isDoPrint());
        assertTrue(pool.isDoTrack());
        assertTrue(pool.isDoAdvancedOperation());

        assertEquals(500, pool.getRandMax());
    }

    @Test
    public void ManagerParallelArrayTest() {
        PoolManager pool = PoolManager.builder()
                .pool_type(PoolType.PARALLEL)
                .pool_get_type(PoolGetType.ARRAY)
                .n_obj(N_OBJ)
                .n_max_obj(N_OBJ_MAX)
                .loop(LOOP)
                .rand_min(RAND_MIN)
                .rand_max(RAND_MAX)
                .do_track(DO_TRACK)
                .do_print(DO_PRINT)
                .do_advanced_operation(DO_ADVANCED_OPERATION)
                .build();

        pool.utilize();

        assertEquals(LOOP, pool.getDoneThreads());
        assertTrue(pool.getPooled_n() <= N_OBJ_MAX);
        assertEquals(pool.getPooled_objects().size(), pool.getFree_objects().size());
        assertEquals(0, pool.getBusy_objects().size());
    }

    @Test
    public void ManagerParallelFlagTest() {
        PoolManager pool = PoolManager.builder()
                .pool_type(PoolType.PARALLEL)
                .pool_get_type(PoolGetType.FLAG)
                .n_obj(N_OBJ)
                .n_max_obj(N_OBJ_MAX)
                .loop(LOOP)
                .rand_min(RAND_MIN)
                .rand_max(RAND_MAX)
                .do_track(DO_TRACK)
                .do_print(DO_PRINT)
                .do_advanced_operation(DO_ADVANCED_OPERATION)
                .build();

        pool.utilize();

        assertEquals(LOOP, pool.getDoneThreads());
        assertTrue(pool.getPooled_n() <= N_OBJ_MAX);
        assertEquals(pool.getPooled_objects().size(), pool.getFree_objects().size());
        assertEquals(0, pool.getBusy_objects().size());
    }

    @Test
    public void ManagerThreadsArrayTest() {
        PoolManager pool = PoolManager.builder()
                .pool_type(PoolType.THREADS)
                .pool_get_type(PoolGetType.ARRAY)
                .n_obj(N_OBJ)
                .n_max_obj(N_OBJ_MAX)
                .loop(LOOP)
                .rand_min(RAND_MIN)
                .rand_max(RAND_MAX)
                .do_track(DO_TRACK)
                .do_print(DO_PRINT)
                .do_advanced_operation(DO_ADVANCED_OPERATION)
                .build();

        pool.utilize();

        assertEquals(LOOP, pool.getDoneThreads());
        assertTrue(pool.getPooled_n() <= N_OBJ_MAX);
        assertEquals(pool.getPooled_objects().size(), pool.getFree_objects().size());
        assertEquals(0, pool.getBusy_objects().size());
    }

    @Test
    public void ManagerThreadsFlagTest() {
        PoolManager pool = PoolManager.builder()
                .pool_type(PoolType.THREADS)
                .pool_get_type(PoolGetType.FLAG)
                .n_obj(N_OBJ)
                .n_max_obj(N_OBJ_MAX)
                .loop(LOOP)
                .rand_min(RAND_MIN)
                .rand_max(RAND_MAX)
                .do_track(DO_TRACK)
                .do_print(DO_PRINT)
                .do_advanced_operation(DO_ADVANCED_OPERATION)
                .build();

        pool.utilize();

        assertEquals(LOOP, pool.getDoneThreads());
        assertTrue(pool.getPooled_n() <= N_OBJ_MAX);
        assertEquals(pool.getPooled_objects().size(), pool.getFree_objects().size());
        assertEquals(0, pool.getBusy_objects().size());
    }

    @Test
    public void ManagerTest() {
        PoolManager pool = PoolManager.builder()
                .pool_type(PoolType.PARALLEL)
                .pool_get_type(PoolGetType.FLAG)
                .n_obj(2)
                .n_max_obj(10)
                .loop(500)
                .rand_min(20)
                .rand_max(500)
                .do_track(false)
                .do_print(false)
                .do_advanced_operation(true)
                .build();

        pool.utilize();
    }
}
