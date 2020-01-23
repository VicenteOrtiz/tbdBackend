package com.tallerbd.backend.seconddb.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.tallerbd.backend.seconddb.domain.FormEquipment;

public interface FormEquipmentRepository extends CrudRepository<FormEquipment, Long> {
	
	List<FormEquipment> findAll();
}