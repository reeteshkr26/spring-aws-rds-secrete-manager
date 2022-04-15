package com.dev.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dev.app.model.Person;
import com.dev.app.repo.PersonRepository;

import lombok.SneakyThrows;

@RestController
@RequestMapping("/api/v1")
public class PersonApi {

	@Autowired
	private PersonRepository personRepo;
	
	@PostMapping("/person")
	public Person savePerson(@RequestBody Person person) {
		return personRepo.save(person);
	}
	
	@GetMapping("/persons")
	public List<Person> findPersons(){
		return personRepo.findAll();
	}
	
	@SneakyThrows
	@GetMapping("/person/{id}")
	public Person findPerson(@PathVariable("id") int id) {
		return personRepo.findById(id).orElseThrow(() -> new Exception("Person not found by given id"));
	}
}
