package com.endor;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet Filter implementation class CSPFilter
 */
@WebFilter("/CSPFilter")
public class CSPFilter implements Filter {

    /**
     * Default constructor. 
     */
    public CSPFilter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		// place your code here

		// pass the request along the filter chain
		
		PrintWriter out=servletResponse.getWriter();  
		
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		response.setContentType("text/plain");
		response.addHeader("X-Content-Type-Options", "nosniff");
		chain.doFilter(servletRequest, response);
		out.print("filter is invoked to add X-Content-Type-Options:nosniff");  
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
