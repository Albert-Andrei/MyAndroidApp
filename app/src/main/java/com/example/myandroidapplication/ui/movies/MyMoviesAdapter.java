package com.example.myandroidapplication.ui.movies;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myandroidapplication.Model.MovieList;
import com.example.myandroidapplication.R;

import java.util.ArrayList;


public class MyMoviesAdapter extends RecyclerView.Adapter<MyMoviesAdapter.ViewHolder>{

    private ArrayList<MovieList> lists;
    // private Context context;
    final private OnListItemClickListener mOnListItemClickListener;

    public MyMoviesAdapter(ArrayList<MovieList> lists, OnListItemClickListener mOnListItemClickListener) {
        this.lists = lists;
        this.mOnListItemClickListener = mOnListItemClickListener;
    }

    public interface OnListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.my_movie_item, parent, false);
        return new MyMoviesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(lists.get(position).getName());
        String m = holder.size.getText().toString();
        holder.size.setText(lists.get(position).getSize() + " " + m);
        holder.image.setImageResource(lists.get(position).getImageId());
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView name;
        ImageView image;
        TextView size;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.listName);
            size = itemView.findViewById(R.id.listSize);
            image = itemView.findViewById(R.id.imageViewListItem);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mOnListItemClickListener.onListItemClick(getAdapterPosition());
        }
    }
}
