package com.example.map;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    // Only update location marker per 500 meters
    public static final float UPDATE_DISTANCE = 500.0;
    public static final int ZOOM_LEVEL = 17;
            
    public GoogleMap mMap;
    private GoogleApiClient gApiClient;
    private Marker lastMarker;
    private LocationRequest locationRequest;
    private Location lastLoc;
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
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        gApiClient = Utils.initializeMaps(this);
        // Add a marker in Sydney, Australia, and move the camera.
//         LatLng sydney = new LatLng(-34, 151);
//         mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//         mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("MapsActivity.onLocationChanged", "called");
        // Update loction marker when reached threshold
        if(lastLoc == null || lastLoc.distanceTo(location) >= UPDATE_DISTANCE)
        {
            // Remove last marker.
            if(lastMarker != null)
            {
                lastMarker.remove();
            }
            double[] latlng = Utils.getLatLng(location);
            LatLng currLatLng = new LatLng(latlng[0], latlng[1]);
            MarkerOptions markerOptions = new MarkerOptions()
                .position(currLatLng)
                .title("Current Location");
            lastMarker = mMap.addMarker(markerOptions);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(currLatLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(ZOOM_LEVEL));
        }
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
        Utils.pollLocationUpdate(a, 1000, 1000);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

}



