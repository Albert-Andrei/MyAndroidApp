package com.example.myandroidapplication.ui.movies;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
    private  Movie toFavorite;
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
                double value = Double.parseDouble(rating.getText().toString());
                toFavorite.setPersonalRating(value);
                dialog.dismiss();
                viewModel.saveMovie("favorite", toFavorite);
                toFavorite = null;
            });

            this.notifyItemRemoved(position);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView name;
        TextView rating;
        TextView personalRating;
        ImageView image;
        ImageView imageAdd;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.nameSelectedListItem);
            rating = itemView.findViewById(R.id.ratingSelectedListItem);
            personalRating = itemView.findViewById(R.id.personalRating);
            image = itemView.findViewById(R.id.imageSelectedListItem);
            imageAdd = itemView.findViewById(R.id.addToFavorite);

            if (id.equals("favorite")) {
                imageAdd.setVisibility(View.INVISIBLE);
            }

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mOnListItemClickListener.onListItemClick(getAdapterPosition());
        }
    }
}
