package com.tallerbd.backend.seconddb.controller;

import java.util.ArrayList;
import java.util.List;

import com.tallerbd.backend.WebMercatorSINGLETON.WebMercatorSINGLETON;
import com.tallerbd.backend.firstdb.domain.User;
import com.tallerbd.backend.firstdb.domain.Volunteer;
import com.tallerbd.backend.firstdb.repo.UserRepository;
import com.tallerbd.backend.firstdb.repo.VolunteerRepository;
import com.tallerbd.backend.seconddb.domain.Emergency;
import com.tallerbd.backend.seconddb.domain.Form;
import com.tallerbd.backend.seconddb.domain.Task;
import com.tallerbd.backend.seconddb.dto.EmergencyDTO;
import com.tallerbd.backend.seconddb.dto.FormDTO;
import com.tallerbd.backend.seconddb.repo.EmergencyRepository;
import com.tallerbd.backend.seconddb.repo.FormEquipmentRepository;
import com.tallerbd.backend.seconddb.repo.FormRepository;
import com.tallerbd.backend.seconddb.repo.FormRequirementRepository;
import com.tallerbd.backend.seconddb.repo.TaskRepository;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.util.GeometricShapeFactory;

import com.tallerbd.backend.seconddb.domain.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/emergencies")
public class EmergencyController{

    private final EmergencyRepository emergencyRepository;

    private final TaskRepository taskRepository;

    private final FormRepository formRepository;

    private final FormRequirementRepository formRequirementRepository;

    private final FormEquipmentRepository formEquipmentRepository;

    private final VolunteerRepository volunteerRepository;
    
    private final UserRepository userRepository;

    public EmergencyController(EmergencyRepository emergencyRepository,
            TaskRepository taskRepository,
            FormRequirementRepository formRequirementRepository,
            FormEquipmentRepository formEquipmentRepository,
            FormRepository formRepository,
            VolunteerRepository volunteerRepository,
            UserRepository userRepository){
        this.emergencyRepository = emergencyRepository;
        this.taskRepository = taskRepository;
        this.formRequirementRepository = formRequirementRepository;
        this.formEquipmentRepository = formEquipmentRepository;
        this.formRepository = formRepository;
        this.volunteerRepository = volunteerRepository;
        this.userRepository = userRepository;
    }

    @GetMapping()
	public ResponseEntity getAllEmergencies(){
		return new ResponseEntity<>(emergencyRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
	public ResponseEntity getEmergencyById(@PathVariable("id") Long id) {
        Emergency emergency = emergencyRepository.findById(id).orElse(null);
        if( emergency == null ){
            return new ResponseEntity<>( "an emergency with that id does not exist", HttpStatus.BAD_REQUEST);
        }else{
            return new ResponseEntity<>(emergency, HttpStatus.OK);
        }
    }
    
    private EmergencyDTO generateResponse( Emergency emergency,List<Task> tasks){

        EmergencyDTO response = new EmergencyDTO();

        response.setId( emergency.getId() );
        response.setTitle( emergency.getTitle() );
        response.setInCharge( emergency.getInCharge() );
        // response.setLatitude( emergency.getLatitude() );
        // response.setLongitude( emergency.getLongitude() );
        response.setLongitude( emergency.getLocation().getX() );
        response.setLatitude( emergency.getLocation().getY() );

        response.setTasks( tasks );

        return response;
    }

    @PostMapping()
    public ResponseEntity createEmergency(@RequestBody EmergencyDTO emergencyDTO){

        // create emergency
        Emergency emergency = new Emergency();

        // set base emergency atributes
        emergency.setTitle( emergencyDTO.getTitle() );
        emergency.setInCharge( emergencyDTO.getInCharge() );
        // emergency.setLatitude( emergencyDTO.getLatitude() );
        // emergency.setLongitude( emergencyDTO.getLongitude() );
        
        // point creation
        WebMercatorSINGLETON factory = WebMercatorSINGLETON.getDefaultFactory();

        emergency.setLocation( factory.generatePoint(emergencyDTO.getLatitude(), emergencyDTO.getLongitude() ) );
        
        // Location location = new Location();
        // location.setPoint(emergencyDTO.getLatitude(), emergencyDTO.getLongitude());

        // location = locationRepository.save(location);

        // emergency.setLocation( location );

        // emergency.setForm(null);

        emergency = emergencyRepository.save(emergency);

        // link and save tasks
        List<Task> tasksAux = new ArrayList<>();
        for( Task task : emergencyDTO.getTasks() ){
            task.setEmergency(emergency);
            taskRepository.save(task);
            tasksAux.add( task );
        }
        return new ResponseEntity<>( generateResponse( emergency, tasksAux), HttpStatus.OK);
	}
	
    @PostMapping("/add-task/{emergency_id}")
    public ResponseEntity addTaskToEmergency(@RequestBody Task task, @PathVariable("emergency_id") Long emergency_id){
		Emergency emergency = emergencyRepository.findById(emergency_id).orElse(null);
		if( emergency == null ){
			return new ResponseEntity<>( "an emergency with that id does not exist", HttpStatus.BAD_REQUEST);
		}else{
			emergency.getTasks().add( task );
            task.setEmergency(emergency);
            taskRepository.save(task);
			return new ResponseEntity<>( emergency, HttpStatus.OK);
		}
    }
    
    @PostMapping("/link-form/{emergency_id}")
    public ResponseEntity linkFormToEmergency(@RequestBody FormDTO formDTO, @PathVariable("emergency_id") Long emergency_id){

        Emergency emergency = emergencyRepository.findById(emergency_id).orElse(null);

        if( emergency == null ){
            return new ResponseEntity<>( "an emergency with that id does not exist", HttpStatus.BAD_REQUEST);
        }else{

            Form form = new Form();
            form.setNeededGender( formDTO.getNeededGender() );

            form.setEmergency(emergency);
            form = formRepository.save(form);

            // link and save requirement
            for( FormRequirement requirement : formDTO.getFormRequirements() ){
                form.getFormRequirements().add( requirement );
                // requirement.setForm(form);
                formRequirementRepository.save(requirement);
            }

            // link and save equipment
            for( FormEquipment equipment : formDTO.getFormEquipment() ){
                form.getFormEquipment().add( equipment );
                // equipment.setForm(form);
                formEquipmentRepository.save( equipment );
            }

            emergency.setForm(form);

            return new ResponseEntity<>( emergencyRepository.save(emergency), HttpStatus.OK);
        }
    }

    @GetMapping("/{user_id}/{search_radius}")
    public ResponseEntity searchEmergenciesNearby(@PathVariable("user_id") Long user_id, @PathVariable("search_radius") Double search_radius){
        User user = userRepository.findById(user_id).orElse(null);
        if( user == null ){
            return new ResponseEntity<>("User not Found", HttpStatus.BAD_REQUEST);
        }else{
            Volunteer volunteer = volunteerRepository.findById( user.getVolunteer().getId() ).orElse(null);
            if( volunteer == null ){
                return new ResponseEntity<>("User Volunteer not found", HttpStatus.BAD_REQUEST);
            }else{
                // get volunteer location
                Point volunteerLocation = volunteer.getLocation();

                // generate shape
                WebMercatorSINGLETON factory = WebMercatorSINGLETON.getDefaultFactory();

                GeometricShapeFactory shapeFactory = new GeometricShapeFactory( factory.getGeometryFactory() );

                shapeFactory.setCentre( new Coordinate(volunteerLocation.getX(), volunteerLocation.getY() ) );
                shapeFactory.setSize( 2 * search_radius );
                shapeFactory.setNumPoints(100);

                Polygon searchArea = shapeFactory.createCircle();
                // nota: al generar el area no se si los puntos se generan con las proporciones del mercator

                // intersect with emergency points
                // this aproach is not optimized
                List<Emergency> emergencies = emergencyRepository.findAll();
                
                List<EmergencyDTO> enElArea = new ArrayList<>();
                for (Emergency emergency : emergencies) {
                    Point emergencyLocation = emergency.getLocation();
                    if( searchArea.contains(emergencyLocation)){
                        EmergencyDTO emergenciaBonita = generateResponse(emergency, emergency.getTasks() );
                        enElArea.add(emergenciaBonita);
                    }
                }

                return new ResponseEntity<>(enElArea, HttpStatus.OK);
            }
        }
    }
}