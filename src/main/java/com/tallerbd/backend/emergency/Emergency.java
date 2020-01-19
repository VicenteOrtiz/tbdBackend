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

import com.tallerbd.backend.form.Form;
// import com.tallerbd.backend.location.Location;
import com.tallerbd.backend.task.Task;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

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

    // @Column(name = "latitude") // to use on front
    // private Double latitude;

    // @Column(name = "longitude") // to use on front
    // private Double longitude;

    // @OneToOne(cascade = CascadeType.ALL)
    // @JoinColumn(name="location_id")
    // private Location location;

    @Column(name="location")
    private Point location;

    @Column(name="area")
    private Polygon area;
    // borrar luego (ver volunteer controller)

    // @ManyToOne
    // @JoinColumn(name="coordinator_id")
    // private Coordinator creator;
    
    @OneToMany(mappedBy = "emergency")
    private List<Task> tasks = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="form_id")
    private Form form;
}
