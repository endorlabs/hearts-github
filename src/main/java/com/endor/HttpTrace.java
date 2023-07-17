package com.endor;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class HttpTrace
 */
@WebServlet(name = "HttpTrace")
public class HttpTrace extends HttpServlet {
       

    public HttpTrace() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

        PrintWriter out = null;
        try {
            out = response.getWriter();
        } catch (Exception e) {
            e.printStackTrace();
        }

        HtmlUtil.printHtmlHeader(response);
        HtmlUtil.startBody(response);
        HtmlUtil.printMenu(response);
        HtmlUtil.openTable(response);
        HtmlUtil.openRow(response);
        HtmlUtil.openCol(response);
        HtmlUtil.printCurrentTitle("Http Tracing", response);

        String form = "<form action=\"httptrace\">" +
        		"Http Tracing<br>" +
                "--------------------------------------<br><br>"+
                "<input type=\"radio\" name=\"enabled\" value=\"tracing\">: Enable Http Tracing<br>" +
                "<input type=\"submit\" value=\"Submit\">" + "</form>";
        out.println(form);
        
        String sqltype = request.getParameter("enabled");
        String retVal = "Failed";
        if(!sqltype.isEmpty()) {
        	retVal = "Succeeded";
        }
        
        
        HtmlUtil.closeCol(response);
        HtmlUtil.openCol(response);
        if (retVal.equalsIgnoreCase("Succeeded")){
            retVal = HttpURLConnectionExample.sendTRACE();
         }
        out.println("<h2> Http Tracing Successful" + "</h2>");
        HtmlUtil.closeCol(response);
        HtmlUtil.closeRow(response);
        HtmlUtil.closeTable(response);
        out.println("</body>");
        out.println("</html>");
        
        //doTrace(request,response);
        
		//response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	protected void doTrace(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("Success");
	}

}
