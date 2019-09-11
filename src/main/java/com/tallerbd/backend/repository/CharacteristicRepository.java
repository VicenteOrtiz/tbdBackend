package com.tallerbd.backend.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.tallerbd.backend.model.Characteristic;

public interface CharacteristicRepository extends CrudRepository<Characteristic, Integer> {
	
	List<Characteristic> findAll();

}
