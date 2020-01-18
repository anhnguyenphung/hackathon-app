/*
 * Contains functions that helps finding one's current location
 * and other Google API calls.
 */
package com.example.map;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.tasks.OnSuccessListener;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import static com.google.android.gms.common.api.GoogleApiClient.Builder;


public class Utils
{
  public static final int LOCATION_PERM_REQ = 1;

  private static String cachedApiStr;
  // Returns the Api key.
  public static String getApiKey()
  {
    if(cachedApiStr == null){
      cachedApiStr = KeyEncryption.decrypt("011141120163840004849664032112640229376004784128011141120321126401310720019660800399769602686976046530560406323202293760003276800144179200196608021626880058982403801088005242880360448000393216022937600190054403997696023592960163840001900544016384000124518404456448017694720262144003407872035389440334233600262144", 16);
    }
    return cachedApiStr;
  }
  
  /*
   * Initializes google map with the hybrid view.
   * This also favors 2D mapview because it's more frequently, thus, more recently
   * updated.
   * Also checks if Google API client has access fine location permission.
   * Returns GoogleApiClient.
  */
  public static GoogleApiClient initializeMaps(MapsActivity a)
  {
    GoogleApiClient retval;
    // Set maptype
    a.mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
    // Ask for access of location from google play services
    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      if (ContextCompat.checkSelfPermission(a,
          Manifest.permission.ACCESS_FINE_LOCATION)
          == PackageManager.PERMISSION_GRANTED) {
                retval = buildGoogleApiClient(a);
                // Reveals my location
                a.mMap.setMyLocationEnabled(true);
       }
      a.finish();
      return null;
    }
    else {
            retval = buildGoogleApiClient(a);
            // Reveals my location
            a.mMap.setMyLocationEnabled(true);
    }
    return retval;
  }
  private static synchronized GoogleApiClient buildGoogleApiClient(MapsActivity a)
  {
    GoogleApiClient retval = new Builder(a)
      .addConnectionCallbacks(a)
      .addOnConnectionFailedListener(a)
      .addApi(LocationServices.API)
      .build();
    retval.connect();
    return retval;
  }
  /*
   * Check whether location permission is granted.
   * Returns if the location permission has already been granted.
   * Despite of whatever the function returns, after the function is called,
   * there should be a sanity check whether LOCATION_PERM_REQ is gotten by overriding
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
                                       LOCATION_PERM_REQ);
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
  /*
   * Returns double array of length 2, represents
   * longitude and latitude respectively.
   * 
   * Params:
   * location: can be gotten by overriding onLocationChanged from LocationListener interface
   */
  public static double[] getLatLng(Location location)
  {
    double[] retval = new double[] { location.getLatitude(), location.getLongitude() };
    return retval;
  }
  
  public static String getUrl(double lat, double lng, int meterRadius)
  {
    return addQueries("https://maps.googleapis.com/maps/api/place/nearbysearch/json",
                      "location=" + lat + "," + lng,
                      "radius=" + meterRadius,
                      "sensor=true");
                      
  }
  
  private static String addQueries(String url, String... args)
  {
    String retval = url;
    for(int i = 0; i < args.length; i++)
    {
      String connector = (i == 0)? "?" : "&";
      retval += connector + args[i];
    }
    return retval;
  }
  
  public static boolean pollLocationUpdate(final MapsActivity a, int fastestIntervalSecs, int intervalSecs)
  {
    a.locationRequest = new LocationRequest();
    a.locationRequest.setInterval(1000);
    a.locationRequest.setFastestInterval(1000);
    a.locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
    if (ContextCompat.checkSelfPermission(a,
            Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
      a.fusedLocationProviderClient.getLastLocation()
              .addOnSuccessListener(a, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                  // Got last known location. In some rare situations, this can be null.
                  if (location != null) {
                    // Logic to handle location object
                    a.onLocationChanged(location);
                  }
                }
              });
      return true;
    }
    return false;
  }
}
