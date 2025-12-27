package org.personal;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// 1. @WebServlet 어노테이션은 "이 주소(/hello)로 요청이 오면 나를 실행해줘!"라고 
//    서블릿 컨테이너(WAS)에게 알리는 표식입니다.
@WebServlet("/hello")
public class HelloServlet extends HttpServlet {

	// 2. 서블릿 컨테이너가 서블릿을 처음 만들 때 딱 한 번 호출합니다 (생성)
	@Override
	public void init() throws ServletException {
		System.out.println("나의 생명주기: 서블릿이 생성되었습니다!");
	}

	// 3. 브라우저에서 GET 방식으로 요청이 들어오면 WAS가 스레드를 배정해 이 메서드를 실행합니다.
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {

		System.out.println("나의 생명주기: 요청이 들어와서 일을 시작합니다.");

		// 사용자가 보낸 데이터 읽기 (예: /hello?name=Gemini)
		String name = request.getParameter("name");
		if (name == null) name = "Guest";

		// 응답할 데이터 형식 지정 (HTML)
		response.setContentType("text/html;charset=UTF-8");

		// 4. 실제로 브라우저에 보낼 내용을 작성합니다.
		PrintWriter out = response.getWriter();
		out.println("<html>");
		out.println("<body>");
		out.println("<h1>안녕, " + name + "!</h1>");
		out.println("<p>이 페이지는 서블릿 컨테이너가 동적으로 만들었습니다.</p>");
		out.println("</body>");
		out.println("</html>");
	}

	// 5. 서버가 종료되거나 서블릿이 더 이상 필요 없을 때 호출됩니다 (소멸)
	@Override
	public void destroy() {
		System.out.println("나의 생명주기: 서블릿이 소멸됩니다. 안녕!");
	}
}