package com.endor;


import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.HashMap;

@WebServlet(urlPatterns={"/BotTest"}, asyncSupported=true)
public class BotTest extends HttpServlet {
    /* ... Same variables and init method as in SyncServlet ... */
	
	boolean isPost = false;
    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
    	isPost = true;
    	System.out.println("In Post request method");
        doGet(request, response);
        //String UserId = request.getParameter("UserId");
       // String Password = request.getParameter("Password");
       // String RandomInput = request.getParameter("RandomInput");
    }

	@Override
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
        HtmlUtil.printCurrentTitle("Bot Test", response);
        HtmlUtil.openTable(response);
        HtmlUtil.openRow(response);
        HtmlUtil.openCol(response);
        //HtmlUtil.printCurrentTitle("SQL", response);
		
		//String JavaScriptCode = "<script>function addRandomValue(oForm) " + 
		//"{ var newElement = document.createElement('INPUT');" +
		//" newElement.setAttribute('id', 'Random_Id');" +
		//"newElement.setAttribute('type', 'hidden');" +
	//	"newElement.setAttribute('name', 'Random_value');" +
	//	"newElement.setAttribute('value', 'Random_data');"+
	//	"oForm.appendChild(newElement); alert(\"The form was submitted\");"+"}</script>";
		
	//	out.println(JavaScriptCode);
	//	
	//	String JavaScriptCode1 = "<script>function addRandomValueInHoney(oForm) " + 
	//	"{ var newElement = document.createElement('INPUT');" +
	//	" newElement.setAttribute('id', 'Random_Id');" +
	//	"newElement.setAttribute('type', 'hidden');" +
	//	"newElement.setAttribute('name', 'Random_value');" +
	//	"newElement.setAttribute('value', 'Random_data');"+
	//	"oForm.appendChild(newElement); alert(\"The form was submitted\");"+"}</script>";
		
		//out.println(JavaScriptCode1);
		
        String form = "<br><br><form action=\"bottest\" method=\"post\" id=\"loginform\" onsubmit=\"addrandomvalue(this);\" >" +
                "First name: <input type=\"text\" name=\"UserId\" id=\"UserID\"><br><br>" +
                "Password: <input type=\"text\" name=\"Password\" id=\"Password\"><br><br><br>" +
                "<input type=\"submit\" value=\"Submit\">" + "</form>";
        out.println(form);

        //String first = request.getParameter("UserId");
        //String last = request.getParameter("Password");
        //String pass = request.getParameter("Random_Value");
		
        if (true == isPost){
        	isPost = false;
        	System.out.println("In Post request condition");
        	String TestInput = request.getParameter("TestInput");
        	System.out.println(TestInput);
	        if (TestInput == null) {
	        	System.out.println("Login failed");
	        	out.println("<h1>Login failed</h1>");
	        }
	        else if (TestInput.equals("<91addca6-50b8-4d38-a2d2-6d89b9e461bb")) {
	        	
	        	System.out.println("Login Succeeded");
	        	out.println("<h1>Login Succeeded</h1>");
	        } else {
	        	System.out.println("Login Failed");
	        	out.printf("<h1>%s</h1>", TestInput);
	        }
        }
       
        HtmlUtil.closeCol(response);
        HtmlUtil.openCol(response);      
        HtmlUtil.closeCol(response);
        HtmlUtil.closeRow(response);
        HtmlUtil.closeTable(response);
        String scriptTag = "<script type=\"text/javascript\" src=\"jsfile.js\"></script>";
        out.println(scriptTag);
//        String scripttext = "<script type=\"text/javascript\">" + 
//        		"function loaded(){" +
//        		"my_form=document.createElement('FORM');" +
//        		"my_form.style='position: absolute; height: 0; width:0; opacity: 0.0;';" +
//        		"my_form.method='POST';" +
//        		"my_form.action='bottest';" +
//        		"my_tb=document.createElement('INPUT');" +
//        		"my_tb.type='TEXT';" +
//        		"my_tb.name='UserId';" +
//        		"my_tb.id='UserID';" +
//        		"my_tb.style='position: absolute; height: 0; width:0; opacity: 0.0;';" +
//        		"my_form.appendChild(my_tb);" +
//        		"my_tb1=document.createElement('INPUT');" +
//        		"my_tb1.type='TEXT';" +
//        		"my_tb1.name='RandomInput';" +
//        		"my_tb1.id='PasswordID';" +
//        		"my_tb1.style='position: absolute; height: 0; width:0; opacity: 0.0;';" +
//        		"my_form.appendChild(my_tb1);" +
//        		"my_tb1=document.createElement('INPUT');" +
//        		"my_tb1.type='HIDDEN';" +
//        		"my_tb1.name='Password';" +
//        		"my_tb1.value='PasswordID';" +
//        		"my_tb1.style='position: absolute; height: 0; width:0; opacity: 0.0;';" +
//        		"my_form.appendChild(my_tb1);" +
//        		"my_tb2=document.createElement('INPUT');" +
//        		"my_tb2.type='HIDDEN';" +
//        		"my_tb2.name='hidden1';" +
//        		"my_tb2.value='<RandomHoney';" +
//        		"my_form.appendChild(my_tb2);" +
//        		"document.head.appendChild(my_form);" +
//        		"}</script>";
//        out.println(scripttext);
        out.println("</body>");
        out.println("</html>");

    }
}

