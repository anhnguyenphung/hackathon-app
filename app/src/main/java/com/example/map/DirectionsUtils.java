package com.example.map;

public class DirectionsUtils {
    public static String getUrl(double[] originLatLng, double[] ... destLatLngs)
    {
        String destStr = "";
        for(int i = 0; i < destLatLngs.length; i++)
        {
            String connector = (i > 0 ? "|":"");
            destStr+= connector;
            destStr += Double.toString(destLatLngs[i][0]);
            destStr += ",";
            destStr += Double.toString(destLatLngs[i][1]);
        }
        return Utils.addQueries("https://maps.googleapis.com/maps/api/directions/json",
                "key="+Utils.getApiKey(),
                "origins="+Double.toString(originLatLng[0])+","+Double.toString(originLatLng[1]),
                "destinations="+destStr,
                "mode=walking"
        );
    }

    public static class DataParser
    {

    }
}
