package com.example.myandroidapplication.ui.movies;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myandroidapplication.Model.MovieList;
import com.example.myandroidapplication.R;
import com.example.myandroidapplication.ViewModel.HomeViewModel;
import com.example.myandroidapplication.ViewModel.MoviesViewModel;
import com.google.gson.Gson;

import java.util.ArrayList;

public class MyMovies extends Fragment implements MyMoviesAdapter.OnListItemClickListener {

    private MoviesViewModel viewModel;
    private Gson gson;
    private ArrayList<MovieList> list;
    RecyclerView recyclerView;
    private ProgressBar progressBar;
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private MutableLiveData<ArrayList<MovieList>> caloh = new MutableLiveData<>();

    public MyMovies() {
        this.gson = new Gson();
        list = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.viewModel = MoviesViewModel.getInstance();
        viewModel.init();

        MovieList opa = new MovieList("favorites");
        MovieList opa1 = new MovieList("watch later");
        MovieList opa2 = new MovieList("archive");
        ArrayList<MovieList> privet = new ArrayList<>();
        privet.add(opa1);
        privet.add(opa);
        privet.add(opa2);
        caloh.setValue(privet);

        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.my_movies_fragment, container, false);
        viewModel = MoviesViewModel.getInstance();

        isLoading.setValue(true);

        progressBar = root.findViewById(R.id.myMoviesProgressBar);
        recyclerView = root.findViewById(R.id.myMoviesRecycleView);
        recyclerView.hasFixedSize();

        GridLayoutManager manager = new GridLayoutManager(getContext(), 2, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(manager);


        getCalhoz().observe(getViewLifecycleOwner(), lists -> {
            // Controleaza asta ca ciota interesant poate sa iasa aici
            // asta nu merge ca lista e live pahodu

            for (int i = 0; i < lists.size(); i++) {
                list.add(lists.get(i));
            }
            isLoading.setValue(false);

            //   MovieAdapter adapter = new MovieAdapter(l, getContext(), this);
            MyMoviesAdapter adapter = new MyMoviesAdapter(lists, this);
            recyclerView.setAdapter(adapter);
        });

        isLoading().observe(getViewLifecycleOwner(), isLoading -> {
            int visibility = isLoading ? View.VISIBLE : View.INVISIBLE;
            progressBar.setVisibility(visibility);
        });
        return root;
    }

    public LiveData<Boolean> isLoading() {
        return isLoading;
    }

    public LiveData<ArrayList<MovieList>> getCalhoz() {
        return caloh;
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {

    }
}
