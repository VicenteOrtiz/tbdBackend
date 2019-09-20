package com.tallerbd.backend.user;

import com.tallerbd.backend.role.Role;
import com.tallerbd.backend.role.RoleRepository;

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


@RestController
@RequestMapping("/users")
public class UserController {
	
	//@Autowired
	private final UserRepository userRepository;

	private final RoleRepository roleRepository;

	public UserController(UserRepository userRepository, RoleRepository roleRepository) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
	}
	
	@GetMapping()
	@ResponseBody
	public ResponseEntity getAll(){
		return new ResponseEntity<>(userRepository.findAll(), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	@ResponseBody
	public ResponseEntity getById(@PathVariable Long id) {
		User target = userRepository.findById(id).get();
        if( target == null ){
            return new ResponseEntity<>("User not Found", HttpStatus.BAD_REQUEST);
        }else{
            return new ResponseEntity<>(target, HttpStatus.OK);
        }
	}

	@GetMapping("/{user_id}/roles/{role_id}")
	@ResponseBody
	public ResponseEntity setRoleToUser(@PathVariable Long user_id, @PathVariable Long role_id){
		Role targetRole = roleRepository.findById(role_id).get();
		User targetUser = userRepository.findById(user_id).get();

		if(targetRole != null && targetUser != null){
			//targetRole.getUsers().add(targetRole);
			targetUser.setRole(targetRole);
			userRepository.save(targetUser);
			//return new ResponseEntity<>("User with email " + targetUser.getEmail() + " now have Role " + targetRole.getName(), HttpStatus.OK);
			return new ResponseEntity<>(userRepository.findById(user_id), HttpStatus.OK);
		}else{
			return new ResponseEntity<>("User or Role does not exist", HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/{id}/role")
	@ResponseBody
	public ResponseEntity getUserRole(@PathVariable Long id){
		User target = userRepository.findById(id).get();
		if(target == null){
			return new ResponseEntity<>("User not found", HttpStatus.BAD_REQUEST);
		}else{
			if( target.getRole() == null ){
				return new ResponseEntity<>("User without role", HttpStatus.BAD_REQUEST);
			}
			return new ResponseEntity<>(target.getRole(), HttpStatus.OK);
		}
	}
	
	@PostMapping()
	@ResponseBody
	public ResponseEntity create(@RequestBody User user) {
		if( userRepository.findByEmail( user.getEmail() ) == null ){
            return new ResponseEntity<>(userRepository.save(user), HttpStatus.CREATED);
        }else{
            return new ResponseEntity<>("An User with that email already exist", HttpStatus.BAD_REQUEST);
        }
	}
	
	@PostMapping("/{id}")
	@ResponseBody
	public ResponseEntity update(@PathVariable Long id, @RequestBody User newUser) {
		User target = userRepository.findById(id).get();
        if( target == null ){
            return new ResponseEntity<>("User not Found", HttpStatus.BAD_REQUEST);
        }else{
            if( userRepository.findByEmail( newUser.getEmail() ) != null ){
                return new ResponseEntity<>("An User with that email already exist", HttpStatus.BAD_REQUEST);
            }else{
                target.setFrom(newUser);
                return new ResponseEntity<>(userRepository.save(target), HttpStatus.CREATED);
            }
        }
	}
	
	@DeleteMapping("/{id}")
	@ResponseBody
	public ResponseEntity delete(@PathVariable Long id) {
		if( userRepository.findById(id) == null ){
            return new ResponseEntity<>("User not Found", HttpStatus.BAD_REQUEST);
        }else{
			userRepository.deleteById(id);
			return new ResponseEntity<>("User deleted", HttpStatus.OK);
        }
	}
}