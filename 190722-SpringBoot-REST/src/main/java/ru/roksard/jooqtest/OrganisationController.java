package ru.roksard.jooqtest;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController	
@RequestMapping("/organisation")
public class OrganisationController {
	
	@Autowired
	private DBInteract db;
	
	private static final String template = "Hello, %s!";
	private final AtomicLong counter = new AtomicLong();
	
//	@PostMapping("/add")
//    public GenericEntity addEntity(GenericEntity entity) {
//        entityList.add(entity);
//        return entity;
//    }
	
	@GetMapping("/employees") //add paging params //?offset=0&limit=20
	public List<Organisation> getEmployeeList(
			@RequestParam("nameSearch") String nameSearch, 
			@RequestParam("organisationNameSearch") String organisationNameSearch,
			@RequestParam("offset") int offset,
			@RequestParam("limit") int limit) {
		db.getEmployeeListByName(nameSearch, organisationNameSearch, offset, limit);
		return null;
		
	}
	
	@GetMapping("/{id}")
	public Organisation getOrganisation(@PathVariable int id) {
		
		return null;		
	}
	
	@GetMapping("/test{id}")
	public List<Organisation> getTest(@PathVariable int id) {
		int id1 = db.addOrganisation(new Organisation("orgA", 0));
		int id2 = db.addOrganisation(new Organisation("orgB", id1));
		int id3 = db.addOrganisation(new Organisation("orgC", id1));
		LinkedList<Organisation> list = new LinkedList<>();
		list.add(db.getOrganisation(id1));
		list.add(db.getOrganisation(id2));
		list.add(db.getOrganisation(id3));
		list.add(new Organisation(" "+id1+" "+id2+" "+id3, 999));
		int e1 = db.addEmployee(new Employee("jack in orgA", 0, id1));
		int e2 = db.addEmployee(new Employee("mike in orgA under jack", e1, id1));
		int e3 = db.addEmployee(new Employee("andrew in orgB", 0, id2));
		System.out.println("e1="+e1);
		System.out.println("e2"+e2);
		System.out.println("e3="+e3);
		return list;		
	}
	
	@GetMapping("/insert")
	public Organisation insertData() {
//		db.insertData();
		return new Organisation(1, "stored successfully", 0);
	}
	
	@GetMapping("/extract")
	public Organisation extractData() {
//		db.extractDataAfterInsert();;
		return new Organisation(1, "extraction complete", 0);
	}
	
	@GetMapping("/update")
	public Organisation updateData() {
//		db.updateData();
		return new Organisation(1, "update complete", 0);
	}
	
	@GetMapping("/extractUpd")
	public Organisation extractUpdData() {
//		db.extractDataAfterUpdate();
		return new Organisation(1, "extractionUPD complete", 0);
	}
	
	@GetMapping("/delete")
	public Organisation delData() {
//		db.deleteData();
		return new Organisation(1, "delete complete", 0);
	}
	
	@GetMapping("/extractDel")
	public Organisation extractDelData() {
//		db.extractAfterDelete();;
		return new Organisation(1, "extractionDEL complete", 0);
	}
}
