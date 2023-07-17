package com.endor.wrapper;

import javax.servlet.*;
import java.io.IOException;
import java.io.PrintWriter;

public class WrapperFilter implements Filter
{

    public void init(FilterConfig filterConfig)
    {
    }

    public void destroy()
    {
    }

    //This method is called each time a client requests for a web resource i.e. preprocessing request
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException
    {
        response.setContentType("text/html");
        PrintWriter out = null;
        try {
            out = response.getWriter();
        } catch (IOException e) {
            e.printStackTrace();
        }

        out.println("<b> <i>Filtering request and passing it to Wrapper class</i> </b> <br/>");


        // Calling the constructor of request wrapper class
        RequestWrapper1 requestWrapper = new RequestWrapper1(request);


        // This method calls the next filter in the chain
        chain.doFilter(requestWrapper,response);
    }

}