package ru.roksard.jooqtest;

import static ru.roksard.jooqtest.BaseResponse.CODE_ERROR;
import static ru.roksard.jooqtest.BaseResponse.CODE_SUCCESS;
import static ru.roksard.jooqtest.BaseResponse.ERROR_STATUS;
import static ru.roksard.jooqtest.BaseResponse.SUCCESS_STATUS;

import java.util.List;
import java.util.Map;

import org.javatuples.Triplet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
			BaseResponse response = db.deleteOrganisation(id);
			return response;
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
	public Map<Organisation, Integer> getOrganisationListEmployeeCount(
			@RequestParam("nameSearch") String nameSearch, 
			@RequestParam("offset") int offset,
			@RequestParam("limit") int limit) {
		try {
			return db.getOrganisationListEmployeeCount(nameSearch, offset, limit);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		//something went wrong
		return null;
	}
	
	/**
	 * Get list of organisations, search by name and paging. 
	 * Parameters passed through URL like so: ?offset=0&limit=20
	 * @param nameSearch parameter containing a name or part of a name of organisation.
	 * @param offset amount of first records to skip from list
	 * @param limit max amount of records to select from list
	 * @return a Map containing entries: <Organisation, number of employees>
	 */
	@GetMapping("/organisation/{id}/tree")
	public Triplet<Organisation, List<Organisation>, Organisation>  getChildOrganisationList(
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
}
