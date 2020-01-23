package com.tallerbd.backend.seconddb.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.tallerbd.backend.seconddb.domain.Form;

public interface FormRepository extends CrudRepository<Form, Long>{

    List<Form> findAll();
}