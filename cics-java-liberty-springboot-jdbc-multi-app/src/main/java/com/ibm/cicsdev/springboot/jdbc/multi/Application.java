/* Licensed Materials - Property of IBM                                   */
/*                                                                        */
/* SAMPLE                                                                 */
/*                                                                        */
/* (c) Copyright IBM Corp. 2020 All Rights Reserved                       */
/*                                                                        */
/* US Government Users Restricted Rights - Use, duplication or disclosure */
/* restricted by GSA ADP Schedule Contract with IBM Corp                  */
/*                                                                        */

package com.ibm.cicsdev.springboot.jdbc.multi; 

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;

/**
 * 
 * Spring boot application entry-point (including main method and @SpringBootApplication annotation).
 * 
 * The @SpringBootApplication annotation is equivalent to:
 *   
 *   @EnableAutoConfiguration: enable Spring Boot’s auto-configuration mechanism
 *   @ComponentScan: scan all the beans and package declarations when the application initializes.
 *   @Configuration: allow to register extra beans in the context or import additional configuration classes
 * 
 */
@SpringBootApplication
public class Application 
{
	// Helper Class to lookup our DataSources from JNDI (Liberty server.xml)
	JndiDataSourceLookup dataSourceLookup = new JndiDataSourceLookup();
		
	
	/**
	 * @param args - inputs
	 */
	public static void main(String args[]) 
	{
		SpringApplication.run(Application.class, args);
	}
		
	
	// Method to create a DataSource Bean from the dataSource JNDI URL
	@Bean	
    public DataSource getType2DataSource() 
    {    	    			
    	//return this.dataSourceLookup.getDataSource(type2JNDIName);    	
    	return this.dataSourceLookup.getDataSource("jdbc/t2DataSource");
    }
	
	
	// Method to create a DataSource Bean from the dataSource JNDI URL
	@Bean	
	public DataSource getType4DataSource() 
	{    	    			
	   	//return this.dataSourceLookup.getDataSource(type4JNDIName);    	
	   	return this.dataSourceLookup.getDataSource("jdbc/t4DataSource");
	}
    
	
    // JDBC Template bean using dataSource for type 2 connectivity (native, DB2CONN)
    @Bean
    @Qualifier("type2JdbcTemplate")
    public JdbcTemplate getType2JdbcTemplate() 
    {
    	return new JdbcTemplate(getType2DataSource());
    }

    
    // JDBC Template bean using dataSource for type 4 connectivity (Java based, TCP/IP)
    @Bean	    
    @Qualifier("type4JdbcTemplate")
    public JdbcTemplate getType4JdbcTemplate() 
    {
    	return new JdbcTemplate(getType4DataSource());    	
    }        

}
