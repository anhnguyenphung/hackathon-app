package com.example.map;

import java.util.ArrayList;
import java.util.List;

public class Review {
    public static final int N_CATEGORIES = 4;

    public List<Double> ratingCategories;
    public String description;
    public String user;
    public Review(List<Double> ratings, String desc, String user)
    {
        ratingCategories = ratings;
        description = desc;
        this.user = user;
    }
    public static List<Double> getEmptyRatings()
    {
        List<Double> ratings = new ArrayList<>();
        for(int i = 0; i < N_CATEGORIES; i++)
        {
            ratings.add(0.0);
        }
        return ratings;
    }
    public static Review getEmpty()
    {
        List<Double> ratings = getEmptyRatings();
        return new Review(ratings, "", "");
    }
}
