package com.endor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.lang.reflect.Field;

public class HttpURLConnectionExample {

    private static final String USER_AGENT = "Mozilla/5.0";

    private static final String GET_URL = "http://localhost:8080";

    //private static final String POST_URL = "http://localhost:9090/SpringMVCExample/home";
    private static final String POST_URL = "http://localhost:8080/endor-webapp/ExtraServlet";

    private static final String POST_PARAMS = "userName=Pankaj";

    public static void main(String[] args) throws IOException {

        sendGET();
        System.out.println("GET DONE");
       // sendPOST();
        System.out.println("POST DONE");
    }

    public static String sendGET() throws IOException {
        URL obj = new URL(GET_URL);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", USER_AGENT);
        int responseCode = con.getResponseCode();
        System.out.println("GET Response Code :: " + responseCode);
        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // print result
            System.out.println(response.toString());
            return response.toString();
            
        } else {
            System.out.println("GET request fa worked");
            return "Request Failed!!";
        }

    }   

    public static void sendPOST() throws IOException {
        URL obj = new URL(POST_URL);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);

        // For POST only - START
        con.setDoOutput(true);
        OutputStream os = con.getOutputStream();
        os.write(POST_PARAMS.getBytes());
        os.flush();
        os.close();
        // For POST only - END

        int responseCode = con.getResponseCode();
        System.out.println("POST Response Code :: " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) { //success
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // print result
            System.out.println(response.toString());
        } else {
            System.out.println("POST request not worked");
        }
    }

    public static void sendPOSTwithParameter(String last, String pass, String multileg) throws IOException {
    	
        URL obj = new URL(POST_URL);

        // For POST only - START
        String POST_PARAMS = "last="+last+"&pass="+pass+"&multileg="+multileg;
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setDoOutput(true);
        OutputStream os = con.getOutputStream();
        os.write(POST_PARAMS.getBytes());
        os.flush();
        os.close();
        // For POST only - END

        int responseCode = con.getResponseCode();
        System.out.println("POST Response Code :: " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) { //success
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // print result
            System.out.println(response.toString());
        } else {
            System.out.println("POST request not worked");
        }
    }

    public static int sendPOSTwithParameter(String last, String pass, String multileg, String url) throws IOException {

        URL obj = new URL(url);

        // For POST only - START
        String POST_PARAMS = "last="+last+"&pass="+pass+"&multileg="+multileg;
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setDoOutput(true);
        OutputStream os = con.getOutputStream();
        os.write(POST_PARAMS.getBytes());
        os.flush();
        os.close();
        // For POST only - END

        int responseCode = con.getResponseCode();
        System.out.println("POST Response Code :: " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) { //success
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // print result
            // System.out.println(response.toString());
        } else {
            System.out.println("POST request not worked");
        }
        return responseCode;
    }
    
    public static String sendTRACE() throws IOException {
    	String TRACE_URL = "http://localhost:8080/endor-webapp/httptrace";
        URL obj = new URL(TRACE_URL);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("TRACE");
        
        /*
        final Object target;
        try {
            
            if (con instanceof HttpURLConnection) {
                Field delegate = HttpURLConnection.class.getDeclaredField("delegate");
                delegate.setAccessible(true);
                target = delegate.get(con);
            } else {
                target = con;
            }
            final Field f = HttpURLConnection.class.getDeclaredField("method");
            f.setAccessible(true);
            f.set(target, "TRACE");
        } catch (IllegalAccessException | NoSuchFieldException ex) {
            throw new AssertionError(ex);
        }*/
        
        con.setRequestProperty("User-Agent", USER_AGENT);
        int responseCode = con.getResponseCode();
        System.out.println("TRACE Response Code :: " + responseCode);
        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // print result
            System.out.println(response.toString());
            return response.toString();
            
        } else {
            System.out.println("TRACE request fa worked");
            return "Request Failed!!";
        }

    }
    
    private void setRequestMethod(final HttpURLConnection c, final String value) {
        
    }

}
