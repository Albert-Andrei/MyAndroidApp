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

//        viewModel.getWatchLaterListFromDB().observe(getViewLifecycleOwner(), movieList -> {
//            if (list.isEmpty()) {
//                list.add(movieList);
//            }
//            caloh.setValue(list);
//        });

        viewModel.getAllListsFromDB().observe(getViewLifecycleOwner(), something -> {
            if (list.isEmpty()) {
                for (MovieList movieList : something) {
                    list.add(movieList);
//                    Log.i("CHEEEEEECK", "<><><><<><>><>><><> onCreateView: " + movieList.getName() +
//                            " " + movieList.getSize());
                }
            }
            caloh.setValue(list);
        });


        getCalhoz().observe(getViewLifecycleOwner(), listsOfMovieList -> {
            // Controleaza asta ca ciota interesant poate sa iasa aici
            // asta nu merge ca lista e live pahodu

            if (listsOfMovieList.size() == 0) {
                textView.setText("No movies, please add some");
                isLoading.setValue(false);
            } else {
                textView.setText("");
                isLoading.setValue(false);
            }

            GridLayoutManager manager = new GridLayoutManager(getContext(), 2, RecyclerView.VERTICAL, false);
            recyclerView.setLayoutManager(manager);
            //   MovieAdapter adapter = new MovieAdapter(l, getContext(), this);
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
//        Toast.makeText(getContext(), list.get(clickedItemIndex).getList().get(3).getName() , Toast.LENGTH_SHORT).show();

//        String toNewFragment = gson.toJson(list.get(clickedItemIndex).getList());
//
//        viewModel.sendGenreInfo(toNewFragment);
        if (list.get(clickedItemIndex).isEmpty()) {
            Toast.makeText(getActivity(), "Nothing here yet", Toast.LENGTH_SHORT).show();
        } else {
            navigationViewModel.setListOfMovies(list.get(clickedItemIndex));
            Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.navigate_to_selected_list);
        }
    }
}
