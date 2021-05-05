package com.example.myandroidapplication.Model;

import com.example.myandroidapplication.Model.movie.Movie;

import java.util.ArrayList;

public class MovieList {
    private ArrayList<Movie> list;
    private String name;
    private int imageId;

    public MovieList() {
        this.list = new ArrayList<>();
        this.name = "";
    }

    public MovieList(String name) {
        this.name = name;
        this.list = new ArrayList<>();
    }

    public ArrayList<Movie> getList() {
        return list;
    }

    public void setList(ArrayList<Movie> list) {
        this.list = list;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public int getSize() {
        return list.size();
    }

    public void addMovie(Movie movie)
    {
        list.add(movie);
    }

    public void removeMovie(int id) {
        list.remove(id);
    }
}
