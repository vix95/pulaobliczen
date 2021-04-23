package Benchmark;

import PoolPattern.PoolGetType;
import PoolPattern.PoolManager;
import PoolPattern.PoolType;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
public class ParallelBenchmark {
    @Param({"1", "4"})
    int N_OBJ;

    @Param({"FLAG", "ARRAY"})
    PoolGetType GET_TYPE;

    public static void main(String[] args) throws Exception {
        org.openjdk.jmh.Main.main(args);
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    @Fork(value = 1, warmups = 4)
    @Warmup(iterations = 5, time = 1)
    @Measurement(iterations = 10, time = 2)
    public void ParallelBench() {
        PoolManager pool = PoolManager.builder()
                .pool_type(PoolType.PARALLEL)
                .pool_get_type(GET_TYPE)
                .n_obj(N_OBJ)
                .loop(5000)
                .rand_min(10)
                .rand_max(100)
                .do_advanced_operation(true)
                .build();

        pool.utilize();
    }
}
