# Java Local Cache Benchmark

Java의 대표적인 로컬 캐시 라이브러리 3종(**Caffeine, Ehcache 3, Guava Cache**)의 성능을 **JMH (Java Microbenchmark Harness)**를 사용하여 비교 분석하는 프로젝트입니다.

## 🛠 기술 스택

*   **Language**: Java 17
*   **Build Tool**: Gradle (Kotlin DSL)
*   **Benchmark Tool**: JMH (via `me.champeau.jmh` plugin)
*   **Target Libraries**:
    *   Caffeine (Latest)
    *   Ehcache 3 (On-heap configuration)
    *   Guava Cache

## 📊 벤치마크 시나리오

모든 테스트는 **최대 캐시 크기 10,000개**, **4개의 동시 스레드(Concurrency)** 환경에서 수행됩니다.

| 테스트 항목 | 메서드명 | 설명 |
|---|---|---|
| **Read Throughput** | `read[CacheName]` | 0 ~ 9,999 범위의 키를 조회하여 **100% Hit**율을 보장한 상태에서의 순수 읽기 성능 측정. |
| **Write Throughput** | `write[CacheName]` | 0 ~ 19,999 범위의 키를 무작위로 삽입하여 **캐시 교체(Eviction)** 및 락 경합(Lock Contention)이 발생하는 상황에서의 쓰기 성능 측정. |

## 🚀 실행 방법

터미널에서 프로젝트 디렉토리(`cache-benchmark`)로 이동한 후, 다음 명령어를 실행하십시오.

```bash
./gradlew jmh
```

## 📈 결과 해석 가이드

벤치마크 실행 후 출력되는 결과는 다음과 같이 해석할 수 있습니다.

*   **Mode**: `thrpt` (Throughput, 처리량)
*   **Score**: `ops/ms` (밀리초당 작업 처리 횟수)
    *   **수치가 높을수록 성능이 우수합니다.**
*   **GC Alloc Rate**: 작업당 메모리 할당량. 수치가 낮을수록 GC 부하가 적습니다.

### 일반적인 성능 양상
보편적으로 **Caffeine**이 읽기/쓰기 모든 면에서 가장 뛰어난 성능을 보이며, **Guava**는 상대적으로 가장 낮은 성능을 보입니다.
