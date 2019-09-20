package com.tallerbd.backend.user;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
	
	List<User> findAll();

	User findByEmail(String email);

}
