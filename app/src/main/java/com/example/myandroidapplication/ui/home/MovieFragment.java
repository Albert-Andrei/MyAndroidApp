package com.example.myandroidapplication.ui.home;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.myandroidapplication.Model.movie.Movie;
import com.example.myandroidapplication.R;
import com.example.myandroidapplication.ViewModel.HomeViewModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;

public class MovieFragment extends Fragment implements MovieAdapter.OnListItemClickListener {

    private HomeViewModel viewModel;
    private Gson gson;
    private ArrayList<Movie> list;
    RecyclerView movieList;
    private ProgressBar progressBar;
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);

    public MovieFragment() {
        this.gson = new Gson();
        list = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_movie, container, false);

        isLoading.setValue(true);
        progressBar = root.findViewById(R.id.movieProgressBar);
        movieList = root.findViewById(R.id.movieRecycleView);
        movieList.hasFixedSize();
        movieList.setLayoutManager(new LinearLayoutManager(getActivity()));
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        viewModel.getMovies().observe(getViewLifecycleOwner(), movies -> {
                    for (int i = 0; i < movies.size(); i++)
                    {
                        list.add(movies.get(i));
                    }
                    isLoading.setValue(false);
                    MovieAdapter adapter = new MovieAdapter(movies, getContext(), this);
                    movieList.setAdapter(adapter);
                }
        );

        isLoading().observe(getViewLifecycleOwner(), isLoading -> {
            int visibility = isLoading ? View.VISIBLE : View.INVISIBLE;
                progressBar.setVisibility(visibility);
        });

        return root;
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {

        Intent intent = new Intent(getContext(), SelectedMovieActivity.class);
        String toNewView = gson.toJson(list.get(clickedItemIndex));
        intent.putExtra("movie", toNewView);
        intent.putExtra("my_movies", false);

        startActivityForResult(intent, 1);
    }

    public LiveData<Boolean> isLoading() {
        return isLoading;
    }
}