package com.tallerbd.backend.seconddb.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.tallerbd.backend.seconddb.domain.Emergency;

public interface EmergencyRepository extends CrudRepository<Emergency, Long> {

    List<Emergency> findAll();
}
