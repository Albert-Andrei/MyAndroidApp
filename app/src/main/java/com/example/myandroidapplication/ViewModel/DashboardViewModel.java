package com.example.myandroidapplication.ViewModel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myandroidapplication.Model.genre.Genre;
import com.example.myandroidapplication.Model.movie.Movie;
import com.example.myandroidapplication.Repository.GenresData;
import com.example.myandroidapplication.Repository.MovieData;

import java.util.ArrayList;

public class DashboardViewModel extends ViewModel {

    GenresData genreRepository;
    MovieData movieRepository;
    MutableLiveData<String> calhozGrav;
    private static DashboardViewModel instance;

    private DashboardViewModel() {
        genreRepository = GenresData.getInstance();
        movieRepository = MovieData.getInstance();
        calhozGrav = new MutableLiveData<>();
    }

    public static synchronized DashboardViewModel getInstance() {
        if (instance == null) {
            instance = new DashboardViewModel();
        }
        return instance;
    }


    public LiveData<ArrayList<Genre>> getGenres() {
        return genreRepository.getGenres();
    }

    public LiveData<ArrayList<Movie>> getMoviesByGenre() {
        return movieRepository.getMoviesByGenre();
    }

    public LiveData<ArrayList<Movie>> getSearchedMovies() {
        return movieRepository.getSearchedMovies();
    }

    public void getGenresFromAPI() {
        genreRepository.getGenresFromAPI();
    }

    public void getMoviesByGenreFromAPI(int id) {
        movieRepository.getMoviesByGenreFromAPI(id);
    }

    public void searchMovie(String name) {
        movieRepository.searchMovie(name);
    }

    public void sendGenreInfo(String message) {
        calhozGrav.setValue(message);
    }

    public LiveData<String> getGenreInfo() {
        return calhozGrav;
    }
}