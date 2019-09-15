package com.tallerbd.backend.controller;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;

import com.tallerbd.backend.model.Characteristic;
import com.tallerbd.backend.repository.CharacteristicRepository;

@RestController
// @RequestMapping("/characteristic") ESTO NO FUNCIONA MAN ):
public class CharacteristicController {
    //para añadir una ruta agregar @GetMapping(), @PostMapping(), etc, con su respectiva
    //dependencia. ver usuarioController.
	
	@Autowired 
	private CharacteristicRepository characteristicRepository;
	
	@RequestMapping(value="/characteristics", method= RequestMethod.GET)
	public List<Characteristic> getCharacteristics(){
		return characteristicRepository.findAll();
	}
	
	@RequestMapping(value="/characteristics", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public Characteristic createUser(@RequestBody Characteristic characteristic) {
		
		Characteristic characteristicToCreate = new Characteristic();
		characteristicToCreate.setDescripcion(characteristic.getDescripcion());

		
		return characteristicRepository.save(characteristicToCreate);
	}
	
}
