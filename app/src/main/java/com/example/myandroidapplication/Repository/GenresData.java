package com.example.myandroidapplication.Repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.myandroidapplication.Model.genre.Genre;
import com.example.myandroidapplication.Model.genre.GenreResponse;
import com.example.myandroidapplication.RemoteDataSource.genre.GenreAPI;
import com.example.myandroidapplication.RemoteDataSource.genre.GenreServiceGenerator;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

public class GenresData {

    private static GenresData instance;
    private final MutableLiveData<ArrayList<Genre>> genres;

    private GenresData() {
        genres = new MutableLiveData<>();
    }

    public static synchronized GenresData getInstance() {
        if (instance == null) {
            instance = new GenresData();
        }
        return instance;
    }

    public LiveData<ArrayList<Genre>> getGenres() {
        return genres;
    }

    public void getGenresFromAPI() {
        GenreAPI genreAPI = GenreServiceGenerator.getGenreAPI();
        Call<GenreResponse> call = genreAPI.getGenres();
        call.enqueue(new Callback<GenreResponse>() {
            @EverythingIsNonNull
            @Override
            public void onResponse(Call<GenreResponse> call, Response<GenreResponse> response) {
                if (response.isSuccessful()) {
                    genres.setValue(response.body().getGenre());
                }
            }
            @EverythingIsNonNull
            @Override
            public void onFailure(Call<GenreResponse> call, Throwable t) {
                Log.i("Retrofit", "Something went wrong :(");
            }
        });
    }
}
