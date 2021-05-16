package com.example.myandroidapplication.Model.Data;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.myandroidapplication.Model.MovieList;
import com.example.myandroidapplication.Model.movie.Movie;
import com.example.myandroidapplication.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class MovieDAO extends LiveData<Movie> {

    private DatabaseReference myRef;
    private final MutableLiveData<MovieList> selectedMovieList;
    private final MutableLiveData<ArrayList<MovieList>> allMovieLists;
    private String deletedMovie = "";

    public MovieDAO(String userId) {
        selectedMovieList = new MutableLiveData<>();
        allMovieLists = new MutableLiveData<>();
        myRef = FirebaseDatabase.getInstance("https://mymoviedb-9fd2a-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference().child("users").child(userId);
    }

    /**
     * Removes a movie from db by it's id and the id of the list where this movie is situated
     *
     * @param listId
     * @param id
     */
    public void remove(String listId, String id) {
        myRef.child(listId).child(id).removeValue();
    }

    /**
     * Here a movie is save a movie into a specific list in the database
     *
     * @param movieToSave
     * @param listId
     */
    public void saveMovie(String listId, Movie movieToSave) {
        deletedMovie = myRef.child(listId).push().getKey();
        myRef.child(listId).child(deletedMovie).setValue(movieToSave);
    }

    public String getJustDeletedMovieId() {
        return deletedMovie;
    }

    public LiveData<ArrayList<MovieList>> getAllListsFromDB() {
        ArrayList<MovieList> allLists = new ArrayList<>();

        myRef.addValueEventListener(new ValueEventListener() {

            public void onDataChange(DataSnapshot dataSnapshot) {
                allLists.clear();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    MovieList movieList = getMovieList(child.getKey());
                    allLists.add(movieList);
//                    Log.i("CHECK", "<><><><><><><><><<><<><><><><><>onDataChange: " + child.getKey());
                }
                allMovieLists.setValue(allLists);
            }

            public void onCancelled(DatabaseError databaseError) {
            }
        });

        return allMovieLists;
    }

    public MovieList getMovieList(String id) {
        MovieList movieList;

        switch (id) {
            case "watch_later":
                movieList = new MovieList("Watch later");
                movieList.setImageId(R.drawable.ic_wtch_later_icon);
                break;
            case "archive":
                movieList = new MovieList("Archive");
                movieList.setImageId(R.drawable.ic_baseline_archive_24);
                break;
            case "favorite":
                movieList = new MovieList("Favorite");
                movieList.setImageId(R.drawable.add_to_favorite);
                break;
            default:
                movieList = new MovieList("Unknown");
                movieList.setImageId(R.drawable.ic_baseline_do_not_disturb_alt_24);
        }

        myRef.child(id).orderByChild("personalRating").addValueEventListener(new ValueEventListener() {

            public void onDataChange(DataSnapshot dataSnapshot) {
                movieList.getList().clear();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Movie movie = child.getValue(Movie.class);
                    movie.setId(child.getKey());
                    movieList.setId(dataSnapshot.getKey());
                    movieList.addMovie(movie);
                }
                Collections.reverse(movieList.getList());
            }

            public void onCancelled(DatabaseError databaseError) {}
        });

        return movieList;
    }
//
//    public LiveData<MovieList> getWatchLaterListFromDB() {
//        MovieList movieList = new MovieList("Watch later");
//
//        myRef.child("watch_later").addValueEventListener(new ValueEventListener() {
//
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                movieList.getList().clear();
//                for (DataSnapshot child : dataSnapshot.getChildren()) {
//                    Movie movie = child.getValue(Movie.class);
//                    movie.setId(child.getKey());
//                    movieList.setId(dataSnapshot.getKey());
//                    movieList.addMovie(movie);
//                }
//                selectedMovieList.setValue(movieList);
//            }
//
//            public void onCancelled(DatabaseError databaseError) {
//            }
//        });
//
//        movieList.setImageId(R.drawable.ic_wtch_later_icon);
//        return selectedMovieList;
//    }
//
//    public LiveData<MovieList> getWatchLaterList() {
//        return selectedMovieList;
//    }
}
