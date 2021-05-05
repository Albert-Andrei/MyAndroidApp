package com.example.myandroidapplication.ui.home;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

    public SelectedMovieActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        viewModel.init();

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

        Button watchLater = findViewById(R.id.watchLater);
        watchLater.setOnClickListener(v -> {
            viewModel.saveToWatchLater(movie);
            Toast.makeText(this, movie.getName() + " Saved", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    public void back(View view) {
        finish();
    }
}