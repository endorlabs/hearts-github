package com.endor;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.InvalidParameterException;
import java.sql.*;
import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.io.BufferedReader;
import java.io.FileReader;

import javax.script.ScriptEngineManager;
import javax.script.ScriptEngine;
import javax.script.ScriptException;


// TODO rename to SQLServlet
@WebServlet(name = "BooksServlet")
public class BooksServlet extends HttpServlet {
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
                
        		"Customer Specific<br>" +
        		"--------------------------------------<br><br>"+
                "First name: <input type=\"text\" name=\"first\">  -- ( For SQL with Update)  <br><br>" +
                "Last name: <input type=\"text\" name=\"name\"><br><br>" +
                "Password: <input type=\"text\" name=\"password\"><br><br>" +
                "Filter: <input type=\"text\" name=\"filtername\"> -- (Enter last name with starting chars:)  <br><br><br>" +
                
                "Account Specific<br>" +
        		"--------------------------------------<br><br>"+
        		"Enter ID: <input type=\"text\" name=\"singleID\">  <br><br><br>" +
                
                "Use Stored Procedure<br>" +
        		"--------------------------------------<br><br>"+
                "Stored procedure name: <input type=\"text\" name=\"procedure_name\"><br><br><br>" +
                
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
                "<input type=\"radio\" name=\"sqltype\" value=\"storedproc.executeScript\">: storedproc.executeScript() -- (It takes SQL input from local file, Input file path:/opt/sqlinput.txt)<br>" +
                "<input type=\"radio\" name=\"sqltype\" value=\"PreparedStatement.executeScript\">: PreparedStatement.executeScript() -- (It takes SQL input from local file, Input file path:/opt/sqlinput1.txt)<br>" +
                "<input type=\"radio\" name=\"sqltype\" value=\"storedproc.callbyName()\">: storedproc.callbyName()<br>" +
                "<input type=\"radio\" name=\"sqltype\" value=\"PreparedStatementDirectPara.execute()\">: PreparedStatementDirectPara.execute()(Execute PreparedStatement on same thread)<br>" +
                "<input type=\"radio\" name=\"sqltype\" value=\"PreparedStatementDirectParaAsync.execute()\">: PreparedStatementDirectParaAsync.execute()(Execute PreparedStatement on seperate thread)<br>" +
                "<input type=\"radio\" name=\"sqltype\" value=\"StoredProcDirectPara.execute()\">: StoredProcDirectPara.execute()(Execute PreparedStatement on same thread)<br>" +
                "<input type=\"radio\" name=\"sqltype\" value=\"StoredProcDirectParaAsync.execute()\">: StoredProcDirectParaAsync.execute()(Execute StoredProc on seperate thread)<br>" +
                "<input type=\"radio\" name=\"sqltype\" value=\"StoredProcAsync.execute()\">: StoredProcAsync.execute()(Execute StoredProc using setString() on seperate thread)<br>" +
                "<input type=\"radio\" name=\"sqltype\" value=\"storedproccallwithsqlinj.execute()\">: storedproccallwithsqlinj.execute()(Insert Sql injection with stored procedure, 1=1 or call verifyuser(?,?))<br>" +
                "<input type=\"radio\" name=\"sqltype\" value=\"multiplestoredproc\">: Multiple Stored Procedure<br>" +
                "<input type=\"radio\" name=\"sqltype\" value=\"nonvulnstoredproc\">: Non Vulnerable Stored Procedure<br>" +
                "<input type=\"radio\" name=\"sqltype\" value=\"PreparedStatementDirectParaIdentifier1\">: PreparedStatementDirectParaIdentifier1.execute()<br>" +
                "<input type=\"radio\" name=\"sqltype\" value=\"PreparedStatementDirectParaIdentifier2\">: PreparedStatementDirectParaIdentifier2.execute() (order by clause)<br>" +
                "<input type=\"radio\" name=\"sqltype\" value=\"PreparedStatementDirectParaIdentifier3\">: PreparedStatementDirectParaIdentifier3.execute() (order by clause)<br>" +
                "<input type=\"radio\" name=\"sqltype\" value=\"PreparedStatementQueryConnectingStrings\">: PreparedStatementQueryConnectingStrings.execute() <br>" +
                "<input type=\"radio\" name=\"sqltype\" value=\"PreparedStatementQueryMultiLegs\">: PreparedStatementQueryMultiLegs.execute() <br>" +
                "<input type=\"radio\" name=\"sqltype\" value=\"PreparedStatementQueryMultiLegs_second\">: PreparedStatementQueryMultiLegs_second.execute() <br>" +
                "<input type=\"radio\" name=\"sqltype\" value=\"StoredProcedureMultihub\">: StoredProcedureMultihubs.execute() <br>" +
                "<input type=\"radio\" name=\"sqltype\" value=\"StoredProcedureMultihub_second\">: StoredProcedureMultihubs_second.execute() <br>" +
                "<br><br>" +

                "<input type=\"submit\" value=\"Submit\">" + "</form>";
        out.println(form);

        if (createRecord(request, out)) {
            return;
        }
        String first = request.getParameter("first");
        String last = request.getParameter("name");
        String pass = request.getParameter("password");
        String filtername = request.getParameter("filtername");
        String singleID = request.getParameter("singleID");


        String procedure_name = request.getParameter("procedure_name");

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
            put("storedproc.executeScript",13);
            put("PreparedStatement.executeScript",14);
            put("storedproc.callbyName()",15);
            put("PreparedStatementDirectPara.execute()",16);
            put("PreparedStatementDirectParaAsync.execute()",17);
            put("StoredProcDirectPara.execute()",18);
            put("StoredProcDirectParaAsync.execute()",19);
            put("StoredProcAsync.execute()",20);
            put("storedproccallwithsqlinj.execute()",21);
            put("multiplestoredproc", 22);
            put("nonvulnstoredproc", 23);
            put("PreparedStatementDirectParaIdentifier1", 24);
            put("PreparedStatementDirectParaIdentifier2", 25);
            put("PreparedStatementDirectParaIdentifier3", 26);
            put("PreparedStatementQueryConnectingStrings",27);
            put("PreparedStatementQueryMultiLegs",28);
            put("PreparedStatementQueryMultiLegs_second",29);
            put("StoredProcedureMultihub",30);
            put("StoredProcedureMultihub_second",31);


            
        }};

        String sqltypeStr = request.getParameter("sqltype");
        int sqltype = sqltypeMap.get(sqltypeStr);
        String retVal = "Failed!";

        switch (sqltype) {
            case 0: //storedproc
            	try {
                    if (!(last.isEmpty())  && getCustomersStoredProc(1,last,pass)) {
                        retVal = "Succeeded";
                        break;
                    }
            	} catch (NullPointerException e) { }
            	
            	try {
                    if (!(filtername.isEmpty()) && getCustomersStoredProc(1,filtername, pass)) {
                        retVal = "Succeeded";
                        break;
                    }
            	} catch (NullPointerException e) { }
            	
            	try {
                    if (!(singleID.isEmpty()) && getCustomersStoredProc(singleID)) {
                        retVal = "Succeeded";
                        break;
                    }
            	} catch (NullPointerException e) { }
            	break;
                
            case 1: // executeUpdateSQLColNames
                if (getCustomersUpdateColName(first, last, pass)) {
                    retVal = "Succeeded";
                }
                break;
            case 2: //executeQuerySQL
            	try {
            		if (!(last.isEmpty()) && executeQuerySQL(1, last, pass)) {
            			retVal = "Succeeded";
            			break;
            		}
                } catch(NullPointerException e) { }
            	try {
            		if (!(filtername.isEmpty()) && executeQuerySQL(2, filtername, pass)) {
                        retVal = "Succeeded";
                        break;
            		}
                } catch(NullPointerException e) { }
                try {
                    if (!(singleID.isEmpty()) && executeQuerySQL(singleID)) {
                        retVal = "Succeeded";
                        break;
                    }
                } catch(NullPointerException e) { }
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
            case 13: //storedproc executeScript
                if (getCustomersStoredProc2()) {
                    retVal = "Succeeded";
                }
                break;
            case 14:
            	if (getCustomerPreparedStatement2()) {
            		retVal = "Succeeded";
            	}
                break;
            case 15:
            	if (storedproccallbyName(procedure_name, last, pass)) {
            		retVal = "Succeeded";
            	}
            	break;
            case 16:
            	if (PreparedStatementDirectPara(last, pass)) {
            		retVal = "Succeeded";
            	}
            	break;
            case 17:
            	if (PreparedStatementDirectParaAsync(last,pass)) {
            		retVal = "Succeeded";
            	}
            	break;
            case 18:
            	if (StoredProcDirectPara(last,pass)) {
            		retVal = "Succeeded";
            	}
            	break;
            case 19:
            	if (StoredProcDirectParaAsync(last,pass)) {
            		retVal = "Succeeded";
            	}
            	break;
            case 20:
            	if (getCustomersStoredProcAsync(last, pass)) {
                    retVal = "Succeeded";
                }
                break;
            case 21:
            	if (storedproccallwithsqlinj(last, pass)) {
                    retVal = "Succeeded";
                }
                break;
            case 22: //multiplestoredproc
                if (getCustomersMultipleStoredProc(1, last, pass)) {
                    retVal = "Succeeded";
                }
                break;
            case 23: //nonvulnstoredproc
                if (getCustomersNonvulnerableStoredProc(1, last, pass)) {
                    retVal = "Succeeded";
                }
                break;
            case 24:
            	if (PreparedStatementDirectParaIdentifier1(last, pass)) {
            		retVal = "Succeeded";
            	}
            	break;
            case 25:
            	if (PreparedStatementDirectParaIdentifier2(last, pass)) {
            		retVal = "Succeeded";
            	}
            	break;
            case 26:
            	if (PreparedStatementDirectParaIdentifier3(last, pass)) {
            		retVal = "Succeeded";
            	}
            	break;
            case 27:
            	try {
            	    if (!(last.isEmpty()) && PreparedStatementEexecuteQuerySQL(1, last, pass)) {
                        retVal = "Succeeded";
                        break;
                    }
            	} catch (NullPointerException e) { }
            	
            	try {
            	    if(!(filtername.isEmpty()) && PreparedStatementEexecuteQuerySQL(2, filtername, pass)) {
                	    retVal = "Succeeded";
                	    break;
                    }
            	} catch (NullPointerException e) { }
            	
            	try {
            	    if (!(singleID.isEmpty()) && PreparedStatementEexecuteQuerySQL(singleID)) {
                        retVal = "Succeeded";
                        break;
            	    }
                } catch(NullPointerException e) { }
            	break;
                
            case 28:
            	try {
                	Thread.sleep(2000);
                	} catch (InterruptedException e) {
                	e.printStackTrace();
                	}
            	HttpURLConnectionExample.sendPOSTwithParameter(last,pass,"prepared_statement");
            	retVal = "Succeeded";
            	//response.sendRedirect("ExtraServlet");
            	break;
            case 29:
            	try {
                	Thread.sleep(2000);
                	} catch (InterruptedException e) {
                	e.printStackTrace();
                	}
            	HttpURLConnectionExample.sendPOSTwithParameter("prakash'--","psmo","prepared_statement");
            	retVal = "Succeeded";
            	//response.sendRedirect("ExtraServlet");
            	break;
            
            case 30:
            	try {
                	Thread.sleep(2000);
                	} catch (InterruptedException e) {
                	e.printStackTrace();
                	}
                
                HttpURLConnectionExample.sendPOSTwithParameter(last, pass,"stored_procedure");
                retVal = "Succeeded";
                break;
                
            case 31:
            	try {
                	Thread.sleep(2000);
                	} catch (InterruptedException e) {
                	e.printStackTrace();
                	}
            	HttpURLConnectionExample.sendPOSTwithParameter("prakash'--","psmo","stored_procedure");
            	retVal = "Succeeded";
            	//response.sendRedirect("ExtraServlet");
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

    // Handling "executeQuerySQL" and "PreparedStatementEexecuteQuerySQL" for customer specific info
    public boolean executeQuerySQL(int n, String name, String pass) {
        return executeSQLHelper("executeQuerySQL", n, name, pass);
    }
    public boolean PreparedStatementEexecuteQuerySQL(int n, String name, String pass) {
        return executeSQLHelper("PreparedStatementEexecuteQuerySQL", n, name, pass);
    }
    
    // Handling "executeQuerySQL" and "PreparedStatementEexecuteQuerySQL" for account specific info
    public boolean executeQuerySQL(String ids) {
            return executeSQLHelper("executeQuerySQL", ids);
    	}
 
    public boolean PreparedStatementEexecuteQuerySQL(String ids) {
            return executeSQLHelper("PreparedStatementEexecuteQuerySQL", ids);
    	} 
    
    // Handling "executeSQL" and "executeUpdateSQL" for customer specific info
    public boolean executeSQL(String name, String pass) {
        return executeSQLHelper("executeSQL", name, pass);
    }

    public boolean executeUpdateSQL(String name, String pass) {
        return executeSQLHelper("executeUpdateSQL", name, pass);
    }

    
    public boolean executeSQLHelper(String methodName, String name, String pass) {
        boolean retVal = false;
        Connection conn = connect();
        if (conn == null)
            return false;
        
        try {
            StringBuffer sbuf = new StringBuffer();
            String query = new String();
            
            query = "select FIRST, LAST from CUSTOMERS WHERE LAST=\'" + name + "\' AND PASSWORD= \'" + pass + "\'";
            
            
            if (methodName.equalsIgnoreCase("executeQuerySQL")) {
            	System.out.println("QUERY :" + query);
            	Statement stmt = conn.createStatement();
            	ResultSet rs = stmt.executeQuery(query);
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
            } else if (methodName.equalsIgnoreCase("PreparedStatementEexecuteQuerySQL")) {
            	System.out.println("PreparedStatementQUERY :" + query);
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
            } else if (methodName.equalsIgnoreCase("executeSQL")) {
            	Statement stmt = conn.createStatement();
                retVal = stmt.execute(query);
                stmt.close();
            } else if (methodName.equalsIgnoreCase("executeUpdateSQL")) {
            	Statement stmt = conn.createStatement();
                retVal = stmt.executeUpdate(query) > 0;
                stmt.close();
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
    
    public boolean executeSQLHelper(String methodName, int n, String name, String pass) {
        boolean retVal = false;
        Connection conn = connect();
        if (conn == null)
            return false;       
        
        // Check for multiple values entry before constructing the query
        String[] name_values = name.split(",");
        String parse_name_values = name_values[0];
        for (int i = 1; i< name_values.length; i++ ) {
        	parse_name_values += "\',\'" ;
        	parse_name_values += name_values[i];
        }
        
        try {
            StringBuffer sbuf = new StringBuffer();
            String query;
            
            // Check for filter operation
            if(n==2) {
            	query = "select FIRST, LAST from CUSTOMERS WHERE LAST like \'" + name + "%\' AND PASSWORD= \'" + pass + "\'";
            }
            // Check for normal query operation
            else {
            	if(parse_name_values.contains(",")) {
            		query = "select FIRST, LAST from CUSTOMERS WHERE LAST IN (\'" + parse_name_values + "\')";
            	} else {
            		query = "select FIRST, LAST from CUSTOMERS WHERE LAST=\'" + parse_name_values + "\' AND PASSWORD= \'" + pass + "\'";
            	}
            }
            System.out.println("QUERY :" + query);  
            
            // Check for preparedstatement
            if (methodName.equalsIgnoreCase("executeQuerySQL")) {
            	Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query);
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
            } else if (methodName.equalsIgnoreCase("PreparedStatementEexecuteQuerySQL")) {
            	System.out.println("PreparedStatementQUERY :" + query);
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
            } else if (methodName.equalsIgnoreCase("executeSQL")) {
            	Statement stmt = conn.createStatement();
                retVal = stmt.execute(query);
                stmt.close();
            } else if (methodName.equalsIgnoreCase("executeUpdateSQL")) {
            	Statement stmt = conn.createStatement();
                retVal = stmt.executeUpdate(query) > 0;
                stmt.close();
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
    
    public boolean executeSQLHelper(String methodName, String ids) {
        boolean retVal = false;
        String query = new String() ;
        Connection conn = connect();
        if (conn == null)
            return false;
        
        if(ids.contains(",")) {
        	query = "select ID, NAME from ACCOUNTS WHERE ID IN (" + ids + ")" ;
        } else {
        	query = "select ID from ACCOUNTS WHERE ID = " + ids ;
        }
        
        try {
            if (methodName.equalsIgnoreCase("executeQuerySQL")) {
            	StringBuffer sbuf = new StringBuffer();
                Statement stmt = conn.createStatement();
                System.out.println("QUERY :" + query);
            	ResultSet rs = stmt.executeQuery(query);
                // Loop through the data and print all artist names
                while (rs.next()) {
                	sbuf.append("Customer id: " + rs.getString("ID"));
                	System.out.println("Customer id: " + rs.getString("ID"));
                	sbuf.append("<br>");
                	retVal = sbuf.toString().length() > 2;
                	}
                // Clean up
                stmt.close();
                rs.close();
            } else if (methodName.equalsIgnoreCase("PreparedStatementEexecuteQuerySQL")) {
            	System.out.println("PreparedStatementQUERY :" + query);
            	PreparedStatement stmt = conn.prepareStatement(query);
            	ResultSet rs = stmt.executeQuery();
            	
            	StringBuffer sbuf = new StringBuffer();
                // Loop through the data and print all artist names
                while (rs.next()) {
                	sbuf.append("Customer id: " + rs.getString("ID"));
                	System.out.println("Customer id: " + rs.getString("ID"));
                	sbuf.append("<br>");
                	retVal = sbuf.toString().length() > 2;
                	}
                // Clean up
                stmt.close();
                rs.close();
            } else if (methodName.equalsIgnoreCase("executeSQL")) {
                Statement stmt = conn.createStatement();
                retVal = stmt.execute(query);
                stmt.close();
            } else if (methodName.equalsIgnoreCase("executeUpdateSQL")) {
                Statement stmt = conn.createStatement();
                retVal = stmt.executeUpdate(query) > 0;
                stmt.close();
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
    
    public static boolean isNumeric(String strNum) {
        try {
            double d = Integer.parseInt(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
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

    public boolean storedproccallwithsqlinj(String name, String pass)
    {
    	Connection conn = connect();
        if (conn == null)
            return false;
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        int output = 0;
        try {
            // close the statement as its not a callable statement
            stmt.close();
            CallableStatement c = null;
            String ProcQuery = "{" + name + " call verifyuser(?,?,?)}";
            c = conn.prepareCall(ProcQuery);
            c.setString(1, pass);
            c.setString(2, "prakash");
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
    
    public boolean storedproccallbyName(String procedure_name, String name, String pass)
    {
    	Connection conn = connect();
        if (conn == null)
            return false;
        int output = 0;
        try {
            CallableStatement c = null;
            if(name.isEmpty() && procedure_name.equalsIgnoreCase("verifyuser_withresponse")) {
            	String ProcQuery = "{call "+ procedure_name + "(?)}";
            	c = conn.prepareCall(ProcQuery);
            	c.registerOutParameter(1, Types.INTEGER);
            	c.execute();
            	output = c.getInt(1);
            }
            else if(name.isEmpty() && procedure_name.equalsIgnoreCase("verifyuser_noresponse")) {
            	String ProcQuery = "{call "+ procedure_name + "()}";
            	c = conn.prepareCall(ProcQuery);
            	c.execute();
            }
            else {
            	String ProcQuery = "{call "+ procedure_name + "(?,?,?)}";
                c = conn.prepareCall(ProcQuery);
                c.setString(1, name);
                c.setString(2, pass);
                c.registerOutParameter(3, Types.INTEGER);
                c.execute();
                output = c.getInt(3);
            }
            
            System.out.println("DB stored Proc being called");            
            System.out.println("Customer Count: " + output);
            
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
        
    	return output >= 0;
    }
    
    public boolean getCustomersStoredProcAsync(String name, String pass) {
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
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            System.out.println("Created ExecutorService");
            CallableStatementTask task = new CallableStatementTask(c);
            System.out.println("Created CallableStatementTask object");
            Future<Boolean> future = executorService.submit(task);
            System.out.println("Executed executorService.submit(task)");
            hasResults = future.get().booleanValue();
            executorService.shutdown();
            System.out.println("Finished");
            System.out.println("DB stored Proc being called");
            //c.execute();
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
        try {
            // close the statement as its not a callable statement
            stmt.close();
            CallableStatement c = null;
            String query = "{call verifyuser(?,?,?)}";
            c = conn.prepareCall(query);
            c.setString(1, name);
            c.setString(2, pass);
            c.registerOutParameter(3, Types.INTEGER);
            System.out.println("DB stored Proc being called");
            System.out.println(query);
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
    
    public boolean getCustomersStoredProc(int callType, String name, String filtername, String pass) {
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
            if (filtername.isEmpty()) {
            	query = "{call verifyuser(?,?,?)}";
                c = conn.prepareCall(query);
                c.setString(1, name);
                c.setString(2, pass);
                c.registerOutParameter(3, Types.INTEGER);
                System.out.println("DB stored Proc being called");
                System.out.println(query);
                if (callType==1){
                    c.execute();
                }
                if (callType == 2){
                    c.executeQuery();
                }
                output = c.getInt(3);
                System.out.println("Customer Count: " + output);
            } else {
            	query = "{ ? = call filter_names_using_like(?,?)}";
                c = conn.prepareCall(query);
                c.registerOutParameter(1, Types.INTEGER);
                c.setString(2, filtername);
                c.setString(3, pass);
                System.out.println("DB stored Proc being called");
                System.out.println(query);
                if (callType==1){
                    c.execute();
                }
                if (callType == 2){
                    c.executeQuery();
                }
                output = c.getInt(1);
                System.out.println("Customer Count: " + output);
            }
            
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
    
    public boolean getCustomersStoredProc(String ids) {
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
            String query;
            if (ids.contains(",")) {
            	query = "{ ? = call filter_ids_using_in( ? ) }";
            }
            else {
            	query = "{ ? = call filter_ids_using_singleid( ? ) }";
            }
            c = conn.prepareCall(query);
            c.registerOutParameter(1, Types.INTEGER);
            c.setString(2, ids);
            System.out.println("DB stored Proc being called");
            System.out.println(query);
            c.execute();
            //c.executeQuery();
            // Loop through the data and print all artist names
            output = c.getInt(1);
            System.out.println("ID Count: " + output);
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
    public boolean getCustomersNonvulnerableStoredProc(int callType, String name, String pass) {
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
            c = conn.prepareCall("{?=call verifyuser1(?,?,?)}");
            c.setString(2, name);
            c.setString(3, pass);
            c.setString(4, "test");
            System.out.println("Executed the query" );
            c.registerOutParameter(1, Types.BOOLEAN);
            System.out.println("DB stored Proc being called");
            if (callType==1){
                c.execute();
            }
            if (callType == 2){
                c.executeQuery();
            }
            //c.executeQuery();
            // Loop through the data and print all artist names
            //output = c.getInt(3);
            System.out.println("Executed the query" );
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
    public boolean getCustomersMultipleStoredProc(int callType, String name, String pass) {
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
        CallableStatement c;
        try {
        	System.out.println("Executing first procedure");
        	c = null;
        	c = conn.prepareCall("{call verifyusera(?,?,?,?)}");
            c.setString(1, name);
            c.setString(2, pass);
            c.setString(3, "SARDIWAL");
            c.registerOutParameter(4, Types.INTEGER);
            System.out.println("DB stored Proc being called");
            if (callType==1){
                c.execute();
            }
            if (callType == 2){
                c.executeQuery();
            }
            System.out.println("Customer Count: " + c.getInt(4));
            System.out.println("Executed second procedure");
            c.close();
        	} catch(Exception e) {
        		System.out.println("Exception : "+e.getMessage());
        	}
        try {
            	System.out.println("Executing second procedure");
            	c = null;
            	c = conn.prepareCall("{call verifyuserb(?,?,?,?)}");
                c.setString(1, name);
                c.setString(2, pass);
                c.setString(3, "SARDIWAL");
                c.registerOutParameter(4, Types.INTEGER);
                System.out.println("DB stored Proc being called");
                if (callType==1){
                    c.execute();
                }
                if (callType == 2){
                    c.executeQuery();
                }
                System.out.println("Customer Count: " + c.getInt(4));
                System.out.println("Executed third procedure");
                c.close();
            	} catch(Exception e) {
            		System.out.println("Exception : "+e.getMessage());
            	}
        try {
        	System.out.println("Executing third procedure");
        	c = null;
        	c = conn.prepareCall("{call verifyuserc(?,?,?,?)}");
            c.setString(1, name);
            c.setString(2, pass);
            c.setString(3, "SARDIWAL");
            c.registerOutParameter(4, Types.INTEGER);
            System.out.println("DB stored Proc being called");
            if (callType==1){
                c.execute();
            }
            if (callType == 2){
                c.executeQuery();
            }
            System.out.println("Customer Count: " + c.getInt(4));
            System.out.println("Executed third procedure");
            c.close();
        	} catch(Exception e) {
        		System.out.println("Exception : "+e.getMessage());
        	}
        	
        	try {
            	System.out.println("Executing fourth procedure");
            	c = null;
            	c = conn.prepareCall("{? = call verifyuserd(?,?)}");
                c.setString(2, name);
                c.setString(3, pass);
               // c.setString(3, "SARDIWAL");
                c.registerOutParameter(1, Types.VARCHAR);
                System.out.println("DB stored Proc being called");
                if (callType==1){
                    c.execute();
                }
                if (callType == 2){
                    c.executeQuery();
                }
                System.out.println("Customer Count: ");
                System.out.println("Executed fourth procedure");
                c.close();
            	} catch(Exception e) {
            		System.out.println("Exception : "+e.getMessage());
            	}
        	finally {
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
    public boolean getCustomersStoredProc2() {
        Connection conn = connect();
        if (conn == null)
            return false;
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
		// Open the input file for sql query input values and keep the reader ready
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader("/opt/sqlinput.txt"));
			System.out.println("sqlinput.txt file opened successfully");
			} catch (IOException e) {
				System.out.println("Failed to open Input file");
				e.printStackTrace();
			}
        int output = 0;
        boolean hasResults = false;
        try {
            // close the statement as its not a callable statement
            stmt.close();
            CallableStatement c = null;
		    String line = reader.readLine();
		    while (null != line) {
		    	String line1 = line.replaceAll("[\\n]", "");
	    	    System.out.println("Stored procedure being called with :" + line1);
		    	c = conn.prepareCall("{call verifyuser(?,?,?)}");
		    	c.setString(1, line1);
		    	c.setString(2, "shiva");
		    	c.registerOutParameter(3, Types.INTEGER);
	    	    //System.out.println("Stored procedure called successfully for1 " + line);
		    	try {
		    		c.execute();
		    	    c.close();
		    	} catch (Exception e) {
		            System.out.println("Exception !");
		            System.err.println(e.getMessage());
		        }
            // Loop through the data and print all artist names
		    	//output = c.getInt(3);
	    	    line = reader.readLine();
	    	    Thread.sleep(2000);
	    	    
		    }
            //System.out.println("Customer Count: " + c.getInt(3));
            // Clean up
            //c.close();
	    	reader.close();
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
    
    public boolean  getCustomerPreparedStatement2() {
    	
    	// Open the input file for sql query input values and keep the reader ready
    	BufferedReader reader = null;
    	try {
    			reader = new BufferedReader(new FileReader("/opt/sqlinput1.txt"));
    			System.out.println("sqlinput.txt file opened successfully");
    		}
    	catch (IOException e) {
    					System.out.println("Failed to open Input file");
    					e.printStackTrace();
    					return false;
    			}
    	        int output = 0;
    	        boolean hasResults = false;
    	        try {
    			    	String line = reader.readLine();
    			    	while (null != line) {
    			    	String line1 = line.replaceAll("[\\n]", "");
    		    	    System.out.println("Stored procedure being called with :" + line1);
    		    	    hasResults = getCustomersPreparedStatementExecute(line1, "Sardiwal");
    		    	    line = reader.readLine();
    		    	    Thread.sleep(2000);
    			    	}
    			    	reader.close();
    	        	}
    			   catch (Exception ex){
    				   ex.getStackTrace();
    				   return false;
    			   }
    	        
    	return true;
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

    public boolean StoredProcDirectPara(String name, String pass) {
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
            //String Proc_query = "{CALL sql_login('" + name +"',"+ pass + "')}";
            String Proc_query = "{? = call verifyuser('" + name +"','"+ pass + "')}";
            System.out.println("Created Procedure query string : " + Proc_query);
            c = conn.prepareCall(Proc_query);
            System.out.println("conn.prepareCall(Proc_query) called");
            //c.setString(1, name);
            //c.setString(2, pass);
            //c.registerOutParameter(3, Types.INTEGER);
            c.registerOutParameter(1, Types.INTEGER);
            System.out.println("DB stored Proc being called");
            c.execute();
            //c.executeQuery();
            // Loop through the data and print all artist names
            //output = c.getInt(1);
            System.out.println("Customer Count: " + c.getInt(1));
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
    
    public boolean StoredProcDirectParaAsync(String name, String pass) {
    	Connection conn = connect();
    	if (null == name)
    	{name = "Shiva";}
    	if (null == pass)
    	{pass = "Prakash";}
        boolean hasResults = false;
        if (conn == null)
            return false;
        //PreparedStatement stmt = null;
        CallableStatement c = null;
        try {
            String Proc_query = "{CALL sql_login('" + name +"',"+ pass + "')}";
            System.out.println("Created Procedure query string : " + Proc_query);
            c = conn.prepareCall(Proc_query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        int output = 0;
        try {
            //stmt.setString(1, name);
            //stmt.setString(2, pass);
            System.out.println("CallableStatement.execute() being called with Last='" + name + "' Password='" + pass +"'");
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            System.out.println("Created ExecutorService");
            CallableStatementTask task = new CallableStatementTask(c);
            System.out.println("Created CallableStatementTask object");
            Future<Boolean> future = executorService.submit(task);
            System.out.println("Executed executorService.submit(task)");
            hasResults = future.get().booleanValue();
            executorService.shutdown();
            System.out.println("Finished");
            //hasResults = stmt.execute();
            // Loop through the data and print all artist names
        } catch (Exception e) {
            System.out.println("Exception !");
            System.err.println(e.getMessage());
        } finally {
            try {
                // Close connection
                c.close();
                conn.close();
            } catch (SQLException e) {
                System.err.println(e.getMessage());
                System.out.println("Exception 2");
            }
        }
    	return hasResults;
    }
    
    public boolean PreparedStatementDirectParaAsync(String name, String pass) {
    	Connection conn = connect();
    	if (null == name)
    	{name = "Shiva";}
    	if (null == pass)
    	{pass = "Prakash";}
        boolean hasResults = false;
        if (conn == null)
            return false;
        PreparedStatement stmt = null;
        try {
            String query = "SELECT FIRST, LAST from CUSTOMERS WHERE LAST = '" + name + "' AND PASSWORD = '" + pass + "'";
            System.out.println("QUERY :" + query);
            stmt = conn.prepareStatement(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        int output = 0;
        try {
            //stmt.setString(1, name);
            //stmt.setString(2, pass);
            System.out.println("PreparedStatement.execute being called with Last='" + name + "' Password='" + pass +"'");
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            System.out.println("Created ExecutorService");
            PrepareStatementTask task = new PrepareStatementTask(stmt);
            System.out.println("Created PrepareStatementTask");
            Future<Boolean> future = executorService.submit(task);
            System.out.println("Executed executorService.submit(task)");
            hasResults = future.get().booleanValue();
            executorService.shutdown();
            System.out.println("Finished");
            //hasResults = stmt.execute();
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
    
    public boolean PreparedStatementDirectParaIdentifier1(String name, String pass) {
    	Connection conn = connect();
    	if (null == name)
    	{name = "Shiva";}
    	if (null == pass)
    	{pass = "Prakash";}
        boolean hasResults = false;
        if (conn == null)
            return false;
        PreparedStatement stmt = null;
        try {
            String query = "SELECT FIRST," +  name + " from CUSTOMERS WHERE LAST = \'" + pass + "\'";
            System.out.println("QUERY :" + query);
            stmt = conn.prepareStatement(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        int output = 0;
        try {
            //stmt.setString(1, name);
            //stmt.setString(2, pass);
            System.out.println("PreparedStatement.execute being called with Last='" + name + "' Password='" + pass +"'");
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
    
    public boolean PreparedStatementDirectParaIdentifier2(String name, String pass) {
    	Connection conn = connect();
    	if (null == name)
    	{name = "Shiva";}
    	if (null == pass)
    	{pass = "Prakash";}
        boolean hasResults = false;
        if (conn == null)
            return false;
        PreparedStatement stmt = null;
        try {
            String query = "SELECT FIRST,last from CUSTOMERS WHERE first = \'" + pass + "\' order by " +  name;
            System.out.println("QUERY :" + query);
            stmt = conn.prepareStatement(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        int output = 0;
        try {
            //stmt.setString(1, name);
            //stmt.setString(2, pass);
            System.out.println("PreparedStatement.execute being called with Last='" + name + "' Password='" + pass +"'");
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
    
    public boolean PreparedStatementDirectParaIdentifier3(String name, String pass) {
    	Connection conn = connect();
    	if (null == name)
    	{name = "Shiva";}
    	if (null == pass)
    	{pass = "Prakash";}
        boolean hasResults = false;
        if (conn == null)
            return false;
        PreparedStatement stmt = null;
        try {
            String query = "SELECT FIRST,\"" +  name + "\" from CUSTOMERS WHERE LAST = \'" + pass + "\'";
            System.out.println("QUERY :" + query);
            stmt = conn.prepareStatement(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        int output = 0;
        try {
            //stmt.setString(1, name);
            //stmt.setString(2, pass);
            System.out.println("PreparedStatement.execute being called with Last='" + name + "' Password='" + pass +"'");
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
    
    public boolean PreparedStatementDirectPara(String name, String pass) {
    	Connection conn = connect();
    	if (null == name)
    	{name = "Shiva";}
    	if (null == pass)
    	{pass = "Prakash";}
        boolean hasResults = false;
        if (conn == null)
            return false;
        PreparedStatement stmt = null;
        try {
            String query = "SELECT FIRST, LAST from CUSTOMERS WHERE LAST = \'" + name + "\' AND PASSWORD = \'" + pass + "\'";
            System.out.println("QUERY :" + query);
            stmt = conn.prepareStatement(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        int output = 0;
        try {
            //stmt.setString(1, name);
            //stmt.setString(2, pass);
            System.out.println("PreparedStatement.execute being called with Last='" + name + "' Password='" + pass +"'");
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
    
    /**
     * Asynchronously executes the callable statement.
     *
     */
    public static class CallableStatementTask implements Callable<Boolean> {
        private PreparedStatement stmt;
        public CallableStatementTask(CallableStatement statement){
            stmt = statement;
        }
        
        public Boolean call() throws InvalidParameterException {
            boolean hasResults = false;
            int output = 0;
            try {
                System.out.println("CallableStatement.execute callable task");
                hasResults = stmt.execute();
                // Loop through the data and print all artist names
            } catch (Exception e) {
                System.out.println("Exception !");
                System.err.println(e.getMessage());
            } finally {
                try {
                    // Close connection
                    stmt.close();
                } catch (SQLException e) {
                    System.out.println("Exception 2");
                    System.err.println(e.getMessage());
                }
            }
            return hasResults;
        }
    }
    
    /**
     * Asynchronously executes the prepared statement.
     *
     */
    public static class PrepareStatementTask implements Callable<Boolean> {
        private PreparedStatement stmt;
        public PrepareStatementTask(PreparedStatement statement){
            stmt = statement;
        }
        
        public Boolean call() throws InvalidParameterException {
            boolean hasResults = false;
            int output = 0;
            try {
                System.out.println("PreparedStatement.execute callable task");
                hasResults = stmt.execute();
                // Loop through the data and print all artist names
            } catch (Exception e) {
                System.out.println("Exception !");
                System.err.println(e.getMessage());
            } finally {
                try {
                    // Close connection
                    stmt.close();
                } catch (SQLException e) {
                	System.out.println("Exception 2");
                    System.err.println(e.getMessage());
                }
            }
            return hasResults;
        }
    }
}
