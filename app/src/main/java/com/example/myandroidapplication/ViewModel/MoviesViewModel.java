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
    MutableLiveData<String> calhozGrav;

    private MoviesViewModel() {
        repository = MovieData.getInstance();
        calhozGrav = new MutableLiveData<>();
    }

    public static synchronized MoviesViewModel getInstance() {
        if (instance == null) {
            instance = new MoviesViewModel();
        }
        return instance;
    }

    public LiveData<MovieList> getWatchLaterListFromDB() {
        return repository.getWatchLaterListFromDB();
    }

    public LiveData<MovieList> getWatchLaterList() {
        return repository.getWatchLaterList();
    }

    public void sendGenreInfo(String message) {
        calhozGrav.setValue(message);
    }

    public LiveData<String> getGenreInfo() {
        return calhozGrav;
    }
}