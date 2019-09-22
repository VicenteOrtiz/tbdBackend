package com.tallerbd.backend.coordinator;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tallerbd.backend.user.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="coordinator")
public class Coordinator{

    @Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "institution", unique = true)
	private String institution;
	
	@OneToOne(mappedBy = "coordinator", cascade = CascadeType.ALL)
	@JoinColumn(name = "user_id")
	@JsonIgnore
	private User user;

	public void setFrom(Coordinator coordinator){
		this.institution = coordinator.getInstitution();
	}
}