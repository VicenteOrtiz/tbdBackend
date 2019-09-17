package com.tallerbd.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.tallerbd.backend.model.Role;
import com.tallerbd.backend.model.Usuario;
import com.tallerbd.backend.repository.RoleRepository;
import com.tallerbd.backend.repository.UsuarioRepository;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
	
	//@Autowired
	private UsuarioRepository usuarioRepository;

	private BCryptPasswordEncoder bCryptPasswordEncoder;

	public UsuarioController(UsuarioRepository usuarioRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.usuarioRepository = usuarioRepository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}
	
	@GetMapping()
	public List<Usuario> getAll(){
		return usuarioRepository.findAll();
	}

	@GetMapping("/{id}")
	public Usuario getById(@PathVariable int id) {
		return usuarioRepository.findById(id).get();
	}

	@GetMapping("/{email}")
	public Usuario getByEmail(@PathVariable String email) {
		return usuarioRepository.findByEmail(email);
	}
	
	@PostMapping()
	@ResponseBody
	public Usuario createUsuario(@RequestBody Usuario usuario) {
		usuario.setPassword(bCryptPasswordEncoder.encode(usuario.getPassword()));
		return usuarioRepository.save(usuario);
	}
	
	@PutMapping("/{id}")
	public Usuario update(@PathVariable int id, @RequestBody Usuario newUsuario, BindingResult result) {
		return usuarioRepository.findById(id)
                .map(usuario -> {
                    usuario.setFrom( newUsuario );
                    return usuarioRepository.save( usuario );
                })
                .orElse(null);
	}
	
	@DeleteMapping("/{id}")
	public void delete(@PathVariable int id) {
		usuarioRepository.deleteById(id);
	}
}