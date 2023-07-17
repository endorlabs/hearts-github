package com.endor;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Servlet implementation class ExtraServlet
 */
@WebServlet("/ExtraServlet")
public class ExtraServlet extends HttpServlet {
	//private static final long serialVersionUID = 1L;
    static String connectionUrl = "";
    static String dbUser = "";
    static String dbPassword = "";
    static String dbType = "";
    static String DB_TYPE_ORACLE = "Oracle";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    
    @Override
    public void init() throws ServletException {
        super.init();
        connectionUrl =System.getProperty("endor_connection_url", "jdbc:oracle:thin:@10.0.22.108:1521:XE");
        dbUser =System.getProperty("endor_db_user", "sys as sysdba");
        dbPassword =System.getProperty("endor_db_password", "Psmo0601");
        dbType =System.getProperty("endor_db_type", DB_TYPE_ORACLE);

    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
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
        HtmlUtil.openTable(response);
        HtmlUtil.openRow(response);
        HtmlUtil.openCol(response);
        HtmlUtil.printCurrentTitle("SQL", response);
        String form = "<form action=\"booklist\">" +
                "This URL is testing multi leg features <br>" +
        		"--------------------------------------<br><br>"+ "</form>";
        out.println(form);
        HtmlUtil.closeCol(response);

        String retVal = "Failed!";
        //PreparedStatement execution with input parameter
        int len = request.getContentLength();
        if (len > 0) {
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
            
            System.out.println("\nlast=" +last+ "\npass=" +pass+ "\nmultileg=" +multileg);
                        
            if(multileg.equalsIgnoreCase("prepared_statement") && executeSQLHelper(last, pass)) {
            	retVal = "Succeeded";
            }
            
            else if(multileg.equalsIgnoreCase("stored_procedure") && getCustomersStoredProc(last, pass)) {
            	retVal = "Succeeded";
            }
            
            HtmlUtil.openCol(response);
            out.println("<h2> SQL execution for Input Parameter " + retVal + "</h2>");
            HtmlUtil.closeCol(response);
        }
        
        
        //PreparedStatement with hard-coded value
        /*
        retVal = "Succeeded";
        executeSQLHelper("prakash'--", "psmo");
        */
        
        if (retVal.equalsIgnoreCase("Succeeded")){
           retVal = HttpURLConnectionExample.sendGET();
        }
        
        /*
        
        HtmlUtil.openCol(response);
        out.println("<h2> SQL execution for internal values " + retVal + "</h2>");
        HtmlUtil.closeCol(response);
        */
        
        HtmlUtil.closeRow(response);
        HtmlUtil.closeTable(response);
        out.println("</body>");
        out.println("</html>");
    }
	
    private Connection connect() {
        Connection conn = null;
        try {
            // Create database connection
            conn = DriverManager.getConnection(connectionUrl, dbUser, dbPassword);
            System.out.println("DB Connection established");
        } catch (Exception e) {
            System.err.println("ERROR: failed to connect DB");
            e.printStackTrace();
            return null;
        }
        return conn;
    }
    
	public boolean executeSQLHelper(String name, String pass) {
        boolean retVal = false;
        Connection conn = connect();
        if (conn == null)
            return false;
        
        try {
            StringBuffer sbuf = new StringBuffer();
            String query = new String();
            query = "select FIRST, LAST from CUSTOMERS WHERE LAST=\'" + name + "\' AND PASSWORD= \'" + pass + "\'";
            System.out.println("Multileg PreparedStatementQUERY:" + query);
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            // Loop through the data and print all artist names
            while (rs.next()) {
            	sbuf.append("Customer Name: " + rs.getString("FIRST") + " " + rs.getString("LAST"));
                System.out.println("Customer Name: " + rs.getString("FIRST") + " " + rs.getString("LAST"));
                sbuf.append("<br>");
                retVal = sbuf.toString().length() > 2;
            }
            // Clean up
            stmt.close();
            rs.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                // Close connection
                conn.close();
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
        return retVal;
    }
	
	public boolean getCustomersStoredProc(String name, String pass) {
        Connection conn = connect();
        if (conn == null)
            return false;
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        int output = 0;
        boolean hasResults = false;
        try {
            // close the statement as its not a callable statement
            stmt.close();
            String query;
            CallableStatement c = null;
            
            query = "{call verifyuser(?,?,?)}";
            c = conn.prepareCall(query);
            c.setString(1, name);
            c.setString(2, pass);
            c.registerOutParameter(3, Types.INTEGER);
            System.out.println("Multihub DB stored Proc being called");
            System.out.println(query);
            c.execute();
            output = c.getInt(3);
            System.out.println("Customer Count: " + output);
            
            //c.executeQuery();
            // Loop through the data and print all artist names
            // Clean up
            c.close();
        } catch (Exception e) {
            System.out.println("Exception !");
            System.err.println(e.getMessage());
        } finally {
            try {
                // Close connection
                conn.close();
            } catch (SQLException e) {
                System.err.println(e.getMessage());
                System.out.println("Exception 2");
            }
        }
        return output > 0;
    }

}
