package com.example.myandroidapplication.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myandroidapplication.Model.movie.Movie;
import com.example.myandroidapplication.R;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    private List<Movie> movies;
    private Context context;
    final private OnListItemClickListener mOnListItemClickListener;

    public MovieAdapter(List<Movie> movies, Context context, OnListItemClickListener listener) {
        this.movies = movies;
        this.context = context;
        this.mOnListItemClickListener = listener;
    }

    public interface OnListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    @NonNull
    @Override
    public MovieAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.movie_item, parent, false);
        return new MovieAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapter.ViewHolder holder, int position) {
        holder.name.setText(movies.get(position).getName());
        holder.rating.setText(movies.get(position).getRating());
        holder.overview.setText(movies.get(position).getOverview());
        // Using Glide library to display the images
        Glide.with(context)
                .load("https://image.tmdb.org/t/p/w500" + movies.get(position).getPosterImage())
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView name;
        TextView rating;
        ImageView image;
        TextView overview;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.m_name);
            image = itemView.findViewById(R.id.movie_image_v);
            rating = itemView.findViewById(R.id.m_rating);
            overview = itemView.findViewById(R.id.m_overview);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mOnListItemClickListener.onListItemClick(getAdapterPosition());
        }
    }
}
