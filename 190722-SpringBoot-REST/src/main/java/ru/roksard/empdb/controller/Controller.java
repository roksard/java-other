package ru.roksard.empdb.controller;

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

@CrossOrigin
@RestController	
public class Controller {
	
	@Autowired
	private DBInteract db;
	
	
	@GetMapping("/organisation/{id}")
	public Organisation getOrganisation(@PathVariable int id) {
		try {
			return db.getOrganisation(id);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 
	 * @param org
	 * @return Status and id of new record added in db
	 */
	@PostMapping("/organisation/add")
	public ResponseEntity<Value> addOrganisation(@RequestBody Organisation org) {
		int result = 0;
		try {
			result = db.addOrganisation(org);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		if(result != 0)
			return new ResponseEntity<>(new Value("OK"), HttpStatus.OK);
		else
			return new ResponseEntity<>(new Value("Error"), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@PostMapping("/organisation/update")
	public ResponseEntity<Value> updateOrganisation(@RequestBody Organisation org) {
		try {
			db.updateOrganisation(org);
			return new ResponseEntity<>(new Value("OK"), HttpStatus.OK);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(new Value("Error"), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@PostMapping("/organisation/{id}/update")
	public ResponseEntity<Value> updateOrganisation(@PathVariable int id, @RequestBody Organisation org) {
		try {
			org.setId(id);
			db.updateOrganisation(org);
			return new ResponseEntity<>(new Value("OK"), HttpStatus.OK);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(new Value("Error"), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@PostMapping("/organisation/{id}/delete")
	public ResponseEntity<Value> deleteOrganisation(@PathVariable int id) {
		try {
			return db.deleteOrganisation(id);
		} catch (Throwable e) {
			e.printStackTrace();
			return new ResponseEntity<>(new Value(e.getMessage()), 
					HttpStatus.EXPECTATION_FAILED);
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
	public OrganisationEmployeeCount[] getOrganisationEmployeeNumberList(
			@RequestParam(value="nameSearch", defaultValue="") String nameSearch, 
			@RequestParam("offset") int offset,
			@RequestParam("limit") int limit) {
		try {
			return db.getOrganisationEmployeeNumberList(nameSearch, offset, limit);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		//something went wrong
		return null;
	}
	
	@GetMapping("/organisation/list/count")
	public Value countOrganisationEmployeeNumberList(
			@RequestParam(value="nameSearch", defaultValue="") String nameSearch) {
		try {
			return new Value(db.countOrganisationEmployeeNumberList(nameSearch));
		} catch (Throwable e) {
			e.printStackTrace();
		}
		//something went wrong
		return null;
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
	public OrganisationTree  getChildOrganisationList(
			@PathVariable("id") int id,
			@RequestParam("offset") int offset,
			@RequestParam("limit") int limit) {
		try {
			return db.getChildOrganisationList(id, offset, limit);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		//something went wrong
		return null;
	}
	
	@GetMapping("/organisation/{id}/tree/count")
	public Value  countChildOrganisationList(
			@PathVariable("id") int id) {
		try {
			return new Value(db.countChildOrganisationList(id));
		} catch (Throwable e) {
			e.printStackTrace();
		}
		//something went wrong
		return null;
	}
	
	@GetMapping("/employee/{id}")
	public Employee getEmployee(@PathVariable int id) {
		try {
			return db.getEmployee(id);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 
	 * @param empl employee object passed to be added into db
	 * @return Status and id of new record added in db
	 */
	@PostMapping("/employee/add")
	public ResponseEntity<Value> addEmployee(@RequestBody Employee emp) {
		try {
			return db.addEmployee(emp);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(new Value("Error"), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@PostMapping("/employee/update")
	public ResponseEntity<Value> updateEmployee(@RequestBody Employee emp) {
		try {
			return db.updateEmployee(emp);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(new Value("Error"), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@PostMapping("/employee/{id}/delete")
	public ResponseEntity<Value> deleteEmployee(@PathVariable int id) {
		try {
			return db.deleteEmployee(id);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		//something went wrong
		return new ResponseEntity<>(new Value("Error"), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@GetMapping("/employee/list")
	public EmployeeOrganisationBoss[] getEmployeeListByName(
			@RequestParam(value="nameSearch", defaultValue="") String nameSearch,
			@RequestParam(value="organisationNameSearch", defaultValue="") String organisationNameSearch, 
			@RequestParam("offset") int offset,
			@RequestParam("limit") int limit) {
		try {
			return db.getEmployeeListByName(nameSearch, organisationNameSearch, offset, limit);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		//something went wrong
		return null;
	}
	
	@GetMapping("/employee/list/count")
	public Value countEmployeeListByName(
			@RequestParam(value="nameSearch", defaultValue="") String nameSearch,
			@RequestParam(value="organisationNameSearch", defaultValue="") 
				String organisationNameSearch) {
		try {
			return new Value(db.countEmployeeListByName(nameSearch, organisationNameSearch));
		} catch (Throwable e) {
			e.printStackTrace();
		}
		//something went wrong
		return null;
	}
	
	@GetMapping("/employee/{id}/tree")
	public EmployeeTree getChildEmployeeList(
			@PathVariable("id") int id,
			@RequestParam("offset") int offset,
			@RequestParam("limit") int limit) {
		try {
			return db.getChildEmployeeList(id, offset, limit);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		//something went wrong
		return null;
	}
	
	@GetMapping("/employee/{id}/tree/count")
	public Value countChildEmployeeList(@PathVariable("id") int id) {
		try {
			return new Value(db.countChildEmployeeList(id));
		} catch (Throwable e) {
			e.printStackTrace();
		}
		//something went wrong
		return new Value(0); 
	}
}
