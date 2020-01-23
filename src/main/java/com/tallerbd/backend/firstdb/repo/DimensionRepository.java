package com.tallerbd.backend.firstdb.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.tallerbd.backend.firstdb.domain.Dimension;

public interface DimensionRepository extends CrudRepository<Dimension, Long> {

    List<Dimension> findAll();
}