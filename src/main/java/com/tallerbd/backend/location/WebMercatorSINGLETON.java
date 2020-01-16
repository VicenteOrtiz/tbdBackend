package com.tallerbd.backend.location;

import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;

public class WebMercatorSINGLETON{

    private final static int srid = 3857; // epsg:3857 -- wgs84 web mercator (auxiliary sphere)

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
}