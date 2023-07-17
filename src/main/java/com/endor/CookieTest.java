package com.endor;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(urlPatterns={"/cookietest"})
public class CookieTest extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        HtmlUtil.printHtmlHeader(response);
        HtmlUtil.startBody(response);
        HtmlUtil.printMenu(response);
        HtmlUtil.printCurrentTitle("Cookie Test", response);

        HtmlUtil.openTable(response);
        HtmlUtil.openRow(response);
        HtmlUtil.openCol(response);

        out.println("<h3>Added these cookies using HttpServletResponse.addCookie(Cookie c) method</h3>");
        out.println("<br/>");
        out.println("<br/>");

        int count = 0;

        // Set-Cookie: addCookie1=Secure_HttpOnly; Path=/; Secure; HttpOnly
        Cookie secure_HttpOnlyCookie = new Cookie("addCookie1", "Secure_HttpOnly");
        secure_HttpOnlyCookie.setPath("/");
        secure_HttpOnlyCookie.setHttpOnly(true);
        secure_HttpOnlyCookie.setSecure(true);
        response.addCookie(secure_HttpOnlyCookie);
        out.println(++count + ". addCookie1=Secure_HttpOnly; Path=/; Secure; HttpOnly");
        out.println("<br/>");

        // Set-Cookie: addCookie2=NotSecure_HttpOnly; Path=/; HttpOnly
        Cookie notSecure_HttpOnlyCookie = new Cookie("addCookie2", "NotSecure_HttpOnly");
        notSecure_HttpOnlyCookie.setPath("/");
        notSecure_HttpOnlyCookie.setHttpOnly(true);
        notSecure_HttpOnlyCookie.setSecure(false);
        response.addCookie(notSecure_HttpOnlyCookie);
        out.println(++count + ". addCookie2=NotSecure_HttpOnly; Path=/; HttpOnly");
        out.println("<br/>");

        // Set-Cookie: addCookie3=Secure_NotHttpOnly; Path=/; Secure;
        Cookie secure_NotHttpOnlyCookie = new Cookie("addCookie3", "Secure_NotHttpOnly");
        secure_NotHttpOnlyCookie.setPath("/");
        secure_NotHttpOnlyCookie.setHttpOnly(false);
        secure_NotHttpOnlyCookie.setSecure(true);
        response.addCookie(secure_NotHttpOnlyCookie);
        out.println(++count + ". addCookie3=Secure_NotHttpOnly; Path=/; Secure;");
        out.println("<br/>");

        // Set-Cookie: addCookie4=NotSecure_NotHttpOnly; Path=/;
        Cookie notSecure_NotHttpOnlyCookie = new Cookie("addCookie4", "NotSecure_NotHttpOnly");
        notSecure_NotHttpOnlyCookie.setPath("/");
        notSecure_NotHttpOnlyCookie.setHttpOnly(false);
        notSecure_NotHttpOnlyCookie.setSecure(false);
        response.addCookie(notSecure_NotHttpOnlyCookie);
        out.println(++count + ". addCookie4=NotSecure_NotHttpOnly; Path=/;");
        out.println("<br/>");

        out.println("<br/>");
        out.println("<br/>");
        out.println("<br/>");

        out.println("<h3>Added these cookies using HttpServletResponse.addHeader(String name, String value) method with name as 'Set-Cookie'</h3>");
        out.println("<br/>");
        out.println("<br/>");

        // Set-Cookie: addCookie1=Secure_HttpOnly; Path=/; Secure; HttpOnly
        response.addHeader("Set-Cookie","addHeaderCookie1=Secure_HttpOnly; Path=/; Secure; HttpOnly");
        out.println(++count + ". addHeaderCookie1=Secure_HttpOnly; Path=/; Secure; HttpOnly");
        out.println("<br/>");

        // Set-Cookie: addCookie2=NotSecure_HttpOnly; Path=/; HttpOnly
        response.addHeader("set-cookie","addHeaderCookie2=NotSecure_HttpOnly; Path=/; HttpOnly");
        out.println(++count + ". addHeaderCookie2=NotSecure_HttpOnly; Path=/; HttpOnly");
        out.println("<br/>");

        // Set-Cookie: addCookie3=Secure_NotHttpOnly; Path=/; Secure;
        response.addHeader("set-cookie","addHeaderCookie3=Secure_NotHttpOnly; Path=/; Secure");
        out.println(++count + ". addHeaderCookie3=Secure_NotHttpOnly; Path=/; Secure;");
        out.println("<br/>");

        // Set-Cookie: addCookie4=NotSecure_NotHttpOnly; Path=/;
        response.addHeader("set-cookie","addHeaderCookie4=NotSecure_NotHttpOnly; Path=/;");
        out.println(++count + ". addHeaderCookie4=NotSecure_NotHttpOnly; Path=/;");
        out.println("<br/>");

        out.println("</body>");
        out.println("</html>");
    }
}
