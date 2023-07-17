package com.endor;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@WebServlet("/clothing-shop/RecordServlet")
public class RecordServlet extends HttpServlet {
    static String connectionUrl = "";
    static String dbUser = "";
    static String dbPassword = "";
    static String dbType = "";
    static String DB_TYPE_ORACLE = "Oracle";

    @Override
    public void init() throws ServletException {
        super.init();
        connectionUrl =System.getProperty("endor_connection_url", "jdbc:oracle:thin:@10.0.22.108:1521:XE");
        dbUser =System.getProperty("endor_db_user", "sys as sysdba");
        dbPassword =System.getProperty("endor_db_password", "Psmo0601");
        dbType =System.getProperty("endor_db_type", DB_TYPE_ORACLE);
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

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
        HtmlUtil.printClothingShopMenu(response);
        HtmlUtil.openTable(response);
        HtmlUtil.openRow(response);
        HtmlUtil.openCol(response);
        HtmlUtil.printCurrentTitle("Record Fetching Page", response);
        HtmlUtil.closeCol(response);

        String retVal = "Failed!";
        
        // PreparedStatement execution with input parameter
        String username = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("username")) username = cookie.getValue();
            }
        }


		// Fetch records
		String dbcall = request.getParameter("dbcall");
		if (dbcall.equalsIgnoreCase("prepared_statement")) {
			executeSQLHelper(username,out);
		} else {
			getCustomersStoredProc(username,out);
		}

		HttpSession session = request.getSession(false);
        out.println("<br/><br/>");
        out.println("<font color=red> Hi " + username + " your session id is: " + request.getSession().getId() + "</font>");
        out.println("<br/><br/>");
        out.print("<br><a href='logout'><button type=\"button\">Logout</button></a>");
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

    public boolean executeSQLHelper(String username,java.io.PrintWriter out) {
        boolean retVal = false;
        Connection conn = connect();
        if (conn == null)
            return false;

        try {
            StringBuffer sbuf = new StringBuffer();
            String query = new String();
            if(username.contains("admin")) {
                query = "select EID,ENAME,ADDRESS,SALARY,NOMINEE from EMPLOYEE_DETAILS";
                PreparedStatement stmt = conn.prepareStatement(query);
                ResultSet rs = stmt.executeQuery();
                try {
					show_resultset(rs,out);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                // Clean up
                stmt.close();
                rs.close();
            }
            else {
                query = "select EID,ENAME,ADDRESS from EMPLOYEE_DETAILS";
                PreparedStatement stmt = conn.prepareStatement(query);
                ResultSet rs = stmt.executeQuery();
                // Loop through the data and print all artist names
                try {
					show_resultset(rs,out);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                // Clean up
                stmt.close();
                rs.close();
            }
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
    
    private int show_resultset(java.sql.ResultSet rs, java.io.PrintWriter out)
    	    throws Exception {
        int rowCount = 0;

        out.println("<P ALIGN='center'><TABLE BORDER=1>");
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();
        // table header
        out.println("<TR>");
        for (int i = 0; i < columnCount; i++) {
            out.println("<TH>" + rsmd.getColumnLabel(i + 1) + "</TH>");
        }
        out.println("</TR>");
        // the data
        while (rs.next()) {
            rowCount++;
            out.println("<TR>");
            for (int i = 0; i < columnCount; i++) {
                out.println("<TD>" + rs.getString(i + 1) + "</TD>");
            }
            out.println("</TR>");
        }
        out.println("</TABLE></P>");
        return rowCount;
    }
    
    private int show_resultset(java.sql.ResultSet rs, java.io.PrintWriter out, String username)
    	    throws Exception {
        int rowCount = 0;

        out.println("<P ALIGN='center'><TABLE BORDER=1>");
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();
        if(username.contains("user")) {
            columnCount -= 2;
        }
        // table header
        out.println("<TR>");
        for (int i = 0; i < columnCount; i++) {
            out.println("<TH>" + rsmd.getColumnLabel(i + 1) + "</TH>");
        }
        out.println("</TR>");
        // the data
        while (rs.next()) {
            rowCount++;
            out.println("<TR>");
            for (int i = 0; i < columnCount; i++) {
                out.println("<TD>" + rs.getString(i + 1) + "</TD>");
            }
            out.println("</TR>");
        }
        out.println("</TABLE></P>");
        return rowCount;
    }


    public boolean getCustomersStoredProc(String username,java.io.PrintWriter out) {
        Connection conn = connect();
        if (conn == null)
            return false;

        int output = 0;
        try {
            String query;
            CallableStatement cs = null;

            query = "{ ? = call read_employee_details(?)}";
            //query = "{call employee_details(?)}";

            cs = conn.prepareCall(query);
            //cs.setString(1, username);

            cs.setString(2, username);
            cs.registerOutParameter(1, Types.REF_CURSOR);

            System.out.println("Stored Proc being called");
            System.out.println(query);

            conn.setAutoCommit(false);
            cs.execute();

            ResultSet rs = (ResultSet) cs.getObject(1);
            try {
                show_resultset(rs,out);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            // Clean up
            rs.close();
            cs.close();
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
        return output >= 0;
    }
}
