package com.tallerbd.backend.form;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface FormEquipmentRepository extends CrudRepository<FormEquipment, Long> {
	
	List<FormEquipment> findAll();
}