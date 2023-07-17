package com.endor;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(urlPatterns={"/GetInputStreamTest"})
public class GetInputStreamTest extends HttpServlet {

	boolean isPost = false;
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	isPost = true;
    	System.out.println("In Post request method");
        doGet(request, response);
    }

    private static final String POST_URL_GET_PARAMETER = "http://localhost:8080/endor-webapp/GetInputStreamInnerTest";

	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = null;
        System.out.println("request.getContentType() = " + request.getContentType());
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

        StringBuilder form = new StringBuilder();
        form.append("<br><br><form action=\"GetInputStreamTest\" method=\"post\" id=\"loginform\" >")
                .append("last: <input type=\"text\" name=\"last\" id=\"last\">")
                .append("<br><br>")
                .append("pass: <input type=\"text\" name=\"pass\" id=\"pass\">")
                .append("<br><br>")
                .append("<label> Select the multileg option </label>")
                .append("<select name =\"multileg\">")
                .append("<option disabled selected value> -- Select an Option -- </option>")
                .append("<option value = \"prepared_statement\"> prepared_statement</option>")
                .append("<option value = \"stored_procedure\"> stored_procedure</option>")
                .append("</select>")
                .append("<br><br>")

                .append("<input type=\"submit\" value=\"Submit\">" + "</form>");
        out.println(form);

        int len = request.getContentLength();
        System.out.println("request.getContentLength() = " + request.getContentLength());
        if (len > 0) {
        	isPost = false;
        	System.out.println("In Post request condition");
            byte[] input = new byte[len];
            //System.out.println("length of input" + len);

            ServletInputStream sin = request.getInputStream();
            int c, count = 0 ;
            while ((c = sin.read(input, count, input.length-count)) != -1) {
                count +=c;
            }
            sin.close();

            String inString = new String(input);
            int index = inString.indexOf("&");
            String lastvalue = inString.substring(0,index);
            String restvalue = inString.substring(index+1);
            index = restvalue.indexOf("&");
            String passvalue = restvalue.substring(0,index);
            String multileg_value = restvalue.substring(index+1);

            index = lastvalue.indexOf("=");
            String last = lastvalue.substring(index+1);

            index = passvalue.indexOf("=");
            String pass = passvalue.substring(index+1);

            index = multileg_value.indexOf("=");
            String multileg = multileg_value.substring(index+1);

            int responseCode = HttpURLConnectionExample.sendPOSTwithParameter(last, pass, multileg, POST_URL_GET_PARAMETER);

            out.println("<h2> Got response code for GetInputStreamInnerTest " + responseCode + "</h2>");

            System.out.println("last=" + last);
            System.out.println("pass=" + pass);
            System.out.println("multileg=" + multileg);
        }

        HtmlUtil.closeCol(response);
        HtmlUtil.openCol(response);      
        HtmlUtil.closeCol(response);
        HtmlUtil.closeRow(response);
        HtmlUtil.closeTable(response);
        String scriptTag = "<script type=\"text/javascript\" src=\"jsfile.js\"></script>";
        out.println(scriptTag);
        out.println("</body>");
        out.println("</html>");
    }
}

