package com.example.nayle.movieapp;

/**
 * Created by Nayle on 12/25/2015.
 */
public class MovieMain {

    String title;
    String id;
    String PosterPath;
    String OverView;
    int Rate;

    public MovieMain(String id, String title, String posterPath) {
        this.id = id;
        this.title = title;
        this.PosterPath = posterPath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPosterPath() {
        return PosterPath;
    }

    public void setPosterPath(String posterPath) {
        this.PosterPath = posterPath;
    }

    public String getOverView() {
        return OverView;
    }

    public void setOverView(String overView) {
        OverView = overView;
    }

    public int getRate() {
        return Rate;
    }

    public void setRate(int rate) {
        Rate = rate;
    }
}


