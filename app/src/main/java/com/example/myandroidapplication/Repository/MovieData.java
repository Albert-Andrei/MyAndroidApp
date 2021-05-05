package com.example.myandroidapplication.Repository;

import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.myandroidapplication.Model.Data.MovieDAO;
import com.example.myandroidapplication.Model.movie.Movie;
import com.example.myandroidapplication.Model.movie.MovieResponse;
import com.example.myandroidapplication.RemoteDataSource.movie.MovieAPI;
import com.example.myandroidapplication.RemoteDataSource.movie.MovieServiceGenerator;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
    private DatabaseReference myRef;
    private MovieDAO message;
    String userId;

    private MovieData() {
        movies = new MutableLiveData<>();
        moviesByGenre = new MutableLiveData<>();
        searchedMovies = new MutableLiveData<>();
        userId = "";
    }

    public static synchronized MovieData getInstance() {
        if (instance == null) {
            instance = new MovieData();
        }
        return instance;
    }

    public void init(String userId) {
        myRef = FirebaseDatabase.getInstance("https://mymoviedb-9fd2a-default-rtdb.europe-west1.firebasedatabase.app/").getReference().child("users").child(userId);
        message = new MovieDAO(myRef);
    }

    public void saveToWatchLater(Movie movieToSave) {
        myRef.child("watch_later").push().setValue(movieToSave);
    }

//
//    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("message123");
////        myRef.push().setValue(list.get(clickedItemIndex));
//
//        myRef.addValueEventListener(new ValueEventListener() {
//
//        public void onDataChange(DataSnapshot dataSnapshot) {
//            Movie movie = dataSnapshot.getValue(Movie.class);
//            Log.i("MOVIE", movie.getName() + " <><><><><><><><><><><><>");
//        }
//
//        public void onCancelled(DatabaseError databaseError) {}
//
//    });

    public MovieDAO getMovieFromDB() {
        return message;
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

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }
}
