package com.tallerbd.backend.emergency;

import java.util.ArrayList;
import java.util.List;

import com.tallerbd.backend.WebMercatorSINGLETON.WebMercatorSINGLETON;
import com.tallerbd.backend.form.Form;
import com.tallerbd.backend.form.FormDTO;
import com.tallerbd.backend.form.FormEquipment;
import com.tallerbd.backend.form.FormEquipmentRepository;
import com.tallerbd.backend.form.FormRepository;
import com.tallerbd.backend.form.FormRequirement;
import com.tallerbd.backend.form.FormRequirementRepository;
import com.tallerbd.backend.task.Task;
import com.tallerbd.backend.task.TaskRepository;
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

    public EmergencyController(EmergencyRepository emergencyRepository,
            TaskRepository taskRepository,
            FormRequirementRepository formRequirementRepository,
            FormEquipmentRepository formEquipmentRepository,
            FormRepository formRepository){
        this.emergencyRepository = emergencyRepository;
        this.taskRepository = taskRepository;
        this.formRequirementRepository = formRequirementRepository;
        this.formEquipmentRepository = formEquipmentRepository;
        this.formRepository = formRepository;
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

}