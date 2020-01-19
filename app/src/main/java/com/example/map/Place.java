package com.example.map;

import android.location.Location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Place {
    String name;
    String vicinity;
    double[] latlng;
    List<Double> meanRatings;
    List<Review> reviews;
    /*
     * See https://docs.google.com/spreadsheets/d/1kW0grOGKZgpqgICPzMgds8cNXQPkT-bVkFs1oQZNMRA/edit?usp=sharing
     * for how numbers are derived.
     */
    @Override
    public int hashCode()
    {
        int a = (int) (latlng[0]*4448.0);
        int b = (int) (latlng[1]*4448.0);
        return (a << 11) + (b << 9);
    }
    @Override
    public boolean equals(Object o)
    {
        if(this == o) return true;
        if(o == null) return false;
        if(this.getClass() != o.getClass()) return false;
        Place other = (Place) o;
        return other.latlng[0] == this.latlng[0] &&
                other.latlng[1] == this.latlng[1] &&
                other.vicinity.equals(this.vicinity) &&
                other.name.equals(this.name);
    }
    public List<Double> getMeanRatings()
    {
        return meanRatings;
    }
    public double getMeanOverallRatings()
    {
        double retval = 0.0;
        for(int i = 0; i < meanRatings.size(); i++)
        {
            retval += meanRatings.get(i);
        }
        retval/=(double) meanRatings.size();
        return retval;
    }
    public String toString()
    {
        return "{" + String.join(", ", name, vicinity, String.valueOf(latlng[0]), String.valueOf(latlng[1]));
    }
    public float distanceFromUser(double[] userLatLng)
    {
        float[] result = new float[1];
        Location.distanceBetween(userLatLng[0], userLatLng[1], latlng[0], latlng[1],result);
        return result[0];
    }

    public Place(String placeName, String placeVicinity, double[] placeLatLng,
                 List<Double> meanRatings, List<Review> reviews)
    {
        name = placeName;
        vicinity = placeVicinity;
        latlng = placeLatLng;
        this.meanRatings = meanRatings;
        this.reviews = reviews;
    }

    public Place(String placeName, String placeVicinity, double[] placeLatLng)
    {
        name = placeName;
        vicinity = placeVicinity;
        latlng = placeLatLng;
        meanRatings = Review.getEmptyRatings();
        reviews = new ArrayList<Review>();
    }

    public static Place getFromData(HashMap<String, String> placeData)
    {
        return new Place(placeData.get("place_name"), placeData.get("vicinity"),
                new double[] {
                        Double.parseDouble(placeData.get("lat")),
                        Double.parseDouble(placeData.get("lng"))
                });
    }
}
