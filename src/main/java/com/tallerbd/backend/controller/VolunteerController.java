package com.tallerbd.backend.controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import com.tallerbd.backend.model.Volunteer;
import com.tallerbd.backend.repository.VolunteerRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/volunteer")
public class VolunteerController {
    //para añadir una ruta agregar @GetMapping(), @PostMapping(), etc, con su respectiva
    //dependencia. ver usuarioController.

    @Autowired
    private VolunteerRepository volunteerRepository;

    @GetMapping()
    public List<Volunteer> getVolunteers(){
        return volunteerRepository.findAll();
    }

    @GetMapping("/csv")
    public List<Volunteer> seedVolunteers(){
        BufferedReader objReader = null;
        try {
            
            String strCurrentLine;

            objReader = new BufferedReader(new FileReader("src/main/resources/TBD VOLUNTARIOS.csv"));

            int i = 0;
            while ((strCurrentLine = objReader.readLine()) != null) {
                // System.out.println(strCurrentLine);
                if(i !=0){
                    String[] splitedLine = strCurrentLine.split(",");
                    Volunteer volunteer = new Volunteer();
                    volunteer.setId( Integer.parseInt(splitedLine[0]) );
                    volunteer.setName( splitedLine[1] );
                    volunteer.setLastname( splitedLine[2] );
                    volunteer.setEmail( splitedLine[3] );
                    volunteer.setGender( splitedLine[4] );
                    volunteerRepository.save(volunteer);
                }
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (objReader != null) objReader.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return volunteerRepository.findAll();
        }
    }
}
