package com.tallerbd.backend.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.tallerbd.backend.model.Emergency;

public interface EmergencyRepository extends CrudRepository<Emergency, Integer> {
	
	List<Emergency> findAll();

}
