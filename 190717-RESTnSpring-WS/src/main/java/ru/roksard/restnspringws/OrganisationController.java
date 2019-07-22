package ru.roksard.restnspringws;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController	
@RequestMapping("/organisation")
public class OrganisationController {
	
	
	private static final String template = "Hello, %s!";
	private final AtomicLong counter = new AtomicLong();
	
	@GetMapping("/") //add paging params ?offset=0 &?limit=20
	public List<Organisation> getOrganisationList() {
		return null;
	}
	
	@GetMapping("/{id}")
	public Organisation getOrganisation(@PathVariable int id) {
		return null;
	}
}
