package ru.roksard.jooqtest;

public class OrganisationEmployeeCount {
	private Organisation organisation;
	private int employeeCount = 0;
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + employeeCount;
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
		OrganisationEmployeeCount other = (OrganisationEmployeeCount) obj;
		if (employeeCount != other.employeeCount)
			return false;
		if (organisation == null) {
			if (other.organisation != null)
				return false;
		} else if (!organisation.equals(other.organisation))
			return false;
		return true;
	}
	public Organisation getOrganisation() {
		return organisation;
	}
	public int getEmployeeCount() {
		return employeeCount;
	}
	
	public OrganisationEmployeeCount() {	}
	public OrganisationEmployeeCount(Organisation organisation, int employeeCount) {
		super();
		this.organisation = organisation;
		this.employeeCount = employeeCount;
	}
	public void setOrganisation(Organisation organisation) {
		this.organisation = organisation;
	}
	public void setEmployeeCount(int employeeCount) {
		this.employeeCount = employeeCount;
	}
	@Override
	public String toString() {
		return "OrganisationEmployeeCount [organisation=" + organisation + ", employeeCount=" + employeeCount + "]";
	}
	
}
