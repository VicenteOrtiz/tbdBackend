package com.tallerbd.backend.seconddb.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.tallerbd.backend.seconddb.domain.FormRequirement;

public interface FormRequirementRepository extends CrudRepository<FormRequirement, Long> {
	
	List<FormRequirement> findAll();
}
