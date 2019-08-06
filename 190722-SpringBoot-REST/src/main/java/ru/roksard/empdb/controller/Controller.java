package ru.roksard.empdb.controller;

//@formatter:off

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ru.roksard.empdb.dao.DBInteract;
import ru.roksard.empdb.dataobject.Employee;
import ru.roksard.empdb.dataobject.EmployeeOrganisationBoss;
import ru.roksard.empdb.dataobject.EmployeeTree;
import ru.roksard.empdb.dataobject.Organisation;
import ru.roksard.empdb.dataobject.OrganisationEmployeeCount;
import ru.roksard.empdb.dataobject.OrganisationTree;
import ru.roksard.empdb.dataobject.Value;

import static ru.roksard.empdb.util.HttpHeadersUtil.httpHeadersMsg;

@CrossOrigin
@RestController	
public class Controller {
	
	@Autowired
	private DBInteract db;
	
	
	@GetMapping("/organisation/{id}")
	public ResponseEntity<Organisation> getOrganisation(@PathVariable int id) {
		try {
			return ResponseEntity
					.ok(db.getOrganisation(id));
		} catch (Throwable e) {
			e.printStackTrace();
			return ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.headers(httpHeadersMsg(e.getMessage()))
					.build();
		}
	}
	
	/**
	 * 
	 * @param org
	 * @return Status and id of new record added in db
	 */
	@PostMapping("/organisation/add")
	public ResponseEntity<?> addOrganisation(@RequestBody Organisation org) {
		try {
			return db.addOrganisation(org);
		} catch (Throwable e) {
			e.printStackTrace();
			return ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.headers(httpHeadersMsg(e.getMessage()))
					.build();
		}
	}

	@PostMapping("/organisation/update")
	public ResponseEntity<?> updateOrganisation(@RequestBody Organisation org) {
		try {
			return db.updateOrganisation(org);
		} catch (Throwable e) {
			e.printStackTrace();
			return ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.headers(httpHeadersMsg(e.getMessage()))
					.build();
		}
	}
	
	@PostMapping("/organisation/{id}/update")
	public ResponseEntity<?> updateOrganisation(@PathVariable int id, @RequestBody Organisation org) {
		try {
			org.setId(id);
			return db.updateOrganisation(org);
		} catch (Throwable e) {
			e.printStackTrace();
			return ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.headers(httpHeadersMsg(e.getMessage()))
					.build();
		}
	}
	
	@PostMapping("/organisation/{id}/delete")
	public ResponseEntity<?> deleteOrganisation(@PathVariable int id) {
		try {
			return db.deleteOrganisation(id);
		} catch (Throwable e) {
			e.printStackTrace();
			return ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.headers(httpHeadersMsg(e.getMessage()))
					.build();
		}
	}
	
	/**
	 * Get list of organisations, search by name and paging. 
	 * Parameters passed through URL like so: ?offset=0&limit=20
	 * @param nameSearch parameter containing a name or part of a name of organisation.
	 * @param offset amount of first records to skip from list
	 * @param limit max amount of records to select from list
	 * @return a Map containing entries: <Organisation, number of employees>
	 */
	@GetMapping("/organisation/list")
	public ResponseEntity<OrganisationEmployeeCount[]> getOrganisationEmployeeNumberList(
			@RequestParam(value="nameSearch", defaultValue="") String nameSearch, 
			@RequestParam("offset") int offset,
			@RequestParam("limit") int limit) {
		try {
			return db.getOrganisationEmployeeNumberList(nameSearch, offset, limit);
		} catch (Throwable e) {
			e.printStackTrace();
			return ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.headers(httpHeadersMsg(e.getMessage()))
					.build();
		}
	}
	
	@GetMapping("/organisation/list/count")
	public ResponseEntity<Value<Integer>> countOrganisationEmployeeNumberList(
			@RequestParam(value="nameSearch", defaultValue="") String nameSearch) {
		try {
			return ResponseEntity.ok(new Value<>(
							db.countOrganisationEmployeeNumberList(nameSearch)));
		} catch (Throwable e) {
			e.printStackTrace();
			return ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.headers(httpHeadersMsg(e.getMessage()))
					.build();
		}
	}
	
	/**
	 * Method is used to show certain level of tree structure of organisations based on id of an 
	 * organisation. It also allows
	 * to move through tree structure of Organisations, using references to parent and child 
	 * organisations. Parent organisation allows to go to upper tree. Child organisation list 
	 * allows to go to lower level trees. List of child organisations can be sliced into pages
	 * using offset and limit parameters.
	 * @param id current organisation id
	 * @param offset offset in a list of child organisations
	 * @param limit limit list of child organisations by this value
	 * @return Triplet structure, containing <this org, list of child orgs, parent org>
	 */
	@GetMapping("/organisation/{id}/tree")
	public ResponseEntity<OrganisationTree> getChildOrganisationList(
			@PathVariable("id") int id,
			@RequestParam("offset") int offset,
			@RequestParam("limit") int limit) {
		try {
			return db.getChildOrganisationList(id, offset, limit);
		} catch (Throwable e) {
			e.printStackTrace();
			return ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.headers(httpHeadersMsg(e.getMessage()))
					.build();
		}
	}
	
	@GetMapping("/organisation/{id}/tree/count")
	public ResponseEntity<Value<Integer>> countChildOrganisationList( @PathVariable("id") int id) {
		try {
			return ResponseEntity.ok(new Value<>(
					db.countChildOrganisationList(id)));
		} catch (Throwable e) {
			e.printStackTrace();
			return ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.headers(httpHeadersMsg(e.getMessage()))
					.build();
		}
	}
	
	@GetMapping("/employee/{id}")
	public ResponseEntity<Employee> getEmployee(@PathVariable int id) {
		try {
			return db.getEmployee(id);
		} catch (Throwable e) {
			e.printStackTrace();
			return ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.headers(httpHeadersMsg(e.getMessage()))
					.build();
		}
	}
	
	/**
	 * 
	 * @param empl employee object passed to be added into db
	 * @return Status and id of new record added in db
	 */
	@PostMapping("/employee/add")
	public ResponseEntity<?> addEmployee(@RequestBody Employee emp) {
		try {
			return db.addEmployee(emp);
		} catch (Throwable e) {
			e.printStackTrace();
			return ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.headers(httpHeadersMsg(e.getMessage()))
					.build();
		}
	}
	
	@PostMapping("/employee/update")
	public ResponseEntity<?> updateEmployee(@RequestBody Employee emp) {
		try {
			return db.updateEmployee(emp);
		} catch (Throwable e) {
			e.printStackTrace();
			return ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.headers(httpHeadersMsg(e.getMessage()))
					.build();
		}
	}
	
	@PostMapping("/employee/{id}/delete")
	public ResponseEntity<?> deleteEmployee(@PathVariable int id) {
		try {
			return db.deleteEmployee(id);
		} catch (Throwable e) {
			e.printStackTrace();
			return ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.headers(httpHeadersMsg(e.getMessage()))
					.build();
		}
	}
	
	@GetMapping("/employee/list")
	public ResponseEntity<EmployeeOrganisationBoss[]> getEmployeeListByName(
			@RequestParam(value="nameSearch", defaultValue="") String nameSearch,
			@RequestParam(value="organisationNameSearch", defaultValue="") String organisationNameSearch, 
			@RequestParam("offset") int offset,
			@RequestParam("limit") int limit) {
		try {
			return db.getEmployeeListByName(nameSearch, organisationNameSearch, offset, limit);
		} catch (Throwable e) {
			e.printStackTrace();
			return ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.headers(httpHeadersMsg(e.getMessage()))
					.build();
		}
	}
	
	@GetMapping("/employee/list/count")
	public ResponseEntity<Value<Integer>> countEmployeeListByName(
			@RequestParam(value="nameSearch", defaultValue="") String nameSearch,
			@RequestParam(value="organisationNameSearch", defaultValue="") 
				String organisationNameSearch) {
		try {
			return ResponseEntity.ok(new Value<>(
					db.countEmployeeListByName(nameSearch, organisationNameSearch)));
		} catch (Throwable e) {
			e.printStackTrace();
			return ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.headers(httpHeadersMsg(e.getMessage()))
					.build();
		}
	}
	
	@GetMapping("/employee/{id}/tree")
	public ResponseEntity<EmployeeTree> getChildEmployeeList(
			@PathVariable("id") int id,
			@RequestParam("offset") int offset,
			@RequestParam("limit") int limit) {
		try {
			return db.getChildEmployeeList(id, offset, limit);
		} catch (Throwable e) {
			e.printStackTrace();
			return ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.headers(httpHeadersMsg(e.getMessage()))
					.build();
		}
	}
	
	@GetMapping("/employee/{id}/tree/count")
	public ResponseEntity<Value<Integer>> countChildEmployeeList(
											@PathVariable("id") int id) {
		try {
			return ResponseEntity.ok(new Value<>(
					db.countChildEmployeeList(id)));
		} catch (Throwable e) {
			e.printStackTrace();
			return ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.headers(httpHeadersMsg(e.getMessage()))
					.build();
		}
	}
}
