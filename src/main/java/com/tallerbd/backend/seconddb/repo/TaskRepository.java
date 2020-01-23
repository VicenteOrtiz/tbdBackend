package com.tallerbd.backend.seconddb.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.tallerbd.backend.seconddb.domain.Task;

public interface TaskRepository extends CrudRepository<Task, Long> {
	
	List<Task> findAll();
}
