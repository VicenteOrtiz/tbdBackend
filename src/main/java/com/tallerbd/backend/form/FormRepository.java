package com.tallerbd.backend.form;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface FormRepository extends CrudRepository<Form, Long>{

    List<Form> findAll();
}