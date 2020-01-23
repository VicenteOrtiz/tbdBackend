package com.tallerbd.backend.seconddb.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="formRequirement")
public class FormRequirement{
    
    @Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "minRange")
    private int minRange;

    @Column(name = "maxRange")
    private int maxRange;

    @ManyToOne
    @JoinColumn(name="form_id")
    @JsonIgnore
	private Form form;
}