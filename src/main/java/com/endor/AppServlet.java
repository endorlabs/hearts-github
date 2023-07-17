package com.endor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URL;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

@javax.servlet.annotation.WebServlet(name = "AppServlet", urlPatterns = "/AppServlet")
public class AppServlet extends javax.servlet.http.HttpServlet {
    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        //response.getWriter().println("Hello world");
        PrintWriter out = null;
        try {
            out = response.getWriter();
        } catch (Exception e) {
            e.printStackTrace();
        }
        HtmlUtil.printHtmlHeader(response);
        HtmlUtil.startBody(response);
        HtmlUtil.printMenu(response);
        HtmlUtil.printCurrentTitle("SSRF", response);

        String form = "<form action=\"ssrf\">" +
                "URL: <input type=\"text\" name=\"ssrf\" id=\"ssrf\"> -- (If ssrf=file then inputs will be parsed from the file /opt/ssrfinput.txt)<br><br>" +
                "Https URL: <input type=\"text\" name=\"httpsssrf\" id=\"httpsssrf\"><br><br>" +
                "<input type=\"submit\" value=\"Submit\">" + "</form>";
        out.println(form);


        String loopback =  request.getParameter("isloopback");
        String ssrfUrl = request.getParameter("ssrf");
        String httpsssrfUrl = request.getParameter("httpsssrf");
        
        System.out.printf("loopback : %s\n",loopback);
        System.out.printf("ssrfUrl : %s\n",ssrfUrl);
        System.out.printf("httpsssrfUrl : %s\n",httpsssrfUrl);
        
        if (loopback == null && ssrfUrl.equalsIgnoreCase("file")) {
        	BufferedReader reader = null;
        	try {
        			reader = new BufferedReader(new FileReader("/opt/ssrfinput.txt"));
        			System.out.println("ssrfinput.txt file opened successfully");
        		}
        	catch (IOException e) {
        					System.out.println("Failed to open Input file");
        					e.printStackTrace();
        			}
        	        try {
        			    	String line = reader.readLine();
        			    	while (null != line) {
        			    		System.out.println("SSRF being called with :" + line);
        			    		UseUrlOpenConnection(request, response, line);
        			    		line = reader.readLine();
        			    		Thread.sleep(2000);
        			    	}
        			    	reader.close();
        	        	}
        			   catch (Exception ex){
        				   ex.getStackTrace();
        			   }        	
        } else if(loopback == null && ssrfUrl !=null && ssrfUrl.length() > 0) {
            UseUrlOpenConnection(request, response, ssrfUrl);
//            String countStr =  request.getParameter("loop");
//            int count =  Integer.parseInt(countStr);
//            for (int i =0; i< count;i++) {
//                restCall(request, response, i);
//            }
        } else if (loopback == null && 0 == httpsssrfUrl.toUpperCase().indexOf("HTTPS://")) {
        	System.out.println("Inside https://, calling UseUrlOpenConnectionhttps()");
        	UseUrlOpenConnectionhttps(request, response, httpsssrfUrl);
        	
        }
        
        System.out.println("Executed URLOpen");

    }

    public void UseUrlOpenConnection(javax.servlet.http.HttpServletRequest request,
                                     javax.servlet.http.HttpServletResponse response, String ssrfURL) throws javax.servlet.ServletException, IOException {
        try {
            response.getWriter().println("Inside Url.openStream");
            String url  = "https://www.oracle.com/";
            if (ssrfURL != null && ssrfURL.length() > 0) {
                url = ssrfURL;
            }
            URL oracle = new URL(url);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(oracle.openStream()));

            String inputLine;
            while ((inputLine = in.readLine()) != null){
                System.out.println(inputLine);
                response.getWriter().print(inputLine);}
            in.close();
        } catch (Exception e) {
            response.getWriter().println("Exception!!");
            response.getWriter().print(e.getMessage());

        }
    }

    public void UseUrlOpenConnectionhttps(javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response, String ssrfURL) throws javax.servlet.ServletException, IOException {
    	
    	String hostname = "www.verisign.com";
        

        String hostname2 = "time.nist.gov";
        
        String UrlToOpen = ssrfURL.replaceFirst("HTTPS://", "");
        UrlToOpen = UrlToOpen.replaceFirst("https://", "");
        
        try {
        	System.out.printf("Opening SSL socket for host : %s\n", UrlToOpen);
            SSLSocketFactory factory =
                    (SSLSocketFactory)SSLSocketFactory.getDefault();
            SSLSocket socket =
                    (SSLSocket)factory.createSocket(UrlToOpen, 443);

            /*
             * send http request
       
             */
            socket.startHandshake();

            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));

            out.println("GET / HTTP/1.0");
            out.println();
            out.flush();

            /*
             * Make sure there were no surprises
             */
            if (out.checkError())
                System.out.println(
                        "SSLSocketClient:  java.io.PrintWriter error");

            /* read response */
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            socket.getInputStream()));

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println(inputLine);
                response.getWriter().print(inputLine);
            }
            in.close();
            out.close();
            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}