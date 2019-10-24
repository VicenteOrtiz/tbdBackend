package com.tallerbd.backend.user;

import com.tallerbd.backend.role.Role;
import com.tallerbd.backend.role.RoleRepository;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/users")
public class UserController {

	private final String defaultRole = "not_assigned";

	private final UserRepository userRepository;

	private final RoleRepository roleRepository;

	public UserController(UserRepository userRepository, RoleRepository roleRepository) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
	}

	private void checkOrCreateDefaultRole(){
        if( roleRepository.findByName(this.defaultRole) == null ){
            Role not_asigned = new Role();
            not_asigned.setName(this.defaultRole);
            roleRepository.save(not_asigned);
        }
    }
	
	@GetMapping()
	public ResponseEntity getAllUsers(){
		return new ResponseEntity<>(userRepository.findAll(), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity getUserById(@PathVariable Long id) {
		User target = userRepository.findById(id).orElse(null);
        if( target == null ){
            return new ResponseEntity<>("User not Found", HttpStatus.BAD_REQUEST);
        }else{
            return new ResponseEntity<>(target, HttpStatus.OK);
        }
	}

	@PostMapping()
	public ResponseEntity createUser(@RequestBody User user) {
		if( userRepository.findByEmail( user.getEmail() ) == null ){
			checkOrCreateDefaultRole();
			user.setRole( roleRepository.findByName(this.defaultRole) );
			user.setCoordinator(null);
			user.setVolunteer(null);
            return new ResponseEntity<>(userRepository.save(user), HttpStatus.CREATED);
        }else{
            return new ResponseEntity<>("An User with that email already exist", HttpStatus.BAD_REQUEST);
        }
	}
	
	@PostMapping("/{id}")
	public ResponseEntity updateUser(@PathVariable Long id, @RequestBody User newUser) {
		User target = userRepository.findById(id).orElse(null);
        if( target == null ){
            return new ResponseEntity<>("User not Found", HttpStatus.BAD_REQUEST);
        }else{

			// Change from here to update per atribute in the future

            if( userRepository.findByEmail( newUser.getEmail() ) != null ){
                return new ResponseEntity<>("An User with that email already exist", HttpStatus.BAD_REQUEST);
            }else{
                target.setFrom(newUser);
                return new ResponseEntity<>(userRepository.save(target), HttpStatus.CREATED);
            }
        }
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity deleteUser(@PathVariable Long id) {
		if( userRepository.findById(id) == null ){
            return new ResponseEntity<>("User not Found", HttpStatus.BAD_REQUEST);
        }else{
			userRepository.deleteById(id);
			return new ResponseEntity<>("User deleted", HttpStatus.OK);
        }
	}
}