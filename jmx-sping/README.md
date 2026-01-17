# Spring Actuator Debugging Guide

This project is configured to help you explore how Spring Actuator works using a debugger.

## Configuration
- **Dependencies**: `spring-boot-starter-actuator` and `spring-boot-starter-web` are included.
- **Exposure**: All Web and JMX endpoints are exposed via `application.properties`.

## How to Debug

1. **Run the Application**:
   - Open the project in your IDE (IntelliJ IDEA, Eclipse, VS Code).
   - Locate `src/main/java/org/personal/jmxsping/JmxSpingApplication.java`.
   - Run the `main` method in **Debug** mode.

2. **Trigger Endpoints**:
   - Once the application is running (usually on port 8080), open your browser or use `curl`.
   - List all endpoints: [http://localhost:8080/actuator](http://localhost:8080/actuator)
   - Health check: [http://localhost:8080/actuator/health](http://localhost:8080/actuator/health)
   - Beans list: [http://localhost:8080/actuator/beans](http://localhost:8080/actuator/beans)
   - **Custom Endpoint**: [http://localhost:8080/actuator/custom](http://localhost:8080/actuator/custom)
     - I added `CustomEndpoint.java` so you can debug a simple custom implementation.
     - Try setting a breakpoint in `CustomEndpoint.getCustomData()`.

3. **Recommended Breakpoints**:
   To understand the internal flow, set breakpoints in the following classes (search for them in your IDE):

   - **`org.springframework.boot.actuate.endpoint.web.servlet.WebMvcEndpointHandlerMapping`**:
     - This is responsible for mapping HTTP requests to Actuator endpoints.
     - Breakpoint in `getHandler(HttpServletRequest request)` to see how it matches requests.

   - **`org.springframework.boot.actuate.endpoint.invoke.reflect.ReflectiveOperationInvoker`**:
     - This invokes the actual method on the endpoint bean.
     - Breakpoint in `invoke(InvocationContext context)`.

   - **`org.springframework.web.servlet.DispatcherServlet`**:
     - The entry point for all Spring MVC requests.
     - Breakpoint in `doDispatch(HttpServletRequest request, HttpServletResponse response)`.

## JMX Debugging
You can also connect via JConsole or VisualVM to the running Java process to inspect JMX beans under the `org.springframework.boot` domain.
