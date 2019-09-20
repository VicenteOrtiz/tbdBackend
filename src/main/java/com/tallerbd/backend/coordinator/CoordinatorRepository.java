package com.tallerbd.backend.coordinator;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface CoordinatorRepository extends CrudRepository<Coordinator, Long> {
	
	List<Coordinator> findAll();

	Coordinator findByInstitution( String institution);
}
