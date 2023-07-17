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

@WebServlet(urlPatterns={"/asyncservlet"}, asyncSupported=true)
public class AsyncServlet extends HttpServlet {
    /* ... Same variables and init method as in SyncServlet ... */

    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        doGet(request, response);
    }


    @Override
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) {
        response.setContentType("text/html;charset=UTF-8");
        //       acontext.dispatch("/booklist");

        final AsyncContext acontext = request.startAsync();
        acontext.start(new Runnable() {
            public void run() {
                HttpServletRequest request1 = (HttpServletRequest) acontext.getRequest();
                HttpServletResponse response1 = (HttpServletResponse) acontext.getResponse();
                /* ... print to the response ... */
                try {
                    doGetAsync(request1, response1);
                } catch (ServletException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                acontext.complete();
            }
        });
    }


    protected void doGetAsync(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
                "First name: <input type=\"text\" name=\"first\">  -- ( For SQL with Update)  <br><br>" +
                "Last name: <input type=\"text\" name=\"name\"><br><br>" +
                "Password: <input type=\"text\" name=\"password\"><br><br><br>" +
                "<input type=\"radio\" name=\"sqltype\" value=\"storedproc\">: Stored Procedure<br>" +
                "<input type=\"radio\" name=\"sqltype\" value=\"executeUpdateSQLColNames\">: Statement.executeUpdate(SQL, column_names[]) <br>" +
                "<input type=\"radio\" name=\"sqltype\" value=\"executeQuerySQL\">: Statement.executeQuery(SQL)<br>" +
                "<input type=\"radio\" name=\"sqltype\" value=\"executeSQL\">: Statement.execute(SQL) <br>" +
                "<input type=\"radio\" name=\"sqltype\" value=\"executeUpdateSQL\">: Statement.executeUpdate(SQL) <br>" +
                "<input type=\"radio\" name=\"sqltype\" value=\"executeSQLColIndex\">: Statement.execute(SQL, column_indexes[]) <br>" +
                "<input type=\"radio\" name=\"sqltype\" value=\"executeUpdateSQLColIndex\">: Statement.executeUpdate(SQL, column_indexes[]) <br>" +
                "<input type=\"radio\" name=\"sqltype\" value=\"executeSQLAutogenkeys\">: Statement.execute(SQL, auto_gen_keys) <br>" +
                "<input type=\"radio\" name=\"sqltype\" value=\"executeUpdateSQLAutogenkeys\">: Statement.executeUpdate(SQL, auto_gen_keys) <br>" +
                "<input type=\"radio\" name=\"sqltype\" value=\"preparedStatement.execute\">: PreparedStatement.execute() <br>" +
                "<input type=\"radio\" name=\"sqltype\" value=\"preparedStatement.executeQuery\">: PreparedStatement.executeQuery() <br>" +
                "<input type=\"radio\" name=\"sqltype\" value=\"preparedStatement.executeUpdate\">: PreparedStatement.executeUpdate() <br>" +
                "<input type=\"radio\" name=\"sqltype\" value=\"storedproc.executeQuery\">: storedproc.executeQuery() <br>" +
                "<br><br>" +

                "<input type=\"submit\" value=\"Submit\">" + "</form>";
        out.println(form);

        if (createRecord(request, out)) {
            return;
        }
        String first = request.getParameter("first");
        String last = request.getParameter("name");
        String pass = request.getParameter("password");

        HashMap<String, Integer> sqltypeMap = new HashMap<String, Integer>() {{
            put("storedproc", 0);
            put("executeUpdateSQLColNames", 1);
            put("executeQuerySQL", 2);
            put("executeSQL", 3);
            put("executeUpdateSQL", 4);
            put("executeSQLColIndex", 5);
            put("executeUpdateSQLColIndex", 6);
            put("executeSQLAutogenkeys", 7);
            put("executeUpdateSQLAutogenkeys", 8);
            put("preparedStatement.execute", 9);
            put("preparedStatement.executeQuery", 10);
            put("preparedStatement.executeUpdate", 11);
            put("storedproc.executeQuery", 12);



        }};

        String sqltypeStr = request.getParameter("sqltype");
        int sqltype = sqltypeMap.get(sqltypeStr);
        String retVal = "Failed!";

        switch (sqltype) {
            case 0: //storedproc
                if (getCustomersStoredProc(1, last, pass)) {
                    retVal = "Succeeded";
                }
                break;
            case 1: // executeUpdateSQLColNames
                if (getCustomersUpdateColName(first, last, pass)) {
                    retVal = "Succeeded";
                }
                break;
            case 2: //executeQuerySQL
                if (executeQuerySQL(last, pass)) {
                    retVal = "Succeeded";
                }
                break;
            case 3: //executeSQL
                if (executeSQL(last, pass)) {

                    retVal = "Succeeded";
                }
                break;
            case 4: //executeUpdateSQL
                if (executeUpdateSQL(last, pass)) {
                    retVal = "Succeeded";
                }
                break;
            case 5: //executeSQLColIndex
                if (executeSQLWithColIndex("execute", first, last, pass)) {
                    retVal = "Succeeded";
                }
                break;
            case 6: //executeUpdateSQLColIndex
                if (executeSQLWithColIndex("executeUpdate", first, last, pass)) {
                    retVal = "Succeeded";
                }
                break;
            case 7: //executeSQLAutogenkeys
                if (executeSQLWithAutogenkeys("execute", first, last, pass)) {
                    retVal = "Succeeded";
                }
                break;
            case 8: //executeUpdateSQLAutogenkeys
                if (executeSQLWithAutogenkeys("executeUpdate", first, last, pass)) {
                    retVal = "Succeeded";
                }
                break;
            case 9: //preparedStatement.execute
                if (getCustomersPreparedStatement( "execute", last, pass)) {
                    retVal = "Succeeded";
                }
                break;
            case 10: //preparedStatement.executeQuery
                if (getCustomersPreparedStatement( "executeQuery", last, pass)) {
                    retVal = "Succeeded";
                }
                break;
            case 11: //preparedStatement.executeUpdate
                if (getCustomersPreparedStatement( "executeUpdate", first, last)) {
                    retVal = "Succeeded";
                }
                break;
            case 12: //storedproc executeQuery
                if (getCustomersStoredProc(2, last, pass)) {
                    retVal = "Succeeded";
                }
                break;
            default:
                System.out.println("SQL Type not found");
        }
        HtmlUtil.closeCol(response);
        HtmlUtil.openCol(response);
        if (retVal.equalsIgnoreCase("Succeeded")){
            retVal = HttpURLConnectionExample.sendGET();
        }
        out.println("<h2> SQL execution " + retVal + "</h2>");
        HtmlUtil.closeCol(response);
        HtmlUtil.closeRow(response);
        HtmlUtil.closeTable(response);
        out.println("</body>");
        out.println("</html>");

    }

    private boolean createRecord(HttpServletRequest request, PrintWriter out) {
        String fullName = request.getParameter("add");
        if (fullName != null) {
            String[] firstLast = fullName.split(" ");
            if (firstLast.length == 3) {
                insertCustomers(firstLast[0], firstLast[1], firstLast[2]);
                out.println("Added " + fullName);
                return true;
            }
        }
        return false;
    }

    private boolean getCustomersUpdateColName(String first, String last, String pass) {
        StringBuffer sbuf = new StringBuffer();
        Connection conn = connect();
        if (conn == null)
            return false;
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            String[] cols = {"FIRST", "LAST"};
            String query = String.format("UPDATE CUSTOMERS SET FIRST = '%s' WHERE LAST = '%s' AND PASSWORD = '%s'", first, last, pass);
            System.out.println("QUERY :" + query);
            int ret = stmt.executeUpdate(query, cols);
            // Clean up
            stmt.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println(e.getMessage());
                return false;
            }
        }
        return true;
    }

    private boolean executeSQLWithColIndex(String methodName, String first, String last, String pass) {
        Connection conn = connect();
        if (conn == null)
            return false;
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            int[] cols = {1, 2};
            String query = String.format("UPDATE CUSTOMERS SET FIRST = '%s' WHERE LAST = '%s' AND PASSWORD = '%s'", first, last, pass);
            System.out.println("QUERY :" + query);
            if (methodName.equalsIgnoreCase("execute")) {
                boolean ret = stmt.execute(query, cols);
            } else if (methodName.equalsIgnoreCase("executeUpdate")) {
                int ret = stmt.executeUpdate(query, cols);
            } else {
                System.out.println("Invalid SQL method!");
            }
            // Clean up
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        } finally {
            try {
                stmt.close();
                conn.close();
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
        return true;
    }

    private boolean executeSQLWithAutogenkeys(String methodName, String first, String last, String pass) {
        Connection conn = connect();
        if (conn == null)
            return false;
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            int autogenkeys = Statement.RETURN_GENERATED_KEYS;
            String query = String.format("UPDATE CUSTOMERS SET FIRST = '%s' WHERE LAST = '%s' AND PASSWORD = '%s'", first, last, pass);
            System.out.println("QUERY :" + query);
            if (methodName.equalsIgnoreCase("execute")) {
                boolean ret = stmt.execute(query, autogenkeys);
            } else if (methodName.equalsIgnoreCase("executeUpdate")) {
                int ret = stmt.executeUpdate(query, autogenkeys);
            } else {
                System.out.println("Invalid SQL method!");
            }
            // Clean up
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        } finally {
            try {
                stmt.close();
                conn.close();
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
        return true;
    }


    private Connection connect() {
        Connection conn = null;
        boolean retval = false;
        try {
            // Create database connection
            System.out.println("Oracle JDBC Driver Loaded");
            System.out.println("Oracle Connecting..");
            String nameForConnect = "sys as sysdba";
            String pass1 = "Psmo0601";
            String url = "jdbc:oracle:thin:@10.0.22.108:1521:XE";
            conn = DriverManager.getConnection(url, nameForConnect, pass1);
            System.out.println("Oracle Connected");
        } catch (Exception e) {
            System.err.println("ERROR: failed to load Oracle JDBC driver.");
            e.printStackTrace();
            return null;
        }
        return conn;
    }

    public static String insertCustomers(String first, String last, String pass) {
        StringBuffer sbuf = new StringBuffer();

        Connection conn = null;
        String db = "jdbc:hsqldb:hsql://localhost/xdb";
        String user = "SA";
        String password = "";

        try {
            // Create database connection
            conn = DriverManager.getConnection(db, user, password);

            // Create and execute statement
            Statement stmt = conn.createStatement();
            String sql = "INSERT INTO CUSTOMER VALUES (\'" + first + "\',\'" + last + "\', \'" + pass + "')";
            System.out.println("Adding: " + sql);
            stmt.executeQuery(sql);
            System.out.println("Inserted into Database");

            // Clean up
            stmt.close();
        } catch (SQLException e) {
            System.err.println("SQL Error:" + e.getMessage());
        } finally {
            try {
                // Close connection
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
        return sbuf.toString();
    }

    public boolean executeSQL(String name, String pass) {
        return executeSQLHelper("executeSQL", name, pass);
    }

    public boolean executeQuerySQL(String name, String pass) {
        return executeSQLHelper("executeQuerySQL", name, pass);
    }

    public boolean executeUpdateSQL(String name, String pass) {
        return executeSQLHelper("executeUpdateSQL", name, pass);
    }

    public boolean executeSQLHelper(String methodName, String name, String pass) {
        boolean retVal = false;
        Connection conn = connect();
        if (conn == null)
            return false;
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            StringBuffer sbuf = new StringBuffer();

            String query = "select FIRST, LAST from CUSTOMERS WHERE LAST=\'" + name + "\' AND PASSWORD= \'" + pass + "\'";
            System.out.println("QUERY :" + query);
            if (methodName.equalsIgnoreCase("executeQuerySQL")) {
                ResultSet rs = stmt.executeQuery(query);
                // Loop through the data and print all artist names
                while (rs.next()) {
                    sbuf.append("Customer Name: " + rs.getString("FIRST") + " " + rs.getString("LAST"));
                    System.out.println("Customer Name: " + rs.getString("FIRST") + " " + rs.getString("LAST"));
                    sbuf.append("<br>");
                    retVal = sbuf.toString().length() > 2;
                }
                // Clean up
                rs.close();
            } else if (methodName.equalsIgnoreCase("executeSQL")) {
                retVal = stmt.execute(query);
            } else if (methodName.equalsIgnoreCase("executeUpdateSQL")) {
                retVal = stmt.executeUpdate(query) > 0;
            }

            stmt.close();
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
    public boolean getCustomersStoredProc1(String name, String pass) {
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
        try {
            // close the statement as its not a callable statement
            stmt.close();
            CallableStatement c = null;
            c = conn.prepareCall("{call verifyuser(?,?,?)}");
            c.setString(1, name);
            c.setString(2, pass);
            c.registerOutParameter(3, Types.INTEGER);
            System.out.println("DB stored Proc being called");
            boolean hasResults = c.execute();
            // Loop through the data and print all artist names
            output = c.getInt(3);
            System.out.println("Customer Count: " + c.getInt(3));
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

    public boolean getCustomersStoredProc(int callType, String name, String pass) {
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
            CallableStatement c = null;
            c = conn.prepareCall("{call verifyuser(?,?,?)}");
            c.setString(1, name);
            c.setString(2, pass);
            c.registerOutParameter(3, Types.INTEGER);
            System.out.println("DB stored Proc being called");
            if (callType==1){
                c.execute();
            }
            if (callType == 2){
                c.executeQuery();
            }
            //c.executeQuery();
            // Loop through the data and print all artist names
            output = c.getInt(3);
            System.out.println("Customer Count: " + c.getInt(3));
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
    public boolean getCustomersPreparedStatement(String methodName, String param1, String param2) {
        if ( methodName.equals("execute")){
            return getCustomersPreparedStatementExecute(param1, param2);
        }
        if ( methodName.equals("executeQuery")){
            return getCustomersPreparedStatementExecuteQuery(param1, param2);
        }
        if ( methodName.equals("executeUpdate")){
            return getCustomersPreparedStatementExecuteUpdate(param1, param2);
        }

        return false;
    }

    public boolean getCustomersPreparedStatementExecute(String name, String pass) {
        Connection conn = connect();
        boolean hasResults = false;
        if (conn == null)
            return false;
        PreparedStatement stmt = null;
        try {
            String query = "SELECT FIRST, LAST from CUSTOMERS WHERE LAST = ? AND PASSWORD = ?";
            System.out.println("QUERY :" + query);
            stmt = conn.prepareStatement(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        int output = 0;
        try {
            stmt.setString(1, name);
            stmt.setString(2, pass);
            System.out.println("PreparedStatement.execute being called with Last=" + name + " Password=" + pass);
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

    public boolean getCustomersPreparedStatementExecuteQuery(String name, String pass) {
        Connection conn = connect();
        boolean hasResults = false;
        if (pass ==null || pass.length() ==0){
            pass = "";

        }
        if (conn == null)
            return false;
        PreparedStatement stmt = null;
        try {
            String query = "SELECT FIRST, LAST from CUSTOMERS WHERE LAST = ? AND PASSWORD = ?";
            System.out.println("SQL:" + query);
            stmt = conn.prepareStatement(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        int output = 0;
        try {
            stmt.setString(1, name);
            stmt.setString(2, pass);
            System.out.println("PreparedStatement.executeQuery being called with Last=" + name + " Password=" + pass);
            ResultSet result = stmt.executeQuery();
            while (result.next()){
                hasResults = true;
                System.out.println("PreparedStatement.executeQuery- returned true");
                break;
            }
            System.out.println("PreparedStatement.executeQuery- returned" + hasResults);

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

    public boolean getCustomersPreparedStatementExecuteUpdate(String first, String last) {
        Connection conn = connect();
        boolean hasResults = false;
        if (conn == null)
            return false;
        PreparedStatement stmt = null;
        try {
            String query = "UPDATE CUSTOMERS SET FIRST = ? WHERE LAST = ?";
            System.out.println("SQL:" + query);
            stmt = conn.prepareStatement(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        int output = 0;
        try {
            stmt.setString(1, first);
            stmt.setString(2, last);
            System.out.println("PreparedStatement.executeUpdate being called with First=" + first + " Last=" + last);
            int count = stmt.executeUpdate();
            hasResults = count > 0;
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

}