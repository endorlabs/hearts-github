package com.endor;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.nio.CharBuffer;
import java.sql.*;
//import java.util.Base64;
import java.util.HashMap;


class SomeClass implements Serializable {
	 private static String cmd = "cat /etc/passwd";
	 private void readObject( ObjectInputStream stream )
	 throws Exception {
	 stream.defaultReadObject();
	 Runtime.getRuntime().exec( cmd );
	 }
}

// TODO rename to SQLServlet
@WebServlet(name = "Deserialize1")
@MultipartConfig(fileSizeThreshold=1024*1024*10, 	// 10 MB 
maxFileSize=1024*1024*50,      	// 50 MB
maxRequestSize=1024*1024*100) 

public class Deserialize1 extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      
    	ObjectInputStream ois = null;
    	try {
    	InputStream inStr = request.getPart("inputfile").getInputStream();
    	byte byteArray[] = new byte[inStr.available()];
    	int iBytesRead = inStr.read(byteArray);
    	    	
    	System.out.println("Numberof bytes read from uploaded file : " + iBytesRead);
    	ois = new ObjectInputStream(new ByteArrayInputStream(byteArray));
    	}
    	catch (Exception ex) {
    		ex.printStackTrace();
    	}
	    
	    boolean bflag = false;
	    //Read the object from the data stream, and convert it back to a String
	    try {
	    	System.out.println("Trying to call object.readObject");
			Object o = ois.readObject();
			System.out.println("Successfull called object.readObject \n Exiting the servlet.\n");
			bflag = true;
		} catch (ClassNotFoundException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			//doGet(request, response);
		}

	    //Print the result.
	    System.out.println("Done");
        PrintWriter out = null;
        try {
            out = response.getWriter();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
        	Thread.sleep(5000);
        }
        catch (Exception ex)
        {
        	ex.getStackTrace();
        }
        HtmlUtil.printHtmlHeader(response);
        HtmlUtil.startBody(response);
        HtmlUtil.printMenu(response);
        HtmlUtil.openTable(response);
        HtmlUtil.openRow(response);
        HtmlUtil.openCol(response);
        HtmlUtil.printCurrentTitle("Deserialzation", response);

        String form = "<form action=\"deserialize1\" method=\"post\" enctype=\"multipart/form-data\">" +
        				"<label for=\"inputfile\">Select a file: </label>"+
        				"<input type=\"file\" id=\"inputfile\" name=\"inputfile\"><br><br>"+
        				"<input type=\"submit\" value=\"Submit\">" + "</form>";
        out.println(form);
        String retVal = HttpURLConnectionExample.sendGET();
        //if (bflag)
        out.println("Submitted");
        out.println("</body>");
        out.println("</html>");
        
	    ois.close();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
    {
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
        HtmlUtil.printCurrentTitle("Deserialzation", response);

        SomeClass s1 = new SomeClass();
        FileOutputStream fos = new FileOutputStream("object.ser");
	    ObjectOutputStream os = new ObjectOutputStream(fos);
	    os.writeObject(s1);
	    os.close();
	    
        String form = "<form action=\"deserialize1\" method=\"post\" enctype=\"multipart/form-data\">" +
        				"<label for=\"inputfile\">Select a file: </label>"+
        				"<input type=\"file\" id=\"inputfile\" name=\"inputfile\"><br><br>"+
        				"<input type=\"submit\" value=\"Submit\">" + "</form>";
        out.println(form);
        out.println("</body>");
        out.println("</html>");
        

    }

}
