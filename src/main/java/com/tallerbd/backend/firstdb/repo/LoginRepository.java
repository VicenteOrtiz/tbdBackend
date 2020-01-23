package com.tallerbd.backend.firstdb.repo;

import java.util.List;

import com.tallerbd.backend.firstdb.domain.Login;

import org.springframework.data.repository.CrudRepository;

public interface LoginRepository extends CrudRepository<Login, Long>{
    List<Login> findAll();
    Login findTopByOrderByIdDesc();
}