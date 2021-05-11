package com.example.myandroidapplication.Model.Data;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.myandroidapplication.Model.MovieList;
import com.example.myandroidapplication.Model.movie.Movie;
import com.example.myandroidapplication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MovieDAO extends LiveData<Movie> {

    private DatabaseReference myRef;
    private final MutableLiveData<MovieList> selectedMovieList;

    public MovieDAO(String userId) {
        selectedMovieList = new MutableLiveData<>();
        myRef = FirebaseDatabase.getInstance("https://mymoviedb-9fd2a-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference().child("users").child(userId);
    }

    public void saveToWatchLater(Movie movieToSave) {
        myRef.child("watch_later").push().setValue(movieToSave);
    }

    public LiveData<MovieList> getWatchLaterListFromDB() {
        MovieList movieList = new MovieList("Watch later");

        myRef.child("watch_later").addValueEventListener(new ValueEventListener() {

            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Movie movie = child.getValue(Movie.class);
                    movieList.addMovie(movie);
                }
                selectedMovieList.setValue(movieList);
            }

            public void onCancelled(DatabaseError databaseError) {
            }
        });
        movieList.setImageId(R.drawable.ic_wtch_later_icon);
        return selectedMovieList;
    }

    public LiveData<MovieList> getWatchLaterList() {
        return selectedMovieList;
    }
}
