package com.tallerbd.backend.role;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.tallerbd.backend.role.Role;

public interface RoleRepository extends CrudRepository<Role, Long> {
	
	List<Role> findAll();

	Role findByName(String name);
}
