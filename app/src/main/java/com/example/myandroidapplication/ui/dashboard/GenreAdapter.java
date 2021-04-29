package com.example.myandroidapplication.ui.dashboard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myandroidapplication.Model.genre.Genre;
import com.example.myandroidapplication.R;

import java.util.List;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.ViewHolder>{

    private List<Genre> genres;
    final private OnListItemClickListener mOnListItemClickListener;


    public GenreAdapter(List<Genre> genres, OnListItemClickListener mOnListItemClickListener) {
        this.genres = genres;
        this.mOnListItemClickListener = mOnListItemClickListener;
    }

    public interface OnListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.genre_item, parent, false);
        return new GenreAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(genres.get(position).getName());
//        holder.size.setText("Size: " + "0");
        holder.image.setImageResource(genres.get(position).getImageId());
    }

    @Override
    public int getItemCount() {
        return genres.size();
    }


        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView name;
        TextView size;
        ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.genreName);
//            size = itemView.findViewById(R.id.genreSize);
            image = itemView.findViewById(R.id.genreImage);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mOnListItemClickListener.onListItemClick(getAdapterPosition());
        }
    }
}
