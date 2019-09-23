package com.tallerbd.backend.emergency;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface EmergencyRepository extends CrudRepository<Emergency, Long> {

    List<Emergency> findAll();
}
