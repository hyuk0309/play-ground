plugins {
    id("java")
    id("me.champeau.jmh") version "0.7.2"
}

group = "com.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // JMH Core (Plugin automatically handles basic dependencies, but creating configurations explicitly is safe)
    jmh("org.openjdk.jmh:jmh-core:1.37")
    jmh("org.openjdk.jmh:jmh-generator-annprocess:1.37")

    // Cache Libraries
    jmh("com.github.ben-manes.caffeine:caffeine:3.1.8")
    jmh("org.ehcache:ehcache:3.10.8")
    jmh("com.google.guava:guava:33.0.0-jre")

    // Annotation Processor for IntelliJ Support
    jmhAnnotationProcessor("org.openjdk.jmh:jmh-generator-annprocess:1.37")
    
    // Logging for Ehcache (optional but good to silence slf4j warnings if needed)
    jmh("org.slf4j:slf4j-simple:2.0.9")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

jmh {
    // JMH Configuration Strategy
    // 실행 시 반복 횟수나 워밍업을 줄여서 빠르게 테스트해볼 수 있도록 설정합니다.
    // 실제 정밀한 벤치마크를 위해서는 이 값을 늘려야 합니다.
    warmupIterations.set(1)
    iterations.set(3)
    fork.set(1)
    threads.set(4) // 기본적으로 4개의 스레드 사용 (멀티스레드 환경 시뮬레이션)
    resultFormat.set("text") // 결과 출력 포맷
    profilers.add("gc") // GC 프로파일러 추가 (선택 사항)
}
