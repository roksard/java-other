package ru.roksard.jooqtest;

import static ru.roksard.jooqtest.BaseResponse.CODE_ERROR;
import static ru.roksard.jooqtest.BaseResponse.CODE_SUCCESS;
import static ru.roksard.jooqtest.BaseResponse.ERROR_STATUS;
import static ru.roksard.jooqtest.BaseResponse.SUCCESS_STATUS;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.javatuples.Triplet;
import org.jooq.DSLContext;
import org.jooq.Record1;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ru.roksard.jooqgenerated.tables.EmployeeChild;
import ru.roksard.jooqgenerated.tables.Employees;
import ru.roksard.jooqgenerated.tables.OrganisationChild;
import ru.roksard.jooqgenerated.tables.OrganisationEmployee;
import ru.roksard.jooqgenerated.tables.Organisations;
import ru.roksard.jooqgenerated.tables.records.EmployeesRecord;
import ru.roksard.jooqgenerated.tables.records.OrganisationEmployeeRecord;
import ru.roksard.jooqgenerated.tables.records.OrganisationsRecord;

@Component
public class DBInteract {
	@Autowired
	private DSLContext dsl;
	
	Organisations organisations = Organisations.ORGANISATIONS;
	Employees employees = Employees.EMPLOYEES;
	OrganisationChild organisation_child = OrganisationChild.ORGANISATION_CHILD;
	OrganisationEmployee organisation_employee = OrganisationEmployee.ORGANISATION_EMPLOYEE;
	EmployeeChild employee_child = EmployeeChild.EMPLOYEE_CHILD;
	
	public Organisation getOrganisation(int id) {
		OrganisationsRecord result = 
				dsl.selectFrom(organisations)
					.where(organisations.ID.equal(id))
					.fetchOne();
		
		Organisation org = null;
		if(result != null) {
			org = new Organisation(
					result.getValue(organisations.ID), 
					result.getValue(organisations.NAME),
					result.getValue(organisations.PARENTID));
		}
		return org;
	}
	
	/**
	 * Insert new record into db, storing given organisation
	 * @param org object to be stored in db
	 * @return 'id' of new record in db
	 */
	public int addOrganisation(Organisation org) {
		OrganisationsRecord result = dsl.insertInto(organisations)
			.set(organisations.NAME, org.getName())
			.set(organisations.PARENTID, org.getParentId())
			.returning(organisations.ID)
			.fetchOne();
		
		int childId = 0;
		if(result != null)
			childId = result.getId();
		
		//keeping record of parent-child relationships:
		if(org.getParentId() != 0)
			dsl.insertInto(organisation_child)
				.set(organisation_child.PARENT_ID, org.getParentId())
				.set(organisation_child.CHILD_ID, childId)
				.execute();
		return childId;
	}
	
	public void updateOrganisation(Organisation org) {
		dsl.update(organisations)
			.set(organisations.NAME, org.getName())
			.set(organisations.PARENTID, org.getParentId())
			.where(organisations.ID.equal(org.getId()))
			.execute();
		
		//if parentId == 0, means there is no parent and we delete record from organisation_child
		if(org.getParentId() != 0)
			dsl.update(organisation_child)
				.set(organisation_child.PARENT_ID, org.getParentId())
				.set(organisation_child.CHILD_ID, org.getId())
				.where(organisation_child.CHILD_ID.equal(org.getId()))
				.execute();
		else
			dsl.deleteFrom(organisation_child)
				.where(organisation_child.CHILD_ID.equal(org.getId()))
				.execute();
	}
	
	public BaseResponse deleteOrganisation(int id) {
		int childSum = 0;
		//count number of child orgs:
		childSum += dsl.fetchCount(dsl.selectFrom(organisation_child)
			.where(organisation_child.PARENT_ID.equal(id)));
		
		//count number of employees
		childSum += dsl.fetchCount(dsl.selectFrom(organisation_employee)
				.where(organisation_employee.ORGANISATION_ID.equal(id)));
		
		//delete only if org has no child orgs nor employees
		if(childSum == 0) {
			dsl.deleteFrom(organisations)
				.where(organisations.ID.equal(id))
				.execute();
			return new BaseResponse(SUCCESS_STATUS, CODE_SUCCESS);
		}
		return new BaseResponse(ERROR_STATUS+": cannot delete while contains child elements: "
				+childSum, CODE_ERROR);
	}
	
	public Map<Organisation,Integer> getOrganisationListEmployeeCount(
			String nameSearch, int offset, int limit) {
		Result<OrganisationsRecord> result = 
			dsl.selectFrom(organisations)
				.where(organisations.NAME.containsIgnoreCase(nameSearch))
				.offset(offset)
				.limit(limit)
				.fetch();
		
		Map<Organisation, Integer> map = new HashMap<Organisation,Integer>();
		
		for(OrganisationsRecord rec : result) {
			Organisation org = new Organisation(
					rec.getValue(organisations.ID), 
					rec.getValue(organisations.NAME),
					rec.getValue(organisations.PARENTID));
			
			//get number of employees
			int emplCount = getOrganisationEmployeeCount(org.getId());
			map.put(org, emplCount);
		}
		
		return map;
	}
	
	public int getOrganisationEmployeeCount(int orgId) {
		return dsl.fetchCount(dsl.selectFrom(organisation_employee)
				.where(organisation_employee.ORGANISATION_ID.equal(orgId)));
	}
	
	/**
	 * Method is used to show certain level of tree structure of organisations based on id of an 
	 * organisation. It also allows
	 * to move through tree structure of Organisations, using references to parent and child 
	 * organisations. Parent organisation allows to go to upper tree. Child organisation list 
	 * allows to go to lower level trees. List of child organisations can be sliced into pages
	 * using offset and limit parameters.
	 * @param orgId current organisation id
	 * @param offset offset in a list of child organisations
	 * @param limit limit list of child organisations by this value
	 * @return Triplet structure, containing <this org, list of child orgs, parent org>
	 */
	public Triplet<Organisation, List<Organisation>, Organisation> getChildOrganisationList(
			int orgId, int offset, int limit) {
		Result<Record1<Integer>> result =
			dsl.select(organisation_child.CHILD_ID)
				.from(organisation_child)
				.where(organisation_child.PARENT_ID.equal(orgId))
				.offset(offset)
				.limit(limit)
				.fetch();
		
		List<Organisation> list = new LinkedList<Organisation>();
		
		for(Record1<Integer> rec : result) {
			Organisation org = getOrganisation(
					rec.getValue(organisation_child.CHILD_ID));
			list.add(org);
		}
		
		Organisation thisOrg = getOrganisation(orgId);
		Organisation parenOrg = getOrganisation(thisOrg.getParentId());
		
		return new Triplet<Organisation, List<Organisation>, Organisation>(
			thisOrg, list, parenOrg);
	}
	
	public boolean employeeBelongsToOrganisation(int empId, int orgId) {
		OrganisationEmployeeRecord rec = dsl.selectFrom(organisation_employee)
			.where(organisation_employee.EMPLOYEE_ID.equal(empId))
			.fetchOne();
		if(rec != null) 
			if (rec.getValue(organisation_employee.ORGANISATION_ID) == orgId)
				return true;
		return false;
	}
	
	/**
	 * Insert new record into db, storing given employee
	 * @param org object to be stored in db
	 * @return 'id' of new record in db
	 */ 
	public BaseResponse addEmployee(Employee emp) {
		//check if boss belongs to that organisation
		if(!employeeBelongsToOrganisation(emp.getParentId(), emp.getOrganisationId()))
			//if not, then fail adding:
			return new BaseResponse(ERROR_STATUS+": boss must belong to the same organisation: "
					+ "boss.orgId: "+getEmployee(emp.getParentId()).getOrganisationId()
					+ "thisEmployee.orgId: "+emp.getOrganisationId()
					, CODE_ERROR); 
		EmployeesRecord result = dsl.insertInto(employees)
			.set(employees.NAME, emp.getName())
			.set(employees.PARENTID, emp.getParentId())
			.set(employees.ORGANISATIONID, emp.getOrganisationId())
			.returning(employees.ID)
			.fetchOne();
		
		int childId = 0;
		if(result != null)
			childId = result.getId();

		//keeping record of parent-child relationships:
		if(emp.getParentId() != 0)
			dsl.insertInto(employee_child)
				.set(employee_child.PARENT_ID, emp.getParentId())
				.set(employee_child.CHILD_ID, childId)
				.execute();
		
		//keeping record of organisation-employee relationships:
		dsl.insertInto(organisation_employee)
			.set(organisation_employee.ORGANISATION_ID, emp.getOrganisationId())
			.set(organisation_employee.EMPLOYEE_ID, childId)
			.execute();
		return new BaseResponse(SUCCESS_STATUS +"(id:"+childId+")", CODE_SUCCESS);
	}
	
	public BaseResponse updateEmployee(Employee emp) {
		//check if boss belongs to that organisation
		if(!employeeBelongsToOrganisation(emp.getParentId(), emp.getOrganisationId()))
			//if not, then fail adding:
			return new BaseResponse(ERROR_STATUS+": boss must belong to the same organisation: "
					+ "boss.orgId: "+getEmployee(emp.getParentId()).getOrganisationId()
					+ "thisEmployee.orgId: "+emp.getOrganisationId()
					, CODE_ERROR); 
		dsl.update(employees)
			.set(employees.NAME, emp.getName())
			.set(employees.PARENTID, emp.getParentId())
			.set(employees.ORGANISATIONID, emp.getOrganisationId())
			.where(employees.ID.equal(emp.getId()))
			.execute();
		
		//if parentId == 0, means there is no parent and we delete record from employee_child
		if(emp.getParentId() != 0)
			dsl.update(employee_child)
				.set(employee_child.PARENT_ID, emp.getParentId())
				.set(employee_child.CHILD_ID, emp.getId())
				.where(employee_child.CHILD_ID.equal(emp.getId()))
				.execute();
		else
			dsl.deleteFrom(employee_child)
				.where(employee_child.CHILD_ID.equal(emp.getId()))
				.execute();
		
		dsl.update(organisation_employee)
		.set(organisation_employee.ORGANISATION_ID, emp.getOrganisationId())
		.set(organisation_employee.EMPLOYEE_ID, emp.getId())
		.where(organisation_employee.EMPLOYEE_ID.equal(emp.getId()))
		.execute();
		return new BaseResponse(SUCCESS_STATUS, CODE_SUCCESS);
	}
	
	public BaseResponse deleteEmployee(int id) {
		int childSum = 0;
		//count number of child employees:
		childSum += dsl.fetchCount(dsl.selectFrom(employee_child)
			.where(employee_child.PARENT_ID.equal(id)));
		
		//delete only if employee has no child employees
		if(childSum == 0) {
			dsl.deleteFrom(employees)
				.where(employees.ID.equal(id))
				.execute();
			return new BaseResponse(SUCCESS_STATUS, CODE_SUCCESS);
		}
		return new BaseResponse(ERROR_STATUS+": cannot delete while contains child elements: "
				+childSum, CODE_ERROR);
	}
	
	public Employee getEmployee(int id) {
		EmployeesRecord result = 
				dsl.selectFrom(employees)
					.where(employees.ID.equal(id))
					.fetchOne();
		
		Employee emp = null;
		if(result != null) {
			emp = new Employee(
					result.getValue(employees.ID), 
					result.getValue(employees.NAME),
					result.getValue(employees.PARENTID),
					result.getValue(employees.ORGANISATIONID));
		}
		return emp;
	}
	
	public List<Triplet<Employee, Organisation, Employee>> getEmployeeListByName(
			String nameSearch, String organisationNameSearch, int offset, int limit) {
		Result<EmployeesRecord> result = dsl.selectFrom(employees)
			.where(employees.ORGANISATIONID.in(
					dsl.select(organisations.ID)
						.from(organisations)
						.where(organisations.NAME.containsIgnoreCase(organisationNameSearch)))
			.and(employees.NAME.containsIgnoreCase(nameSearch)))
			.offset(offset)
			.limit(limit)
			.fetch();
		List<Triplet<Employee, Organisation, Employee>> list = new 
				LinkedList<Triplet<Employee, Organisation, Employee>>();
		
		for(EmployeesRecord rec : result) {
			Employee emp = new Employee(
					rec.getValue(employees.ID), 
					rec.getValue(employees.NAME),
					rec.getValue(employees.PARENTID),
					rec.getValue(employees.ORGANISATIONID));
			Employee parent = getEmployee(emp.getParentId());
			Organisation org = getOrganisation(emp.getOrganisationId());
			
			Triplet<Employee, Organisation, Employee> outputRecord = 
					new Triplet<Employee, Organisation, Employee>(emp, org, parent);
			list.add(outputRecord);
		}
		return list;
	}
	
	/**
	 * Method is used to show certain level of tree structure of employees based on id of an 
	 * employee. It also allows
	 * to move through tree structure of employees, using references to parent and child 
	 * employees. Parent employee allows to go to upper tree. Child employees list 
	 * allows to go to lower level trees. List of child employees can be sliced into pages
	 * using offset and limit parameters.
	 * @param empId current employee id
	 * @param offset offset in a list of child employees
	 * @param limit limit list of child employees by this value
	 * @return Triplet structure, containing <this empl, list of child empls, parent empl>
	 */
	public Triplet<Employee, List<Employee>, Employee> getChildEmployeeList(
			int empId, int offset, int limit) {
		Result<Record1<Integer>> result =
			dsl.select(employee_child.CHILD_ID)
				.from(employee_child)
				.where(employee_child.PARENT_ID.equal(empId))
				.offset(offset)
				.limit(limit)
				.fetch();
		
		List<Employee> list = new LinkedList<Employee>();
		
		for(Record1<Integer> rec : result) {
			Employee emp = getEmployee(
					rec.getValue(employee_child.CHILD_ID));
			list.add(emp);
		}
		
		Employee thisEmp = getEmployee(empId);
		Employee parentEmp = getEmployee(thisEmp.getParentId());
		
		return new Triplet<Employee, List<Employee>, Employee>(
			thisEmp, list, parentEmp);
	}
}
