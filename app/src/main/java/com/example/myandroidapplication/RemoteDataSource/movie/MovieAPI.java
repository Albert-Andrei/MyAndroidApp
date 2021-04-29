package com.example.myandroidapplication.RemoteDataSource.movie;

import com.example.myandroidapplication.Model.movie.MovieResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieAPI {
    @GET("3/movie/popular?api_key=33da4b0735e9ca10f0db031928a139eb")
    Call<MovieResponse> getMovies();

    @GET("3/genre/{id}/movies?api_key=33da4b0735e9ca10f0db031928a139eb&include_adult=false")
    Call<MovieResponse> getMoviesByGenre(@Path("id") int id);

    @GET("3/search/movie?api_key=33da4b0735e9ca10f0db031928a139eb&language=en-US&page=1&include_adult=false")
    Call<MovieResponse> searchMovie(@Query("query") String name);
}
