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


import javax.xml.XMLConstants;
import javax.xml.parsers.*;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.traversal.DocumentTraversal;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.NodeIterator;
import org.xml.sax.SAXException;

// TODO rename to SQLServlet
@WebServlet(name = "XmlXXE")
@MultipartConfig(fileSizeThreshold=1024*1024*10, 	// 10 MB
        maxFileSize=1024*1024*50,      	// 50 MB
        maxRequestSize=1024*1024*100)

public class XmlXXE extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
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
            out.println("<h1>" + "XML XXE Test" + "</h1>");
            String form = "<form action=\"xmlxxe\" method=\"post\" enctype=\"multipart/form-data\">" +
                    "<label for=\"inputfile\">Select a file: </label>"+
                    "<input type=\"file\" id=\"inputfile\" name=\"inputfile\"><br><br>"+
                    "<input type=\"submit\" value=\"Submit\">" + "</form>";
            out.println(form);
            out.println("Submitted");

            InputStream inStr = request.getPart("inputfile").getInputStream();
            byte byteArray[] = new byte[inStr.available()];
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

//            System.out.println("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
//            System.out.println("Check the properties in the environment");
//            System.out.println("javax.xml.accessExternalSchema - " + System.getProperty("javax.xml.accessExternalSchema"));
//            System.out.println("javax.xml.accessExternalDTD - " + System.getProperty("javax.xml.accessExternalDTD"));

//            System.out.println("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
//            System.out.println("Set the properties in the environment");
//            System.setProperty("javax.xml.accessExternalSchema", "http");
//            System.setProperty("javax.xml.accessExternalDTD", "http");

//            System.out.println("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
//            System.out.println("Check the properties in the environment");
//            System.out.println("javax.xml.accessExternalSchema - " + System.getProperty("javax.xml.accessExternalSchema"));
//            System.out.println("javax.xml.accessExternalDTD - " + System.getProperty("javax.xml.accessExternalDTD"));
//
//            System.out.println("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
//            System.out.println("Check the properties in the DocumentBuilderFactory.getAttribute method");
//            System.out.println("XMLConstants.ACCESS_EXTERNAL_SCHEMA - " + factory.getAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA));
//            System.out.println("XMLConstants.ACCESS_EXTERNAL_DTD - " + factory.getAttribute(XMLConstants.ACCESS_EXTERNAL_DTD));

//            System.out.println("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
//            System.out.println("Set the properties in the DocumentBuilderFactory.setAttribute method");
//            factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
//            factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");

            System.out.println("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
            System.out.println("Check the properties in the environment");
            System.out.println("javax.xml.accessExternalSchema - " + System.getProperty("javax.xml.accessExternalSchema"));
            System.out.println("javax.xml.accessExternalDTD - " + System.getProperty("javax.xml.accessExternalDTD"));

            System.out.println("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
            System.out.println("Check the properties in the DocumentBuilderFactory.getAttribute method");
            System.out.println("XMLConstants.ACCESS_EXTERNAL_DTD - " + factory.getAttribute(XMLConstants.ACCESS_EXTERNAL_DTD));
            System.out.println("XMLConstants.ACCESS_EXTERNAL_SCHEMA - " + factory.getAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA));
/*
            System.out.println("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
            System.out.println("Set the properties in the DocumentBuilderFactory.setAttribute method");
            factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "file");
            factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "file");

            System.out.println("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
            System.out.println("Check the properties in the environment");
            System.out.println("javax.xml.accessExternalSchema - " + System.getProperty("javax.xml.accessExternalSchema"));
            System.out.println("javax.xml.accessExternalDTD - " + System.getProperty("javax.xml.accessExternalDTD"));

            System.out.println("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
            System.out.println("Check the properties in the DocumentBuilderFactory.getAttribute method");
            System.out.println("XMLConstants.ACCESS_EXTERNAL_DTD - " + factory.getAttribute(XMLConstants.ACCESS_EXTERNAL_DTD));
            System.out.println("XMLConstants.ACCESS_EXTERNAL_SCHEMA - " + factory.getAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA));
*/

            DocumentBuilder parser = factory.newDocumentBuilder();
            System.out.println("before calling DocumentBuilder.parse(InputStream is)");
            Document document = parser.parse(inStr);
            System.out.println("After calling DocumentBuilder.parse(InputStream is)");
            DocumentTraversal trav = (DocumentTraversal) document;
            NodeIterator it = trav.createNodeIterator(document.getDocumentElement(),
                    NodeFilter.SHOW_ELEMENT, null, true);
            for (Node node = it.nextNode(); node != null;
                 node = it.nextNode()) {
                String name = node.getNodeName();
                NodeList nodelist = node.getChildNodes();
                Node firstn = nodelist.item(0);
                System.out.printf("%s\n", name);
                System.out.println(firstn.getNodeValue());
                out.println("<h4>" + name + "</h4>");
                out.println(firstn.getNodeValue());
            }
            System.out.println("Done");
            out.println("</body>");
            out.println("</html>");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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
        out.println("<h1>" + "XML XXE Test" + "</h1>");
        String form = "<form action=\"xmlxxe\" method=\"post\" enctype=\"multipart/form-data\">" +
                "<label for=\"inputfile\">Select a file: </label>"+
                "<input type=\"file\" id=\"inputfile\" name=\"inputfile\"><br><br>"+
                "<input type=\"submit\" value=\"Submit\">" + "</form>";
        out.println(form);
        out.println("</body>");
        out.println("</html>");
    }
}
