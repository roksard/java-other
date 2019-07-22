package ru.roksard.jooqtest;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController	
@RequestMapping("/organisation")
public class OrganisationController {
	
	@Autowired
	private DBInteract db;
	
	private static final String template = "Hello, %s!";
	private final AtomicLong counter = new AtomicLong();
	
	@GetMapping("/") //add paging params ?offset=0 &?limit=20
	public List<Organisation> getOrganisationList() {
		return null;
	}
	
	@GetMapping("/{id}")
	public Organisation getOrganisation(@PathVariable int id) {
		return new Organisation(id, "somename", 0);
	}
	
	@GetMapping("/insert")
	public Organisation insertData() {
		db.insertData();
		return new Organisation(1, "stored successfully", 0);
	}
	
	@GetMapping("/extract")
	public Organisation extractData() {
		db.extractDataAfterInsert();;
		return new Organisation(1, "extraction complete", 0);
	}
	
	@GetMapping("/update")
	public Organisation updateData() {
		db.updateData();
		return new Organisation(1, "update complete", 0);
	}
	
	@GetMapping("/extractUpd")
	public Organisation extractUpdData() {
		db.extractDataAfterUpdate();
		return new Organisation(1, "extractionUPD complete", 0);
	}
	
	@GetMapping("/delete")
	public Organisation delData() {
		db.deleteData();
		return new Organisation(1, "delete complete", 0);
	}
	
	@GetMapping("/extractDel")
	public Organisation extractDelData() {
		db.extractAfterDelete();;
		return new Organisation(1, "extractionDEL complete", 0);
	}
}
