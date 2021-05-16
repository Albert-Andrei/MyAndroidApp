package com.example.myandroidapplication.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myandroidapplication.Model.genre.Genre;
import com.example.myandroidapplication.Model.movie.Movie;
import com.example.myandroidapplication.R;
import com.example.myandroidapplication.ViewModel.DashboardViewModel;
import com.example.myandroidapplication.ui.home.MovieAdapter;
import com.example.myandroidapplication.ui.home.SelectedMovieActivity;
import com.google.gson.Gson;

import java.util.ArrayList;

public class SelectedGenreFragment extends Fragment implements MovieAdapter.OnListItemClickListener {

    private DashboardViewModel viewModel;
    private RecyclerView genreList;
    private Gson gson;
    private Genre genre;
    private RecyclerView movieList;
    private ProgressBar progressBar;
    private ArrayList<Movie> list;
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);

    public SelectedGenreFragment() {
        this.gson = new Gson();
        this.viewModel = DashboardViewModel.getInstance();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View myFragment = inflater.inflate(R.layout.fragment_selected_genre, container, false);
        getActivity().getWindow().setStatusBarColor(getActivity().getColor(R.color.main));

//        String name = getArguments().getString("name");
//        TextView genreName = getActivity().findViewById(R.id.selectedGenreName);
//        genreName.setText(name);

        isLoading.setValue(true);
        viewModel.getGenreInfo().observe(getViewLifecycleOwner(), name ->{
            genre = gson.fromJson(name, Genre.class);
            TextView genreName = getActivity().findViewById(R.id.selectedGenreName);
            genreName.setText(genre.getName());
            viewModel.getMoviesByGenreFromAPI(genre.getId());
        });


        progressBar = myFragment.findViewById(R.id.movieProgressBarSelectedGenre);
        movieList = myFragment.findViewById(R.id.selectedGenreRecycleView);
        movieList.hasFixedSize();
        movieList.setLayoutManager(new LinearLayoutManager(getActivity()));

        viewModel.getMoviesByGenre().observe(getViewLifecycleOwner(), movies -> {
            isLoading.setValue(false);
            MovieAdapter adapter = new MovieAdapter(movies, getContext(), this);

            list = new ArrayList<>();
            for (int i = 0; i < movies.size(); i++)
            {
                list.add(movies.get(i));
            }
            movieList.setAdapter(adapter);
        });

            isLoading().observe(getViewLifecycleOwner(), isLoading -> {
                int visibility = isLoading ? View.VISIBLE : View.INVISIBLE;
                    progressBar.setVisibility(visibility);
            });

        myFragment.findViewById(R.id.textViewSelectedGenre).setOnClickListener((View view) -> {
            Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.navigate_to_search_fragment);
            getActivity().getWindow().setStatusBarColor(getActivity().getColor(R.color.main));
        });

//        myFragment.findViewById(R.id.imageViewSelectedGenre).setOnClickListener((View view) -> {
//            Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.navigate_to_search_fragment);
//            getActivity().getWindow().setStatusBarColor(getActivity().getColor(R.color.main));
//        });

        getActivity().getWindow().setStatusBarColor(getActivity().getColor(R.color.white));
        return myFragment;
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {

        Intent intent = new Intent(getContext(), SelectedMovieActivity.class);
        String toNewView = gson.toJson(list.get(clickedItemIndex));
        intent.putExtra("movie", toNewView);

        startActivityForResult(intent, 1);
    }

    public LiveData<Boolean> isLoading() {
        return isLoading;
    }
}
