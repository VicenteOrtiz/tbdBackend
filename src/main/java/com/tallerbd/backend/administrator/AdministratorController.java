package com.tallerbd.backend.administrator;

import com.tallerbd.backend.role.Role;
import com.tallerbd.backend.role.RoleRepository;
import com.tallerbd.backend.user.User;
import com.tallerbd.backend.user.UserRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/admin")
public class AdministratorController{

    /*
    This controller only works with user and role entity.
    There isn't an admin entity because an admin user doesn't have agregated atributes (yet),
    as oposed to coordinator or volunteer entities.
    */

    private final String roleName = "admin";

    private final UserRepository userRepository;

	private final RoleRepository roleRepository;

	public AdministratorController(UserRepository userRepository, RoleRepository roleRepository) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
    }

    private void checkOrCreateAdminRole(){
        if( roleRepository.findByName(this.roleName) == null ){
            Role admin = new Role();
            admin.setName(this.roleName);
            roleRepository.save(admin);
        }
    }

    @GetMapping()
    public ResponseEntity getAllAdminUsers(){
        checkOrCreateAdminRole();
        Role admin = roleRepository.findByName(this.roleName);
        return new ResponseEntity<>( admin.getUsers() , HttpStatus.OK);
    }

    @GetMapping("/{user_id}")
    public ResponseEntity getAdminUserById(@PathVariable Long user_id){
        User target = userRepository.findById(user_id).orElse(null);
        if( target == null ){
            return new ResponseEntity<>("User not found", HttpStatus.BAD_REQUEST);
        }else{
            checkOrCreateAdminRole();
            Role admin = roleRepository.findByName(this.roleName);

            if( target.getRole().getId() == admin.getId() ){
                return new ResponseEntity<>(target, HttpStatus.OK);
            }else{
                return new ResponseEntity<>("User is not administrator", HttpStatus.BAD_REQUEST);
            }
        }
    }
    
    // @GetMapping("/user/{id}")
    // public ResponseEntity setAdminRoleToUser(@PathVariable Long id){
    //     User target = userRepository.findById(id).orElse(null);

    //     checkOrCreateAdminRole();

    //     Role admin = roleRepository.findByName(this.roleName);
    //     target.setRole(admin);
        
    //     return new ResponseEntity<>(userRepository.save(target), HttpStatus.OK);
    // }

    @PostMapping()
    public ResponseEntity createAdmin(@RequestBody User user){
        if( userRepository.findByEmail( user.getEmail() ) == null ){

            checkOrCreateAdminRole();
            Role admin = roleRepository.findByName(this.roleName);

            user.setRole(admin);

            return new ResponseEntity<>(userRepository.save(user), HttpStatus.CREATED);
        }else{
            return new ResponseEntity<>("An User with that email already exist", HttpStatus.BAD_REQUEST);
        }
    }    
}