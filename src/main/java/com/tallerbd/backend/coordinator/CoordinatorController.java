package com.tallerbd.backend.coordinator;

import com.tallerbd.backend.role.Role;
import com.tallerbd.backend.role.RoleRepository;
import com.tallerbd.backend.user.User;
import com.tallerbd.backend.user.UserRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/coordinators")
public class CoordinatorController{
	
	private final CoordinatorRepository coordinatorRepository;

	private final UserRepository userRepository;

	private final RoleRepository roleRepository;
	
	public CoordinatorController ( CoordinatorRepository coordinatorRepository, UserRepository userRepository, RoleRepository roleRepository){
		this.coordinatorRepository = coordinatorRepository;
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
	}
	
	@GetMapping()
	@ResponseBody
	public ResponseEntity getAll(){
		return new ResponseEntity<>(coordinatorRepository.findAll(), HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	@ResponseBody
	public ResponseEntity getById(@PathVariable Long id) {
		Coordinator target = coordinatorRepository.findById(id).get();
		if( target == null ){
			return new ResponseEntity<>("Role not Found", HttpStatus.BAD_REQUEST);
		}else{
			return new ResponseEntity<>(target, HttpStatus.OK);
		}
	}

	private void checkOrCreateCoordinatorRole(){
        if( roleRepository.findByName("coordinator") == null ){
            Role coordinator = new Role();
            coordinator.setName("coordinator");
            roleRepository.save(coordinator);
        }
    }
	
	@PostMapping("/users/{id}")
	@ResponseBody
	public ResponseEntity create(@RequestBody Coordinator coordinator, @PathVariable Long id){
        if( coordinatorRepository.findByInstitution( coordinator.getInstitution() ) == null ){

			User target = userRepository.findById(id).get();

			checkOrCreateCoordinatorRole();
			target.setRole( roleRepository.findByName("coordinator") );

			target.setCoordinator( coordinator );
			coordinator.setUser( target );

			coordinatorRepository.save( coordinator );

			return new ResponseEntity<>( userRepository.save( target ), HttpStatus.CREATED);
		}else{
			return new ResponseEntity<>("A coordinator in that institution already exist", HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/{id}")
	@ResponseBody
	public ResponseEntity update(@RequestBody Coordinator newCoordinator, @PathVariable Long id){
		Coordinator target = coordinatorRepository.findById(id).get();
		if( target == null ){
			return new ResponseEntity<>("Coordinator not Found", HttpStatus.BAD_REQUEST);
		}else{
			if( coordinatorRepository.findByInstitution( newCoordinator.getInstitution() ) != null ){
				return new ResponseEntity<>("A coordinator in that institution already exist", HttpStatus.BAD_REQUEST);
			}else{
				target.setFrom(newCoordinator);
				return new ResponseEntity<>(coordinatorRepository.save(target), HttpStatus.CREATED);
			}
		}
	}
	
	@DeleteMapping("/{id}")
	@ResponseBody
	public ResponseEntity delete(@PathVariable Long id){
		if( coordinatorRepository.findById(id) == null ){
			return new ResponseEntity<>("Coordinator not Found", HttpStatus.BAD_REQUEST);
		}else{
			coordinatorRepository.deleteById(id);
			return new ResponseEntity<>("Coordinator deleted", HttpStatus.OK);
		}
	}
}