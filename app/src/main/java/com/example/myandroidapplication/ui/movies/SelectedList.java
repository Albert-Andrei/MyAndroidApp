package com.example.myandroidapplication.ui.movies;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myandroidapplication.Model.MovieList;
import com.example.myandroidapplication.R;
import com.example.myandroidapplication.ViewModel.MoviesViewModel;
import com.google.gson.Gson;

import java.util.ArrayList;

public class SelectedList extends Fragment implements SelectedListAdapter.OnListItemClickListener{

    View fragment;
    private MoviesViewModel viewModel;
//    private Gson gson;
//    private ArrayList<MovieList> list;
    TextView textView;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private MutableLiveData<ArrayList<MovieList>> caloh = new MutableLiveData<>();

    public SelectedList() {
//        this.gson = new Gson();
//        list = new ArrayList<>();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragment = inflater.inflate(R.layout.selected_list_fragment, container, false);
        getActivity().getWindow().setStatusBarColor(getActivity().getColor(R.color.white));

        viewModel = MoviesViewModel.getInstance();
        isLoading.setValue(true);

        progressBar = fragment.findViewById(R.id.selectedListPB);
        recyclerView = fragment.findViewById(R.id.selectedListRecycleView);
        textView = fragment.findViewById(R.id.selectedListName);

        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        viewModel.getWatchLaterList().observe(getViewLifecycleOwner(), movieList -> {
            textView.setText(movieList.getName());
            SelectedListAdapter adapter = new SelectedListAdapter(movieList.getList(),getContext(), this);
            isLoading.setValue(false);
            recyclerView.setAdapter(adapter);
        });

        fragment.findViewById(R.id.textViewSelectedGenre).setOnClickListener((View view) -> {
            Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.navigate_to_my_movies);
            getActivity().getWindow().setStatusBarColor(getActivity().getColor(R.color.main));
        });
//        getCalhoz().observe(getViewLifecycleOwner(), listsOfMovieList -> {
//            // Controleaza asta ca ciota interesant poate sa iasa aici
//            // asta nu merge ca lista e live pahodu
//
//            if (listsOfMovieList.size() == 0) {
//                textView.setText("No movies, please add some");
//                isLoading.setValue(false);
//            } else {
//                textView.setText("");
//
//            }
//
//            //   MovieAdapter adapter = new MovieAdapter(l, getContext(), this);
//
//        });

        isLoading().observe(getViewLifecycleOwner(), isLoading -> {
            int visibility = isLoading ? View.VISIBLE : View.INVISIBLE;
            progressBar.setVisibility(visibility);
        });
        return fragment;
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
