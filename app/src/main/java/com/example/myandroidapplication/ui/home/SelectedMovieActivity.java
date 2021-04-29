package com.example.myandroidapplication.ui.home;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.myandroidapplication.Model.movie.Movie;
import com.example.myandroidapplication.R;
import com.google.gson.Gson;

public class SelectedMovieActivity extends AppCompatActivity {

    private Gson gson = new Gson();

    public SelectedMovieActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_movie);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        setTitle("Send mail");

        Bundle bundle = getIntent().getExtras();
        String data = bundle.getString("movie");
        Movie movie = gson.fromJson(data, Movie.class);

        TextView textView = findViewById(R.id.expandedMovieTitle);
        textView.setText(movie.getName());

        TextView MovieDescription = findViewById(R.id.expandedMovieDescription);
        MovieDescription.setText("      " + movie.getOverview());

        TextView rating = findViewById(R.id.expanded_movie_rating);
        rating.setText(movie.getRating());

        ImageView image = findViewById(R.id.backdropImage);
        Glide.with(this)
                .load("https://image.tmdb.org/t/p/w500" + movie.getBackdropImage())
                .into(image);
    }

    public void back(View view) {
        finish();
    }
}