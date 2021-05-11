package com.example.myandroidapplication.ui.movies;

import android.content.Context;
import android.util.Log;
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

import java.util.ArrayList;

public class SelectedListAdapter extends RecyclerView.Adapter<SelectedListAdapter.ViewHolder> {

    private ArrayList<Movie> list;
    private Context context;
    // private Context context;
    final private SelectedListAdapter.OnListItemClickListener mOnListItemClickListener;

    public SelectedListAdapter(ArrayList<Movie> list, Context context, SelectedListAdapter.OnListItemClickListener mOnListItemClickListener) {
        this.list = list;
        this.context = context;
        this.mOnListItemClickListener = mOnListItemClickListener;
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
        holder.name.setText(list.get(position).getName());
        holder.rating.setText("TMDB Rating: " + list.get(position).getRating());
        Glide.with(context)
                .load("https://image.tmdb.org/t/p/w500" + list.get(position).getPosterImage())
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView name;
        TextView rating;
        ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.nameSelectedListItem);
            rating = itemView.findViewById(R.id.ratingSelectedListItem);
            image = itemView.findViewById(R.id.imageSelectedListItem);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mOnListItemClickListener.onListItemClick(getAdapterPosition());
        }
    }
}
