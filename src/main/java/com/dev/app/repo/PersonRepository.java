package com.dev.app.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dev.app.model.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer>{

}
