package com.tallerbd.backend.form;

import com.tallerbd.backend.emergency.Emergency;
import com.tallerbd.backend.emergency.EmergencyController;
import com.tallerbd.backend.emergency.EmergencyDTO;
import com.tallerbd.backend.emergency.EmergencyRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/form")
public class FormController{

    private final EmergencyRepository emergencyRepository;

    private final FormRequirementRepository formRequirementRepository;

    private final FormEquipmentRepository formEquipmentRepository;

    private final FormRepository formRepository;

    public FormController( EmergencyRepository emergencyRepository,
        FormRequirementRepository formRequirementRepository,
        FormEquipmentRepository formEquipmentRepository,
        FormRepository formRepository){
        this.emergencyRepository = emergencyRepository;
        this.formRequirementRepository = formRequirementRepository;
        this.formEquipmentRepository = formEquipmentRepository;
        this.formRepository = formRepository;
    }

    @PostMapping("/emergency/{id}")
    public ResponseEntity createEmergency(@RequestBody FormDTO formDTO, @PathVariable Long id){

        Emergency emergency = emergencyRepository.findById(id).orElse(null);

        if( emergency == null ){
            return new ResponseEntity<>( "Emergency not found", HttpStatus.BAD_REQUEST);
        }else{

            Form form = new Form();
            form.setNeededGender( formDTO.getNeededGender() );

            emergency.setForm(form);
            form.setEmergency(emergency);

            emergencyRepository.save(emergency);
            //formRepository.save(form);

            // link and save tasks
            for( FormRequirement requirement : formDTO.getFormRequirements() ){
                form.getFormRequirements().add( requirement );
                requirement.setForm(form);
                formRequirementRepository.save(requirement);
            }

            // link and save tasks
            for( FormEquipment equipment : formDTO.getFormEquipment() ){
                form.getFormEquipment().add( equipment );
                equipment.setForm(form);
                formEquipmentRepository.save( equipment );
            }

            //formRepository.save(form);

            return new ResponseEntity<>( emergency, HttpStatus.OK);
        }
    }
}