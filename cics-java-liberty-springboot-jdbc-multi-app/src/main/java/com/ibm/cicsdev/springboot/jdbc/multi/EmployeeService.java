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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * Service class which retrieves the data requested by the REST controller
 *    makes use of two different jdbcTemplates (DataSources with type 2 and type 4 connectivity) 
 *    to retrieve the data from table EMP
 * 
 * @Autowired Marks a constructor, field, setter method, or config method as to be autowired by Spring's dependency injection facilities
 * @Service Marks a class as providing business logic
 */

@Service
public class EmployeeService 
{
	// The autowired JbdcTemplates are created from the Bean methods in Application.java
	// The Qualifiers tell Spring which Bean to inject.
	@Autowired
	@Qualifier("type2JdbcTemplate")
	private JdbcTemplate jdbcTemplateT2;
	
	@Autowired
	@Qualifier("type4JdbcTemplate")
	private JdbcTemplate jdbcTemplateT4;		

	// Create a timestamp (used when adding an Employee)
	private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd"); 
	private LocalDateTime now = LocalDateTime.now();
	
	
	// Based on the jdbcType string, select which jdbcTemplate to use
	private JdbcTemplate selectJdbcTemplate(String jdbcType)
	{
		if(jdbcType.equalsIgnoreCase("type2"))
		{
			return this.jdbcTemplateT2;
		}
		
		return this.jdbcTemplateT4;
	}
	
	
	
	/**
	 * Select all rows from the Employee table
	 * 
	 * @return a list of employees
	 * @throws NamingException
	 */
	public List<Employee> selectAll(String jdbcType) 
	{
		// The jdbcType (2 or 4) is provided in the URL from the user/browser.
		// We map it to the template DataSource which provides that capability.
		JdbcTemplate jdbcTemplate = selectJdbcTemplate(jdbcType);
		
		// setup the select SQL
		String sql = "SELECT * FROM emp";

		// run the query
		return jdbcTemplate.query(
				sql,
				(rs, rowNum) ->
				new Employee(
						rs.getString("EMPNO"),
						rs.getString("FIRSTNME"),
						rs.getString("MIDINIT"),
						rs.getString("LASTNAME"),
						rs.getString("WORKDEPT"),
						rs.getString("PHONENO"),
						rs.getDate("HIREDATE"),
						rs.getString("JOB"),
						rs.getInt("EDLEVEL"),
						rs.getString("SEX"),
						rs.getString("BIRTHDATE"),
						rs.getLong("SALARY"),
						rs.getLong("BONUS"),
						rs.getLong("COMM")));
	}

		
	/**
	 * @param empNo
	 * @return a list of employee records for a specific employee number
	 */
	public List<Employee> selectWhereEmpno(String jdbcType, String empNo) 
	{
		JdbcTemplate jdbcTemplate = selectJdbcTemplate(jdbcType);
		
		String sql = "SELECT * FROM emp where empno = ?";

		return jdbcTemplate.query(
				sql,
				new Object [] {empNo},
				(rs, rowNum) ->
				new Employee(
						rs.getString("EMPNO"),
						rs.getString("FIRSTNME"),
						rs.getString("MIDINIT"),
						rs.getString("LASTNAME"),
						rs.getString("WORKDEPT"),
						rs.getString("PHONENO"),
						rs.getDate("HIREDATE"),
						rs.getString("JOB"),
						rs.getInt("EDLEVEL"),
						rs.getString("SEX"),
						rs.getString("BIRTHDATE"),
						rs.getLong("SALARY"),
						rs.getLong("BONUS"),
						rs.getLong("COMM")));
	}


	/**
	 * @param fName - first name 
	 * @param lName - last name
	 * @return a string indicating the result of the add operation
	 */
	public String addEmployee(String jdbcType, String fName, String lName) 
	{
		JdbcTemplate jdbcTemplate = selectJdbcTemplate(jdbcType);
		
		// Firstname and lastname are passed in by the REST caller,
		// for demo purposes all the other fields are set by this method      	
		 
		// generate an empNo between 300000 and 999999
		int max = 999999;
		int min = 300000;
		String empno = String.valueOf((int) Math.round((Math.random()*((max-min)+1))+min));

		String midInit = "A";
		String workdept = "E21";
		String phoneNo = "1234";

		// get today's date and set as hiredate
		String hireDate= this.dtf.format(this.now);  

		String job = "Engineer";
		int edLevel =3 ;
		String sex ="M";
		String birthDate = "1999-01-01" ;
		long salary = 20000;
		long bonus= 1000;
		long comm = 1000;

		// setup the SQL
		String sql = "insert into emp (EMPNO, FIRSTNME, MIDINIT,LASTNAME,WORKDEPT,PHONENO,HIREDATE,JOB,EDLEVEL,SEX,BIRTHDATE,SALARY,BONUS,COMM) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

		// do the insert
		int numRows =  jdbcTemplate.update (sql,
				empno,
				fName,
				midInit,
				lName,
				workdept,
				phoneNo,
				hireDate,
				job,
				edLevel,
				sex,
				birthDate,
				salary,
				bonus,
				comm);

		// numRows is the number of rows inserted - will be zero if the insert fails
		if (numRows > 0) 
		{
			return "employee " + empno + " added";
		}
		
		return "employee insert failed try again";
	}


	/**
	 * @param empNo - employee number to be deleted
	 * @return - a message to indicate success or failure of the delete operation
	 */
	public String deleteEmployee(String jdbcType, String empNo)
	{
		JdbcTemplate jdbcTemplate = selectJdbcTemplate(jdbcType);
		
		// set up the delete SQL
		String sql = "DELETE FROM emp WHERE empno =?";

		// do the delete
		int numRows = jdbcTemplate.update(sql, empNo);

		// numRows is the number of rows deleted - will be zero if the delete fails
		if (numRows > 0) 
		{
			return "employee " + empNo + " deleted";
		}
		
		return "employee delete failed try again";
	}


	/**
	 * @param newSalary - update the employee record with this salary amount
	 * @param empNo - the employee number which is to be uodated
	 * @return a message to indicate success or failure of the update operation
	 */
	public String updateEmployee(String jdbcType, int newSalary, String empNo) 
	{
		JdbcTemplate jdbcTemplate = selectJdbcTemplate(jdbcType);
		
		// set up the update SQL
		String sql = "update emp set salary =? where empNo = ?";

		// do the update
		int numRows = jdbcTemplate.update(sql, newSalary, empNo);

		// numRows is the number of rows updated - will be zero if the update fails   
		if (numRows > 0) 
		{
			return "employee " + empNo + " salary changed to " + newSalary;
		}
		
		return "employee update failed try again";
	}
	
}
