package com.tallerbd.backend.emergency;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
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
import com.tallerbd.backend.form.Form;
import com.tallerbd.backend.form.FormRequirement;
import com.tallerbd.backend.task.Task;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="emergency")
public class Emergency{

    @Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Column(name = "title")
    private String title;

    @Column(name = "inCharge")
    private String inCharge;

    @Column(name = "location")
    private String location; // cambiar para usar con postgis

    // @ManyToOne
    // @JoinColumn(name="coordinator_id")
    // private Coordinator creator;
    
    @OneToMany(mappedBy = "emergency")
    private List<Task> tasks = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="form_id")
    private Form form;
}
