package com.tallerbd.backend.seconddb.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

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

    @OneToOne(mappedBy = "form", cascade = CascadeType.ALL)
    @JsonIgnore
    private Emergency emergency;
    
    @OneToMany(mappedBy = "form")
    private List<FormRequirement> formRequirements = new ArrayList<>();

    @OneToMany(mappedBy = "form")
    private List<FormEquipment> formEquipment = new ArrayList<>();
}