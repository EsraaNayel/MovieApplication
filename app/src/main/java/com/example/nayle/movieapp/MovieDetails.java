package com.example.nayle.movieapp;

import com.example.nayle.movieapp.datamovie.Review;
import com.example.nayle.movieapp.datamovie.Trailer;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Nayle on 1/9/2016.
 */
public class MovieDetails implements Serializable {

    String movieID;
    String Title;
    String posterPath;
    String OverView;
    int Rate;
    public ArrayList<Trailer> trailerList;
    public ArrayList<Review> ReviewList;

    public MovieDetails(String movieID, String title, String posterPath, ArrayList<Trailer> trailerList, ArrayList<Review> reviewList, String overView, int rate) {
        this.movieID = movieID;
        this.Title = title;
        this.posterPath = posterPath;
        this.OverView = overView;
        this.Rate = rate;
        this.trailerList = trailerList;
        ReviewList = reviewList;
    }
}
