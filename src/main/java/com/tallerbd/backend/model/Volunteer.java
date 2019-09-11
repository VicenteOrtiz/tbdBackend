package com.tallerbd.backend.model;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name="Voluntario")
public class Volunteer {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@Column(name ="name")
	private String name;
	
	@ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "task_volunteer",
        joinColumns = @JoinColumn(name = "task_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "volunteer_id", referencedColumnName = "id"))
    private Set<Task> tasks;

	//public Volunteer(String name, Task... tasks) {
    //    this.name = name;
    //    this.tasks = Stream.of(tasks).collect(Collectors.toSet());
    //    this.tasks.forEach(x -> x.getVolunteers().add(this));
    //}
}
