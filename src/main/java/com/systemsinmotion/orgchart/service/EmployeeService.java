package com.systemsinmotion.orgchart.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.mysema.query.types.expr.BooleanExpression;
import com.systemsinmotion.orgchart.data.EmployeeRepository;
import com.systemsinmotion.orgchart.entity.Employee;
import com.systemsinmotion.orgchart.entity.QEmployee;

@SuppressWarnings("restriction")
@Service("EmployeeService")
public class EmployeeService {

	@Autowired
	private EmployeeRepository repository;

	public List<Employee> findAllEmployees() {
		// TODO Auto-generated method stub
		return this.repository.findAll();
	}

	public List<Employee> findByJobTitleName(String title) {
		return this.repository.findByJobTitleName(title);
	}

	public Employee findEmployeeByID(Integer employeeId) {
		// TODO Auto-generated method stub
		return this.repository.findOne(employeeId);
	}

	public Employee storeEmployee(Employee employee) {
		// TODO Auto-generated method stub
		employee.setIsActive(true);
		return this.repository.save(employee);
	}

	public Employee updateEmployee(Employee employee) {
		return this.repository.save(employee);
	}

	public List<Employee> activeEmployees() {
		return this.repository.findByIsActiveIsTrue();
	}

	public List<Employee> filterEmployees(String firstName, String lastName,
			String department, String title) {
		QEmployee employee = QEmployee.employee;

		BooleanExpression expression = employee.isActive.eq(true);

		if (!firstName.equals(" ")) {
			BooleanExpression firstNameExpression = null;
			if (lastName.equals(" ")) {
				firstNameExpression = (employee.firstName.eq(firstName))
						.or(employee.lastName.eq(firstName));
			} else {
				firstNameExpression = (employee.firstName.eq(firstName));
			}
			expression = expression.and(firstNameExpression);
		}
		if (!lastName.equals(" ")) {
			BooleanExpression lastNameExpression = employee.lastName
					.eq(lastName);
			expression = expression.and(lastNameExpression);
		}

		if (!department.equals("")) {
			BooleanExpression departmentExpression = employee.department.id
					.eq(Integer.parseInt(department));
			expression = expression.and(departmentExpression);
		}

		if (!title.equals("")) {
			BooleanExpression jobTitleExpression = employee.jobTitle.id
					.eq(Integer.parseInt(title));
			expression = expression.and(jobTitleExpression);
		}

		List<Employee> employees = (ArrayList<Employee>) repository
				.findAll(expression);

		return employees;

	}

	public String autoComplete(String name) {
		QEmployee employee = QEmployee.employee;
		BooleanExpression expression = employee.firstName
				.containsIgnoreCase(name)
				.or(employee.lastName.containsIgnoreCase(name))
				.and(employee.isActive.eq(true));

		Gson json = new Gson();
		List<Employee> employees = (ArrayList<Employee>) repository
				.findAll(expression);

		return json.toJson(employees);

	}

}