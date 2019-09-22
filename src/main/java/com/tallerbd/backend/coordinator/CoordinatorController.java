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

	private final String roleName = "coordinator";
	
	private final CoordinatorRepository coordinatorRepository;

	private final UserRepository userRepository;

	private final RoleRepository roleRepository;
	
	public CoordinatorController ( CoordinatorRepository coordinatorRepository, UserRepository userRepository, RoleRepository roleRepository){
		this.coordinatorRepository = coordinatorRepository;
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
	}

	private void checkOrCreateCoordinatorRole(){
        if( roleRepository.findByName(this.roleName) == null ){
            Role coordinator = new Role();
            coordinator.setName(this.roleName);
            roleRepository.save(coordinator);
        }
    }
	
	@GetMapping()
	public ResponseEntity getAllCoordinators(){
		checkOrCreateCoordinatorRole();
		Role coordinator = roleRepository.findByName(this.roleName);
		return new ResponseEntity<>(coordinator.getUsers(), HttpStatus.OK);
	}
	
	@GetMapping("/{user_id}")
	public ResponseEntity getCoordinatorById(@PathVariable Long user_id) {
		User target = userRepository.findById(user_id).get();
        if( target == null ){
            return new ResponseEntity<>("User Coordinator not found", HttpStatus.BAD_REQUEST);
        }else{
			Role coordinator = roleRepository.findByName(this.roleName);

            if( target.getRole().getId() == coordinator.getId() ){
				return new ResponseEntity<>(target, HttpStatus.OK);
			}else{
				return new ResponseEntity<>("User does not have " + this.roleName + " role", HttpStatus.BAD_REQUEST);
			}
        }
	}
	
	@PostMapping("/user/{user_id}")
	public ResponseEntity createCoordinator(@RequestBody Coordinator coordinator, @PathVariable Long user_id){
        if( coordinatorRepository.findByInstitution( coordinator.getInstitution() ) == null ){

			User target = userRepository.findById(user_id).get();

			checkOrCreateCoordinatorRole();
			target.setRole( roleRepository.findByName(this.roleName) );
			target.setCoordinator( coordinator );

			coordinator.setUser( target );

			//userRepository.save( target );

			//coordinatorRepository.save( coordinator );
			
			return new ResponseEntity<>( userRepository.save( target ) , HttpStatus.CREATED);
		}else{
			return new ResponseEntity<>("A coordinator in that institution already exist", HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/{id}")
	public ResponseEntity updateCoordinator(@RequestBody Coordinator newCoordinator, @PathVariable Long id){
		if( coordinatorRepository.findByInstitution( newCoordinator.getInstitution() ) == null ){
			Coordinator target = coordinatorRepository.findById(id).get();
			if( target == null ){
				return new ResponseEntity<>("Coordinator not Found", HttpStatus.BAD_REQUEST);
			}else{
				target.setFrom(newCoordinator);
				coordinatorRepository.save(target);
				return new ResponseEntity<>( target.getUser() , HttpStatus.CREATED);
			}
		}else{
			return new ResponseEntity<>("A coordinator in that institution already exist", HttpStatus.BAD_REQUEST);
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity deleteCoordinator(@PathVariable Long id){
		if( coordinatorRepository.findById(id) == null ){
			return new ResponseEntity<>("Coordinator not Found", HttpStatus.BAD_REQUEST);
		}else{
			coordinatorRepository.deleteById(id);
			return new ResponseEntity<>("Coordinator deleted", HttpStatus.OK);
		}
	}
}