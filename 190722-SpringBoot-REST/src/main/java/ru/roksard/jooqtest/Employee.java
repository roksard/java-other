package ru.roksard.jooqtest;

public class Employee {
	private int id = 0;
	private String name = "";
	private int parentId = 0;
	private int organisationId = 0;
	
	public Employee () {}
	
	public Employee(int id, String name, int parentId, int organisationId) {
		this.id = id;
		this.name = name;
		this.parentId = parentId;
		this.organisationId = organisationId;
	}
	
	public Employee(String name, int parentId, int organisationId) {
		this.id = id;
		this.name = name;
		this.parentId = parentId;
		this.organisationId = organisationId;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	/**
	 * @return Id of boss of this employee 
	 */
	public int getParentId() {
		return parentId;
	}

	public int getOrganisationId() {
		return organisationId;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	public void setOrganisationId(int organisationId) {
		this.organisationId = organisationId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + organisationId;
		result = prime * result + parentId;
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
		Employee other = (Employee) obj;
		if (id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (organisationId != other.organisationId)
			return false;
		if (parentId != other.parentId)
			return false;
		return true;
	}
	
	
}
