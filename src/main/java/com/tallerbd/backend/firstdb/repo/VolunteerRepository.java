package com.tallerbd.backend.firstdb.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.tallerbd.backend.firstdb.domain.Volunteer;

public interface VolunteerRepository extends CrudRepository<Volunteer, Long> {

    List<Volunteer> findAll();
}