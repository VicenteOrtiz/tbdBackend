package com.tallerbd.backend.firstdb.controller;

import com.tallerbd.backend.firstdb.domain.Role;
import com.tallerbd.backend.firstdb.repo.RoleRepository;
import com.tallerbd.backend.firstdb.domain.User;
import com.tallerbd.backend.firstdb.repo.UserRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/roles")
public class RoleController {
	
	private final RoleRepository roleRepository;

	private final UserRepository userRepository;
	
	public RoleController (RoleRepository roleRepository, UserRepository userRepository){
		this.roleRepository = roleRepository;
		this.userRepository = userRepository;
	}
	
	@GetMapping()
	@ResponseBody
	public ResponseEntity getAll(){
		return new ResponseEntity<>(roleRepository.findAll(), HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	@ResponseBody
	public ResponseEntity getById(@PathVariable Long id) {
		Role target = roleRepository.findById(id).orElse(null);
		if( target == null ){
			return new ResponseEntity<>("Role not Found", HttpStatus.BAD_REQUEST);
		}else{
			return new ResponseEntity<>(target, HttpStatus.OK);
		}
	}

	@GetMapping("/name/{role_name}")
	@ResponseBody
	public ResponseEntity getByName(@PathVariable String role_name) {
		Role target = roleRepository.findByName(role_name);
		if( target == null ){
			return new ResponseEntity<>("Role not Found", HttpStatus.BAD_REQUEST);
		}else{
			return new ResponseEntity<>(target, HttpStatus.OK);
		}
	}

	@GetMapping("/revoke/{user_id}")
    public ResponseEntity revokeRoleToUser(@PathVariable Long user_id){
        User target = userRepository.findById(user_id).orElse(null);

        target.setRole(null);
        
        return new ResponseEntity<>(userRepository.save(target), HttpStatus.OK);
    }
	
	@PostMapping()
	@ResponseBody
	public ResponseEntity create(@RequestBody Role role){
		if( roleRepository.findByName( role.getName() ) == null ){
			return new ResponseEntity<>(roleRepository.save(role), HttpStatus.CREATED);
		}else{
			return new ResponseEntity<>("Role " + role.getName() + " already exist", HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/{id}")
	@ResponseBody
	public ResponseEntity update(@RequestBody Role newRole, @PathVariable Long id){
		Role target = roleRepository.findById(id).orElse(null);
		if( target == null ){
			return new ResponseEntity<>("Role not Found", HttpStatus.BAD_REQUEST);
		}else{
			if( roleRepository.findByName( newRole.getName() ) != null ){
				return new ResponseEntity<>("Role " + newRole.getName() + " already exist", HttpStatus.BAD_REQUEST);
			}else{
				//target.setFrom(newRole); // if uncommented, uncomment setFrom on model too
				target.setName(newRole.getName());
				return new ResponseEntity<>(roleRepository.save(target), HttpStatus.CREATED);
			}
		}
	}
	
	@DeleteMapping("/{id}")
	@ResponseBody
	public ResponseEntity delete(@PathVariable Long id){
		if( roleRepository.findById(id) == null ){
			return new ResponseEntity<>("Role not Found", HttpStatus.BAD_REQUEST);
		}else{
			roleRepository.deleteById(id);
			return new ResponseEntity<>("Role deleted", HttpStatus.OK);
		}
	}
}