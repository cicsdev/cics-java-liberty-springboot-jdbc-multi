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
	private T4EmployeeService type4EmployeeService;
	
	@Autowired  
	private T2EmployeeService type2EmployeeService;

	
	/**
	 * Simple endpoint - returns date and time - simple test of the application
	 * 
	 * @return  a Hello message 
	 */
	@GetMapping("/")
	@ResponseBody
	public String Index()
	{    
		Date myDate = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd:HH-mm-ss.SSSSSS");
		String myDateString = sdf.format(myDate);
		
		return "<h1>Spring Boot JDBC Employee REST sample (Multiple DataSources). Date/Time: " + myDateString + "</h1>"
		+ "<h3>Usage:</h3>"
		+ "<b>/type2/allEmployees</b> - return a list of employees using a classic SELECT statement<br>"
		+ "<b>/type2/listEmployee/{empno}</b> - a list of employee records for the employee number provided<br>"
		+ "<b>/type2/addEmployee/{firstName}/{lastName}</b> - add an employee<br>"				
		+ "<b>/type2/deleteEmployee/{empNo}</b> - delete an employee<br>"
		+ "<b>/type2/updateEmployee/{empNo}/{newSalary}</b> - update employee salary"
		+ "<b>"
		+ "<b>/type4/allEmployees</b> - return a list of employees using a classic SELECT statement<br>"
		+ "<b>/type4/listEmployee/{empno}</b> - a list of employee records for the employee number provided<br>"
		+ "<b>/type4/addEmployee/{firstName}/{lastName}</b> - add an employee<br>"				
		+ "<b>/type4/deleteEmployee/{empNo}</b> - delete an employee<br>"
		+ "<b>/type4/updateEmployee/{empNo}/{newSalary}</b> - update employee salary";
	}

	
	/**
	 *  example url http://<server>:<port>/type2/allEmployees
	 *  
	 * @return a list of employees
	 */
	@GetMapping({"/type2/allEmployees","/type2/allEmployees/"})
	public List<Employee> getAllEmployees() 
	{
		return type2EmployeeService.selectAll();
	}

	
	/**
	 * example url http://<server>:<port>/type2/listEmployee/000100
	 * 
	 * @param empno - employee number
	 * @return a list of employee records for the passed parameter number
	 */
	@GetMapping("/type2/listEmployee/{empno}")
	public List<Employee> listEmployee(@PathVariable String empno) 
	{
		return type2EmployeeService.selectWhereEmpno(empno);
	}
	
	
	/**
	 *  example url http://<server>:<port>/type2/addEmployee/Bugs/Bunny
	 *  
	 * @param firstName - employee first name
	 * @param lastName - employee last name
	 * @return a message indicating success or failure of the add operation
	 */
	@GetMapping("/type2/addEmployee/{firstName}/{lastName}")
	@ResponseBody
	public String addEmp(@PathVariable String firstName , @PathVariable String lastName) 
	{
		String result = type2EmployeeService.addEmployee(firstName,lastName);
		return result;
	}

	
	/**
	 *  example url http://<server>:<port>/type2/addEmployeeTx/Roger/Rabbit
	 *  
	 * @param firstName - employee first name
	 * @param lastName - employee last name
	 * @return a message indicating success or failure of the add operation
	 */
	@GetMapping("/type2/addEmployeeTx/{firstName}/{lastName}")
	@ResponseBody
	@Transactional
	public String addEmpTx(@PathVariable String firstName , @PathVariable String lastName) 
	{
		String result = type2EmployeeService.addEmployee(firstName,lastName);
		return result;
	}

		
	/**
	 *  example url http://<server>:<port>/type2/deleteEmployee/368620
	 *  
	 * @param empNo - employee number to be deleted
	 * @return a message indicating success or failure of the delete operation
	 */
	@GetMapping("/type2/deleteEmployee/{empNo}")
	@ResponseBody
	public String delEmployee(@PathVariable String empNo) 
	{
		String result = type2EmployeeService.deleteEmployee(empNo);
		return result;
	}
	
	
	/**
	 * example url http://<server>:<port>/type2/updateEmployee/368620/33333
	 * 
	 * @param empNo - employee number to be updated
	 * @param newSalary - the new salary to be given to the employee
	 * @return a message indicating success or failure of the update operation
	 */
	@GetMapping("/type2/updateEmployee/{empNo}/{newSalary}")
	@ResponseBody
	public String updateEmp(@PathVariable String empNo, @PathVariable int newSalary) 
	{
		String result = type2EmployeeService.updateEmployee(newSalary, empNo);
		return result;
	}
	
}
