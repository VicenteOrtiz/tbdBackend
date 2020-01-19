package com.tallerbd.backend.volunteer;

import java.util.ArrayList;
import java.util.List;

import com.tallerbd.backend.WebMercatorSINGLETON.WebMercatorSINGLETON;
import com.tallerbd.backend.dimension.Dimension;
import com.tallerbd.backend.dimension.DimensionRepository;
import com.tallerbd.backend.emergency.Emergency;
import com.tallerbd.backend.emergency.EmergencyRepository;
import com.tallerbd.backend.requirement.Requirement;
import com.tallerbd.backend.requirement.RequirementRepository;
import com.tallerbd.backend.role.Role;
import com.tallerbd.backend.role.RoleRepository;
import com.tallerbd.backend.user.User;
import com.tallerbd.backend.user.UserRepository;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.util.GeometricShapeFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/volunteers")
public class VolunteerController{
    
    private final String roleName = "volunteer";

    private final String defaultPassword = "thegame";
	
	private final VolunteerRepository volunteerRepository;

	private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final DimensionRepository dimensionRepository;

    private final RequirementRepository requirementRepository;

    private final EmergencyRepository emergencyRepository;
    
    public VolunteerController ( VolunteerRepository volunteerRepository,
        UserRepository userRepository,
        RoleRepository roleRepository,
        DimensionRepository dimensionRepository,
        RequirementRepository requirementRepository,
        EmergencyRepository emergencyRepository){
		this.volunteerRepository = volunteerRepository;
		this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.dimensionRepository = dimensionRepository;
        this.requirementRepository = requirementRepository;
        this.emergencyRepository = emergencyRepository;
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
		User target = userRepository.findById(user_id).orElse(null);
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

    private VolunteerDTO generateResponse( 
        User user,
        Volunteer volunteer,
        List<Dimension> dimensions,
        List<Requirement> requirements){

        VolunteerDTO response = new VolunteerDTO();

        response.setId( user.getId() );
        response.setFirstname( user.getFirstname() );
        response.setLastname( user.getLastname() );
        response.setEmail( user.getEmail() );

        response.setGender( volunteer.getGender() );
        response.setBirth( volunteer.getBirth().toString() );
        // response.setLatitude( volunteer.getLatitude() );
        // response.setLongitude( volunteer.getLongitude() );
        // response.setLocation( volunteer.getLocation());
        response.setLongitude( volunteer.getLocation().getX() );
        response.setLatitude( volunteer.getLocation().getY() );

        response.setDimensions( dimensions );
        response.setRequirements( requirements );

        return response;
    }
    
    @PostMapping()
    @ResponseBody
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
            // volunteer.setLatitude( volunteerDTO.getLatitude() );
            // volunteer.setLongitude( volunteerDTO.getLongitude() );


            // point creation
            volunteer.setLocation( WebMercatorSINGLETON.getDefaultFactory()
                        .generatePoint(volunteerDTO.getLatitude(), volunteerDTO.getLongitude() ) );

            volunteer = volunteerRepository.save( volunteer);
            volunteer.setUser( user );

            // set volunteer to user
            user.setVolunteer( volunteer );

            //userRepository.save( user );
            
            // link and save dimensions
            List<Dimension> dimensionsAux = new ArrayList<>();

            for( Dimension dimension : volunteerDTO.getDimensions() ){
                //volunteer.getDimensions().add( dimension );
                dimension.setVolunteer( volunteer );
                dimension = dimensionRepository.save( dimension );
                dimensionsAux.add( dimension );
            }

            // link and save requirements
            List<Requirement> requirementAux = new ArrayList<>();

            for( Requirement requirement : volunteerDTO.getRequirements() ){
                //volunteer.getRequirements().add( requirement );
                requirement.setVolunteer( volunteer );
                requirementRepository.save( requirement );
                requirementAux.add( requirement );
            }

            user = userRepository.save( user );
            
            return new ResponseEntity<>( generateResponse(user, volunteer, dimensionsAux, requirementAux) , HttpStatus.CREATED);
		}else{
			return new ResponseEntity<>("An User with that email already exist", HttpStatus.BAD_REQUEST);
		}
    }
    
    @GetMapping("/{emergency_id}/{emergency_radius}")
    public ResponseEntity getVolunteersInEmergencyRadius(@PathVariable("emergency_id") Long emergency_id, @PathVariable("emergency_radius") Double emergency_radius){
        Emergency emergency = emergencyRepository.findById(emergency_id).orElse(null);
        if( emergency == null ){
            return new ResponseEntity<>( "an emergency with that id does not exist", HttpStatus.BAD_REQUEST);
        }else{
            // get emergency location
            // Location location = emergency.getLocation();
            Point emergencyLocation = emergency.getLocation();

            // generate shape
            WebMercatorSINGLETON factory = WebMercatorSINGLETON.getDefaultFactory();

            GeometricShapeFactory shapeFactory = new GeometricShapeFactory( factory.getGeometryFactory() );

            shapeFactory.setCentre( new Coordinate(emergencyLocation.getX(), emergencyLocation.getY() ) );
            shapeFactory.setSize( 2 * emergency_radius );
            shapeFactory.setNumPoints(100);

            Polygon emergencyArea = shapeFactory.createCircle();
            // nota: al generar el area no se si los puntos se generan con las proporciones del mercator

            emergency.setArea( emergencyArea );
            emergency = emergencyRepository.save(emergency);
            // borrar luego (ver emergency entity)

            // intersect with volunteer points
            // this aproach is not optimized
            checkOrCreateVolunteerRole();
            Role volunteer = roleRepository.findByName(this.roleName);
            List<User> voluntarios = volunteer.getUsers();
            
            List<VolunteerDTO> enElArea = new ArrayList<>();
            for (User user : voluntarios) {
                Point userLocation = user.getVolunteer().getLocation();
                if( emergencyArea.contains(userLocation)){
                    VolunteerDTO voluntarioBonito = generateResponse(user, user.getVolunteer(), user.getVolunteer().getDimensions(), user.getVolunteer().getRequirements() );
                    enElArea.add(voluntarioBonito);
                }
            }

            return new ResponseEntity<>(enElArea, HttpStatus.OK);
        }
    }
}