package com.example.myandroidapplication.Repository;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.myandroidapplication.Model.Data.MovieDAO;
import com.example.myandroidapplication.Model.MovieList;
import com.example.myandroidapplication.Model.movie.Movie;
import com.example.myandroidapplication.Model.movie.MovieResponse;
import com.example.myandroidapplication.R;
import com.example.myandroidapplication.RemoteDataSource.movie.MovieAPI;
import com.example.myandroidapplication.RemoteDataSource.movie.MovieServiceGenerator;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

public class MovieData {

    private static MovieData instance;
    private final MutableLiveData<ArrayList<Movie>> movies;
    private final MutableLiveData<ArrayList<Movie>> moviesByGenre;
    private final MutableLiveData<ArrayList<Movie>> searchedMovies;
    private MovieDAO movieDAO;

    private MovieData() {
        movies = new MutableLiveData<>();
        moviesByGenre = new MutableLiveData<>();
        searchedMovies = new MutableLiveData<>();
    }

    public static synchronized MovieData getInstance() {
        if (instance == null) {
            instance = new MovieData();
        }
        return instance;
    }

    public void init(String userId) {
        movieDAO = new MovieDAO(userId);
    }

    public void remove(String listId, String id) {
        movieDAO.remove(listId, id);
    }

    public void saveMovie(String listId, Movie movieToSave) {
        movieDAO.saveMovie(listId, movieToSave);
    }

    public LiveData<ArrayList<MovieList>> getAllListsFromDB() {
        return movieDAO.getAllListsFromDB();
    }

    public String getJustDeletedMovieId() {
        return movieDAO.getJustDeletedMovieId();
    }

    public LiveData<ArrayList<Movie>> getMovies() {
        return movies;
    }

    public LiveData<ArrayList<Movie>> getMoviesByGenre() {
        return moviesByGenre;
    }

    public LiveData<ArrayList<Movie>> getSearchedMovies() {
        return searchedMovies;
    }

    public void getMoviesFromAPI() {
        MovieAPI movieAPI = MovieServiceGenerator.getMovieAPI();
        Call<MovieResponse> call = movieAPI.getMovies();
        call.enqueue(new Callback<MovieResponse>() {
            @EverythingIsNonNull
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful()) {
                    movies.setValue(response.body().getMovie());
                }
            }

            @EverythingIsNonNull
            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Log.i("Retrofit", "Something went wrong :(");
            }
        });
    }

    public void getMoviesByGenreFromAPI(int id) {
        MovieAPI movieAPI = MovieServiceGenerator.getMovieAPI();
        Call<MovieResponse> call = movieAPI.getMoviesByGenre(id);
        call.enqueue(new Callback<MovieResponse>() {
            @EverythingIsNonNull
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful()) {
                    moviesByGenre.setValue(response.body().getMovie());
                }
            }

            @EverythingIsNonNull
            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Log.i("Retrofit", "Something went wrong :(");
            }
        });
    }

    public void searchMovie(String name) {
        MovieAPI movieAPI = MovieServiceGenerator.getMovieAPI();
        Call<MovieResponse> call = movieAPI.searchMovie(name);
        call.enqueue(new Callback<MovieResponse>() {
            @EverythingIsNonNull
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful()) {
                    searchedMovies.setValue(response.body().getMovie());
                }
            }

            @EverythingIsNonNull
            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Log.i("Retrofit", "Something went wrong :(");
            }
        });
    }
}
