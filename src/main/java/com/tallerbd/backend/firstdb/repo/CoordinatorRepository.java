package com.tallerbd.backend.firstdb.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.tallerbd.backend.firstdb.domain.Coordinator;

public interface CoordinatorRepository extends CrudRepository<Coordinator, Long> {
	
	List<Coordinator> findAll();

	Coordinator findByInstitution( String institution);
}
