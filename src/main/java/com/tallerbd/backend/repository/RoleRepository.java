package com.tallerbd.backend.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.tallerbd.backend.model.Role;

public interface RoleRepository extends CrudRepository<Role, Integer> {
	
	List<Role> findAll();

}
