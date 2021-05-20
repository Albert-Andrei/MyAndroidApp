package com.example.myandroidapplication.ui.home;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
    private Dialog dialog;
    private String listId;

    public SelectedMovieActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        setContentView(R.layout.activity_selected_movie);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_dialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.animation;
        dialog.setCancelable(false);

        Button submit = dialog.findViewById(R.id.doneRating);
        EditText ratingDialogTView = dialog.findViewById(R.id.ratingEditText);

        Bundle bundle = getIntent().getExtras();
        String data = bundle.getString("movie");
        boolean check = bundle.getBoolean("my_movies");
        listId = bundle.getString("listId");
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

        if (listId.equals("watch_later")) {
            editMovie.setVisibility(View.INVISIBLE);
            ViewGroup.LayoutParams params = new LinearLayout.LayoutParams(1, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
            editMovie.setLayoutParams(params);
        }

        editMovie.setOnClickListener(v -> {
            dialog.show();

            submit.setOnClickListener(v1 -> {
                if (ratingDialogTView.getText().toString().equals("")) {
                    Toast.makeText(this, "Please rate this movie", Toast.LENGTH_SHORT).show();
                } else {
                    double value = Double.parseDouble(ratingDialogTView.getText().toString());
                    dialog.dismiss();
                    ratingDialogTView.setText("");
                    movie.setPersonalRating(value);
                    viewModel.editMoviePersonalRating(listId, movie.getId(), value);
                }
            });
        });

        watchLater.setOnClickListener(v -> {
            viewModel.saveMovie("watch_later", movie);
            Toast.makeText(this, movie.getName() + " Saved", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    public void back(View view) {
        finish();
    }
}