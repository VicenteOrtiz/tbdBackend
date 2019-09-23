package com.tallerbd.backend.form;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface FormRequirementRepository extends CrudRepository<FormRequirement, Long> {
	
	List<FormRequirement> findAll();
}
