package com.example.myandroidapplication.RemoteDataSource.movie;

import com.example.myandroidapplication.Model.movie.Movie;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieServiceGenerator {
    private static MovieAPI movieAPI;

    public static MovieAPI getMovieAPI() {
        if (movieAPI == null) {
            movieAPI = new Retrofit.Builder()
                    .baseUrl("https://api.themoviedb.org")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(MovieAPI.class);
        }
        return movieAPI;
    }
}
