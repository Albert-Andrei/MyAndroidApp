package com.example.myandroidapplication.ViewModel;

import androidx.lifecycle.ViewModel;

import com.example.myandroidapplication.Model.MovieList;
import com.example.myandroidapplication.Repository.MovieData;

public class NavigationViewModel extends ViewModel {

    private static NavigationViewModel instance;
    private MovieData repository;
    private MovieList list;

    private NavigationViewModel() {
        repository = MovieData.getInstance();
    }

    public static synchronized NavigationViewModel getInstance() {
        if (instance == null) {
            instance = new NavigationViewModel();
        }
        return instance;
    }

    public MovieList getListOfMovies() {
        return list;
    }

    public void setListOfMovies(MovieList list) {
        this.list = list;
    }
}
