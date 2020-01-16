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
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.PrecisionModel;

import org.hibernate.annotations.Type;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="location")
public class Location{

    // model atributes

    @Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "point")
    Point point;

    // class atributes
    final static double RADIUS_MAJOR = 6378137.0;

    private double SphericalMercatorXAxisProjection(double longitude) {
        return Math.toRadians(longitude) * RADIUS_MAJOR;
    }

    private double SphericalMercatorYAxisProjection(double latitude) {
        return Math.log(Math.tan(Math.PI / 4 + Math.toRadians(latitude) / 2)) * RADIUS_MAJOR;
    }

    public void setPoint(Float latitude, Float longitude){

        // longitude and latitude in a nutshell: longitude and latiture correspond to angles.
        // On latitude, it goes from -90 degree (90S, south pole) to +90 degree (90N, north pole),
        // being 0 degrees the ecuator.
        // On longitude, it goes from -180 (180W) to +180 (180E), being 0 the Greenwich meridian.

        Double x = SphericalMercatorXAxisProjection(longitude);
        Double y = SphericalMercatorYAxisProjection(latitude);

        WebMercatorSINGLETON factory = WebMercatorSINGLETON.getDefaultFactory();

        point = factory.getGeometryFactory().createPoint( new Coordinate(x, y));
    }
}