/*
 * Contains functions that helps finding one's current location
 * and other Google API calls.
 */
package com.example.map;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class Utils
{
  public static final int LOCATION_PERM_REQ = 1;
  
  
  /*
   * Initializes google map with the hybrid view.
   * This also favors 2D mapview because it's more frequently, thus, more recently
   * updated.
   * Also checks if Google API client has access fine location permission.
  */
  public static void intializeMapsActivity(MapsActivity a)
  {
    // Set maptype
    a.mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
    // Ask for access of location from google play services
    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      if (ContextCompat.checkSelfPermission(a,
          Manifest.permission.ACCESS_FINE_LOCATION)
          == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                a.mMap.setMyLocationEnabled(true);
       }
    }
    else {
            buildGoogleApiClient();
            a.mMap.setMyLocationEnabled(true);
    }
  }
  /*
   * Check whether location permission is granted.
   * Returns if the location permission has already been granted.
   * Despite of whatever the function returns, after the function is called,
   * there should be a sanity check whether LOCATION_PER_REQ is gotten by overriding
   * onRequestPermissionsResult
   */
  public static boolean checkLocationPerm(MapsActivity a)
  {
    // If location permission is not granted
    if(ContextCompat.checkSelfPermission(a, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
    {
      // Request for permission of location
      ActivityCompat.requestPermissions(a,
                                       new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                       LOCATION_PER_REQ);
      return false;
    }
    // Location permission is granted
    return true;
  }
  /*
   * Checks whether google play services is installed
   */
  public static boolean checkGooglePlayServices(MapsActivity a)
  {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(a);
        if(result != ConnectionResult.SUCCESS) {
            if(googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(a, result,
                        0).show();
            }
            return false;
        }
        return true;
  }
  
  
}
