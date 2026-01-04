# JIT Compiler Demo

이 프로젝트는 Java의 JIT(Just-In-Time) 컴파일러가 어떻게 동작하는지 관찰하고 이해하기 위한 간단한 데모 프로젝트입니다.

## 프로젝트 목적
- **JIT 컴파일러 관찰**: JVM이 반복적으로 실행되는 코드(Hotspot)를 감지하고, 바이트코드를 네이티브 기계어로 컴파일하여 성능을 최적화하는 과정을 확인합니다.
- **성능 최적화 이해**: `calculate` 메서드와 같은 간단한 연산이 수백만 번 반복될 때, 인터프리터 방식에서 컴파일 방식으로 전환되는 지점을 학습할 수 있습니다.

## Local 실행 방법

이 프로젝트는 Gradle을 사용하여 빌드하고 실행할 수 있습니다.

### 1. 프로젝트 빌드
터미널에서 아래 명령어를 입력하여 프로젝트를 빌드하고 실행 가능한 JAR 파일을 생성합니다.

```bash
./gradlew clean jar
```

### 2. JAR 파일 실행
빌드가 완료되면 `build/libs` 디렉토리에 생성된 JAR 파일을 실행합니다.

```bash
java -jar build/libs/jit-compiler-demo-1.0-SNAPSHOT.jar
```

### JIT 컴파일 로그 확인 (추가 팁)
JIT 컴파일러가 실제로 동작하는 로그를 확인하고 싶다면, 실행 시 아래의 JVM 옵션을 추가해 보세요.

```bash
java -XX:+PrintCompilation -jar build/libs/jit-compiler-demo-1.0-SNAPSHOT.jar
```
이 옵션을 사용하면 어떤 메서드가 어느 시점에 JIT 컴파일되었는지 실시간으로 확인할 수 있습니다.
