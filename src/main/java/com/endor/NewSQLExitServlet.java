package com.endor;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@WebServlet(name = "NewSQLExitServlet")
public class NewSQLExitServlet extends HttpServlet {
    static AtomicInteger totalNewDNAsInThisRun = new AtomicInteger(0);
    static AtomicInteger totalDNAsCreated =new AtomicInteger(0);
    static int finalDnaCount = 0;
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

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
        HtmlUtil.printCurrentTitle("SQL - Creates  new exit DNA per request", response);

        String form = "<form action=\"newsqlexit\" method=\"post\">" +
                "DNA count: <input type=\"text\" name=\"dnacount\">  <br><br>" +
                "First name: <input type=\"text\" name=\"first\"><br><br>" +
                "Last name: <input type=\"text\" name=\"name\"><br><br>" +
                "Password: <input type=\"text\" name=\"password\"><br><br><br>" +

//                "SQL query Statement: <input type=\"text\" name=\"SQLStatement\"> <-- Not used<br><br>" +
                "<br><br>" +
                "<input type=\"submit\" value=\"Submit\">" + "</form>";
        out.println(form);
        HtmlUtil.closeCol(response);
        HtmlUtil.openCol(response);

        String first = request.getParameter("first");
        String last = request.getParameter("name");
        String pass = request.getParameter("password");
        String dnaCount = request.getParameter("dnacount");
        String retVal = "Failed!";
         if (last != null && pass != null && last.length()>0 && pass.length() >0) {
             setDnaCont(dnaCount);
             if (getCustomersPreparedStatementExecuteNewExit(last, pass)) {
                 retVal = "Succeeded";
             }
             if (retVal.equalsIgnoreCase("Succeeded")) {
                 retVal = HttpURLConnectionExample.sendGET();
             }
             out.println("<h2> SQL execution " + retVal + "</h2>");
         } else{
             out.println("Enter last name and password");
         }
        HtmlUtil.closeCol(response);
        HtmlUtil.closeRow(response);
        HtmlUtil.closeTable(response);
        out.println("</body>");
        out.println("</html>");

    }

    private void setDnaCont(String dnaCount) {
        if (dnaCount != null && dnaCount.length() >0){
            try{
                int count  = Integer.parseInt(dnaCount);
                totalNewDNAsInThisRun.set(count);
                finalDnaCount = totalNewDNAsInThisRun.get();
            } catch (Exception e){}
        }
    }


    public boolean getCustomersPreparedStatementExecuteNewExit(String name, String pass) {
        Connection conn = connect();
        boolean hasResults = false;
        if (conn == null)
            return false;
        PreparedStatement stmt = null;
        try {
            String comment = " /* Random Comment:" + UUID.randomUUID() + " */";
            String query = "SELECT FIRST, LAST from CUSTOMERS WHERE LAST = ? AND PASSWORD = ?";
            int curVal  = totalDNAsCreated.get();
            if (curVal < finalDnaCount) {
                query += comment;
                totalDNAsCreated.incrementAndGet();
            } else {
                System.out.println("QUERY At Max DNA Value :" + query);
            }
            stmt = conn.prepareStatement(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        int output = 0;
        try {
            stmt.setString(1, name);
            stmt.setString(2, pass);
   //         System.out.println("PreparedStatement.execute being called with Last=" + name + " Password=" + pass);
            hasResults = stmt.execute();
            // Loop through the data and print all artist names
        } catch (Exception e) {
            System.out.println("Exception !");
            System.err.println(e.getMessage());
        } finally {
            try {
                // Close connection
                stmt.close();
                conn.close();
            } catch (SQLException e) {
                System.err.println(e.getMessage());
                System.out.println("Exception 2");
            }
        }
        return hasResults;
    }

    /** Shiva use the following java system properties instead of new connection function.
        -Dendor_connection_url="jdbc:postgresql://localhost:5432/sqlinject?sslmode=disable"
        -Dendor_db_user="postgres"
        -Dendor_db_password=""Psqlpsmo@1"
        -Dendor_db_type="Postgress"
     */
    private Connection connectpsql() {
        Connection conn = null;
        try {
            // Create database connection
            String dbURL = "jdbc:postgresql://localhost:5432/sqlinject?sslmode=disable";
            String user = "postgres";
            String password = "Psqlpsmo@1";
            conn = DriverManager.getConnection(dbURL, user, password);
            System.out.println("DB Connection established");
        } catch (Exception e) {
            System.err.println("ERROR: failed to connect postgres SQL.");
            e.printStackTrace();
            return null;
        }
        return conn;
    }


    /** Shiva use java system properties instead of new connection function.
     -Dendor_connection_url="jdbc:postgresql://localhost:5432/sqlinject?sslmode=disable"
     -Dendor_db_user="postgres"
     -Dendor_db_password=""Psqlpsmo@1"
     -Dendor_db_type="Postgress"
     */

    private Connection connect() {
        Connection conn = null;
        boolean retval = false;
        try {
            // Create database connection
     //       System.out.println("Oracle JDBC Driver Loaded");
     //       System.out.println("Oracle Connecting..");
//            String nameForConnect = "sys as sysdba";
//            String pass1 = "Psmo0601";
//            String url = "jdbc:oracle:thin:@10.0.22.108:1521:XE";
//            DriverManager.registerDriver(new OracleDriver());
//            conn = DriverManager.getConnection(url, nameForConnect, pass1);
            conn = DriverManager.getConnection(connectionUrl, dbUser, dbPassword);

        } catch (Exception e) {
            System.err.println("ERROR: failed to load Oracle JDBC driver.");
            e.printStackTrace();
            return null;
        }
        return conn;
    }
}
