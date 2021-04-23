package Benchmark;

import PoolPattern.PoolGetType;
import PoolPattern.PoolManager;
import PoolPattern.PoolType;
import org.openjdk.jmh.annotations.*;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
public class DemandingBenchmarkRunner {
    @Param({"10", "100"})
    int LOOP;

    @Param({"PARALLEL", "THREADS"})
    PoolType TYPE;

    @Param({"FLAG", "ARRAY"})
    PoolGetType GET_TYPE;

    @Param({"1", "3"})
    int N_OBJ;

    @Param({"2", "20"})
    int RAND_MIN;

    @Param({"50", "100"})
    int RAND_MAX;

    @Param({"true", "false"})
    boolean ADVANCED_OPERATION;

    public static void main(String[] args) throws Exception {
        org.openjdk.jmh.Main.main(args);
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    @Fork(value = 1, warmups = 2)
    @Warmup(iterations = 3, time = 1)
    @Measurement(iterations = 5, time = 2)
    public void SimpleConfiguration() {
        PoolManager pool = PoolManager.builder()
                .pool_type(TYPE)
                .pool_get_type(GET_TYPE)
                .n_obj(N_OBJ)
                .loop(LOOP)
                .rand_min(RAND_MIN)
                .rand_max(RAND_MAX)
                .do_advanced_operation(ADVANCED_OPERATION)
                .build();

        pool.utilize();
    }
}