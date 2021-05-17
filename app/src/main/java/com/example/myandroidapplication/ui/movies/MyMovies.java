package com.example.myandroidapplication.ui.movies;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myandroidapplication.Model.MovieList;
import com.example.myandroidapplication.R;
import com.example.myandroidapplication.ViewModel.HomeViewModel;
import com.example.myandroidapplication.ViewModel.MoviesViewModel;
import com.example.myandroidapplication.ViewModel.NavigationViewModel;
import com.google.gson.Gson;

import java.util.ArrayList;

public class MyMovies extends Fragment implements MyMoviesAdapter.OnListItemClickListener {

    private MoviesViewModel viewModel;
    private NavigationViewModel navigationViewModel;
    private Gson gson;
    private TextView textView;
    private ArrayList<MovieList> list;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private String name;
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private MutableLiveData<ArrayList<MovieList>> caloh = new MutableLiveData<>();

    public MyMovies() {
        this.gson = new Gson();
        list = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.my_movies_fragment, container, false);
        getActivity().getWindow().setStatusBarColor(getActivity().getColor(R.color.main));
        viewModel = MoviesViewModel.getInstance();
        navigationViewModel = NavigationViewModel.getInstance();
        isLoading.setValue(true);

        progressBar = root.findViewById(R.id.myMoviesProgressBar);
        recyclerView = root.findViewById(R.id.myMoviesRecycleView);
        textView = root.findViewById(R.id.myMoviesListText);
        recyclerView.hasFixedSize();

        viewModel.getAllListsFromDB().observe(getViewLifecycleOwner(), theList -> {
            if (list.isEmpty()) {
                for (MovieList movieList : theList) {
                    if (movieList.getId() != null) {
                        switch (movieList.getId()) {
                            case "watch_later":
                                name = getResources().getString(R.string.watchLater);
                                movieList.setName(name);
                                break;
                            case "archive":
                                name = getResources().getString(R.string.archive);
                                movieList.setName(name);
                                break;
                            case "favorite":
                                name = getResources().getString(R.string.favorite);
                                movieList.setName(name);
                                break;
                            default:
                                movieList = new MovieList("Unknown");
                                movieList.setImageId(R.drawable.ic_baseline_do_not_disturb_alt_24);
                        }
                    }
                    list.add(movieList);
                }
            }
            caloh.setValue(list);
        });


        getCalhoz().observe(getViewLifecycleOwner(), listsOfMovieList -> {
            if (listsOfMovieList.size() == 0) {
                textView.setText("No movies, please add some");
                isLoading.setValue(false);
            } else {
                textView.setText("");
                isLoading.setValue(false);
            }

            GridLayoutManager manager = new GridLayoutManager(getContext(), 2, RecyclerView.VERTICAL, false);
            recyclerView.setLayoutManager(manager);
            MyMoviesAdapter adapter = new MyMoviesAdapter(listsOfMovieList, this);
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
        if (list.get(clickedItemIndex).isEmpty()) {
            Toast.makeText(getActivity(), "Nothing here yet", Toast.LENGTH_SHORT).show();
        } else {
            navigationViewModel.setListOfMovies(list.get(clickedItemIndex));
            Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.navigate_to_selected_list);
        }
    }
}
