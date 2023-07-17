package com.endor;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/hsts/securepage")
public class SecurePage extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SecurePage() {
        super();
        // TODO Auto-generated constructor stub
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = null;
        try {
            out = response.getWriter();
        } catch (Exception e) {
            e.printStackTrace();
        }

        HtmlUtil.printHtmlHeader(response);
        HtmlUtil.startBody(response);
        HtmlUtil.openTable(response);
        HtmlUtil.openRow(response);
        HtmlUtil.openCol(response);
        out.println("<h1>" + "Secure Page Test" + "</h1>");
        out.println("<h4>" + "This is a secure page" + "</h4>");
        out.println("</body>");
        out.println("</html>");
    }
}
