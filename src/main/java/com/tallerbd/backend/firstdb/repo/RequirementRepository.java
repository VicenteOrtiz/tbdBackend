package com.tallerbd.backend.firstdb.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.tallerbd.backend.firstdb.domain.Requirement;

public interface RequirementRepository extends CrudRepository<Requirement, Long> {

    List<Requirement> findAll();
}