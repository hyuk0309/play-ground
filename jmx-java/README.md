# JMX Monitoring Application

JMX(Java Management Extensions)를 이용해 애플리케이션의 리소스를 모니터링하고 설정을 변경하는 간단한 예제 프로젝트입니다.

## 1. 프로젝트 구조

- **`SystemConfigMBean.java`**: MBean 인터페이스. 모니터링할 속성(`ThreadCount`, `SchemaName`)과 오퍼레이션(`doConfig`)을 정의.
- **`SystemConfig.java`**: 실제 비즈니스 로직 구현체. MBean 인터페이스를 구현하며 값 변경 시 로그를 출력.
- **`JmxAgent.java`**: 메인 애플리케이션. MBeanServer에 `SystemConfig` 객체를 등록하고 JConsole 연결을 대기.

## 2. 컴파일 및 실행

### 2.1 컴파일
소스 코드를 컴파일하여 `out` 디렉토리에 저장합니다.

```bash
mkdir -p out
javac -d out src/main/java/com/example/jmx/*.java
```

### 2.2 실행 (JMX 에이전트 활성화)
JMX 원격 접속을 허용하는 옵션을 포함하여 애플리케이션을 실행합니다.

```bash
java -cp out \
  -Dcom.sun.management.jmxremote \
  -Dcom.sun.management.jmxremote.port=9999 \
  -Dcom.sun.management.jmxremote.authenticate=false \
  -Dcom.sun.management.jmxremote.ssl=false \
  com.example.jmx.JmxAgent
```

**옵션 설명:**
- `-Dcom.sun.management.jmxremote`: JMX 원격 관리 기능 활성화
- `-Dcom.sun.management.jmxremote.port=9999`: 9999번 포트로 접속 허용
- `-Dcom.sun.management.jmxremote.authenticate=false`: 인증 절차 생략 (테스트용)
- `-Dcom.sun.management.jmxremote.ssl=false`: SSL 암호화 사용 안 함 (테스트용)

실행 후 터미널에 **"Press Enter to exit..."** 메시지가 뜨면 정상적으로 실행 중인 상태입니다.

## 3. 모니터링 (JConsole)

### 3.1 JConsole 실행
새로운 터미널 창에서 아래 명령어를 입력합니다.

```bash
jconsole
```

### 3.2 연결 설정
1. **JConsole** 창이 뜨면 **"Remote Process" (원격 프로세스)**를 선택합니다.
2. 주소 입력란에 `localhost:9999`를 입력합니다.
3. Username/Password는 비워두고 **Connect**를 클릭합니다.
4. 보안 경고창(Secure connection failed)이 뜨면 **"Insecure connection"**을 선택하여 진행합니다.

### 3.3 커스텀 MBean 확인
1. 상단 탭에서 **[MBeans]**를 클릭합니다.
2. 왼쪽 트리 메뉴에서 `com.example.jmx` 패키지를 찾아 펼칩니다.
3. `SystemConfig`를 클릭하여 다음을 수행할 수 있습니다.
    - **Attributes**: `ThreadCount`, `SchemaName` 값을 실시간으로 조회하고 수정할 수 있습니다.
    - **Operations**: `doConfig` 버튼을 눌러 메소드를 원격으로 실행하고 결과를 확인할 수 있습니다.

> **참고:** `java.lang`, `java.util` 등은 JVM이 기본적으로 제공하는 **Platform MXBean**으로, 메모리나 스레드 상태 등을 모니터링하는 데 사용됩니다.
