package com.tallerbd.backend.login;

import java.util.List;

import com.tallerbd.backend.login.Login;

import org.springframework.data.repository.CrudRepository;

public interface LoginRepository extends CrudRepository<Login, Long>{
    List<Login> findAll();
}