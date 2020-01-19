package com.tallerbd.backend.volunteer;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
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

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tallerbd.backend.dimension.Dimension;
// import com.tallerbd.backend.location.Location;
import com.tallerbd.backend.requirement.Requirement;
import com.tallerbd.backend.user.User;
import com.vividsolutions.jts.geom.Point;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="volunteer")
public class Volunteer{

    @Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Column(name = "gender")
    private int gender;

    @Column(name = "birth")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birth;

    // @Column(name = "latitude") // to use on front
    // private Double latitude;

    // @Column(name = "longitude") // to use on front
    // private Double longitude;

    // @OneToOne(cascade = CascadeType.ALL)
    // @JoinColumn(name="location_id")
    // private Location location;

    @Column(name="location")
    private Point location;

    @OneToMany(mappedBy = "volunteer")
    private List<Dimension> dimensions = new ArrayList<>();

    @OneToMany(mappedBy = "volunteer")
    private List<Requirement> requirements = new ArrayList<>();

    @OneToOne(mappedBy = "volunteer", cascade = CascadeType.ALL)
	@JsonIgnore
    private User user;
    
    public int getAge() {
        return Period.between(this.getBirth(), LocalDate.now()).getYears();
    }

    public void setBirth(String birth){
        this.birth = LocalDate.parse(birth, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}