package com.example.myandroidapplication.ui.movies;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myandroidapplication.Model.movie.Movie;
import com.example.myandroidapplication.R;
import com.example.myandroidapplication.ViewModel.MoviesViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Collections;

public class SelectedListAdapter extends RecyclerView.Adapter<SelectedListAdapter.ViewHolder> {

    private ArrayList<Movie> list;
    private Context context;
    private MoviesViewModel viewModel;
    private Movie toFavorite;
    private String id;
    final private SelectedListAdapter.OnListItemClickListener mOnListItemClickListener;

    public SelectedListAdapter(String id, ArrayList<Movie> list, Context context, SelectedListAdapter.OnListItemClickListener mOnListItemClickListener) {
        this.list = list;
        this.context = context;
        this.mOnListItemClickListener = mOnListItemClickListener;
        this.viewModel = MoviesViewModel.getInstance();
        this.id = id;
    }

    public interface OnListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.movie_list_item, parent, false);
        return new SelectedListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.custom_dialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.animation;
        dialog.setCancelable(false);

        Button submit = dialog.findViewById(R.id.doneRating);
        EditText rating = dialog.findViewById(R.id.ratingEditText);


        holder.name.setText(list.get(position).getName());
        holder.rating.setText("TMDB Rating: " + list.get(position).getRating());
        if (list.get(position).getPersonalRating() == 0.0) {
            holder.personalRating.setText("");
        } else {
            holder.personalRating.setText(String.valueOf(list.get(position).getPersonalRating()));
        }
        Glide.with(context)
                .load("https://image.tmdb.org/t/p/w500" + list.get(position).getPosterImage())
                .into(holder.image);

        holder.imageAdd.setOnClickListener(v -> {
            toFavorite = list.get(position);
            list.remove(position);
            viewModel.remove(id, toFavorite.getId());
            dialog.show();

            submit.setOnClickListener(v1 -> {
                if (rating.getText().toString().equals("")) {
                    Toast.makeText(context, "Please rate this movie", Toast.LENGTH_SHORT).show();
                } else {
                    double value = Double.parseDouble(rating.getText().toString());
                    toFavorite.setPersonalRating(value);
                    dialog.dismiss();
                    rating.setText("");
                    viewModel.saveMovie("favorite", toFavorite);
                    toFavorite = null;
                }
            });

            this.notifyItemRemoved(position);
        });

        holder.shareMovie.setOnClickListener(v -> {
            Movie share = list.get(position);
            Intent myIntent = new Intent(Intent.ACTION_SEND);
            myIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{});
            myIntent.setType("text/plain");
            String msg = context.getResources().getString(R.string.share_message);
            String body = share.getName() + msg;
            String sub = "Movie from MyMovieDB";
            myIntent.putExtra(Intent.EXTRA_SUBJECT, sub);
            myIntent.putExtra(Intent.EXTRA_TEXT, body);
            context.startActivity(Intent.createChooser(myIntent, "Share using"));
        });

        holder.editMovie.setOnClickListener(v -> {
            dialog.show();

            submit.setOnClickListener(v1 -> {
                if (rating.getText().toString().equals("")) {
                    Toast.makeText(context, "Please rate this movie", Toast.LENGTH_SHORT).show();
                } else {
                    double value = Double.parseDouble(rating.getText().toString());
                    dialog.dismiss();
                    rating.setText("");
                    list.get(position).setPersonalRating(value);
                    viewModel.editMoviePersonalRating(id, list.get(position).getId(), value);
                    notifyDataSetChanged();
                }
            });
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name;
        TextView rating;
        TextView personalRating;
        ImageView image;
        ImageView imageAdd;
        ImageView shareMovie;
        ImageView editMovie;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.nameSelectedListItem);
            rating = itemView.findViewById(R.id.ratingSelectedListItem);
            personalRating = itemView.findViewById(R.id.personalRatingSelectedList);
            image = itemView.findViewById(R.id.imageSelectedListItem);
            imageAdd = itemView.findViewById(R.id.addToFavorite);
            shareMovie = itemView.findViewById(R.id.shareMovie);
            editMovie = itemView.findViewById(R.id.editMovie);

            if (id.equals("favorite")) {
                ViewGroup.LayoutParams paramsForAdd = new LinearLayout.LayoutParams(1, 1, 1f);
                imageAdd.setLayoutParams(paramsForAdd);
                imageAdd.setVisibility(View.INVISIBLE);
            }

            if (id.equals("watch_later")) {
                editMovie.setVisibility(View.INVISIBLE);
            }

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mOnListItemClickListener.onListItemClick(getAdapterPosition());
        }
    }
}