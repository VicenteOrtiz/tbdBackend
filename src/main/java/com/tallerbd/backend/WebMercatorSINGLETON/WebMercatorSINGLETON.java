package com.tallerbd.backend.WebMercatorSINGLETON;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.PrecisionModel;

public class WebMercatorSINGLETON{

    private final static int srid = 3857; // epsg:3857 -- wgs84 web mercator (auxiliary sphere)

    private final static double RADIUS_MAJOR = 6378137.0;

    private static GeometryFactory gf;

    private static WebMercatorSINGLETON geomFactory;

    private WebMercatorSINGLETON(){
        gf = new GeometryFactory( new PrecisionModel(), srid);
    }

    private WebMercatorSINGLETON(int customSRID){
        gf = new GeometryFactory( new PrecisionModel(), customSRID);
    }

    public static WebMercatorSINGLETON getDefaultFactory(){
        if (geomFactory == null){
            geomFactory = new WebMercatorSINGLETON();
        }
        return geomFactory;
    }

    public static WebMercatorSINGLETON getCustomFactory(int customSRID){
        if (geomFactory == null){
            geomFactory = new WebMercatorSINGLETON(customSRID);
        }
        return geomFactory;
    }

    public GeometryFactory getGeometryFactory(){
        return gf;
    }

    public double SphericalMercatorXAxisProjection(double longitude) {
        return Math.toRadians(longitude) * RADIUS_MAJOR;
    }

    public double SphericalMercatorYAxisProjection(double latitude) {
        return Math.log(Math.tan(Math.PI / 4 + Math.toRadians(latitude) / 2)) * RADIUS_MAJOR;
    }

    public Point generatePoint(Double latitude, Double longitude){
        // longitude and latitude in a nutshell: longitude and latiture correspond to angles.
        // On latitude, it goes from -90 degree (90S, south pole) to +90 degree (90N, north pole),
        // being 0 degrees the ecuator.
        // On longitude, it goes from -180 (180W) to +180 (180E), being 0 the Greenwich meridian.

        // Double x = SphericalMercatorXAxisProjection(longitude);
        // Double y = SphericalMercatorYAxisProjection(latitude);
        // return gf.createPoint( new Coordinate(x, y));

        // if( gf == null ){

        // }

        return gf.createPoint( new Coordinate( longitude, latitude ) );
    }
}