package com.example.myandroidapplication.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.myandroidapplication.Model.movie.Movie;
import com.example.myandroidapplication.Repository.MovieData;

import java.util.ArrayList;

public class HomeViewModel extends ViewModel {

    private MovieData repository;

    public HomeViewModel() {
        repository = MovieData.getInstance();
    }

    public LiveData<ArrayList<Movie>> getMovies() {
        repository.getMoviesFromAPI();
        return repository.getMovies();
    }
}