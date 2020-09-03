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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


/**
 * 
 * Employee REST controller
 * 
 * A REST controller used to direct incoming REST requests to the correct business service.
 *  
 * In a real world application some of these functions would most likely be done by a POST
 * request. For simplicity all requests to this sample application are done with a GET request
 */
@RestController
public class EmployeeRestController
{	
	@Autowired  
	private EmployeeService employeeService;
	
		
	/**
	 * Root endpoint
	 * 
	 * @return Date/time and usage information 
	 */
	@GetMapping("/")
	@ResponseBody
	public String Index()
	{    
		Date myDate = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd:HH-mm-ss.SSSSSS");
		String myDateString = sdf.format(myDate);
		
		return "<h1>Spring Boot JDBC Employee REST sample (Multiple DataSources). Date/Time: " + myDateString + "</h1>"
		+ "<h3>Usage: http://<server>:<port>/application-root/...</h3>"
		+ "<b>/type2/allEmployees</b> - return a list of employees using a classic SELECT statement<br>"
		+ "<b>/type2/listEmployee/{empno}</b> - a list of employee records for the employee number provided<br>"
		+ "<br> --- Update operations --- <br>"
		+ "<b>/type2/addEmployee/{firstName}/{lastName}</b> - add an employee<br>"				
		+ "<b>/type2/deleteEmployee/{empNo}</b> - delete an employee<br>"
		+ "<b>/type2/updateEmployee/{empNo}/{newSalary}</b> - update employee salary"
		+ "<br> --- Update operations within a Global (XA) Transaction --- <br>"
		+ "<b>/type2/addEmployeeTx/{firstName}/{lastName}</b> - add an employee<br>"				
		+ "<b>/type2/deleteEmployeeTx/{empNo}</b> - delete an employee<br>"
		+ "<b>/type2/updateEmployeeTx/{empNo}/{newSalary}</b> - update employee salary"
		+ "<br>"
		+ "<b>/type4/allEmployees</b> - return a list of employees using a classic SELECT statement<br>"
		+ "<b>/type4/listEmployee/{empno}</b> - a list of employee records for the employee number provided<br>"
		+ "<br> --- Update operations --- <br>"
		+ "<b>/type4/addEmployee/{firstName}/{lastName}</b> - add an employee<br>"				
		+ "<b>/type4/deleteEmployee/{empNo}</b> - delete an employee<br>"
		+ "<b>/type4/updateEmployee/{empNo}/{newSalary}</b> - update employee salary"
		+ "<br> --- Update operations within a Global (XA) Transaction --- <br>"
		+ "<b>/type4/addEmployeeTx/{firstName}/{lastName}</b> - add an employee<br>"				
		+ "<b>/type4/deleteEmployeeTx/{empNo}</b> - delete an employee<br>"
		+ "<b>/type4/updateEmployeeTx/{empNo}/{newSalary}</b> - update employee salary";
	}

	
	/**
	 * Show all Employees
	 * 
	 * @return a list of employees
	 */
	@GetMapping({"/{jdbcType}/allEmployees","/{jdbcType}/allEmployees/"})
	public List<Employee> getAllEmployees(@PathVariable String jdbcType) 
	{
		return this.employeeService.selectAll(jdbcType);
	}

	
	/**
	 * List one Employee
	 * 
	 * @param empno - employee number
	 * @return a list of employee records for the passed parameter number
	 */
	@GetMapping("/{jdbcType}/listEmployee/{empno}")
	public List<Employee> listEmployee(@PathVariable String jdbcType, @PathVariable String empno) 
	{
		return this.employeeService.selectWhereEmpno(jdbcType, empno);
	}
	
	
	/**
	 * Add an Employee
	 * 
	 * @param firstName - employee first name
	 * @param lastName - employee last name
	 * @return a message indicating success or failure of the add operation
	 */
	@GetMapping("/{jdbcType}/addEmployee/{firstName}/{lastName}")
	@ResponseBody
	public String addEmp(@PathVariable String jdbcType, @PathVariable String firstName , @PathVariable String lastName) 
	{
		String result = this.employeeService.addEmployee(jdbcType, firstName,lastName);
		return result;
	}

	
	/**  
	 * Add an employee within a Global (XA) transaction
	 * Note the @Transactional annotation.
	 * 
	 * @param firstName - employee first name
	 * @param lastName - employee last name
	 * @return a message indicating success or failure of the add operation
	 */
	@GetMapping("/{jdbcType}/addEmployeeTx/{firstName}/{lastName}")
	@ResponseBody
	@Transactional
	public String addEmpTx(@PathVariable String jdbcType, @PathVariable String firstName , @PathVariable String lastName) 
	{
		String result = this.employeeService.addEmployee(jdbcType, firstName,lastName);
		return result;
	}

		
	/**
	 * Delete an Employee
	 *  
	 * @param empNo - employee number to be deleted
	 * @return a message indicating success or failure of the delete operation
	 */
	@GetMapping("/{jdbcType}/deleteEmployee/{empNo}")
	@ResponseBody	
	public String delEmployee(@PathVariable String jdbcType, @PathVariable String empNo) 
	{
		String result = this.employeeService.deleteEmployee(jdbcType, empNo);
		return result;
	}
	
		
	/**
	 * Delete an Employee within a Global (XA) transaction
	 * Note the @Transactional annotation.
	 *  
	 * @param empNo - employee number to be deleted
	 * @return a message indicating success or failure of the delete operation (XA)
	 */
	@GetMapping("/{jdbcType}/deleteEmployeeTx/{empNo}")
	@ResponseBody
	@Transactional
	public String delEmployeeTx(@PathVariable String jdbcType, @PathVariable String empNo) 
	{
		String result = this.employeeService.deleteEmployee(jdbcType, empNo);
		return result;
	}

		
	/**
	 * Update the salary of an Employee
	 * 
	 * @param empNo - employee number to be updated
	 * @param newSalary - the new salary to be given to the employee
	 * @return a message indicating success or failure of the update operation
	 */
	@GetMapping("/{jdbcType}/updateEmployee/{empNo}/{newSalary}")
	@ResponseBody
	public String updateEmp(@PathVariable String jdbcType, @PathVariable String empNo, @PathVariable int newSalary) 
	{
		String result = this.employeeService.updateEmployee(jdbcType, newSalary, empNo);
		return result;
	}
	
	
	/**
	 * Update the Salary of an Employee within a Global (XA) transaction
	 * Note the @Transactional annotation.
	 * 
	 * @param empNo - employee number to be updated
	 * @param newSalary - the new salary to be given to the employee
	 * @return a message indicating success or failure of the update operation (XA)
	 */
	@GetMapping("/{jdbcType}/updateEmployeeTx/{empNo}/{newSalary}")
	@ResponseBody
	@Transactional
	public String updateEmpTx(@PathVariable String jdbcType, @PathVariable String empNo, @PathVariable int newSalary) 
	{
		String result = this.employeeService.updateEmployee(jdbcType, newSalary, empNo);
		return result;
	}
	
}
