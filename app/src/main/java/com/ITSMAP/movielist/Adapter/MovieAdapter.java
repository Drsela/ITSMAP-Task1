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
import com.ITSMAP.movielist.drawableGenerator;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    public static final String ADAPTER_POSITION = "ADAPTER POSITION";
    private List<com.ITSMAP.movielist.JSONResponse.Movie> movieList;
    private Context context;
    private static final Integer MOVIE_FROM_ADAPTER_CODE = 100;
    private com.ITSMAP.movielist.drawableGenerator drawableGenerator;
    public static String MOVIE_FROM_ADAPTER = "Adapter movie";

    public MovieAdapter(List<com.ITSMAP.movielist.JSONResponse.Movie> movies, Context context) {
        movieList = movies;
        this.context = context;
        drawableGenerator = new drawableGenerator(context);
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

            // Get Poster


            //viewHolder.movieSeenStatus.append(currentMovie.hasBeenWatched() ? context.getResources().getString(R.string.edit_movie_watched_status) : context.getResources().getString(R.string.movie_not_seen));
            //viewHolder.movieUserRating.setText(currentMovie.hasUserRating() ? currentMovie.getUserRating() : null);
            //viewHolder.poster.setImageDrawable(drawableGenerator.getDrawableByGenre(currentMovie));
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
