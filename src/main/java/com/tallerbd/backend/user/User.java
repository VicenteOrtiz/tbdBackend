package com.tallerbd.backend.user;

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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tallerbd.backend.coordinator.Coordinator;
import com.tallerbd.backend.role.Role;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="usuario")
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name="firstname")
	private String firstname;

	@Column(name="lastname")
	private String lastname;
	
	@Column(name="email", unique = true)
	private String email;

	@Column(name = "password")
	private String password;

	@ManyToOne
	@JoinColumn(name="role_id")
	private Role role;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "coordinator_id")
	private Coordinator coordinator;

	public void setFrom(User newUser){
		this.firstname = newUser.getFirstname();
		this.lastname = newUser.getLastname();
		this.email = newUser.getEmail();
		this.password = newUser.getPassword();
		this.role = newUser.getRole();
	}
}
