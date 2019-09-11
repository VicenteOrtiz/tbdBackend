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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;

import com.tallerbd.backend.model.Usuario;
import com.tallerbd.backend.repository.UsuarioRepository;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@GetMapping()
	public List<Usuario> getUsuarios(){
		return usuarioRepository.findAll();
	}

	@GetMapping("/{id}")
	public Usuario getByIdUsuario(@PathVariable int id) {
		return usuarioRepository.findById(id).get();
	}
	
	@PostMapping()
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public Usuario createUsuario(@RequestBody Usuario usuario, BindingResult result) {
		return usuarioRepository.save(usuario);
	}
	
	@PutMapping("/{id}")
	public Usuario updateUsuario(@PathVariable int id, @RequestBody Usuario usuario, BindingResult result) {
		Usuario c = usuarioRepository.findById(id).get();
		
		if(c != null) {
			c.setNombre(usuario.getNombre());
			c.setEmail(usuario.getEmail());
			
			return usuarioRepository.save(c);
		}
		return null;	
	}
	
	@DeleteMapping("/{id}")
	public void deleteUsuario(@PathVariable int id) {
		usuarioRepository.deleteById(id);
	}
}