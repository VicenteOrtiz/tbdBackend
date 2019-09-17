package com.tallerbd.backend.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.tallerbd.backend.model.Usuario;

public interface UsuarioRepository extends CrudRepository<Usuario, Integer> {
	
	List<Usuario> findAll();

	Usuario findByEmail(String email);

}
