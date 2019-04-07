package com.ITSMAP.movielist.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ITSMAP.movielist.GUI.DetailsActivity;
import com.ITSMAP.movielist.GUI.EditActivity;
import com.ITSMAP.movielist.R;
import com.bumptech.glide.Glide;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    private List<com.ITSMAP.movielist.JSONResponse.Movie> movieList;
    private Context context;


    public MovieAdapter(List<com.ITSMAP.movielist.JSONResponse.Movie> movies, Context context) {
        movieList = movies;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.movie_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(contactView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
            com.ITSMAP.movielist.JSONResponse.Movie currentMovie = movieList.get(i);
            viewHolder.movieName.setText(currentMovie.getTitle());
            viewHolder.movieRating.setText(currentMovie.getImdbRating());
            viewHolder.movieSeenStatus.setText(currentMovie.isWatched() ? context.getString(R.string.movie_status_watched) : context.getString(R.string.movie_status_unwatched));
            viewHolder.movieUserRating.setText(currentMovie.getPersonalRating() != null ? currentMovie.getPersonalRating() : "");
            // Get Poster
            Glide
                    .with(context)
                    .load(currentMovie.getPoster())
                    .into(viewHolder.poster);
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView movieName;
        TextView movieRating;
        TextView movieUserRating;
        TextView movieSeenStatus;
        ImageView poster;

        public ViewHolder(
                View itemView) {
            super(itemView);

            movieName = itemView.findViewById(R.id.search_title);
            movieRating = itemView.findViewById(R.id.list_item_rating);
            movieUserRating = itemView.findViewById(R.id.list_item_userRating);
            movieSeenStatus = itemView.findViewById(R.id.list_item_viewStatus);
            poster = itemView.findViewById(R.id.imageView);

            itemView.setOnClickListener(v -> {
                Intent detailsIntent = new Intent(context, DetailsActivity.class);
                detailsIntent.putExtra("MOVIE_DB_ID",movieList.get(getAdapterPosition()).getId());
                context.startActivity(detailsIntent);
            });

            itemView.setOnLongClickListener(v -> {
                Intent editIntent = new Intent(context, EditActivity.class);
                editIntent.putExtra("MOVIE_DB_ID",movieList.get(getAdapterPosition()).getId());
                context.startActivity(editIntent);
                return true;
            });
        }
    }
}
