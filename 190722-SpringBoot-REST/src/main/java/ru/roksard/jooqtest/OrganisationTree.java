package ru.roksard.jooqtest;

import java.util.Arrays;

public class OrganisationTree {
	private Organisation parentOrganisation;
	private Organisation organisation;
	private Organisation[] childOrganisationList;
	public OrganisationTree(Organisation parentOrganisation, Organisation organisation,
			Organisation[] childOrganisationList) {
		super();
		this.parentOrganisation = parentOrganisation;
		this.organisation = organisation;
		this.childOrganisationList = childOrganisationList;
	}
	public Organisation getParentOrganisation() {
		return parentOrganisation;
	}
	public Organisation getOrganisation() {
		return organisation;
	}
	public Organisation[] getChildOrganisationList() {
		return childOrganisationList;
	}
	public void setParentOrganisation(Organisation parentOrganisation) {
		this.parentOrganisation = parentOrganisation;
	}
	public void setOrganisation(Organisation organisation) {
		this.organisation = organisation;
	}
	public void setChildOrganisationList(Organisation[] childOrganisationList) {
		this.childOrganisationList = childOrganisationList;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(childOrganisationList);
		result = prime * result + ((organisation == null) ? 0 : organisation.hashCode());
		result = prime * result + ((parentOrganisation == null) ? 0 : parentOrganisation.hashCode());
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
		OrganisationTree other = (OrganisationTree) obj;
		if (!Arrays.equals(childOrganisationList, other.childOrganisationList))
			return false;
		if (organisation == null) {
			if (other.organisation != null)
				return false;
		} else if (!organisation.equals(other.organisation))
			return false;
		if (parentOrganisation == null) {
			if (other.parentOrganisation != null)
				return false;
		} else if (!parentOrganisation.equals(other.parentOrganisation))
			return false;
		return true;
	}
	
}
