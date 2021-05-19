package com.example.myandroidapplication.ui.movies;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myandroidapplication.Model.MovieList;
import com.example.myandroidapplication.Model.movie.Movie;
import com.example.myandroidapplication.R;
import com.example.myandroidapplication.ViewModel.MoviesViewModel;
import com.example.myandroidapplication.ViewModel.NavigationViewModel;
import com.example.myandroidapplication.ui.home.SelectedMovieActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.util.ArrayList;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class SelectedList extends Fragment implements SelectedListAdapter.OnListItemClickListener {

    private View fragment;
    private MoviesViewModel viewModel;
    private NavigationViewModel navigationViewModel;
    private ArrayList<Movie> list;
    private TextView textView;
    private RecyclerView recyclerView;
    private MovieList listFromNavigation = new MovieList();
    private ProgressBar progressBar;
    private SelectedListAdapter adapter;
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private Button submit;
    private Dialog dialog;
    private EditText rating;
    private Gson gson = new Gson();

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragment = inflater.inflate(R.layout.selected_list_fragment, container, false);
        getActivity().getWindow().setStatusBarColor(getActivity().getColor(R.color.white));

        viewModel = MoviesViewModel.getInstance();
        navigationViewModel = NavigationViewModel.getInstance();

        isLoading.setValue(true);

        progressBar = fragment.findViewById(R.id.selectedListPB);
        recyclerView = fragment.findViewById(R.id.selectedListRecycleView);
        textView = fragment.findViewById(R.id.selectedListName);


        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.custom_dialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.animation;
        dialog.setCancelable(false);

        submit = dialog.findViewById(R.id.doneRating);
        rating = dialog.findViewById(R.id.ratingEditText);

        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        textView.setText(navigationViewModel.getListOfMovies().getName());
        listFromNavigation = navigationViewModel.getListOfMovies();
        list = navigationViewModel.getListOfMovies().getList();
        adapter = new SelectedListAdapter(listFromNavigation.getId(), list, getContext(), this);

        ItemTouchHelper itemTouchHelper;
        if (listFromNavigation.getId().equals("archive")) {
            itemTouchHelper = new ItemTouchHelper(simpleCallbackOnlyDelete);
        } else {
            itemTouchHelper = new ItemTouchHelper(simpleCallback);
        }
        itemTouchHelper.attachToRecyclerView(recyclerView);

        isLoading.setValue(false);

        recyclerView.setAdapter(adapter);

        fragment.findViewById(R.id.textViewSelectedGenre).setOnClickListener((View view) -> {
            Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.navigate_to_my_movies);
            getActivity().getWindow().setStatusBarColor(getActivity().getColor(R.color.main));
        });

        isLoading().observe(getViewLifecycleOwner(), isLoading -> {
            int visibility = isLoading ? View.VISIBLE : View.INVISIBLE;
            progressBar.setVisibility(visibility);
        });
        return fragment;
    }

    public LiveData<Boolean> isLoading() {
        return isLoading;
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        Movie deletedMovie = null;

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            int position = viewHolder.getAdapterPosition();

            switch (direction) {
                case ItemTouchHelper.LEFT:
                    deletedMovie = list.get(position);
                    list.remove(position);
                    viewModel.remove(listFromNavigation.getId(), deletedMovie.getId());
                    adapter.notifyItemRemoved(position);
                    Snackbar snackbar = Snackbar.make(recyclerView, deletedMovie.getName() + " Removed", Snackbar.LENGTH_LONG)
                            .setActionTextColor(ContextCompat.getColor(getContext(), R.color.red))
                            .setAction("Undo", v -> {
                                list.add(position, deletedMovie);
                                viewModel.saveMovie(listFromNavigation.getId(), deletedMovie);
                                adapter.notifyItemInserted(position);
                            });
                    View snackBarView = snackbar.getView();
                    snackBarView.setTranslationY(-45);
                    snackbar.show();
                    break;
                case ItemTouchHelper.RIGHT:
                    // Saving the movie that have to be deleted in a temporal variable
                    deletedMovie = list.get(position);
                    // Removing movie from the lis that is displayed in screen
                    list.remove(position);
                    // Removing movie in DB from the list we are currently
                    viewModel.remove(listFromNavigation.getId(), deletedMovie.getId());
                    // Saving the movie to archive list in DB
                    dialog.show();

                    String deleted = getResources().getString(R.string.removed);
                    Snackbar snackbarArchive = Snackbar.make(recyclerView, deletedMovie.getName() + " " + deleted, Snackbar.LENGTH_LONG)
                            .setActionTextColor(ContextCompat.getColor(getContext(), R.color.red))
                            .setAction("Undo", v -> {
                                list.add(position, deletedMovie);
                                viewModel.remove("archive", viewModel.getJustDeletedMovieId());
                                if (listFromNavigation.getId().equals("watch_later")){
                                    deletedMovie.setPersonalRating(0.0);
                                }
                                viewModel.saveMovie(listFromNavigation.getId(), deletedMovie);
                                adapter.notifyItemInserted(position);
                            });
                    View snackBarViewArchive = snackbarArchive.getView();
                    snackBarViewArchive.setTranslationY(-45);


                    submit.setOnClickListener(v -> {
                        String str = rating.getText().toString();
                        double value;
                        if (!str.equals("")) {
                             value = Double.parseDouble(str);
                        } else {
                            value = 0.0;
                        }
                        deletedMovie.setPersonalRating(value);
                        dialog.dismiss();
                        viewModel.saveMovie("archive", deletedMovie);
                        snackbarArchive.show();
                    });

                    adapter.notifyItemRemoved(position);
                    break;
            }
        }


        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(getContext(), R.color.main))
                    .addSwipeRightBackgroundColor(ContextCompat.getColor(getContext(), R.color.main))
                    .addSwipeLeftActionIcon(R.drawable.ic_baseline_delete_24)
                    .addSwipeRightActionIcon(R.drawable.ic_baseline_archive_24)
                    .create()
                    .decorate();

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };


    ItemTouchHelper.SimpleCallback simpleCallbackOnlyDelete = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        Movie deletedMovie = null;

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            int position = viewHolder.getAdapterPosition();

            switch (direction) {
                case ItemTouchHelper.LEFT:
                    deletedMovie = list.get(position);
                    list.remove(position);
                    viewModel.remove(listFromNavigation.getId(), deletedMovie.getId());
                    adapter.notifyItemRemoved(position);
                    Snackbar snackbar = Snackbar.make(recyclerView, deletedMovie.getName() + " Removed", Snackbar.LENGTH_LONG)
                            .setActionTextColor(ContextCompat.getColor(getContext(), R.color.red))
                            .setAction("Undo", v -> {
                                list.add(position, deletedMovie);
                                viewModel.saveMovie(listFromNavigation.getId(), deletedMovie);
                                adapter.notifyItemInserted(position);
                            });
                    View snackBarView = snackbar.getView();
                    snackBarView.setTranslationY(-45);
                    snackbar.show();
                    break;
            }
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(getContext(), R.color.main))
                    .addSwipeLeftActionIcon(R.drawable.ic_baseline_delete_24)
                    .create()
                    .decorate();

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    @Override
    public void onListItemClick(int clickedItemIndex) {
        Intent intent = new Intent(getContext(), SelectedMovieActivity.class);
        String toNewView = gson.toJson(list.get(clickedItemIndex));
        intent.putExtra("movie", toNewView);
        intent.putExtra("my_movies", true);
        intent.putExtra("listId", listFromNavigation.getId());

        startActivityForResult(intent, 1);
    }
}
