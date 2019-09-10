package com.tallerbd.backend.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="Voluntario")
public class Volunteer {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

}
