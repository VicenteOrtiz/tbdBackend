package com.tallerbd.backend.administrator;

import com.tallerbd.backend.role.Role;
import com.tallerbd.backend.role.RoleRepository;
import com.tallerbd.backend.user.User;
import com.tallerbd.backend.user.UserRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdministratorController{

    private final UserRepository userRepository;

	private final RoleRepository roleRepository;

	public AdministratorController(UserRepository userRepository, RoleRepository roleRepository) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
    }

    private void checkOrCreateAdminRole(){
        if( roleRepository.findByName("admin") == null ){
            Role admin = new Role();
            admin.setName("admin");
            roleRepository.save(admin);
        }
    }
    
    @GetMapping("/users/{id}")
    public ResponseEntity setAdminRoleToUser(@PathVariable Long id){
        User target = userRepository.findById(id).get();

        checkOrCreateAdminRole();

        Role admin = roleRepository.findByName("admin");
        target.setRole(admin);
        
        return new ResponseEntity<>(userRepository.save(target), HttpStatus.OK);
    }
}