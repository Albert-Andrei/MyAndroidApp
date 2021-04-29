package com.example.myandroidapplication.RemoteDataSource.genre;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GenreServiceGenerator {
    private static GenreAPI genreAPI;

    public static GenreAPI getGenreAPI() {
        if (genreAPI == null) {
            genreAPI = new Retrofit.Builder()
                    .baseUrl("https://api.themoviedb.org")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(GenreAPI.class);
        }
        return genreAPI;
    }
}
