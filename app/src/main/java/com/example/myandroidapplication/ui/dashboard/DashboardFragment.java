package com.example.myandroidapplication.ui.dashboard;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myandroidapplication.Model.genre.Genre;
import com.example.myandroidapplication.Model.movie.Movie;
import com.example.myandroidapplication.R;
import com.example.myandroidapplication.ViewModel.DashboardViewModel;
import com.example.myandroidapplication.ui.home.MovieAdapter;
import com.google.gson.Gson;

import java.util.ArrayList;

public class DashboardFragment extends Fragment implements GenreAdapter.OnListItemClickListener, MovieAdapter.OnListItemClickListener{

    private DashboardViewModel viewModel;
    private RecyclerView genreList;
    private RecyclerView searchResult;
    private Gson gson;
    private ArrayList<Genre> list;
    private ArrayList<Movie> movieList;
    private ProgressBar progressBar;
    private EditText searchEditText;
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private MutableLiveData<Boolean> isSearched = new MutableLiveData<>(false);

    public DashboardFragment() {
        this.gson = new Gson();
        this.list = new ArrayList<>();
        this.movieList = new ArrayList<>();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        viewModel = DashboardViewModel.getInstance();

        isLoading.setValue(true);
        genreList = root.findViewById(R.id.searchRecycleView);
        searchResult = root.findViewById(R.id.searchedMovieRecycleView);
        searchResult.setVisibility(View.INVISIBLE);

        progressBar = root.findViewById(R.id.selectedGenreProgressBar);
        genreList.hasFixedSize();
        GridLayoutManager manager = new GridLayoutManager(getContext(),3,RecyclerView.VERTICAL,false);
        genreList.setLayoutManager(manager);

        viewModel.getGenresFromAPI();
        viewModel.getGenres().observe(getViewLifecycleOwner(), genres -> {
            for (Genre genre: genres) {
                switch (genre.getId()) {
                    case 28:
                        genre.setImageId(R.drawable.action_c);
                        break;
                    case 12:
                        genre.setImageId(R.drawable.adventure_c);
                        break;
                    case 16:
                        genre.setImageId(R.drawable.animation);
                        break;
                    case 35:
                        genre.setImageId(R.drawable.comedy_c);
                        break;
                    case 80:
                        genre.setImageId(R.drawable.crime);
                        break;
                    case 99:
                        genre.setImageId(R.drawable.documentary_c);
                        break;
                    case 18:
                        genre.setImageId(R.drawable.drama_c);
                        break;
                    case 10751:
                        genre.setImageId(R.drawable.ic_baseline_family);
                        break;
                    case 14:
                        genre.setImageId(R.drawable.castle_c2);
                        break;
                    case 36:
                        genre.setImageId(R.drawable.history_c2);
                        break;
                    case 27:
                        genre.setImageId(R.drawable.horror_c);
                        break;
                    case 10402:
                        genre.setImageId(R.drawable.ic_baseline_music_note_24);
                        break;
                    case 9648:
                        genre.setImageId(R.drawable.mistery_c);
                        break;
                    case 10749:
                        genre.setImageId(R.drawable.romance_c);
                        break;
                    case 878:
                        genre.setImageId(R.drawable.scienceandfiction_c);
                        break;
                    case 10770:
                        genre.setImageId(R.drawable.tvshows_c);
                        break;
                    case 53:
                        genre.setImageId(R.drawable.triller_c);
                        break;
                    case 10752:
                        genre.setImageId(R.drawable.war_c);
                        break;
                    case 37:
                        genre.setImageId(R.drawable.western_c);
                        break;
                    default:
                        genre.setImageId(R.drawable.ic_dashboard_black_24dp);
                        break;
                }
            }

            for (int i = 0; i < genres.size(); i++)
            {
                list.add(genres.get(i));
            }

            isLoading.setValue(false);

            GenreAdapter adapter = new GenreAdapter(genres, this);
            genreList.setAdapter(adapter);
        });

        isLoading().observe(getViewLifecycleOwner(), isLoading -> {
            int visibility = isLoading ? View.VISIBLE : View.INVISIBLE;
            progressBar.setVisibility(visibility);
            int visibilityForGenreList = isLoading ? View.INVISIBLE : View.VISIBLE;
            genreList.setVisibility(visibilityForGenreList);

        });

        searchEditText = root.findViewById(R.id.searchMovieEditText);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                searchMovie(s.toString());
            }
        });
        return root;
    }

    public LiveData<Boolean> isLoading() {
        return isLoading;
    }

    public LiveData<Boolean> isSearched() {
        return isSearched;
    }

    private void closeKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager manager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(view.getWindowToken(), 0);

            InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {

//        Bundle bundle = new Bundle();
//        bundle.putString("name", list.get(clickedItemIndex).getName());
//
//        Fragment fragment = new SelectedGenreFragment();
//        fragment.setArguments(bundle);
//
//        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.container,fragment ).commit();

        String toNewFragment = gson.toJson(list.get(clickedItemIndex));

        viewModel.sendGenreInfo(toNewFragment);
        Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.navigate_to_selected_genre);
    }

//    @Overridex
//    public void onListItemClick(int clickedItemIndex) {
//        String toNewFragment = gson.toJson(list.get(clickedItemIndex));
//
//        viewModel.sendGenreInfo(toNewFragment);
//        Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.navigate_to_selected_genre);
//    }

    public void searchMovie(String name) {
        viewModel.searchMovie(name);


        isSearched().observe(getViewLifecycleOwner(), isSearched -> {
            int visibilityForGenreList = isSearched ? View.INVISIBLE : View.VISIBLE;
            genreList.setVisibility(visibilityForGenreList);
            int visibilityForSearchedMovieList = isSearched ? View.VISIBLE : View.INVISIBLE;
            searchResult.setVisibility(visibilityForSearchedMovieList);
        });

        viewModel.getSearchedMovies().observe(getViewLifecycleOwner(), movies -> {
            if (!movies.isEmpty()) {

                searchResult.hasFixedSize();
                searchResult.setLayoutManager(new LinearLayoutManager(getActivity()));

                MovieAdapter adapter = new MovieAdapter(movies, getContext(), this);

                movieList = new ArrayList<>();
                for (int i = 0; i < movies.size(); i++)
                {
                    movieList.add(movies.get(i));
                }

                isSearched.setValue(true);
                searchResult.setAdapter(adapter);
            }
        });
    }
}