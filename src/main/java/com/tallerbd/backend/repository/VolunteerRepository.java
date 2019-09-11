package com.tallerbd.backend.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.tallerbd.backend.model.Volunteer;

public interface VolunteerRepository extends CrudRepository<Volunteer, Integer> {
	
	List<Volunteer> findAll();

}
