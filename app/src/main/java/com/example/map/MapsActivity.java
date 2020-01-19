package com.example.map;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    // Only update location markers per 500 meters
    public static final float UPDATE_DISTANCE = 500.0f;
    public static final int ZOOM_LEVEL = 17;
    // Only find nearby places when the accuracy radius is within 500 meters
    public static final float TARGET_ACCURACY_RADIUS = 500.0F;
    public static final int NEARBY_DISTANCE_RADIUS = 2000;

            
    public GoogleMap mMap;
    public LocationRequest locationRequest;
    public GoogleApiClient gApiClient;
    public FusedLocationProviderClient fusedLocationProviderClient;
    public LocationCallback locationCallback;

    private Marker lastMarker;
    public Location lastUpdateLocation;
    private boolean findNearbyRequested;
    public List<Place> lastNearbyPlaces;

    // Markers that are being displaced on mMap that shows locations of lastNearbyPlaces.
    private List<Marker> nearbyMarkers;

    public static int targetIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Ensure that location access is permitted by user.
        if(Utils.checkLocationPerm(this))
        {
            Log.d("MapsActivity.onCreate", "Location perm already granted.");
        }
        else
        {
            Log.d("MapsActivity.onCreate", "Location perm is prompted.");
        }
        // Ensure that google play services is installed.
        // Since this project is heavily dependent on it, if GP Services is
        // not installed, just exit out.
        if(!Utils.checkGooglePlayServices(this))
        {
            Log.e("MapsActivity.onCreate", "Google Play Services not available!");
            finish();
        }
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null || locationResult.getLastLocation() == null) {
                    return;
                }
                onLocationChanged(locationResult.getLastLocation());
            };
        };

        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Log.d("MapActivity.onCreate", "onCreate done.");
        findNearbyRequested = true;
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        // Add a marker in Sydney, Australia, and move the camera.
        LatLng az = new LatLng(32.229806,-110.955023);
//mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(az));
        //cameraTo(new double[]{32.229806,-110.955023});
        Log.d("onMapReady", "Initializing map");
        gApiClient = Utils.initializeMaps(this);
        // Add a marker in Sydney, Australia, and move the camera.
//         LatLng sydney = new LatLng(-34, 151);
//         mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//         mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

    }

    /*
     * Remove markers from map
     * then set findNearbyRequested to true.
     */
    public void promptNearbyRequest()
    {
        findNearbyRequested = true;
        for(Marker marker: nearbyMarkers)
        {
            // Remove marker from list
            marker.remove();
        }
    }
    @Override
    public void onLocationChanged(Location location) {
        Log.d("onLocationChanged", "called");
        Log.d("onLocationChanged", "location: " + location.toString());
        if(location != null &&
                findNearbyRequested &&
                (location.hasAccuracy() && location.getAccuracy() <= TARGET_ACCURACY_RADIUS) || !location.hasAccuracy())
        {
            if(lastUpdateLocation == null || location.distanceTo(lastUpdateLocation) >= UPDATE_DISTANCE)
            {
                Log.i("onLocationChanged", "Getting nearby places");
                // Get nearby places.
                List<Place> newNearbyPlaces = Utils.getNearbyPlaces(location, NEARBY_DISTANCE_RADIUS);
                Log.w("onLocationChanged", "API Called");
                Log.d("onLocationChanged", "Nearby places: ");
                // TODO: Do something with newNearbyPlaces here and attempt to reserve last known data
                lastNearbyPlaces = newNearbyPlaces;
                try {
                    for (Place nearbyPlace : lastNearbyPlaces) {
                        Log.d("onLocationChanged", nearbyPlace.toString());
                    }
                    nearbyMarkers = new ArrayList<Marker>();
                    for (Place place : lastNearbyPlaces) {
                        nearbyMarkers.add(PlacesUtils.markPlace(place, mMap));
                    }
                    findNearbyRequested = false;
                }
                catch(Exception e){}

            }
        }
        // Update loction marker when reached threshold
        if(//lastLoc == null || lastLoc.distanceTo(location) >= UPDATE_DISTANCE
        true)
        {
            // Remove last marker.
            if(lastMarker != null)
            {
                lastMarker.remove();
            }
            double[] latlng = Utils.getLatLng(location);
            Log.d("onLocationCreated","Putting mark in current location");
            // Mark the current location
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(new LatLng(latlng[0], latlng[1]))
                    .title("Current Location")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            lastMarker = mMap.addMarker(markerOptions);
            // move camera to current location
            //cameraTo(latlng);
        }
    }

    private void cameraTo(double[] latlng) {
        LatLng currLatLng = new LatLng(latlng[0], latlng[1]);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currLatLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(ZOOM_LEVEL));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        // When device is connected, attempt to do location updates.
        Log.d("onConnected", "called");
        Utils.pollLocationUpdate(this, 100, 1000);
    }

    @Override
    public void onRequestPermissionsResult(int rCode, String perms[], int[] grantResults)
    {
        switch(rCode)
        {
            case Utils.LOCATION_PERM_REQ:
            {
                // If a request of location perm was granted
                if(grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    // Make extra sure
                    if(ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED)
                    {
                        if(gApiClient == null)
                        {
                            Utils.buildGoogleApiClient(this);
                        }
                        mMap.setMyLocationEnabled(true);
                        Utils.pollLocationUpdate(this, 100, 1000);
                    }
                }
                else
                {
                    Log.e("onPermResults", "Location perm is denied");
                }
            }
        }
    }



    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

}



