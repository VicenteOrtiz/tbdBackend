package com.tallerbd.backend.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="Usuario")
public class Usuario {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;	
	
	@Column(name="nombre")
	private String nombre;
	
	@Column(name="email", unique = true)
	private String email;

	@Column(name = "password")
	private String password;

	// @ManyToOne(cascade = {CascadeType.ALL})
	// @JsonIgnore
	// @JoinColumn(name="role_id")
	// private Role role;

	public Usuario(String nombre, String email, String password){
		this.nombre = nombre;
		this.email = email;
		this.password = password;
	}

	public void setFrom(Usuario newUser){
		this.nombre = newUser.getNombre();
		this.email = newUser.getEmail();
		this.password = newUser.getPassword();
		//this.role = newUser.getRole();
	}
}
