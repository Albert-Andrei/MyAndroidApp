package com.example.myandroidapplication.ViewModel;

import android.app.Application;
import android.net.Uri;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.myandroidapplication.Model.movie.Movie;
import com.example.myandroidapplication.Repository.UserRepository;
import com.example.myandroidapplication.Repository.MovieData;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class HomeViewModel extends AndroidViewModel {

    private final UserRepository userRepository;
    private MovieData repository;

    public HomeViewModel(Application app) {
        super(app);
        userRepository = UserRepository.getInstance(app);
        repository = MovieData.getInstance();
    }

    public void init (){
        String userId = userRepository.getCurrentUser().getValue().getUid();
        repository.init(userId);
    }

    public LiveData<FirebaseUser> getCurrentUser(){
        return userRepository.getCurrentUser();
    }

    public void signOut() {
        userRepository.signOut();
    }

    public LiveData<ArrayList<Movie>> getMovies() {
        repository.getMoviesFromAPI();
        return repository.getMovies();
    }

    public void saveMovie(String listId, Movie movieToSave) {
        repository.saveMovie(listId, movieToSave);
    }

    public void editMoviePersonalRating(String listId, String movieId, double rating) {
        repository.editMoviePersonalRating(listId, movieId, rating);
    }
}