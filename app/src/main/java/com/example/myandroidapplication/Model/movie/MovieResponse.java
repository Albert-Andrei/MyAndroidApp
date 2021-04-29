package com.example.myandroidapplication.Model.movie;

import java.util.ArrayList;
import java.util.Arrays;

public class MovieResponse {
    private int page;
    private Movie[] results;

    public ArrayList<Movie> getMovie() {
        return new ArrayList<Movie>(Arrays.asList(results));
    }

    class Movies {
        private boolean adult;
        private int id;
        private String title;
        private String overview;
        private String poster_path;
        private String backdrop_path;
        private String release_date;
        private double vote_average;
    }
}
