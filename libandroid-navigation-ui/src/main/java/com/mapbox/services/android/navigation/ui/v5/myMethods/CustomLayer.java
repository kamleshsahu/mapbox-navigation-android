package com.mapbox.services.android.navigation.ui.v5.myMethods;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.mapbox.core.constants.Constants;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by k on 4/24/2019.
 */
public class CustomLayer {
    Style mapboxStyle;
    Context context;
    public CustomLayer(Style mapboxStyle, Context context) {
       this.mapboxStyle=mapboxStyle;
       this.context=context;
    }

    public void addMarkers (int iconid, String MARKER_IMAGE, String MARKER_SOURCE, Point point, String
            MARKER_STYLE_LAYER, String index){
        List<Feature> features = new ArrayList<>();
        /* Source: A data source specifies the geographic coordinate where the image marker gets placed. */
        //  features.add(Feature.fromGeometry(Point.fromLngLat(-78.7448, 40.2489)));

        features.add(Feature.fromGeometry(point, null, index));
        FeatureCollection featureCollection = FeatureCollection.fromFeatures(features);
        GeoJsonSource source = new GeoJsonSource(MARKER_SOURCE, featureCollection);
        mapboxStyle.removeSource(source.getId());
        mapboxStyle.addSource(source);

   /* Style layer: A style layer ties together the source and image and specifies how they are displayed on the map. */
        SymbolLayer markerStyleLayer = new SymbolLayer(MARKER_STYLE_LAYER, MARKER_SOURCE)
                .withProperties(
                        PropertyFactory.iconAllowOverlap(true),
                        PropertyFactory.iconImage(MARKER_IMAGE)
                );

        Bitmap icon = BitmapFactory.decodeResource(
                context.getResources(), iconid);
        mapboxStyle.addLayer(markerStyleLayer);
        mapboxStyle.addImage(MARKER_IMAGE, icon);

    }

    public void addPolyline(String poly, String LINE_LAYER_ID, int color,int selectedroute) {
        List<Feature> features = new ArrayList<>();
        /* Source: A data source specifies the geographic coordinate where the image marker gets placed. */
        //  features.add(Feature.fromGeometry(Point.fromLngLat(-78.7448, 40.2489)));


        features.add(Feature.fromGeometry(LineString.fromPolyline(poly, Constants.PRECISION_6), null, LINE_LAYER_ID));




        FeatureCollection featureCollection = FeatureCollection.fromFeatures(features);
        GeoJsonSource source = new GeoJsonSource(LINE_LAYER_ID, featureCollection);

// The layer properties for our line. This is where we make the line dotted, set the
// color, etc.
        LineLayer lineLayer= new LineLayer(LINE_LAYER_ID, LINE_LAYER_ID);

        if(LINE_LAYER_ID.equals("p"+selectedroute)){
            lineLayer.withProperties(PropertyFactory.lineCap(Property.LINE_CAP_SQUARE),
                    PropertyFactory.lineJoin(Property.LINE_JOIN_MITER),
                    PropertyFactory.lineOpacity(.9f),
                    PropertyFactory.lineWidth(8f),
                    PropertyFactory.lineColor(color));

        }else {
            lineLayer.withProperties(PropertyFactory.lineCap(Property.LINE_CAP_SQUARE),
                    PropertyFactory.lineJoin(Property.LINE_JOIN_MITER),
                    PropertyFactory.lineOpacity(.9f),
                    PropertyFactory.lineWidth(7f),
                    PropertyFactory.lineColor(color));

        }


        //      mapboxStyle.addLayer(markerStyleLayer);

        mapboxStyle.addLayer(lineLayer);
        mapboxStyle.addSource(source);

    }


}