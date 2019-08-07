package ru.roksard.empdb.dao;

//@formatter:off

import java.util.Iterator;

import org.jooq.DSLContext;
import org.jooq.Record1;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import ru.roksard.empdb.dataobject.Employee;
import ru.roksard.empdb.dataobject.EmployeeOrganisationBoss;
import ru.roksard.empdb.dataobject.EmployeeTree;
import ru.roksard.empdb.dataobject.Organisation;
import ru.roksard.empdb.dataobject.OrganisationEmployeeCount;
import ru.roksard.empdb.dataobject.OrganisationTree;
import ru.roksard.empdb.dataobject.Value;
import ru.roksard.empdb.jooqgenerated.tables.EmployeeChild;
import ru.roksard.empdb.jooqgenerated.tables.Employees;
import ru.roksard.empdb.jooqgenerated.tables.OrganisationChild;
import ru.roksard.empdb.jooqgenerated.tables.OrganisationEmployee;
import ru.roksard.empdb.jooqgenerated.tables.Organisations;
import ru.roksard.empdb.jooqgenerated.tables.records.EmployeesRecord;
import ru.roksard.empdb.jooqgenerated.tables.records.OrganisationEmployeeRecord;
import ru.roksard.empdb.jooqgenerated.tables.records.OrganisationsRecord;

import static ru.roksard.empdb.util.HttpHeadersUtil.httpHeadersDefault;
import static ru.roksard.empdb.util.HttpHeadersUtil.httpHeadersMsg;

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
		OrganisationsRecord result = dsl.selectFrom(organisations)
			.where(organisations.ID.equal(id))
			.fetchOne();

		Organisation org = null;
		if (result != null) {
			org = new Organisation(result.getValue(organisations.ID), result.getValue(organisations.NAME),
					result.getValue(organisations.PARENTID));
		}
		return org;
	}

	/**
	 * Insert new record into db, storing given organisation
	 * 
	 * @param org
	 *                object to be stored in db
	 * @return 'id' of new record in db
	 */
	public ResponseEntity<?> addOrganisation(Organisation org) {
		OrganisationsRecord result = dsl
				.insertInto(organisations)
				.set(organisations.NAME, org.getName())
				.set(organisations.PARENTID, org.getParentId())
				.returning(organisations.ID)
				.fetchOne();

		int thisId = 0;
		if (result != null)
			thisId = result.getId();

		// keeping record of parent-child relationships:
		if (org.getParentId() != 0)
			dsl
			.insertInto(organisation_child)
			.set(organisation_child.PARENT_ID, org.getParentId())
			.set(organisation_child.CHILD_ID, thisId)
			.execute();
		return ResponseEntity.ok()
			.headers(httpHeadersMsg("id:"+thisId))
			.build();
	}

	public ResponseEntity<?> updateOrganisation(Organisation org) {
		int orgUpdated = dsl.update(organisations)
    		.set(organisations.NAME, org.getName())
    		.set(organisations.PARENTID, org.getParentId())
    		.where(organisations.ID.equal(org.getId()))
    		.execute();

		// if parentId == 0, means there is no parent and we delete record from
		// organisation_child
		int orgChildUpdated = 0;
		if (org.getParentId() != 0) {
			orgChildUpdated = dsl.update(organisation_child)
    			.set(organisation_child.PARENT_ID, org.getParentId())
    			.set(organisation_child.CHILD_ID, org.getId())
    			.where(organisation_child.CHILD_ID.equal(org.getId()))
    			.execute();
			
			//record is not found (possibly when prev parentId was 0), manually add it
			if(orgChildUpdated == 0) {
				orgChildUpdated = dsl.insertInto(organisation_child)
    				.set(organisation_child.PARENT_ID, org.getParentId())
    				.set(organisation_child.CHILD_ID, org.getId())
    				.execute();
			}
		} else {
			orgChildUpdated = dsl.deleteFrom(organisation_child)
    			.where(organisation_child.CHILD_ID.equal(org.getId()))
    			.execute();
		}
		if(orgUpdated > 0 && orgChildUpdated > 0)
			return ResponseEntity.ok().build();
		else
			return ResponseEntity.notFound()
					.headers(httpHeadersMsg("Could not update, org not found (id:" + org.getId() + ")"))
					.build();
	}

	public ResponseEntity<?> deleteOrganisation(int id) {
		int childSum = 0;
		// count number of child orgs:
		childSum += dsl.fetchCount(
				dsl
				.selectFrom(organisation_child)
				.where(organisation_child.PARENT_ID.equal(id)));

		// count number of employees
		childSum += dsl.fetchCount(
				dsl
				.selectFrom(organisation_employee)
				.where(organisation_employee.ORGANISATION_ID.equal(id)));

		// delete only if org has no child orgs nor employees
		if (childSum == 0) {
			int count = dsl
					.deleteFrom(organisations)
					.where(organisations.ID.equal(id))
					.execute();
			if (count > 0)
				return ResponseEntity.ok().build(); 
			else {
				return ResponseEntity
						.status(HttpStatus.NOT_FOUND)
						.headers(httpHeadersMsg("Could not delete organisation (or not found) (id:" + id + ")"))
						.build();
			}
		}

		return ResponseEntity
				.status(HttpStatus.CONFLICT)
				.headers(httpHeadersMsg("Cannot delete while contains child elements: " + childSum))
				.build();
	}

	public ResponseEntity<OrganisationEmployeeCount[]> getOrganisationEmployeeNumberList(
																String nameSearch, int offset, int limit) {
		Result<OrganisationsRecord> result = 
				dsl
				.selectFrom(organisations)
				.where(organisations.NAME.containsIgnoreCase(nameSearch))
				.offset(offset)
				.limit(limit)
				.fetch();

		OrganisationEmployeeCount[] array = new OrganisationEmployeeCount[result.size()];

		Iterator<OrganisationsRecord> it = result.iterator();
		for (int i = 0; i < result.size(); i++) {
			OrganisationsRecord rec = it.next();
			Organisation org = new Organisation(
					rec.getValue(organisations.ID), 
					rec.getValue(organisations.NAME),
					rec.getValue(organisations.PARENTID));

			// get number of employees
			int emplCount = countEmployeesInOrganisation(org.getId());

			array[i] = new OrganisationEmployeeCount(org, emplCount);
		}
		return ResponseEntity.ok(array);
	}

	public int countOrganisationEmployeeNumberList(String nameSearch) {
		int result = dsl.fetchCount(
				dsl
				.selectFrom(organisations)
				.where(organisations.NAME.containsIgnoreCase(nameSearch)));
		return result;
	}

	public int countEmployeesInOrganisation(int orgId) {
		return dsl.fetchCount(
				dsl
				.selectFrom(organisation_employee)
				.where(organisation_employee.ORGANISATION_ID.equal(orgId)));
	}

	/**
	 * Method is used to show certain level of tree structure of organisations based
	 * on id of an organisation. It also allows to move through tree structure of
	 * Organisations, using references to parent and child organisations. Parent
	 * organisation allows to go to upper tree. Child organisation list allows to go
	 * to lower level trees. List of child organisations can be sliced into pages
	 * using offset and limit parameters.
	 * 
	 * @param orgId
	 *                   current organisation id
	 * @param offset
	 *                   offset in a list of child organisations
	 * @param limit
	 *                   limit list of child organisations by this value
	 * @return Triplet structure, containing <this org, list of child orgs, parent
	 *         org>
	 */
	public ResponseEntity<OrganisationTree> getChildOrganisationList(int orgId, int offset, int limit) {
		Result<Record1<Integer>> result = 
				dsl
				.select(organisation_child.CHILD_ID)
				.from(organisation_child)
				.where(organisation_child.PARENT_ID.equal(orgId))
				.offset(offset)
				.limit(limit)
				.fetch();

		// we have to use array, so jackson can form beautiful json representation of
		// this
		Organisation[] listOfChildOrgs = new Organisation[result.size()];

		Iterator<Record1<Integer>> it = result.iterator();
		for (int i = 0; i < result.size(); i++) {
			Record1<Integer> rec = it.next();
			Organisation org = getOrganisation(rec.getValue(organisation_child.CHILD_ID));
			listOfChildOrgs[i] = org;
		}

		Organisation thisOrg = getOrganisation(orgId);
		if (thisOrg == null)
			return ResponseEntity
					.notFound()
					.headers(httpHeadersMsg("organisation not found (id:" + orgId + ")"))
					.build();
		
		Organisation parentOrg = getOrganisation(thisOrg.getParentId());

		return ResponseEntity
				.ok()
				.headers(httpHeadersDefault())
				.body(
					new OrganisationTree(parentOrg, thisOrg, listOfChildOrgs)
				);
	}
	

	public int countChildOrganisationList(int orgId) {
		int result = dsl.fetchCount(
				dsl.select(organisation_child.CHILD_ID)
					.from(organisation_child)
					.where(organisation_child.PARENT_ID.equal(orgId)));
		return result;
	}

	public boolean employeeBelongsToOrganisation(int empId, int orgId) {
		OrganisationEmployeeRecord rec = 
				dsl.selectFrom(organisation_employee)
					.where(organisation_employee.EMPLOYEE_ID.equal(empId))
					.fetchOne();
		if (rec != null)
			if (rec.getValue(organisation_employee.ORGANISATION_ID) == orgId)
				return true;
		return false;
	}
	
	public boolean employeeExists(int id) {
	    return getEmployee(id) != null;
	}
	
	public boolean parentBelongsToSameOrganisation(Employee emp, Value<ResponseEntity<?>> failResponse) {
    	    // check if boss belongs to that organisation
    	    if (employeeExists(emp.getParentId())) {
        		if (!employeeBelongsToOrganisation(emp.getParentId(), emp.getOrganisationId())) {
        		    // if not, then fail adding:
        		    failResponse.setValue(ResponseEntity
            			.status(HttpStatus.CONFLICT)
            			.headers(httpHeadersMsg("Parent employee must belong to the same organisation"))
            			.build());
        			return false;
        		}
    	    } else {
    	    	failResponse.setValue(ResponseEntity
        			.status(HttpStatus.NOT_FOUND)
        			.headers(httpHeadersMsg("Employee not found (id:"+emp.getParentId()+")"))
        			.build());
    			return false;
    	    }
		return true;
	}

	/**
	 * Insert new record into db, storing given employee
	 * 
	 * @param org
	 *                object to be stored in db
	 * @return 'id' of new record in db
	 */
	public ResponseEntity<?> addEmployee(Employee emp) {
		if(emp.getParentId() != 0) {//0 = there is no boss for that employee
			Value<ResponseEntity<?>> failResponse = new Value<>(null);
			if (!parentBelongsToSameOrganisation(emp, failResponse))
				return failResponse.getValue();
		}
		
		EmployeesRecord result = dsl.insertInto(employees)
			.set(employees.NAME, emp.getName())
			.set(employees.PARENTID, emp.getParentId())
			.set(employees.ORGANISATIONID, emp.getOrganisationId())
			.returning(employees.ID)
			.fetchOne();

		int thisId = 0;
		if (result != null)
			thisId = result.getId();

		// keeping record of parent-child relationships:
		if (emp.getParentId() != 0)
			dsl.insertInto(employee_child)
				.set(employee_child.PARENT_ID, emp.getParentId())
				.set(employee_child.CHILD_ID, thisId)
				.execute();

		// keeping record of organisation-employee relationships:
    		dsl.insertInto(organisation_employee)
    			.set(organisation_employee.ORGANISATION_ID, emp.getOrganisationId())
    			.set(organisation_employee.EMPLOYEE_ID, thisId)
    			.execute();
    		return ResponseEntity.ok()
    				.headers(httpHeadersMsg("id:"+thisId))
    				.build();
	}

	public ResponseEntity<?> updateEmployee(Employee emp) {
		if(emp.getParentId() != 0) {//0 = there is no boss for that employee
			Value<ResponseEntity<?>> failResponse = new Value<>(null);
			if (!parentBelongsToSameOrganisation(emp, failResponse))
				return failResponse.getValue();
		}
	    	
		int empUpdated = dsl.update(employees)
			.set(employees.NAME, emp.getName())
			.set(employees.PARENTID, emp.getParentId())
			.set(employees.ORGANISATIONID, emp.getOrganisationId())
			.where(employees.ID.equal(emp.getId()))
			.execute();

		// if parentId == 0, means there is no parent and we should delete record from
		// employee_child
		int empChildUpdated = 0;
		if (emp.getParentId() != 0) {
			empChildUpdated = dsl.update(employee_child)
				.set(employee_child.PARENT_ID, emp.getParentId())
				.where(employee_child.CHILD_ID.equal(emp.getId()))
				.execute();
		
    		//record is not found (possibly when prev parentId was 0), manually add it
    		if(empChildUpdated == 0) {
    			empChildUpdated = dsl.insertInto(employee_child)
					.set(employee_child.PARENT_ID, emp.getParentId())
					.set(employee_child.CHILD_ID, emp.getId())
					.execute();
    		}
    			
		} else {
			dsl.deleteFrom(employee_child)
				.where(employee_child.CHILD_ID.equal(emp.getId()))
				.execute();
		}

		int orgEmpUpdated = dsl.update(organisation_employee)
			.set(organisation_employee.ORGANISATION_ID, emp.getOrganisationId())
			.where(organisation_employee.EMPLOYEE_ID.equal(emp.getId()))
			.execute();
		
		if(empUpdated > 0 && empChildUpdated > 0 && orgEmpUpdated > 0) 
			return ResponseEntity.ok().build();
		else
			return ResponseEntity.notFound()
					.headers(httpHeadersMsg("Could not update, employee not found (id:" + emp.getId() + ")"))
					.build();
	}

	public ResponseEntity<?> deleteEmployee(int id) {
		int childSum = 0;
		// count number of child employees:
		childSum += dsl.fetchCount(
			dsl.selectFrom(employee_child)
			.where(employee_child.PARENT_ID.equal(id)));

		// delete only if employee has no child employees
		if (childSum == 0) {
			dsl.deleteFrom(employees)
			.where(employees.ID.equal(id))
			.execute();
			
			return ResponseEntity.ok().build();
		}
		return ResponseEntity.status(HttpStatus.CONFLICT)
				.headers(httpHeadersMsg("child elements: " + childSum))
				.build();
	}

	public ResponseEntity<Employee> getEmployee(int id) {
		EmployeesRecord result = dsl.selectFrom(employees)
			.where(employees.ID.equal(id))
			.fetchOne();

		Employee emp = null;
		if (result != null) {
			emp = new Employee(
				result.getValue(employees.ID), 
				result.getValue(employees.NAME),
				result.getValue(employees.PARENTID), 
				result.getValue(employees.ORGANISATIONID));
			return ResponseEntity.ok(emp);
		}
		return ResponseEntity.notFound()
				.headers(httpHeadersMsg("Employee not found (id:"+ id + ")"))
				.build();
	}

	public ResponseEntity<EmployeeOrganisationBoss[]> getEmployeeListByName(
						String nameSearch, String organisationNameSearch, int offset, int limit) {
		Result<EmployeesRecord> result = dsl.selectFrom(employees)
				.where(employees.ORGANISATIONID
					.in(
						dsl.select(organisations.ID)
						.from(organisations)
						.where(organisations.NAME.containsIgnoreCase(organisationNameSearch)))
						.and(employees.NAME.containsIgnoreCase(nameSearch)))
				.offset(offset)
				.limit(limit)
				.fetch();

		EmployeeOrganisationBoss[] list = new EmployeeOrganisationBoss[result.size()];

		Iterator<EmployeesRecord> it = result.iterator();
		for (int i = 0; i < result.size(); i++) {
			EmployeesRecord rec = it.next();
			Employee emp = new Employee(rec.getValue(employees.ID), rec.getValue(employees.NAME),
					rec.getValue(employees.PARENTID), rec.getValue(employees.ORGANISATIONID));
			Employee boss = getEmployee(emp.getParentId()).getBody();
			Organisation org = getOrganisation(emp.getOrganisationId());

			list[i] = new EmployeeOrganisationBoss(emp, org, boss);
		}
		return ResponseEntity.ok(list);
	}

	public int countEmployeeListByName(String nameSearch, String organisationNameSearch) {
		int result = dsl.fetchCount(
			dsl.selectFrom(employees)
			.where(employees.ORGANISATIONID
				.in(
					dsl.select(organisations.ID)
					.from(organisations)
					.where(organisations.NAME.containsIgnoreCase(organisationNameSearch)))
					.and(employees.NAME.containsIgnoreCase(nameSearch))));
		return result;
	}

	/**
	 * Method is used to show certain level of tree structure of employees based on
	 * id of an employee. It also allows to move through tree structure of
	 * employees, using references to parent and child employees. Parent employee
	 * allows to go to upper tree. Child employees list allows to go to lower level
	 * trees. List of child employees can be sliced into pages using offset and
	 * limit parameters.
	 * 
	 * @param empId
	 *                   current employee id
	 * @param offset
	 *                   offset in a list of child employees
	 * @param limit
	 *                   limit list of child employees by this value
	 * @return Triplet structure, containing <this empl, list of child empls, parent
	 *         empl>
	 */
	public ResponseEntity<EmployeeTree> getChildEmployeeList(int empId, int offset, int limit) {
		Result<Record1<Integer>> result = 
			dsl.select(employee_child.CHILD_ID)
			.from(employee_child)
			.where(employee_child.PARENT_ID.equal(empId))
			.offset(offset)
			.limit(limit)
			.fetch();

		Employee[] list = new Employee[result.size()];

		Iterator<Record1<Integer>> it = result.iterator();
		for (int i = 0; i < result.size(); i++) {
			Record1<Integer> rec = it.next();
			Employee emp = getEmployee(rec.getValue(employee_child.CHILD_ID)).getBody();
			list[i] = emp;
		}

		Employee thisEmp = getEmployee(empId).getBody();
		if (thisEmp == null)
			return ResponseEntity
					.notFound()
					.headers(httpHeadersMsg("employee not found (id:" + empId + ")"))
					.build();

		Employee boss = getEmployee(thisEmp.getParentId()).getBody();

		return ResponseEntity
				.ok()
				.headers(httpHeadersDefault())
				.body(
						new EmployeeTree(boss, thisEmp, list)
				);
	}

	public int countChildEmployeeList(int empId) {
		int result = dsl.fetchCount(
			dsl.select(employee_child.CHILD_ID)
			.from(employee_child)
			.where(employee_child.PARENT_ID.equal(empId)));
		return result;

	}

}
