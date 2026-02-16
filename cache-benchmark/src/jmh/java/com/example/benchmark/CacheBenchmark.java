package com.example.benchmark;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.common.cache.CacheBuilder;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Threads(4) // 멀티스레드 환경 시뮬레이션
public class CacheBenchmark {

    private static final int CACHE_SIZE = 10_000;
    private static final int KEY_RANGE = 10_000; // Read Hit율 100%를 위해 사이즈와 동일하게 설정

    // Caffeine Cache
    private Cache<Integer, String> caffeineCache;

    // Guava Cache
    private com.google.common.cache.Cache<Integer, String> guavaCache;

    // Ehcache 3
    private CacheManager ehcacheManager;
    private org.ehcache.Cache<Integer, String> ehcache;

    @Setup(Level.Trial)
    public void setup() {
        // 1. Configure Caffeine
        caffeineCache = Caffeine.newBuilder()
                .maximumSize(CACHE_SIZE)
                .build();

        // 2. Configure Guava
        guavaCache = CacheBuilder.newBuilder()
                .maximumSize(CACHE_SIZE)
                .build();

        // 3. Configure Ehcache 3
        ehcacheManager = CacheManagerBuilder.newCacheManagerBuilder().build(true);
        ehcache = ehcacheManager.createCache("benchmarkCache",
                CacheConfigurationBuilder.newCacheConfigurationBuilder(
                        Integer.class, String.class,
                        ResourcePoolsBuilder.heap(CACHE_SIZE)) // On-heap 전용
        );

        // Pre-populate caches for Read tests to ensure 100% Hit ratio
        for (int i = 0; i < KEY_RANGE; i++) {
            String value = "Value-" + i;
            caffeineCache.put(i, value);
            guavaCache.put(i, value);
            ehcache.put(i, value);
        }
    }

    @TearDown(Level.Trial)
    public void tearDown() {
        if (ehcacheManager != null) {
            ehcacheManager.close();
        }
    }

    // =========================================================
    // Read Throughput (100% Hit)
    // =========================================================

    @Benchmark
    public void readCaffeine(Blackhole bh) {
        int key = ThreadLocalRandom.current().nextInt(KEY_RANGE);
        bh.consume(caffeineCache.getIfPresent(key));
    }

    @Benchmark
    public void readGuava(Blackhole bh) {
        int key = ThreadLocalRandom.current().nextInt(KEY_RANGE);
        bh.consume(guavaCache.getIfPresent(key));
    }

    @Benchmark
    public void readEhcache(Blackhole bh) {
        int key = ThreadLocalRandom.current().nextInt(KEY_RANGE);
        bh.consume(ehcache.get(key));
    }

    // =========================================================
    // Write Throughput (Insert & Eviction)
    // =========================================================

    @Benchmark
    public void writeCaffeine() {
        // 캐시 크기보다 큰 범위의 키를 사용하여 Eviction 유도 가능
        // 혹은 단순히 계속 덮어쓰기/새로운 키 삽입 테스트
        int key = ThreadLocalRandom.current().nextInt(CACHE_SIZE * 2); 
        caffeineCache.put(key, "Value-" + key);
    }

    @Benchmark
    public void writeGuava() {
        int key = ThreadLocalRandom.current().nextInt(CACHE_SIZE * 2);
        guavaCache.put(key, "Value-" + key);
    }

    @Benchmark
    public void writeEhcache() {
        int key = ThreadLocalRandom.current().nextInt(CACHE_SIZE * 2);
        ehcache.put(key, "Value-" + key);
    }

    // 디버깅을 위한 Main 메서드
    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(CacheBenchmark.class.getSimpleName())
                .forks(0) // 디버깅을 위해 필수 (0 = 현재 프로세스에서 실행)
                .warmupIterations(0) // 빠른 디버깅을 위해 Warmup 제거
                .measurementIterations(1) // 빠른 디버깅을 위해 1회만 실행
                .build();

        new Runner(opt).run();
    }
}
