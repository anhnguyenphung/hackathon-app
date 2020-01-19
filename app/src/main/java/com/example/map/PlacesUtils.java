package com.example.map;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import java.util.HashMap;
import java.util.List;

public class PlacesUtils {

    public static String retrieveRawString(String urlStr) throws IOException
    {
        return Utils.retrieveRawString(urlStr);
    }

//    public static void ShowNearbyPlaces(List<HashMap<String, String>> nearbyPlacesList) {
//        for (int i = 0; i < nearbyPlacesList.size(); i++) {
//            Log.d("onPostExecute","Entered into showing locations");
//            MarkerOptions markerOptions = new MarkerOptions();
//            HashMap<String, String> googlePlace = nearbyPlacesList.get(i);
//            double lat = Double.parseDouble(googlePlace.get("lat"));
//            double lng = Double.parseDouble(googlePlace.get("lng"));
//            String placeName = googlePlace.get("place_name");
//            String vicinity = googlePlace.get("vicinity");
//            LatLng latLng = new LatLng(lat, lng);
//            markerOptions.position(latLng);
//            markerOptions.title(placeName + " : " + vicinity);
//            mMap.addMarker(markerOptions);
//            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
//            //move map camera
//            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
//            mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
//        }
//    }
    public static Marker markPlace(Place place, GoogleMap gmap)
    {
        Log.d("markPlace"," Called");
        MarkerOptions markerOptions = new MarkerOptions()
                .position(new LatLng(place.latlng[0],place.latlng[1]))
                .title(place.name)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        return gmap.addMarker(markerOptions);

    }
    public static class GetNearbyPlacesData extends AsyncTask<Object, String, String> {

        String googlePlacesData;
        //GoogleMap mMap;
        String url;

        /*
         * Execute getting json output from url.
         * Couple this with get() to get raw json string.
         */
        @Override
        protected String doInBackground(Object... params) {
            try {
                Log.d("GetNearbyPlacesData", "doInBackground entered");
                //mMap = (GoogleMap) params[0];
                url = (String) params[0];
                googlePlacesData = retrieveRawString(url);
                Log.d("GooglePlacesReadTask", "doInBackground Exit");
            } catch (Exception e) {
                Log.d("GooglePlacesReadTask", e.toString());
            }
            return googlePlacesData;
        }

//        @Override
//        protected void onPostExecute(String result) {
//            Log.d("GooglePlacesReadTask", "onPostExecute Entered");
//            List<HashMap<String, String>> nearbyPlacesList = null;
//            DataParser dataParser = new DataParser();
//            nearbyPlacesList =  dataParser.parse(result);
//            ShowNearbyPlaces(nearbyPlacesList);
//            Log.d("GooglePlacesReadTask", "onPostExecute Exit");
//        }


    }
}
