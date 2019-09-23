package com.tallerbd.backend.volunteer;

import com.tallerbd.backend.role.Role;
import com.tallerbd.backend.role.RoleRepository;
import com.tallerbd.backend.user.User;
import com.tallerbd.backend.user.UserRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/volunteers")
public class VolunteerController{
    
    private final String roleName = "volunteer";

    private final String defaultPassword = "password1234";
	
	private final VolunteerRepository volunteerRepository;

	private final UserRepository userRepository;

    private final RoleRepository roleRepository;
    
    public VolunteerController ( VolunteerRepository volunteerRepository, UserRepository userRepository, RoleRepository roleRepository){
		this.volunteerRepository = volunteerRepository;
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
    }
    
    private void checkOrCreateVolunteerRole(){
        if( roleRepository.findByName(this.roleName) == null ){
            Role volunteer = new Role();
            volunteer.setName(this.roleName);
            roleRepository.save(volunteer);
        }
    }

    @GetMapping()
	public ResponseEntity getAllVolunteers(){
		checkOrCreateVolunteerRole();
		Role volunteer = roleRepository.findByName(this.roleName);
		return new ResponseEntity<>(volunteer.getUsers(), HttpStatus.OK);
    }
    
    @GetMapping("/{user_id}")
	public ResponseEntity getVolunteerById(@PathVariable Long user_id) {
		User target = userRepository.findById(user_id).get();
        if( target == null ){
            return new ResponseEntity<>("User Volunteer not found", HttpStatus.BAD_REQUEST);
        }else{
			Role coordinator = roleRepository.findByName(this.roleName);

            if( target.getRole().getId() == coordinator.getId() ){
				return new ResponseEntity<>(target, HttpStatus.OK);
			}else{
				return new ResponseEntity<>("User does not have " + this.roleName + " role", HttpStatus.BAD_REQUEST);
			}
        }
    }
    
    @PostMapping()
	public ResponseEntity createVolunteer(@RequestBody VolunteerDTO volunteerDTO){
        if( userRepository.findByEmail( volunteerDTO.getEmail() ) == null ){

            User user = new User();

            // set base user atributes
            user.setFirstname( volunteerDTO.getFirstname() );
            user.setLastname( volunteerDTO.getLastname() );
            user.setEmail( volunteerDTO.getEmail() );
            user.setPassword( this.defaultPassword );

            // set volunteer role
            checkOrCreateVolunteerRole();
            user.setRole( roleRepository.findByName(this.roleName) );

            // set null coordinator because role
            user.setCoordinator(null);

            Volunteer volunteer = new Volunteer();

            // set basic volunteer atributes
            volunteer.setGender( volunteerDTO.getGender() );
            volunteer.setBirth( volunteerDTO.getBirth() );
            volunteer.setLatitude( volunteerDTO.getLatitude() );
            volunteer.setLongitude( volunteerDTO.getLongitude() );
            // set dimensions
            // set requirements
            volunteer.setUser( user );

            // set volunteer to user
            user.setVolunteer( volunteer );

			return new ResponseEntity<>( userRepository.save( user ) , HttpStatus.CREATED);
		}else{
			return new ResponseEntity<>("An User with that email already exist", HttpStatus.BAD_REQUEST);
		}
    }
    
    // @PostMapping("/test")
    // public ResponseEntity test(@RequestBody String json){
    //     return new ResponseEntity<>( json , HttpStatus.CREATED);
    // }
}