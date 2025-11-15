# java-library: Gradle `api` vs `implementation` 예제

이 저장소는 Java 라이브러리를 만들 때 Gradle `java-library` 플러그인의 `api`와 `implementation` 의존성 차이를 코드로 보여주는 최소 예제입니다.

## 프로젝트 구조

```
java-library/
├─ build.gradle            # 공통 설정 (Java 21 등)
├─ settings.gradle
├─ internallibrary/        # 내부 유틸 (소비자에게 직접 제공하지 않을 수도 있는 모듈)
│  ├─ build.gradle
│  └─ src/main/java/com/example/internallibrary/InternalLibrary.java
├─ mylibrary/              # 외부에 배포하는 라이브러리(퍼블릭 API 제공)
│  ├─ build.gradle         # 여기서 internallibrary 를 api 또는 implementation 으로 의존
│  └─ src/main/java/com/example/mylibrary/MyLibrary.java
└─ service/                # 라이브러리 소비자(애플리케이션)
   ├─ build.gradle
   └─ src/main/java/com/example/service/App.java
```

## 핵심 개념 정리

- `api` (java-library 플러그인 전용)
  - 의존 모듈의 공개 타입을 “소비자에게 노출”합니다.
  - 즉, 현재 모듈의 퍼블릭 API 시그니처에 그 의존 모듈의 타입이 등장해도, 그 타입은 소비자 쪽 컴파일 클래스패스에 자동으로 포함됩니다(전이적, transitive).

- `implementation`
  - 의존 모듈을 “내부 구현 세부사항”으로 취급합니다.
  - 의존 모듈의 타입은 소비자에게 노출되지 않습니다. 현재 모듈의 퍼블릭 API에 그 타입이 등장하면 소비자 쪽에서 컴파일 오류가 납니다(숨겨야 할 구현 세부사항이 새고 있다는 신호).

정리하면, “퍼블릭 API 시그니처에 등장하는 타입”의 소스 모듈은 `api`로, “내부에서만 사용하고 소비자에게 숨기고 싶은 구현 상세”는 `implementation`으로 선언하는 것이 원칙입니다.

## 이 예제에서의 적용

- `mylibrary/build.gradle`

```groovy
plugins {
    id 'java-library'
}

dependencies {
    api project(':internallibrary')
    // implementation project(':internallibrary')
}

java {
    withJavadocJar()
    withSourcesJar()
}
```

- `mylibrary`의 퍼블릭 API(`MyLibrary.greetV2`)는 `internallibrary`의 타입을 그대로 노출합니다.

```java
// mylibrary/src/main/java/com/example/mylibrary/MyLibrary.java
public class MyLibrary {
    public InternalLibrary.Name greetV2(String name) {
        return new InternalLibrary.Name(greet(name));
    }
}
```

- 소비자 애플리케이션(`service`)에서는 내부 라이브러리 타입을 직접 사용할 수 있습니다. 이는 `mylibrary`가 `internallibrary`를 `api`로 의존했기 때문입니다.

```java
// service/src/main/java/com/example/service/App.java
import com.example.internallibrary.InternalLibrary; // <-- 소비자에서 보임 (api 이므로)
import com.example.mylibrary.MyLibrary;

public class App {
    public static void main(String[] args) {
        MyLibrary service = new MyLibrary();
        String name = args.length > 0 ? args[0] : null;
        System.out.println(service.greet(name));

        InternalLibrary.Name greetMsg = service.greetV2(name); // 타입도 소비자에서 인식됨
        System.out.println(greetMsg);
    }
}
```

## 실습: `api` → `implementation`으로 바꿔 보기

1) `mylibrary/build.gradle`에서 다음처럼 수정합니다.

```groovy
dependencies {
    // api project(':internallibrary')
    implementation project(':internallibrary')
}
```

2) 빌드 또는 소비자 모듈 컴파일을 시도합니다.

```
./gradlew clean :service:build
```

3) 예상 결과

- 소비자(`service`)는 더 이상 `com.example.internallibrary.InternalLibrary` 타입을 볼 수 없으므로 컴파일 오류가 발생합니다. 대표적으로 다음과 같은 오류가 날 수 있습니다.
  - `error: cannot find symbol class InternalLibrary`
  - 또는 `MyLibrary#greetV2`의 반환 타입(`InternalLibrary.Name`)을 인식하지 못해 관련 컴파일 에러가 납니다.

즉, `implementation`은 소비자에게 타입을 노출하지 않고 숨기기 때문에, 퍼블릭 API에 그 타입이 등장하면 소비자에서 컴파일이 깨집니다. 이런 경우엔 다음 둘 중 하나를 선택해야 합니다.

- 그 타입을 퍼블릭 API에서 제거/래핑한다 → 계속 `implementation` 유지 가능
- 그 타입이 퍼블릭 API에 남아야 한다 → 해당 의존성을 `api`로 승격

## 실행 방법(현재 설정 기준: `api` 사용 중)

1) 빌드

```
./gradlew clean build
```

2) 애플리케이션 실행

`service` 모듈은 `jar`에 `Main-Class`가 설정되어 있습니다. 빌드 후 생성된 JAR로 실행합니다.

```
java -jar service/build/libs/service-0.1.0-SNAPSHOT.jar Junie
```

예상 출력(인자 미제공 시 기본 값 포함):

```
Hello, Junie!
Name[name=Hello, Junie!]
```

## 베스트 프랙티스 체크리스트

- 퍼블릭 API 시그니처(메서드 반환/파라미터/필드)에 등장하는 모든 타입의 소스 모듈은 `api`로 선언한다.
- 소비자에게 감추고 싶은 구현 세부사항(로깅, 내부 유틸, 파서, HTTP 클라이언트 등)은 `implementation`으로 선언한다.
- 의존성이 `implementation`인데 퍼블릭 API에 그 타입이 새어 나오지 않는지 수시로 점검한다.
- 만약 새어 나온다면:
  - 타입을 래핑하여 내부 구현을 은닉하거나,
  - 또는 해당 의존성을 `api`로 승격한다.

## 참고

- Gradle User Manual – Java Library Plugin: https://docs.gradle.org/current/userguide/java_library_plugin.html
