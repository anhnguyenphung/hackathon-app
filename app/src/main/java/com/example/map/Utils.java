/*
 * Contains functions that helps finding one's current location
 * and other Google API calls.
 */
package com.example.map;

import android.Manifest;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Looper;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
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
    GoogleApiClient retval = null;
    // Set maptype
    a.mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    // Ask for access of location from google play services
    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      if (ContextCompat.checkSelfPermission(a,
          Manifest.permission.ACCESS_FINE_LOCATION)
          == PackageManager.PERMISSION_GRANTED) {
                retval = buildGoogleApiClient(a);
                // Reveals my location
                a.mMap.setMyLocationEnabled(true);
       }
      else
      {
        a.finish();
      }
    }
    else {
            retval = buildGoogleApiClient(a);
            // Reveals my location
            a.mMap.setMyLocationEnabled(true);
    }
    return retval;
  }
  public static synchronized GoogleApiClient buildGoogleApiClient(MapsActivity a)
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
               "key=" + getApiKey(),
                      "location=" + lat + "," + lng,
                      //"radius=" + meterRadius,
                      "sensor=true",
                      "rankby=distance");
                      
  }
  
  public static String addQueries(String url, String... args)
  {
    String retval = url;
    for(int i = 0; i < args.length; i++)
    {
      String connector = (i == 0)? "?" : "&";
      retval += connector + args[i];
    }
    return retval;
  }

  /*
   * Returns null if unable to retrieve nearby places.
   */
  public static List<Place> getNearbyPlaces(Location userLocation, int searchRadiusMeters)
  {
    List<Place> retval = null;
    String requestUrl = getUrl(userLocation.getLatitude(), userLocation.getLongitude(),
            searchRadiusMeters);
    Log.d("getNearbyPlaces", "url: " + requestUrl);
    PlacesUtils.GetNearbyPlacesData getNearbyPlacesFunc = new PlacesUtils.GetNearbyPlacesData();
    try
    {
      String rawJson = getNearbyPlacesFunc.execute(new Object[]{requestUrl}).get();
      Log.d("getNearbyPlaces", "json: " + rawJson);
      retval = new ArrayList<Place>();
      List<HashMap<String, String>> nearbyPlacesList = DataParser.parse(rawJson);
      for(HashMap<String, String> nearbyPlaceData: nearbyPlacesList)
      {
        retval.add(Place.getFromData(nearbyPlaceData));
      }
    }
    catch (Exception e)
    {
      Log.w("getNearbyPlaces.Exception", e.getStackTrace().toString());
    }
    return retval;
  }
  
  public static void pollLocationUpdate(final MapsActivity a, int fastestIntervalSecs, int intervalSecs)
  {
    Log.d("pollLocationUpdate", "poll started");
    a.locationRequest = LocationRequest.create();
    a.locationRequest.setInterval(intervalSecs);
// <<<<<<< Pegasust
//     a.locationRequest.setFastestInterval(1000);
//     a.locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
//     // See if user enabled GPS
//     LocationSettingsRequest.Builder rBuilder = new LocationSettingsRequest.Builder()
//             .addLocationRequest(a.locationRequest);
//     // Check state of GPS
//     Task<LocationSettingsResponse> result =
//             LocationServices.getSettingsClient(a).checkLocationSettings(rBuilder.build());

//     result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
//       @Override
//       public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
//         try {
//           LocationSettingsResponse response = task.getResult(ApiException.class);
//           // All location settings are satisfied. The client can initialize location
//           // requests here.
//         } catch (ApiException exception) {
//           switch (exception.getStatusCode()) {
//             case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
//               // Location settings are not satisfied. But could be fixed by showing the
//               // user a dialog.
//               try {
//                 // Cast to a resolvable exception.
//                 ResolvableApiException resolvable = (ResolvableApiException) exception;
//                 // Show the dialog by calling startResolutionForResult(),
//                 // and check the result in onActivityResult().
//                 resolvable.startResolutionForResult(
//                         a,
//                         LocationRequest.PRIORITY_HIGH_ACCURACY);
//               } catch (IntentSender.SendIntentException e) {
//                 // Ignore the error.
//               } catch (ClassCastException e) {
//                 // Ignore, should be an impossible error.
//               }
//               break;
//             case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
//               // Location settings are not satisfied. However, we have no way to fix the
//               // settings so we won't show the dialog.
//               break;
// =======
    a.locationRequest.setFastestInterval(fastestIntervalSecs);
    a.locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
            .addLocationRequest(a.locationRequest);
    SettingsClient client = LocationServices.getSettingsClient(a);
    Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
    task.addOnSuccessListener(a, new OnSuccessListener<LocationSettingsResponse>() {
      @Override
      public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
        // All location settings are satisfied. The client can initialize
        // location requests here.
        // ...
        Log.i("pullLocationUpdate", "Called onSuccess");
        a.fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(a, new OnSuccessListener<Location>()
                {
                  @Override
                  public void onSuccess(Location location) {
                    if(location != null)
                    {
                      // a.onLocationChanged(location);
                      a.fusedLocationProviderClient.requestLocationUpdates(
                              a.locationRequest, a.locationCallback, Looper.getMainLooper());
                    }
                  }
                });
      }
    });

    task.addOnFailureListener(a, new OnFailureListener() {
      @Override
      public void onFailure(@NonNull Exception e) {
        if (e instanceof ResolvableApiException) {
          // Location settings are not satisfied, but this can be fixed
          // by showing the user a dialog.
          try {
            // Show the dialog by calling startResolutionForResult(),
            // and check the result in onActivityResult().
            ResolvableApiException resolvable = (ResolvableApiException) e;
            int REQUEST_CHECK_SETTINGS = 2;
            resolvable.startResolutionForResult(a,
                    REQUEST_CHECK_SETTINGS);
          } catch (IntentSender.SendIntentException sendEx) {
            // Ignore the error.
// >>>>>>> master
          }
        }
      }
    });
// <<<<<<< Pegasust
//     // See if we have the permission
//     if (ContextCompat.checkSelfPermission(a,
//             Manifest.permission.ACCESS_FINE_LOCATION)
//             == PackageManager.PERMISSION_GRANTED) {
//       a.fusedLocationProviderClient.getLastLocation()
//               .addOnSuccessListener(a, new OnSuccessListener<Location>() {
//                 @Override
//                 public void onSuccess(Location location) {
//                   // Got last known location. In some rare situations, this can be null.
//                   if (location != null) {
//                     // Logic to handle location object
//
//                   }
//                 }
//               });
//     }
// =======
// >>>>>>> master

  }
  public static String retrieveRawString(String urlStr) throws IOException
  {
    String data = "";
    InputStream iStream = null;
    HttpURLConnection urlConnection = null;
    try {
      URL url = new URL(urlStr);

      // Creating an http connection to communicate with url
      urlConnection = (HttpURLConnection) url.openConnection();

      // Connecting to url
      urlConnection.connect();

      // Reading data from url
      iStream = urlConnection.getInputStream();

      BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

      StringBuffer sb = new StringBuffer();

      String line = "";
      while ((line = br.readLine()) != null) {
        sb.append(line);
      }

      data = sb.toString();
      Log.d("downloadUrl", data.toString());
      br.close();

    } catch (Exception e) {
      Log.d("Exception", e.toString());
    } finally {
      iStream.close();
      urlConnection.disconnect();
    }
    return data;
  }
  public static double round(double v, int precision)
  {
    int s = (int) Math.pow(10,precision);
    return (double) Math.round(v * s) / s;
  }
}
