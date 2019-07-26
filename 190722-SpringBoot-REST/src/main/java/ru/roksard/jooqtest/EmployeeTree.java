package ru.roksard.jooqtest;

import java.util.Arrays;

public class EmployeeTree {
	Employee boss;
	Employee employee;
	Employee[] childEmployeeList;
	
	public Employee getEmployee() {
		return employee;
	}
	public Employee[] getChildEmployeeList() {
		return childEmployeeList;
	}
	public Employee getBoss() {
		return boss;
	}
	public void setEmployee(Employee employee) {
		this.employee = employee;
	}
	public void setChildEmployeeList(Employee[] childEmployeeList) {
		this.childEmployeeList = childEmployeeList;
	}
	public void setBoss(Employee boss) {
		this.boss = boss;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((boss == null) ? 0 : boss.hashCode());
		result = prime * result + Arrays.hashCode(childEmployeeList);
		result = prime * result + ((employee == null) ? 0 : employee.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EmployeeTree other = (EmployeeTree) obj;
		if (boss == null) {
			if (other.boss != null)
				return false;
		} else if (!boss.equals(other.boss))
			return false;
		if (!Arrays.equals(childEmployeeList, other.childEmployeeList))
			return false;
		if (employee == null) {
			if (other.employee != null)
				return false;
		} else if (!employee.equals(other.employee))
			return false;
		return true;
	}
	public EmployeeTree(Employee boss, Employee employee, Employee[] childEmployeeList) {
		super();
		this.employee = employee;
		this.childEmployeeList = childEmployeeList;
		this.boss = boss;
	}
	
}
