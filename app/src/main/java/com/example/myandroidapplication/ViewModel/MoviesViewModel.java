package com.example.myandroidapplication.ViewModel;

import androidx.lifecycle.ViewModel;

import com.example.myandroidapplication.Repository.MovieData;

public class MoviesViewModel extends ViewModel {

    private static MoviesViewModel instance;
    private MovieData repository;

    private MoviesViewModel() {
        repository = MovieData.getInstance();
    }

    public static synchronized MoviesViewModel getInstance() {
        if (instance == null) {
            instance = new MoviesViewModel();
        }
        return instance;
    }

    public void init() {
//        repository.getWatchLaterList();
    }
}