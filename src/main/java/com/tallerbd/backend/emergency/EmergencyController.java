package com.tallerbd.backend.emergency;

import com.tallerbd.backend.form.Form;
import com.tallerbd.backend.form.FormDTO;
import com.tallerbd.backend.form.FormEquipment;
import com.tallerbd.backend.form.FormEquipmentRepository;
import com.tallerbd.backend.form.FormRepository;
import com.tallerbd.backend.form.FormRequirement;
import com.tallerbd.backend.form.FormRequirementRepository;
import com.tallerbd.backend.task.Task;
import com.tallerbd.backend.task.TaskRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @PostMapping()
    public ResponseEntity createEmergency(@RequestBody EmergencyDTO emergencyDTO){

        Emergency emergency = new Emergency();
        // set base emergency atributes
        emergency.setTitle( emergencyDTO.getTitle() );
        emergency.setInCharge( emergencyDTO.getInCharge() );
        emergency.setLocation( emergencyDTO.getLocation() );
        
        emergency = emergencyRepository.save(emergency);

        // link and save tasks
        for( Task task : emergencyDTO.getTasks() ){
            emergency.getTasks().add( task );
            task.setEmergency(emergency);
            taskRepository.save(task);
        }

        emergency.setForm(null);
        return new ResponseEntity<>( emergency, HttpStatus.OK);
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