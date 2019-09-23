package com.tallerbd.backend.emergency;

import com.tallerbd.backend.form.Form;
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

    private final FormRequirementRepository formRequirementRepository;

    public EmergencyController(EmergencyRepository emergencyRepository, TaskRepository taskRepository, FormRequirementRepository formRequirementRepository){
        this.emergencyRepository = emergencyRepository;
        this.taskRepository = taskRepository;
        this.formRequirementRepository = formRequirementRepository;
    }

    @GetMapping()
	public ResponseEntity getAllEmergencies(@RequestParam(value = "page") int page, @RequestParam(value = "quantity") int quantity){
		return new ResponseEntity<>(emergencyRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
	public ResponseEntity getEmergencyById(@PathVariable("id") Long id) {
        Emergency emergency = emergencyRepository.findById(id).orElse(null);
        if( emergency == null ){
            return new ResponseEntity<>("Emergency id not found", HttpStatus.BAD_REQUEST);
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
        
        emergencyRepository.save(emergency);

        // link and save tasks
        for( Task task : emergencyDTO.getTasks() ){
            emergency.getTasks().add( task );
            task.setEmergency(emergency);
            taskRepository.save(task);
        }

        emergency.setForm(null);

        // link and save formRequirements
        // for( FormRequirement requirement : emergencyDTO.getFormRequirements() ){
        //     emergency.getFormRequirements().add( requirement );
        //     formRequirementRepository.save(requirement);
        // }
        //return new ResponseEntity<>( emergencyRepository.save(emergency), HttpStatus.OK);
        return new ResponseEntity<>( emergency, HttpStatus.OK);
    }
}