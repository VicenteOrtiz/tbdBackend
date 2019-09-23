package com.tallerbd.backend.form;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tallerbd.backend.emergency.Emergency;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="form")
public class Form{
    
    @Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "neededGender")
    private int neededGender;

    @OneToOne
    @JoinColumn(name="emergency_id")
    @JsonIgnore
    private Emergency emergency;
    
    @OneToMany(mappedBy = "form")
    private List<FormRequirement> formRequirements = new ArrayList<>();

    @OneToMany(mappedBy = "form")
    private List<FormEquipment> formEquipment = new ArrayList<>();
}