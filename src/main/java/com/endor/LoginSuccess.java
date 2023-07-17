package com.endor;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/clothing-shop/LoginSuccess")
public class LoginSuccess extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = null;
		try {
			out = response.getWriter();
		} catch (Exception e) {
			e.printStackTrace();
		}

		HtmlUtil.printHtmlHeader(response);
		HtmlUtil.startBody(response);
		HtmlUtil.printClothingShopMenu(response);
		HtmlUtil.openTable(response);
		HtmlUtil.openRow(response);
		HtmlUtil.openCol(response);
		HtmlUtil.printCurrentTitle("Login Success Page", response);

		StringBuilder form = new StringBuilder();
		form.append("<br><br><form action=\"RecordServlet\" id=\"recordform\" >")
				.append("This URL is fetching records from employee_details table <br>")
				.append("--------------------------------------<br><br>")
				.append("<label> Select the DB Call option </label>")
				.append("<select name =\"dbcall\">")
				.append("<option disabled selected value> -- Select an Option -- </option>")
				.append("<option value = \"prepared_statement\"> prepared_statement</option>")
				.append("<option value = \"stored_procedure\" selected=\"selected\"> stored_procedure</option>")
				.append("</select>")
				.append("<br><br>")
				.append("<input type=\"submit\" value=\"Submit Request\">" + "</form>");
		out.println(form);

		// allow access only if session exists
		String username = null;
		Cookie[] cookies = request.getCookies();
		if(cookies != null) {
			for(Cookie cookie : cookies) {
				if(cookie.getName().equals("username")) username = cookie.getValue();
			}
		}

		out.println("<font color=red> Hi " + username + " your session id is: " + request.getSession().getId() + "</font>");

		out.println("<br/><br/>");
		out.print("<br><a href='logout'><button type=\"button\">Logout</button></a>");

		String retVal = "Succeeded";
		HtmlUtil.closeCol(response);
		HtmlUtil.openCol(response);
		out.println("<h2> Login  " + retVal + "</h2>");
		HtmlUtil.closeCol(response);
		HtmlUtil.closeRow(response);
		HtmlUtil.closeTable(response);
		out.println("</body>");
		out.println("</html>");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
