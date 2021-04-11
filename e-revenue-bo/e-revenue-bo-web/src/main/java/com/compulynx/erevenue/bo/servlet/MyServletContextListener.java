/**
 * 
 */
package com.compulynx.erevenue.bo.servlet;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.sql.DataSource;

/**
 * @author Anita
 * @date May 4, 2016
 *
 */
public class MyServletContextListener implements ServletContextListener {
	@Resource(name="jdbc/erevenueDS")
	  private DataSource ds;

	  @Override
	  public void contextInitialized(ServletContextEvent servletContextEvent) {
	    System.out.println("contextInitialized");
	    ServletContext context = servletContextEvent.getServletContext();
	    context.setAttribute("ds",ds);
	  }

	  @Override
	  public void contextDestroyed(ServletContextEvent servletContextEvent) {
	    System.out.println("contextDestroyed");

	  }
}
