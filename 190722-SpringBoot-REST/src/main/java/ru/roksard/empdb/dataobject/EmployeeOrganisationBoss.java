package ru.roksard.empdb.dataobject;

public class EmployeeOrganisationBoss {
	Employee employee;
	Organisation organisation;
	Employee boss;
	public Employee getEmployee() {
		return employee;
	}
	public Organisation getOrganisation() {
		return organisation;
	}
	public Employee getBoss() {
		return boss;
	}
	public void setEmployee(Employee employee) {
		this.employee = employee;
	}
	public void setOrganisation(Organisation organisation) {
		this.organisation = organisation;
	}
	public void setBoss(Employee boss) {
		this.boss = boss;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((boss == null) ? 0 : boss.hashCode());
		result = prime * result + ((employee == null) ? 0 : employee.hashCode());
		result = prime * result + ((organisation == null) ? 0 : organisation.hashCode());
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
		EmployeeOrganisationBoss other = (EmployeeOrganisationBoss) obj;
		if (boss == null) {
			if (other.boss != null)
				return false;
		} else if (!boss.equals(other.boss))
			return false;
		if (employee == null) {
			if (other.employee != null)
				return false;
		} else if (!employee.equals(other.employee))
			return false;
		if (organisation == null) {
			if (other.organisation != null)
				return false;
		} else if (!organisation.equals(other.organisation))
			return false;
		return true;
	}
	public EmployeeOrganisationBoss(Employee employee, Organisation organisation, Employee boss) {
		super();
		this.employee = employee;
		this.organisation = organisation;
		this.boss = boss;
	}
}
