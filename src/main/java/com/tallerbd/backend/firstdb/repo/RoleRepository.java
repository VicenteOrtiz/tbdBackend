package com.tallerbd.backend.firstdb.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.tallerbd.backend.firstdb.domain.Role;

public interface RoleRepository extends CrudRepository<Role, Long> {
	
	List<Role> findAll();

	Role findByName(String name);
}
