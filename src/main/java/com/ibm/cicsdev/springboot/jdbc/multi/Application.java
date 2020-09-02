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
import org.springframework.beans.factory.annotation.Value;
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
	/**
	 * @param args - inputs
	 */
	public static void main(String args[]) 
	{
		SpringApplication.run(Application.class, args);
	}
	
	// Helper Class to lookup our DataSources from JNDI (Liberty server.xml)
	JndiDataSourceLookup dataSourceLookup = new JndiDataSourceLookup();
	
	// Custom values in application.properties that provide the JNDI DataSource URLs
	@Value("${spring.type2.datasource.jndi-name}")
	private String type2JNDIName;
	
	@Value("${spring.type4.datasource.jndi-name}")
	private String type4JNDIName;
		
	
	// Method to create a DataSource object from the URL
	@Bean
    public DataSource getDataSource(String jndiName) 
    {    	    			
    	return this.dataSourceLookup.getDataSource(jndiName);    	
    }
    
    // JDBC Template for type 2 connectivity
    @Bean
    @Qualifier("type2JdbcTemplate")
    public JdbcTemplate getType2JdbcTemplate() 
    {
    	return new JdbcTemplate(getDataSource(this.type2JNDIName));
    }

    // JDBC Template for type 4 connectivity
    @Bean
    @Qualifier("type4JdbcTemplate")
    public JdbcTemplate getType4JdbcTemplate() 
    {
    	return new JdbcTemplate(getDataSource(this.type4JNDIName));
    }
               
}
