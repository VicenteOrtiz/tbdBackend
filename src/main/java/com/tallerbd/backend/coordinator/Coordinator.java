package com.tallerbd.backend.coordinator;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tallerbd.backend.emergency.Emergency;
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
	@JsonIgnore
	private User user;

	// @OneToMany(mappedBy = "creator")
	// @JsonIgnore
	// private List<Emergency> createdEmergencies = new ArrayList<>();

	public void setFrom(Coordinator coordinator){
		this.institution = coordinator.getInstitution();
	}
}