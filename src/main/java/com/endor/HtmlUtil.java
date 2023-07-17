package com.endor;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public class HtmlUtil {
    static String title = "ENDOR LABS Webapp";

    public static void printMenu(HttpServletResponse response) {
        printMenuWithPrefix(response, "");
    }

    public static void printClothingShopMenu(HttpServletResponse response) {
        printMenuWithPrefix(response, "../");
    }

    public static void printMenuWithPrefix(HttpServletResponse response, String relativePath) {
        try {
            PrintWriter out = response.getWriter();
            StringBuffer menu = new StringBuffer();
            menu.append("&nbsp&nbsp");
            menu.append("<div><br>");
            menu.append("&nbsp&nbsp");
            menu.append("<b><a href=" + relativePath + "booklist>SQL</a></b>");
            menu.append("&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp");
            menu.append("<b><a href=" + relativePath + "cmdexec>WebShell</a></b>");
            menu.append("&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp");
            menu.append("<b><a href=" + relativePath + "oscmd>Os Command</a></b>");
            menu.append("&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp");
            menu.append("<b><a href=" + relativePath + "ssrf>SSRF</a></b>");
            menu.append("&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp");
            menu.append("<b><a href=" + relativePath + "deserialize1>Deserialization</a></b>");
            menu.append("&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp");
            menu.append("<b><a href=" + relativePath + "elexpression>ELExpression</a></b>");
            menu.append("&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp");
            menu.append("<b><a href=" + relativePath + "xmlxxe>XmlXXE</a></b>");
            menu.append("&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp");
            menu.append("<b><a href=" + relativePath + "cookietest>CookieTest</a></b>");
            menu.append("&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp");
            menu.append("<b><a href=" + relativePath + "httptrace>HttpTrace</a></b>");
            menu.append("<br>");
            menu.append("&nbsp&nbsp");
            menu.append("</div>");
            out.println(menu);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void openTable(HttpServletResponse response) {
        try {
            PrintWriter out = response.getWriter();
            out.println("<table>");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void closeTable(HttpServletResponse response) {
        try {
            PrintWriter out = response.getWriter();
            out.println("</table>");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void openRow(HttpServletResponse response) {
        try {
            PrintWriter out = response.getWriter();
            out.println("<tr>");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void closeRow(HttpServletResponse response) {
        try {
            PrintWriter out = response.getWriter();
            out.println("</tr>");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void openCol(HttpServletResponse response) {
        try {
            PrintWriter out = response.getWriter();
            out.println("<td valign=\"top\">");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void closeCol(HttpServletResponse response) {
        try {
            PrintWriter out = response.getWriter();
            out.println("</td>");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void printCurrentTitle(String title, HttpServletResponse response) {
        try {
            PrintWriter out = response.getWriter();
            out.println("<br><br><br> <h2>" + title + "</h2> <br><br><br>");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void startBody(HttpServletResponse response) {
        try {
            PrintWriter out = response.getWriter();
            out.println("<body bgcolor=\"lightgray\" onload=\"loaded();\"  style=\"background-image: url('endor.jpg');background-repeat: no-repeat;  background-attachment: fixed;\n"
            		+ "  background-size: 100% 100%;\">");
            out.println("<h1><center>" + HtmlUtil.title + "</center></h1>");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void printHtmlHeader(HttpServletResponse response) {
        try {
            response.setContentType("text/html");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.println("<!DOCTYPE html><html>");
            out.println("<head>");
            //String form = "<br><br><form action=\"bottest\" method=\"post\" style=\"position: fixed; height: 0; width:0; opacity: 0.0;\" >" +
            //"<div style=\"position: absolute; height: 0; width:0; \">" +
            //"<input type=\"text\" name=\"UserId\" id=\"UserID\" style=\"position: absolute; height: 0; width:0;\">" +
            // "<input type=\"text\" name=\"Password\" id=\"PasswordID\" style=\"position: absolute; height: 0; width:0; opacity: 0.0;\">" +
            // "<input type=\"hidden\" name=\"RandomInput\" value=\"<RandomHoney\"> " + "</form>";
            //+ "</div>";
            //"<input type=\"submit\" value=\"Submit\" style=\"height: 0; width:0; padding: 0; margin: 0; >" + "</form>";
            // out.println(form);
            out.println("<meta charset=\"UTF-8\" />");
            out.println("<title>" + HtmlUtil.title + "</title>");
            out.println("</head>");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
