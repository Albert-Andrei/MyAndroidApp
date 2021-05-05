package com.example.myandroidapplication.Model.Data;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.myandroidapplication.Model.movie.Movie;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class MovieDAO extends LiveData<Movie> {

    DatabaseReference databaseReference;

    public MovieDAO(DatabaseReference ref) {
        databaseReference = ref;
    }

    private final ValueEventListener listener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            Movie movie = snapshot.getValue(Movie.class);
            setValue(movie);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
        }
    };

    @Override
    protected void onActive() {
        super.onActive();
        databaseReference.addValueEventListener(listener);
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        databaseReference.removeEventListener(listener);
    }
}
