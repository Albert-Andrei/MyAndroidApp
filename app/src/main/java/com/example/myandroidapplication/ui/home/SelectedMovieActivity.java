package com.example.myandroidapplication.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.myandroidapplication.Model.movie.Movie;
import com.example.myandroidapplication.R;
import com.example.myandroidapplication.ViewModel.HomeViewModel;
import com.google.gson.Gson;

public class SelectedMovieActivity extends AppCompatActivity {

    private Gson gson = new Gson();
    private HomeViewModel viewModel;
    private Movie movie;

    public SelectedMovieActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        setContentView(R.layout.activity_selected_movie);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        Bundle bundle = getIntent().getExtras();
        String data = bundle.getString("movie");
        boolean check = bundle.getBoolean("my_movies");
        movie = gson.fromJson(data, Movie.class);

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

        Button watchLater = findViewById(R.id.watchLater);
        LinearLayout linearLayout = findViewById(R.id.shareAndEditLinearLay);
        linearLayout.setVisibility(View.INVISIBLE);
        if (check) {
            watchLater.setVisibility(View.INVISIBLE);
            linearLayout.setVisibility(View.VISIBLE);
        }

        ImageView shareMovie = findViewById(R.id.shareMovieAc);
        ImageView editMovie = findViewById(R.id.editMovieAc);

        shareMovie.setOnClickListener(v -> {
            Intent myIntent = new Intent(Intent.ACTION_SEND);
            myIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{});
            myIntent.setType("text/plain");
            String msg = getResources().getString(R.string.share_message);
            String body = movie.getName() + msg;
            String sub = "Movie from MyMovieDB";
            myIntent.putExtra(Intent.EXTRA_SUBJECT, sub);
            myIntent.putExtra(Intent.EXTRA_TEXT, body);
            startActivity(Intent.createChooser(myIntent, "Share using"));
        });

        editMovie.setOnClickListener(v -> {
            // To implement
        });

        watchLater.setOnClickListener(v -> {
            viewModel.saveMovie("watch_later",movie);
            Toast.makeText(this, movie.getName() + " Saved", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    public void back(View view) {
        finish();
    }
}