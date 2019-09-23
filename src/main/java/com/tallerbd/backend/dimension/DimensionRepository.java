package com.tallerbd.backend.dimension;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface DimensionRepository extends CrudRepository<Dimension, Long> {

    List<Dimension> findAll();
}