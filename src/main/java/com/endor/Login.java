package com.endor;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/clothing-shop/login")
public class Login extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public Login() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

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
        HtmlUtil.printCurrentTitle("Login Page", response);

        String form = "<form action=\"login\">" +
                "--------------------------------------<br><br>"+
                "User ID: <input type=\"text\" name=\"username\">  <br><br>" +
                "Password: <input type=\"text\" name=\"password\"><br><br>" +

                "<input type=\"submit\" value=\"Submit\">" + "</form>";
        out.println(form);
        String retVal = "";
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        HashMap<String, String> hash_map = new HashMap<>();
        hash_map.put("app-admin1", "password");
        hash_map.put("app-admin2", "password");
        hash_map.put("app-admin3", "password");
        hash_map.put("app-admin4", "password");
        hash_map.put("app-admin5", "password");
        hash_map.put("app-admin6", "password");
        hash_map.put("app-user1", "password");
        hash_map.put("app-user2", "password");
        hash_map.put("app-user3", "password");
        hash_map.put("app-user4", "password");
        hash_map.put("app-user5", "password");
        hash_map.put("app-user6", "password");

        if(username.isEmpty() || password.isEmpty()) {
            out.println("<font color=blue>Enter username and password.</font>");
            return;
        }

        if(hash_map.containsKey(username) && hash_map.get(username).equals(password)){
            Cookie loginCookie = new Cookie("username",username);
            // setting cookie to expiry in 30 mins
            loginCookie.setMaxAge(30*60);
            response.addCookie(loginCookie);
            out.println("<font color=red> User Name or Password are Correct...Redirecting...</font>");

            response.sendRedirect("LoginSuccess");
            retVal = "Succeeded";
        } else {
            retVal = "Failed";
            out.println("<font color=red>Either user name or password is wrong.</font>");
        }

        HtmlUtil.closeCol(response);
        HtmlUtil.openCol(response);
        out.println("<h2> Login Process " + retVal + "</h2>");
        HtmlUtil.closeCol(response);
        HtmlUtil.closeRow(response);
        HtmlUtil.closeTable(response);
        out.println("</body>");
        out.println("</html>");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        doGet(request, response);
    }
}
