package com.tallerbd.backend.controller;

import java.util.List;

import com.tallerbd.backend.model.Role;
import com.tallerbd.backend.repository.RoleRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private RoleRepository roleRepository;
    
    @GetMapping()
	public List<Role> getAll(){
		return roleRepository.findAll();
	}

	@GetMapping("/{id}")
	public Role getById(@PathVariable int id) {
		//return usuarioRepository.findById(id).get();
		return roleRepository.findById(id).orElse(null);
    }
    
    @PostMapping()
    public ResponseEntity create(@RequestBody Role role){
        roleRepository.save(role);
        return new ResponseEntity<>("Rol " + role.getName() + " creado",HttpStatus.OK);
    }
}
