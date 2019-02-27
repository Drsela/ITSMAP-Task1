package com.ITSMAP.movielist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.ITSMAP.movielist.DTO.Movie;

import java.io.Serializable;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    private List<com.ITSMAP.movielist.DTO.Movie>  movieList;private AdapterView.OnItemClickListener listener;
    private Context context;
    public MovieAdapter(List<com.ITSMAP.movielist.DTO.Movie> movies, Context context){
        movieList = movies;
        this.context = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.movie_item, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Movie currentMovie = movieList.get(i);
        viewHolder.movieName.setText(currentMovie.getName());
        viewHolder.movieRating.setText(currentMovie.getiMDBRating());
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView movieName;
        public TextView movieRating;
        public TextView movieUserRating;
        public TextView movieSeenStatus;

        public ViewHolder(
        View itemView) {
            super(itemView);

            movieName = itemView.findViewById(R.id.list_item_title);
            movieRating = itemView.findViewById(R.id.list_item_rating);
            movieUserRating = itemView.findViewById(R.id.list_item_userRating);
            movieSeenStatus = itemView.findViewById(R.id.list_item_viewStatus);

            itemView.setOnClickListener(v -> {
                // TODO: 27-02-2019 Create intent to new window
                //Toast.makeText(itemView.getContext() ,String.valueOf(movieList.get(getAdapterPosition()).getName()), Toast.LENGTH_SHORT).show();

                Movie clickedMovie = movieList.get(getAdapterPosition());
                Intent detailsIntent = new Intent(context, Details.class);

                detailsIntent.putExtra("MOVIE",clickedMovie);
                context.startActivity(detailsIntent);

            });

        }

    }
}
