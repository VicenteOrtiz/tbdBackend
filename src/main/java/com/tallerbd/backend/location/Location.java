package com.tallerbd.backend.location;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tallerbd.backend.volunteer.Volunteer;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="location")
public class Location{

    @Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "geometry")
    Point geometry;

    @OneToOne(mappedBy = "location", cascade = CascadeType.ALL)
    @JsonIgnore
    Volunteer volunteer;

    public void setGeometry(Float latitude, Float longitude){

        // short resume on longitude and latitude: longitude and latiture correspond to angles.
        // On latitude, it goes from -90 degree (90S, south pole) to +90 degree (90N, north pole),
        // being 0 degrees the ecuator.
        // On longitude, it goes from -180 (180W) to +180 (180E), being 0 the Greenwich meridian.

        Double lat = Double.valueOf(latitude);
        Double lon = Double.valueOf(longitude);

        int radius = 6371 * 1000; // in meters

        Double x = radius * Math.cos(lat) * Math.cos(lon);
        Double y = radius * Math.cos(lat) * Math.sin(lon);
        Double z = radius * Math.sin(lat);

        //lat = Math.asin(z/radius);
        //lon = Math.atan2(y, x);

        GeometryFactory gf=new GeometryFactory();

        geometry = gf.createPoint( new Coordinate(x, y, z));
    }
}