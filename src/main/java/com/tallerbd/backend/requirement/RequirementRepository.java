package com.tallerbd.backend.requirement;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface RequirementRepository extends CrudRepository<Requirement, Long> {

    List<Requirement> findAll();
}