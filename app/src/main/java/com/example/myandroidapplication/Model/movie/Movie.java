package com.example.myandroidapplication.Model.movie;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Movie {
    private String id;
    private String title;
    @SerializedName("vote_average")
    private String rating;
    private String overview;
    private boolean adult;
    @SerializedName("poster_path")
    private String posterImage;
    @SerializedName("backdrop_path")
    private String backdropImage;
    @SerializedName("original_language")
    private String originalLanguage;

    public Movie() {
    }

    public Movie(String id, String title, String rating, String overview, boolean adult, String verticalImageId, String horizontalImageId, String originalLanguage) {
        this.id = id;
        this.title = title;
        this.rating = rating;
        this.overview = overview;
        this.adult = adult;
        this.posterImage = verticalImageId;
        this.backdropImage = horizontalImageId;
        this.originalLanguage = originalLanguage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return title;
    }

    public void setName(String name) {
        this.title = name;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        adult = adult;
    }

    public String getPosterImage() {
        return posterImage;
    }

    public void setPosterImage(String posterImage) {
        this.posterImage = posterImage;
    }

    public String getBackdropImage() {
        return backdropImage;
    }

    public void setBackdropImage(String backdropImage) {
        this.backdropImage = backdropImage;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }
}
