package com.tallerbd.backend.volunteer;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface VolunteerRepository extends CrudRepository<Volunteer, Long> {

    List<Volunteer> findAll();
}