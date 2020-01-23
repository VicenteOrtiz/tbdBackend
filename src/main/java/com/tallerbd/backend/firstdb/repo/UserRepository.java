package com.tallerbd.backend.firstdb.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.tallerbd.backend.firstdb.domain.User;

public interface UserRepository extends CrudRepository<User, Long> {
	
	List<User> findAll();

	User findByEmail(String email);

	User findTopByOrderByIdDesc();

}
