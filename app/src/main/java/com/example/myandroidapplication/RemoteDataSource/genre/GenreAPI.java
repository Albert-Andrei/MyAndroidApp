package com.example.myandroidapplication.RemoteDataSource.genre;

import com.example.myandroidapplication.Model.genre.GenreResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface GenreAPI {
    @GET("3/genre/movie/list?api_key=33da4b0735e9ca10f0db031928a139eb&language=en-US")
    Call<GenreResponse> getGenres();
}
