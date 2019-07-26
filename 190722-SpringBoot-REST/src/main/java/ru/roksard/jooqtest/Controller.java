package ru.roksard.jooqtest;

import static ru.roksard.jooqtest.BaseResponse.CODE_ERROR;
import static ru.roksard.jooqtest.BaseResponse.CODE_SUCCESS;
import static ru.roksard.jooqtest.BaseResponse.ERROR_STATUS;
import static ru.roksard.jooqtest.BaseResponse.SUCCESS_STATUS;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
	public BaseResponse addOrganisation(@RequestBody Organisation org) {
		int result = 0;
		try {
			result = db.addOrganisation(org);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		if(result != 0)
			return new BaseResponse(SUCCESS_STATUS +"(id:"+result+")", CODE_SUCCESS);
		else
			return new BaseResponse(ERROR_STATUS, CODE_ERROR);
	}
	
	@PostMapping("/organisation/update")
	public BaseResponse updateOrganisation(@RequestBody Organisation org) {
		try {
			db.updateOrganisation(org);
			return new BaseResponse(SUCCESS_STATUS, CODE_SUCCESS);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return new BaseResponse(ERROR_STATUS, CODE_ERROR);
	}
	
	@PostMapping("/organisation/{id}/update")
	public BaseResponse updateOrganisation(@PathVariable int id, @RequestBody Organisation org) {
		try {
			org.setId(id);
			db.updateOrganisation(org);
			return new BaseResponse(SUCCESS_STATUS, CODE_SUCCESS);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return new BaseResponse(ERROR_STATUS, CODE_ERROR);
	}
	
	@PostMapping("/organisation/{id}/delete")
	public BaseResponse deleteOrganisation(@PathVariable int id) {
		try {
			return db.deleteOrganisation(id);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		//something went wrong
		return new BaseResponse(ERROR_STATUS, CODE_ERROR);
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
	public int countOrganisationEmployeeNumberList(
			@RequestParam(value="nameSearch", defaultValue="") String nameSearch) {
		try {
			return db.countOrganisationEmployeeNumberList(nameSearch);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		//something went wrong
		return 0;
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
	public int  countChildOrganisationList(
			@PathVariable("id") int id) {
		try {
			return db.countChildOrganisationList(id);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		//something went wrong
		return 0;
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
	public BaseResponse addEmployee(@RequestBody Employee emp) {
		try {
			return db.addEmployee(emp);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return new BaseResponse(ERROR_STATUS, CODE_ERROR);
	}
	
	@PostMapping("/employee/update")
	public BaseResponse updateEmployee(@RequestBody Employee emp) {
		try {
			return db.updateEmployee(emp);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return new BaseResponse(ERROR_STATUS, CODE_ERROR);
	}
	
	@PostMapping("/employee/{id}/delete")
	public BaseResponse deleteEmployee(@PathVariable int id) {
		try {
			return db.deleteEmployee(id);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		//something went wrong
		return new BaseResponse(ERROR_STATUS, CODE_ERROR);
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
	public int countEmployeeListByName(
			@RequestParam(value="nameSearch", defaultValue="") String nameSearch,
			@RequestParam(value="organisationNameSearch", defaultValue="") 
				String organisationNameSearch) {
		try {
			return db.countEmployeeListByName(nameSearch, organisationNameSearch);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		//something went wrong
		return 0;
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