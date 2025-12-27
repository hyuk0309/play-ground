# 🚀 Pure Java Servlet & Tomcat 수동 배포 가이드

이 가이드는 Spring 프레임워크 없이 **순수 자바 서블릿(Pure Java Servlet)**을 작성하고, Homebrew로 설치한 **Tomcat**에 수동으로 배포하여 WAS(Web Application Server)의 동작 원리를 확인하는 과정을 다룹니다.

---

## 1. 환경 준비

- **OS**: macOS (Apple Silicon / Intel)
- **Java**: JDK 17 이상 권장
- **WAS**: Apache Tomcat 10.x 이상 (Homebrew 설치)

### Tomcat 설치 (Homebrew)
```bash
# Tomcat 설치
brew install tomcat

# 설치 경로 확인 (M1/M2/M3 맥 기준)
# /opt/homebrew/opt/tomcat/libexec
```

---

## 2. 프로젝트 구조 (Standard Web Directory)

톰캣은 아래와 같은 엄격한 폴더 구조를 준수해야 애플리케이션을 인식합니다.

```plaintext
my-app/                 # 애플리케이션 루트 (Context Path)
└── WEB-INF/            # 브라우저에서 직접 접근 불가능한 설정 영역
    └── classes/        # 컴파일된 .class 파일이 위치하는 곳
        └── org/
            └── personal/
                └── HelloServlet.class
```

---

## 3. 소스 코드 작성 (`HelloServlet.java`)

> **중요**: Tomcat 10 이상부터는 `javax.servlet` 대신 `jakarta.servlet` 패키지를 사용합니다.

```java
package org.personal;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/hello")
public class HelloServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        String name = request.getParameter("name");
        if (name == null) name = "Guest";

        PrintWriter out = response.getWriter();
        out.println("<h1>Hello, " + name + "!</h1>");
        out.println("<p>This is a 동적 페이지 created by Servlet Container.</p>");
    }
}
```

---

## 4. 컴파일 및 배포 단계

### Step 1: 컴파일
톰캣의 라이브러리(`servlet-api.jar`)를 참조하여 컴파일합니다.

```bash
javac -cp "/opt/homebrew/opt/tomcat/libexec/lib/servlet-api.jar" -d . HelloServlet.java
```
*성공 시 현재 폴더에 `org/` 폴더 구조와 함께 `.class` 파일이 생성됩니다.*

### Step 2: 배포 폴더 생성
```bash
mkdir -p /opt/homebrew/opt/tomcat/libexec/webapps/my-app/WEB-INF/classes
```

### Step 3: 파일 복사 (배포)
```bash
cp -r org /opt/homebrew/opt/tomcat/libexec/webapps/my-app/WEB-INF/classes/
```

---

## 5. 서버 실행 및 결과 확인

### 서버 실행
```bash
catalina run
```

### 브라우저 접속
아래 주소로 접속하여 결과를 확인합니다.

- **기본 접속**: [http://localhost:8080/my-app/hello](http://localhost:8080/my-app/hello)
- **파라미터 전달**: [http://localhost:8080/my-app/hello?name=Gemini](http://localhost:8080/my-app/hello?name=Gemini)

---

## 🛠 트러블슈팅

- **`package jakarta.servlet does not exist`**: 컴파일 시 `-cp` 경로가 잘못되었거나 톰캣 버전이 낮은 경우입니다.
- **`404 Not Found`**: `webapps` 아래의 폴더 구조가 정확한지, `@WebServlet("/hello")` 경로가 맞는지 확인하세요.
- **`cd: too many arguments`**: 파일 복사 시 `cd` 대신 `cp -r` 명령어를 사용했는지 확인하세요.

---

이제 이 `README.md` 내용대로라면 언제든 WAS의 기초를 복습하실 수 있습니다!

혹시 나중에 이 과정을 자동화해주는 **빌드 도구(Maven/Gradle)**에 대해서도 궁금해지시면 언제든 말씀해 주세요. 이제 수동의 고통을 알았으니 자동화의 소중함을 느끼실 차례입니다! 🚀