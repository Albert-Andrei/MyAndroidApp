package com.example.myandroidapplication.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myandroidapplication.Model.MovieList;
import com.example.myandroidapplication.Model.movie.Movie;
import com.example.myandroidapplication.Repository.MovieData;

import java.util.ArrayList;

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

    public void remove(String listId, String id) {
        repository.remove(listId, id);
    }

    public void saveMovie(String listId, Movie movieToSave) {
        repository.saveMovie(listId, movieToSave);
    }

    public void editMoviePersonalRating(String listId, String movieId, double rating) {
        repository.editMoviePersonalRating(listId, movieId, rating);
    }
    public LiveData<ArrayList<MovieList>> getAllListsFromDB() {
        return repository.getAllListsFromDB();
    }

    public String getJustDeletedMovieId() {
        return repository.getJustDeletedMovieId();
    }
}