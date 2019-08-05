package ru.roksard.empdb.dataobject;

public class Organisation {
	private int id = 0;
	private String name = "";
	private int parentId = 0;
	public int getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	
	/**
	 * 
	 * @return Id of parent (head) organisation
	 */
	public int getParentId() {
		return parentId;
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
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		Organisation other = (Organisation) obj;
		if (id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (parentId != other.parentId)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Organisation [id=" + id + ", name=" + name + ", parentId=" + parentId + "]";
	}
	
	public Organisation() {}
	
	public Organisation(int id, String name, int parentId) {
		this.id = id;
		this.name = name;
		this.parentId = parentId;
	}
	
	public Organisation(String name, int parentId) {
		this.name = name;
		this.parentId = parentId;
	}
	
}
